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
import static com.sun.tools.javac.Main.compile;

import java.io.File;
import java.lang.Class;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;

public class build {
    public static void main(final String[] args) throws Exception {
        System.out.println("**** IT WORKS ****");
        System.out.println(build.class.getClassLoader());
        System.out.println(Class.forName("com.sun.tools.javac.Main", false, build.class.getClassLoader()));

        /*
        new File("target/classes").mkdirs();
        // javac -sourcepath src/main/java -d target/classes `find src/main/java -name *.java`
        final List<String> compilerArgs = new ArrayList<String>();
        //final String[] compilerArgs = { "-sourcepath", "src/main/java", "-d", "target/classes", "src/main/java/org/jboss/noxius/Noxius.java" };
        compilerArgs.add("-sourcepath");
        compilerArgs.add("src/main/java");
        compilerArgs.add("-d");
        compilerArgs.add("target/classes");
        compilerArgs.addAll(filesIn("src/main/java"));
        System.exit(compile(compilerArgs.toArray(new String[0])));
        */
    }
}
