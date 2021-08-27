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
package org.eclipse.glsp.server.internal.di;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionRegistry;

public class DIActionRegistry extends MapRegistry<String, Map<String, Class<? extends Action>>>
   implements ActionRegistry {
   protected Map<String, List<String>> serverHandledActionKinds = new HashMap<>();

   @Override
   public boolean register(final String diagramType, final String actionKind, final Class<? extends Action> action,
      final boolean isServerAction) {
      Map<String, Class<? extends Action>> actionMap = elements.computeIfAbsent(diagramType, k -> new HashMap<>());
      boolean registered = actionMap.putIfAbsent(actionKind, action) == null;
      if (registered && isServerAction) {
         serverHandledActionKinds.computeIfAbsent(diagramType, k -> new ArrayList<>()).add(actionKind);
      }
      return registered;
   }

   @Override
   public boolean register(final String diagramType, final Map<String, Class<? extends Action>> actionMap) {
      return actionMap.entrySet().stream()
         .allMatch(entry -> register(diagramType, entry.getKey(), entry.getValue(), true));
   }

   @Override
   public boolean deregister(final String diagramType) {
      boolean deregistered = super.deregister(diagramType);
      if (deregistered) {
         serverHandledActionKinds.remove(diagramType);
      }
      return deregistered;
   }

   @Override
   public List<String> getServerHandledAction(final String diagramType) {
      return serverHandledActionKinds.computeIfAbsent(diagramType, k -> new ArrayList<>());
   }

   @Override
   public Map<String, List<String>> getServerHandledActions() { return serverHandledActionKinds; }

   @Override
   public Map<String, Class<? extends Action>> getAllAsMap() {
      Map<String, Class<? extends Action>> result = new HashMap<>();
      getAll().forEach(actionMap -> result.putAll(actionMap));
      return result;
   }

}
