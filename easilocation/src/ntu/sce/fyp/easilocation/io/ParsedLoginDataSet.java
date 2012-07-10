package ntu.sce.fyp.easilocation.io;

public class ParsedLoginDataSet {
	private String status = null;
	private String message = null;

	public String getExtractedString() {
		return status;
	}

	public void setExtractedString(String extractedString) {
		this.status = extractedString;
	}

	public void setMessage(String extractedString) {
		this.message = extractedString;
	}

	public String getMessage() {
		return this.message;
	}
}
