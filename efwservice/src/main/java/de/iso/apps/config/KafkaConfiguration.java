package de.iso.apps.config;

import de.iso.apps.service.dto.MailChangingDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration public class KafkaConfiguration {
    @Autowired private KafkaProperties kafkaProperties;
    @Value("${tpd.topic-name}") private String topicName;
    // Producer configuration
    // omitted...
    
    // Consumer configuration
    
    // If you only need one kind of deserialization, you only need to set the
    // Consumer configuration properties. Uncomment this and remove all others below.
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tpd-loggers");
        
        return props;
    }
    
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
}
