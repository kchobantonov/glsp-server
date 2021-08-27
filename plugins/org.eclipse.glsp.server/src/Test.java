import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.BasicActionHandler;
import org.eclipse.glsp.server.di.GLSPDiagramModule;
import org.eclipse.glsp.server.di.GLSPServerModule;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.diagram.EdgeTypeHint;
import org.eclipse.glsp.server.diagram.ShapeTypeHint;
import org.eclipse.glsp.server.internal.di.DIDiagramConfiguration;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.protocol.GLSPServer;
import org.eclipse.glsp.server.protocol.InitializeClientSessionParameters;
import org.eclipse.glsp.server.protocol.InitializeParameters;
import org.eclipse.glsp.server.types.Severity;
import org.eclipse.glsp.server.utils.LaunchUtil;
import org.eclipse.glsp.server.utils.MultiBinding;

import com.google.inject.Guice;
import com.google.inject.Injector;

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

public class Test {

   public static void main(final String[] args) throws IOException {
      LaunchUtil.configureLogger(true, Level.DEBUG);

      GLSPServerModule serverModule = new GLSPServerModule() {

         @Override
         public void configureDiagramModules(final MultiBinding<GLSPDiagramModule> binding) {
            binding.add(TestDiagramModule.class);
         }
      };

      Injector serverInjector = Guice.createInjector(serverModule);
      GLSPServer glspServer = serverInjector.getInstance(GLSPServer.class);
      InitializeParameters initializeParams = new InitializeParameters();
      initializeParams.setApplicationId("MySexyApplication");
      initializeParams.setProtocolVersion("0.9.0");
      glspServer.initialize(initializeParams);

      InitializeClientSessionParameters sessionParams = new InitializeClientSessionParameters();
      sessionParams.setClientSessionId("MyFancyNewSessioN");
      sessionParams.setDiagramType("workflow-diagram");
      glspServer.initializeClientSession(sessionParams);
      System.out.println();
   }

   static class TestDiagramModule extends GLSPDiagramModule {

      @Override
      public String getDiagramType() { return "workflow-diagram"; }

      @Override
      protected Class<? extends DiagramConfiguration> bindDiagramConfiguration() {
         return TestDiagramConfiguration.class;
      }

      @Override
      public void configureActionHandlers(final MultiBinding<ActionHandler> binding) {
         super.configureActionHandlers(binding);
         binding.add(LogActionHandler.class);
      }

   }

   static class TestDiagramConfiguration extends DIDiagramConfiguration {

      @Override
      public List<ShapeTypeHint> getShapeTypeHints() { return new ArrayList<>(); }

      @Override
      public List<EdgeTypeHint> getEdgeTypeHints() { return new ArrayList<>(); }

   }

   public static class LogActionHandler extends BasicActionHandler<LogAction> {
      private static Logger LOG = Logger.getLogger(LogActionHandler.class);

      @Override
      protected List<Action> executeAction(final LogAction action, final GModelState modelState) {
         LOG.log(toLevel(action.getSeverity()), action.getMessage());
         return Collections.emptyList();
      }

      private static Level toLevel(final Severity severity) {
         return Level.toLevel(severity.toString(), Level.DEBUG);
      }

   }

   public static class LogAction extends Action {
      public static final String KIND = "logAction";

      private Severity severity;
      private String message;

      public LogAction() {
         super(KIND);
      }

      public LogAction(final Severity severity, final String message) {
         this();
         this.severity = severity;
         this.message = message;
      }

      public void setSeverity(final Severity severity) { this.severity = severity; }

      public Severity getSeverity() { return severity; }

      public void setMessage(final String message) { this.message = message; }

      public String getMessage() { return message; }

   }

}
