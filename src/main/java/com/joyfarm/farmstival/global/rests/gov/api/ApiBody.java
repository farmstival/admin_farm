package com.joyfarm.farmstival.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //값이 없는 것 배제->null값으로 대체
public class ApiBody {
    private ApiItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
