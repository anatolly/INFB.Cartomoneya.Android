package com.intrafab.cartomoneya.http;

import com.intrafab.cartomoneya.data.BizCard;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.data.User;

import java.util.List;

import retrofit.http.GET;

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

    @GET("/bizcard?populate=[personage]")
    public List<BizCard> getBizCards();
}
