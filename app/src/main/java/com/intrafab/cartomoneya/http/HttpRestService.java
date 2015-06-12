package com.intrafab.cartomoneya.http;

import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.data.Personage;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.data.User;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
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

    @PUT("/shopcard/{id}")
    public ShopCard updateShopCard(@Path("id") String id, @Body ShopCard card);

    // Порядок важен. Файл должен быть последним параметром
    @Multipart
    @POST("/shopcard/storeImageByFormData")
    public ShopCard uploadImageShopCard(
            @Part("shopCardImageType") TypedString imageType,
            @Part("shopCardID") TypedString cardID,
            @Part("_data") TypedFile file);

    @POST("/bizcard")
    public BusinessCard createBusinessCard(@Body BusinessCard card);

    @PUT("/bizcard/{id}")
    public BusinessCard updateBusinessCard(@Path("id") String id, @Body BusinessCard card);

    @GET("/bizcard")
    public List<BusinessCard> getBusinessCards();

    @GET("/bizcard?populate=[personage]")
    public List<BusinessCardPopulated> getBusinessCardsPopulated();

    // Порядок важен. Файл должен быть последним параметром
    @Multipart
    @POST("/bizcard/storeImageByFormData")
    public BusinessCard uploadImageBusinessCard(
            @Part("bizCardImageType") TypedString imageType,
            @Part("bizCardID") TypedString cardID,
            @Part("_data") TypedFile file);

    @GET("/shopcard/{id}")
    public Personage getPersonage(@Path("id") String id);

    @POST("/bizcardpersonage")
    public Personage createPersonage(@Body Personage card);

    @PUT("/bizcardpersonage/{id}")
    public Personage updatePersonage(@Path("id") String id, @Body Personage personage);
}
