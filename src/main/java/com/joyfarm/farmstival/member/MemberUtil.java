package com.joyfarm.farmstival.member;


import com.joyfarm.farmstival.member.constants.Authority;
import com.joyfarm.farmstival.member.entities.Authorities;
import com.joyfarm.farmstival.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberUtil {

    public boolean isLogin() {
        return getMember() != null;
    }

    public boolean isAdmin() {
        if (isLogin()) {
            Member member = getMember();
            List<Authorities> authorities = member.getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority() == Authority.ADMIN);
        }

        return false;
    }

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) {
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();

            return memberInfo.getMember();
        }

        return null;
    }
}