package com.joyfarm.farmstival.tour.services;


import com.joyfarm.farmstival.global.rests.gov.api.ApiItem;
import com.joyfarm.farmstival.global.rests.gov.api.ApiResult;
import com.joyfarm.farmstival.tour.controllers.TourPlaceSearch;
import com.joyfarm.farmstival.tour.entities.QTourPlace;
import com.joyfarm.farmstival.tour.entities.TourPlace;
import com.joyfarm.farmstival.tour.repositories.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
public class TourPlaceInfoService {

    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;

    //공공데이터 황용 신청 시 받은 인증키
    private String serviceKey = "7rW1xx6jrYQOtd7Nx%2BpX73qloNsfDQ2swHW4soL3zbuBupPgs6qNfQ6s2Hp0DiiBwTGQ4%2FE2AP%2BPgT15L95r%2BQ%3D%3D";

    public List<TourPlace> getList(TourPlaceSearch search) {
        double lat = search.getLatitude();
        double lon = search.getLongitude();
        int radius = search.getRadius();

        String url = String.format("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?MobileOS=AND&MobileApp=test&mapX=%f&mapY=%f&radius=%d&numOfRows=5000&serviceKey=%s&_type=json", lon, lat, radius, serviceKey);
        try {
            ResponseEntity<ApiResult> response = restTemplate.getForEntity(URI.create(url), ApiResult.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResponse().getHeader().getResultCode().equals("0000")) {

                List<Long> ids = response.getBody().getResponse().getBody().getItems().getItem().stream().map(ApiItem::getContentid).toList();
                if(!ids.isEmpty()) {
                    QTourPlace tourPlace = QTourPlace.tourPlace;
                    List<TourPlace> items = (List<TourPlace>) repository.findAll(tourPlace.contentId.in(ids), Sort.by(asc("contentId")));

                    return items;
                } //endif
            }//endif

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
