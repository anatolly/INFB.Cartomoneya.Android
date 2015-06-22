package com.intrafab.cartomoneya.actions;

import android.os.Bundle;
import android.text.TextUtils;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShopCardListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by Artemiy Terekhov on 28.05.2015.
 */
public class ActionRequestUpdateShopCardTask extends GroundyTask {

    public static final String ARG_FRONT_IMAGE = "arg_front_image";
    public static final String ARG_BACK_IMAGE = "arg_back_image";
    public static final String ARG_SHOP_CARD = "arg_shop_card";

    private static final String IMAGE_TYPE_FRONT = "front";
    private static final String IMAGE_TYPE_BACK = "back";

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        String frontImagePath = inputBundle.getString(ARG_FRONT_IMAGE);
        String backImagePath = inputBundle.getString(ARG_BACK_IMAGE);
        ShopCard newCard = inputBundle.getParcelable(ARG_SHOP_CARD);

        if (newCard == null) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        ShopCard createdCard = null;

        try {
            HttpRestService service = RestApiConfig.getRestService();

            createdCard = service.updateShopCard(String.valueOf(newCard.getId()), newCard);
            //ShopCard createdCard = newCard;
            //createdCard.setId(15);
            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            if (!TextUtils.isEmpty(frontImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(frontImagePath));
                createdCard = service.uploadImageShopCard(new TypedString(IMAGE_TYPE_FRONT), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            if (!TextUtils.isEmpty(backImagePath)) {
                TypedFile typedFile = new TypedFile("image/jpeg", new File(backImagePath));
                createdCard = service.uploadImageShopCard(new TypedString(IMAGE_TYPE_BACK), new TypedString(String.valueOf(createdCard.getId())), typedFile);
            }

            if (createdCard == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }


            List<ShopCard> dbItems = DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_SHOPPING_CARDS, ShopCard[].class);
            List<ShopCard> items = new LinkedList<ShopCard>(dbItems);

            int count = items.size();
            for (int i = 0; i < count; ++i) {
                ShopCard card = items.get(i);
                if (card.getId() == createdCard.getId()) {
                    items.set(i, createdCard);
                    break;
                }
            }

            if (items.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), ShopCardListLoader.class, Constants.Prefs.PREF_PARAM_SHOPPING_CARDS, items, ShopCard.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded()
                .add(Constants.Extras.PARAM_SHOP_CARD, createdCard);
    }
}
