package bulkContactsImport;

public class SyncResponse {

	private String syncUri;
	private int count;
    private String severity;
    private String message;
	public String getSyncUri() {
		return syncUri;
	}
	public void setSyncUri(String syncUri) {
		this.syncUri = syncUri;
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
    
    
}
