package com.joyfarm.farmstival.member.admin.services;

import com.joyfarm.farmstival.member.admin.controllers.RequestMember;
import com.joyfarm.farmstival.member.admin.repositories.AuthoritiesRepository;
import com.joyfarm.farmstival.member.constants.Authority;
import com.joyfarm.farmstival.member.entities.Authorities;
import com.joyfarm.farmstival.member.entities.Member;
import com.joyfarm.farmstival.member.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberEditService {

    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;

    public void editMember(RequestMember form){

        Long seq = form.getSeq();
        Member member = memberRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(form.getEmail()));

        String userName = form.getUserName();
        String email = form.getEmail();
        String mobile = form.getMobile();

        List<String> strAuthorities = form.getAuthorities();

        List<Authorities> authoList = new ArrayList<>();

        for(String autho : strAuthorities){
            Authorities authorities = new Authorities();
            authorities.setMember(member);
            authorities.setAuthority(Authority.valueOf(autho));

            authoList.add(authorities);
            authoritiesRepository.saveAndFlush(authorities);
        }

        member.setUserName(userName);
        member.setEmail(email);
        member.setMobile(mobile);
        member.setAuthorities(authoList);

        memberRepository.saveAndFlush(member);
    }
}
