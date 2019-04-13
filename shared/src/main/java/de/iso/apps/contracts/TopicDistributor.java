package de.iso.apps.contracts;

import org.springframework.kafka.support.SendResult;

import java.util.function.Consumer;

public interface TopicDistributor<T> {
    
    
    void send(T value,
              Consumer<SendResult<String, T>> sendResultFunc,
              Consumer<Throwable> errorResult);
    
    void send(T value);
    
    void send(T value,
              Consumer<Throwable> errorResult);
}

