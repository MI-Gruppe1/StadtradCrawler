/**
 * @author Flah-Uddin Ahmad
 * @author Andreas Loeffler
 * @version 1.0
 */

package stadtradcrawl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * App starts a timer, which will fire up every 5 mins a {@link WebCrawler}.
 */
public class App extends TimerTask {
	
	private static WebCrawler crawler;
	
	public static void main(String[] args) {
		
		/*Create Timer. Time will execute every hour.*/
		Timer timer = new Timer();
		timer.schedule(new App(), 1000, 300000);//3600000

	}

	/**
	 * Creates a WebCrawler and send a Request to the given URL.
	 * Call Crawler-Method {@link WebCrawler#sendDataToDB()}.
	 */
	public void run(){
		try {
			crawler = new WebCrawler("http://api.citybik.es/v2/networks/stadtrad?fields=stations");
			crawler.sendDataToDB();	
		} catch (Exception e) {
			MailNotification.sendMail(e);
		}
			
	}

}
