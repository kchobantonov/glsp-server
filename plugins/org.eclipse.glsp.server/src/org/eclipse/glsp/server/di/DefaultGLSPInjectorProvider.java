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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultGLSPInjectorProvider implements GLSPInjectorProvider {

   protected final Map<String, Injector> languageInjectors;
   protected final Map<String, Injector> sessionInjectors;

   private final Injector globalInjector;

   @Inject
   public DefaultGLSPInjectorProvider(final Injector globalInjector, final Set<GlspLanguageModule> languageModules) {
      this.globalInjector = globalInjector;
      languageInjectors = new HashMap<>();
      for (GlspLanguageModule module : languageModules) {
         Injector languageInjector = globalInjector.createChildInjector(module);
         languageInjectors.put(module.getLanguageId(), languageInjector);
      }
      sessionInjectors = new HashMap<>();
   }

   @Override
   public Injector getGlobalInjector() { return globalInjector; }

   @Override
   public Optional<Injector> getLanguageInjector(final String languageId) {
      return Optional.ofNullable(languageInjectors.get(languageId));
   }

   @Override
   public List<Injector> getLanguageInjectors() { return new ArrayList<>(languageInjectors.values()); }

}
