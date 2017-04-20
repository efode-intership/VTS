package vn.efode.vts.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tuan on 10/04/2017.
 */

/**
 * Class để parse JsonObject từ google map API lấy các location
 */

public class PathJSONParser {
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            jRoutes = jObject.getJSONArray("routes");
            for (int i=0 ; i < jRoutes.length() ; i ++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String,String>>();
                for(int j = 0 ; j < jLegs.length() ; j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    for(int k = 0 ; k < jSteps.length() ; k ++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);
                        for(int l = 0 ; l < list.size() ; l ++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat",
                                    Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng",
                                    Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public static Double pareDistance(JSONObject result){
        JSONArray rows = null;
        JSONArray elements = null;
        Double distanceValue = 0.0;
        try {
            rows = result.getJSONArray("rows");

            elements = ((JSONObject) rows.get(0)).getJSONArray("elements");
//            JSONObject distance = (JSONObject) ((JSONObject) elements.get(0)).get("distance");
//            distanceValue = distance.getString("text");

            JSONObject obj = (JSONObject) rows.get(0);
            for (int i = 0; i < elements.length(); i++) {
                JSONObject jsonObject  = (JSONObject) elements.get(i);
                JSONObject distance  = (JSONObject) jsonObject.get("distance");
                String[] value =  distance.getString("text").split(" ");
                distanceValue += Double.parseDouble(value[0]);
                JSONObject duration  = (JSONObject) jsonObject.get("duration");
            }

            Log.d("AAAAAAAAAAAAAa", String.valueOf(distanceValue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distanceValue;

    }

}
