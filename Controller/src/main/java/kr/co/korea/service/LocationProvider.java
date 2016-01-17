package kr.co.korea.service;

import kr.co.korea.domain.Coordination;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ideapad on 2016-01-16.
 */
public class LocationProvider {
    /**
     * 출발지와 목적지의 좌표를 얻기 위한 공통 메서드.
     *
     * @param locationName
     * @return
     */
    public static Map<String, Coordination> getCoordination(String locationName){
        Map<String, Coordination> coordinationMap = null;
        Coordination coordination = null;

        try {
            String gecodeInfo = LocationProvider.getGeocodeInfo(locationName);

            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(gecodeInfo);
            String longitude = (String) jsonObject.get("EPSG_4326_X");
            String latitude = (String) jsonObject.get("EPSG_4326_Y");

            if((longitude != null) && (latitude != null)){
                coordinationMap = new HashMap<String, Coordination>();
                coordination = new Coordination();
                coordination.setLongitude(Double.parseDouble(longitude));
                coordination.setLatitude(Double.parseDouble(latitude));

                coordinationMap.put(locationName, coordination);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return coordinationMap;
    }

    public static String getGeocodeInfo(String locationName) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String address = URLEncoder.encode(locationName, "utf-8");
        HttpGet httpGet = new HttpGet("http://apis.vworld.kr/jibun2coord.do?q=" + address + "&apiKey=3624E362-E330-3CED-B124-1086F4FFE022&domain=&output=json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0");

        String result = "";

        try {

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            String inputLine;
            StringBuffer response = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            result = response.toString();

            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
