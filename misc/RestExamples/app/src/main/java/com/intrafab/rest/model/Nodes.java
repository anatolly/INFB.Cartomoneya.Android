package com.intrafab.rest.model;
import java.util.List;
import com.intrafab.rest.Node;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mikhailzubov on 02.05.15.
 */
public interface Nodes {
    @GET("/userdata/node.json")
    List<Node> listNodes();
}
