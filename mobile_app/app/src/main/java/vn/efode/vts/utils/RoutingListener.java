package vn.efode.vts.utils;

import com.directions.route.Route;
import com.directions.route.RouteException;

import java.util.List;

/**
 * Created by Tuan on 26/04/2017.
 */

public interface RoutingListener {
    void onRoutingFailure(RouteException e);

    void onRoutingStart();

    void onRoutingSuccess(List<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}
