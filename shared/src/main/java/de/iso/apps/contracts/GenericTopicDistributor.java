package de.iso.apps.contracts;

import lombok.var;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

public abstract class GenericTopicDistributor<T> implements TopicDistributor<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericTopicDistributor.class);
    protected final KafkaTemplate<String, T> kafkaTemplate;
    private final NewTopic newTopic;
    
    public GenericTopicDistributor(
            KafkaTemplate<String, T> kafkaTemplate, NewTopic newTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.newTopic = newTopic;
    }
    
    @Async
    @Override
    public void send(T value,
                     Consumer<SendResult<String, T>> sendResultFunc,
                     Consumer<Throwable> errorResult) {
        if (newTopic.name().isEmpty()) {
            log.warn("no topic given");
            return;
        }
        sending(value, sendResultFunc, errorResult);
    }
    
    private void sending(T value,
                         Consumer<SendResult<String, T>> sendResultFunc,
                         Consumer<Throwable> errorResult) {
        log.info("Sending a kafka notification for {} because email changed!", newTopic.name());
        var result = kafkaTemplate.send(newTopic.name(), value);
        result.addCallback(sendResultFunc::accept, errorResult::accept);
    }
    
    @Async
    @Override
    public void send(T value) {
        send(value,
             ex -> {
                 onError(newTopic, ex);
             });
    }
    
    @Async
    @Override
    public void send(T value,
                     Consumer<Throwable> errorResult) {
        send(value,
             item -> {
                 onSuccess(newTopic);
             },
             errorResult);
    }
    
    protected abstract void onSuccess(NewTopic newTopic);
    
    protected abstract void onError(NewTopic newTopic, Throwable ex);
}
