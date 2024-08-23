package com.joyfarm.farmstival.member.admin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthoritiesDeleteService {

//    private final AuthoritiesRepository repository;
//    private final MemberUtil memberUtil;
//
//    /**
//     * 특정 authorities 삭제
//     * @param seq : authorities의 시퀀스
//     */
//    public void delete(Long seq){
//        Authorities authorities = repository.findById(memberUtil.getMember().getEmail());
//        if(authorities != null){
//            repository.delete(authorities);
//            repository.flush();
//        }
//
//    }


//    /**
//     * 특정 멤버의 authorities 전체 삭제
//     * @param seq : 멤버 시퀀스
//     */
//    public void deleteAuthList(Long seq){
//        List<Authorities> authorities = (List<Authorities>) repository.findById(memberUtil.getMember().getEmail());
//        for(Authorities autho : authorities){
//            delete(autho.getMember().getSeq());
//        }
//    }

}
