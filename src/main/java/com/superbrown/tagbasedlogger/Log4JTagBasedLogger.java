package com.superbrown.tagbasedlogger;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.util.*;

/**
 * <p>A log4j implementation of a TagBasedLogger, with the added feature of automatically adding
 * source code location (class name, file name and line number) to any entries above a certain
 * threshold level. The default is WARN, but it can be reset using the
 * setThresholdLevelForRecordingSourceCodeLocation() method.</p>
 *
 * <p>The relationship between "tags" and appenders piggy-backs on log4j's mechanism for configuring
 * loggers and appenders. This is because "tags" are essentially implemented as log4j loggers. So
 * configure your loggers as you always have, only using the names of the tags you've used in your
 * code. All of log4j's logger behavior regarding the use of the root logger and logger
 * parent/child relationships as embedded hierarchically in logger names (or tag names) will
 * function as always.</p>
 *
 * <p>If more than one tag is mapped to a particular appender, the appender will create an entry
 * containing each of the tags. However, no duplicate entries will be made. So, for example,
 * if an appender were set up to handle the tags STATUS_CHECKER as well as CONNECTION_POOL, the
 * entry in the log would look something like this:</p>
 *
 * <p>
 * <pre>
 * {@code
 * [[STATUS_CHECKER][CONNECTION_POOL]] Content of log message.
 * }
 * </pre>
 * </p>
 *
 * @author Mike Brown
 */
public class Log4JTagBasedLogger implements TagBasedLogger {

	private Level thresholdLevelForRecordingSourceCodeLocation = Level.WARN;
	/**
	 * Sets the threshold level for automatically recording source code location (class name and
	 * line number) in log entries.  The default is WARN.
	 * @param level
	 */
	public void setThresholdLevelForRecordingSourceCodeLocation(Level level) {
		this.thresholdLevelForRecordingSourceCodeLocation = level;
	}
	public Level getThresholdLevelForRecordingSourceCodeLocation() {
		return thresholdLevelForRecordingSourceCodeLocation;
	}


	private Set<String> tagsToBeMinimallyAppliedToAllLogging = new LinkedHashSet();
	@Override
	public void setTagsToBeMinimallyAppliedToAllLogging(Set<String> tags) {
		this.tagsToBeMinimallyAppliedToAllLogging = tags;
	}
	@Override
	public Set<String> getTagsToBeMinimallyAppliedToAllLogging() {
		return tagsToBeMinimallyAppliedToAllLogging;
	}

	/**
	 * Constructor
	 * @param tagsToBeMinimallyAppliedToAllLogging tags to be minimally applied to all log entries
	 *                                                from this logger
	 */
	public Log4JTagBasedLogger(Object... tagsToBeMinimallyAppliedToAllLogging) {

		Set<String> tagValues = toTagValues(tagsToBeMinimallyAppliedToAllLogging);

		for (String tagValue : tagValues) {

			if (tagValue.equals("")) {
				continue;
			}

			this.tagsToBeMinimallyAppliedToAllLogging.add(tagValue);
		}
	}

	@Override
	public void fatal(Object message, Object... tags) { log(Level.FATAL, message, tags); }
	@Override
	public void error(Object message, Object... tags) { log(Level.ERROR, message, tags); }
	@Override
	public void warn(Object message, Object... tags) { log(Level.WARN, message, tags); }
	@Override
	public void info(Object message, Object... tags) { log(Level.INFO, message, tags); }
	@Override
	public void debug(Object message, Object... tags) { log(Level.DEBUG, message, tags); }
	@Override
	public void trace(Object message, Object... tags) { log(Level.TRACE, message, tags); }

	@Override
	public void fatal(Object message, Throwable e, Object... tags) { log(Level.FATAL, message, e, tags); }
	@Override
	public void error(Object message, Throwable e, Object... tags) { log(Level.ERROR, message, e, tags); }
	@Override
	public void warn(Object message, Throwable e, Object... tags) { log(Level.WARN, message, e, tags); }
	@Override
	public void info(Object message, Throwable e, Object... tags) { log(Level.INFO, message, e, tags); }
	@Override
	public void debug(Object message, Throwable e, Object... tags) { log(Level.DEBUG, message, e, tags); }
	@Override
	public void trace(Object message, Throwable e, Object... tags) { log(Level.TRACE, message, e, tags); }

	public void log(Level level, Object message, Object... tags) {

		log(level, message, null, tags);
	}

	public void log(Level level, Object message, Throwable e, Object... tags) {

		// DESIGN NOTE: If the level is warning or above, we want to capture the class and line
		//              number.  It's a little bit of a performance hit, but if the app is working
		//              well, these should occur infrequently.  The information will serve as a
		//              valuable aid in diagnosing issues.

		if (level.isGreaterOrEqual(thresholdLevelForRecordingSourceCodeLocation)) {

			StackTraceElement loggingCall = getFirstExternalStackTraceElement(getStackTrace());
			String sourceCodeLocation = toString(loggingCall);
			message = "(" + sourceCodeLocation + ") " + message;
		}

		Set<String> tagValues = toTagValues(tags);

		tagValues.addAll(getTagsToBeMinimallyAppliedToAllLogging());

		Set<Logger> enabledLoggers = getEnabledLoggers(level, tagValues);

		Map<Appender, Set<Logger>> mapOfAppendersToLoggers =
				getMapOfAppendersToLoggers(enabledLoggers);

		log(mapOfAppendersToLoggers, level, message, e);
	}

	@Override
	public boolean isErrorEnabled(Object... tags) {
		return isEnabledFor(Level.ERROR, tags);
	}

	@Override
	public boolean isWarnEnabled(Object... tags) {
		return isEnabledFor(Level.WARN, tags);
	}

	@Override
	public boolean isInfoEnabled(Object... tags) {
		return isEnabledFor(Level.INFO, tags);
	}

	@Override
	public boolean isDebugEnabled(Object... tags) {
		return isEnabledFor(Level.DEBUG, tags);
	}

	@Override
	public boolean isTraceEnabled(Object... tags) {
		return isEnabledFor(Level.TRACE, tags);
	}

	private boolean isEnabledFor(Level level, Object... tags) {

		Set<String> tagValues = toTagValues(tags);

		tagValues.addAll(getTagsToBeMinimallyAppliedToAllLogging());

		Set<Logger> enabledLoggers = getEnabledLoggers(level, tagValues);

		return (enabledLoggers.size() > 0);
	}

	protected Set<Logger> getEnabledLoggers(Level level, Set<String> tags) {

		Set<Logger> enabledLoggers = new LinkedHashSet();

		if (tags.size() == 0) {

			Logger rootLogger = Logger.getRootLogger();

			if (rootLogger.isEnabledFor(level)) {

				enabledLoggers.add(rootLogger);
			}
		}
		else {

			for (String tag : tags) {

				Logger logger = getLogger(tag);

				if (logger.isEnabledFor(level))  {

					enabledLoggers.add(logger);
				}
			}
		}

		return enabledLoggers;
	}

	private String toTagStringValue(Object tagObject) {

		String tag;

		if (tagObject instanceof Class) {

			tag = ((Class)tagObject).getName();
		}
		else {

			tag = tagObject.toString().trim();
		}

		return tag;
	}

	protected Map<Appender, Set<Logger>> getMapOfAppendersToLoggers(Set<Logger> loggers) {

		Map<Appender, Set<Logger>> mapOfAppendersToLoggers = new HashMap();

		for (Logger logger : loggers) {

			List<Appender> appenders = getAllAppenders(logger);

			for (Appender appender : appenders)  {

				Set<Logger> loggersUsingAppender = mapOfAppendersToLoggers.get(appender);

				if (loggersUsingAppender == null) {

					loggersUsingAppender = new LinkedHashSet();
					mapOfAppendersToLoggers.put(appender, loggersUsingAppender);
				}

				loggersUsingAppender.add(logger);
			}
		}

		return mapOfAppendersToLoggers;
	}

	protected List<Appender> getAllAppenders(Category logger) {

		List<Appender> appenders = new ArrayList();

		while (logger != null) {

			Enumeration appendersForCategory = logger.getAllAppenders();

			addToList(appenders, appendersForCategory);

			if (logger.getAdditivity() == false) {
				break;
			}

			logger = logger.getParent();
		}

		return appenders;
	}

	private void addToList(List<Appender> appenders, Enumeration appendersToBeAdded) {

		while (appendersToBeAdded.hasMoreElements()) {

			appenders.add((Appender)appendersToBeAdded.nextElement());
		}
	}

	protected void log(
			Map<Appender, Set<Logger>> mapOfAppendersToLoggers,
			Level level,
			Object message,
			Throwable e) {

		for (Appender appender : mapOfAppendersToLoggers.keySet()) {

			Set<Logger> loggers = mapOfAppendersToLoggers.get(appender);
			log(appender, loggers, level, message, e);
		}
	}

	protected void log(
			Appender appender,
			Set<Logger> loggers,
			Level level,
			Object message,
			Throwable e) {

		String stringContainingAllTagNames = createStringContainingAllLoggerNames(loggers);
		long timeStamp = System.currentTimeMillis();
		Category category = new Category(stringContainingAllTagNames) {};

		LoggingEvent loggingEvent = new LoggingEvent(
				category.getName(),
				category,
				timeStamp,
				level,
				message,
				e);

		appender.doAppend(loggingEvent);
	}

	protected String toString(StackTraceElement stackTraceElement) {

		StringBuilder sourceCodeLocation = new StringBuilder();

		sourceCodeLocation.append(stackTraceElement.getClassName());
		sourceCodeLocation.append(".");
		sourceCodeLocation.append(stackTraceElement.getMethodName());
		sourceCodeLocation.append("(");
		sourceCodeLocation.append(stackTraceElement.getFileName());
		sourceCodeLocation.append(":");
		sourceCodeLocation.append(stackTraceElement.getLineNumber());
		sourceCodeLocation.append(")");

		return sourceCodeLocation.toString();
	}

	protected Set<String> toTagValues(Object... tags) {

		Set<String> listOfTagStringValues = new LinkedHashSet();

		for (Object tag : tags) {

			listOfTagStringValues.add(toTagStringValue(tag));
		}

		return listOfTagStringValues;
	}

	// DESIGN NOTE: This method is here merely for ease of stubbing out for unit testing.
	protected StackTraceElement[] getStackTrace() {

		return Thread.currentThread().getStackTrace();
	}

	// DESIGN NOTE: This method is here merely for ease of stubbing out for unit testing.
	protected Logger getLogger(Object tag) {

		return Logger.getLogger(tag.toString());
	}

	protected String createStringContainingAllLoggerNames(Set<Logger> loggers) {

		StringBuilder tagString = new StringBuilder();

		if (loggers.size() == 0) {

			tagString.append("[]");
		}
		else {

			for (Logger logger : loggers) {
				tagString.append("[");
				tagString.append(logger.getName());
				tagString.append("]");
			}
		}

		return tagString.toString();
	}

	protected StackTraceElement getFirstExternalStackTraceElement(StackTraceElement[] stackTrace) {

		String myClassName = this.getClass().getName();

		// DESIGN NOTE: This is beginning with index 1 because index 0 will always contain
		//              java.lang.Thread.getStackTrace()
		for (int i = 1; i < stackTrace.length; i++) {

			StackTraceElement stackTraceElement = stackTrace[i];

			if (stackTraceElement.getClassName().startsWith(myClassName) == false) {

				return stackTraceElement;
			}
		}

		// DESIGN NOTE: This can't happen, because something external *must* be in the stack trace
		//              somewhere.  If it does happen, something must be defective in the code
		//              above.
		throw new RuntimeException("code defect");
	}
}
