/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import java.io.File;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class build {
    private static void compile(final Collection<String> options) {
        final int result = com.sun.tools.javac.Main.compile(options.toArray(new String[0]));
        if (result != 0)
            throw new RuntimeException("Compilation failed");
    }

    private static Collection<String> filesIn(final String directoryName) {
        final Collection<String> files = new ArrayList<String>();
        final File directory = new File(directoryName);
        if (!directory.isDirectory())
            return Collections.EMPTY_LIST;
        for (File file : directory.listFiles()) {
            if (file.isDirectory())
                files.addAll(filesIn(file.getAbsolutePath()));
            else
                files.add(file.getAbsolutePath());
        }
        return files;
    }

    private static void jar(final String... args) {
        sun.tools.jar.Main.main(args);
    }

    public static void main(final String[] args) throws Exception {
        new File("target/classes").mkdirs();

        // javac -sourcepath src/main/java -d target/classes `find src/main/java -name *.java`
        final List<String> compilerArgs = new ArrayList<String>();
        compilerArgs.add("-sourcepath");
        compilerArgs.add("src/main/java");
        compilerArgs.add("-d");
        compilerArgs.add("target/classes");
        compilerArgs.addAll(filesIn("src/main/java"));
        compile(compilerArgs);

        //jar cvfe target/jboss-noxius-bootstrap.jar org.jboss.noxius.bootstrap.Bootstrap -C target/classes/ .
        jar("cvfe", "target/jboss-noxius-bootstrap.jar", "org.jboss.noxius.bootstrap.Bootstrap", "-C", "target/classes/", ".");
    }
}
