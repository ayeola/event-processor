package com.saltech.event_processor.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Customer {

    private String referenceId;
    private String nin;
    private String bvn;
    private String msisdn;
    private String firstName;
    private String lastName;
    private String dob;
    private String address;
    private String email;
    private String gender;
    private String status;



}
