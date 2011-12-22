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

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class NoxiusFileManager extends ForwardingJavaFileManager<JavaFileManager> {
//    private static final URLClassLoader BOOT_CLASS_LOADER = new URLClassLoader(Launcher.getBootstrapClassPath().getURLs());
    private Collection<URLJavaFileObject> sources = new LinkedList<URLJavaFileObject>();
    private Map<String, InMemJavaFileObject> classes = new HashMap<String, InMemJavaFileObject>();

    /**
     * Creates a new instance of NoxiusFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    NoxiusFileManager(final JavaFileManager fileManager, final Iterable<URLJavaFileObject> sources) {
        super(fileManager);
        for (final URLJavaFileObject source : sources) {
            this.sources.add(source);
        }
    }

    private static CharSequence args(final Object... args) {
        final StringBuilder sb = new StringBuilder("(");
        for (int i = 0; args != null && i < args.length; i++) {
            sb.append(args[i]);
            if (i < (args.length - 1))
                sb.append(", ");
        }
        sb.append(")");
        return sb;
    }

    public ClassLoader getClassLoader(Location location) {
//        System.out.println("NoxiusFileManager.getClassLoader(" + location + ")");
        final ClassLoader classLoader;
        // we could, but we don't
        if (location == StandardLocation.CLASS_OUTPUT)
            throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.getClassLoader(" + location + ")");
//        else if (location == StandardLocation.CLASS_PATH)
//            classLoader = Thread.currentThread().getContextClassLoader();
        else
            classLoader = super.getClassLoader(location);
//        System.out.println("classLoader = " + Arrays.toString(((URLClassLoader) classLoader).getURLs()));
        return classLoader;
    }

    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
//        System.out.println("list(" + location + ", " + packageName + ", " + kinds + ", " + recurse + ")");
        final Collection<JavaFileObject> result = new LinkedList<JavaFileObject>();
        if (location == StandardLocation.SOURCE_PATH && kinds.contains(JavaFileObject.Kind.SOURCE)) {
            for (final URLJavaFileObject source : sources) {
                if (source.binaryName.startsWith(packageName))
                    result.add(source);
            }
        }
        final Iterable<JavaFileObject> others = super.list(location, packageName, kinds, recurse);
        for (JavaFileObject other : others) {
            result.add(other);
        }
        return result;
    }

    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof URLJavaFileObject)
            return ((URLJavaFileObject) file).binaryName;
        return super.inferBinaryName(location, file);
    }

    public boolean isSameFile(FileObject a, FileObject b) {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.isSameFile");
    }

    public boolean handleOption(String current, Iterator<String> remaining) {
        //throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.handleOption");
        return super.handleOption(current, remaining);
    }

    public boolean hasLocation(Location location) {
        if (location == StandardLocation.SOURCE_PATH)
            return true;
        if (location == StandardLocation.CLASS_OUTPUT)
            return true;
        return super.hasLocation(location);
    }

    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
//        System.out.println("getJavaFileForInput(" + location + ", " + className + ", " + kind + ")");
        if (location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS) {
            return classes.get(className);
        }
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.getJavaFileForInput");
    }

    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS) {
            final InMemJavaFileObject classFile = new InMemJavaFileObject(className);
            classes.put(className, classFile);
            return classFile;
        }
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.getJavaFileForOutput" + args(location, className, kind, sibling));
    }

    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.getFileForInput");
    }

    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.getFileForOutput");
    }

//    public void flush() throws IOException {
//        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.flush");
//    }

    public void close() throws IOException {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.close");
    }

    public int isSupportedOption(String option) {
        throw new RuntimeException("NYI: org.jboss.noxius.bootstrap.NoxiusFileManager.isSupportedOption");
    }
}
