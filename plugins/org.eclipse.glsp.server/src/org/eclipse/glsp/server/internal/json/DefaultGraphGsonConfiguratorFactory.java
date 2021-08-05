/********************************************************************************
 * Copyright (c) 2019-2021 EclipseSource and others.
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
package org.eclipse.glsp.server.internal.json;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.glsp.graph.gson.GGraphGsonConfigurator;
import org.eclipse.glsp.server.di.GLSPInjector;
import org.eclipse.glsp.server.json.GGraphGsonConfiguration;
import org.eclipse.glsp.server.json.GGraphGsonConfiguratorFactory;

import com.google.inject.Inject;

public class DefaultGraphGsonConfiguratorFactory implements GGraphGsonConfiguratorFactory {

   private List<GGraphGsonConfiguration> ggraphConfigurations;

   @Inject()
   public void init(final GLSPInjector glspInjector) {
      String tempSessionId = "TEMPSESSION";
      ggraphConfigurations = glspInjector.getDiagramLanguageIds().stream()
         .map(languageId -> glspInjector.getInstance(GGraphGsonConfiguration.class, languageId, tempSessionId))
         .collect(Collectors.toList());
   }

   @Override
   public GGraphGsonConfigurator create() {
      GGraphGsonConfigurator configurator = new GGraphGsonConfigurator();
      ggraphConfigurations.forEach(configuration -> configuration.configure(configurator));
      return configurator;
   }
}
