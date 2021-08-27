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
import org.eclipse.glsp.server.diagram.DiagramModuleRegistry;
import org.eclipse.glsp.server.diagram.gson.GGraphGsonConfiguratorFactory;
import org.eclipse.glsp.server.diagram.gson.GsonConfigurator;
import org.eclipse.glsp.server.internal.di.DIActionRegistry;
import org.eclipse.glsp.server.internal.di.DIDiagramModuleRegistry;
import org.eclipse.glsp.server.internal.json.DefaultGGraphGsonConfiguratorFactory;
import org.eclipse.glsp.server.internal.json.DefaultGsonConfigurator;
import org.eclipse.glsp.server.jsonrpc.ClientSessionFactory;
import org.eclipse.glsp.server.jsonrpc.DefaultClientSessionFactory;
import org.eclipse.glsp.server.jsonrpc.DefaultClientSessionManager;
import org.eclipse.glsp.server.jsonrpc.DefaultGLSPServer;
import org.eclipse.glsp.server.protocol.ClientConnectionListener;
import org.eclipse.glsp.server.protocol.ClientSessionListener;
import org.eclipse.glsp.server.protocol.ClientSessionManager;
import org.eclipse.glsp.server.protocol.GLSPClient;
import org.eclipse.glsp.server.protocol.GLSPServer;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Provides;
import com.google.inject.Singleton;

public abstract class GLSPServerModule extends GLSPModule {

   @Override
   protected void configure() {
      bind(GLSPServer.class).to(bindGLSPServer());
      bind(ClientSessionManager.class).to(bindClientSessionManager()).in(Singleton.class);
      bind(ClientSessionFactory.class).to(bindClientSessionFactory());
      configure(MultiBinding.create(ClientConnectionListener.class), this::configureClientConnectionListeners);
      configure(MultiBinding.create(ClientSessionListener.class), this::configureClientSessionListeners);

      bind(ActionRegistry.class).to(bindActionRegistry()).in(Singleton.class);

      bind(GsonConfigurator.class).to(bindGsonConfigurator()).in(Singleton.class);
      bind(GGraphGsonConfiguratorFactory.class).to(bindGGraphGsonConfiguratorFactory());

      configure(MultiBinding.create(GLSPDiagramModule.class), this::configureDiagramModules);
      bind(DiagramModuleRegistry.class).to(bindDiagramRegistry()).in(Singleton.class);

   }

   public abstract void configureDiagramModules(MultiBinding<GLSPDiagramModule> binding);

   public void configureClientConnectionListeners(final MultiBinding<ClientConnectionListener> binding) {

   }

   public void configureClientSessionListeners(final MultiBinding<ClientSessionListener> binding) {

   }

   protected Class<? extends ClientSessionManager> bindClientSessionManager() {
      return DefaultClientSessionManager.class;
   }

   protected Class<? extends ClientSessionFactory> bindClientSessionFactory() {
      return DefaultClientSessionFactory.class;
   }

   protected Class<? extends ActionRegistry> bindActionRegistry() {
      return DIActionRegistry.class;
   }

   protected Class<? extends DiagramModuleRegistry> bindDiagramRegistry() {
      return DIDiagramModuleRegistry.class;
   }

   protected Class<? extends GGraphGsonConfiguratorFactory> bindGGraphGsonConfiguratorFactory() {
      return DefaultGGraphGsonConfiguratorFactory.class;
   }

   protected Class<? extends GLSPServer> bindGLSPServer() {
      return DefaultGLSPServer.class;
   }

   protected Class<? extends GsonConfigurator> bindGsonConfigurator() {
      return DefaultGsonConfigurator.class;
   }

   @Provides
   protected GLSPClient getGLSPClient(final GLSPServer glspServer) {
      return glspServer.getClient();
   }
}
