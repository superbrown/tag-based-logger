# tag-based-logger
A logging framework wrapper that allows logging based on tags.

The primary idea is to make it easier later to locate log entries of interest by assigning them
tags.   A developer may find it helpful to designate multiple tags for any particular log entry,
since many log entries are logically related to multiple things.  Tags can also be used to designate
special handling of particular entries.

So far there's a log4j implementation, Log4JTagBasedLogger.  It allows you to pass in tags as
optional arguments to the logging methods.  Under the covers the tags map to standard log4j loggers.
Basically this allows you to log to multiple loggers at once.  If more than one of the tags map to
a particular appender, the appender will make a single entry (rather than multiple) and the tag
names will be consolidated in the portion of the entry containing the logger's name.  So, for
example, if an appender were set up to handle the tags STATUS_CHECKER as well as CONNECTION_POOL,
and I made a call like this:

log.warn("No connections available.", "STATUS_CHECKER", "CONNECTION_POOL");

The entry in the log would look something like this:

2015-05-05 08:34:12 WARN [[STATUS_CHECKER][CONNECTION_POOL]] No connections available.


The relationship between "tags" and appenders piggy-backs on log4j's mechanism for configuring
loggers and appenders.  So you configure your loggers as you always have, only using the names of
your tags.  All of log4j's logger behavior regarding the use of the root logger and logger
parent/child relationships being embedded in logger names (or tag names) will continue to function
as always.

An added benefit of this approach is it makes it easy to have special handling for logging events
using tags as switches.  For instance, one could have a tag to indicate support personnel should be
notified and configure the logging framework to send out an e-mail message whenever the tag is
encountered (by mapping its logger to an e-mailing appender).
