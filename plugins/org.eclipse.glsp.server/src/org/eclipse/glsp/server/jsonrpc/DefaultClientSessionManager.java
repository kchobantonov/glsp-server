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
package org.eclipse.glsp.server.jsonrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.glsp.server.protocol.ClientConnectionListener;
import org.eclipse.glsp.server.protocol.ClientSessionListener;
import org.eclipse.glsp.server.protocol.ClientSessionManager;
import org.eclipse.glsp.server.protocol.GLSPClient;

import com.google.inject.Inject;
import com.google.inject.Injector;

public final class DefaultClientSessionManager implements ClientSessionManager {

   @Inject()
   protected Injector serverInjector;

   @Inject()
   protected ClientSessionFactory clientSessionFactory;

   private GLSPClient clientProxy;

   private final Map<String, ClientSession> clientSessions = new HashMap<>();
   private final Set<ClientSessionListener> sessionListeners = new LinkedHashSet<>();
   private final Set<ClientConnectionListener> connectionListeners = new LinkedHashSet<>();

   @Inject()
   public void initialize(final Set<ClientConnectionListener> connectionListeners,
      final Set<ClientSessionListener> sessionListeners) {
      this.connectionListeners.addAll(connectionListeners);
      this.sessionListeners.addAll(sessionListeners);
   }

   @Override
   public boolean connectClient(final GLSPClient clientProxy) {
      if (this.clientProxy != null) {
         return false;
      }

      this.clientProxy = clientProxy;
      new ArrayList<>(connectionListeners).forEach(listener -> listener.clientConnected(clientProxy));
      return true;
   }

   @Override
   public synchronized Optional<ClientSession> initializeClientSession(final String clientSessionId,
      final String diagramType) {
      if (clientSessions.containsKey(clientSessionId)) {
         return Optional.empty();
      }

      ClientSession session = clientSessionFactory.create(clientSessionId, diagramType);
      clientSessions.put(clientSessionId, session);
      new ArrayList<>(sessionListeners).forEach(listener -> listener.sessionInitialized(session));
      return Optional.of(session);
   }

   @Override
   public synchronized boolean disposeClientSession(final String clientSessionId) {
      ClientSession session = clientSessions.remove(clientSessionId);
      if (session == null) {
         return false;
      }

      new ArrayList<>(sessionListeners).forEach(listener -> listener.sessionDisposed(session));
      return true;
   }

   @Override
   public synchronized boolean disconnectClient(final GLSPClient clientProxy) {
      if (this.clientProxy != clientProxy) {
         return false;
      }
      clientSessions.values().forEach(session -> disposeClientSession(session.getId()));
      new ArrayList<>(connectionListeners).forEach(listener -> listener.clientDisconnected(clientProxy));
      connectionListeners.clear();
      this.clientProxy = null;
      return true;
   }

   @Override
   public Optional<ClientSession> getClientSession(final String clientSessionId) {
      return Optional.ofNullable(clientSessions.get(clientSessionId));
   }

   @Override
   public boolean addListener(final ClientSessionListener listener) {
      return sessionListeners.add(listener);
   }

   @Override
   public boolean removeListener(final ClientSessionListener listener) {
      return sessionListeners.remove(listener);
   }

   @Override
   public boolean addListener(final ClientConnectionListener listener) {
      return connectionListeners.add(listener);
   }

   @Override
   public boolean removeListener(final ClientConnectionListener listener) {
      return connectionListeners.remove(listener);
   }

}
