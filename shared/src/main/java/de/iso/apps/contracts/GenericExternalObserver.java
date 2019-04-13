package de.iso.apps.contracts;

import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.StreamSupport;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

public abstract class GenericExternalObserver<TKey, TValue> implements ExternalObserver<TKey, TValue> {
    private ConcurrentSkipListSet<ExternalObservailable<TKey, TValue>> list = new ConcurrentSkipListSet<>();
    
    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                            .filter(header -> header.key()
                                                    .equals("__TypeId__"))
                            .findFirst()
                            .map(header -> new String(header.value()))
                            .orElse("N/A");
    }
    
    protected Set<ExternalObservailable<TKey, TValue>> getList() {
        return list.clone();
    }
    
    protected void logEntree(ConsumerRecord<String, MailChangingEventArgs> cr,
                             MailChangingEventArgs payload) {
        log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}",
                 cr.key(),
                 typeIdHeader(cr.headers()),
                 payload,
                 cr.toString());
    }
    
    @Async
    @Override
    public void add(@NonNull ExternalObservailable<TKey, TValue> observailable) {
        list.add(observailable);
    }
    
    public abstract void listenAsObject(ConsumerRecord<TKey, TValue> cr,
                                        @Payload
                                                TValue payload);
}
