# tag-based-logger

**a Java logging framework wrapper that allows logging based on tags**

The idea is to make it easier to locate log entries of interest by assigning them tags when they're
generated.  Tags can also be used to designate special handling for particular types of entries.

For example:

		log.warn("No connections available.", e, "CONNECTION_POOL", "NOTIFY_ADMIN");

In this example, CONNECTION_POOL and NOTIFY_ADMIN are tags.

The only implementation so far is a wrapper for log4j.  The API is similar to log4j, just having
optional arguments to designate tags (as above).  Also, logger constructors take 0..n tags as
initialization arguments.  These will then be applied to each log entry the logger makes.

Under the covers the tags map to standard log4j loggers.  (Think of this as having the ability to
log to multiple loggers at once.)  Since multiple loggers can map to the same appender, it's not
uncommon for an entry's tags to be handled by the same appender.  In case like that, the appender
will make a single entry (rather than multiple) and the tags will appear consolidated.

For example:

	TagBasedLogger log = Log4jTagBasedLogManager.getLogger("STATUS", "CONNECTION_POOL");
	log.warn("No connections available.", "NOTIFY_ADMIN");

The entry in the log would look something like this:

	2015-05-05 08:34:12 WARN [[STATUS][CONNECTION_POOL][NOTIFY_ADMIN]] No connections available.

The relationship between "tags" and appenders piggy-backs on log4j's mechanism for associating
loggers and appenders.  Loggers are configured as always, only using tag names.  (Note: Class
objects can also be used as tags, the same way they're used to designate loggers in many projects
using log4j.)  All of log4j's logger behaviors -- including the role of the root logger, logger
parent/child relationships and additivity rules -- will continue to function as always.

An added benefit of this approach to logging is that it allows you to have special handling using
tags as switches.  For instance, you could have a tag to indicate support personnel should be
notified and configure the logging framework to send out an e-mail message whenever that tag got
logged (in the case of log4j, by mapping its logger to an e-mailing appender).

Initial performance tests of the log4j implementation indicate it's no more taxing on system
performance than log4j on its own.  In fact, the performance tests include in the unit test suite
using  mock appenders indicate it's considerably quicker, although it's hard to imagine how that
could be the case.  (Independent tests are welcomed.)
