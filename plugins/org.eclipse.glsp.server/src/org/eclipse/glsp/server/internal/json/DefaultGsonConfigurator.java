/*******************************************************************************
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
 ******************************************************************************/
package org.eclipse.glsp.server.internal.json;

import org.eclipse.glsp.graph.gson.EnumTypeAdapter;
import org.eclipse.glsp.server.actions.ActionRegistry;
import org.eclipse.glsp.server.internal.json.ActionTypeAdapter.Factory;
import org.eclipse.glsp.server.json.GGraphGsonConfiguratorFactory;
import org.eclipse.glsp.server.json.GsonConfigurator;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.inject.Inject;

public class DefaultGsonConfigurator implements GsonConfigurator {

   private ActionRegistry actionRegistry;
   private GGraphGsonConfiguratorFactory gsonConfigurationFactory;

   @Inject
   public void init(final ActionRegistry actionRegistry,
      final GGraphGsonConfiguratorFactory gsonConfigurationFactory) {
      this.actionRegistry = actionRegistry;
      this.gsonConfigurationFactory = gsonConfigurationFactory;
   }

   protected TypeAdapterFactory getActionTypeAdapterFactory() {
      return new ActionTypeAdapter.Factory(actionRegistry.toMap());
   }

   @Override
   public GsonBuilder configureGsonBuilder(final GsonBuilder gsonBuilder) {
      gsonBuilder.registerTypeAdapterFactory(getActionTypeAdapterFactory());
      gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapter.Factory());
      return gsonConfigurationFactory.create().configureGsonBuilder(gsonBuilder);
   }

}
