package org.egov.nationaldashboardingest.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import javax.validation.Valid;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IngestRequest {

    @Valid
    @JsonProperty("Data")
    private Data ingestData;

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}
