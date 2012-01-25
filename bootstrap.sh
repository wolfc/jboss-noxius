#!/bin/bash
set -e
set -x
cd bootstrap
mkdir -p target/bootstrap-classes
javac -sourcepath src/main/java -d target/bootstrap-classes `find src/main/java -name \*.java`
#(cd target/bootstrap-classes && jar cvfe ../jboss-noxius-bootstrap-bootstrap.jar org.jboss.noxius.bootstrap.Bootstrap `find . -name *.class`)
jar cvfe target/jboss-noxius-bootstrap-bootstrap.jar org.jboss.noxius.bootstrap.Bootstrap -C target/bootstrap-classes/ .
