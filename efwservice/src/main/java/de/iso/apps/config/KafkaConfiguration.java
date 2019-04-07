package de.iso.apps.config;

import de.iso.apps.contracts.ExternalObservailable;
import de.iso.apps.contracts.ExternalObserver;
import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import lombok.NonNull;
import lombok.var;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.Headers;
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
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

@Configuration @EnableKafka public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;
    @Value("${kafka-topics.employee.name}") private String name;
    private static final Logger log = LoggerFactory.getLogger(KafkaConfiguration.class);
    
    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }
    
    @Bean("externalObserver")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    ExternalObserver<String, MailChangingDTO> externalObserver() {
        return new MailChangingObserver();
    }
    
    @Bean
    public Topicable<MailChangingDTO> userProducer() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        log.info("Producerconfig: {}", props.toString());
        var factory = new DefaultKafkaProducerFactory<String, MailChangingDTO>(props);
        var template = new KafkaTemplate<String, MailChangingDTO>(factory);
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
    public ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> kafkaListenerContainerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        log.info("Consumerconfig: {}", props.toString());
        JsonDeserializer<MailChangingDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        DefaultKafkaConsumerFactory<String, MailChangingDTO> consumerFactory = new DefaultKafkaConsumerFactory<>(props,
                                                                                                                 new StringDeserializer(),
                                                                                                                 jsonDeserializer);
        ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
    
    private static class MailChangingObserver implements ExternalObserver<String, MailChangingDTO> {
        private ConcurrentSkipListSet<ExternalObservailable<String, MailChangingDTO>> list = new ConcurrentSkipListSet<>();
        
        @Override
        public void add(@NonNull ExternalObservailable<String, MailChangingDTO> observailable) {
            list.add(observailable);
        }
        
        @KafkaListener(topics = "${kafka-topics.user.name}",
                       clientIdPrefix = "efwservice",
                       containerFactory = "mailChangingListenerContainerFactory",
                       groupId = "efwservice",
                       id = "usermailchanged",
                       errorHandler = "errorHandler")
        public void listenAsObject(ConsumerRecord<String, MailChangingDTO> cr, @Payload MailChangingDTO payload) {
            
            
            for (ExternalObservailable<String, MailChangingDTO> item : list) {
                try {
                    item.valueChanged(cr.key(), payload);
                } catch (Exception e) {
                    log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}",
                    
                             cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
                    log.error("Error in Listener", e);
                }
            }
        }
        
        private static String typeIdHeader(Headers headers) {
            return StreamSupport.stream(headers.spliterator(), false)
                                .filter(header -> header.key()
                                                        .equals("__TypeId__"))
                                .findFirst()
                                .map(header -> new String(header.value()))
                                .orElse("N/A");
        }
    }
    
    private static class TopicableMailChangingImpl implements Topicable<MailChangingDTO> {
        private final KafkaTemplate<String, MailChangingDTO> kafkaTemplate;
        private final NewTopic newTopic;
        private static final Logger log = LoggerFactory.getLogger(TopicableMailChangingImpl.class);
    
        TopicableMailChangingImpl(KafkaTemplate<String, MailChangingDTO> kafkaTemplate, NewTopic newTopic) {
            this.kafkaTemplate = kafkaTemplate;
            this.newTopic = newTopic;
        }
        
        
        @Override
        public void send(MailChangingDTO mailChangingDTO,
                         Consumer<SendResult<String, MailChangingDTO>> sendResultFunc,
                         Consumer<Throwable> errorResult) {
            var result = kafkaTemplate.send(newTopic.name(), mailChangingDTO);
            result.addCallback(sendResultFunc::accept, errorResult::accept);
        }
        
        @Override
        public void send(MailChangingDTO mailChangingDTO) {
            send(mailChangingDTO, (item) -> {
            }, ex -> log.error(ex.toString()));
        }
        
        @Override
        public void send(MailChangingDTO mailChangingDTO, Consumer<Throwable> errorResult) {
            send(mailChangingDTO, item -> {
            }, errorResult);
        }
        
    }
}
