package com.superbrown.tagbasedlogger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class Log4jTagBasedLogger_FunctionalTest
{
	private Logger logger_root;
	private Logger logger_a;
	private Logger logger_ab;
	private Logger logger_abc;
	private Logger logger_abcd;
	private Logger logger_abcde;
	private Logger logger_b;
	private Logger logger_c;

	private MockAppender appender_root;
	private MockAppender appender_a;
	private MockAppender appender_ab;
	private MockAppender appender_abc;
	private MockAppender appender_abcd;
	private MockAppender appender_abcde;
	private MockAppender appender_b;
	private MockAppender appender_c;

	@Before
	public void before() throws Exception {

		logger_root = Logger.getRootLogger();
		logger_a = Logger.getLogger("a");
		logger_ab = Logger.getLogger("a.b");
		logger_abc = Logger.getLogger("a.b.c");
		logger_abcd = Logger.getLogger("a.b.c.d");
		logger_abcde = Logger.getLogger("a.b.c.d.e");
		logger_b = Logger.getLogger("b");
		logger_c = Logger.getLogger("c");

		logger_root.removeAllAppenders();
		logger_a.removeAllAppenders();
		logger_ab.removeAllAppenders();
		logger_abc.removeAllAppenders();
		logger_abcd.removeAllAppenders();
		logger_abcde.removeAllAppenders();
		logger_b.removeAllAppenders();
		logger_c.removeAllAppenders();

		logger_root.setLevel(Level.TRACE);
		logger_a.setLevel(Level.TRACE);
		logger_ab.setLevel(Level.TRACE);
		logger_abc.setLevel(Level.TRACE);
		logger_abcd.setLevel(Level.TRACE);
		logger_abcde.setLevel(Level.TRACE);
		logger_b.setLevel(Level.TRACE);
		logger_c.setLevel(Level.TRACE);

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(true);
		logger_abc.setAdditivity(true);
		logger_abcd.setAdditivity(true);
		logger_abcde.setAdditivity(true);
		logger_b.setAdditivity(true);
		logger_c.setAdditivity(true);

		appender_root = new MockAppender("root");
		appender_a = new MockAppender("a");
		appender_ab = new MockAppender("ab");
		appender_abc = new MockAppender("abc");
		appender_abcd = new MockAppender("abcd");
		appender_abcde = new MockAppender("abcde");
		appender_b = new MockAppender("b");
		appender_c = new MockAppender("c");

		logger_root.addAppender(appender_root);
		logger_a.addAppender(appender_a);
		logger_ab.addAppender(appender_ab);
		logger_abc.addAppender(appender_abc);
		logger_abcd.addAppender(appender_abcd);
		logger_abcde.addAppender(appender_abcde);
		logger_b.addAppender(appender_b);
		logger_c.addAppender(appender_c);
	}

	@After
	public void after() throws Exception {

	}

	@Test
	public void testAdditivityAllTrue() {

		Log4JTagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.fatal("testing 123", "a.b.c.d");

		assertTrue(appender_abcd.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abcd.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abcd.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_ab.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_ab.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_ab.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_a.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_a.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_a.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
	}

	@Test
	public void testConstructLoggerWithATagAndCallLogMethodWithoutAnyTags() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d");
		tagBasedLogger.fatal("testing 123");

		assertTrue(appender_abcd.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abcd.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abcd.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_ab.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_ab.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_ab.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_a.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_a.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_a.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
	}

	@Test
	public void testConstructLoggerWithMultipleTagsAndCallLogMethodWithoutAnyTags() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a", "b", "c");
		tagBasedLogger.fatal("testing 123");

		assertTrue(appender_a.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_a.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_a.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_b.loggingEvent.getLoggerName().equals("[b]"));
		assertTrue(appender_b.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_b.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_c.loggingEvent.getLoggerName().equals("[c]"));
		assertTrue(appender_c.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_c.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a][b][c]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
	}

	@Test
	public void testAdditivityNotAllTrue() {

		logger_a.setAdditivity(true);
		logger_ab.setAdditivity(true);
		logger_abc.setAdditivity(false);
		logger_abcd.setAdditivity(true);

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.fatal("testing 123", "a.b.c.d");

		assertTrue(appender_abcd.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abcd.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abcd.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[a.b.c.d]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_ab.loggingEvent == null);

		assertTrue(appender_a.loggingEvent == null);

		assertTrue(appender_root.loggingEvent == null);
	}

	@Test
	public void testConstructLoggerWithNoArgsAndCallLogMethodWithNoTags() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.fatal("testing 123");

		assertTrue(appender_abcd.loggingEvent == null);

		assertTrue(appender_abc.loggingEvent == null);

		assertTrue(appender_ab.loggingEvent == null);

		assertTrue(appender_a.loggingEvent == null);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
	}

	@Test
	public void testCallLoggerWithMultipleTags() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.fatal("testing 123", "a", "b", "c");

		assertTrue(appender_a.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_a.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_a.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_b.loggingEvent.getLoggerName().equals("[b]"));
		assertTrue(appender_b.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_b.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_c.loggingEvent.getLoggerName().equals("[c]"));
		assertTrue(appender_c.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_c.loggingEvent.getLevel() == Level.FATAL);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a][b][c]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
	}

	@Test
	public void testLogLevels_levelIsDependantUponHighestMatchingLogger() {

		logger_root.setLevel(Level.FATAL);
		logger_a.setLevel(Level.FATAL);
		logger_ab.setLevel(Level.FATAL);
		logger_abc.setLevel(Level.FATAL);
		logger_abcd.setLevel(Level.FATAL);
		logger_abcde.setLevel(Level.TRACE);

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d.e");
		tagBasedLogger.trace("testing 123");

		assertTrue(appender_abcde.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_abcde.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abcde.loggingEvent.getLevel() == Level.TRACE);

		assertTrue(appender_abcd.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_abcd.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abcd.loggingEvent.getLevel() == Level.TRACE);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.TRACE);

		assertTrue(appender_ab.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_ab.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_ab.loggingEvent.getLevel() == Level.TRACE);

		assertTrue(appender_a.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_a.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_a.loggingEvent.getLevel() == Level.TRACE);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a.b.c.d.e]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.TRACE);
	}

	@Test
	public void testTagsWithoutSpecificSettingsArePickedUpByTagsHigherInTheHierarchy() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		logger_abc.setLevel(Level.INFO);

		String tag = "a.b.c.SomeClassName";

		appender_abc.loggingEvent = null;
		tagBasedLogger.fatal("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.FATAL);

		appender_abc.loggingEvent = null;
		tagBasedLogger.error("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.ERROR);

		appender_abc.loggingEvent = null;
		tagBasedLogger.warn("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.WARN);

		appender_abc.loggingEvent = null;
		tagBasedLogger.info("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.INFO);

		appender_abc.loggingEvent = null;
		tagBasedLogger.debug("testing 123", "[" + tag + "]");

		assertTrue(appender_abc.loggingEvent == null);

		appender_abc.loggingEvent = null;
		tagBasedLogger.trace("testing 123", "[" + tag + "]");

		assertTrue(appender_abc.loggingEvent == null);

		tag = "a.b.c.somePackage.SomeClassName";

		appender_abc.loggingEvent = null;
		tagBasedLogger.fatal("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.FATAL);

		appender_abc.loggingEvent = null;
		tagBasedLogger.error("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.ERROR);

		appender_abc.loggingEvent = null;
		tagBasedLogger.warn("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.WARN);

		appender_abc.loggingEvent = null;
		tagBasedLogger.info("testing 123", tag);

		assertTrue(appender_abc.loggingEvent.getLoggerName().equals("[" + tag + "]"));
		assertTrue(appender_abc.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_abc.loggingEvent.getLevel() == Level.INFO);

		appender_abc.loggingEvent = null;
		tagBasedLogger.debug("testing 123", "[" + tag + "]");

		assertTrue(appender_abc.loggingEvent == null);

		appender_abc.loggingEvent = null;
		tagBasedLogger.trace("testing 123", "[" + tag + "]");

		assertTrue(appender_abc.loggingEvent == null);
	}

	@Test
	public void testIsEnabledFor() {

		logger_root.setLevel(Level.INFO);
		logger_a.setLevel(null);
		logger_ab.setLevel(null);
		logger_abc.setLevel(null);
		logger_abcd.setLevel(null);
		logger_abcde.setLevel(null);

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d.e.f");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d.e");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		logger_root.setLevel(Level.INFO);
		logger_a.setLevel(null);
		logger_ab.setLevel(null);
		logger_abc.setLevel(Level.FATAL);
		logger_abcd.setLevel(null);
		logger_abcde.setLevel(null);


		tagBasedLogger = new Log4JTagBasedLogger();

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d.e.f");

		assertTrue(tagBasedLogger.isErrorEnabled() == false);
		assertTrue(tagBasedLogger.isWarnEnabled() == false);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d.e");

		assertTrue(tagBasedLogger.isErrorEnabled() == false);
		assertTrue(tagBasedLogger.isWarnEnabled() == false);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c.d");

		assertTrue(tagBasedLogger.isErrorEnabled() == false);
		assertTrue(tagBasedLogger.isWarnEnabled() == false);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b.c");

		assertTrue(tagBasedLogger.isErrorEnabled() == false);
		assertTrue(tagBasedLogger.isWarnEnabled() == false);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a.b");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		tagBasedLogger = new Log4JTagBasedLogger("a");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == true);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);


		logger_root.setLevel(Level.FATAL);
		logger_a.setLevel(Level.ERROR);
		logger_ab.setLevel(Level.WARN);
		logger_abc.setLevel(Level.INFO);
		logger_abcd.setLevel(Level.DEBUG);
		logger_abcde.setLevel(Level.TRACE);

		tagBasedLogger = new Log4JTagBasedLogger();

		assertTrue(tagBasedLogger.isErrorEnabled() == false);
		assertTrue(tagBasedLogger.isWarnEnabled() == false);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a") == false);
		assertTrue(tagBasedLogger.isInfoEnabled("a") == false);
		assertTrue(tagBasedLogger.isDebugEnabled("a") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b") == false);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c.d") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c.d.e") == true);

		// a couple of different tags, should go with the value level of the tag that is least restrictive
		assertTrue(tagBasedLogger.isErrorEnabled("a", "a.b.c") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a", "a.b.c") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a", "a.b.c") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a", "a.b.c") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a", "a.b.c") == false);


		logger_root.setLevel(Level.FATAL);
		logger_a.setLevel(Level.WARN);
		logger_ab.setLevel(Level.WARN);
		logger_abc.setLevel(Level.INFO);
		logger_abcd.setLevel(Level.DEBUG);
		logger_abcde.setLevel(Level.TRACE);

		tagBasedLogger = new Log4JTagBasedLogger("a");

		assertTrue(tagBasedLogger.isErrorEnabled() == true);
		assertTrue(tagBasedLogger.isWarnEnabled() == true);
		assertTrue(tagBasedLogger.isInfoEnabled() == false);
		assertTrue(tagBasedLogger.isDebugEnabled() == false);
		assertTrue(tagBasedLogger.isTraceEnabled() == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a") == false);
		assertTrue(tagBasedLogger.isDebugEnabled("a") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b") == false);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c.d") == true);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c.d") == false);

		assertTrue(tagBasedLogger.isErrorEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isDebugEnabled("a.b.c.d.e") == true);
		assertTrue(tagBasedLogger.isTraceEnabled("a.b.c.d.e") == true);
	}

	@Test
	public void testLogLevels_fatal() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.fatal("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);

		tagBasedLogger.fatal("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);

		Exception e = new Exception();
		tagBasedLogger.fatal("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.FATAL);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testLogLevels_error() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.error("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.ERROR);

		tagBasedLogger.error("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.ERROR);

		Exception e = new Exception();
		tagBasedLogger.error("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.ERROR);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testLogLevels_warn() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.warn("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.WARN);

		tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.warn("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.WARN);

		Exception e = new Exception();
		tagBasedLogger.warn("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.WARN);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testLogLevels_info() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.info("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.INFO);

		tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.info("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.INFO);

		Exception e = new Exception();
		tagBasedLogger.info("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.INFO);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testLogLevels_debug() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.debug("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.DEBUG);

		tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.debug("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.DEBUG);

		Exception e = new Exception();
		tagBasedLogger.debug("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.DEBUG);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testLogLevels_trace() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.trace("testing 123");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[root]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.TRACE);

		tagBasedLogger = new Log4JTagBasedLogger();
		tagBasedLogger.trace("testing 123", "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.TRACE);

		Exception e = new Exception();
		tagBasedLogger.trace("testing 123", e, "a");

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[a]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("testing 123"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.TRACE);
		assertTrue(appender_root.loggingEvent.getThrowableInformation().getThrowable() == e);
	}

	@Test
	public void testThatClassesAreTreatedCorrectlyAsTags() {

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger(Object.class);
		Set<String> tagsToBeMinimallyAppliedToAllLogging = tagBasedLogger.getTagsToBeMinimallyAppliedToAllLogging();
		assertTrue(tagsToBeMinimallyAppliedToAllLogging.size() == 1);
		Object tag = tagsToBeMinimallyAppliedToAllLogging.iterator().next();
		assertTrue(tag instanceof String);
		assertTrue(tag.equals("java.lang.Object"));

		Logger logger_javaLang = Logger.getLogger("java.lang");
		logger_javaLang.setLevel(Level.WARN);

		tagBasedLogger = new Log4JTagBasedLogger(Object.class);

		assertTrue(tagBasedLogger.isErrorEnabled("java.lang.Object") == true);
		assertTrue(tagBasedLogger.isWarnEnabled("java.lang.Object") == true);
		assertTrue(tagBasedLogger.isInfoEnabled("java.lang.Object") == false);
		assertTrue(tagBasedLogger.isDebugEnabled("java.lang.Object") == false);
		assertTrue(tagBasedLogger.isTraceEnabled("java.lang.Object") == false);

		assertTrue(tagBasedLogger.isErrorEnabled(Object.class) == true);
		assertTrue(tagBasedLogger.isWarnEnabled(Object.class) == true);
		assertTrue(tagBasedLogger.isInfoEnabled(Object.class) == false);
		assertTrue(tagBasedLogger.isDebugEnabled(Object.class) == false);
		assertTrue(tagBasedLogger.isTraceEnabled(Object.class) == false);

		appender_root.loggingEvent = null;
		tagBasedLogger = new Log4JTagBasedLogger();

		tagBasedLogger.warn("message", Object.class);

		assertTrue(appender_root.loggingEvent.getLoggerName().equals("[java.lang.Object]"));
		assertTrue(appender_root.loggingEvent.getMessage().toString().endsWith("message"));
		assertTrue(appender_root.loggingEvent.getLevel() == Level.WARN);
	}
}
