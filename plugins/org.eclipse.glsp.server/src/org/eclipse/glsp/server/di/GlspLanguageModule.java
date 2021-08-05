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
package org.eclipse.glsp.server.di;

import static org.eclipse.glsp.server.actions.ClientActionHandler.CLIENT_ACTIONS;

import java.util.Optional;

import org.eclipse.glsp.graph.GraphExtension;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.ActionHandlerRegistry;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.actions.InitializeClientSessionActionHandler;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProvider;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProviderRegistry;
import org.eclipse.glsp.server.features.directediting.ContextEditValidator;
import org.eclipse.glsp.server.features.directediting.ContextEditValidatorRegistry;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProvider;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProviderRegistry;
import org.eclipse.glsp.server.internal.action.DefaultActionDispatcherV2;
import org.eclipse.glsp.server.internal.di.DIActionHandlerRegistry;
import org.eclipse.glsp.server.internal.di.DIActionRegistryConfigurator;
import org.eclipse.glsp.server.internal.di.DIContextActionsProviderRegistry;
import org.eclipse.glsp.server.internal.di.DINavigationTargetProviderRegistry;
import org.eclipse.glsp.server.internal.di.DIOperationHandlerRegistry;
import org.eclipse.glsp.server.json.GGraphGsonConfiguration;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.model.GModelStateImpl;
import org.eclipse.glsp.server.operations.OperationHandler;
import org.eclipse.glsp.server.operations.OperationHandlerRegistry;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Singleton;
import com.google.inject.multibindings.OptionalBinder;

public abstract class GlspLanguageModule extends AbstractGlspModule {

   public abstract String getLanguageId();

   @Override
   protected void configure() {

      bind(DiagramConfiguration.class).to(bindDiagramConfiguration()).in(Singleton.class);
      bind(GGraphGsonConfiguration.class).to(bindGGraphGsonConfiguration()).in(Singleton.class);
      bind(ActionRegistryConfigurator.class).to(bindActionRegistryConfigurator()).in(Singleton.class);
      OptionalBinder.newOptionalBinder(binder(), GraphExtension.class);
      Optional.ofNullable(bindGraphExtension()).ifPresent(ext -> bind(GraphExtension.class).to(ext));

      // bind(ModelSourceLoader.class).to(bindSourceModelLoader());
      // bind(GModelFactory.class).to(bindGModelFactory());
      // bind(ModelSourceWatcher.class).to(bindModelSourceWatcher()).in(Singleton.class);
      // bind(PopupModelFactory.class).to(bindPopupModelFactory());
      // bind(ILayoutEngine.class).to(bindLayoutEngine());
      // bind(ModelValidator.class).to(bindModelValidator());
      // bind(ActionDispatcher.class).to(bindActionDispatcher()).in(Singleton.class);
      // bind(LabelEditValidator.class).to(bindLabelEditValidator());
      // bind(ToolPaletteItemProvider.class).to(bindToolPaletteItemProvider());
      // bind(CommandPaletteActionProvider.class).to(bindCommandPaletteActionProvider());
      // bind(ContextMenuItemProvider.class).to(bindContextMenuItemProvider());
      // bind(NavigationTargetResolver.class).to(bindNavigationTargetResolver());

      // configureMultiBinding();

      bind(GModelState.class).to(bindGModelState()).in(Singleton.class);
      bind(ActionDispatcher.class).to(bindActionDispatcher());
      bind(OperationHandlerRegistry.class).to(bindOperationHandlerRegistry()).in(Singleton.class);
      bind(ActionHandlerRegistry.class).to(bindActionHandlerRegistry()).in(Singleton.class);

      configureMultiBindings();
      // bind(ContextActionsProviderRegistry.class).to(bindContextActionsProviderRegistry()).in(Singleton.class);
      // bind(ContextEditValidatorRegistry.class).to(bindContextEditValidatorRegistry()).in(Singleton.class);
      // bind(NavigationTargetProviderRegistry.class).to(bindNavigationTargetProviderRegistry()).in(Singleton.class);

   }

   protected void configureMultiBindings() {
      configure(MultiBinding.create(Action.class).setAnnotationName(CLIENT_ACTIONS), this::configureClientActions);

      // Configure multibindings and the corresponding registries
      configure(MultiBinding.create(ActionHandler.class), this::configureActionHandlers);

      configure(MultiBinding.create(OperationHandler.class), this::configureOperationHandlers);

      configure(MultiBinding.create(ContextActionsProvider.class), this::configureContextActionsProviders);

      configure(MultiBinding.create(ContextEditValidator.class), this::configureContextEditValidators);

      configure(MultiBinding.create(NavigationTargetProvider.class), this::configureNavigationTargetProviders);

   }

   protected void configureClientActions(final MultiBinding<Action> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_CLIENT_ACTIONS);
   }

   public void configureActionHandlers(final MultiBinding<ActionHandler> binding) {
      binding.add(InitializeClientSessionActionHandler.class);
      // binding.addAll(MultiBindingDefaults.DEFAULT_ACTION_HANDLERS);
   }

   public void configureOperationHandlers(final MultiBinding<OperationHandler> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_OPERATION_HANDLERS);
   }

   protected void configureContextActionsProviders(final MultiBinding<ContextActionsProvider> binding) {}

   protected void configureContextEditValidators(final MultiBinding<ContextEditValidator> binding) {}

   protected void configureNavigationTargetProviders(final MultiBinding<NavigationTargetProvider> binding) {}

   protected Class<? extends ActionDispatcher> bindActionDispatcher() {
      return DefaultActionDispatcherV2.class;
   }

   protected Class<? extends GModelState> bindGModelState() {
      return GModelStateImpl.class;
   }

   protected Class<? extends ActionHandlerRegistry> bindActionHandlerRegistry() {
      return DIActionHandlerRegistry.class;
   }

   protected Class<? extends OperationHandlerRegistry> bindOperationHandlerRegistry() {
      return DIOperationHandlerRegistry.class;
   }

   protected Class<? extends ContextActionsProviderRegistry> bindContextActionsProviderRegistry() {
      return DIContextActionsProviderRegistry.class;
   }

   protected Class<? extends NavigationTargetProviderRegistry> bindNavigationTargetProviderRegistry() {
      return DINavigationTargetProviderRegistry.class;
   }

   protected Class<? extends ContextEditValidatorRegistry> bindContextEditValidatorRegistry() {
      return ContextEditValidatorRegistry.class;
   }

   protected Class<? extends ActionRegistryConfigurator> bindActionRegistryConfigurator() {
      return DIActionRegistryConfigurator.class;
   }

   protected abstract Class<? extends GGraphGsonConfiguration> bindGGraphGsonConfiguration();

   //
   protected abstract Class<? extends DiagramConfiguration> bindDiagramConfiguration();

   //
   // protected abstract Class<? extends ModelSourceLoader> bindSourceModelLoader();
   //
   // protected Class<? extends ModelSourceWatcher> bindModelSourceWatcher() {
   // return ModelSourceWatcher.NullImpl.class;
   // }
   //
   // protected abstract Class<? extends GModelFactory> bindGModelFactory();
   //
   protected abstract Class<? extends GraphExtension> bindGraphExtension();

   //
   // protected Class<? extends PopupModelFactory> bindPopupModelFactory() {
   // return PopupModelFactory.NullImpl.class;
   // }
   //
   // protected Class<? extends ILayoutEngine> bindLayoutEngine() {
   // return ILayoutEngine.NullImpl.class;
   // }
   //
   // protected Class<? extends ModelValidator> bindModelValidator() {
   // return ModelValidator.NullImpl.class;
   // }
   //
   // protected Class<? extends ActionDispatcher> bindActionDispatcher() {
   // return ActionDispatcher.NullImpl.class;
   // }
   //
   // protected Class<? extends LabelEditValidator> bindLabelEditValidator() {
   // return LabelEditValidator.NullImpl.class;
   // }
   //
   // protected Class<? extends CommandPaletteActionProvider> bindCommandPaletteActionProvider() {
   // return CommandPaletteActionProvider.NullImpl.class;
   // }
   //
   // protected Class<? extends ContextMenuItemProvider> bindContextMenuItemProvider() {
   // return ContextMenuItemProvider.NullImpl.class;
   // }
   //
   // protected Class<? extends ToolPaletteItemProvider> bindToolPaletteItemProvider() {
   // return ToolPaletteItemProvider.NullImpl.class;
   // }
   //
   // protected Class<? extends NavigationTargetResolver> bindNavigationTargetResolver() {
   // return NavigationTargetResolver.NullImpl.class;
   // }
   //

}
