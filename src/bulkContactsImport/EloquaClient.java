package bulkContactsImport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class EloquaClient {
	private String _authToken;
	private String _baseUrl;
	private int _conTimeOut;
	private String clientProxyUrl;
	private int clientProxyPort;

	private static final Logger logger = Logger.getLogger(EloquaClient.class.getName());

	public EloquaClient(String authToken, String url, String proxyUrl, int proxyPort) {
		_baseUrl = url;
		clientProxyUrl = proxyUrl;
		clientProxyPort = proxyPort;
		_authToken = authToken;

		setConTimeOut(300); /*
							 * Assign default connection time out in minutes,
							 * this can be re-assigned from within the variable
							 * visibility
							 */
	}

	public Response get(String uri) throws Exception {
		return this.execute(uri, MethodEnum.GET, null);
	}

	public Response post(String uri, String body) throws Exception {
		return this.execute(uri, MethodEnum.POST, body);
	}

	public Response put(String uri, String body) throws Exception {
		return this.execute(uri, MethodEnum.PUT, body);
	}

	public void delete(String uri) throws Exception {
		this.execute(uri, MethodEnum.DELETE, null);
	}

	public Response execute(String uri, MethodEnum method, String body) throws Exception {
		Response response = new Response();

		try {
			URL url = new URL(_baseUrl + uri);
			// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
			// InetSocketAddress(clientProxyUrl, clientProxyPort));
			HttpURLConnection conn = null;
			// if (clientProxyUrl != null && !clientProxyUrl.isEmpty()) {
			// conn = (HttpURLConnection) url.openConnection(proxy);
			// } else {
			conn = (HttpURLConnection) url.openConnection();
			// }
			// conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(method.toString());
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", _authToken);
			if (method == MethodEnum.POST || method == MethodEnum.PUT) {
				conn.setDoOutput(true);
				final OutputStream os = conn.getOutputStream();
				os.write(body.getBytes());
				os.flush();
				os.close();
			}

			InputStream is = conn.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = rd.readLine()) != null) {
				response.body += line;
			}
			rd.close();
			/* header details */
			for (Map.Entry<String, List<String>> k : conn.getHeaderFields().entrySet()) {
				for (String v : k.getValue()) {
					response.header = response.header + k.getKey() + " : " + v + "\n";
				}
			}
			response.statusCode = conn.getResponseCode();
			if ((response.statusCode < 200 || response.statusCode >= 300)) {
				throw new RuntimeException(response.body);
			}

			conn.disconnect();
		} catch (Exception e) {
			logger.info("Eloqua Client Server Error : " + e.getMessage(), e);
			response.exception = e.getMessage();
			throw e;
		}
		return response;
	}

	public int getConTimeOut() {
		return _conTimeOut;
	}

	/* without access modifier restrict the access only to package level */
	void setConTimeOut(int _conTimeOut) {
		this._conTimeOut = _conTimeOut;
	}

}