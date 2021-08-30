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
import org.eclipse.glsp.server.actions.ActionRegistryConfiguration;
import org.eclipse.glsp.server.di.scope.DiagramType;
import org.eclipse.glsp.server.di.scope.DiagramTypeScope;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.diagram.gson.GGraphGsonConfiguration;
import org.eclipse.glsp.server.features.commandpalette.CommandPaletteActionProvider;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProvider;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProviderRegistry;
import org.eclipse.glsp.server.features.contextmenu.ContextMenuItemProvider;
import org.eclipse.glsp.server.features.core.model.GModelFactory;
import org.eclipse.glsp.server.features.core.model.ModelSourceLoader;
import org.eclipse.glsp.server.features.directediting.ContextEditValidator;
import org.eclipse.glsp.server.features.directediting.ContextEditValidatorRegistry;
import org.eclipse.glsp.server.features.directediting.LabelEditValidator;
import org.eclipse.glsp.server.features.modelsourcewatcher.ModelSourceWatcher;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProvider;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProviderRegistry;
import org.eclipse.glsp.server.features.navigation.NavigationTargetResolver;
import org.eclipse.glsp.server.features.popup.PopupModelFactory;
import org.eclipse.glsp.server.features.toolpalette.ToolPaletteItemProvider;
import org.eclipse.glsp.server.features.validation.ModelValidator;
import org.eclipse.glsp.server.internal.action.DefaultActionDispatcher;
import org.eclipse.glsp.server.internal.di.DIActionHandlerRegistry;
import org.eclipse.glsp.server.internal.di.DIActionRegistryConfiguration;
import org.eclipse.glsp.server.internal.di.DIContextActionsProviderRegistry;
import org.eclipse.glsp.server.internal.di.DIContextEditValidatorRegistry;
import org.eclipse.glsp.server.internal.di.DINavigationTargetProviderRegistry;
import org.eclipse.glsp.server.internal.di.DIOperationHandlerRegistry;
import org.eclipse.glsp.server.internal.json.DefaultGGraphGsonConfiguration;
import org.eclipse.glsp.server.layout.LayoutEngine;
import org.eclipse.glsp.server.model.DefaultGModelState;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.operations.OperationHandler;
import org.eclipse.glsp.server.operations.OperationHandlerRegistry;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public abstract class DiagramModule extends GLSPModule {
   private String clientId = "";

   @Inject()
   protected DiagramTypeScope typeScope;

   @Override
   protected void configure() {
      bindBasics();

      // Configurations
      bind(DiagramConfiguration.class).to(bindDiagramConfiguration()).in(DiagramType.class);
      bind(GGraphGsonConfiguration.class).to(bindGGraphGsonConfiguration()).in(Singleton.class);
      bind(ActionRegistryConfiguration.class).to(bindActionRegistryConfiguration()).in(Singleton.class);

      // Model-related bindings
      bind(GModelState.class).to(bindGModelState()).in(Singleton.class);
      bind(ModelSourceLoader.class).to(bindSourceModelLoader());
      bind(GModelFactory.class).to(bindGModelFactory());
      bind(ModelSourceWatcher.class).to(bindModelSourceWatcher()).in(Singleton.class);

      // Model Validation
      bind(ModelValidator.class).to(bindModelValidator());
      bind(LabelEditValidator.class).to(bindLabelEditValidator());

      // ContextActionProviders
      bind(ToolPaletteItemProvider.class).to(bindToolPaletteItemProvider());
      bind(CommandPaletteActionProvider.class).to(bindCommandPaletteActionProvider());
      bind(ContextMenuItemProvider.class).to(bindContextMenuItemProvider());
      configure(MultiBinding.create(ContextActionsProvider.class), this::configureContextActionsProviders);
      bind(ContextActionsProviderRegistry.class).to(bindContextActionsProviderRegistry()).in(Singleton.class);

      // Action & Operation related bindings
      bind(ActionDispatcher.class).to(bindActionDispatcher()).in(Singleton.class);
      configure(MultiBinding.create(Action.class).setAnnotationName(CLIENT_ACTIONS), this::configureClientActions);
      configure(MultiBinding.create(ActionHandler.class), this::configureActionHandlers);
      bind(ActionHandlerRegistry.class).to(bindActionHandlerRegistry());
      configure(MultiBinding.create(OperationHandler.class), this::configureOperationHandlers);
      bind(OperationHandlerRegistry.class).to(bindOperationHandlerRegistry());

      // Navigation
      bind(NavigationTargetResolver.class).to(bindNavigationTargetResolver());
      configure(MultiBinding.create(NavigationTargetProvider.class), this::configureNavigationTargetProviders);
      bind(NavigationTargetProviderRegistry.class).to(bindNavigationTargetProviderRegistry()).in(Singleton.class);

      // ContextEdit
      configure(MultiBinding.create(ContextEditValidator.class), this::configureContextEditValidators);
      bind(ContextEditValidatorRegistry.class).to(bindContextEditValidatorRegistry()).in(Singleton.class);

      // Misc
      bind(PopupModelFactory.class).to(bindPopupModelFactory());
      bind(LayoutEngine.class).to(bindLayoutEngine());
      bindOptionally(GraphExtension.class, bindGraphExtension());

   }

   /**
    * Bind base classes to enable {@link DiagramType} based scoping and provide the
    * diagram type as named-injection. These bindings are essential to ensure that the GLSP server framework works as
    * expected and normally there should be no need for customisation. For corner cases it is possible to override the
    * implementation of this method. However, this can cause unintended side effects and is not recommended!
    */
   protected void bindBasics() {
      bindScope(DiagramType.class, typeScope);
      bind(String.class).annotatedWith(Names.named(DIAGRAM_TYPE)).toInstance(getDiagramType());
      bind(String.class).annotatedWith(Names.named(CLIENT_ID)).toInstance(getClientId());
   }

   protected abstract Class<? extends DiagramConfiguration> bindDiagramConfiguration();

   protected Class<? extends GGraphGsonConfiguration> bindGGraphGsonConfiguration() {
      return DefaultGGraphGsonConfiguration.class;
   }

   protected Class<? extends ActionRegistryConfiguration> bindActionRegistryConfiguration() {
      return DIActionRegistryConfiguration.class;
   }

   protected Class<? extends GModelState> bindGModelState() {
      return DefaultGModelState.class;
   }

   protected abstract Class<? extends ModelSourceLoader> bindSourceModelLoader();

   protected abstract Class<? extends GModelFactory> bindGModelFactory();

   protected Class<? extends ModelSourceWatcher> bindModelSourceWatcher() {
      return ModelSourceWatcher.NullImpl.class;
   }

   protected Class<? extends ModelValidator> bindModelValidator() {
      return ModelValidator.NullImpl.class;
   }

   protected Class<? extends LabelEditValidator> bindLabelEditValidator() {
      return LabelEditValidator.NullImpl.class;
   }

   protected Class<? extends ToolPaletteItemProvider> bindToolPaletteItemProvider() {
      return ToolPaletteItemProvider.NullImpl.class;
   }

   protected Class<? extends CommandPaletteActionProvider> bindCommandPaletteActionProvider() {
      return CommandPaletteActionProvider.NullImpl.class;
   }

   protected Class<? extends ContextMenuItemProvider> bindContextMenuItemProvider() {
      return ContextMenuItemProvider.NullImpl.class;
   }

   protected void configureContextActionsProviders(final MultiBinding<ContextActionsProvider> binding) {}

   protected Class<? extends ContextActionsProviderRegistry> bindContextActionsProviderRegistry() {
      return DIContextActionsProviderRegistry.class;
   }

   protected Class<? extends ActionDispatcher> bindActionDispatcher() {
      return DefaultActionDispatcher.class;
   }

   protected abstract void configureClientActions(MultiBinding<Action> binding);

   protected abstract void configureActionHandlers(MultiBinding<ActionHandler> binding);

   protected Class<? extends ActionHandlerRegistry> bindActionHandlerRegistry() {
      return DIActionHandlerRegistry.class;
   }

   protected void configureOperationHandlers(final MultiBinding<OperationHandler> binding) {}

   protected Class<? extends OperationHandlerRegistry> bindOperationHandlerRegistry() {
      return DIOperationHandlerRegistry.class;
   }

   protected Class<? extends NavigationTargetResolver> bindNavigationTargetResolver() {
      return NavigationTargetResolver.NullImpl.class;
   }

   protected void configureNavigationTargetProviders(final MultiBinding<NavigationTargetProvider> binding) {}

   protected Class<? extends NavigationTargetProviderRegistry> bindNavigationTargetProviderRegistry() {
      return DINavigationTargetProviderRegistry.class;
   }

   protected void configureContextEditValidators(final MultiBinding<ContextEditValidator> binding) {}

   protected Class<? extends ContextEditValidatorRegistry> bindContextEditValidatorRegistry() {
      return DIContextEditValidatorRegistry.class;
   }

   protected Class<? extends PopupModelFactory> bindPopupModelFactory() {
      return PopupModelFactory.NullImpl.class;
   }

   protected Class<? extends LayoutEngine> bindLayoutEngine() {
      return LayoutEngine.NullImpl.class;
   }

   protected Class<? extends GraphExtension> bindGraphExtension() {
      return null;
   }

   public abstract String getDiagramType();

   public String getClientId() { return clientId; }

   public void setClientId(final String clientId) { this.clientId = clientId; }

}
