package yh.subsys.oa.asset.act;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.HypyUtil;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.data.YHCpCptlRecord;
import yh.subsys.oa.asset.data.YHCpcptl;
import yh.subsys.oa.asset.logic.YHCpCptlRecordLogic;
import yh.subsys.oa.asset.logic.YHProductInfoLogic;
public class YHProductImportAct {

//批量导入产品
  public String importProduct (HttpServletRequest request,HttpServletResponse response) throws Exception{
	    Connection dbConn = null;
	    PreparedStatement stmt = null;
	    InputStream is = null;
	    int numOne = 0;
	    int num=0;
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHFileUploadForm fileForm = new YHFileUploadForm();
	      fileForm.parseUploadRequest(request);
	      is = fileForm.getInputStream();
	      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
	      numOne = drl.size();
	      for (int i = 0; i < drl.size(); i++) {
	        YHDbRecord rd = new YHDbRecord();
	        rd = drl.get(i);
	        String proCode = (String)rd.getValueByName("产品编号");
	        String proName = (String)rd.getValueByName("产品名称");
	        String proType = (String)rd.getValueByName("产品规格");
	        String price = (String)rd.getValueByName("标准价格");
	        String unitName = (String)rd.getValueByName("计量单位");
	        String type = (String)rd.getValueByName("类别");
	        String parentName = (String)rd.getValueByName("上级类别");
	        String style = (String)rd.getValueByName("所属大类");
	        String remark = (String)rd.getValueByName("备注");
	        YHProductInfoLogic ypImport=new YHProductInfoLogic();
	        HypyUtil hu=new HypyUtil();
	        String unitId=ypImport.selectUnit(dbConn, unitName);
	        String styleId=ypImport.selectStyle(dbConn, style);
	        String typeId=ypImport.selectTypeName(dbConn, type);
	        Map<String,Object> typeMap=ypImport.selectType(dbConn, parentName);
	        //判断计量单位是否存在，存在直接读id，不存在则添加此数据--start
	        if("".equals(unitId)){
	        	 unitId=UUID.randomUUID().toString();
	        	String unitSql="insert into erproduct_unit (unit_id,unit_name) value('"+unitId+"','"+unitName+"')";
	        	 stmt = dbConn.prepareStatement(unitSql);
	 	         stmt.executeUpdate();//执行SQL
	        }
	        //判断计量单位是否存在，存在直接读id，不存在则添加此数据--end
	        
	        //判断所属大类是否存在，存在直接读id，不存在则添加此数据--start
	        if("".equals(styleId)){
	        	styleId=UUID.randomUUID().toString();
	        	String styleSql="insert into erp_product_style (id,name) value('"+styleId+"','"+style+"')";
	        	stmt = dbConn.prepareStatement(styleSql);
	        	stmt.executeUpdate();//执行SQL
	         }
	       //判断所属大类是否存在，存在直接读id，不存在则添加此数据--end
	        
	       //判断 类别是否存在，存在直接读id，不存在则添加此数据--start
	        if("".equals(typeId)){
	        	typeId=UUID.randomUUID().toString();
	        	String treeCode="";
	        	String treeName="";
	        	String parentId="";
	        	if(typeMap.get("typeId")==null){
	        		parentId=UUID.randomUUID().toString();
	        		String typeSql="insert into erp_product_type (id,name,tree_code,tree_name,parent_id,parent_name) value('"+parentId+"','"+parentName+"','"+parentId+"','"+parentName+"','-1','"+parentName+"')";
		        	stmt = dbConn.prepareStatement(typeSql);
		        	stmt.executeUpdate();//执行SQL
		        	treeCode=parentId+","+typeId;
		        	treeName=parentName+","+type;
	        	}else{
	        	 treeCode=typeMap.get("treeCode").toString()+","+typeId;
	        	 treeName=typeMap.get("treeName").toString()+","+type;
	        	 parentId=typeMap.get("typeId").toString();
	        	}
	        	
	        	String typeSql="insert into erp_product_type (id,name,tree_code,tree_name,parent_id,parent_name) value('"+typeId+"','"+type+"','"+treeCode+"','"+treeName+"','"+parentId+"','"+parentName+"')";
	        	stmt = dbConn.prepareStatement(typeSql);
	        	stmt.executeUpdate();//执行SQL
	        }
	        //判断类别是否存在，存在直接读id，不存在则添加此数据--end
	        String param="'"+proCode+"','"+proName+"','"+proType+"',"+price+",'"+unitId+"','"+typeId+"','"+styleId+"','"+remark+"'";
	        String shortName=hu.cn2py(proName);
	        String proId=UUID.randomUUID().toString();
	        String sql="insert into  erp_product(id,pro_code,pro_name,pro_type,pro_price,unit_id,pt_id,ps_id,remark,shortName) values('"+proId+"',"+param+",'"+shortName+"')";
	        stmt = dbConn.prepareStatement(sql);
	        stmt.executeUpdate();//执行SQL
	        num++;
	      }
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "导入数据失败");
	      throw e;
	    }
	    return "/subsys/oa/asset/manage/mgs.jsp?num=" + num + "&numOne=" + numOne;
	  }
  /**
   * 下载CSV模板
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String downCSVTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String fileName = URLEncoder.encode("产品信息模板.csv", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      YHDbRecord record = new YHDbRecord();
      record.addField("产品编号", "");
      record.addField("产品名称", "");
      record.addField("产品规格", "");
      record.addField("标准价格", "");
      record.addField("计量单位", "");
      record.addField("类别", "");
      record.addField("上级类别", "");
      record.addField("所属大类", "");
      record.addField("备注", "");
      dbL.add(record);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }


}

