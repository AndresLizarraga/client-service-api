package com.pinapp.clientservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerMetricsDTO {

    @JsonProperty(defaultValue = "0")
    private Double ageAvg;
    @JsonProperty(defaultValue = "0")
    private Double ageStandardDeviation;
}
