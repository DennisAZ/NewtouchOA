package oa.spring.control.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.ProduceService;
import oa.spring.util.DoubleCaulUtil;
import oa.spring.util.StaticData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/produce")
public class ProduceController {
	@Autowired
	private ProduceService produceService;
	
	private static final Logger logger = Logger.getLogger(ProduceController.class);
	//查询出生产工艺列表
	@RequestMapping(value = "getCrafts/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getCrafts(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT crafts.id, crafts.pro_id,pro.pro_code,pro.pro_name,crafts.crafts_version,is_using,crafts.remark FROM erp_produce_crafts crafts LEFT JOIN erp_product pro ON pro.id = crafts.pro_id ";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getWareHouse", method = RequestMethod.POST)
	public @ResponseBody String getWareHouse(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT w.id AS wh_id,w.name AS wh_name,ps.name AS type_name,w.address FROM erp_warehouse w LEFT JOIN erp_product_style ps ON w.type=ps.id AND ps.is_del ='0'";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	@RequestMapping(value = "getBOMList/{timestamp}", method = RequestMethod.POST)
    public @ResponseBody String getBOMList(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="SELECT b.id,b.code,b.bom_type,p.code as plan_code,b.pp_id,b.status,b.create_time,b.remark FROM erp_produce_bom b LEFT JOIN erp_produce_plan p ON b.pp_id=p.id";
        	logger.debug(sql);
        	str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
    }
	@RequestMapping(value = "getExamList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getExamList(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT b.id,b.code,p.code AS plan_code,b.pp_id,b.status,b.create_time,b.remark FROM erp_produce_exam b LEFT JOIN erp_produce_plan p ON b.pp_id = p.id";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getProCrafts", method = RequestMethod.POST)
	public @ResponseBody String getProCrafts(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String pro_id = request.getParameter("pro_id");
			String sql="SELECT crafts.id, crafts.pro_id,pro.pro_code,pro.pro_name,crafts.crafts_version,is_using,crafts.remark FROM erp_produce_crafts crafts LEFT JOIN erp_product pro ON pro.id = crafts.pro_id where pro.id='"+pro_id+"'";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getNotify/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getNotify(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT id,CODE,TYPE,type_id,produce_time,person_id,person,create_time,STATUS,remark FROM erp_produce_notify ";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getType/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getType(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="select id,code,machine_name,remark from erp_produce_machine_type";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getPlanList/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getPlanList(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="SELECT pp.id,pp.code ,pp.pn_id,pn.code as pnCode,pp.person_id,pp.person,pp.create_time,pp.status,pp.remark FROM erp_produce_plan pp LEFT JOIN erp_produce_notify pn ON pn.id=pp.pn_id ";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	@RequestMapping(value = "getPlanCodeAndInsertBom", method = RequestMethod.POST)
	public @ResponseBody List getPlanCodeAndInsertBom(HttpServletRequest request,HttpServletResponse response) {
		String planId=request.getParameter("planId");
		String type=request.getParameter("type");
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		List<Map<String,Object>> list = produceService.getPlanCodeAndInsertBom(planId,type,String.valueOf(user.getSeqId()),user.getUserName());
		return list;
	}
	@RequestMapping(value = "getWHExamDetial", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getWHExamDetial(HttpServletRequest request,HttpServletResponse response) {
		String planId=request.getParameter("planId");
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		List<Map<String,Object>> list = produceService.getWHExamDetial(planId,String.valueOf(user.getSeqId()),user.getUserName());
		return list;
	}
	@RequestMapping(value = "getBOMDetial", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getBOMDetial(HttpServletRequest request,HttpServletResponse response) {
		String bom_id=request.getParameter("bom_id");
		List<Map<String,Object>> list = produceService.getBOMDetial(bom_id);
		return list;
	}
	@RequestMapping(value = "getBOMDetial1", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getBOMDetial1(HttpServletRequest request,HttpServletResponse response) {
		String bom_id=request.getParameter("bom_id");
		List<Map<String,Object>> list = produceService.getBOMDetial1(bom_id);
		return list;
	}
	@RequestMapping(value = "getExamProDetial", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getExamProDetial(HttpServletRequest request,HttpServletResponse response) {
		String exam_id=request.getParameter("exam_id");
		List<Map<String,Object>> list = produceService.getExamProDetial(exam_id);
		return list;
	}
	//根据id查询出生产工艺列表
	@RequestMapping(value = "getCraftsById", method = RequestMethod.POST)
	public @ResponseBody Map getCraftsById(HttpServletRequest request,HttpServletResponse response) {
		String craftsId=request.getParameter("craftsId");
		Map map =produceService.getCrafts(craftsId);
		return map;
	}
	@RequestMapping(value = "getTypeById", method = RequestMethod.POST)
	public @ResponseBody Map getTypeById(HttpServletRequest request,HttpServletResponse response) {
		String typeId=request.getParameter("typeId");
		Map map =produceService.getType(typeId);
		return map;
	}
	@RequestMapping(value = "getMachineType/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody List  getMachineType(HttpServletRequest request,HttpServletResponse response) {
		List list=produceService.getMachineType();
		return list;
	}
	@RequestMapping(value = "getProcessById", method = RequestMethod.POST)
	public @ResponseBody List  getProcessById(HttpServletRequest request,HttpServletResponse response) {
		String craftsId=request.getParameter("craftsId");
		List list=produceService.getProcessById(craftsId);
		return list;
	}
	@RequestMapping(value = "getNotifyProById", method = RequestMethod.POST)
	public @ResponseBody List  getNotifyProById(HttpServletRequest request,HttpServletResponse response) {
		String notifyId=request.getParameter("notifyId");
		List list=produceService.getNotifyProById(notifyId);
		return list;
	}
	@RequestMapping(value = "getPlanPro", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>>  getPlanPro(HttpServletRequest request,HttpServletResponse response) {
		String planId=request.getParameter("planId");
		List<Map<String,Object>> list=produceService.getPlanPro(planId);
		return list;
	}
	@RequestMapping(value = "getNotifyById", method = RequestMethod.POST)
	public @ResponseBody Map  getNotifyById(HttpServletRequest request,HttpServletResponse response) {
		String notifyId=request.getParameter("notifyId");
		Map map=produceService.getNotifyById(notifyId);
		return map;
}
	@RequestMapping(value = "getPlan", method = RequestMethod.POST)
	public @ResponseBody Map  getPlan(HttpServletRequest request,HttpServletResponse response) {
		String planId=request.getParameter("planId");
		Map map=produceService.getPlan(planId);
		return map;
	}
	@RequestMapping(value = "getProcessByIds", method = RequestMethod.POST)
	public @ResponseBody Map  getProcessByIds(HttpServletRequest request,HttpServletResponse response) {
		String id=request.getParameter("id");
		Map map=produceService.getProcessByIds(id);
		return map;
	}
	@RequestMapping(value = "getProcessIndex", method = RequestMethod.POST)
	public @ResponseBody Map  getProcessIndex(HttpServletRequest request,HttpServletResponse response) {
		String craftsId=request.getParameter("craftsId");
		String processIndex=request.getParameter("processIndex");
		Map map1=new HashMap();
		map1.put("craftsId", craftsId);
		map1.put("processIndex", processIndex);
		Map map=produceService.getProcessIndex(map1);
		return map;
	}
@RequestMapping(value = "getProcessPro", method = RequestMethod.POST)
public @ResponseBody List  getProcessPro(HttpServletRequest request,HttpServletResponse response) {
	String process_id=request.getParameter("processId");
	List list=produceService.getProcessPro(process_id);
	return list;
}
	//根据id查询出生产工艺列表
	@RequestMapping(value = "deleteProduce", method = RequestMethod.POST)
	public @ResponseBody String deleteProduce(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String craftsId=request.getParameter("craftsId");
		try{
		produceService.deleteCrafts(craftsId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	//根据id查询出生产工艺列表
	@RequestMapping(value = "deleteProcess", method = RequestMethod.POST)
	public @ResponseBody String deleteProcess(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String processId=request.getParameter("processId");
		try{
			produceService.deleteProcess(processId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	//新建生产工艺
	@RequestMapping(value = "addCrafts", method = RequestMethod.POST)
	public @ResponseBody
	String addCrafts(HttpServletRequest request) {
		String rtStr = "0";
		String proId = request.getParameter("proId");
		String crafts_version = request.getParameter("crafts_version");
		String is_using = request.getParameter("is_using");
		String remark = request.getParameter("remark");
		String craftsId=UUID.randomUUID().toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("proId", proId);
		map.put("craftsId", craftsId);
		map.put("crafts_version", crafts_version);
		map.put("is_using", is_using);
		map.put("remark", remark);
		try {
				produceService.addCrafts(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//编辑生产工艺
	@RequestMapping(value = "updateCrafts", method = RequestMethod.POST)
	public @ResponseBody
	String updateCrafts(HttpServletRequest request) {
		String rtStr = "0";
		String proId = request.getParameter("proId");
		String craftsId = request.getParameter("craftsId");
		String crafts_version = request.getParameter("crafts_version");
		String is_using = request.getParameter("is_using");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("proId", proId);
		map.put("craftsId", craftsId);
		map.put("crafts_version", crafts_version);
		map.put("is_using", is_using);
		map.put("remark", remark);
		try {
			produceService.updateCrafts(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	//新建生产加工类型
	@RequestMapping(value = "addType", method = RequestMethod.POST)
	public @ResponseBody
	String addType(HttpServletRequest request) {
		String rtStr = "0";
		String code = request.getParameter("code");
		String machine_name = request.getParameter("machine_name");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		String typeId=UUID.randomUUID().toString();
		map.put("typeId", typeId);
		map.put("code", code);
		map.put("machine_name", machine_name);
		map.put("remark", remark);
		try {
			produceService.addType(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//新建生产加工类型
	@RequestMapping(value = "updateType", method = RequestMethod.POST)
	public @ResponseBody
	String updateType(HttpServletRequest request) {
		String rtStr = "0";
		String code = request.getParameter("code");
		String typeId = request.getParameter("typeId");
		String machine_name = request.getParameter("machine_name");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeId", typeId);
		map.put("code", code);
		map.put("machine_name", machine_name);
		map.put("remark", remark);
		try {
			produceService.updateType(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//根据id查询删除加工类型
	@RequestMapping(value = "deleteType", method = RequestMethod.POST)
	public @ResponseBody String deleteType(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String typeId=request.getParameter("typeId");
		try{
		produceService.deleteType(typeId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 根据图纸类别Id标识获取图纸类别对象
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getDrawingTypeById", method = RequestMethod.POST)
	public @ResponseBody Map getDrawingTypeById(HttpServletRequest request,HttpServletResponse response) {
		String drawingTypeId=request.getParameter("drawingTypeId");
		Map map =produceService.getDrawingType(drawingTypeId);
		return map;
	}
	
	/**
	 * 根据图纸类别Id标识获取图纸类别对象
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "selectDrawingTypeByDtId", method = RequestMethod.POST)
	public @ResponseBody Map selectDrawingTypeByDtId(HttpServletRequest request,HttpServletResponse response) {
		String dt_id=request.getParameter("dt_id");
		Map map =produceService.getDrawingTypeByDtId(dt_id);
		return map;
	}
	
	
	@RequestMapping(value = "deleteDrawing", method = RequestMethod.POST)
	public @ResponseBody
	String deleteContract(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "0";
		String drawingId = request.getParameter("drawingId");
		try {
			produceService.deleteDrawing(drawingId);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateDrawing", method = RequestMethod.POST)
	public @ResponseBody String updateDrawing(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "0";
		String code = request.getParameter("code");
		String drawingType = request.getParameter("drawingType");
		String pro_id = request.getParameter("proId");
		String drawing_version = request.getParameter("drawing_version");
		String is_using = request.getParameter("is_using");
		String remark = request.getParameter("remark");
		String attachmentId = request.getParameter("attachmentId");
		String attachmentName = request.getParameter("attachmentName");
		String drawingId=request.getParameter("drawingId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("drawingId", drawingId);
		map.put("dt_id", drawingType);
		map.put("code", code);
		map.put("pro_id", pro_id);
		map.put("drawing_version", drawing_version);
		map.put("is_using", is_using);
		map.put("remark", remark);
		map.put("attachment_id", attachmentId);
		map.put("attachment_name", attachmentName);
		try {
			produceService.updateDrawing(map);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 根据图纸类别Id标识获取图纸类别对象
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "selectProductByProId", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> selectProductByProId(HttpServletRequest request,HttpServletResponse response) {
		String proId=request.getParameter("proId");
		Map<String,Object> map =produceService.selectProductByProId(proId);
		return map;
	}
	
	/**
	 * 新增图纸类别 srm 
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "addDrawingType", method = RequestMethod.POST)
	public @ResponseBody String addDrawingType(HttpServletRequest request) {
		String rtStr = "0";
		String drawing_type_name = request.getParameter("drawing_type_name");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		String drawingTypeId=UUID.randomUUID().toString(); //获取随机的字符串，唯一标识
		map.put("drawingTypeId", drawingTypeId);
		map.put("drawing_type_name", drawing_type_name);
		map.put("remark", remark);
		try {
			produceService.addDrawingType(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	/**
	 * 查询生产图纸类别列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getDrawingType/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getDrawingType(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		StringBuffer sb = new StringBuffer("[");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="select id,name,remark from erp_produce_drawing_type";
			logger.debug(sql);
			PreparedStatement st = null;
			ResultSet rs = null;
			st=dbConn.prepareStatement(sql);
			rs=st.executeQuery(sql);
			while (rs.next()) {
					  sb.append("{");
					  sb.append("id:\"" + rs.getString("id") + "\"");
					  sb.append(",name:\"" + rs.getString("name") + "\"");
					  sb.append("},");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length() - 1);       
			}
			  sb.append("]");
			  PrintWriter out = null;
				try {
					out = response.getWriter();
					out.print(sb);
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					out.close();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 查询生产图纸对象
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getDrawing", method = RequestMethod.POST)
	public @ResponseBody Map getDrawing(HttpServletRequest request,HttpServletResponse response) {
		String drawingId=request.getParameter("drawingId");
		Map map =produceService.getDrawing(drawingId);
		return map;
	}
	
	/**
	 * 查询生产图纸类别列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getAllDrawingType/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getAllDrawingType(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql="select id,name,remark from erp_produce_drawing_type";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 修改图纸类别 srm 
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateDrawingType", method = RequestMethod.POST)
	public @ResponseBody
	String updateDrawingType(HttpServletRequest request) {
		String rtStr = "0";
		String drawingTypeId = request.getParameter("drawingTypeId");
		String drawing_type_name = request.getParameter("drawing_type_name");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("drawingTypeId", drawingTypeId);
		map.put("drawing_type_name", drawing_type_name);
		map.put("remark", remark);
		try {
			produceService.updateDrawingType(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	/**
	 * 删除图纸类别 srm 
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "deleteDrawingType", method = RequestMethod.POST)
	public @ResponseBody String deleteDrawingType(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String drawingTypeId=request.getParameter("drawingTypeId");
		try{
		produceService.deleteDrawingType(drawingTypeId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 获取图纸列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "drawingManage/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String contractManage(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "select id,code,pro_id,dt_id,drawing_version,is_using,attachment_id,attachment_name,remark from erp_produce_drawing";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql).getBytes("UTF-8"),
					"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "drawingProManage", method = RequestMethod.POST)
	public @ResponseBody
	String drawingProManage(HttpServletRequest request,HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String pro_id = request.getParameter("pro_id");
			String sql = "select id,code,pro_id,dt_id,drawing_version,is_using from erp_produce_drawing where pro_id='"+pro_id+"'";
			logger.debug(sql);
			str = new String(produceService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql).getBytes("UTF-8"),
			"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	@RequestMapping(value = "addDrawing", method = RequestMethod.POST)
	public @ResponseBody String addDrawing(HttpServletRequest request, HttpServletResponse response) {
		String str = "0";
		String code = request.getParameter("code");
		String drawingType = request.getParameter("drawingType");
		String pro_id = request.getParameter("proId");
		String drawing_version = request.getParameter("drawing_version");
		String is_using = request.getParameter("is_using");
		String remark = request.getParameter("remark");
		String attachmentId = request.getParameter("attachmentId");
		String attachmentName = request.getParameter("attachmentName");
		String drawingId=UUID.randomUUID().toString(); //获取随机的字符串，唯一标识
		Map<String, String> map = new HashMap<String, String>();
		map.put("drawingId", drawingId);
		map.put("dt_id", drawingType);
		map.put("code", code);
		map.put("pro_id", pro_id);
		map.put("drawing_version", drawing_version);
		map.put("is_using", is_using);
		map.put("remark", remark);
		map.put("attachment_id", attachmentId);
		map.put("attachment_name", attachmentName);
		try {
			produceService.addDrawing(map);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	
	//新建生产工序
	@RequestMapping(value = "addProcess", method = RequestMethod.POST)
	public @ResponseBody
	String addProcess(HttpServletRequest request) {
		String rtStr = "0";
		String proId = request.getParameter("cpId");
		String crafts_id = request.getParameter("craftsId");
		String name = request.getParameter("name");
		String machine_type = request.getParameter("machine_type");
		String process_index = request.getParameter("process_index");
		String bad_rate = request.getParameter("bad_rate");
		String process_time = request.getParameter("process_time");
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String remark = request.getParameter("remark");
		String processId=UUID.randomUUID().toString();
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		List list=new ArrayList();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String consumeId=UUID.randomUUID().toString();
			map1.put("consumeId", consumeId);
			map1.put("processId", processId);
			map1.put("proId", arrPro[i]);
			map1.put("num", arrNumbers[i]);
			
			list.add(map1);
			
			
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processId", processId);
		map.put("proId", proId);
		map.put("craftsId", crafts_id);
		map.put("name", name);
		map.put("machine_type", machine_type);
		map.put("process_index", process_index);
		map.put("process_time", process_time);
		map.put("bad_rate", bad_rate);
		map.put("remark", remark);
		try {
				produceService.addProcess(map,list);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//编辑生产工序
	@RequestMapping(value = "updateProcess", method = RequestMethod.POST)
	public @ResponseBody
	String updateProcess(HttpServletRequest request) {
		String rtStr = "0";
		String proId = request.getParameter("cpId");
		String craftsId = request.getParameter("craftsId");
		String processId = request.getParameter("processId");
		String name = request.getParameter("name");
		String machine_type = request.getParameter("machine_type");
		String process_index = request.getParameter("process_index");
		String bad_rate = request.getParameter("bad_rate");
		String process_time = request.getParameter("process_time");
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String remark = request.getParameter("remark");
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		List list=new ArrayList();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String consumeId=UUID.randomUUID().toString();
			map1.put("consumeId", consumeId);
			map1.put("processId", processId);
			map1.put("proId", arrPro[i]);
			map1.put("num", arrNumbers[i]);
			
			list.add(map1);
			
			
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processId", processId);
		map.put("proId", proId);
		map.put("craftsId", craftsId);
		map.put("name", name);
		map.put("machine_type", machine_type);
		map.put("process_index", process_index);
		map.put("process_time", process_time);
		map.put("bad_rate", bad_rate);
		map.put("remark", remark);
		try {
			produceService.updateProcess(map,list,processId);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "addNotify", method = RequestMethod.POST)
	public @ResponseBody
	String addNotify(HttpServletRequest request) {
		String rtStr = "0";
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		String type_id = request.getParameter("type_id")==null?"":request.getParameter("type_id");
		String beginTime = request.getParameter("beginTime");
		String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String remark = request.getParameter("remark");
		
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String trueDate = request.getParameter("trueDate");
		
		String notifyId=UUID.randomUUID().toString();
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		String[] arrTrueDate=trueDate.split(",");
		
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String noProId=UUID.randomUUID().toString();
			map1.put("noProId", noProId);
			map1.put("notifyId", notifyId);
			map1.put("proId", arrPro[i]);
			map1.put("number", arrNumbers[i]);
			map1.put("status", StaticData.NEW_CREATE);
			map1.put("end_time", arrTrueDate[i]);
			map1.put("remark", remark);
			list.add(map1);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notifyId", notifyId);
		map.put("produce_time", beginTime);
		map.put("create_time", createTime);
		map.put("code", code);
		map.put("status", StaticData.NEW_CREATE);
		map.put("person_id",user.getSeqId());
		map.put("person",user.getUserName());
		map.put("type_id",type_id);
		map.put("type",type);
		map.put("remark", remark);
		try {
				produceService.addNotify(map,list);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updateNotify", method = RequestMethod.POST)
	public @ResponseBody
	String updateNotify(HttpServletRequest request) {
		String rtStr = "0";
		String notifyId=request.getParameter("notifyId");
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		String beginTime = request.getParameter("beginTime");
//		String createTime = request.getParameter("createTime");
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String trueDate = request.getParameter("trueDate");
		String remark = request.getParameter("remark");
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		String[] arrTrueDate=trueDate.split(",");
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String noProId=UUID.randomUUID().toString();
			map1.put("noProId", noProId);
			map1.put("notifyId", notifyId);
			map1.put("proId", arrPro[i]);
			map1.put("number", arrNumbers[i]);
			map1.put("status", StaticData.NEW_CREATE);
			map1.put("end_time", arrTrueDate[i]);
			map1.put("remark", remark);
			list.add(map1);
			
			
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notifyId", notifyId);
		map.put("produce_time", beginTime);
//		map.put("create_time", createTime);
		map.put("code", code);
		map.put("status", StaticData.NEW_CREATE);
		map.put("person_id",user.getSeqId());
		map.put("person",user.getUserName());
		map.put("type_id","");
		map.put("type",type);
		map.put("remark", remark);
		try {
			produceService.updateNotify(map,list,notifyId);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//根据id查询删除生产通知单
	@RequestMapping(value = "deleteNotify", method = RequestMethod.POST)
	public @ResponseBody String deleteNotify(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String notifyId=request.getParameter("notifyId");
		try{
		produceService.deleteNotify(notifyId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "addPlan", method = RequestMethod.POST)
	public @ResponseBody
	String addPlan(HttpServletRequest request) {
		String rtStr = "0";
		String palnCode = request.getParameter("planCode");
		String notifyId = request.getParameter("notifyId");
		String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String craftsIds = request.getParameter("craftsIds");
		String drawingIds = request.getParameter("drawingIds");
		String remark = request.getParameter("remark");
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		String[] arrCrafts=craftsIds.split(",");
		String[] arrDrawing=drawingIds.split(",");
		String planId=UUID.randomUUID().toString();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String planProId=UUID.randomUUID().toString();
			map1.put("planProId", planProId);
			map1.put("pp_id", planId);
			map1.put("proId", arrPro[i]);
			map1.put("number", arrNumbers[i]);
			map1.put("status", StaticData.NEW_CREATE);
			map1.put("crafts_id", arrCrafts[i]);
			map1.put("drawing_id", arrDrawing[i]);
			map1.put("remark", remark);
			list.add(map1);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pn_id", notifyId);
		map.put("create_time", createTime);
		map.put("planId", planId);
		map.put("code", palnCode);
		map.put("status", StaticData.NEW_CREATE);
		map.put("person_id",user.getSeqId());
		map.put("person",user.getUserName());
		map.put("remark", remark);
		try {
				produceService.addPlan(map,list);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updatePlan", method = RequestMethod.POST)
	public @ResponseBody
	String updatePlan(HttpServletRequest request) {
		String rtStr = "0";
		String palnCode = request.getParameter("planCode");
		String planId = request.getParameter("planId");
//		String notifyId = request.getParameter("notifyId");
//		String createTime = request.getParameter("createTime");
		String numbers = request.getParameter("numbers");
		String proIds = request.getParameter("proId");
		String craftsIds = request.getParameter("craftsIds");
		String drawingIds = request.getParameter("drawingIds");
		String remark = request.getParameter("remark");
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String[] arrNumbers=numbers.split(",");
		String[] arrPro=proIds.split(",");
		String[] arrCrafts=craftsIds.split(",");
		String[] arrDrawing=drawingIds.split(",");
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for(int i =0;i<arrPro.length;i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			String planProId=UUID.randomUUID().toString();
			map1.put("planProId", planProId);
			map1.put("pp_id", planId);
			map1.put("proId", arrPro[i]);
			map1.put("number", arrNumbers[i]);
			map1.put("status", StaticData.NEW_CREATE);
			map1.put("crafts_id", arrCrafts[i]);
			map1.put("drawing_id", arrDrawing[i]);
			map1.put("remark", remark);
			list.add(map1);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("pn_id", notifyId);
//		map.put("create_time", createTime);
		map.put("planId", planId);
		map.put("code", palnCode);
//		map.put("status", StaticData.NEW_CREATE);
		map.put("person_id",user.getSeqId());
		map.put("person",user.getUserName());
		map.put("remark", remark);
		try {
			produceService.updatePlan(map,list,planId);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "updateBOM", method = RequestMethod.POST)
	public @ResponseBody String updateBOM(HttpServletRequest request) {
		String rtStr = "0";
		String productIds = request.getParameter("productIds");
		String numbers = request.getParameter("numbers");
		String bomCode = request.getParameter("bomCode");
		String remark = request.getParameter("remark");
		String bom_id = request.getParameter("bom_id");
		String planId = request.getParameter("planId");
		String type = request.getParameter("type");
		
		
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", bom_id);
		map.put("code", bomCode);
		map.put("bom_type", type);//领料
		map.put("pp_id", planId);
		map.put("status", StaticData.NEW_CREATE);
		map.put("person_id", String.valueOf(user.getSeqId()));
		map.put("person", user.getUserName());
		map.put("remark", remark);

		String[] arrNumbers=numbers.split(",");
		String[] arrPro=productIds.split(",");
		Map<String, String> newMap = new HashMap<String, String>();
		for(int i =0;i<arrPro.length;i++){
			if(newMap.get(arrPro[i]) == null){
				newMap.put(arrPro[i], arrNumbers[i]);
			}else{
				double number1 = Double.parseDouble(newMap.get(arrPro[i]));
				newMap.put(arrPro[i], String.valueOf(Double.parseDouble(arrNumbers[i])+number1));
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Iterator<Entry<String,String>> it = newMap.entrySet().iterator();
		while(it.hasNext()){
			Map<String,Object> map1 = new HashMap<String,Object>();
			Entry<String,String> e = it.next();
			map1.put("bom_id", bom_id);
			map1.put("pro_id",  e.getKey());
			map1.put("number", e.getValue());
			list.add(map1);
		}
		
		try {
			produceService.updateBOM(map,list,bom_id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
	
		return rtStr;
	}
	//领料
	@RequestMapping(value = "updateBOMDetial", method = RequestMethod.POST)
	public @ResponseBody String updateBOMDetial(HttpServletRequest request) {
		String rtStr = "0";
		String str = request.getParameter("data");
		String bom_id = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(str != null && !"".equals(str)){
			String[] arr1 = str.split(";");
			for(int i=0;i<arr1.length;i++){
				String[] arr2 = arr1[i].split(",");
				Map<String,Object> map = new HashMap<String,Object>();
				//var key = item.db_id+","+item.pro_id+","+item.wh_id+","+item.batch+","+bom_id+numer;
				map.put("batch", arr2[3]);
				map.put("wh_id", arr2[2]);
				map.put("pro_id", arr2[1]);
				bom_id = arr2[4];
				map.put("pod_id", bom_id);
				map.put("number", arr2[5]);
				map.put("flag", StaticData.PRODUCE_PICK);
				map.put("status", StaticData.NEW_CREATE);
				String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				map.put("time", time);
				list.add(map);
			}
		}
		if(list.size() > 0 && bom_id != null){
			produceService.updateBOMDetial(list,bom_id,StaticData.PRODUCE_PICK);
		}
		return rtStr;
	}
	//退料
	@RequestMapping(value = "updateBOMDetial1", method = RequestMethod.POST)
	public @ResponseBody String updateBOMDetial1(HttpServletRequest request) {
		String rtStr = "0";
		String str = request.getParameter("data");
		String bom_id = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(str != null && !"".equals(str)){
			String[] arr1 = str.split(";");
			for(int i=0;i<arr1.length;i++){
				String[] arr2 = arr1[i].split(",");
				Map<String,Object> map = new HashMap<String,Object>();
				//var key = item.db_id+","+item.pro_id+","+item.wh_id+","+item.batch+","+bom_id+numer;
				map.put("batch", arr2[3]);
				map.put("wh_id", arr2[2]);
				map.put("pro_id", arr2[1]);
				bom_id = arr2[4];
				map.put("pod_id", bom_id);
				map.put("number", arr2[5]);
				map.put("flag", StaticData.PRODUCE_RETURN);
				map.put("status", StaticData.NEW_CREATE);
				String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				map.put("time", time);
				list.add(map);
			}
		}
		if(list.size() > 0 && bom_id != null){
			produceService.updateBOMDetial(list,bom_id,StaticData.PRODUCE_RETURN);
		}
		return rtStr;
	}
	@RequestMapping(value = "updateExamDetial", method = RequestMethod.POST)
	public @ResponseBody String updateExamDetial(HttpServletRequest request) {
		String rtStr = "0";
		String str = request.getParameter("data");
		String exam_code = request.getParameter("exam_code");
		String remark = request.getParameter("remark");
		String planId = request.getParameter("planId");
		String exam_id = null;
		List<Map<String,Object>> dbLogList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> examProList = new ArrayList<Map<String,Object>>();
		if(str != null && !"".equals(str)){
			String[] arr1 = str.split(";");
			Map<String,Object> map1 = new HashMap<String,Object>();
			for(int i=0;i<arr1.length;i++){
				String[] arr2 = arr1[i].split(",");
				Map<String,Object> map = new HashMap<String,Object>();
				//item.wh_id+","+item.pro_id+","+exam_id+"","+item.price+","+item.batch+","+item.invalid+number;
				map.put("wh_id", arr2[0]);
				map.put("pro_id", arr2[1]);
				exam_id = arr2[2];
				map.put("pod_id", exam_id);
				map.put("number", arr2[6]);
				map.put("price", arr2[3]);
				map.put("batch", arr2[4]);
				map.put("invalid_time", arr2[5]);
				map.put("flag", StaticData.CHECK_IN);
				map.put("status", StaticData.NEW_CREATE);
				String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				map.put("time", time);
				dbLogList.add(map);
				if(map1.containsKey(arr2[1])){
					double number = DoubleCaulUtil.sum(map1.get(arr2[1]).toString(), arr2[6]);
					map1.put(arr2[1], number);
				}else{
					map1.put(arr2[1], arr2[6]);
				}
				
			}
			
			Iterator<Entry<String,Object>> it = map1.entrySet().iterator();
			while(it.hasNext()){
				Map<String,Object> examProMap = new HashMap<String,Object>();
				Entry<String,Object> e = it.next();
				//erp_produce_exam_product表数据--start
				examProMap.put("exam_id", exam_id);
				examProMap.put("pro_id", e.getKey());
				examProMap.put("qualified_number", e.getValue().toString());
				//erp_produce_exam_product表数据--end
				examProList.add(examProMap);
			}
		}
		//erp_produce_exam表数据--start
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		Map<String, Object> examMap = new HashMap<String, Object>();
		examMap.put("id", exam_id);
		examMap.put("code", exam_code);
		examMap.put("pp_id", planId);
		examMap.put("status", StaticData.NEW_CREATE);
		examMap.put("person_id", String.valueOf(user.getSeqId()));
		examMap.put("person", user.getUserName());
		examMap.put("remark", remark);
		//erp_produce_exam表数据--end

		if(dbLogList.size() > 0 && exam_id != null){
			produceService.updateExamDetial(examProList,dbLogList,exam_id,examMap);
		}
		return rtStr;
	}
	@RequestMapping(value = "changeExamWay", method = RequestMethod.POST)
	public @ResponseBody String changeExamWay(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String exam_id=request.getParameter("exam_id");
		String pro_id=request.getParameter("pro_id");
		String exam_way=request.getParameter("exam_way");
		Map<String,String> map = new HashMap<String,String>();
		map.put("exam_id", exam_id);
		map.put("pro_id", pro_id);
		map.put("exam_way", exam_way);
		try{
			produceService.changeExamWay(map);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "deletePlan", method = RequestMethod.POST)
	public @ResponseBody String deletePlan(HttpServletRequest request,HttpServletResponse response) {
		String str="0";
		String planId=request.getParameter("planId");
		try{
			produceService.deletePlan(planId);
		}catch(Exception e){
			str="1";
			e.printStackTrace();
		}
		return str;
	}
}
