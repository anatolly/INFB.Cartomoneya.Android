package com.intrafab.cartomoneya.actions;

import android.os.Bundle;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShopCardListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ActionRequestShoppingCardsTask extends GroundyTask {

    public static final String ARG_USER = "arg_user";

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        User user = inputBundle.getParcelable(ARG_USER);

        try {
            HttpRestService service = RestApiConfig.getRestService("");
            List<ShopCard> list = service.getShopCards();

            if (list == null)
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);

            if (user != null) {
                Iterator<ShopCard> it = list.iterator();
                while (it != null && it.hasNext()) {
                    ShopCard card = it.next();
                    if (card.getBelongsToUser() != user.getId()) {
                        it.remove();
                    }
                }
            }

            if (list.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), ShopCardListLoader.class, Constants.Prefs.PREF_PARAM_SHOPPING_CARDS, list, ShopCard.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
