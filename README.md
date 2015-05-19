# tag-based-logger

**a logging framework wrapper that allows logging based on tags**

The primary idea is to make it easier to locate log entries of interest by assigning them tags.   A
developer may find it helpful to apply multiple tags to log entries, since many log entries relate
to multiple things.  Tags can also be used to designate special handling for particular entries.

The only implementation do far is a wrapper for log4j.  The API is very similar to log4j, but allows
you to pass in tags as optional arguments to the logging methods.  Under the covers the tags map to
standard log4j loggers.  Basically this allows you to log to multiple loggers at once.  If more than
one of the tags map to a particular appender, the appender will make a single entry (rather than
multiple entries) and the tag names will appear consolidated.  For example, if an appender were
configured to handle the tags STATUS, CONNECTION_POOL, and NOTIFY_ADMIN, (or was just the root
appender), and we had code like the following:

	TagBasedLogger log = Log4jTagBasedLogManager.getLogger("STATUS", "CONNECTION_POOL");
	log.warn("No connections available.", "NOTIFY_ADMIN");

The entry in the log would look something like this:

	2015-05-05 08:34:12 WARN [[STATUS][CONNECTION_POOL][NOTIFY_ADMIN]] No connections available.

The relationship between "tags" and appenders piggy-backs on log4j's mechanism for configuring
loggers and appenders.  Loggers are configured as always, only using tag names.  (Note: Class
objects can also be used as tags, like they're used to identify loggers in many projects using
log4j.)  All of log4j's logger behavior regarding the use of the root logger and logger
parent/child relationships and additivity rules will continue to function as always.

An added benefit of this approach to logging is that it makes it easy to have special handling for
logging events using tags as switches.  For instance, one could have a tag to indicate support
personnel should be notified and configure the logging framework to send out an e-mail message
whenever that tag was encountered, by mapping its logger to an e-mailing appender.
