package com.joyfarm.farmstival.global;

import lombok.Data;

import java.util.List;

/* 리스트와 페이징을 같이 담아줄 수 있는 형태 */
@Data
public class ListData<T> {
    private List<T> items; //목록 데이터
    private Pagination pagination;
}
