package com.intrafab.cartomoneya.actions;

import android.os.Bundle;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.http.HttpRestService;
import com.intrafab.cartomoneya.http.RestApiConfig;
import com.intrafab.cartomoneya.utils.Connectivity;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by Artemiy Terekhov on 26.05.2015.
 */
public class ActionRequestCreateShopCardTask extends GroundyTask {

    public static final String ARG_FRONT_IMAGE = "arg_front_image";

    @Override
    protected TaskResult doInBackground() {

        if (!Connectivity.isNetworkConnected()) {
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, false);
        }

        Bundle inputBundle = getArgs();
        String frontImagePath = inputBundle.getString(ARG_FRONT_IMAGE);

        try {
            HttpRestService service = RestApiConfig.getRestService("");

            TypedFile typedFile = new TypedFile("multipart/form-data", new File(frontImagePath));
            String description = "hello, this is description speaking";

            service.createShopCard(typedFile, description, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Logger.e("Upload", "success: " + s);
                }

                @Override
                public void failure(RetrofitError error) {
                    Logger.e("Upload", "error: " + error.getResponse());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return failed()
                    .add(Constants.Extras.PARAM_INTERNET_AVAILABLE, true);
        }

        return succeeded();
    }
}
