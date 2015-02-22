package my.manager;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
/**
 * Class is used to update single server status using its url, port and 
 * timeout from settings;
 * @author Ayder
 */
public class UpdaterCallable implements Callable<ResponseResult> {
	public static Logger log = Logger.getLogger(UpdaterCallable.class);
	private String host="";
	private String urlPath="";
	private int timeout = 3;
	private int port = 80;
	ResponseResult result;
	/**
	 * Init constructor
	 */
	public UpdaterCallable(String adress, String urlPath, int port, int timeout)
	{
		this.host = adress;
		this.urlPath = urlPath;
		this.port = port;
		this.timeout = timeout;
		result = new ResponseResult();
	}
	/**
	 * Checks server and returns result
	 */
	@Override
	public ResponseResult call() throws Exception {
		HttpResponse response = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		URL url = new URL("http", host, port, urlPath);
		log.info("Checking host:" + host + ":" + port + " timeout = " + timeout);
		try {
			
			HttpGet request = new HttpGet(url.toURI());
			
			httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);		//use HTTP_1_1
			httpClient.getParams().setParameter("http.socket.timeout", new Integer(1000*timeout));	//set timeout
			
			response = httpClient.execute(request);	//execute connection
			
			result.code = response.getStatusLine().getStatusCode();

			result.responseMessage = response.getStatusLine().toString();
			
		}
		catch(InterruptedIOException e){	//in case of timeout
			result.code = 504;
			result.responseMessage = "connection timeout exceeded";
			log.error("Error checking host:" + host + ":" + port +" timeout = " + timeout + " error: " + e);
		} 
		catch(ConnectException e){	//in case of refused etc.
			result.code = 403;
			result.responseMessage = "Connection refused";
			log.error("Error checking host:" + host + ":" + port +" timeout = " + timeout + " error: " + e);
		}
		catch(UnknownHostException e){	//in case of unreachable (connection problem)
			result.code = 500;
			result.responseMessage = "Host is unreachable";
			log.error("Error checking host:" + host + ":" + port +" timeout = " + timeout + " error: " + e);
		}
		catch(SocketException e){	//in case of unreachable (connection problem)
			result.code = 500;
			result.responseMessage = "Network is unreachable";
			log.error("Error checking host:" + host + ":" + port +" timeout = " + timeout + " error: " + e);
		}
		catch (Exception e) { 	//write results anyway
			result.code = 500;
			result.responseMessage = "Exception caught while connecting";
			log.error("Error checking host:" + host + ":" + port +" timeout = " + timeout + " error: " + e);
		}
		return result;
	}
}
