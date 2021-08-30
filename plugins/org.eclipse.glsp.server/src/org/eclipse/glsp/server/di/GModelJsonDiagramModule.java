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

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.features.core.model.GModelFactory;
import org.eclipse.glsp.server.features.core.model.JsonFileGModelLoader;
import org.eclipse.glsp.server.features.core.model.ModelSourceLoader;
import org.eclipse.glsp.server.internal.di.MultiBindingDefaults;
import org.eclipse.glsp.server.utils.MultiBinding;

public abstract class GModelJsonDiagramModule extends DiagramModule {

   @Override
   protected Class<? extends ModelSourceLoader> bindSourceModelLoader() {
      return JsonFileGModelLoader.class;
   }

   @Override
   protected Class<? extends GModelFactory> bindGModelFactory() {
      return GModelFactory.NullImpl.class;
   }

   @Override
   protected void configureClientActions(final MultiBinding<Action> binding) {
      binding.addAll(MultiBindingDefaults.DEFAULT_CLIENT_ACTIONS);
   }

   @Override
   protected void configureActionHandlers(final MultiBinding<ActionHandler> binding) {
      binding.addAll(MultiBindingDefaults.DEFAULT_ACTION_HANDLERS);
   }

}
