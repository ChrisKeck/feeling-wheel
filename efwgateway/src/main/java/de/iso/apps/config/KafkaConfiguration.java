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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;

@Configuration @EnableKafka public class KafkaConfiguration {
    private static final String employeeIdent = "${kafka-topics.employee.name}";
    private static final String userIdent = "${kafka-topics.user.name}";
    private final KafkaProperties kafkaProperties;
    @Value(userIdent)
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
    
    @Bean("userProducer")
    public TopicDistributor<MailChangingEventArgs> userProducer() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        log.info("Producerconfig: {}", props.toString());
        var factory = new DefaultKafkaProducerFactory<String, MailChangingEventArgs>(props);
        var template = new KafkaTemplate<String, MailChangingEventArgs>(factory);
        return new TopicMailDistributor(template, new NewTopic(name, 1, (short) 1));
    }
    
    //
    //
    // Consumer configuration
    //
    @Bean("listenerContainerFactory")
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
        @Async
        @KafkaListener(topics = employeeIdent,
                       clientIdPrefix = "gateway",
                       containerFactory = "listenerContainerFactory",
                       groupId = "efw")
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
    
    private static class TopicMailDistributor extends GenericTopicDistributor<MailChangingEventArgs> {
        
        TopicMailDistributor(KafkaTemplate<String, MailChangingEventArgs> kafkaTemplate, NewTopic newTopic) {
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
