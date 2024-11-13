package com.saltech.event_processor.batch;

import com.google.gson.Gson;
import com.saltech.event_processor.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class CustomItemReader implements ItemStreamReader<Customer> {

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }


}
