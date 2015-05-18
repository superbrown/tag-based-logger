package com.superbrown.tagbasedlogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static junit.framework.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class Log4jTagBasedLogManagerTest
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
	}
} 
