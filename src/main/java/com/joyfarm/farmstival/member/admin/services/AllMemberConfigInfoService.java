package com.joyfarm.farmstival.member.admin.services;

import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.joyfarm.farmstival.member.MemberInfo;
import com.joyfarm.farmstival.member.constants.Authority;
import com.joyfarm.farmstival.member.controllers.MemberSearch;
import com.joyfarm.farmstival.member.entities.Authorities;
import com.joyfarm.farmstival.member.entities.Member;
import com.joyfarm.farmstival.member.entities.QMember;
import com.joyfarm.farmstival.member.repositories.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllMemberConfigInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final HttpServletRequest request;

    /* 회원 정보가 필요할때마다 호출되는 메서드 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//유저의 정보를 불러와서 UserDetails로 리턴

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); //회원 없을 경우 예외 발생

        /*
        MemberInfo쪽에 getAuthorities()메서드를 통해서 사용자 권한 조회,
        권한이 null이거나 비어있을 때 대체 할 기본권한 -> USER, null이 아닌 경우 기존 권한 그대로 반환
         */
        List<Authorities> tmp = member.getAuthorities();
        if(tmp == null || tmp.isEmpty()){
            tmp = List.of(Authorities.builder().member(member).authority(Authority.USER).build());
        }

        /*
        tmp에서 가져온 Authorities 객체 리스트를 Spring Security가 이해할 수 있는 SimpleGrantedAuthority 객체 리스트로 변환하는 가공 단계가 필요하다.
         */
        List<SimpleGrantedAuthority> authorities = tmp.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
                .toList();//Authority enum의 name 메서드를 호출하여 문자열로 변환해야한다.(authority는 enum상수로 되어있기 때문!)

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
    /**
     * 회원 목록
     *
     * @param search
     * @return
     */
    public ListData<Member> getList(MemberSearch search) {

        int page = Math.max(search.getPage(), 1); // 페이지 번호
        int limit = Math.max(search.getLimit(), 20); // 1페이지당 레코드 갯수
        int offset = (page - 1) * limit; // 레코드 시작 위치 번호

        BooleanBuilder andBuilder = new BooleanBuilder();
        QMember member = QMember.member;

        PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, "member");

        List<Member> items = new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")))
                .fetch();

        /* 페이징 처리 S */
        int total = (int)memberRepository.count(andBuilder); // 총 레코드 갯수

        Pagination pagination = new Pagination(page, total, 10, limit, request);
        /* 페이징 처리 E */

        return new ListData<>(items, pagination);
    }
}
