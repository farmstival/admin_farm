package com.joyfarm.farmstival.tour.controllers;

import lombok.Data;

@Data
public class TourPlaceSearch { //위치기반
    private Double latitude;
    private Double longitude;
    private Integer radius = 1000;
}
