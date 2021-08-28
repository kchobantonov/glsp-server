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

import org.eclipse.glsp.graph.GraphExtension;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.ActionHandlerRegistry;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.di.scope.DiagramType;
import org.eclipse.glsp.server.di.scope.DiagramTypeScope;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.diagram.gson.GGraphGsonConfiguration;
import org.eclipse.glsp.server.internal.action.DefaultActionDispatcher;
import org.eclipse.glsp.server.internal.di.DIActionHandlerRegistry;
import org.eclipse.glsp.server.internal.di.DIActionRegistryConfigurator;
import org.eclipse.glsp.server.internal.di.DIOperationHandlerRegistry;
import org.eclipse.glsp.server.internal.di.MultiBindingDefaults;
import org.eclipse.glsp.server.internal.json.DefaultGGraphGsonConfiguration;
import org.eclipse.glsp.server.model.DefaultGModelState;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.operations.OperationHandler;
import org.eclipse.glsp.server.operations.OperationHandlerRegistry;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public abstract class GLSPDiagramModule extends GLSPModule {
   public static final String CLIENT_ACTIONS = "ClientActions";
   public static final String DIAGRAM_TYPE = "DiagramType";

   public abstract String getDiagramType();

   @Inject()
   protected DiagramTypeScope diagramScope;

   @Override
   protected void configure() {
      bindScope(DiagramType.class, diagramScope);

      bind(String.class).annotatedWith(Names.named(DIAGRAM_TYPE)).toInstance(getDiagramType());
      bind(DiagramConfiguration.class).to(bindDiagramConfiguration()).in(DiagramType.class);
      bind(GGraphGsonConfiguration.class).to(bindGGraphGsonConfiguration()).in(Singleton.class);
      bind(ActionRegistryConfigurator.class).to(bindActionRegistryConfigurator()).in(Singleton.class);

      bind(GModelState.class).to(bindGModelState()).in(Singleton.class);
      bind(ActionDispatcher.class).to(bindActionDispatcher()).in(Singleton.class);
      bindOptionally(GraphExtension.class, bindGraphExtension());

      configure(MultiBinding.create(Action.class).setAnnotationName(CLIENT_ACTIONS), this::configureClientActions);

      configure(MultiBinding.create(ActionHandler.class), this::configureActionHandlers);
      bind(ActionHandlerRegistry.class).to(bindActionHandlerRegistry());

      configure(MultiBinding.create(OperationHandler.class), this::configureOperationHandlers);
      bind(OperationHandlerRegistry.class).to(bindOperationHandlerRegistry());

   }

   protected void configureClientActions(final MultiBinding<Action> binding) {
      binding.addAll(MultiBindingDefaults.DEFAULT_CLIENT_ACTIONS);
   }

   public void configureActionHandlers(final MultiBinding<ActionHandler> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_ACTION_HANDLERS);
   }

   public void configureOperationHandlers(final MultiBinding<OperationHandler> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_OPERATION_HANDLERS);
   }

   protected Class<? extends ActionHandlerRegistry> bindActionHandlerRegistry() {
      return DIActionHandlerRegistry.class;
   }

   protected Class<? extends OperationHandlerRegistry> bindOperationHandlerRegistry() {
      return DIOperationHandlerRegistry.class;
   }

   protected Class<? extends ActionDispatcher> bindActionDispatcher() {
      return DefaultActionDispatcher.class;
   }

   protected Class<? extends GModelState> bindGModelState() {
      return DefaultGModelState.class;
   }

   protected Class<? extends ActionRegistryConfigurator> bindActionRegistryConfigurator() {
      return DIActionRegistryConfigurator.class;
   }

   protected Class<? extends GGraphGsonConfiguration> bindGGraphGsonConfiguration() {
      return DefaultGGraphGsonConfiguration.class;
   }

   protected abstract Class<? extends DiagramConfiguration> bindDiagramConfiguration();

   protected Class<? extends GraphExtension> bindGraphExtension() {
      return null;
   }

}
