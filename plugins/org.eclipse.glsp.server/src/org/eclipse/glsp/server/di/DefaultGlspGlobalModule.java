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
import org.eclipse.glsp.server.internal.di.DefaultActionRegistry;
import org.eclipse.glsp.server.jsonrpc.DefaultClientSessionManager;
import org.eclipse.glsp.server.jsonrpc.DefaultGLSPServerV2;
import org.eclipse.glsp.server.protocol.ClientSessionManager;
import org.eclipse.glsp.server.protocol.GLSPServer;

public abstract class DefaultGlspGlobalModule extends GLSPGlobalModule {

   @Override
   protected Class<? extends ClientSessionManager> bindClientSessionManager() {
      return DefaultClientSessionManager.class;
   }

   @SuppressWarnings("rawtypes")
   @Override
   protected Class<? extends GLSPServer> bindGLSPServer() {
      return DefaultGLSPServerV2.class;
   }

   @Override
   protected Class<? extends GLSPInjectorProvider> bindGLSPInjectorProvider() {
      return DefaultGLSPInjectorProvider.class;
   }

   @Override
   protected Class<? extends ActionRegistry> configureActionRegistry() {
      return DefaultActionRegistry.class;
   }

}
