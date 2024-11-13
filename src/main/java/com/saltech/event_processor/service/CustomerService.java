package com.saltech.event_processor.service;

import com.saltech.event_processor.model.Customer;
import com.saltech.event_processor.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerService {


    ResponseEntity<ApiResponse> onboarding(Customer customer);
}
