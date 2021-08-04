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

import java.util.Map;

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionRegistry;
import org.eclipse.glsp.server.actions.ActionRegistryConfigurator;
import org.eclipse.glsp.server.di.GLSPInjectorProvider;

import com.google.inject.Inject;

public class DefaultActionRegistry extends MapRegistry<String, Class<? extends Action>> implements ActionRegistry {

   @Inject()
   public void init(final GLSPInjectorProvider injectorProvider) {
      injectorProvider.getLanguageInjectors().forEach(injector -> {
         ActionRegistryConfigurator configurator = injector.getInstance(ActionRegistryConfigurator.class);
         configurator.configure(this);
      });
   }

   @Override
   public Map<String, Class<? extends Action>> toMap() {
      return elements;
   }

}
