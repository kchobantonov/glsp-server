/********************************************************************************
 * Copyright (c) 2020-2021 EclipseSource and others.
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
package org.eclipse.glsp.server.protocol;

import java.util.Optional;

import org.eclipse.glsp.server.jsonrpc.ClientSession;

public interface ClientSessionManager {

   boolean connectClient(GLSPClient client);

   Optional<ClientSession> initializeClientSession(String clientSessionId, String diagramType);

   boolean disposeClientSession(String clientSesionId);

   Optional<ClientSession> getClientSession(String clientSessionId);

   boolean disconnectClient(GLSPClient client);

   boolean addListener(ClientSessionListener listener);

   boolean removeListener(ClientSessionListener listener);

   boolean addListener(ClientConnectionListener listener);

   boolean removeListener(ClientConnectionListener listener);

}
