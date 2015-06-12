package com.intrafab.cartomoneya.actions;

import android.os.Bundle;
import android.text.TextUtils;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.data.Personage;
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
    private static final String IMAGE_TYPE_FRONT = "front";
    private static final String IMAGE_TYPE_BACK = "back";
    public static final String ARG_BUSINESS_CARD_POPULATED = "arg_business_card_populated";
    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        String frontImagePath = inputBundle.getString(ARG_FRONT_IMAGE);
        String backImagePath = inputBundle.getString(ARG_BACK_IMAGE);
        BusinessCardPopulated newCard = inputBundle.getParcelable(ARG_BUSINESS_CARD_POPULATED);

        if (newCard == null ) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService("Basic d3FlcXdlOnF3ZXF3ZQ==");

            Personage personage = service.createPersonage(newCard.getPersonage());

            if (personage == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            newCard.setPersonage(personage);

            BusinessCard createdCard = service.createBusinessCard(newCard.getBusinessCard());
            //ShopCard createdCard = newCard;
            //createdCard.setId(15);
            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            newCard.setId(createdCard.getId());
            newCard.setCreatedAt(createdCard.getCreatedAt());
            newCard.setUpdatedAt(createdCard.getUpdatedAt());



            if (!TextUtils.isEmpty(frontImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(frontImagePath));
                createdCard = service.uploadImageBusinessCard(new TypedString(IMAGE_TYPE_FRONT), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            newCard.setFrontImageFile(createdCard.getFrontImageFile());

            if (!TextUtils.isEmpty(backImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(backImagePath));
                createdCard = service.uploadImageBusinessCard(new TypedString(IMAGE_TYPE_BACK), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            newCard.setBackImageFile(createdCard.getBackImageFile());

            List<BusinessCardPopulated> dbItems = DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, BusinessCardPopulated[].class);
            List<BusinessCardPopulated> items = new LinkedList<>(dbItems);

            int count = items.size();
            count = count - 1;
            for(;count >= 0; count--) {
                BusinessCardPopulated card = items.get(count);
                if (card.getId() == createdCard.getId()) {
                    items.set(count, newCard);
                    break;
                }
            }

            if (count < 0 )
            {
                items.add(newCard);
            }

            if (items.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, items, BusinessCardPopulated.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
