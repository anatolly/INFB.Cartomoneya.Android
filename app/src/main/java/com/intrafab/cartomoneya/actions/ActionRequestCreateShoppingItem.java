package com.intrafab.cartomoneya.actions;

import android.os.Bundle;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.loaders.ShoppingListLoader;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 12/06/15.
 */
public class ActionRequestCreateShoppingItem extends GroundyTask {
    public static final String ARG_SHOPPING_ITEM = "arg_shopping_item";

    @Override
    protected TaskResult doInBackground() {
        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        ShoppingListItem item = inputBundle.getParcelable(ARG_SHOPPING_ITEM);

        if (item == null) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        try {
            HttpRestService service = RestApiConfig.getRestService("Basic d3FlcXdlOnF3ZXF3ZQ==");
            ShoppingListItem createdItem = service.createShopListItem(item);

            if (createdItem == null) {
                return failed()
                        .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
            }

            List<ShoppingListItem> dbItems = DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_SHOPPING_LIST, ShoppingListItem[].class);
            List<ShoppingListItem> items = new LinkedList<ShoppingListItem>(dbItems);

            items.add(createdItem);

            if (items.size() > 0)
                DBManager.getInstance().insertArrayObject(getContext(), ShoppingListLoader.class, Constants.Prefs.PREF_PARAM_SHOPPING_LIST, items, ShoppingListItem.class);
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
