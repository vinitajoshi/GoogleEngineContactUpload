package bulkContactsImport;

import java.util.List;

public class SyncResponseBean {

	String syncedInstanceUri;
	String status;
	String createdAt;
	String createdBy;
	String uri;
	int schedulerRunId;
	String elqSyncBatchId;
	String syncBatchStatus;
	String maObjectType;
	int count;
	String severity;
	String message;
	String statusCode;

	@SuppressWarnings("rawtypes")
	private List items;

	public int getSchedulerRunId() {
		return schedulerRunId;
	}

	public void setSchedulerRunId(int schedulerRunId) {
		this.schedulerRunId = schedulerRunId;
	}

	public String getElqSyncBatchId() {
		return elqSyncBatchId;
	}

	public void setElqSyncBatchId(String elqSyncBatchId) {
		this.elqSyncBatchId = elqSyncBatchId;
	}

	public String getSyncBatchStatus() {
		return syncBatchStatus;
	}

	public void setSyncBatchStatus(String syncBatchStatus) {
		this.syncBatchStatus = syncBatchStatus;
	}

	public String getMaObjectType() {
		return maObjectType;
	}

	public void setMaObjectType(String maObjectType) {
		this.maObjectType = maObjectType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public String getSyncedInstanceUri() {
		return syncedInstanceUri;
	}

	public void setSyncedInstanceUri(String syncedInstanceUri) {
		this.syncedInstanceUri = syncedInstanceUri;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "SyncResponseBean [syncedInstanceUri=" + syncedInstanceUri
				+ ", status=" + status + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", uri=" + uri + "]";
	}

}
