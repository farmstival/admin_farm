package com.joyfarm.farmstival.member.admin.controllers;

import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.joyfarm.farmstival.member.admin.services.AllMemberConfigInfoService;
import com.joyfarm.farmstival.member.controllers.MemberSearch;
import com.joyfarm.farmstival.member.entities.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class AdminController  {
    private AllMemberConfigInfoService memberConfigInfoService;
    private final HttpServletRequest request;

    @GetMapping
    public String index(@ModelAttribute MemberSearch search, Model model){
        ListData<Member> data = memberConfigInfoService.getList(search);
        List<Member> items = data.getItems();
        Pagination pagination = data.getPagination();

        model.addAttribute("items", items);
        model.addAttribute("pagination", pagination);

        return "member/admin";
    }
}
