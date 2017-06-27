package bulkContactsImport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.etouch.segment.SearchResponse;
import com.etouch.sharedList.Import;
import com.etouch.sharedList.RuleType;
import com.etouch.sharedList.SyncAction;
import com.etouch.sharedList.SyncActionType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ContactSyncResponse {
	SyncResponseBean syncResponseBean;

	public String createContactImportSync(String uri) throws Exception {
		try {
			if (uri.isEmpty() || uri.equalsIgnoreCase("") || uri == null) {
				throw new Exception();
			} else {
				String jsonForSync = "{ \"syncedInstanceUri\" : \"" + uri + "\"}";
				Response syncResponse = EloquaApiConnector.getBulkApi().post("/syncs", jsonForSync);

				Gson gson = new Gson();
				syncResponseBean = gson.fromJson(syncResponse.body, SyncResponseBean.class);
			}

		} catch (Exception e) {
			throw e;
		}
		return syncResponseBean.getUri();
	}

	public String getSyncResultAPIcall(String elqSyncUri, HttpServletResponse resp) {
		Response response = null;
		String returnSyncStatusValue = SyncResponseEnum.ERROR.getValue();
		try {
			if (elqSyncUri.isEmpty() || elqSyncUri.equalsIgnoreCase("") || elqSyncUri == null) {
				throw new Exception();
			} else {
				Gson gson = new Gson();
				response = EloquaApiConnector.getBulkApi().get(elqSyncUri);
				SyncResponseBean syncResponse = gson.fromJson(response.body, SyncResponseBean.class);
				// // resp.getWriter().println("getSyncResultAPIcall
				// response.body:::" + response.body);

				returnSyncStatusValue = (syncResponse.getStatus() != null) ? syncResponse.getStatus().toString().trim()
						: returnSyncStatusValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnSyncStatusValue;
	}

	public void postContactBatchToEloqua(List recordList, HttpServletResponse resp, String elqDestinationUri,
			valueDisplay disaplyJspValue) throws Exception {
		Gson contactsGson = new Gson();

		try {
			// resp.getWriter().println("Input Json::" +
			// contactsGson.toJson(recordList));
			// String elqDestinationUri = "/contacts/imports/2";
			Response response = EloquaApiConnector.getBulkApi().post(elqDestinationUri + "/data",
					contactsGson.toJson(recordList));

			Gson gson = new Gson();
			syncResponseBean = gson.fromJson(response.body, SyncResponseBean.class);
			syncResponseBean.getSyncedInstanceUri();
			syncResponseBean.getStatus();
			String uri = syncResponseBean.getUri();
			if (syncResponseBean.getStatus().equals("pending")) {
				Thread.sleep(1000);
				getSyncResultAPIcall(uri, resp);
				Thread.sleep(1000);
				checkContactImportStatus(uri, disaplyJspValue);
				System.out.println("failed count:" + disaplyJspValue.getFailedCount());
				System.out.println("created count:" + disaplyJspValue.getCreatedCount() + " :::updated::"
						+ disaplyJspValue.getUpdatedCount());
				// disaplyJspValue.setFailedCount(disaplyJspValue.getFailedCount());
				// disaplyJspValue.setSucessCount(disaplyJspValue.getCreatedCount()+disaplyJspValue.getUpdatedCount()+disaplyJspValue.getSucessCount());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in q::" + e.getMessage());
			// resp.getWriter().println("postContactBatchToEloqua error::" +
			// e.getMessage());
		}
	}

	public void postSharedListContactsToEloqua(List recordList, HttpServletResponse resp, String elqDestinationUri,
			valueDisplay setJspValues) throws Exception {
		Gson contactsGson = new Gson();

		try {
			resp.getWriter().println("Input Json::" +
			contactsGson.toJson(recordList));
			// String elqDestinationUri = "/contacts/imports/3";
			Response response = EloquaApiConnector.getBulkApi().post(elqDestinationUri + "/data",
					contactsGson.toJson(recordList));

			resp.getWriter().println("Eloqua response::" + response.body);

			Gson gson = new Gson();
			syncResponseBean = gson.fromJson(response.body, SyncResponseBean.class);
			syncResponseBean.getSyncedInstanceUri();
			syncResponseBean.getStatus();
			String uri = syncResponseBean.getUri();
			if (syncResponseBean.getStatus().equals("pending")) {
				Thread.sleep(1000);
				getSyncResultAPIcall(uri, resp);
				Thread.sleep(1000);
				checkContactImportStatus(uri, setJspValues);
			}

		} catch (Exception e) {
			resp.getWriter().println("postContactBatchToEloqua error::" +
			e.getMessage());
		}
	}

	public String createSharedListBulkStructure(int listId) {
		try {
			ContactImportResponse contImportResp = null;
			String strtDateStr = "";
			Map<String, String> importStruct = getContactImportStructure(); /*
																			 * This
																			 * is
																			 * for
																			 * to
																			 * creating
																			 * import
																			 * structure
																			 */
			Import impCont = new Import();
			List<SyncAction> syncList = new ArrayList<SyncAction>();
			SyncAction syncObj = new SyncAction();
			syncObj.action = SyncActionType.add;
			// syncObj.destination="{{ContactList[1035]}}";//bulk2.0
			// listId=1063;
			// logger.info("listid ...::"+listId);
			syncObj.destination = "{{ContactList[" + listId + "]}}";// bulk2.0
			syncList.add(syncObj);

			impCont.name = "Bulk Import";
			impCont.updateRule = RuleType.always;
			impCont.identifierFieldName = "C_EmailAddress";
			impCont.secondsToRetainData = 3600;// PT1H
			impCont.isSyncTriggeredOnImport = true;
			impCont.syncActions = syncList;
			impCont.fields = importStruct;

			Gson gson = new Gson();
			Response response = EloquaApiConnector.getBulkApi().post("/contacts/imports", gson.toJson(impCont));
			contImportResp = gson.fromJson(response.body, ContactImportResponse.class);
			String importUri = contImportResp.getUri();

			return importUri;
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

	public Map<String, String> getContactImportStructure() {
		try {
			Map<String, String> impStruMap = new HashMap<String, String>();
			impStruMap.put("C_FirstName", "{{Contact.Field(C_FirstName)}}");
			impStruMap.put("C_LastName", "{{Contact.Field(C_LastName)}}");
			impStruMap.put("C_EmailAddress", "{{Contact.Field(C_EmailAddress)}}");
			impStruMap.put("C_Industry1", "{{Contact.Field(C_Industry1)}}");
			impStruMap.put("C_Title", "{{Contact.Field(C_Title)}}");
			impStruMap.put("C_Salutation", "{{Contact.Field(C_Salutation)}}");
			impStruMap.put("C_Address1", "{{Contact.Field(C_Address1)}}");
			impStruMap.put("C_State_Prov", "{{Contact.Field(C_State_Prov)}}");
			impStruMap.put("C_Zip_Postal", "{{Contact.Field(C_Zip_Postal)}}");
			impStruMap.put("C_Country", "{{Contact.Field(C_Country)}}");
			impStruMap.put("C_BusPhone", "{{Contact.Field(C_BusPhone)}}");
			impStruMap.put("C_Company", "{{Contact.Field(C_Company)}}");
			impStruMap.put("C_City", "{{Contact.Field(C_City)}}");
			impStruMap.put("C_linkedIn_Url1", "{{Contact.Field(C_linkedIn_Url1)}}");
			return impStruMap;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}// end method

	public void checkContactImportStatus(String elqSyncUri, valueDisplay disaplyJspValue) {
		try {
			int totCntWarning = 0;
			int contactCreatedCount = 0;
			int contactUpdatedCount = 0;
			disaplyJspValue.setCreatedCount(0);
			disaplyJspValue.setUpdatedCount(0);
			disaplyJspValue.setTotalContactCount(0);
			disaplyJspValue.setWarningCount(0);
			SearchResponse<SyncResponse> syncResp;
			String syncUri = elqSyncUri + "/logs";
			Response response = EloquaApiConnector.getBulkApi().get(syncUri);
			Gson gson = new Gson();
			Type syncResponseType = new TypeToken<SearchResponse<SyncResponse>>() {
			}.getType();
			syncResp = gson.fromJson(response.body, syncResponseType);

			List listValues = syncResp.getItems();
			boolean contactSyncCompletedFlag = false;
			for (int i = 0; i < listValues.size(); i++) {
				String uri = syncResp.getItems().get(i).getSyncUri();
				String message = syncResp.getItems().get(i).getMessage();
				int count = syncResp.getItems().get(i).getCount();
				String sevirity = syncResp.getItems().get(i).getSeverity();
				if (sevirity.equals("warning")) {
					totCntWarning = syncResp.getItems().get(i).getCount();
					disaplyJspValue.setWarningCount(totCntWarning);
				}
				if (message != null && message.equalsIgnoreCase("Contacts created.")) {
					contactSyncCompletedFlag = true;
					contactCreatedCount = syncResp.getItems().get(i).getCount();
					disaplyJspValue.setCreatedCount(contactCreatedCount);
				}
				if (message != null && message.equalsIgnoreCase("Contacts updated.")) {
					contactSyncCompletedFlag = true;
					contactUpdatedCount = syncResp.getItems().get(i).getCount();
					disaplyJspValue.setUpdatedCount(contactUpdatedCount);
				}

			}
			disaplyJspValue.setTotalContactCount(disaplyJspValue.getCreatedCount() + disaplyJspValue.getUpdatedCount());
		} catch (Exception e) {/*
								 * dont throw error as one record fails it
								 * shouldnt effect other process
								 */
			e.printStackTrace();
			// throw new RuntimeException();
		}

	}// end method
}
