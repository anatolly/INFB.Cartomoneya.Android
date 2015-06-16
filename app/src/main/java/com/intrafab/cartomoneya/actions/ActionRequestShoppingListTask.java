package com.intrafab.cartomoneya.actions;

import android.os.Bundle;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShoppingListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mono on 06/06/15.
 */
public class ActionRequestShoppingListTask extends GroundyTask {

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
            List<ShoppingListItem> list = service.getShoppingList();

            if (list == null)
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);

            if (user != null) {
                Iterator<ShoppingListItem> it = list.iterator();
                while (it != null && it.hasNext()) {
                    ShoppingListItem card = it.next();
                    if (card.getBelongsToUser() != user.getId()) {
                        it.remove();
                    }
                }
            }

            if (list.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), ShoppingListLoader.class, Constants.Prefs.PREF_PARAM_SHOPPING_LIST, list, ShoppingListItem.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
