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
package org.eclipse.glsp.server.jsonrpc;

import static org.eclipse.glsp.server.protocol.GLSPServerException.getOrThrow;

import org.eclipse.glsp.server.di.GLSPDiagramModule;
import org.eclipse.glsp.server.diagram.DiagramModuleRegistry;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultClientSessionFactory implements ClientSessionFactory {
   @Inject()
   protected Injector serverInjector;

   @Inject()
   protected DiagramModuleRegistry diagramModuleRegistry;

   @Override
   public ClientSession create(final String clientSessionId, final String diagramType) {
      GLSPDiagramModule diagramModule = getOrThrow(diagramModuleRegistry.get(diagramType),
         "No diagram module is registered for diagramType: " + diagramType);
      Injector sessionInjector = serverInjector.createChildInjector(diagramModule);
      return new ClientSession(clientSessionId, diagramType, sessionInjector);
   }

}
