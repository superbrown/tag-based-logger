# tag-based-logger

**a Java logging framework wrapper that allows logging based on tags**

The idea is to make it easier to locate log entries of interest by assigning them tags when they're
generated.  Tags can also be used to designate special handling for particular types of entries.

For example:

	log.warn("No connections available.", e, "CONNECTION_POOL", "NOTIFY_ADMIN");

In this example, CONNECTION_POOL and NOTIFY_ADMIN are tags. The log entry generated might look
something like this:

	2015-05-05 08:34:12 WARN [[CONNECTION_POOL][NOTIFY_ADMIN]] No connections available.

The only implementation so far is a wrapper for log4j.  The API is similar to log4j, just having
optional arguments to designate tags (as above).  Also, logger constructors take 0..n tags as
initialization arguments, designating tags to be applied (at a minimum) to each log entry the logger
makes.

For example:

	TagBasedLogger log = Log4jTagBasedLogManager.getLogger("STATUS", "CONNECTION_POOL");
	log.warn("No connections available.", "NOTIFY_ADMIN");

...would produce an entry like this:

	2015-05-05 08:34:12 WARN [[STATUS][CONNECTION_POOL][NOTIFY_ADMIN]] No connections available.

Under the covers the tags map to standard log4j loggers.  (Think of this as having the ability to
log to multiple loggers at once.)  Since multiple loggers can map to the same appender, it's not
uncommon for an entry's tags to be handled by the same appender.  In a case like this, the appender
will make a single entry (rather than multiple) and the tags will appear consolidated (as in the
examples above).

The relationship between "tags" and appenders piggy-backs on log4j's mechanism for associating
loggers and appenders.  Loggers are configured as always, only using tag names.  (Note: Class
objects can also be used as tags, the same way they're used to designate loggers in many projects
using log4j.)  All of log4j's logger behaviors -- including the role of the root logger, logger
parent/child relationships and additivity rules -- will continue to function as always.

An added benefit of tag based logging is that it allows for special handling using tags as switches.
For instance, a tag could be used to indicate support personnel should be notified.  Then, in
something like log4j, the tag could be mapped to an appender configured to send out e-mail messages.

Initial performance tests of the log4j implementation indicate it's no more taxing on system
performance than log4j on its own.  In fact, the performance tests included in the unit test suite
using  mock appenders indicate it's considerably quicker, although it's hard to imagine how that
could be the case.  (Independent tests are welcomed.)
