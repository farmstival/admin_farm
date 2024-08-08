package com.joyfarm.farmstival.tour.services;


import com.joyfarm.farmstival.global.rests.gov.api.ApiItem;
import com.joyfarm.farmstival.global.rests.gov.api.ApiResult;
import com.joyfarm.farmstival.tour.entities.TourPlace;
import com.joyfarm.farmstival.tour.repositories.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {
    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;

    //인증키는 잠시 고정해서 사용, 추후 환경변수로 변경?
    private String serviceKey = "7rW1xx6jrYQOtd7Nx%2BpX73qloNsfDQ2swHW4soL3zbuBupPgs6qNfQ6s2Hp0DiiBwTGQ4%2FE2AP%2BPgT15L95r%2BQ%3D%3D";

    //새롭게 업데이트된 데이터가 있을 수 있기 때문에 업데이트 주기적으로 할 수 있도록 스케줄링 설정
    //@Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS) //실제 서버에는 주기적으로 데이터 들어갈 수 있도록 설정하기
    public void update() {

        for(int i=1; i <=10 ; i++) {
            String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaBasedList1?MobileOS=AND&MobileApp=test&numOfRows=10&pageNo=%d&serviceKey=%s&_type=json", serviceKey, i);
            //pageNo

            ResponseEntity<ApiResult> response = null;
            try {
                response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                List<ApiItem> items = response.getBody().getResponse().getBody().getItems().getItem();
                System.out.println(response);
                for(ApiItem item : items) {
                    try{
                        String address = item.getAddr1();
                        address += StringUtils.hasText(item.getAddr2()) ? " " + item.getAddr2() : "";
                        System.out.println("------------------------------");
                        System.out.println(item);

                        TourPlace tourplace = TourPlace.builder()
                                .contentId(item.getContentid())
                                .contentTypeId(item.getContenttypeid())
                                .category1(item.getCat1())
                                .category2(item.getCat2())
                                .category3(item.getCat3())
                                .title(item.getTitle())
                                .tel(item.getTel())
                                .areaCode(address)
                                .bookTour(item.getBooktour().equals("1"))
                                .distance(item.getDist())
                                .firstImage(item.getFirstimage())
                                .firstImage2(item.getFirstimage2())
                                .cpyrhtDivCd(item.getCpyrhtDivCd())
                                .latitude(item.getMapx())
                                .latitude(item.getMapy())
                                .longitude(item.getMapx())
                                .mapLevel(item.getMlevel())
                                .sigugunCode(item.getSigungucode())
                                .build();
                        repository.saveAndFlush(tourplace);

                    } catch (Exception e) { //예외 발생하면 이미 등록된 여행지
                       e.printStackTrace();
                       //continue;
                    }
                }
            }//endif
        }
    }
}
