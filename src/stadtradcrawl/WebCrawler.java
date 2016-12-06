/**
 * @author Flah-Uddin Ahmad
 * @author Andreas Loeffler
 * @version 1.0
 */

package stadtradcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * WebCrawler crawls Data from citybike.es.
 */
public class WebCrawler {
	
	private JSONObject json = null;
	private JSONArray dataArray = null;
	
	/**
	 * @param url  http://api.citybik.es/v2/networks/stadtrad?fields=stations
	 * @throws JSONException
	 * @throws IOException
	 */
	public WebCrawler(String url) throws JSONException, IOException {
		super();
		json = this.readJsonFromUrl(url);
		dataArray = json.getJSONObject("network").getJSONArray("stations");
	}

	/*Read all from Reader rd and put it to one String. Return whole String */
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/*Create Readers to read from the URL. URL is an inputparameter. Create form the returned String (Methode: readAll) one JSONObject.
	 * JSONObject ist returned */
	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {  //finally will always execute, if an try block exists. Doesnt matter if there is an Exception or not.
			is.close();
		}
	}
	
	private String changeTimeStamptoUnixTimeString(String str) throws ParseException{
		//2016-11-09T13:04:53.973000Z
		String[] timewithoutmillis = str.split("\\.");
		String timeStamp = timewithoutmillis[0] + "Z";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = sdf.parse(timeStamp); 
		long milis = date.getTime()/1000;
		return String.valueOf(milis);
	}

	/*Input-Parameter is JSONArray. Iterate through the array and print needed Information  */
	/**
	 * Extract needed Data and put them into a JSON-Array.
	 * Send Data to StadtradDBService
	 * @throws JSONException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws UnirestException
	 * @throws ParseException
	 */
	public void sendDataToDB() throws JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, UnirestException, ParseException {
		
		ArrayList<HashMap<String, String>> dataSet = new ArrayList<>();
		
		for (int i = 0; i < dataArray.length(); i++) {
			HashMap<String, String> jsonObject = new HashMap<String, String>();
			jsonObject.put("id" ,dataArray.getJSONObject(i).get("id").toString());
			jsonObject.put("name", dataArray.getJSONObject(i).get("name").toString());
			jsonObject.put("free_bikes", dataArray.getJSONObject(i).get("free_bikes").toString());
			jsonObject.put("timestamp", changeTimeStamptoUnixTimeString(dataArray.getJSONObject(i).get("timestamp").toString()));
			jsonObject.put("latitude", dataArray.getJSONObject(i).get("latitude").toString());
			jsonObject.put("longitude", dataArray.getJSONObject(i).get("longitude").toString());

			dataSet.add(jsonObject);
		}
		
		// Send whole data to DBService
		Unirest.post("http://stadtraddbservice:6000/newData")
		//Unirest.post("http://localhost:6000/newData") // Fuer lokalen Test
		  .body(new Gson().toJson(dataSet))
		  .asString();
	}
}