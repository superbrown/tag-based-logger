package com.superbrown.tagbasedlogger;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class MockAppender implements Appender
{
	public LoggingEvent loggingEvent;

	public String name;

	public MockAppender(String name) {
		this.name = name;
	}


	public void doAppend(LoggingEvent event) { this.loggingEvent = event; }

	public void addFilter(Filter newFilter) {}

	public Filter getFilter() {return null;}

	public void clearFilters() {}

	public void close() {}

	public String getName() {return null;}

	public void setErrorHandler(ErrorHandler errorHandler) {}

	public ErrorHandler getErrorHandler() {return null;}

	public void setLayout(Layout layout) {}

	public Layout getLayout() {return null;}

	public void setName(String name) {}

	public boolean requiresLayout() {return false;}


	public String toString() {
		return name;
	}
}
