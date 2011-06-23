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
package org.jboss.noxius.bootstrap;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Noxius {
    static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    public Class<?> compile(final URL resource, final String className) throws URISyntaxException, ClassNotFoundException, NoxiusCompileException {
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager standardJavaFileManager = COMPILER.getStandardFileManager(diagnostics, null, null);
        final Iterable<URLJavaFileObject> compilationUnits = Arrays.asList(new URLJavaFileObject(resource, className));
        final NoxiusFileManager fileManager = new NoxiusFileManager(standardJavaFileManager, compilationUnits);
        final Writer out = null; // System.err
        final Iterable<String> options = null;
        final Iterable<String> classes = null;
        final boolean result = COMPILER.getTask(out, fileManager, diagnostics, options, classes, compilationUnits).call();
        if (!result)
            throw new NoxiusCompileException("compilation failed", diagnostics.getDiagnostics());
        final ClassLoader parent = Thread.currentThread().getContextClassLoader();
        final NoxiusClassLoader classLoader = new NoxiusClassLoader(parent, fileManager);
        return Class.forName(className, true, classLoader);
    }
}
