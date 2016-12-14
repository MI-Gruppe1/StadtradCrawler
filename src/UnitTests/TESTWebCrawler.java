package UnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;


import stadtradcrawl.WebCrawler;

public class TESTWebCrawler {
	
	private static WebCrawler testedCrawler;
	
	@Before
	public void createWorkingset(){
		try {
			testedCrawler = new WebCrawler("http://api.citybik.es/v2/networks/stadtrad?fields=stations");
		} catch (JSONException | IOException e) {
			System.out.println("Something went wrong \n");
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
}
