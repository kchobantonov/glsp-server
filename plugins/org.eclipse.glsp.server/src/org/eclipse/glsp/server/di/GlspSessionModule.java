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

import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.actions.ActionHandlerRegistry;
import org.eclipse.glsp.server.internal.action.DefaultActionDispatcherV2;
import org.eclipse.glsp.server.internal.di.DIActionHandlerRegistry;
import org.eclipse.glsp.server.internal.di.DIOperationHandlerRegistry;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.model.GModelStateImpl;
import org.eclipse.glsp.server.operations.OperationHandlerRegistry;

import com.google.inject.Singleton;

public class GlspSessionModule extends AbstractGlspModule {

   @Override
   protected void configure() {
      bind(GModelState.class).to(bindGModelState()).in(Singleton.class);
      bind(ActionDispatcher.class).to(bindActionDispatcher()).in(Singleton.class);
      bind(ActionHandlerRegistry.class).to(bindActionHandlerRegistry()).in(Singleton.class);
      bind(OperationHandlerRegistry.class).to(bindOperationHandlerRegistry()).in(Singleton.class);

   }

   protected Class<? extends OperationHandlerRegistry> bindOperationHandlerRegistry() {
      return DIOperationHandlerRegistry.class;
   }

   protected Class<? extends ActionDispatcher> bindActionDispatcher() {
      return DefaultActionDispatcherV2.class;
   }

   protected Class<? extends GModelState> bindGModelState() {
      return GModelStateImpl.class;
   }

   protected Class<? extends ActionHandlerRegistry> bindActionHandlerRegistry() {
      return DIActionHandlerRegistry.class;
   }

}
