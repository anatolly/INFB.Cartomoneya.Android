package com.intrafab.cartomoneya.actions;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShopCardListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ActionRequestShoppingCardsTask extends GroundyTask {

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService("");
            List<ShopCard> list = service.getShopCards();

            if (list == null)
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);

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
