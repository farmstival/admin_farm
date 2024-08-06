package com.joyfarm.farmstival.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final DiscoveryClient discoveryClient;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final DiscoveryClient discoveryClient;
//    private final HttpServletRequest request;

    public String url(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");

        return String.format("%s%s", instances.get(0).getUri().toString(), url);
    }
        /*
    public String redirectUrl(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");
        String fromGateway = request.getHeader("from-gateway");
        if (StringUtils.hasText("fromGateway") && fromGateway.equals("true")) {
            String host = request.getHeader("gateway-port");
            String protocol = request.isSecure() ? "https://" : "http://";
             url = protocol + host + "/admin" + url;
        }

        return "redirect:" + url;

    }

     */

    public String url(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");

        return String.format("%s%s", instances.get(0).getUri().toString(), url);
        // 각 서버에서 지원하는 정적 자원의 경로는 게이트웨이 쪽에서 접근하는 것이 바람직 하지 않다!
    }

    public String toUpper(String str) {
        return str.toUpperCase();
    }

    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // FieldErrors


        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes())));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }


    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }

    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[] {code});

        return messages.isEmpty() ? code : messages.get(0);
    }
}
