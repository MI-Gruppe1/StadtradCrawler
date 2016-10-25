/*
 * Quelle: http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 * Author: Andreas L�ffler
 */

package stadtradcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

public class WebCrawler {
	
	private JSONObject json = null;
	private JSONArray dataArray = null;
	
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

	/*Input-Parameter is JSONArray. Iterate through the array and print needed Information  */
	public void persistData() throws JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = (Connection) DriverManager.getConnection(
		    "jdbc:mysql://mysqldb:3306/mi",
		    "mi",
		    "miws16"
		);
		
		String query = " insert into crawledData (station_id, station_name, free_bikes, information_timestamp, latitude, longitude)" +
		               " values (?, ?, ?, ?, ?, ?)";
			      
		for (int i = 0; i < dataArray.length(); i++) {
			PreparedStatement preparedStmt = connection.prepareStatement(query);
		      preparedStmt.setString(1, dataArray.getJSONObject(i).get("id").toString());
		      preparedStmt.setString(2, dataArray.getJSONObject(i).get("name").toString());
		      preparedStmt.setString(3, dataArray.getJSONObject(i).get("free_bikes").toString());
		      preparedStmt.setString(4, dataArray.getJSONObject(i).get("timestamp").toString());
		      preparedStmt.setString(5, dataArray.getJSONObject(i).get("latitude").toString());
		      preparedStmt.setString(6, dataArray.getJSONObject(i).get("longitude").toString());      

		      // execute the preparedstatement
		      preparedStmt.execute();
		}
		
		connection.close();
	}
}
