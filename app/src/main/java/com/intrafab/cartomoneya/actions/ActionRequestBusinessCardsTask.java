package com.intrafab.cartomoneya.actions;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.BizCardPopulatedListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 25/05/15.
 */
public class ActionRequestBusinessCardsTask extends GroundyTask {
    @Override
    protected TaskResult doInBackground() {
        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService("");
            List<BusinessCardPopulated> list = service.getBusinessCardsPopulated();

            if (list == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            if (list.size() > 0) {
                DBManager.getInstance().insertArrayObject(getContext(), BizCardPopulatedListLoader.class, Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, list, BusinessCardPopulated.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
