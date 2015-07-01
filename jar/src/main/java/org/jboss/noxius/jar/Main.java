/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.noxius.jar;

import org.jboss.noxius.bootstrap.NoxiusCompileException;
import org.jboss.noxius.bootstrap.NoxiusCompiler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Main {
    // TODO: integrate with Bootstrap
    private static int exec(final URL noxiusResource, final String noxiusClass, final String[] args) throws ClassNotFoundException, NoxiusCompileException, URISyntaxException, NoSuchMethodException, MalformedURLException {
        final NoxiusCompiler noxius = new NoxiusCompiler();
        final Class<?> bootstrap = noxius.compile(noxiusResource, noxiusClass);
        final Method main = bootstrap.getMethod("main", String[].class);
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
    }

    public static void main(final String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, NoxiusCompileException, URISyntaxException {
        final URL resource = Main.class.getResource("/" + Main.class.getName().replace(".", "/") + ".class");
        final String noxiusMain = ((JarURLConnection) resource.openConnection()).getManifest().getMainAttributes().getValue("Noxius-Main");
        final URL noxiusResource = Main.class.getResource("/" + noxiusMain + ".java");
        if (noxiusResource == null)
            throw new IllegalArgumentException("Failed to find Noxius file " + noxiusMain);
        System.exit(exec(noxiusResource, noxiusMain, args));
    }
}
