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
package org.eclipse.glsp.server.internal.json;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.glsp.graph.GraphExtension;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.diagram.gson.GGraphGsonConfiguration;

import com.google.inject.Inject;

public class DefaultGGraphGsonConfiguration implements GGraphGsonConfiguration {

   @Inject()
   protected DiagramConfiguration diagramConfiguration;

   @Inject()
   protected Optional<GraphExtension> graphExtension;

   @Override
   public Map<String, EClass> getTypeMappings() { return diagramConfiguration.getTypeMappings(); }

   @Override
   public Set<EPackage> getEPackages() {
      Set<EPackage> ePackages = new HashSet<>();
      graphExtension.ifPresent(extension -> ePackages.add(extension.getEPackage()));
      return ePackages;
   }
}
