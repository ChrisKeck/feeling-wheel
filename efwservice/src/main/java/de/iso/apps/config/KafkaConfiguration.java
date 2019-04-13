package de.iso.apps.config;

import de.iso.apps.contracts.ExternalObservailable;
import de.iso.apps.contracts.ExternalObserver;
import de.iso.apps.contracts.GenericExternalObserver;
import de.iso.apps.contracts.GenericTopicDistributor;
import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.contracts.TopicDistributor;
import lombok.var;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.Map;

@Configuration @EnableKafka public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;
    private static final String employeeIdent = "${kafka-topics.employee.name}";
    private static final String userIdent = "${kafka-topics.user.name}";
    @Value(employeeIdent)
    private String name;
    private static final Logger log = LoggerFactory.getLogger(KafkaConfiguration.class);
    
    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }
    
    @Bean("externalObserver")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ExternalObserver<String, MailChangingEventArgs> externalObserver() {
        return new MailChangingObserver();
    }
    
    @Bean
    public TopicDistributor<MailChangingEventArgs> userProducer() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        log.info("Producerconfig: {}", props.toString());
        var factory = new DefaultKafkaProducerFactory<String, MailChangingEventArgs>(props);
        var template = new KafkaTemplate<String, MailChangingEventArgs>(factory);
        return new TopicableMailChangingImpl(template, new NewTopic(name, 1, (short) 1));
    }
    
    //
    //
    // Consumer configuration
    //
    // If you only need one kind of deserialization, you only need to set the
    // Consumer configuration properties. Uncomment this and remove all others below.
    @Bean("errorHandler")
    public KafkaListenerErrorHandler errorHandler() {
        return (message, e) -> {
            log.error(message.toString(), e);
            return message;
        };
    }
    
    @Bean("mailChangingListenerContainerFactory")
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, MailChangingEventArgs> kafkaListenerContainerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        log.info("Consumerconfig: {}", props.toString());
        JsonDeserializer<MailChangingEventArgs> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        DefaultKafkaConsumerFactory<String, MailChangingEventArgs> consumerFactory = new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer);
        ConcurrentKafkaListenerContainerFactory<String, MailChangingEventArgs> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
    
    private static class MailChangingObserver extends GenericExternalObserver<String, MailChangingEventArgs> {
        
        @Override
        @KafkaListener(topics = userIdent,
                       containerFactory = "mailChangingListenerContainerFactory",
                       clientIdPrefix = "employee",
                       groupId = "efw",
                       errorHandler = "errorHandler")
        public void listenAsObject(ConsumerRecord<String, MailChangingEventArgs> cr,
                                   @Payload
                                           MailChangingEventArgs payload) {
            logEntree(cr, payload);
            for (ExternalObservailable<String, MailChangingEventArgs> item : getList()) {
                try {
                    item.valueChanged(cr.key(), payload);
                } catch (Exception e) {
                    log.error("Error in Listener", e);
                }
            }
        }
        
    }
    
    private static class TopicableMailChangingImpl extends GenericTopicDistributor<MailChangingEventArgs> {
        
        private static final Logger log = LoggerFactory.getLogger(TopicableMailChangingImpl.class);
        
        public TopicableMailChangingImpl(KafkaTemplate<String, MailChangingEventArgs> kafkaTemplate,
                                         NewTopic newTopic) {
            super(kafkaTemplate, newTopic);
        }
        @Override
        protected void onSuccess(NewTopic newTopic) {
            log.info("Sending a kafka notification for {} succeeded",
                     newTopic.name());
        }
        
        @Override
        protected void onError(NewTopic newTopic, Throwable ex) {
            log.error("Sending a kafka notification  failed for {}: {}",
                      newTopic,
                      ex.toString());
        }
    }
}
