package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHOfficeProductDraw  implements YHIWFHookPlugin{

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
        String PRO_ID =(String)arrayHandler.get("PRO_ID");
        String BORROWER_ID=(String)arrayHandler.get("BORROWER_ID");
        String REMARK=(String)arrayHandler.get("REMARK");
        String FACT_QTY=(String)arrayHandler.get("TRANS_QTY");
        String remove_reason=(String)arrayHandler.get("REASON");
        String TRANS_ID=(String)arrayHandler.get("KEY");
        String query="select a.PRO_UNIT,a.PRO_NAME,a.PRO_STOCK,a.PRO_PRICE,c.PRO_KEEPER from oa_office_goods a left outer join oa_office_kind b on a.OFFICE_PROTYPE=b.SEQ_ID left outer join oa_office_repertory c on b.TYPE_DEPOSITORY=c.SEQ_ID where a.SEQ_ID='"+PRO_ID+"'";
        
        Statement stmt=null;
        ResultSet rs=null;
        try{
        stmt=conn.createStatement();
        rs=stmt.executeQuery(query);
        String PRO_STOCK="";
        String PRO_PRICE="";
        String PRO_KEEPER="";
        String PRO_UNIT ="";   
        String PRO_NAME ="";
        if(rs.next())
        {
            PRO_STOCK=rs.getString("PRO_STOCK");
            PRO_PRICE=rs.getString("PRO_PRICE");
            PRO_KEEPER=rs.getString("PRO_KEEPER");
            PRO_UNIT =rs.getString("PRO_UNIT");   
            PRO_NAME =rs.getString("PRO_NAME");
        }
        if (YHUtility.isNullorEmpty(PRO_PRICE)
            || !YHUtility.isNumber(PRO_PRICE)) {
          PRO_PRICE = "0.0";
        }
        if(agree)
        {   
           if (!YHUtility.isInteger(FACT_QTY)) {
             FACT_QTY = "0";
           }
           
           int FACT_TRANS_QTY=Integer.parseInt(FACT_QTY)*(-1);
           String update="update oa_office_trans_records set FACT_QTY='"+FACT_QTY+"',OPERATOR='"+BORROWER_ID+"',TRANS_STATE='1',TRANS_QTY='"+FACT_TRANS_QTY+"',PRICE='"+PRO_PRICE+"' where SEQ_ID='"+TRANS_ID+"'";
           YHWorkFlowUtility.updateTableBySql(update, conn);

           int NEW_PRO_STOCK=Integer.parseInt(PRO_STOCK)-Integer.parseInt(FACT_QTY);
           
           update="update oa_office_trans_records set PRO_STOCK ='"+NEW_PRO_STOCK+"' where SEQ_ID='"+PRO_ID+"'";
           YHWorkFlowUtility.updateTableBySql(update, conn);
        }
        else
        {
           String update="update oa_office_trans_records set TRANS_STATE='2',REASON='"+remove_reason+"' where SEQ_ID='"+TRANS_ID+"'";
           YHWorkFlowUtility.updateTableBySql(update, conn);
        }
       }catch(Exception ex) {
         throw ex;
       } finally {
         YHDBUtility.close(stmt, rs, null); 
       }

    return null;
  }

}
