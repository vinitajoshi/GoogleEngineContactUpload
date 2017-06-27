package bulkContactsImport;

public class valueDisplay {
private String fileName;
private int sucessCount;
private int createdCount;
private int updatedCount;
private int failedCount;
private long schedExecTime;
private int totalRecords;
private int warningCount;
private int totalContactCount;
private String sharedListName;
private int shardListPresent;
private String bucketName;
private String exclsvfileName;


public String getExclsvfileName() {
	return exclsvfileName;
}
public void setExclsvfileName(String exclsvfileName) {
	this.exclsvfileName = exclsvfileName;
}
public String getBucketName() {
	return bucketName;
}
public void setBucketName(String bucketName) {
	this.bucketName = bucketName;
}
public int getShardListPresent() {
	return shardListPresent;
}
public void setShardListPresent(int shardListPresent) {
	this.shardListPresent = shardListPresent;
}
public String getSharedListName() {
	return sharedListName;
}
public void setSharedListName(String sharedListName) {
	this.sharedListName = sharedListName;
}
public int getTotalContactCount() {
	return totalContactCount;
}
public void setTotalContactCount(int totalContactCount) {
	this.totalContactCount = totalContactCount;
}
public int getWarningCount() {
	return warningCount;
}
public void setWarningCount(int warningCount) {
	this.warningCount = warningCount;
}
public int getTotalRecords() {
	return totalRecords;
}
public void setTotalRecords(int totalRecords) {
	this.totalRecords = totalRecords;
}
public int getCreatedCount() {
	return createdCount;
}
public void setCreatedCount(int createdCount) {
	this.createdCount = createdCount;
}
public int getUpdatedCount() {
	return updatedCount;
}
public void setUpdatedCount(int updatedCount) {
	this.updatedCount = updatedCount;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public int getSucessCount() {
	return sucessCount;
}
public void setSucessCount(int sucessCount) {
	this.sucessCount = sucessCount;
}
public int getFailedCount() {
	return failedCount;
}
public void setFailedCount(int failedCount) {
	this.failedCount = failedCount;
}
public long getSchedExecTime() {
	return schedExecTime;
}
public void setSchedExecTime(long schedExecTime2) {
	this.schedExecTime = schedExecTime2;
}


}
