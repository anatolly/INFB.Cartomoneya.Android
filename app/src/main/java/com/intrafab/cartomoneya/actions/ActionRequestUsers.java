package com.intrafab.cartomoneya.actions;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class ActionRequestUsers extends GroundyTask {

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed().add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        User userData = null;

        try {
            HttpRestService service = RestApiConfig.getRestService("");
            List<User> listUsers = service.getUsers();

            if (listUsers == null)
                return failed();

            if (listUsers.size() > 0) {
                userData = listUsers.get(0);
                DBManager.getInstance().insertObject(getContext(), Constants.Prefs.PREF_PARAM_ME, userData, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        if (userData != null) {
            return succeeded()
                    .add(Constants.Extras.PARAM_USER_DATA, userData);
        } else {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }
    }
}
