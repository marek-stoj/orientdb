/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.server.distributed.task;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.distributed.ODistributedServerLog;
import com.orientechnologies.orient.server.distributed.ODistributedServerLog.DIRECTION;
import com.orientechnologies.orient.server.distributed.ODistributedServerManager;
import com.orientechnologies.orient.server.distributed.conflict.OReplicationConflictResolver;

/**
 * Distributed task used for synchronization.
 * 
 * @author Luca Garulli (l.garulli--at--orientechnologies.com)
 * 
 */
public class OSQLCommandTask extends OAbstractReplicatedTask {
  private static final long serialVersionUID = 1L;

  protected String          text;

  public OSQLCommandTask() {
  }

  public OSQLCommandTask(final String iCommand) {
    text = iCommand;
  }

  @Override
  public OSQLCommandTask copy() {
    final OSQLCommandTask copy = (OSQLCommandTask) super.copy(new OSQLCommandTask());
    copy.text = text;
    return copy;
  }

  /**
   * Handles conflict between local and remote execution results.
   * 
   * @param localResult
   *          The result on local node
   * @param remoteResult
   *          the result on remote node
   */
  @Override
  public void handleConflict(String iDatabaseName, final String iRemoteNodeId, final Object localResult, final Object remoteResult,
      OReplicationConflictResolver iConfictStrategy) {
    iConfictStrategy.handleCommandConflict(iRemoteNodeId, text, localResult, remoteResult);
  }

  public Object execute(final OServer iServer, ODistributedServerManager iManager, final ODatabaseDocumentTx database)
      throws Exception {
    ODistributedServerLog.debug(this, iManager.getLocalNodeName(), getNodeSource(), DIRECTION.IN, "execute command=%s db=%s",
        text.toString(), database.getName());

    return database.command(new OCommandSQL(text)).execute();
  }

  @Override
  public long getTimeout() {
    return OGlobalConfiguration.DISTRIBUTED_COMMAND_TASK_SYNCH_TIMEOUT.getValueAsLong();
  }

  public OSQLCommandTask copy(final OSQLCommandTask iCopy) {
    super.copy(iCopy);
    iCopy.text = text;
    return iCopy;
  }

  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeUTF(text);
  }

  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    text = in.readUTF();
  }

  @Override
  public String getName() {
    return "command_sql";
  }

  @Override
  public String toString() {
    return super.toString() + "(" + text + ")";
  }

  @Override
  public String getPayload() {
    return text;
  }
}
