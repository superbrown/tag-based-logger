package com.superbrown.tagbasedlogger;

/**
 * @author Mike Brown
 */
public interface TagBasedLogManager
{
	/**
	 * Returns a logger initialized with the specified minimal tags (optional).
	 * @param tagsToBeAppliedToAllLogging
	 * @return a logger initialized with the specified minimal tags
	 */
	TagBasedLogger getLogger(Object... tagsToBeAppliedToAllLogging);
}
