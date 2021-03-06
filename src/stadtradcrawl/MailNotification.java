/**
 * @author Flah-Uddin Ahmad
 * @author Andreas Loeffler
 * @version 1.0
 */

package stadtradcrawl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * MailNotification will send an E-Mail to mi.mailnotification(at)gmail.com when ever a Exception in a Crawler will appear
 */

public class MailNotification {
	  private static final String SMTP_HOST = "smtp.gmail.com";
	  private static final int SMTP_PORT = 465;
	  
	  private static final String USERNAME = "mi.mailnotification@gmail.com";
	  private static final String PASSWORD = "miws2016";
	  
	  static void sendMail(Exception e) {
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  e.printStackTrace(pw);
		    try {
		    	SimpleEmail email = new SimpleEmail();
			    email.setHostName(SMTP_HOST);
			    email.setAuthentication(USERNAME, PASSWORD);
			    email.setDebug(true);
			    email.setSmtpPort(SMTP_PORT);
			    email.setSSLOnConnect(true);
				email.addTo(USERNAME);
				email.setFrom(USERNAME, "StadtCrawlerService");
			    email.setSubject("Exception");
			    email.setMsg(sw.toString());
			    email.send();
			} catch (EmailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	  }
}
