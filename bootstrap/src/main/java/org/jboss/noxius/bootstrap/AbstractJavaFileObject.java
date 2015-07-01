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
package org.jboss.noxius.bootstrap;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class AbstractJavaFileObject implements JavaFileObject {
    protected final String name;
    protected final URI uri;
    protected final Kind kind;

    protected AbstractJavaFileObject(final String name, final URI uri, final Kind kind) {
        System.err.println(name + " : " + uri.getPath());
        this.name = name;
        this.uri = uri;
        this.kind = kind;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    /**
     * @see javax.tools.SimpleJavaFileObject#isNameCompatible
     */
    @Override
    public boolean isNameCompatible(final String simpleName, final Kind kind) {
        String baseName = simpleName + kind.extension;
        return kind.equals(getKind())
                && (baseName.equals(name)
                || name.endsWith("/" + baseName));
    }

    @Override
    public NestingKind getNestingKind() {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.getNestingKind");
    }

    @Override
    public Modifier getAccessLevel() {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.getAccessLevel");
    }

    @Override
    public URI toUri() {
        return uri;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.openInputStream");
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.openOutputStream");
    }

    @Override
    public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.openReader");
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.getCharContent");
    }

    @Override
    public Writer openWriter() throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.openWriter");
    }

    @Override
    public long getLastModified() {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.getLastModified");
    }

    @Override
    public boolean delete() {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.AbstractJavaFileObject.delete");
    }
}
