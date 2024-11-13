package com.saltech.event_processor.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

  @JsonProperty("StatusMessage")
  private String statusMassage;
  @JsonProperty("Status")
  private String status;
}
