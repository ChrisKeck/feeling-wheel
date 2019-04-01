package de.iso.apps.config;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka @Configuration public class KafkaConfiguration {
    @Autowired private KafkaProperties kafkaProperties;
    @Value("${tpd.topic-name}") private String topicName;
    @Value("${tpd.messages-per-request}") private int numberofRequests;
    @Value("${employee.topic-name}") private String employeeTopicName;
    @Value("${employee.messages-per-request}") private int employeeNumberofRequests;
    // Producer configuration
    
    @Bean
    public KafkaTemplate<String, MailChangingDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
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
        return new NewTopic(topicName, 3, (short) 1);
    }
    
    @Bean
    public Topicable topicableMailChanging() {
        return new TopicableMailChangingImpl();
    }
    
    public class TopicableMailChangingImpl implements Topicable<MailChangingDTO> {
        
        
        @Override
        public String getTopic() {
            return topicName;
        }
        
        @Override
        public int getRequestsPerMessage() {
            return numberofRequests;
        }
        
        @Override
        public KafkaTemplate<String, MailChangingDTO> getKafkaTemplate() {
            return kafkaTemplate();
        }
    }
    
}
