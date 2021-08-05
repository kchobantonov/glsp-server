/********************************************************************************
 * Copyright (c) 2021 EclipseSource and others.
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
package org.eclipse.glsp.server.di;

import org.eclipse.glsp.server.actions.ActionRegistry;
import org.eclipse.glsp.server.json.GGraphGsonConfiguratorFactory;
import org.eclipse.glsp.server.json.GsonConfigurator;
import org.eclipse.glsp.server.protocol.ClientSessionManager;
import org.eclipse.glsp.server.protocol.GLSPClient;
import org.eclipse.glsp.server.protocol.GLSPServer;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Provides;
import com.google.inject.Singleton;

public abstract class GLSPGlobalModule extends AbstractGlspModule {

   @Override
   protected void configure() {
      configureMultiBinding();
      bind(GLSPServer.class).to(bindGLSPServer()).in(Singleton.class);
      bind(ClientSessionManager.class).to(bindClientSessionManager()).in(Singleton.class);
      bind(ActionRegistry.class).to(bindActionRegistry()).in(Singleton.class);
      bind(GLSPInjectorProvider.class).to(bindGLSPInjectorProvider()).in(Singleton.class);
      bind(GsonConfigurator.class).to(bindGsonConfigurator());
      bind(GGraphGsonConfiguratorFactory.class).to(bindGGraphGsonConfiguratorFactory());
   }

   public void configureMultiBinding() {
      configure(MultiBinding.create(GlspLanguageModule.class), this::configureGlspLanguageModule);
   }

   public abstract void configureGlspLanguageModule(MultiBinding<GlspLanguageModule> binding);

   protected abstract Class<? extends ClientSessionManager> bindClientSessionManager();

   protected abstract Class<? extends GLSPInjectorProvider> bindGLSPInjectorProvider();

   protected abstract Class<? extends ActionRegistry> bindActionRegistry();

   protected abstract Class<? extends GsonConfigurator> bindGsonConfigurator();

   protected abstract Class<? extends GGraphGsonConfiguratorFactory> bindGGraphGsonConfiguratorFactory();

   @SuppressWarnings("rawtypes")
   protected abstract Class<? extends GLSPServer> bindGLSPServer();

   @Provides
   @SuppressWarnings("rawtypes")
   private GLSPClient getGLSPClient(final GLSPServer glspServer) {
      return glspServer.getClient();
   }

}
