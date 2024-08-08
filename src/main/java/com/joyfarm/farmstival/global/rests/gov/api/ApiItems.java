package com.joyfarm.farmstival.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //값이 없는 것 배제->null값으로 대체
public class ApiItems {
    private List<ApiItem> item;
}
