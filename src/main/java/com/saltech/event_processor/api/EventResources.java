package com.saltech.event_processor.api;

import com.saltech.event_processor.constants.UriConstant;
import com.saltech.event_processor.model.Customer;
import com.saltech.event_processor.producer.EventProducer;
import com.saltech.event_processor.response.ApiResponse;
import com.saltech.event_processor.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/event")
public class EventResources {

    @Autowired
    private CustomerService customerService;


    @PostMapping(path = UriConstant.create_customer, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody Customer customer) {
        log.info("FinchBillerRequest:{}", customer);
        return customerService.onboarding(customer);
    }
}
