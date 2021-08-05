/********************************************************************************
 * Copyright (c) 2019-2020 EclipseSource and others.
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

import static org.eclipse.glsp.server.utils.ServerMessageUtil.error;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.actions.ActionMessage;
import org.eclipse.glsp.server.actions.InitializeClientSessionAction;
import org.eclipse.glsp.server.di.GLSPInjector;
import org.eclipse.glsp.server.protocol.ClientSessionManager;
import org.eclipse.glsp.server.protocol.GLSPServerException;
import org.eclipse.glsp.server.protocol.InitializeClientSessionParameters;
import org.eclipse.glsp.server.protocol.InitializeParameters;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;

public class DefaultGLSPServerV2<T> implements GLSPJsonrpcServer {
   private static Logger log = Logger.getLogger(DefaultGLSPServerV2.class);

   @Inject
   protected ClientSessionManager sessionManager;

   @Inject
   protected GLSPInjector injectorProvider;

   protected GLSPJsonrpcClient clientProxy;
   protected final Class<T> optionsClazz;
   protected CompletableFuture<Boolean> initialized;

   protected String applicationId;

   private Map<String, ActionDispatcher> actionDispatchers;

   public DefaultGLSPServerV2() {
      this(null);
   }

   public DefaultGLSPServerV2(final Class<T> optionsClazz) {
      this.optionsClazz = optionsClazz;
      this.initialized = new CompletableFuture<>();
      actionDispatchers = new HashMap<>();
   }

   @Override
   @SuppressWarnings("checkstyle:IllegalCatch")
   // TODO: refactor to initializeServer
   public CompletableFuture<Boolean> initialize(final InitializeParameters params) {
      try {
         this.applicationId = params.getApplicationId();
         if (optionsClazz != null && params.getOptions() instanceof JsonElement) {
            T options = new Gson().fromJson((JsonElement) params.getOptions(), optionsClazz);
            initialized = handleOptions(options);
         } else {
            initialized = handleOptions(null);
         }
         return initialized;
      } catch (Throwable ex) {
         log.error("Could not initialize server due to corrupted options: " + params.getOptions(), ex);
         initialized.complete(false);
         return initialized;
      }
   }

   public CompletableFuture<Boolean> initializeClientSession(final InitializeClientSessionParameters params) {
      Preconditions.checkArgument(isInitialized(),
         " Could not execute initializeClientSession(). The server has not been initalized yet");

      if (this.clientProxy == null) {
         String errorMsg = String.format(
            "Could not initialize client session with id '%s'. No GLSPClient is connected to the server!",
            params.getClientSessionId());
         throw new GLSPServerException(errorMsg);
      }

      ActionDispatcher actionDispatcher = injectorProvider.getInstance(ActionDispatcher.class,
         params.getDiagramLanguageId(), params.getClientSessionId());
      actionDispatchers.put(params.getClientSessionId(), actionDispatcher);
      actionDispatcher.dispatch(params.getClientSessionId(),
         new InitializeClientSessionAction(params.getClientSessionId()));

      return CompletableFuture.completedFuture(true);
   }

   protected CompletableFuture<Boolean> handleOptions(final T options) {
      return CompletableFuture.completedFuture(true);
   }

   @Override
   public void connect(final GLSPJsonrpcClient clientProxy) {
      this.clientProxy = clientProxy;
      if (clientProxy != null) {
         this.sessionManager.connectClient(clientProxy);
      }
   }

   @Override
   @SuppressWarnings("checkstyle:IllegalCatch")
   public void process(final ActionMessage message) {
      Preconditions.checkArgument(isInitialized(),
         " Could not execute process(). The server has not been initalized yet");
      log.debug("process " + message);
      String clientId = message.getClientId();

      Function<Throwable, Void> errorHandler = ex -> {
         String errorMsg = "Could not process message:" + message;
         log.error("[ERROR] " + errorMsg, ex);
         getClient().process(new ActionMessage(clientId, error("[GLSP-Server] " + errorMsg, ex)));
         return null;
      };

      try {
         ActionDispatcher actionDispatcher = actionDispatchers.get(message.getClientId());
         if (actionDispatcher == null) {
            throw new GLSPServerException(
               String.format(
                  "Could not process action message '%s'. The session for client `%s has not been initalized yet",
                  message, message.getClientId()));
         }
         actionDispatcher.dispatch(message).exceptionally(errorHandler);
      } catch (

      RuntimeException e) {
         errorHandler.apply(e);
      }
   }

   public boolean isInitialized() { return initialized.getNow(false); }

   @Override
   public CompletableFuture<Boolean> shutdown() {
      Preconditions.checkArgument(isInitialized(),
         " Could not execute shutdown(). The server has not been initalized yet");
      boolean completed = false;
      if (this.clientProxy != null) {
         completed = sessionManager.disconnectClient(this.clientProxy);
         this.clientProxy = null;
      }
      return CompletableFuture.completedFuture(completed);
   }

   public String getApplicationId() { return applicationId; }

   @Override
   public GLSPJsonrpcClient getClient() { return this.clientProxy; }
}
