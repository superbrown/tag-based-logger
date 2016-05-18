package com.superbrown.tagbasedlogger;

/**
 * <p>A factory for creating TagBasedLoggers</p>
 * 
 * @author Mike Brown
 */
public class Log4jTagBasedLogManager implements TagBasedLogManager
{
	private String closingTagString;
	private String openingTagString;

	public Log4jTagBasedLogManager() {

		closingTagString = "["; // defaulot
		openingTagString = "]"; // defaulot
	}

	public Log4jTagBasedLogManager(String openingTagString, String closingTagString) {

		this.openingTagString = openingTagString;
		this.closingTagString = closingTagString;
	}

	public TagBasedLogger getLogger(Object... tagsToBeAppliedToAllLogging) {

		Log4JTagBasedLogger log4JTagBasedLogger = new Log4JTagBasedLogger(tagsToBeAppliedToAllLogging);
		return log4JTagBasedLogger;
	}

	public String getOpeningTagString() { return openingTagString; }
	public void setOpeningTagString(String openingTagString) { this.openingTagString = openingTagString; }

	public String getClosingTagString() { return closingTagString; }
	public void setClosingTagString(String closingTagString) { this.closingTagString = closingTagString; }
}
