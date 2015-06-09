# tag-based-logger

**a Java logging framework wrapper that allows logging based on tags**

The idea behind this is to make it easier to locate log entries of interest by assigning them tags when 
they're generated.  Tags can also be used to designate special handling for particular entries (more on 
that later).

For example:

	log.warn("No connections available.", e, "CONNECTION_POOL", "NOTIFY_ADMIN");
	
In this example, CONNECTION_POOL and NOTIFY_ADMIN are tags. The log entry generated might look
something like this:

	2015-05-05 08:34:12 WARN [CONNECTION_POOL][NOTIFY_ADMIN] No connections available.
		java.lang.Exception
		at com.superbrown.tagbasedlogger.Main.method03(Main.java:37)
		at com.superbrown.tagbasedlogger.Main.method02(Main.java:32)
		at com.superbrown.tagbasedlogger.Main.method01(Main.java:27)
		at com.superbrown.tagbasedlogger.Main.main(Main.java:16)
		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
		at java.lang.reflect.Method.invoke(Method.java:606)
		at com.intellij.rt.execution.application.AppMain.main(AppMain.java:120)

The only implementation of this so far is a wrapper for Log4j.  Its API mirrors Log4j's, but the 
logging methods have optional arguments to designate tags (as above).  Additionally, the wrapper's
constructor takse 0..n tags as arguments, designating tags that will be applied (at a minimum) to 
each log entry the logger generates.

For example:

	TagBasedLogger log = Log4jTagBasedLogManager.getLogger("MONITOR", "CONNECTION_POOL");
	log.warn("No connections available.", "NOTIFY_ADMIN");

...would produce an entry like this:

	2015-05-05 08:34:12 WARN [MONITOR][CONNECTION_POOL][NOTIFY_ADMIN] No connections available.

Under the covers each tag maps to standard Log4j logger.  (Think of this as having the ability to
log to multiple Log4j loggers at once.)  Since multiple loggers can map to the same appender, it's 
not uncommon for an entry's tags to be handled by the same appender.  In a case like this, the 
appender will make a single entry (rather than multiple) and the tags will appear consolidated 
within the entry (as seen in the examples above).

The relationship between "tags" and appenders piggy-backs on Log4j's mechanism for associating
loggers and appenders.  Loggers are configured as always, only using tag names.  (Note: Class
objects can also be used as tags, the same way they're used to designate loggers in many projects
using Log4j.)  All of Log4j's logger behaviors -- including the role of the root logger, logger
parent/child relationships and additivity rules -- will continue to function as always.

An added benefit of tag based logging is that it allows for special handling using tags as switches.
For example, a tag could be used to indicate support personnel should be notified and it could be 
mapped to an appender configured to send out e-mail notifictions to them.

Initial performance tests of the Log4j implementation indicate it's no more taxing on system
performance than Log4j on its own.  (Independent tests are welcomed.)
