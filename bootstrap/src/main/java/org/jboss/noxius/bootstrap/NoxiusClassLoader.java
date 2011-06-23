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

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class NoxiusClassLoader extends ClassLoader {
    private final NoxiusFileManager fileManager;

    NoxiusClassLoader(final ClassLoader parent, final NoxiusFileManager fileManager) {
        super(parent);
        this.fileManager = fileManager;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            final InMemJavaFileObject file = (InMemJavaFileObject) fileManager.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, name, JavaFileObject.Kind.CLASS);
            if (file != null) {
                byte[] bytes = file.getContents();
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
