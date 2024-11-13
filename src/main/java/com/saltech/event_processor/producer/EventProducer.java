package com.saltech.event_processor.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saltech.event_processor.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@SuppressWarnings("Unchecked")
public class EventProducer {

    private KafkaTemplate<String, String> kafkaTemplate;
    private String createEvent;


    public EventProducer(KafkaTemplate<String, String> kafkaTemplate,
                         @Value("${create.event.topic}") String createEvent) {
        this.kafkaTemplate = kafkaTemplate;
        this.createEvent = createEvent;
    }

    public void send(Customer customer) {

        log.info("KafkaEvent:{}", customer);
        Gson gson = new GsonBuilder().create();
        String message = gson.toJson(customer);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate
                .send(createEvent, message);

//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                log.info("Message [{}] delivered with offset {}",
//                        message,
//                        result.getRecordMetadata().offset());
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.warn("Unable to deliver message [{}]. {}",
//                        message,
//                        ex.getMessage());
//            }
//        });


    }

}
