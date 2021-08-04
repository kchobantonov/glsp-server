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

import java.util.function.Consumer;

import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

public class AbstractGlspModule extends AbstractModule {

   protected <T> void configure(final MultiBinding<T> binding, final Consumer<MultiBinding<T>> configurator) {
      configure(binding, configurator, true);
   }

   protected <T> void configure(final MultiBinding<T> binding, final Consumer<MultiBinding<T>> configurator,
      final boolean isEmptyAllowed) {
      configurator.accept(binding);
      Preconditions.checkArgument(isEmptyAllowed || !binding.isEmpty(),
         "The MultiBinding oject hast to contain at least one binding");
      binding.applyBinding(binder());
   }
}
