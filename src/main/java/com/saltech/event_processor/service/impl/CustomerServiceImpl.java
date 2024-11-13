package com.saltech.event_processor.service.impl;

import com.saltech.event_processor.model.Customer;
import com.saltech.event_processor.producer.EventProducer;
import com.saltech.event_processor.response.ApiResponse;
import com.saltech.event_processor.response.BaseResponse;
import com.saltech.event_processor.response.ResponseBuilder;
import com.saltech.event_processor.response.ResponseConstants;
import com.saltech.event_processor.service.CustomerService;
import com.saltech.event_processor.service.HttpHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceImpl extends HttpHeader implements CustomerService {

    private ResponseBuilder responseBuilder;
    private EventProducer eventProducer;

    public CustomerServiceImpl(EventProducer eventProducer,
                               ResponseBuilder responseBuilder){
        this.responseBuilder = responseBuilder;
        this.eventProducer = eventProducer;
    }


    @Override
    public ResponseEntity<ApiResponse> onboarding(Customer customer) {
        try{
            eventProducer.send(customer);
            BaseResponse response = new BaseResponse();
            response.setStatusMassage("Success");
            response.setStatus("00");
            return responseBuilder.buildResponse(getHeader(),
                    HttpStatus.OK.value(), ResponseConstants.HTTP_DEFAULT_RESPONSE_MESSAGE, response);
        }catch (Exception ex){
            return responseBuilder.buildResponse(getHeader(),
                    HttpStatus.BAD_REQUEST.value(), ResponseConstants.FAILURE_MESSAGE, ex.getMessage());
        }

    }
}
