package com.joyfarm.farmstival.tour.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiUpdateServiceTest {

    @Autowired
    private ApiUpdateService updateService;
    
    @Test
    @DisplayName("스케줄링 확인")
    void test01() {
        updateService.update(); //요청 데이터가 많으면 인증키 이슈로 스케줄링 어려움->보류 
    }
}
