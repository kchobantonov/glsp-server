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

import java.util.function.Consumer;

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.actions.InitializeClientSessionActionHandler;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.internal.di.DIActionRegistryConfigurator;
import org.eclipse.glsp.server.operations.OperationHandler;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public abstract class GlspLanguageModule extends AbstractModule {

   public abstract String getLanguageId();

   @Override
   protected void configure() {
      bind(DiagramConfiguration.class).to(bindDiagramConfiguration()).in(Singleton.class);
      bind(ActionRegistryConfigurator.class).to(bindActionRegistryConfigurator());
      configureMultiBinding();
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
      // bind(GraphGsonConfiguratorFactory.class).to(bindGraphGsonConfiguratorFactory());

      // configureMultiBinding();
      //
      // OptionalBinder.newOptionalBinder(binder(), GraphExtension.class);
      // Optional.ofNullable(bindGraphExtension()).ifPresent(ext -> bind(GraphExtension.class).to(ext));

   }

   public void configureMultiBinding() {
      // Configure multibindings and the corresponding registries
      configure(MultiBinding.create(Action.class).setAnnotationName(CLIENT_ACTIONS), this::configureClientActions);

      configure(MultiBinding.create(ActionHandler.class), this::configureActionHandlers);

      configure(MultiBinding.create(OperationHandler.class), this::configureOperationHandlers);
      //
      // configure(MultiBinding.create(ContextActionsProvider.class), this::configureContextActionsProviders);
      // bind(ContextActionsProviderRegistry.class).to(bindContextActionsProviderRegistry()).in(Singleton.class);
      //
      // configure(MultiBinding.create(ContextEditValidator.class), this::configureContextEditValidators);
      // bind(ContextEditValidatorRegistry.class).to(bindContextEditValidatorRegistry()).in(Singleton.class);
      //
      // configure(MultiBinding.create(NavigationTargetProvider.class), this::configureNavigationTargetProviders);
      // bind(NavigationTargetProviderRegistry.class).to(bindNavigationTargetProviderRegistry()).in(Singleton.class);
   }

   public <T> void configure(final MultiBinding<T> binding, final Consumer<MultiBinding<T>> configurator) {
      configurator.accept(binding);
      binding.applyBinding(binder());
   }

   public void configureClientActions(final MultiBinding<Action> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_CLIENT_ACTIONS);
   }

   public void configureActionHandlers(final MultiBinding<ActionHandler> binding) {
      binding.add(InitializeClientSessionActionHandler.class);
      // binding.addAll(MultiBindingDefaults.DEFAULT_ACTION_HANDLERS);
   }

   public void configureOperationHandlers(final MultiBinding<OperationHandler> binding) {
      // binding.addAll(MultiBindingDefaults.DEFAULT_OPERATION_HANDLERS);
   }

   // protected void configureContextActionsProviders(final MultiBinding<ContextActionsProvider> binding) {}
   //
   // protected void configureContextEditValidators(final MultiBinding<ContextEditValidator> binding) {}
   //
   // protected void configureNavigationTargetProviders(final MultiBinding<NavigationTargetProvider> binding) {}

   //
   protected abstract Class<? extends DiagramConfiguration> bindDiagramConfiguration();

   protected Class<? extends ActionRegistryConfigurator> bindActionRegistryConfigurator() {
      return DIActionRegistryConfigurator.class;
   }

   //
   // protected abstract Class<? extends ModelSourceLoader> bindSourceModelLoader();
   //
   // protected Class<? extends ModelSourceWatcher> bindModelSourceWatcher() {
   // return ModelSourceWatcher.NullImpl.class;
   // }
   //
   // protected abstract Class<? extends GModelFactory> bindGModelFactory();
   //
   // protected abstract Class<? extends GraphExtension> bindGraphExtension();
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
   // public abstract Class<? extends GraphGsonConfiguratorFactory> bindGraphGsonConfiguratorFactory();
   //

   // protected abstract Class<? extends OperationHandlerRegistry> bindOperationHandlerRegistry();
   //
   // protected abstract Class<? extends ContextActionsProviderRegistry> bindContextActionsProviderRegistry();
   //
   // protected abstract Class<? extends NavigationTargetProviderRegistry> bindNavigationTargetProviderRegistry();
   //
   // protected abstract Class<? extends ContextEditValidatorRegistry> bindContextEditValidatorRegistry();

}
