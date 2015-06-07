package com.intrafab.cartomoneya.actions;

import android.os.Bundle;
import android.text.TextUtils;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by Artemiy Terekhov on 26.05.2015.
 */
public class ActionRequestCreateBusinessCardTask extends GroundyTask {

    public static final String ARG_FRONT_IMAGE = "arg_front_image";
    public static final String ARG_BACK_IMAGE = "arg_back_image";
    //public static final String ARG_BUSINESS_CARD = "arg_business_card";

    private static final String IMAGE_TYPE_FRONT = "front";
    private static final String IMAGE_TYPE_BACK = "back";
    public static final String ARG_BUSINESS_CARD = "arg_business_card";;

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        String frontImagePath = inputBundle.getString(ARG_FRONT_IMAGE);
        String backImagePath = inputBundle.getString(ARG_BACK_IMAGE);
        BusinessCard newCard = inputBundle.getParcelable(ARG_BUSINESS_CARD);

        if (newCard == null) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService("Basic d3FlcXdlOnF3ZXF3ZQ==");

            BusinessCard createdCard = service.createBusinessCard(newCard);
            //ShopCard createdCard = newCard;
            //createdCard.setId(15);
            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            if (!TextUtils.isEmpty(frontImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(frontImagePath));
                createdCard = service.uploadImageBusinessCard(new TypedString(IMAGE_TYPE_FRONT), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            if (!TextUtils.isEmpty(backImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(backImagePath));
                createdCard = service.uploadImageBusinessCard(new TypedString(IMAGE_TYPE_BACK), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }


            List<BusinessCard> dbItems = DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS, BusinessCard[].class);
            List<BusinessCard> items = new LinkedList<BusinessCard>(dbItems);

            int count = items.size();
            for(int i = 0; i < count; ++i) {
                BusinessCard card = items.get(i);
                if (card.getId() == createdCard.getId()) {
                    items.set(i, createdCard);
                    break;
                }
            }

            if (items.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS, items, BusinessCard.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
