package com.etouch.sharedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.etouch.segment.SearchResponse;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bulkContactsImport.ContactSyncResponse;
import bulkContactsImport.EloquaApiConnector;
import bulkContactsImport.GoogleEngineContactUploadServlet;
import bulkContactsImport.Response;
import bulkContactsImport.SyncResponse;
import bulkContactsImport.valueDisplay;

/**
 * Servlet implementation class SharedListBulkImport
 */
@SuppressWarnings("serial")
@WebServlet("/SharedListBulkImport")
public class SharedListBulkImport extends HttpServlet {
	public static String BUCKETNAME;// = "segment_marketing";
	public static String FILENAME;// = "contactsUploadStage.csv";//
									// "marketingContacts.csv";
	public static String EXCLUSIVFILENAME;// it is exclusive file name which
											// will replace the contacts from
											// main file.
	public static int totalRecords = 0;
	private static final Logger log = Logger.getLogger(SharedListBulkImport.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long schedStartTime = System.currentTimeMillis();
		SharedListBulkImport sharedListUpload = new SharedListBulkImport();
		valueDisplay setJspValues = new valueDisplay();
		String sharedListName = request.getParameter("shredName");
		BUCKETNAME = request.getParameter("bucketName");
		FILENAME = request.getParameter("fileName");
		EXCLUSIVFILENAME = request.getParameter("exclusivFileName");
		System.out.println("shard name::" + sharedListName);
		setJspValues.setSharedListName(sharedListName);
		if (sharedListName != null) {
			int sharedListId = sharedListUpload.getSharedListName(sharedListName);
			System.out.println("id:::" + sharedListId);
			if (sharedListId != 0) {
				setJspValues.setShardListPresent(1);
				sharedListUpload.uploadSharedList(response, sharedListId, setJspValues);
			} else {
				setJspValues.setShardListPresent(0);
			}
		}
		long schedExecTime = System.currentTimeMillis() - schedStartTime;
		int schedExecTimeInSeconds = (int) ((schedExecTime / 1000) % 60);
		List data = new ArrayList<>();
		setJspValues.setFileName(FILENAME);
		setJspValues.setBucketName(BUCKETNAME);
		setJspValues.setExclsvfileName(EXCLUSIVFILENAME);
		setJspValues.setTotalRecords(totalRecords);
		System.out.println(
				"sucess:" + setJspValues.getUpdatedCount() + "::failed count::" + setJspValues.getFailedCount());
		setJspValues.setSucessCount(setJspValues.getSucessCount());
		setJspValues.setFailedCount(setJspValues.getFailedCount());
		setJspValues.setSchedExecTime(schedExecTimeInSeconds);
		data.add(setJspValues);
		request.setAttribute("shardData", data);
		System.out.println("HI:::");
		RequestDispatcher dispatcher = request.getRequestDispatcher("./sharedList.jsp");
		dispatcher.forward(request, response);
	}

	private void uploadSharedList(HttpServletResponse resp, int sharedListId, valueDisplay setJspValues)
			throws IOException {
		try {
			int status = 0;
			/* comment for local run */
			GcsService gcsService = GcsServiceFactory.createGcsService();
			GcsFilename readFilename = new GcsFilename(BUCKETNAME, FILENAME);
			GcsInputChannel readChannel = null;
			/* Till here comment */
			BufferedReader buffReader = null;
			CsvListReader csvReader;
			ContactSyncResponse sharedListImport = new ContactSyncResponse();
			List<String> fileHeadersList1 = new ArrayList<String>();
			List<Map<String, String>> contactsList = new ArrayList<Map<String, String>>();
			CsvListReader exclusivCsvReader;
			List<String> exclsvFileHeadersList1 = new ArrayList<String>();
			// List<Map<String, String>> exclsvContactsList = new
			// ArrayList<Map<String, String>>();
			List<List<String>> exclsvContactsList = new ArrayList<List<String>>();
			BufferedReader exclusvBuffReader = null;
			try {
				/* comment for local run */
				readChannel = gcsService.openReadChannel(readFilename, 0);
				buffReader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				csvReader = new CsvListReader(buffReader, CsvPreference.STANDARD_PREFERENCE);
				/* comment till here */

				/* uncomment this for local run */
				// GoogleEngineContactUploadServlet readLocalFile = new
				// GoogleEngineContactUploadServlet();
				// csvReader = readLocalFile.localFileRead(FILENAME);
				/* uncomment till here for local run */

				List<String> header = new ArrayList<String>(csvReader.read());
				InputStream app_properties_stream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties app_properties = new Properties();
				app_properties.load(app_properties_stream);
				for (int j = 0; j < header.size(); j++) {
					fileHeadersList1.add(app_properties.getProperty(header.get(j).replaceAll("[^a-zA-Z0-9]", "")));
				}

				if (!EXCLUSIVFILENAME.isEmpty()) {
					System.out.println("inside exclusive file check");
					try {
						/* Comment for local run */
						GcsFilename readExclsvFilename = new GcsFilename(BUCKETNAME, EXCLUSIVFILENAME);
						GcsInputChannel readChannel1 = gcsService.openReadChannel(readExclsvFilename, 0);
						exclusvBuffReader = new BufferedReader(Channels.newReader(readChannel1, "UTF8"));
						exclusivCsvReader = new CsvListReader(exclusvBuffReader, CsvPreference.STANDARD_PREFERENCE);
						/* Till here comment */

						/* unComment for local run */
						// exclusivCsvReader =
						// readLocalFile.localFileRead(EXCLUSIVFILENAME);
						/* uncomment above code */

						List<String> exclusivHeader = new ArrayList<String>(exclusivCsvReader.read());
						InputStream app_properties_stream1 = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream("config.properties");
						Properties app_properties1 = new Properties();
						app_properties1.load(app_properties_stream1);
						for (int j = 0; j < exclusivHeader.size(); j++) {
							exclsvFileHeadersList1.add(
									app_properties1.getProperty(exclusivHeader.get(j).replaceAll("[^a-zA-Z0-9]", "")));
						}
						List<String> rowAsTokens;
						// Read the CSV as List of Maps where each Map
						// represents
						// row data
						// Map<String, String> row = null;
						List<String> row = null;
						while ((rowAsTokens = exclusivCsvReader.read()) != null) {
							// resp.getWriter().println("inside while:::");
							try {
								// row = new HashMap<String, String>();
								row = new ArrayList<String>();
								for (int i = 0; i < exclsvFileHeadersList1.size(); i++) {
									// row.put(exclsvFileHeadersList1.get(i),
									// rowAsTokens.get(i));
									row.add(rowAsTokens.get(i));
								}
								resp.getWriter().println("row::::"+row);
								exclsvContactsList.add(row);
								resp.getWriter().println("contactsList:::"+contactsList);
							} catch (Exception e) {
								log.info("exclusiv" + e.getMessage());
								resp.getWriter().println("Exception while:::" + e.getMessage());
							}

						}
					} catch (Exception e) {
						log.info("exclusiv11:::" + e.getMessage());
						resp.getWriter().println("Exception while:::" + e.getMessage());
					}
				}

				List<String> rowAsTokens;
				// Read the CSV as List of Maps where each Map represents
				// row data
				Map<String, String> row = null;
				while ((rowAsTokens = csvReader.read()) != null) {
					if (!EXCLUSIVFILENAME.isEmpty()) {
						for (int j = 0; j < exclsvContactsList.size(); j++) {
							System.out.println("rowAsTokens::"+rowAsTokens);
							System.out.println("exclsvContactsList::"+exclsvContactsList.get(j));
							String listData = exclsvContactsList.get(j).toString();
							listData = listData.replaceAll("[\\[\\]]", "");
							System.out.println("listdata::"+listData);
							// if(rowAsTokens.contains(exclsvContactsList.get(j).values().toString().)){
							if (rowAsTokens.contains(listData)) {
								status = 1;
								break;
							} else {
								status = 0;
							}
						}
					}
					resp.getWriter().println("inside while:::");
					if (status != 1) {
						try {
							row = new HashMap<String, String>();
							for (int i = 0; i < fileHeadersList1.size(); i++) {
								row.put(fileHeadersList1.get(i), rowAsTokens.get(i));
							}
							resp.getWriter().println("row::::"+row);
							contactsList.add(row);
							resp.getWriter().println("contactsList:::"+contactsList);
						} catch (Exception e) {
							resp.getWriter().println("Exception while:::" + e.getMessage());
						}
					}

				}

				resp.getWriter().println("value:::" + contactsList.size());
				long maxRowcount = contactsList.size(), numberOfLoops;
				totalRecords = (int) maxRowcount;
				long maxBufferCount = Long.parseLong(app_properties.getProperty("MAX_BUFFER_COUNT"));
				int bufferLimit = Integer.parseInt(app_properties.getProperty("BUFFER_LIMIT"));
				String importUri = sharedListImport.createSharedListBulkStructure(sharedListId);
				// String importUri =
				// app_properties.getProperty("SHARED_LIST_IMPORT_URI");
				int start = 0, offset = bufferLimit;
				if ((maxRowcount % bufferLimit) > 0) {
					numberOfLoops = (maxRowcount / bufferLimit)
							+ 1; /*
									 * +1 because it will process total number
									 * of rows including reminder values
									 */
				} else {
					numberOfLoops = (maxRowcount
							/ bufferLimit); /*
											 * if no reminder or maxrows 0 then
											 * loop exactly to quotient value
											 */
				}

				if (numberOfLoops == 0) {

				}

				if (maxRowcount >= maxBufferCount) {
					if ((maxBufferCount % bufferLimit) > 0) {
						numberOfLoops = (maxBufferCount / bufferLimit) + 1;
					} else {
						numberOfLoops = (maxBufferCount / bufferLimit);
					}
				}

				for (long i = 0; i < numberOfLoops; i++) {
					try {
						if (i == (numberOfLoops - 1)) {
							start = offset - bufferLimit;
							offset = contactsList.size();
							uploadContacts(start, offset, contactsList, resp, importUri, setJspValues);
						} else {
							uploadContacts(start, offset, contactsList, resp, importUri, setJspValues);
							start = offset;
							offset = offset + bufferLimit;
						}
					} catch (Exception e) {
						resp.getWriter().println("READ in catch:" + e.getMessage());
					}
				}
			} catch (Exception e) {
				resp.getWriter().println("READ error:" + e.getMessage());
			} finally {
				if (buffReader != null) {
					buffReader.close();
				}
			}

		} catch (Exception e) {
			resp.getWriter().println("READ error outside:" + e.getMessage());
		}

	}

	public void uploadContacts(int start, int end, List<Map<String, String>> contactsList, HttpServletResponse resp,
			String importUri, valueDisplay setJspValues) {
		try {
			List contactSubList = contactsList.subList(start, end);
			ContactSyncResponse contactUpload = new ContactSyncResponse();
			contactUpload.postSharedListContactsToEloqua(contactSubList, resp, importUri, setJspValues);
			setJspValues.setFailedCount(setJspValues.getWarningCount() + setJspValues.getFailedCount());
			setJspValues.setSucessCount(setJspValues.getTotalContactCount() + setJspValues.getSucessCount());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getSharedListName(String sharedName) {
		int id = 0;
		try {
			String uri = "/assets/contact/lists?search=name='" + URLEncoder.encode(sharedName, "UTF-8") + "'";
			// String uri =
			// "/assets/contact/lists?search=name='"+sharedName+"'";
			Response response = EloquaApiConnector.getRestApi().get(uri);
			Gson gson = new Gson();
			SearchResponse<SharedListDetails> listDetails;
			Type syncResponseType = new TypeToken<SearchResponse<SharedListDetails>>() {
			}.getType();
			System.out.println("response::::" + response.body);
			listDetails = gson.fromJson(response.body, syncResponseType);
			if (listDetails.getTotal() > 0) {
				List listValues = listDetails.getElements();
				for (int i = 0; i < listValues.size(); i++) {
					id = listDetails.getElements().get(i).getId();
					System.out.println("id is:::" + id);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}
