
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
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.apache.log4j.Level;
import org.eclipse.glsp.example.workflow.WorkflowGLSPModuleV2;
import org.eclipse.glsp.example.workflow.WorkflowLanguageModule;
import org.eclipse.glsp.server.jsonrpc.DefaultGLSPServerV2;
import org.eclipse.glsp.server.jsonrpc.GLSPJsonrpcClient;
import org.eclipse.glsp.server.protocol.GLSPServer;
import org.eclipse.glsp.server.protocol.InitializeClientSessionParameters;
import org.eclipse.glsp.server.protocol.InitializeParameters;
import org.eclipse.glsp.server.utils.LaunchUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Test {

   private static Injector injector;
   private static final String SESSION_ID_1 = "testSession1";
   private static final String SESSION_ID_2 = "testSession2";

   public static void main(final String[] args) throws IOException {
      LaunchUtil.configureLogger(true, Level.DEBUG);
      injector = Guice.createInjector(new WorkflowGLSPModuleV2());
      DefaultGLSPServerV2 server = (DefaultGLSPServerV2) injector.getInstance(GLSPServer.class);

      GLSPJsonrpcClient client = (message) -> System.out.println("Client is processing action message: " + message);
      server.connect(client);
      String applicationId = "myfancyApplication";

      InitializeParameters initParams = new InitializeParameters();
      initParams.setApplicationId(applicationId);
      server.initialize(initParams)
         .thenApply(success -> initializeClientSession(server))
         .thenApply(success -> initializeClientSession2(server))
         .thenAccept(success -> afterInit(server));

   }

   private static CompletableFuture<Boolean> initializeClientSession(final DefaultGLSPServerV2 server) {
      String languageId = WorkflowLanguageModule.LANGUAGE_ID;

      InitializeClientSessionParameters initParams = new InitializeClientSessionParameters();
      initParams.setClientSessionId(SESSION_ID_1);
      initParams.setDiagramLanguageId(languageId);
      return server.initializeClientSession(initParams);
   }

   private static CompletableFuture<Boolean> initializeClientSession2(final DefaultGLSPServerV2 server) {
      String languageId = WorkflowLanguageModule.LANGUAGE_ID;

      InitializeClientSessionParameters initParams = new InitializeClientSessionParameters();
      initParams.setClientSessionId(SESSION_ID_2);
      initParams.setDiagramLanguageId(languageId);
      return server.initializeClientSession(initParams);
   }

   private static void afterInit(final DefaultGLSPServerV2 server) {
      // server.process(new ActionMessage(SESSION_ID_1, new InitializeClientSessionAction(SESSION_ID_1)));
      System.out.println("Successfull?");
      try {
         Thread.sleep(5000);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}