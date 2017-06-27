package com.etouch.segment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bulkContactsImport.EloquaApiConnector;
import bulkContactsImport.Response;

/**
 * Servlet implementation class ReadSegmentData
 */
@SuppressWarnings("serial")
@WebServlet("/ReadSegmentData")
public class ReadSegmentData extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ReadSegmentData segmentData = new ReadSegmentData();
		segmentData.getEloquaContacts(response);
	}

	public void getEloquaContacts(HttpServletResponse resp) {
		try {
			SearchResponse<Contact> contactRes = null;
			Gson gson = new Gson();
			int pageNumber = 0;
			int totalCount = 0;
			InputStream app_properties_stream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties");
			Properties app_properties = new Properties();
			app_properties.load(app_properties_stream);
			int pageSize = Integer.parseInt(app_properties.getProperty("PAGE_SIZE"));
			String segmentIds = app_properties.getProperty("SEGMENT_ID");
			String[] splitOfSegmentId = segmentIds.split("\\|");
			for (int i = 0; i < splitOfSegmentId.length; i++) {
				System.out.println("Pagesiz::" + pageSize);
				int segmentId = Integer.parseInt(splitOfSegmentId[i]);
				do {
					try {
						pageNumber++;
						String uri = String.format("/data/contacts/segment/%d?depth=complete&page=%d&count=%d",
								segmentId, 1, pageSize);
						System.out.println("URI:::" + uri);
						Response response = EloquaApiConnector.getRestApi().get(uri);
						// resp.getWriter().println("Eloqua response::"
						// +response.body);
						System.out.println("Eloqua response::" + response.body);
						Type eloquacontactshandlerType = new TypeToken<SearchResponse<Contact>>() {
						}.getType();
						contactRes = gson.fromJson(response.body, eloquacontactshandlerType);

						if (pageNumber == 1) {
							totalCount = contactRes.getTotal();
						}
						if (contactRes.getElements().size() != 0) {

							getEloquaContFieldMapping(contactRes, resp);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} while (totalCount > pageSize
						* pageNumber); /*
										 * This will make sure pulling all the
										 * records depending on firstime API
										 * execution total count
										 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void getEloquaContFieldMapping(SearchResponse<Contact> contResp, HttpServletResponse resp) {
		try {
			Map<String, String> elqCxdMap = getEloquaFieldMap(); /*
																	 * get the
																	 * eloqua
																	 * field
																	 * mapping
																	 */
			List<String> tempCxdContRowList = new ArrayList<String>();
			for (Contact contObj : contResp
					.getElements()) {/*
										 * step1 loop through multiple contact
										 * object
										 */
				List<String> rowValEach = new ArrayList<String>();
				try {/*
						 * this will prevent exception of any single row
						 * aborting entire process
						 */

					List<String> tempRowValStore = new ArrayList<String>();
					if (contObj
							.getFieldValues() != null) {/*
														 * keep the collection
														 * sorted for binary
														 * search one time
														 */
						Collections.sort(contObj.getFieldValues(), new EloquaToCxdFieldComp());
					}
					for (String key : elqCxdMap
							.keySet()) { /*
											 * step2 loop through eloqua field
											 * mapping to get value for each row
											 */
						if (key.equals(
								"id")) {/*
										 * generic fields column value retrieve
										 */
							tempRowValStore.add(String.valueOf((contObj.getId() == 0) ? 0 : contObj.getId()));
						} else if (key.equals("emailAddress")) {
							tempRowValStore.add((contObj.getEmailAddress() == null) ? ""
									: contObj.getEmailAddress()
											.toLowerCase());/*
															 * convert email
															 * address to lower
															 * case during
															 * contat extraction
															 * as per the
															 * requirment
															 */
						} else if (key.equals("firstName")) {
							tempRowValStore.add((contObj.getFirstName() == null) ? ""
									: contObj.getFirstName()); /*
																 * object is
																 * null store
																 * null else
																 * corresponding
																 * value
																 */
						} else if (key.equals("lastName")) {
							tempRowValStore.add((contObj.getLastName() == null) ? "" : contObj.getLastName());
						} else if (key.equals("country")) {
							tempRowValStore.add((contObj.getCountry() == null) ? "" : contObj.getCountry());
						} else if (key.equals("address1")) {
							tempRowValStore.add((contObj.getAddress1() == null) ? "" : contObj.getAddress1());
						} else if (key.equals("address2")) {
							tempRowValStore.add((contObj.getAddress2() == null) ? "" : contObj.getAddress2());
						} else if (key.equals("city")) {
							tempRowValStore.add((contObj.getCity() == null) ? "" : contObj.getCity());
						} else if (key.equals("province")) {
							tempRowValStore.add((contObj.getProvince() == null) ? "" : contObj.getProvince());
						} else if (key.equals("postalCode")) {
							tempRowValStore.add((contObj.getPostalCode() == null) ? "" : contObj.getPostalCode());
						} else if (key.equals("businessPhone")) {
							tempRowValStore.add((contObj.getBusinessPhone() == null) ? "" : contObj.getBusinessPhone());
						} else if (key.equals("title")) {
							tempRowValStore.add((contObj.getTitle() == null) ? "" : contObj.getTitle());
						} else if (isInteger(key)) {/*
													 * if the column integer
													 * then it is custom field
													 */

							/*
							 * Sorting custom fields for current row done in
							 * beginning of the loop now search for the field
							 */
							FieldValue fieldComp = new FieldValue();
							fieldComp.setId(Integer.parseInt(key));
							int serchIndx = Collections.binarySearch(contObj.getFieldValues(), fieldComp,
									new EloquaToCxdFieldComp()); /*
																	 * get the
																	 * index of
																	 * the
																	 * element
																	 * in list
																	 */

							if (serchIndx >= 0) {/*
													 * if field found store the
													 * value
													 */
								FieldValue fieldTemp = contObj.getFieldValues().get(serchIndx);

								tempRowValStore.add((fieldTemp.getValue() == null) ? ""
										: fieldTemp.getValue());/*
																 * store field
																 * value if not
																 * null else
																 * null
																 */

							} else {/*
									 * if negative value then element not found
									 */
								tempRowValStore
										.add(null); /*
													 * which adds null to the
													 * value which not found
													 */
							}
						} else {
							tempRowValStore
									.add(null); /*
												 * which adds null to the column
												 * value which is not either
												 * generic or custom fields
												 */
						}
					}
					rowValEach.addAll(tempRowValStore);

				} catch (Exception e) {
					e.printStackTrace();
				}
				tempCxdContRowList
						.addAll(rowValEach); /*
												 * Add the entire row value to
												 * temporary List
												 */

			}
			System.out.println("tempRowValStore::::::" + tempCxdContRowList);
			if (!tempCxdContRowList.isEmpty()) {
				deleteEloquaContact(tempCxdContRowList, resp);
			}

		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public Map<String, String> getEloquaFieldMap() {
		try {
			/* LinkedHashMap maintains the insertion order while retreiving */
			final Map<String, String> colkeyVal = new LinkedHashMap<String, String>();
			colkeyVal.put("100032", "eloquaId");

			return colkeyVal;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	public void deleteEloquaContact(List<String> contactResSet, HttpServletResponse resp) {

		try {
			for (int i = 0; i < contactResSet.size(); i++) {
				String elqContactId = contactResSet.get(i);
				String contactId = "";
				for (int j = 5; j < elqContactId.length(); j++) {
					// str2="0"+str2;
					// System.out.println("Eloqua_id :"+elqContactId.charAt(j));
					contactId = contactId + elqContactId.charAt(j);
				}
				int finalConatctId = Integer.parseInt(contactId.replaceAll("^0+", ""));
				resp.getWriter().println("deleted Eloqua_id :" + finalConatctId);
				System.out.println("deleted Eloqua_id :" + finalConatctId);
				try {
					EloquaApiConnector.getRestApi().delete(String.format("/data/contact/%d", finalConatctId));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
