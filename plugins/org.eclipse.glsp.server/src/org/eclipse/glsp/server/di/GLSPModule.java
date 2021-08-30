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

import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.AbstractModule;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.OptionalBinder;

public abstract class GLSPModule extends AbstractModule {
   public static final String CLIENT_ACTIONS = "ClientActions";
   public static final String DIAGRAM_TYPE = "DiagramType";
   public static final String CLIENT_ID = "ClientId";

   protected <T> void configure(final MultiBinding<T> binding, final Consumer<MultiBinding<T>> configurator) {
      configurator.accept(binding);
      binding.applyBinding(binder());
   }

   protected <T, S extends T> Optional<ScopedBindingBuilder> bindOptionally(final Class<T> key, final Class<S> to) {
      OptionalBinder.newOptionalBinder(binder(), key);
      return Optional.ofNullable(to).map(toClass -> {
         return bind(key).to(toClass);
      });
   }
}
