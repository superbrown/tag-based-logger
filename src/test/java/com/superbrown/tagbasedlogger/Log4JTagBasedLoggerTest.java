package com.superbrown.tagbasedlogger;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static org.apache.log4j.Level.*;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
public class Log4JTagBasedLoggerTest
{
	Log4jTagBasedLogManager log4jTagBasedLogManager;

	@Before
	public void before() throws Exception {

		log4jTagBasedLogManager = new Log4jTagBasedLogManager();
	}

	@After
	public void after() throws Exception {

	}

	@Test
	public void testConstructorThatTakesTags() {

		TagBasedLogger tagBasedLogger = log4jTagBasedLogManager.getLogger();

		Set<String> tags = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();
		assertTrue(tags.size() == 0);

		
		tagBasedLogger = log4jTagBasedLogManager.getLogger("a");
		tags = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();

		assertTrue(tags.size() == 1);
		assertTrue(tags.contains("a"));


		tagBasedLogger = log4jTagBasedLogManager.getLogger("a", "b");
		tags = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();

		assertTrue(tags.size() == 2);
		assertTrue(tags.contains("a"));
		assertTrue(tags.contains("b"));

		tagBasedLogger = log4jTagBasedLogManager.getLogger("", " ", "a ", "  ", "b", "  ", " c");
		tags = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();

		assertTrue(tags.size() == 3);
		assertTrue(tags.contains("a"));
		assertTrue(tags.contains("b"));
		assertTrue(tags.contains("c"));
	}

	@Test
	public void testGetAppenders() {

		Logger logger_root = Logger.getRootLogger();
		Logger logger_a = Logger.getLogger("a");
		Logger logger_ab = Logger.getLogger("a.b");
		Logger logger_abc = Logger.getLogger("a.b.c");
		Logger logger_abcd = Logger.getLogger("a.b.c.d");

		logger_root.removeAllAppenders();
		logger_a.removeAllAppenders();
		logger_ab.removeAllAppenders();
		logger_abc.removeAllAppenders();
		logger_abcd.removeAllAppenders();

		Appender appender_root = Mockito.mock(Appender.class);
		Appender appender_a = Mockito.mock(Appender.class);
		Appender appender_ab = Mockito.mock(Appender.class);
		Appender appender_abc = Mockito.mock(Appender.class);
		Appender appender_abcd = Mockito.mock(Appender.class);

		logger_root.addAppender(appender_root);
		logger_abcd.addAppender(appender_abcd);
		logger_abc.addAppender(appender_abc);
		logger_ab.addAppender(appender_ab);
		logger_a.addAppender(appender_a);

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(true);
		logger_abc.setAdditivity(true);
		logger_abcd.setAdditivity(true);

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		List<Appender> appenders = tagBasedLogger.getAllAppenders(logger_abcd);
		assertTrue(appenders.size() == 5);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));
		assertTrue(appenders.contains(appender_abc));
		assertTrue(appenders.contains(appender_abcd));

		appenders = tagBasedLogger.getAllAppenders(logger_abc);
		assertTrue(appenders.size() == 4);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));
		assertTrue(appenders.contains(appender_abc));

		appenders = tagBasedLogger.getAllAppenders(logger_ab);
		assertTrue(appenders.size() == 3);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));

		appenders = tagBasedLogger.getAllAppenders(logger_a);
		assertTrue(appenders.size() == 2);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(true);
		logger_abc.setAdditivity(true);
		logger_abcd.setAdditivity(false);

		appenders = tagBasedLogger.getAllAppenders(logger_abcd);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abcd));

		appenders = tagBasedLogger.getAllAppenders(logger_abc);
		assertTrue(appenders.size() == 4);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));
		assertTrue(appenders.contains(appender_abc));

		appenders = tagBasedLogger.getAllAppenders(logger_ab);
		assertTrue(appenders.size() == 3);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));

		appenders = tagBasedLogger.getAllAppenders(logger_a);
		assertTrue(appenders.size() == 2);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(true);
		logger_abc.setAdditivity(false);
		logger_abcd.setAdditivity(false);

		appenders = tagBasedLogger.getAllAppenders(logger_abcd);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abcd));

		appenders = tagBasedLogger.getAllAppenders(logger_abc);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abc));

		appenders = tagBasedLogger.getAllAppenders(logger_ab);
		assertTrue(appenders.size() == 3);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));
		assertTrue(appenders.contains(appender_ab));

		appenders = tagBasedLogger.getAllAppenders(logger_a);
		assertTrue(appenders.size() == 2);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(false);
		logger_abc.setAdditivity(false);
		logger_abcd.setAdditivity(false);

		appenders = tagBasedLogger.getAllAppenders(logger_abcd);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abcd));

		appenders = tagBasedLogger.getAllAppenders(logger_abc);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abc));

		appenders = tagBasedLogger.getAllAppenders(logger_ab);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_ab));

		appenders = tagBasedLogger.getAllAppenders(logger_a);
		assertTrue(appenders.size() == 2);
		assertTrue(appenders.contains(appender_root));
		assertTrue(appenders.contains(appender_a));

		logger_a.setAdditivity(false);
		logger_ab.setAdditivity(false);
		logger_abc.setAdditivity(false);
		logger_abcd.setAdditivity(false);

		appenders = tagBasedLogger.getAllAppenders(logger_abcd);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abcd));

		appenders = tagBasedLogger.getAllAppenders(logger_abc);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_abc));

		appenders = tagBasedLogger.getAllAppenders(logger_ab);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_ab));

		appenders = tagBasedLogger.getAllAppenders(logger_a);
		assertTrue(appenders.size() == 1);
		assertTrue(appenders.contains(appender_a));
	}
	
	@Test
	public void testAppendSourceCodeLocationIfAppropriate() {

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		StackTraceElement stackTraceElement = 
				new StackTraceElement("com.packageName.ClassName", "methodName", "ClassName.java", 100);
		
		String result = tagBasedLogger.toString(stackTraceElement);

		assertTrue(result.equals("com.packageName.ClassName.methodName(ClassName.java:100)"));
	}

	@Test
	@PrepareForTest({Logger.class})
	public void testGetMapOfAppendersToLoggers() throws Exception {

		Appender appender01 = createMockAppender("A1");
		Appender appender02 = createMockAppender("A2");
		Appender appender03 = createMockAppender("A3");
		Appender appender04 = createMockAppender("A4");

		Logger mockLogger_001 = createMockLogger("L1");
		Logger mockLogger_002 = createMockLogger("L2");
		Logger mockLogger_003 = createMockLogger("L3");
		Logger mockLogger_004 = createMockLogger("L4");
		Logger mockLogger_005 = createMockLogger("L5");
		Logger mockLogger_006 = createMockLogger("L6");
		Logger mockLogger_007 = createMockLogger("L7");
		Logger mockLogger_008 = createMockLogger("L8");
		Logger mockLogger_009 = createMockLogger("L9");

		when(mockLogger_001.getAllAppenders()).thenReturn(createAppenderEnumeration(appender01, appender02));
		when(mockLogger_002.getAllAppenders()).thenReturn(createAppenderEnumeration(appender01, appender02));
		when(mockLogger_003.getAllAppenders()).thenReturn(createAppenderEnumeration(appender01, appender02));
		when(mockLogger_004.getAllAppenders()).thenReturn(createAppenderEnumeration(appender02));
		when(mockLogger_005.getAllAppenders()).thenReturn(createAppenderEnumeration(appender01, appender02, appender03, appender04));
		when(mockLogger_006.getAllAppenders()).thenReturn(createAppenderEnumeration());
		when(mockLogger_007.getAllAppenders()).thenReturn(createAppenderEnumeration(appender02, appender04));
		when(mockLogger_008.getAllAppenders()).thenReturn(createAppenderEnumeration(appender02, appender04));
		when(mockLogger_009.getAllAppenders()).thenReturn(createAppenderEnumeration(appender03, appender03));

		Set<Logger> loggers = new HashSet();
		loggers.add(mockLogger_001);
		loggers.add(mockLogger_002);
		loggers.add(mockLogger_003);
		loggers.add(mockLogger_004);
		loggers.add(mockLogger_005);
		loggers.add(mockLogger_006);
		loggers.add(mockLogger_007);
		loggers.add(mockLogger_008);
		loggers.add(mockLogger_009);

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		Map<Appender, Set<Logger>> mapOfAppendersToLoggers = tagBasedLogger.getMapOfAppendersToLoggers(loggers);

		assertTrue(mapOfAppendersToLoggers.keySet().size() == 4);

		Set<Logger> loggersForAppender1 = mapOfAppendersToLoggers.get(appender01);
		Set<Logger> loggersForAppender2 = mapOfAppendersToLoggers.get(appender02);
		Set<Logger> loggersForAppender3 = mapOfAppendersToLoggers.get(appender03);
		Set<Logger> loggersForAppender4 = mapOfAppendersToLoggers.get(appender04);

		assertTrue(loggersForAppender1.size() == 4);
		assertTrue(loggersForAppender1.contains(mockLogger_001));
		assertTrue(loggersForAppender1.contains(mockLogger_002));
		assertTrue(loggersForAppender1.contains(mockLogger_003));
		assertTrue(loggersForAppender1.contains(mockLogger_005));

		assertTrue(loggersForAppender2.size() == 7);
		assertTrue(loggersForAppender2.contains(mockLogger_001));
		assertTrue(loggersForAppender2.contains(mockLogger_002));
		assertTrue(loggersForAppender2.contains(mockLogger_003));
		assertTrue(loggersForAppender2.contains(mockLogger_004));
		assertTrue(loggersForAppender2.contains(mockLogger_005));
		assertTrue(loggersForAppender2.contains(mockLogger_007));
		assertTrue(loggersForAppender2.contains(mockLogger_008));

		assertTrue(loggersForAppender3.size() == 2);
		assertTrue(loggersForAppender3.contains(mockLogger_005));
		assertTrue(loggersForAppender3.contains(mockLogger_009));

		assertTrue(loggersForAppender4.size() == 3);
		assertTrue(loggersForAppender4.contains(mockLogger_005));
		assertTrue(loggersForAppender4.contains(mockLogger_007));
		assertTrue(loggersForAppender4.contains(mockLogger_008));
	}

	@Test
	@PrepareForTest({Logger.class})
	public void testCreateStringContainingAllTagNames() throws Exception {

		String name01 = "L1";
		String name02 = "L2";
		String name03 = "L3";

		Logger mockLogger_001 = createMockLogger(name01);
		Logger mockLogger_002 = createMockLogger(name02);
		Logger mockLogger_003 = createMockLogger(name03);

		Set<Logger> loggers = new LinkedHashSet();
		loggers.add(mockLogger_001);
		loggers.add(mockLogger_002);
		loggers.add(mockLogger_003);

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		String returnValue = tagBasedLogger.createStringContainingAllLoggerNames(loggers);
		assertTrue(returnValue.equals("[" + name01 + "][" + name02 + "][" + name03 + "]"));

		loggers.clear();
		returnValue = tagBasedLogger.createStringContainingAllLoggerNames(loggers);
		assertTrue(returnValue.equals("[]"));
	}

	@Test
	@PrepareForTest({Logger.class})
	public void testGetLoggersThatAreEnabledForLevel() {

		final Logger logger_fatal = createMockLogger("fatal", FATAL);
		final Logger logger_error = createMockLogger("error", ERROR);
		final Logger logger_warn = createMockLogger("warn", WARN);
		final Logger logger_info = createMockLogger("info", INFO);
		final Logger logger_debug = createMockLogger("debug", DEBUG);
		final Logger logger_trace = createMockLogger("trace", TRACE);

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger() {
			@Override
			protected Logger getLogger(Object tag) {
				if (tag.equals(logger_fatal.getName())) { return logger_fatal; }
				else
				if (tag.equals(logger_error.getName())) { return logger_error; }
				else
				if (tag.equals(logger_warn.getName())) { return logger_warn; }
				else
				if (tag.equals(logger_info.getName())) { return logger_info; }
				else
				if (tag.equals(logger_debug.getName())) { return logger_debug; }
				else
				if (tag.equals(logger_trace.getName())) { return logger_trace; }
				else
					throw new RuntimeException();
			}
		};
		
		Set<String> tags = new HashSet();
		
		tags.add(logger_fatal.getName());
		tags.add(logger_error.getName());
		tags.add(logger_warn.getName());
		tags.add(logger_info.getName());
		tags.add(logger_debug.getName());
		tags.add(logger_trace.getName());						
		
		Set<Logger> enabledLoggers = tagBasedLogger.getEnabledLoggers(FATAL, tags);

		assertTrue(enabledLoggers.size() == 6);
		assertTrue(enabledLoggers.contains(logger_fatal));
		assertTrue(enabledLoggers.contains(logger_error));
		assertTrue(enabledLoggers.contains(logger_warn));
		assertTrue(enabledLoggers.contains(logger_info));
		assertTrue(enabledLoggers.contains(logger_debug));
		assertTrue(enabledLoggers.contains(logger_trace));

		enabledLoggers = tagBasedLogger.getEnabledLoggers(ERROR, tags);

		assertTrue(enabledLoggers.size() == 5);
		assertTrue(enabledLoggers.contains(logger_error));
		assertTrue(enabledLoggers.contains(logger_warn));
		assertTrue(enabledLoggers.contains(logger_info));
		assertTrue(enabledLoggers.contains(logger_debug));
		assertTrue(enabledLoggers.contains(logger_trace));

		enabledLoggers = tagBasedLogger.getEnabledLoggers(WARN, tags);

		assertTrue(enabledLoggers.size() == 4);
		assertTrue(enabledLoggers.contains(logger_warn));
		assertTrue(enabledLoggers.contains(logger_info));
		assertTrue(enabledLoggers.contains(logger_debug));
		assertTrue(enabledLoggers.contains(logger_trace));

		enabledLoggers = tagBasedLogger.getEnabledLoggers(INFO, tags);

		assertTrue(enabledLoggers.size() == 3);
		assertTrue(enabledLoggers.contains(logger_info));
		assertTrue(enabledLoggers.contains(logger_debug));
		assertTrue(enabledLoggers.contains(logger_trace));

		enabledLoggers = tagBasedLogger.getEnabledLoggers(DEBUG, tags);

		assertTrue(enabledLoggers.size() == 2);
		assertTrue(enabledLoggers.contains(logger_debug));
		assertTrue(enabledLoggers.contains(logger_trace));

		enabledLoggers = tagBasedLogger.getEnabledLoggers(TRACE, tags);

		assertTrue(enabledLoggers.size() == 1);
		assertTrue(enabledLoggers.contains(logger_trace));
	}

	public Appender createMockAppender(String name) {

		Appender appender = mock(Appender.class);
		when(appender.getName()).thenReturn(name);
		when(appender.toString()).thenReturn(name);
		return appender;
	}

	public Logger createMockLogger(String name) {

		return createMockLogger(name, FATAL);
	}

	public Logger createMockLogger(String name, Level level) {

		Logger logger = mock(Logger.class);

		when(logger.getName()).thenReturn(name);

		when(logger.toString()).thenReturn(name);

		when(logger.getLevel()).thenReturn(level);

		when(logger.isEnabledFor(FATAL)).thenReturn(FATAL.isGreaterOrEqual(level));
		when(logger.isEnabledFor(ERROR)).thenReturn(ERROR.isGreaterOrEqual(level));
		when(logger.isEnabledFor(WARN)).thenReturn(WARN.isGreaterOrEqual(level));
		when(logger.isEnabledFor(INFO)).thenReturn(INFO.isGreaterOrEqual(level));
		when(logger.isEnabledFor(DEBUG)).thenReturn(DEBUG.isGreaterOrEqual(level));
		when(logger.isEnabledFor(TRACE)).thenReturn(TRACE.isGreaterOrEqual(level));

		return logger;
	}

	public AppenderEnumeration createAppenderEnumeration(Appender... appenders) {

		return new AppenderEnumeration(Arrays.asList(appenders));
	}

	public class AppenderEnumeration implements Enumeration {

		public final Iterator iterator;

		public AppenderEnumeration(List<Appender> elements) {

			this.iterator = elements.iterator();
		}

		@Override
		public boolean hasMoreElements() {

			return iterator.hasNext();
		}

		@Override
		public Object nextElement() {

			return iterator.next();
		}
	}

	@Test
	public void tesTagsToBeMinimallyAppliedToAllLogging() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		Set<String> incomingTags = new HashSet();
		incomingTags.add("a");
		incomingTags.add("b");
		incomingTags.add("c");
		tagBasedLogger.setTagsToBeMinimallyAppliedToAllLogging(incomingTags);

		Set<String> result = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();

		assertTrue(result.size() == 3);
		assertTrue(result.contains("a"));
		assertTrue(result.contains("b"));
		assertTrue(result.contains("c"));
	}

	@Test
	public void testThresholdLevelForRecordingSourceCodeLocation() {

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		tagBasedLogger.setThresholdLevelForRecordingSourceCodeLocation(Level.FATAL);
		assertTrue(tagBasedLogger.getThresholdLevelForRecordingSourceCodeLocation() == Level.FATAL);

		tagBasedLogger.setThresholdLevelForRecordingSourceCodeLocation(Level.ERROR);
		assertTrue(tagBasedLogger.getThresholdLevelForRecordingSourceCodeLocation() == Level.ERROR);
	}
} 
