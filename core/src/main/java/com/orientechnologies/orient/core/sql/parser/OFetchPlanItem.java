/* Generated By:JJTree: Do not edit this line. OFetchPlanItem.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OFetchPlanItem extends SimpleNode {

  protected Boolean      star;

  protected OInteger     leftDepth;

  protected OInteger     rightDepth;

  protected List<String> fieldChain = new ArrayList<String>();

  public OFetchPlanItem(int id) {
    super(id);
  }

  public OFetchPlanItem(OrientSql p, int id) {
    super(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(OrientSqlVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    if (Boolean.TRUE.equals(star)) {
      builder.append("*");
    } else {
      if (leftDepth != null) {
        builder.append("[");
        leftDepth.toString(params, builder);
        builder.append("]");
      }

      boolean first = true;
      for (String s : fieldChain) {
        if (!first) {
          builder.append(".");
        }
        builder.append(s);
        first = false;
      }

    }
    builder.append(":");
    rightDepth.toString(params, builder);
  }
}
/* JavaCC - OriginalChecksum=b7f4c9a97a8f2ca3d85020e054a9ad16 (do not edit this line) */