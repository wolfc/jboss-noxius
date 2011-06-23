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

import javax.tools.SimpleJavaFileObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class URLJavaFileObject extends SimpleJavaFileObject {
    protected final URL url;
    protected final String binaryName;
    private URLConnection urlConnection;

    /**
     * Construct an URLJavaFileObject of the given kind and with the
     * given URL.
     *
     * @param url        the URL for this file object
     * @param binaryName the binary name of this file object
     */
    URLJavaFileObject(final URL url, final String binaryName) throws URISyntaxException {
        super(url.toURI(), Kind.SOURCE);
        this.url = url;
        this.binaryName = binaryName;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
//        final Class[] expectedContentType = { String.class };
//        final CharSequence charContent = (CharSequence) getURLConnection().getContent(expectedContentType);
        final StringBuilder sb = new StringBuilder();
        final InputStream in = new BufferedInputStream(getURLConnection().getInputStream());
        try {
            int c;
            while((c = in.read()) != -1)
                sb.append((char) c);
            return sb;
        } finally {
            in.close();
        }
    }

    protected URLConnection getURLConnection() throws IOException {
        if (urlConnection == null) {
            urlConnection = url.openConnection();
        }
        return urlConnection;
    }
}
