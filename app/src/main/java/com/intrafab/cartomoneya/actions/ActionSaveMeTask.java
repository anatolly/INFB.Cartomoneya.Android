package com.intrafab.cartomoneya.actions;

import android.os.Bundle;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.db.DBManager;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

/**
 * Created by Artemiy Terekhov on 22.06.2015.
 */
public class ActionSaveMeTask extends GroundyTask {

    public static final String USER_DATA = "arg_user_data";
    @Override
    protected TaskResult doInBackground() {
        Bundle inputBundle = getArgs();
        User userAccount = inputBundle.getParcelable(USER_DATA);

        if (userAccount != null) {
            DBManager.getInstance().insertObject(getContext(), Constants.Prefs.PREF_PARAM_ME, userAccount, User.class);
        }

        return succeeded();
    }
}
