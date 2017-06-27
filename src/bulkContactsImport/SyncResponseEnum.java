package bulkContactsImport;

public enum SyncResponseEnum {

	PENDING("pending"), ACTIVE("active"), SUCCESS("success"), PARTIAL_SUCCESS("partial_success"), WARNING(
			"warning"), ERROR("error"), MINUTES("minutes"), SECONDS("seconds");

	private final String value;

	private SyncResponseEnum(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
