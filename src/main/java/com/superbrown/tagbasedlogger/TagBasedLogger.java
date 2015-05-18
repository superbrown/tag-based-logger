package com.superbrown.tagbasedlogger;

import java.util.Set;


/**
 * <p>Implementations of this interface provide a facade around a logging framework that allows log
 * entry calls to specify zero or more "tags" for use in categorizing the entry as well as the
 * manner in which it should be logged.  The primary idea is to make it easier later to locate log
 * entries of interest as well as provide a way to designate special handling.</p>
 * 
 * <p>A developer may find it helpful to designate multiple tags for any particular log entry, since
 * many log entries are logically related to multiple things.</p>
 * 
 * <p>As an added benefit, this approach makes it easy to have special handling for logging events
 * using tags as switches.  For instance, one could have a tag to indicate support personnel should
 * be notified and configure the logging framework to send out an e-mail message whenever the tag is
 * encountered.</p>
 * 
 * <p>Here's an example of how to use the logger, followed by an explanation.
 * </p>
 *
 * <p>
 * <pre>{@code
 * 
 * TagBasedLogger log = Log4jTagBasedLogManager.getLogger(ConnectionPoolStatusChecker.class,
 * 	                                                    "STATUS_CHECKER", 
 * 	                                                    "CONNECTION_POOL");
 * 	
 * try {
 * 	
 *     getConnection();
 * 	    
 * }
 * catch (ConnectionUnavailableException e) {
 *
 *     log.error("Connection can't be acquired.", 
 *               e, 
 *               "SYSTEM_OUTAGE", "NOTIFY_PRODUCTION_SUPPORT");
 * }
 * 
 * } </pre>
 * </p>
 *
 * <p>The arguments to Log4jTagBasedLogManager.getLogger() are optional. They signify one or more
 * tags that will be applied (at a minimum) for each entry made by the logger. You can see that when
 * the logger's error() method was called, two additional tags were specified. In this case, the log
 * entry would be have five tags: STATUS_CHECKER, CONNECTION_POOL, SYSTEM_OUTAGE,
 * NOTIFY_PRODUCTION_SUPPORT and the string output of ConnectionPoolStatusChecker.class.getName().
 *
 * @author Mike Brown
 */
public interface TagBasedLogger {

	/**
	 * Sets the tags to be applied to all entries this logger generates
	 * @param tags
	 */
	void setTagsToBeMinimallyAppliedToAllLogging(Set<String> tags);

	/**
	 * Gets the tags to be applied to all entries this logger generates
	 * @return
	 */
	Set<String> getTagsToBeMinimallyAppliedToAllLogging();

	/**
	 * Logs a message at the FATAL level, with optional tags.
	 * 
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void fatal(Object message, Object... tags);

	/**
	 * Logs a message at the FATAL level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void fatal(Object message, Throwable e, Object... tags);

	/**
	 * Logs a message at the ERROR level, with optional tags.
	 *
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void error(Object message, Object... tags);

	/**
	 * Logs a message at the FATAL level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void error(Object message, Throwable e, Object... tags);

	/**
	 * Logs a message at the WARN level, with optional tags.
	 *
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void warn(Object message, Object... tags);

	/**
	 * Logs a message at the WARN level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void warn(Object message, Throwable e, Object... tags);

	/**
	 * Logs a message at the INFO level, with optional tags.
	 *
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void info(Object message, Object... tags);

	/**
	 * Logs a message at the INFO level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void info(Object message, Throwable e, Object... tags);

	/**
	 * Logs a message at the DEBUG level, with optional tags.
	 *
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void debug(Object message, Object... tags);

	/**
	 * Logs a message at the DEBUG level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void debug(Object message, Throwable e, Object... tags);

	/**
	 * Logs a message at the TRACE level, with optional tags.
	 *
	 * @param message
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void trace(Object message, Object... tags);

	/**
	 * Logs a message at the TRACE level including the stack trace of the exception, with optional
	 * tags.
	 *
	 * @param message
	 * @param e
	 * @param tags the toString() value of these will be considered each tag's name, with the
	 *             exception of Class.toName()
	 */
	void trace(Object message, Throwable e, Object... tags);

	/**
	 * Returns whether any of the passed in tags, or the tags to be applied to all log entries this
	 * logger generates, is enabled at the ERROR level.
	 * @param tags
	 * @return
	 */
	boolean isErrorEnabled(Object... tags);

	/**
	 * Returns whether any of the passed in tags, or the tags to be applied to all log entries this
	 * logger generates, is enabled at the WARN level.
	 * @param tags
	 * @return
	 */
	boolean isWarnEnabled(Object... tags);

	/**
	 * Returns whether any of the passed in tags, or the tags to be applied to all log entries this
	 * logger generates, is enabled at the INFO level.
	 * @param tags
	 * @return
	 */
	boolean isInfoEnabled(Object... tags);

	/**
	 * Returns whether any of the passed in tags, or the tags to be applied to all log entries this
	 * logger generates, is enabled at the DEBUG level.
	 * @param tags
	 * @return
	 */
	boolean isDebugEnabled(Object... tags);

	/**
	 * Returns whether any of the passed in tags, or the tags to be applied to all log entries this
	 * logger generates, is enabled at the TRACE level.
	 * @param tags
	 * @return
	 */
	boolean isTraceEnabled(Object... tags);
}
