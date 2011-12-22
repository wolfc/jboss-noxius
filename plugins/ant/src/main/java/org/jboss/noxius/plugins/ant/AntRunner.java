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
package org.jboss.noxius.plugins.ant;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.File;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class AntRunner {
    public static void ant(final String antFile, final String baseDir, final String target) {
        final Project project = new Project();
        project.init();
        ProjectHelper.configureProject(project, new File(new File(baseDir), antFile));
        project.setJavaVersionProperty();
        final DefaultLogger logger = new DefaultLogger();
        logger.setErrorPrintStream(System.err);
        logger.setOutputPrintStream(System.out);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        project.addBuildListener(logger);
        project.initProperties();
        project.setBasedir(baseDir);
        project.resolveFile(antFile);
        project.executeTarget(target);
    }
}
