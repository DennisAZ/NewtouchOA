package yh.subsys.oa.addworkfee.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.addworkfee.data.YHRoleBaseFee;
import yh.subsys.oa.addworkfee.logic.YHOndutyLogic;

public class YHOndutyAct{

  /**
   * 增加一个加班费基数
   * @param request
   * @param response
   * @return
   * @throws Throwable
   */
   public String addRoleBaseFee(HttpServletRequest request, HttpServletResponse response) throws Throwable{
     try{
       Connection dbConn = null;
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       String roleIds = request.getParameter("roleId");
       String normal = request.getParameter("normaladd");
       String festival = request.getParameter("festivaladd");
       String weekadd = request.getParameter("weekadd");
       String baseadd = request.getParameter("baseAdd");
       YHRoleBaseFee abf = new YHRoleBaseFee();
       abf.setNormalAdd(Double.parseDouble(normal));
       abf.setFestivalAdd(Double.parseDouble(festival));
       abf.setWeekAdd(Double.parseDouble(weekadd));
       abf.setBaseAdd(Double.parseDouble(baseadd));
       abf.setRoleIds(roleIds);
       YHOndutyLogic logic = new YHOndutyLogic();
       logic.addYHRoleBaseFee(dbConn, abf);
       List<YHRoleBaseFee> fees = logic.findYHRoleBaseFeeList(dbConn);
       request.setAttribute("fees", fees);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/onduty.jsp";
   }
   
   /**
    * 查找所有的加班费基数
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String findRoleBaseFee(HttpServletRequest request, HttpServletResponse response) throws Exception{
     Connection dbConn = null;
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     try{
      dbConn = requestDbConn.getSysDbConn();
      YHOndutyLogic logic = new YHOndutyLogic();
      List<YHRoleBaseFee> fees = logic.findYHRoleBaseFeeList(dbConn);
      request.setAttribute("fees", fees); 
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/onduty.jsp";
   }
   
   /**
    * 编辑加班费
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String editRoleBaseFee(HttpServletRequest request, HttpServletResponse response)throws Exception{
     Connection dbConn = null;
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     try{
      dbConn = requestDbConn.getSysDbConn();
      YHOndutyLogic logic = new YHOndutyLogic();
      String seq_id = request.getParameter("seqId");
      int k = 0;
      if(!YHUtility.isNullorEmpty(seq_id)){
        k = Integer.parseInt(seq_id);
      }
      YHRoleBaseFee fee = logic.findYHRoleBaseFee(dbConn, k);
      List<YHRoleBaseFee> fees = logic.findYHRoleBaseFeeList(dbConn);
      request.setAttribute("fees", fees); 
      request.setAttribute("fee", fee); 
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/onduty.jsp";
   }
   
   /**
    * 删除
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String delRoleBaseFee(HttpServletRequest request, HttpServletResponse response)throws Exception{
     try{
       String seq_id = request.getParameter("seqId");
       Connection dbConn = null;
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHOndutyLogic logic = new YHOndutyLogic();
       logic.delYHRoleBaseFee(dbConn, Integer.parseInt(seq_id));
       List<YHRoleBaseFee> fees = logic.findYHRoleBaseFeeList(dbConn);
       request.setAttribute("fees", fees);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/onduty.jsp";
   }
   
   /**
    * 更新
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String updatRoleBaseFee(HttpServletRequest request, HttpServletResponse response)throws Exception{
     try{
       Connection dbConn = null;
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       String roleId = request.getParameter("roleId");
       String normal = request.getParameter("normaladd");
       String festival = request.getParameter("festivaladd");
       String weekadd = request.getParameter("weekadd");
       String baseadd = request.getParameter("baseAdd");
       String seqId = request.getParameter("seqId");
       YHRoleBaseFee abf = new YHRoleBaseFee();
       abf.setNormalAdd(Double.parseDouble(normal));
       abf.setFestivalAdd(Double.parseDouble(festival));
       abf.setWeekAdd(Double.parseDouble(weekadd));
       abf.setBaseAdd(Double.parseDouble(baseadd));
       abf.setRoleId(Integer.parseInt(roleId));
       abf.setSeqId(Integer.parseInt(seqId));
       YHOndutyLogic logic = new YHOndutyLogic();
       logic.updatYHRoleBaseFee(dbConn, abf);
       List<YHRoleBaseFee> fees = logic.findYHRoleBaseFeeList(dbConn);
       request.setAttribute("fees", fees);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/addworkfee/onduty.jsp";
   }
}
