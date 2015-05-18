# tag-based-logger
A logging framework wrapper that allows logging based on tags.

The primary idea is to make it easier later to locate log entries of interest by assigning them
tags. Tags can also be used to designate special handling of particular entries.

So far there's a log4j implementation, Log4JTagBasedLogger, that allows you to pass in tags as
optional arguments to the logging methods.  Under the covers the tags map to standard log4j loggers.
So basically this allows you to log to multiple loggers at once.  If more than one of the tags map
to a particular appender, the appender will make a single entry (rather than multiple) and the tag
names will be consolidated in the portion of the entry containing the logger's name.

The relationship between "tags" and appenders piggy-backs on log4j's mechanism for configuring
loggers and appenders.  So you configure your loggers as you always have, only using the names of
the tags you've used in your code.  All of log4j's logger behavior regarding the use of the root
logger and logger parent/child relationships being embedded in logger names (or tag names) will
continue to function as always.

An added benefit of this approach is it makes it easy to have special handling for logging events
using tags as switches.  For instance, one could have a tag to indicate support personnel should be
notified and configure the logging framework to send out an e-mail message whenever the tag is
encountered (by mapping its Logger to an e-mailing Appender).
