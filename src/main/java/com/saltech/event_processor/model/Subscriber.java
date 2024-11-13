package com.saltech.event_processor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class Subscriber {

    private String referenceId;
    private String nin;
    private String bvn;
    private String msisdn;
    private String firstName;
    private String lastName;
    private Date dob;
    private String address;
    private String email;
    private String gender;
    private String status;
    private Date createdDate;
    private String createdBy;
}
