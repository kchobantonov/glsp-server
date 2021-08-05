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

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.ActionHandlerRegistry;
import org.eclipse.glsp.server.actions.InitializeClientSessionActionHandler;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProvider;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProviderRegistry;
import org.eclipse.glsp.server.features.directediting.ContextEditValidator;
import org.eclipse.glsp.server.features.directediting.ContextEditValidatorRegistry;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProvider;
import org.eclipse.glsp.server.features.navigation.NavigationTargetProviderRegistry;
import org.eclipse.glsp.server.internal.action.DefaultActionDispatcherV2;
import org.eclipse.glsp.server.internal.di.DIActionHandlerRegistry;
import org.eclipse.glsp.server.internal.di.DIContextActionsProviderRegistry;
import org.eclipse.glsp.server.internal.di.DINavigationTargetProviderRegistry;
import org.eclipse.glsp.server.internal.di.DIOperationHandlerRegistry;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.model.GModelStateImpl;
import org.eclipse.glsp.server.operations.OperationHandler;
import org.eclipse.glsp.server.operations.OperationHandlerRegistry;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Singleton;

public class GlspSessionModule extends AbstractGlspModule {

   @Override
   protected void configure() {
      bind(GModelState.class).to(bindGModelState()).in(Singleton.class);
      bind(ActionDispatcher.class).to(bindActionDispatcher()).in(Singleton.class);
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

}
