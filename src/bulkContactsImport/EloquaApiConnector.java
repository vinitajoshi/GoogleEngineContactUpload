package bulkContactsImport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EloquaApiConnector {

	private static final EloquaApiConnector singletonObj = new EloquaApiConnector();

	private static EloquaClient bulkApi;
	private static EloquaClient restApi;
	private static String bulkApiUrl;
	private static String restApiUrl;

	private static String proxyUrl;
	private static String authToken;
	private static String elqProtocal;
	private static String elqHost;
	private static String proxyHost;
	private static int proxyPort;
	private static String proxyProtocal;

	private static Properties props = new Properties();

	/* Private constructor */
	private EloquaApiConnector() {
		if (EloquaApiConnector.singletonObj != null) {
			throw new InstantiationError("Creating of this object is not allowed.");
		}
	}

	/* to initiate the static members on class load */
	static {
		// setAuthToken(ConfigPropertyManager.getInstance().getProperties().getProperty("auth"));
		// setProxyHost(ConfigPropertyManager.getInstance().getProperties().getProperty("proxy.host"));
		// setProxyPort(Integer.parseInt(ConfigPropertyManager.getInstance().getProperties().getProperty("proxy.port")));
		// setBulkApiUrl(ConfigPropertyManager.getInstance().getProperties().getProperty("bulk_uri"));
		// setRestApiUrl(ConfigPropertyManager.getInstance().getProperties().getProperty("rest_uri"));
		// setBulkApi(new EloquaClient(getAuthToken(), getBulkApiUrl(),
		// getProxyHost(), getProxyPort()));
		// setRestApi(new EloquaClient(getAuthToken(), getRestApiUrl(),
		// getProxyHost(), getProxyPort()));
		InputStream app_properties_stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("config.properties");
		Properties app_properties = new Properties();
		try{
		app_properties.load(app_properties_stream);
		}catch(IOException ex){
			System.out.println("error in static::"+ex.getMessage());
			ex.printStackTrace();
		}
		String rest_uri =app_properties.getProperty("rest_uri");
		String bulk_uri = app_properties.getProperty("bulk_uri");
		String AuthToken = app_properties.getProperty("authToken");
//		String rest_uri = "https://secure.p04.eloqua.com/api/Rest/2.0";
//		String bulk_uri = "https://secure.p04.eloqua.com/api/Bulk/2.0";
//		String AuthToken = "Basic TVNJSVNhbmRib3hcTWFya2V0aW5nLmVUb3VjaDplVG91Y2glMjAxNg==";//stage
//		String AuthToken = "Basic TVNJSVxNYXJrZXRpbmcuZVRvdWNoOk1rdEB1dG9tQHQxb24=";//Production
		setAuthToken(AuthToken);
		setBulkApiUrl(bulk_uri);
		setRestApiUrl(rest_uri);
		setBulkApi(new EloquaClient(getAuthToken(), getBulkApiUrl(), getProxyHost(), getProxyPort()));
		setRestApi(new EloquaClient(getAuthToken(), getRestApiUrl(), getProxyHost(), getProxyPort()));
	}

	public static String getRestApiUrl() {
		return restApiUrl;
	}

	public static void setRestApiUrl(String restApiUrl) {
		EloquaApiConnector.restApiUrl = restApiUrl;
	}

	public static EloquaClient getBulkApi() {
		return bulkApi;
	}

	public static void setBulkApi(EloquaClient bulkApi) {
		EloquaApiConnector.bulkApi = bulkApi;
	}

	public static EloquaClient getRestApi() {
		return restApi;
	}

	public static void setRestApi(EloquaClient restApi) {
		EloquaApiConnector.restApi = restApi;
	}

	public static String getBulkApiUrl() {
		return bulkApiUrl;
	}

	public static void setBulkApiUrl(String bulkApiUrl) {
		EloquaApiConnector.bulkApiUrl = bulkApiUrl;
	}

	public static String getProxyUrl() {
		return proxyUrl;
	}

	public static void setProxyUrl(String proxyUrl) {
		EloquaApiConnector.proxyUrl = proxyUrl;
	}

	public static String getAuthToken() {
		return authToken;
	}

	public static void setAuthToken(String authToken) {
		EloquaApiConnector.authToken = authToken;
	}

	public static String getElqProtocal() {
		return elqProtocal;
	}

	public static void setElqProtocal(String elqProtocal) {
		EloquaApiConnector.elqProtocal = elqProtocal;
	}

	public static String getElqHost() {
		return elqHost;
	}

	public static void setElqHost(String elqHost) {
		EloquaApiConnector.elqHost = elqHost;
	}

	public static String getProxyHost() {
		return proxyHost;
	}

	public static void setProxyHost(String proxyHost) {
		EloquaApiConnector.proxyHost = proxyHost;
	}

	public static int getProxyPort() {
		return proxyPort;
	}

	public static void setProxyPort(int proxyPort) {
		EloquaApiConnector.proxyPort = proxyPort;
	}

	public static String getProxyProtocal() {
		return proxyProtocal;
	}

	public static void setProxyProtocal(String proxyProtocal) {
		EloquaApiConnector.proxyProtocal = proxyProtocal;
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		EloquaApiConnector.props = props;
	}

}
