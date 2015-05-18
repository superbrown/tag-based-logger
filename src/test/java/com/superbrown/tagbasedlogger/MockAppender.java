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

	@Override
	public void doAppend(LoggingEvent event) { this.loggingEvent = event; }
	@Override
	public void addFilter(Filter newFilter) {}
	@Override
	public Filter getFilter() {return null;}
	@Override
	public void clearFilters() {}
	@Override
	public void close() {}
	@Override
	public String getName() {return null;}
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {}
	@Override
	public ErrorHandler getErrorHandler() {return null;}
	@Override
	public void setLayout(Layout layout) {}
	@Override
	public Layout getLayout() {return null;}
	@Override
	public void setName(String name) {}
	@Override
	public boolean requiresLayout() {return false;}

	@Override
	public String toString() {
		return name;
	}
}
