package com.intrafab.cartomoneya.actions;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShopBrandListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class ActionRequestShopBrandTask extends GroundyTask {

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService();
            List<ShopBrand> list = service.getShopBrands();

            if (list == null)
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);

            if (list.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), ShopBrandListLoader.class, Constants.Prefs.PREF_PARAM_SHOP_BRANDS, list, ShopBrand.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
