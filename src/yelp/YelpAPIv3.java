package yelp;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YelpAPIv3 {
	private static final String API_HOST = "api.yelp.com";
	private static final String SEARCH_PATH = "/v3/businesses/search";
	private static final String DEFAULT_TERM = "restaurants"; // "dinner"; //
	private static final int SEARCH_LIMIT = 20;
	private static final String API_KEY = "API_KEY";

	private final OkHttpClient client;
	
	public YelpAPIv3() {
		this.client = new OkHttpClient();
	}

	/**
	 * Creates and sends a request to the Search API by term and location.
	 * 
	 * @throws IOException
	 */
	public String searchForBusinessesByLocation(double lat, double lon) throws IOException {
		// GET /businesses/search
		Request request = new Request.Builder()
				.url("https://" + API_HOST + SEARCH_PATH 
						+ "?term=" + DEFAULT_TERM 
						+ "&latitude=" + lat 
						+ "&longitude=" + lon 
						// + "&sort_by=distance"
						+ "&limit=" + String.valueOf(SEARCH_LIMIT))
				.addHeader("Authorization", "Bearer" + " " + API_KEY)
				.build();
		System.out.println(request);
		return sendRequestAndGetResponse(request);
	}
	
	private String sendRequestAndGetResponse(Request request) throws IOException {
		Response response = client.newCall(request).execute();
		String responseBodyString = response.body().string(); // The response body can be consumed only once
		response.close();
		return responseBodyString;
	}

	/**
	 * Queries the Search API based on the command line arguments and takes the
	 * first result to query the Business API.
	 */
	private static void queryAPI(YelpAPIv3 yelpApi, double lat, double lon) {
		String searchResponseJSON = null;
		JSONObject response = null;
		try {
			searchResponseJSON = yelpApi.searchForBusinessesByLocation(lat, lon);
			response = new JSONObject(searchResponseJSON); // parser
			JSONArray businesses = (JSONArray) response.get("businesses");
			for (int i = 0; i < businesses.length(); i++) {
				JSONObject business = (JSONObject) businesses.get(i);
				System.out.println(business);
//				String name = (String) business.get("name");
//				System.out.println(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		YelpAPIv3 yelpApi = new YelpAPIv3();
		queryAPI(yelpApi, 41.8391, -87.6363);
	}
}
