package com.joyfarm.farmstival.member.validators;

import com.joyfarm.farmstival.global.validators.MobileValidator;
import com.joyfarm.farmstival.member.admin.controllers.RequestMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberFormValidator implements Validator, MobileValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestMember.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        RequestMember form = (RequestMember) target;
        String mobile = form.getMobile();

        if (errors. hasErrors()) { // 커맨드 객체 검증 실패시에는 종료
            return;
        }

        if (!mobileCheck(mobile)) {
            errors.rejectValue("mobile", "Mobile");
        }

    }
}
