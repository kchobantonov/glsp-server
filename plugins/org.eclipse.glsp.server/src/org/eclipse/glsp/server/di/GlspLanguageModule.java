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

import java.util.Optional;

import org.eclipse.glsp.graph.GraphExtension;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.internal.di.DIActionRegistryConfigurator;
import org.eclipse.glsp.server.json.GGraphGsonConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.OptionalBinder;

public abstract class GlspLanguageModule extends AbstractModule {
   public static final String SHARED_ACTION_HANDLERS = "SharedActionHandlers";
   public static final String SHARED_OPERATION_HANDLERS = "SharedActionHandlers";

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

      bind(GlspSessionModule.class);

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
