/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.eclipse.glsp.example.workflow;

import org.eclipse.glsp.graph.GraphExtension;
import org.eclipse.glsp.server.di.GlspLanguageModule;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.internal.json.DefaulGGraphGsonConfiguration;
import org.eclipse.glsp.server.json.GGraphGsonConfiguration;

public class WorkflowLanguageModule extends GlspLanguageModule {

   public static final String LANGUAGE_ID = "workflow";

   @Override
   protected Class<? extends DiagramConfiguration> bindDiagramConfiguration() {
      return WorkflowDiagramConfiguration.class;
   }

   @Override
   public String getLanguageId() { return LANGUAGE_ID; }

   @Override
   protected Class<? extends GraphExtension> bindGraphExtension() {
      return WFGraphExtension.class;
   }

   @Override
   protected Class<? extends GGraphGsonConfiguration> bindGGraphGsonConfiguration() {
      return DefaulGGraphGsonConfiguration.class;
   }

}