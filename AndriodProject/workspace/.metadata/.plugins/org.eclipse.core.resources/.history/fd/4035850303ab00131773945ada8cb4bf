import java.io.*;
import java.net.*;
import javax.xml.bind.DatatypeConverter;

public class createCMAccount {
	private static String email;
	private static String username;
	private static String password;
	private static String cloudmine_appid;
	private static String cloudmine_apikey;
	private static String emailPass;
	
	public static void setCredentials(String cmEmail, String cmUsername, String cmPassword, String cmAppID, String cmApiKey) throws Exception {
		email = cmEmail;
		username = cmUsername;
		password = cmPassword;
		byte[] emailPassEnc = (email + ":" + password).getBytes("UTF-8");
		emailPass = DatatypeConverter.printBase64Binary(emailPassEnc);
		cloudmine_appid = cmAppID;
		cloudmine_apikey = cmApiKey;
	}
	
	public static void createAccount() {
		String url;
		String body;
		
		url = "https://api.cloudmine.me/v1/app/" + cloudmine_appid + "/account/create";
		body = String.format("{\"credentials\": "
			+ "{\"email\": \"%s\","
			+ "\"username\": \"%s\","
			+ "\"password\": \"%s\"}}",email,username,password);
		doRest(url, body);
		url = "https://api.cloudmine.me/v1/app/" + cloudmine_appid + "/account/login";
		body = "";
		doRest(url, body);
	}
	
	public static void doRest(String url, String body) {
			try	{		
				HttpURLConnection urlConn;
				URL mURL = new URL(url);
				urlConn = (HttpURLConnection) mURL.openConnection();
			
				urlConn.setDoOutput(true);
				urlConn.addRequestProperty("Content-Type", "application/json");
				urlConn.addRequestProperty("X-CloudMine-ApiKey", cloudmine_apikey);
				urlConn.setRequestProperty("Content-Length", Integer.toString(body.length()));
				
				if (body.equals("")) urlConn.setRequestProperty("Authorization", "Basic " + emailPass);
		
				OutputStream os = urlConn.getOutputStream();
				os.write(body.getBytes("UTF8"));
				os.close();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String line;
				while((line = br.readLine()) != null) { System.out.println(line); }
			}
		catch (Exception e)	{ e.printStackTrace();	}
	}
}
