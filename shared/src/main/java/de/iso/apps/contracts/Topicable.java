package de.iso.apps.contracts;

import org.springframework.kafka.support.SendResult;

import java.util.function.Consumer;

public interface Topicable<T> {
    
    
    void send(T mailChangingDTO, Consumer<SendResult<String, T>> sendResultFunc, Consumer<Throwable> errorResult);
    
    void send(T mailChangingDTO);
    
    void send(T mailChangingDTO, Consumer<Throwable> errorResult);
}

