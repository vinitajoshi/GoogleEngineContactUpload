package bulkContactsImport;

import java.util.List;
import java.util.Map;

public class ContactImportResponse {
	private String name;
	private String updateRule;
	private Map<String,String> fields;
	private String identifierFieldName;
	private List<Map<String, String>> syncActions;
	private boolean isSyncTriggeredOnImport;
	private int secondsToRetainData;
	private String uri;
	private String createdBy;
	private String createdAt;
	private String updatedBy;
	private String updatedAt;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpdateRule() {
		return updateRule;
	}
	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	public String getIdentifierFieldName() {
		return identifierFieldName;
	}
	public void setIdentifierFieldName(String identifierFieldName) {
		this.identifierFieldName = identifierFieldName;
	}
	public List<Map<String, String>> getSyncActions() {
		return syncActions;
	}
	public void setSyncActions(List<Map<String, String>> syncActions) {
		this.syncActions = syncActions;
	}
	public boolean isSyncTriggeredOnImport() {
		return isSyncTriggeredOnImport;
	}
	public void setSyncTriggeredOnImport(boolean isSyncTriggeredOnImport) {
		this.isSyncTriggeredOnImport = isSyncTriggeredOnImport;
	}
	public int getSecondsToRetainData() {
		return secondsToRetainData;
	}
	public void setSecondsToRetainData(int secondsToRetainData) {
		this.secondsToRetainData = secondsToRetainData;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
