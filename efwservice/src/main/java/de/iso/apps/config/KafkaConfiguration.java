package de.iso.apps.config;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration @EnableKafka public class KafkaConfiguration {
    
    private final KafkaProperties kafkaProperties;
    
    @Value("${employee.topic-name}") private String employeeTopicName;
    
    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }
    
    @Bean
    public KafkaTemplate<String, MailChangingDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    //
//
//
//Producer
//
//
//
    @Bean
    public ProducerFactory<String, MailChangingDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        return props;
    }
    
    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(employeeTopicName, 1, (short) 1);
    }
    
    @Bean
    public Topicable topicableMailChanging() {
        return new TopicableMailChangingImpl();
    }
    
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
    
    
    //
    //
    // Consumer configuration
    //
    // If you only need one kind of deserialization, you only need to set the
    // Consumer configuration properties. Uncomment this and remove all others below.
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MailChangingDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        
        return factory;
    }
    
    @Bean
    public ConsumerFactory<String, MailChangingDTO> consumerFactory() {
        JsonDeserializer<MailChangingDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(),
                                                 new StringDeserializer(),
                                                 jsonDeserializer);
    }
    
    public class TopicableMailChangingImpl implements Topicable<MailChangingDTO> {
        
        
        @Override
        public String getTopic() {
            return employeeTopicName;
        }
        
        @Override
        public KafkaTemplate<String, MailChangingDTO> getKafkaTemplate() {
            return kafkaTemplate();
        }
    }
}
