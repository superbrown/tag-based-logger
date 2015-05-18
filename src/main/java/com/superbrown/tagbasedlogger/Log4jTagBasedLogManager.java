package com.superbrown.tagbasedlogger;

/**
 * <p>A factory for creating TagBasedLoggers</p>
 * 
 * @author Mike Brown
 */
public class Log4jTagBasedLogManager implements TagBasedLogManager
{
	@Override
	public TagBasedLogger getLogger(Object... tagsToBeAppliedToAllLogging) {

		return new Log4JTagBasedLogger(tagsToBeAppliedToAllLogging);
	}
}
