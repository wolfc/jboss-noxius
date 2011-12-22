#!/bin/bash
set -e
set -x
cd bootstrap
mkdir -p target/classes
javac -sourcepath src/main/java -d target/classes `find src/main/java -name \*.java`
(cd target/classes && jar cvfe ../jboss-noxius-bootstrap.jar org.jboss.noxius.bootstrap.Bootstrap `find . -name *.class`)
