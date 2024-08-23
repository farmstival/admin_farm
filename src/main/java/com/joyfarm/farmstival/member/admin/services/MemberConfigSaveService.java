package com.joyfarm.farmstival.member.admin.services;


import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.exceptions.script.AlertException;
import com.joyfarm.farmstival.member.admin.controllers.RequestMember;
import com.joyfarm.farmstival.member.entities.Member;
import com.joyfarm.farmstival.member.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberConfigSaveService {

    private final MemberRepository memberRepository;
    private final AuthoritiesDeleteService authoritiesDeleteService;
    private final Utils utils;

    /* 특정 회원정보 폼에서 변경시 저장*/
    public void save(RequestMember form){
        String email = form.getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AlertException("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        member.setUserName(form.getUserName());
        member.setMobile(form.getMobile());

        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            member.setPassword(form.getPassword());
        }

        memberRepository.saveAndFlush(member);

    }

    public void saveList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("수정할 회원을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks) {
            String userEmail = utils.getParam("email_" + chk);

            Member member = memberRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new AlertException("해당 이메일의 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

            if(member == null) continue;
        }

        // 모든 수정된 사항을 데이터베이스에 반영
        memberRepository.flush();
    }
}
