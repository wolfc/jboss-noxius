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
package org.jboss.noxius;

import org.jboss.noxius.bootstrap.NoxiusCompileException;
import org.jboss.noxius.bootstrap.NoxiusCompiler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class NoxiusCompilationEnvironment implements CompilationEnvironment {

    private final ClassLoader classLoader;
    private final List<String> options = new ArrayList<String>();

    NoxiusCompilationEnvironment(final ClassLoader classLoader) {
        //options.add(OptionName.CLASSPATH.optionName);
        options.add("-classpath");
        final StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) classLoader).getURLs())
            sb.append(url.getFile()).append(File.pathSeparator);
//        for (URL url : ((URLClassLoader) NoxiusCompilationEnvironment.class.getClassLoader()).getURLs())
//            sb.append(url.getFile()).append(File.pathSeparator);
        options.add(sb.toString());
        this.classLoader = classLoader;
    }

    @Override
    public int execute(String fileName, String className, String... args) throws ClassNotFoundException, NoxiusCompileException, URISyntaxException, NoSuchMethodException, MalformedURLException {
        return execute(new File(fileName).toURI().toURL(), className, args);
    }

    @Override
    public int execute(final URL resource, final String className, final String... args) throws ClassNotFoundException, NoxiusCompileException, URISyntaxException, NoSuchMethodException {
        final ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            final NoxiusCompiler compiler = new NoxiusCompiler();
            final Class<?> nox = compiler.compile(resource, className, options);
            final Method main = nox.getMethod("main", String[].class);
            try {
                // cast to make sure we call the varargs one
                main.invoke(null, (Object) args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                final Throwable targetException = e.getTargetException();
                if (targetException instanceof Error)
                    throw (Error) targetException;
                if (targetException instanceof RuntimeException)
                    throw (RuntimeException) targetException;
                throw new RuntimeException(targetException);
            }
            return 0;
        } finally {
            Thread.currentThread().setContextClassLoader(previous);
        }
    }
}
