package yh.subsys.oa.asset.act;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.HypyUtil;
import oa.spring.util.StaticData;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.asset.logic.YHDbInfoLogic;
import yh.subsys.oa.asset.logic.YHProductInfoLogic;
public class YHDbImportAct {

	 public String importDb (HttpServletRequest request,HttpServletResponse response) throws Exception{
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
		        String whName = (String)rd.getValueByName("仓库名称");
		        String whStyle = (String)rd.getValueByName("仓库类型");
		        String proCode = (String)rd.getValueByName("产品编号");
		        String proName = (String)rd.getValueByName("产品名称");
		        String proType = (String)rd.getValueByName("产品规格");
		        String unitName = (String)rd.getValueByName("计量单位");
		        String type = (String)rd.getValueByName("类别");
		        String parentName = (String)rd.getValueByName("上级类别");
		        String style = (String)rd.getValueByName("所属大类");
		        String price = (String)rd.getValueByName("入库单价");
		        Double number = Double.parseDouble(rd.getValueByName("数量").toString());
		        String batch = (String)rd.getValueByName("批次编号");
		        String invalid_time = (String)rd.getValueByName("失效时间");
				
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");//获取当前时间格式化
		        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");//获取当前时间格式化
				YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
			    
				YHDbInfoLogic yhdb=new YHDbInfoLogic();
		        //根据仓库名称查询仓库是否存在
		        String whId= yhdb.selectWhName(dbConn, whName);
		        if("".equals(whId)){
		        	//根据仓库类型查询是否存在此类型
		        	String whStyleId=yhdb.selectStyle(dbConn,whStyle);
		        	if("".equals(whStyleId)){
		        		whStyleId=UUID.randomUUID().toString();
			        	String styleSql="insert into erp_product_style (id,name) value('"+whStyleId+"','"+whStyle+"')";
			        	stmt = dbConn.prepareStatement(styleSql);
			        	stmt.executeUpdate();//执行SQL
		        	}
		        	whId=UUID.randomUUID().toString();
		        	String styleSql="insert into erp_warehouse (id,name,type) value('"+whId+"','"+whName+"','"+whStyleId+"')";
		        	stmt = dbConn.prepareStatement(styleSql);
		        	stmt.executeUpdate();//执行SQL
		        }
		        //根据产品名称查询产品是否存在
		        String proId=yhdb.selectProduct(dbConn, proName);
		        if("".equals(proId)){
		        	YHProductInfoLogic ypImport=new YHProductInfoLogic();
			        HypyUtil hu=new HypyUtil();
			        String unitId=ypImport.selectUnit(dbConn, unitName);
			        String styleId=ypImport.selectStyle(dbConn, style);
			        String typeId=ypImport.selectTypeName(dbConn, type);
			        Map<String,Object> typeMap=ypImport.selectType(dbConn, parentName);
			        //判断计量单位是否存在，存在直接读id，不存在则添加此数据--start
			        if("".equals(unitId)){
			        	 unitId=UUID.randomUUID().toString();
			        	String unitSql="insert into erp_product_unit (unit_id,unit_name) value('"+unitId+"','"+unitName+"')";
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
			        String param="'"+proCode+"','"+proName+"','"+proType+"',"+price+",'"+unitId+"','"+typeId+"','"+styleId+"'";
			        String shortName=hu.cn2py(proName);
			        proId=UUID.randomUUID().toString();
			        String sql="insert into  erp_product(id,pro_code,pro_name,pro_type,pro_price,unit_id,pt_id,ps_id,shortName) values('"+proId+"',"+param+",'"+shortName+"')";
			        stmt = dbConn.prepareStatement(sql);
			        stmt.executeUpdate();//执行SQL
		        }
		        String dbId=UUID.randomUUID().toString();
		        String remark="--操作员："+user.getUserName()+"--时间："+sdf.format(new Date());
		        String param="'"+dbId+"','"+whId+"','"+proId+"','"+price+"','"+number+"','"+batch+"','"+invalid_time+"','"+remark+"'";
		        String sql="INSERT INTO erp_db (id,wh_id,pro_id,price,num,batch,invalid_time,remark)VALUE ("+param+")";
		        stmt = dbConn.prepareStatement(sql);
		        stmt.executeUpdate();//执行SQL
		        //库存添加成功之后插入库存日志信息
		        String dbLogId=UUID.randomUUID().toString();
		        Double total=Double.parseDouble(price)*number;
           		        String dbLogParam="'"+dbLogId+"','"+batch+"','"+whId+"','"+proId+"','"+price+"','"+number+"','"+total+"','"+StaticData.DB+"','"+StaticData.OVER+"','"+sdf1.format(new Date())+"','"+remark+"'";
		        String dbLogSql="INSERT INTO  erp_db_log (id,batch,wh_id,pro_id,price,number,total,flag,STATUS,TIME,remark) VALUE("+dbLogParam+")";
		        stmt = dbConn.prepareStatement(dbLogSql);
		        stmt.executeUpdate();//执行dbLogSql
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

      String fileName = URLEncoder.encode("库存信息模板.csv", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      YHDbRecord record = new YHDbRecord();
      record.addField("仓库名称", "");
      record.addField("仓库类型", "");
      record.addField("产品编号", "");
      record.addField("产品名称", "");
      record.addField("产品规格", "");
      record.addField("计量单位", "");
      record.addField("类别", "");
      record.addField("上级类别", "");
      record.addField("所属大类", "");
      record.addField("入库单价", "");
      record.addField("数量", "");
      record.addField("批次编号", "");
      record.addField("失效时间", "");
      dbL.add(record);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }


}

