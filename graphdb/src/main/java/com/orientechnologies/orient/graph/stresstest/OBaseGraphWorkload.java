/*
 *
 *  *  Copyright 2014 Orient Technologies LTD (info(at)orientechnologies.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://www.orientechnologies.com
 *
 */
package com.orientechnologies.orient.graph.stresstest;

import com.orientechnologies.orient.core.command.OCommandOutputListener;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.stresstest.ODatabaseIdentifier;
import com.orientechnologies.orient.stresstest.ODatabaseUtils;
import com.orientechnologies.orient.stresstest.workload.OBaseWorkload;
import com.orientechnologies.orient.stresstest.workload.OCheckWorkload;
import com.tinkerpop.blueprints.impls.orient.*;

/**
 * CRUD implementation of the workload.
 *
 * @author Luca Garulli
 */
public abstract class OBaseGraphWorkload extends OBaseWorkload implements OCheckWorkload {
  protected boolean tx = false;

  protected OBaseGraphWorkload(final boolean tx) {
    this.tx = tx;
  }

  public class OWorkLoadContext extends OBaseWorkload.OBaseWorkLoadContext {
    OrientBaseGraph graph;
    OrientVertex    lastVertexToConnect;
    int             lastVertexEdges;

    @Override
    public void init(ODatabaseIdentifier dbIdentifier) {
      graph = tx ? getGraph(dbIdentifier) : getGraphNoTx(dbIdentifier);
    }

    @Override
    public void close() {
      if (graph != null)
        graph.shutdown();
    }
  }

  @Override
  protected OBaseWorkLoadContext getContext() {
    return new OWorkLoadContext();
  }

  protected OrientGraphNoTx getGraphNoTx(final ODatabaseIdentifier databaseIdentifier) {
    final ODatabase database = ODatabaseUtils.openDatabase(databaseIdentifier);
    if (database == null)
      throw new IllegalArgumentException("Error on opening database " + databaseIdentifier.getName());

    return new OrientGraphNoTx((ODatabaseDocumentTx) database);
  }

  protected OrientGraph getGraph(final ODatabaseIdentifier databaseIdentifier) {
    final ODatabase database = ODatabaseUtils.openDatabase(databaseIdentifier);
    if (database == null)
      throw new IllegalArgumentException("Error on opening database " + databaseIdentifier.getName());

    return new OrientGraph((ODatabaseDocumentTx) database);
  }

  @Override
  public void check(final ODatabaseIdentifier databaseIdentifier) {
    final OGraphRepair repair = new OGraphRepair();
    repair.repair(getGraphNoTx(databaseIdentifier), new OCommandOutputListener() {
      @Override
      public void onMessage(String iText) {
        System.out.print("   - " + iText);
      }
    });
  }
}