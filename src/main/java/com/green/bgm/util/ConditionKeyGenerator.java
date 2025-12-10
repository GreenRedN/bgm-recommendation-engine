package com.green.bgm.util;

import com.green.bgm.dto.StoreRecommendRequestDto;

public class ConditionKeyGenerator {

    private ConditionKeyGenerator() {}

    public static String generate(StoreRecommendRequestDto req) {
        return String.join("|",
                "interior=" + value(req.getInterior()),
                "weather=" + value(req.getWeather()),
                "season="  + value(req.getSeason()),
                "time="    + value(req.getTime()),
                "density=" + value(req.getDensity()),
                "purpose=" + value(req.getPurpose())
        );
    }

    private static String value(String v) {
        return (v == null || v.isBlank()) ? "-" : v;
    }

}