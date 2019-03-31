package de.iso.apps.service.impl;

import de.iso.apps.config.Constants;
import de.iso.apps.service.dto.MailChangingDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

public class MailChangingListener {
    
    private final Logger log = LoggerFactory.getLogger(FeelWheelServiceImpl.class);
    private CountDownLatch latch = new CountDownLatch(1);
    
    @KafkaListener(topics = Constants.TOPIC_MAIL_CHANGING, clientIdPrefix = "json",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, MailChangingDTO> cr,
                               @Payload MailChangingDTO payload) {
        log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }
    
    @KafkaListener(topics = Constants.TOPIC_MAIL_CHANGING, clientIdPrefix = "string",
                   containerFactory = "kafkaListenerStringContainerFactory")
    public void listenasString(ConsumerRecord<String, String> cr,
                               @Payload String payload) {
        log.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }
    /*
    @KafkaListener(topics = "advice-topic", clientIdPrefix = "bytearray",
                   containerFactory = "kafkaListenerByteArrayContainerFactory")
    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr,
                                  @Payload byte[] payload) {
        log.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }*/
    
    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                            .filter(header -> header.key()
                                                    .equals("__TypeId__"))
                            .findFirst()
                            .map(header -> new String(header.value()))
                            .orElse("N/A");
    }
}
