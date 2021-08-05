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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.di.scope.GlspSessionScope;
import org.eclipse.glsp.server.protocol.GLSPServer;
import org.eclipse.glsp.server.protocol.GLSPServerException;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;

/**
 * A GLSP injector manages all guice {@link Injector}s that are needed to configure a glsp server instance.
 * (global server injector and diagram language specifc child injectors). It is used to create instances of classes that
 * form the entry point of a dependency injection chain like the {@link GLSPServer} or the {@link ActionDispatcher}.
 *
 * It serves as convenience wrapper that hides away the complexity of configuring the underlying guice {@link Injector}
 * for a
 * specific {@link GlspSessionScope}.
 *
 */
public class GLSPInjector {

   private final Injector parentInjector;
   protected Map<String, Injector> diagramLanguageInjectors;

   @Inject()
   public GLSPInjector(final Injector injector, final Set<GlspLanguageModule> languageModules) {
      this.parentInjector = injector;
      diagramLanguageInjectors = new HashMap<>();
      for (GlspLanguageModule module : languageModules) {
         Injector languageInjector = injector.createChildInjector(module);
         diagramLanguageInjectors.put(module.getLanguageId(), languageInjector);
      }
   }

   /***
    * Returns the appropriate instance for the given injection key from its parent injector.
    * Typically this is the main (server) injector.
    *
    * @param <T> Type of the instance that should be injected
    * @param key The injection key.
    * @return appropriate instance for the given injection key
    *
    * @throws ConfigurationException if this injector cannot find or create the provider.
    * @throws ProvisionException     if there was a runtime failure while providing an instance.
    */
   public <T> T getInstance(final Key<T> key) {
      return parentInjector.getInstance(key);
   }

   /***
    * Returns the appropriate instance for the given injection key from its parent injector.
    * Typically this is the main (server) injector.
    *
    * @param <T> Type of the instance that should be injected
    * @param key The injection key.
    * @return appropriate instance for the given injection key
    *
    * @throws ConfigurationException if this injector cannot find or create the provider.
    * @throws ProvisionException     if there was a runtime failure while providing an instance.
    */
   public <T> T getInstance(final Class<T> key) {
      return parentInjector.getInstance(key);
   }

   /***
    * Returns the appropriate instance for the given injection key from diagram language injector.
    * The injector is derived from the given diagram language key and is configured within a {@link GlspSessionScope}
    * for the given clientSessionId.
    *
    * @param <T>               Type of the instance that should be injected
    * @param key               The injection key.
    * @param diagramLanguageId The diagram language id.
    * @param clientSessionId   The client session id.
    * @return appropriate instance for the given injection key
    *
    * @throws GLSPServerException    if not injector is configured for the given diagram language id
    * @throws ConfigurationException if this injector cannot find or create the provider.
    * @throws ProvisionException     if there was a runtime failure while providing an instance.
    */
   public <T> T getInstance(final Key<T> key, final String diagramLanguageId, final String clientSessionId) {
      Injector languageInjector = diagramLanguageInjectors.get(diagramLanguageId);
      if (languageInjector == null) {
         throw new GLSPServerException("No injector is configured for the diagram language id: " + diagramLanguageId);
      }
      return getInstance(key, languageInjector, clientSessionId);
   }

   /***
    * Returns the appropriate instance for the given injection key from diagram language injector.
    * The injector is derived from the given diagram language key and is configured within a {@link GlspSessionScope}
    * for the given clientSessionId.
    *
    * @param <T>               Type of the instance that should be injected
    * @param key               The injection key.
    * @param diagramLanguageId The diagram language id.
    * @param clientSessionId   The client session id.
    * @return appropriate instance for the given injection key
    *
    * @throws ConfigurationException if this injector cannot find or create the provider.
    * @throws ProvisionException     if no injector is configured for the given diagram language id or
    *                                   if there was a runtime failure while providing an instance.
    */
   public <T> T getInstance(final Class<T> key, final String diagramLanguageId, final String clientSessionId) {
      Injector languageInjector = diagramLanguageInjectors.get(diagramLanguageId);
      if (languageInjector == null) {
         throw new GLSPServerException("No injector is configured for the diagram language id: " + diagramLanguageId);
      }
      return getInstance(key, languageInjector, clientSessionId);
   }

   /**
    * Retrieves the diagram language ids of all diagram language injectors that are currently
    * managed by the glsp injector.
    *
    * @return A set of the configured diagram language ids.
    */
   public Set<String> getDiagramLanguageIds() { return diagramLanguageInjectors.keySet(); }

   protected <T> T getInstance(final Key<T> key, final Injector languageInjector, final String clientSessionId) {
      GlspSessionScope scope = languageInjector.getInstance(GlspSessionScope.class);
      scope.enter(clientSessionId);
      T instance = languageInjector.getInstance(key);
      scope.exit();
      return instance;
   }

   protected <T> T getInstance(final Class<T> key, final Injector languageInjector, final String clientSessionId) {
      GlspSessionScope scope = languageInjector.getInstance(GlspSessionScope.class);
      scope.enter(clientSessionId);
      T instance = languageInjector.getInstance(key);
      scope.exit();
      return instance;
   }

}
