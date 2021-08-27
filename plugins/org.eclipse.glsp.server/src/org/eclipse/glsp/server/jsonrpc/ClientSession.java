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

import com.google.inject.Injector;

public class ClientSession {
   private final String id;
   private final String diagramType;
   private final Injector injector;

   ClientSession(final String id, final String diagramType, final Injector injector) {
      super();
      this.id = id;
      this.diagramType = diagramType;
      this.injector = injector;
   }

   public String getId() { return id; }

   public String getDiagramType() { return diagramType; }

   public Injector getInjector() { return injector; }

}
