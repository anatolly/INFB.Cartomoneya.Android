package com.intrafab.cartomoneya.http;

import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.data.User;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by Artemiy Terekhov on 10.04.2015.
 */
public interface HttpRestService {

    @GET("/user")
    public List<User> getUsers();

    @GET("/shopcard")
    public List<ShopCard> getShopCards();

    @GET("/shopbrand")
    public List<ShopBrand> getShopBrands();

    @POST("/shopcard")
    public ShopCard createShopCard(@Body ShopCard card);

    // Порядок важен. Файл должен быть последним параметром
    @Multipart
    @POST("/shopcard/storeImageByFormData")
    public ShopCard uploadImageShopCard(
            @Part("shopCardImageType") TypedString imageType,
            @Part("shopCardID") TypedString cardID,
            @Part("_data") TypedFile file);
}
