package com.joyfarm.farmstival.board.controllers;


import com.joyfarm.farmstival.board.entities.Board;
import com.joyfarm.farmstival.board.services.BoardConfigDeleteService;
import com.joyfarm.farmstival.board.services.BoardConfigInfoService;
import com.joyfarm.farmstival.board.services.BoardConfigSaveService;
import com.joyfarm.farmstival.board.validators.BoardConfigValidator;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.exceptions.ExceptionProcessor;
import com.joyfarm.farmstival.menus.Menu;
import com.joyfarm.farmstival.menus.MenuDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ExceptionProcessor {

    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService configInfoService;
    private final BoardConfigDeleteService configDeleteService;

    private final BoardConfigValidator configValidator;
    private final Utils utils;

    @ModelAttribute("menuCode")
    public String getMenuCode() { // 주 메뉴 코드
        return "board";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() { // 서브 메뉴
        return Menu.getMenus("board");
    }

    /**
     * 게시판 목록
     *
     * @return
     */
    @GetMapping
    public String list(@ModelAttribute BoardSearch search, Model model) {
        commonProcess("list", model); //서브메뉴로 게시판 목록 선택

        ListData<Board> data = configInfoService.getList(search, true); //모든 게시판 목록 조회

        List<Board> items = data.getItems(); //게시판 객체 리스트
        Pagination pagination = data.getPagination();

        model.addAttribute("items", items);
        model.addAttribute("pagination", pagination);

        return "board/list";
    }

    /**
     * 게시판 목록 - 수정
     *
     * @param chks
     * @return
     */
    @PatchMapping//PATCH요청.. 자원의 일부를 변경하고자 할 때 사용
    public String editList(@RequestParam("chk") List<Integer> chks, Model model) {
        //게시판 목록에서 chk이름의 체크박스가 선택되면 폼 제출될때 해당 값 chks 리스트에 받아옴

        commonProcess("list", model); //서브메뉴로 게시판 목록 선택

        configSaveService.saveList(chks); //선택된 게시판 수정 작업

        model.addAttribute("script", "parent.location.reload()");
        return "common/_execute_script";
    }

    @DeleteMapping
    public String deleteList(@RequestParam("chk") List<Integer> chks, Model model) {
        commonProcess("list", model);

        configDeleteService.deleteList(chks);

        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    /**
     * 게시판 등록
     *
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute RequestBoardConfig config, Model model) {
        commonProcess("add", model);

        return "board/add";
    }

    @GetMapping("/edit/{bid}")
    public String edit(@PathVariable("bid") String bid, Model model) {
        commonProcess("edit", model);

        RequestBoardConfig form = configInfoService.getForm(bid);
        System.out.println(form);
        model.addAttribute("requestBoardConfig", form);

        return "board/edit";
    }

    /**
     * 게시판 등록/수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoardConfig config, Errors errors, Model model) {
        String mode = config.getMode();

        commonProcess(mode, model);

        configValidator.validate(config, errors);

        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(System.out::println);
            return "board/" + mode;
        }

        configSaveService.save(config);


        return "redirect:" + utils.redirectUrl("/board");
    }

    /**
     * 게시글 관리
     *
     * @return
     */
    @GetMapping("/posts")
    public String posts(Model model) {
        commonProcess("posts", model);

        return "board/posts";
    }

    /**
     * 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        String pageTitle = "게시판 목록";
        mode = StringUtils.hasText(mode) ? mode : "list"; //기본값 list

        if (mode.equals("add")) { //페이지 제목 동적으로 설정
            pageTitle = "게시판 등록";

        } else if (mode.equals("edit")) {
            pageTitle = "게시판 수정";

        } else if (mode.equals("posts")) {
            pageTitle = "게시글 관리";

        }

        List<String> addScript = new ArrayList<>();

        if (mode.equals("add") || mode.equals("edit")) { // 게시판 등록 또는 수정
            //에디터 편집기, 파일 관리 기능
            addScript.add("ckeditor5/ckeditor");
            addScript.add("fileManager");

            addScript.add("board/form");
        }

        model.addAttribute("pageTitle", pageTitle); //템플릿에서 ${pageTitle}로 참조
        model.addAttribute("subMenuCode", mode); //활성화된 서브 메뉴
        model.addAttribute("addScript", addScript); //템플릿에 자바스크립트 파일을 스크립트 태그로 동적 포함
    }
}
