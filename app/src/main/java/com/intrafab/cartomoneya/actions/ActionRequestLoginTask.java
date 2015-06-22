package com.intrafab.cartomoneya.actions;

import android.os.Bundle;
import android.text.TextUtils;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.data.WrapperLogin;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import org.json.JSONObject;

/**
 * Created by Artemiy Terekhov on 22.06.2015.
 */
public class ActionRequestLoginTask extends GroundyTask {
    public static final String TAG = ActionRequestLoginTask.class.getName();

    public static final String ARG_USER_NAME = "arg_user_name";
    public static final String ARG_USER_PASSWORD = "arg_user_password";

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed().add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        String userName = inputBundle.getString(ARG_USER_NAME);
        String userPassword = inputBundle.getString(ARG_USER_PASSWORD);

        WrapperLogin login = new WrapperLogin();
        login.setUsername(userName);
        login.setPassword(userPassword);

        try {
            HttpRestService service = RestApiConfig.getRestService();
//            Response result = service.login(login);

//            if (result != null) {
//                TypedInput body = result.getBody();
//
//                if (body != null) {
                    String resultString = createTestUser();//new String(((TypedByteArray) body).getBytes());
                    Logger.e(TAG, "ActionRequestLoginTask result: " + resultString);

                    JSONObject bodyJson = new JSONObject(resultString);
                    if (bodyJson != null) {
                        String sessid = null;
                        if (bodyJson.has("sessid")) {
                            sessid = bodyJson.optString("sessid");
                        }

                        String sessionName = null;
                        if (bodyJson.has("session_name")) {
                            sessionName = bodyJson.optString("session_name");
                        }

                        String token = null;
                        if (bodyJson.has("token")) {
                            token = bodyJson.optString("token");
                        }

                        User userAccount = null;
                        if (bodyJson.has("user")) {
                            userAccount = new User(bodyJson.optJSONObject("user"));
                        }

                        if (userAccount != null && !TextUtils.isEmpty(token)) {
                            return succeeded()
                                    .add(Constants.Extras.PARAM_TOKEN, token)
                                    .add(Constants.Extras.PARAM_USER_DATA, userAccount);
                        }
                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();

            return failed().add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return failed().add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
    }

    private static String createTestUser() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("{");
            builder.append("\"token\" : \"vsagsarbafvasgwRBBwvsvwevwev\",");
            builder.append("\"user\" : {");
            builder.append("\"id\" : 1,");
            builder.append("\"name\" : \"Jack\",");
            builder.append("\"login\" : \"jack@mail.ru\",");
            builder.append("\"createdAt\" : \"2015-05-05T17:52:18.030Z\",");
            builder.append("\"updatedAt\" : \"2015-05-05T17:52:18.030Z\"");
            builder.append("}");
            builder.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}