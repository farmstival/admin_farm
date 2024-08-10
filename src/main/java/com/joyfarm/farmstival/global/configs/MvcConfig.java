package com.joyfarm.farmstival.global.configs;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
@EnableDiscoveryClient
public class MvcConfig implements WebMvcConfigurer {


    /**
     * <input type="hidden" name="_method" value="PATCH">  -> PATCH 방식으로 요청
     * ?_method=DELETE
     * @return
     */
    /*
    * HTML FORM에서 전송된 GET,POST타입 빼고 나머지 타입은 FORM에서 지원하지 않기 때문에 기존에는 ajax로 타입을 명시하여 전달 후 받을 수 있었다.
     -> HiddenHttpMethodFilter를 사용하면 기존 FORM방식(GET,POST)으로 전송하고 서버에서는 원하는 응답 데이터 타입으로 받을 수 있음
    * */
    @Bean
    public HiddenHttpMethodFilter httpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
