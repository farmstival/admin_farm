package com.joyfarm.farmstival.tour.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTest2 {

    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("휴양 마을 api 데이터")
    void test01() throws Exception {
        File file = new File("D:/data/farmData.json");
        Map<String, Object> data = om.readValue(file, new TypeReference<>() {});
        System.out.println(data);
    }
}
