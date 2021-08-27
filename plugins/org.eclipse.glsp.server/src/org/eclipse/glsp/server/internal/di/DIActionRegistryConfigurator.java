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
package org.eclipse.glsp.server.internal.di;

import static org.eclipse.glsp.server.di.GLSPDiagramModule.CLIENT_ACTIONS;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.ActionRegistry;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.internal.util.ReflectionUtil;
import org.eclipse.glsp.server.operations.Operation;
import org.eclipse.glsp.server.operations.OperationHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DIActionRegistryConfigurator implements ActionRegistryConfigurator {

   @Inject()
   @Named(CLIENT_ACTIONS)
   protected Set<Action> clientActions;

   @Inject()
   protected Set<ActionHandler> actionHandlers;

   @Inject()
   protected Set<OperationHandler> operationHandlers;

   @Inject()
   protected DiagramConfiguration diagramConfiguration;

   @Override
   public void configure(final ActionRegistry registry) {
      List<String> clientActionKinds = clientActions.stream().map(Action::getKind).collect(Collectors.toList());

      List<? extends Action> derivedActions = ReflectionUtil.construct(actionHandlers.stream()
         .flatMap(h -> h.getHandledActionTypes().stream())
         .collect(Collectors.toList()));

      List<? extends Operation> derivedOpertions = ReflectionUtil.construct(operationHandlers.stream()
         .map(OperationHandler::getHandledOperationType)
         .collect(Collectors.toList()));

      derivedActions.stream().forEach(
         action -> registry.register(diagramConfiguration.getDiagramType(), action.getKind(), action.getClass(),
            !clientActionKinds.contains(action.getKind())));
      derivedOpertions.forEach(operation -> registry.register(diagramConfiguration.getDiagramType(),
         operation.getKind(), operation.getClass()));
   }
}
