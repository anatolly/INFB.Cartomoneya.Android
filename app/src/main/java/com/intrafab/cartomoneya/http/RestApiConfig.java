package com.intrafab.cartomoneya.http;

import com.intrafab.cartomoneya.AppApplication;
import com.intrafab.cartomoneya.Constants;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class RestApiConfig {

    public static final String BASE_HOST_NAME = Constants.RELEASE_MODE ? "caramba-shop.ru:1537/api" : "caramba-shop.ru:1537/api"; //1537 1336
    public static final String VERSION_API = "v1.0";
    public static final String BASE_HOST_URL = "http://" + BASE_HOST_NAME + "/" + VERSION_API;

    public static HttpRestService getRestService() {
        return ServiceGenerator.createService(HttpRestService.class, RestApiConfig.BASE_HOST_URL, AppApplication.getToken());
    }
}
