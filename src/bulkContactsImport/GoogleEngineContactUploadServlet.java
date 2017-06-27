package bulkContactsImport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

@SuppressWarnings("serial")
public class GoogleEngineContactUploadServlet extends HttpServlet {
	public static String BUCKETNAME;// = "segment_marketing";
	public static String FILENAME;// = "contactsUploadStage.csv";//
									// "Testing/TestingMarketingContacts.csv";
	public static String EXCLUSIVFILENAME;// it is exclusive file name which
											// will replace the contacts from
											// main file.
	public static int totalRecords = 0;
	private static final Logger log = Logger.getLogger(GoogleEngineContactUploadServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		GoogleEngineContactUploadServlet parseCSVFile = new GoogleEngineContactUploadServlet();
		Date startDate = new Date();
		long schedStartTime = System.currentTimeMillis();
		valueDisplay disaplyJspValue = new valueDisplay();
		BUCKETNAME = req.getParameter("bucketName");
		FILENAME = req.getParameter("fileName");
		EXCLUSIVFILENAME = req.getParameter("exclusivFileName");
		parseCSVFile.parseCSVtoBean(resp, disaplyJspValue);
		long schedExecTime = System.currentTimeMillis() - schedStartTime;
		int schedExecTimeInSeconds = (int) ((schedExecTime / 1000) % 60);
		HttpSession session = req.getSession(true);

		List data = new ArrayList<>();
		disaplyJspValue.setFileName(FILENAME);
		disaplyJspValue.setBucketName(BUCKETNAME);
		disaplyJspValue.setTotalRecords(totalRecords);
		disaplyJspValue.setExclsvfileName(EXCLUSIVFILENAME);
		disaplyJspValue.setSucessCount(disaplyJspValue.getSucessCount());
		disaplyJspValue.setFailedCount(disaplyJspValue.getFailedCount());
		disaplyJspValue.setSchedExecTime(schedExecTimeInSeconds);
		data.add(disaplyJspValue);
		req.setAttribute("jspData", data);
		RequestDispatcher dispatcher = req.getRequestDispatcher("./contact.jsp");
		dispatcher.forward(req, resp);

	}

	private void parseCSVtoBean(HttpServletResponse resp, valueDisplay disaplyJspValue) throws IOException {
		try {
			/* Comment this code for run in local */
			GcsService gcsService = GcsServiceFactory.createGcsService();
			GcsFilename readFilename = new GcsFilename(BUCKETNAME, FILENAME);
			GcsInputChannel readChannel = null;
			/* Till Here */

			int status = 0;
			disaplyJspValue.setSucessCount(0);
			disaplyJspValue.setFailedCount(0);

			BufferedReader buffReader = null;
			CsvListReader csvReader;
			BufferedReader exclusvBuffReader = null;
			CsvListReader exclusivCsvReader;
			List<String> fileHeadersList1 = new ArrayList<String>();
			List<String> exclsvFileHeadersList1 = new ArrayList<String>();
			List<Map<String, String>> contactsList = new ArrayList<Map<String, String>>();
			List<List<String>> exclsvContactsList = new ArrayList<List<String>>();
			try {
				/* Comment this code for run in local */
				readChannel = gcsService.openReadChannel(readFilename, 0);
				buffReader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				csvReader = new CsvListReader(buffReader, CsvPreference.STANDARD_PREFERENCE);
				/* Comment Till Here */

				/* unComment for local run */
				// System.out.println("bfr csvReader::");
				// GoogleEngineContactUploadServlet readLocalFile = new
				// GoogleEngineContactUploadServlet();
				// csvReader = readLocalFile.localFileRead(FILENAME);
				// System.out.println("csvReader::"+csvReader);
				/* uncomment above code */

				List<String> header = new ArrayList<String>(csvReader.read());
				InputStream app_properties_stream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties app_properties = new Properties();
				app_properties.load(app_properties_stream);
				for (int j = 0; j < header.size(); j++) {
					fileHeadersList1.add(app_properties.getProperty(header.get(j).replaceAll("[^a-zA-Z0-9]", "")));
				}
				if (!EXCLUSIVFILENAME.isEmpty()) {
					try {
						/* Comment this code for run in local */
						GcsFilename readExclsvFilename = new GcsFilename(BUCKETNAME, EXCLUSIVFILENAME);
						GcsInputChannel readChannel1 = gcsService.openReadChannel(readExclsvFilename, 0);
						exclusvBuffReader = new BufferedReader(Channels.newReader(readChannel1, "UTF8"));
						exclusivCsvReader = new CsvListReader(exclusvBuffReader, CsvPreference.STANDARD_PREFERENCE);
						/* Till Here */

						/* unComment this code for run in local */
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
								// resp.getWriter().println("row::::"+row);
								exclsvContactsList.add(row);
								// resp.getWriter().println("contactsList:::"+contactsList);
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
				// System.out.println("exclsvContactsList
				// size::"+exclsvContactsList.size()+"::exclsvContactsList:::"+exclsvContactsList);
				List<String> rowAsTokens;
				// Read the CSV as List of Maps where each Map represents
				// row data
				Map<String, String> row = null;

				while ((rowAsTokens = csvReader.read()) != null) {
					// resp.getWriter().println("inside while:::");
					if (!EXCLUSIVFILENAME.isEmpty()) {
						for (int j = 0; j < exclsvContactsList.size(); j++) {
							// System.out.println("rowAsTokens::"+rowAsTokens);
							// System.out.println("exclsvContactsList::"+exclsvContactsList.get(j));
							String listData = exclsvContactsList.get(j).toString();
							listData = listData.replaceAll("[\\[\\]]", "");
							// System.out.println("listdata::"+listData);
							// if(rowAsTokens.contains(exclsvContactsList.get(j).values().toString().)){
							if (rowAsTokens.contains(listData)) {
								status = 1;
								break;
							} else {
								status = 0;
							}
						}
					}
					// System.out.println("status::"+status);
					if (status != 1) {
						try {
							row = new HashMap<String, String>();
							for (int i = 0; i < fileHeadersList1.size(); i++) {
								row.put(fileHeadersList1.get(i), rowAsTokens.get(i));
							}
							// resp.getWriter().println("row::::"+row);
							contactsList.add(row);
							// System.out.println("contactsList Size
							// ist:::"+contactsList.size());
						} catch (Exception e) {
							log.info("Exception while:::" + e.getMessage());
							resp.getWriter().println("Exception while:::" + e.getMessage());
						}
					}
				}

				// resp.getWriter().println("value:::" + contactsList.size());
				long maxRowcount = contactsList.size(), numberOfLoops;
				totalRecords = (int) maxRowcount;
				long maxBufferCount = Long.parseLong(app_properties.getProperty("MAX_BUFFER_COUNT"));
				int bufferLimit = Integer.parseInt(app_properties.getProperty("BUFFER_LIMIT"));
				String importUri = app_properties.getProperty("CONTACT_IMPORT_URI");
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
							uploadContacts(start, offset, contactsList, resp, importUri, disaplyJspValue);
						} else {
							uploadContacts(start, offset, contactsList, resp, importUri, disaplyJspValue);
							start = offset;
							offset = offset + bufferLimit;
						}
					} catch (Exception e) {
						log.info("READ in catch11::" + e.getMessage());
						resp.getWriter().println("READ in catch:" + e.getMessage());
					}
				}

			} catch (Exception e) {
				log.info("READ in catch22::" + e.getMessage());
				resp.getWriter().println("READ error:" + e.getMessage());
			} finally {
				if (buffReader != null) {
					buffReader.close();
				}
			}

		} catch (Exception e) {
			log.info("READ in catch55::" + e.getMessage());
			resp.getWriter().println("READ error outside:" + e.getMessage());
		}

	}

	public void uploadContacts(int start, int end, List<Map<String, String>> contactsList, HttpServletResponse resp,
			String importUri, valueDisplay disaplyJspValue) {
		try {
			List contactSubList = contactsList.subList(start, end);
			ContactSyncResponse contactUpload = new ContactSyncResponse();
			contactUpload.postContactBatchToEloqua(contactSubList, resp, importUri, disaplyJspValue);
			disaplyJspValue.setFailedCount(disaplyJspValue.getWarningCount() + disaplyJspValue.getFailedCount());
			disaplyJspValue.setSucessCount(disaplyJspValue.getTotalContactCount() + disaplyJspValue.getSucessCount());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CsvListReader localFileRead(String fileName) {
		System.out.println("file Name inside local::" + fileName);
		String fileReadLocal = "C:/Wilson/project/GoogleEngineContactUpload/src/" + fileName;
		CsvListReader csvReader = null;
		try {
			csvReader = new CsvListReader(new FileReader(fileReadLocal), CsvPreference.STANDARD_PREFERENCE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error msg::" + e.getMessage());
		}
		return csvReader;

	}

}
