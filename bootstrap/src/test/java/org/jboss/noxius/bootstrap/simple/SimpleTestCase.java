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
package org.jboss.noxius.bootstrap.simple;

import org.jboss.noxius.bootstrap.Noxius;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class SimpleTestCase {
    @Test
    public void test1() throws Exception {
        System.out.println(System.getProperty("java.class.path"));
        final URL resource = getClass().getResource("nox.java");
        final Noxius noxius = new Noxius();
        final String path = "org/jboss/noxius/bootstrap/simple/";
        final Class<?> noxClass = noxius.compile(resource, path.replace('/', '.') + "nox");
        final Method main = noxClass.getMethod("main", String[].class);
        // cast to make sure we call the varargs one
        main.invoke(null, (Object) new String[0]);
    }
}
