//package com.saltech.event_processor.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class KafkaMessageListener {
//
//
//    @KafkaListener(topics = "create_event_topic",groupId = "customer_group")
//    public void messageLister(String message){
//        log.info("Message:{}",message);
//    }
//}
