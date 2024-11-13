package com.saltech.event_processor.batch;


import com.google.gson.Gson;
import com.saltech.event_processor.enums.Status;
import com.saltech.event_processor.model.Customer;
import com.saltech.event_processor.model.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
@Slf4j
public class CustomerItemProcessor implements ItemProcessor<String,Subscriber>{



    @Override
    public Subscriber process(String item) throws Exception {
        log.info("Item:",item);
        Gson gson = new Gson();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

//        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
//        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Customer customer = gson.fromJson(item,Customer.class);
        Date dob = formatter.parse(customer.getDob());
        Subscriber subscriber =  Subscriber.builder().build();
        subscriber.setReferenceId(customer.getReferenceId());
        subscriber.setBvn(customer.getBvn());
        subscriber.setDob(dob);
        subscriber.setAddress(customer.getAddress());
        subscriber.setEmail(customer.getEmail());
        subscriber.setNin(customer.getNin());
        subscriber.setMsisdn(customer.getMsisdn());
        subscriber.setFirstName(customer.getFirstName());
        subscriber.setLastName(customer.getLastName());
        subscriber.setGender(customer.getGender());
        subscriber.setStatus(Status.APPROVED.name());
        subscriber.setCreatedBy("System");
        subscriber.setCreatedDate(new Date());
        return subscriber;
    }
}
