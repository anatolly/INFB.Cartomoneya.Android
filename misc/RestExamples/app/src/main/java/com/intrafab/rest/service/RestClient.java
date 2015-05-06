package com.intrafab.rest.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import com.intrafab.rest.Node;
//import common.intrafab.com.common.rest.model.ApiService;
import com.intrafab.rest.model.Nodes;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import java.util.List;

/**
 * Created by mikhailzubov on 02.05.15.
 */
public class RestClient {

    private static final String BASE_URL = "http://89.109.55.200:9080";
    private Nodes apiService;

    //class MedicusNodes implements Nodes {
     //   @Override
      //  public List<Node> listNodes(/*String node*/) {
          //  List<Node> nodes = new ArrayList<Node>();

          //  return nodes;
       // }

        // listNodes(String node){
        //
        //    return (List<Node>) nodes;
       // };

  //  }

    public RestClient()
    {


      //  Gson gson = new GsonBuilder()
      //          .registerTypeAdapterFactory(new ItemTypeAdapterFactory()) // This is the important line ;)
      //          .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
      //          .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
             //   .setConverter(new GsonConverter(gson))
               // .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        apiService = restAdapter.create(Nodes.class);
    }

    public Nodes getApiService()
    {
        return apiService;
    }
}
