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
package org.eclipse.glsp.server.di.scope;

import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.glsp.server.utils.disposable.Disposable;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class GlspSessionScope extends Disposable implements Scope {

   private final Map<String, Map<Key<?>, Object>> sessionStores = new ConcurrentHashMap<>();
   private Map<Key<?>, Object> activeSessionStore;

   public void enter(final String sessionId) {
      checkState(activeSessionStore == null, "A scoping block is already in progress");
      activeSessionStore = sessionStores.computeIfAbsent(sessionId, key -> new HashMap<>());
   }

   public void exit() {
      checkState(activeSessionStore != null, "No scoping block in progress");
      activeSessionStore = null;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
      return () -> {
         if (activeSessionStore == null) {
            throw new OutOfScopeException("Cannot access " + key
               + " outside of a scoping block");
         }

         T instance = (T) activeSessionStore.get(key);
         if (instance == null) {
            instance = unscoped.get();
            activeSessionStore.put(key, instance);
         }

         return instance;
      };
   }

   public synchronized boolean dispose(final String sessionId) {
      checkState(activeSessionStore == null || sessionStores.get(sessionId) != activeSessionStore,
         String.format("A scoping block for the session id '%s' is in progress", sessionId));

      return sessionStores.remove(sessionId) != null;
   }
}
