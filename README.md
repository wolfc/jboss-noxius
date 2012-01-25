JBoss Noxius
============

Working Directory
-----------------
Although Java has the notion of a current working directory (system property
"user.dir"), and you can set this property to another value, it has no impact
on the real working directory of the JVM process.

Most operations in Noxius tend to be on files or directories. To make sure
you're operating on the right file you must use absolute path names. This can
be done by prepending system property "user.dir".

This will allow other Noxius operations to change the working directory by
just setting system property "user.dir".
