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

package com.orientechnologies.orient.core.sql.functions.stat;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.functions.misc.OSQLFunctionUUID;
import com.orientechnologies.orient.core.sql.query.OResultSet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test
public class OSQLFunctionUUIDTest {

  private OSQLFunctionUUID uuid;

  @BeforeMethod
  public void setup() {
    uuid = new OSQLFunctionUUID();
  }

  @Test
  public void testEmpty() {
    Object result = uuid.getResult();
    assertNull(result);
  }

  @Test
  public void testResult() {
    String result = (String) uuid.execute(null, null, null, null, null);
    assertNotNull(result);
  }

  @Test
  public void testQuery(){
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("memory:OSQLFunctionUUIDTest").create();
    try {
      final OResultSet<ODocument> result = db.command(new OCommandSQL("select uuid()")).execute();
      assertNotNull(result);
      assertEquals(result.size(), 1 );
      assertNotNull(result.get(0).field("uuid"));

    } finally {
      db.drop();
    }
  }

}
