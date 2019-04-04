package de.iso.apps.contracts;

import org.springframework.kafka.core.KafkaTemplate;

public interface Topicable<T> {
    String getTopic();
    
    
    KafkaTemplate<String, T> getKafkaTemplate();
}
