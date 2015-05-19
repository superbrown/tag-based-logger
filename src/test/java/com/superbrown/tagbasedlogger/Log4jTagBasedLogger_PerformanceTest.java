package com.superbrown.tagbasedlogger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class Log4jTagBasedLogger_PerformanceTest
{

	private Logger logger_root;
	private Logger logger_a;
	private Logger logger_b;
	private Logger logger_c;
	private Logger logger_d;
	private Logger logger_e;
	private Logger logger_f;

	private MockAppender appender_root;
	private MockAppender appender_a;
	private MockAppender appender_b;
	private MockAppender appender_c;
	private MockAppender appender_d;
	private MockAppender appender_e;
	private MockAppender appender_f;

	@Before
	public void before() throws Exception {

		logger_root = Logger.getRootLogger();
		logger_a = Logger.getLogger("a");
		logger_b = Logger.getLogger("b");
		logger_c = Logger.getLogger("c");
		logger_d = Logger.getLogger("d");
		logger_e = Logger.getLogger("e");
		logger_f = Logger.getLogger("f");

		logger_root.removeAllAppenders();
		logger_a.removeAllAppenders();
		logger_b.removeAllAppenders();
		logger_c.removeAllAppenders();
		logger_d.removeAllAppenders();
		logger_e.removeAllAppenders();
		logger_f.removeAllAppenders();

		logger_root.setLevel(Level.TRACE);
		logger_a.setLevel(Level.TRACE);
		logger_b.setLevel(Level.TRACE);
		logger_c.setLevel(Level.TRACE);
		logger_d.setLevel(Level.TRACE);
		logger_e.setLevel(Level.TRACE);
		logger_f.setLevel(Level.TRACE);

		logger_a.setAdditivity(false);
		logger_b.setAdditivity(false);
		logger_c.setAdditivity(false);
		logger_d.setAdditivity(false);
		logger_e.setAdditivity(false);
		logger_f.setAdditivity(false);

		appender_root = new MockAppender("root");
		appender_a = new MockAppender("a");
		appender_b = new MockAppender("b");
		appender_c = new MockAppender("c");
		appender_d = new MockAppender("d");
		appender_e = new MockAppender("e");
		appender_f = new MockAppender("f");

		logger_root.addAppender(appender_root);
		logger_a.addAppender(appender_a);
		logger_b.addAppender(appender_b);
		logger_c.addAppender(appender_c);
		logger_d.addAppender(appender_d);
		logger_e.addAppender(appender_e);
		logger_f.addAppender(appender_f);
	}

	@After
	public void after() throws Exception {

	}

	@Test
	public void whenSeveralAppendersAreInvolved_withNoAdditivity() {

		// make sure we're comparing apples to apples, that we're equally exercising both libraries

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a", "b", "c");
		tagBasedLogger.trace("testing 123", "d", "e", "f");

		assertTrue(appender_root.loggingEvent == null);
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_b.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_c.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_d.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_e.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_a.loggingEvent = null;
		appender_b.loggingEvent = null;
		appender_c.loggingEvent = null;
		appender_d.loggingEvent = null;
		appender_e.loggingEvent = null;
		appender_f.loggingEvent = null;

		logger_a.trace("testing 123");
		logger_b.trace("testing 123");
		logger_c.trace("testing 123");
		logger_d.trace("testing 123");
		logger_e.trace("testing 123");
		logger_f.trace("testing 123");

		assertTrue(appender_root.loggingEvent == null);
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_b.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_c.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_d.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_e.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_a.loggingEvent = null;
		appender_b.loggingEvent = null;
		appender_c.loggingEvent = null;
		appender_d.loggingEvent = null;
		appender_e.loggingEvent = null;
		appender_f.loggingEvent = null;

		// now test it

		try {
			tagBasedLogger = new Log4JTagBasedLogger("a", "b", "c");

			// pause to get everything stable
			Thread.sleep(50);

			long tagBasedLoggerStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				tagBasedLogger.trace("testing 123", "d", "e", "f");
			}
			double durationUsingTagBasedLogger = System.currentTimeMillis() - tagBasedLoggerStartTime;

			// Now do the equivalent thing using raw log4j
			long log4jStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				logger_a.trace("testing 123");
				logger_b.trace("testing 123");
				logger_c.trace("testing 123");
				logger_d.trace("testing 123");
				logger_e.trace("testing 123");
				logger_f.trace("testing 123");
			}
			double durationUsingLog4j = System.currentTimeMillis() - log4jStartTime;

			double tagBasedToLog4jRatio = durationUsingTagBasedLogger / durationUsingLog4j;

			// make sure the tag based logger didn't take longer than raw log4j
			assertTrue("tagBasedToLog4jRatio: " + tagBasedToLog4jRatio, tagBasedToLog4jRatio < 1);
		}
		catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void whenSeveralAppendersAreInvolved_withAdditivityOnOneLogger() {

		// This is what distinguishes this test from the first one.
		logger_a.setAdditivity(true);

		// make sure we're comparing apples to apples, that we're equally exercising both libraries

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a", "b", "c");
		tagBasedLogger.trace("testing 123", "d", "e", "f");

		assertTrue(appender_root.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_b.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_c.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_d.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_e.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_f.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_root.loggingEvent = null;
		appender_a.loggingEvent = null;
		appender_a.loggingEvent = null;
		appender_b.loggingEvent = null;
		appender_c.loggingEvent = null;
		appender_d.loggingEvent = null;
		appender_e.loggingEvent = null;
		appender_f.loggingEvent = null;

		logger_root.trace("testing 123");
		logger_a.trace("testing 123");
		logger_b.trace("testing 123");
		logger_c.trace("testing 123");
		logger_d.trace("testing 123");
		logger_e.trace("testing 123");
		logger_f.trace("testing 123");

		assertTrue(appender_root.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_b.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_c.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_d.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_e.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_f.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_root.loggingEvent = null;
		appender_a.loggingEvent = null;
		appender_a.loggingEvent = null;
		appender_b.loggingEvent = null;
		appender_c.loggingEvent = null;
		appender_d.loggingEvent = null;
		appender_e.loggingEvent = null;
		appender_f.loggingEvent = null;

		// now test it

		try {
			tagBasedLogger = new Log4JTagBasedLogger("a", "b", "c");

			// pause to get everything stable
			Thread.sleep(50);

			long tagBasedLoggerStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				tagBasedLogger.trace("testing 123", "d", "e", "f");
			}
			double durationUsingTagBasedLogger = System.currentTimeMillis() - tagBasedLoggerStartTime;

			// Now do the equivalent thing using raw log4j
			long log4jStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				logger_a.trace("testing 123");
				logger_b.trace("testing 123");
				logger_c.trace("testing 123");
				logger_d.trace("testing 123");
				logger_e.trace("testing 123");
				logger_f.trace("testing 123");
			}
			double durationUsingLog4j = System.currentTimeMillis() - log4jStartTime;

			double tagBasedToLog4jRatio = durationUsingTagBasedLogger / durationUsingLog4j;

			// make sure the tag based logger didn't take longer than raw log4j
			assertTrue("tagBasedToLog4jRatio: " + tagBasedToLog4jRatio, tagBasedToLog4jRatio < 1);
		}
		catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void whenOneAppenderIsInvolved_withNoAdditivity() {

		logger_a.setAdditivity(false);

		// make sure we're comparing apples to apples, that we're equally exercising both libraries

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a");
		tagBasedLogger.trace("testing 123");

		assertTrue(appender_root.loggingEvent == null);
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_a.loggingEvent = null;

		logger_a.trace("testing 123");

		assertTrue(appender_root.loggingEvent == null);
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_a.loggingEvent = null;

		// now test it

		try {

			tagBasedLogger = new Log4JTagBasedLogger("a");

			// pause to get everything stable
			Thread.sleep(50);

			long tagBasedLoggerStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				tagBasedLogger.trace("testing 123");
			}
			double durationUsingTagBasedLogger = System.currentTimeMillis() - tagBasedLoggerStartTime;

			// Now do the equivalent thing using raw log4j
			long log4jStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				logger_a.trace("testing 123");
			}
			double durationUsingLog4j = System.currentTimeMillis() - log4jStartTime;

			double tagBasedToLog4jRatio = durationUsingTagBasedLogger / durationUsingLog4j;

			// make sure the tag based logger didn't take longer than raw log4j
			assertTrue("tagBasedToLog4jRatio: " + tagBasedToLog4jRatio, tagBasedToLog4jRatio < 1);
		}
		catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void whenOneAppenderIsInvolved_withAdditivity() {

		logger_a.setAdditivity(true);

		// make sure we're comparing apples to apples, that we're equally exercising both libraries

		TagBasedLogger tagBasedLogger = new Log4JTagBasedLogger("a");
		tagBasedLogger.trace("testing 123");

		assertTrue(appender_root.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_root.loggingEvent = null;
		appender_a.loggingEvent = null;

		logger_a.trace("testing 123");

		assertTrue(appender_root.loggingEvent.getMessage().equals("testing 123"));
		assertTrue(appender_a.loggingEvent.getMessage().equals("testing 123"));

		// reset
		appender_root.loggingEvent = null;
		appender_a.loggingEvent = null;

		// now test it

		try {
			tagBasedLogger = new Log4JTagBasedLogger("a");

			// pause to get everything stable
				Thread.sleep(50);

			long tagBasedLoggerStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				tagBasedLogger.trace("testing 123");
			}
			double durationUsingTagBasedLogger = System.currentTimeMillis() - tagBasedLoggerStartTime;

			// Now do the equivalent thing using raw log4j
			long log4jStartTime = System.currentTimeMillis();
			for (int i = 0; i < 50000; i++) {

				logger_root.trace("testing 123");
				logger_a.trace("testing 123");
			}
			double durationUsingLog4j = System.currentTimeMillis() - log4jStartTime;

			double tagBasedToLog4jRatio = durationUsingTagBasedLogger / durationUsingLog4j;

			// make sure the tag based logger didn't take longer than raw log4j
			assertTrue("tagBasedToLog4jRatio: " + tagBasedToLog4jRatio, tagBasedToLog4jRatio < 1);
		}
		catch (InterruptedException e) {
			fail();
		}
	}
}
