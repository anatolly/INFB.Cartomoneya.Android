package com.intrafab.cartomoneya.http;

import com.intrafab.cartomoneya.Constants;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class RestApiConfig {

    public static final String BASE_HOST_NAME = Constants.RELEASE_MODE ? "caramba-shop.ru:1537/api" : "caramba-shop.ru:1537/api";
    public static final String VERSION_API = "v1.0";
    public static final String BASE_HOST_URL = "http://" + BASE_HOST_NAME + "/" + VERSION_API;

    public static HttpRestService getRestService(String token) {
        return ServiceGenerator.createService(HttpRestService.class, RestApiConfig.BASE_HOST_URL, token);
    }
}
