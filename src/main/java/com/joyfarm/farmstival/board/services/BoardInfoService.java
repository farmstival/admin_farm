package com.joyfarm.farmstival.board.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyfarm.farmstival.board.controllers.BoardDataSearch;
import com.joyfarm.farmstival.board.controllers.RequestBoard;
import com.joyfarm.farmstival.board.entities.BoardData;
import com.joyfarm.farmstival.file.entities.FileInfo;
import com.joyfarm.farmstival.file.services.FileInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardInfoService {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Utils utils;
    private final FileInfoService fileInfoService;


    public BoardData get(Long seq){
        String url = utils.url("/board/admin/" + seq, "api-service");

        HttpHeaders headers = utils.getCommonHeaders("GET");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<JSONData> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, request, JSONData.class);
        System.out.println(response);

        if (!response.getStatusCode().is2xxSuccessful() || !response.getBody().isSuccess()) {
            return new BoardData();
        }

        Object data = response.getBody().getData();
        try {
            return om.readValue(om.writeValueAsString(data), BoardData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new BoardData();
    }

    public RequestBoard getForm(BoardData data){
        RequestBoard form = new ModelMapper().map(data, RequestBoard.class);
        form.setMode("update");
        return form;
    }

    public ListData<BoardData> getList(BoardDataSearch search) {
        String url = utils.url("/board/admin", "api-service");

        HttpHeaders headers = utils.getCommonHeaders("GET");
        int page = search.getPage();
        int limit = search.getLimit();
        String sopt = Objects.requireNonNullElse(search.getSopt(), "");
        String skey = Objects.requireNonNullElse(search.getSkey(), "");
        String bid = Objects.requireNonNullElse(search.getBid(), "");
        String bids = search.getBids() == null ? "" :
                search.getBids().stream()
                        .map(s -> "bids=" + s).collect(Collectors.joining("&"));

        String sort = Objects.requireNonNullElse(search.getSort(), "");

        url += String.format("?page=%d&limit=%d&sopt=%s&skey=%s&bid=%s&sort=%s&%s", page, limit, sopt, skey, bid, sort, bids);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<JSONData> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, request, JSONData.class);

        if (!response.getStatusCode().is2xxSuccessful() || !response.getBody().isSuccess()) {
            return new ListData<>();
        }

        Object data = response.getBody().getData();
        try {
            return om.readValue(om.writeValueAsString(data), ListData.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ListData<>();
    }

    public ListData<BoardData> getList(String bid, BoardDataSearch search){
        search.setBid(bid);
        return getList(search);
    }

    public ListData<BoardData> getList(String bid){return getList(bid,new BoardDataSearch());}

    /**
     * 게시글에 추가될 정보 처리
     * - 에디터 첨부 이미지 파일, 일반 첨부 파일
     * @param data
     */
    private void addBoardData(BoardData data){
        if(data == null){
            return;
        }
        String gid = data.getGid();

        List<FileInfo> editorFiles = fileInfoService.getList(gid,"editor");

        List<FileInfo> attachFiles = fileInfoService.getList(gid,"attach");

        data.setEditorImages(editorFiles);
        data.setAttachFiles(attachFiles);
    }
}