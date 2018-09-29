package yh.subsys.inforesouce.act;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.inforesouce.data.TempFile;
import yh.subsys.inforesouce.util.FileContainer;
import yh.subsys.inforesouce.util.YHAjaxUtil;

/**
 * 临时使用的类，提供假数据
 * @author Administrator
 *
 */
public class YHFileContainerAct {
  /**
   * 查询 假数据内容
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String fileContainer(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
    Connection dbConn = null;
    List<TempFile> countDate =null; //查询集合中共有多少条假数据
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        StringBuffer sb = new StringBuffer("[");
        FileContainer fc = new FileContainer();
        List<TempFile> tf =   fc.dBcontain(); 
        countDate= fc.countNumber();
        //System.out.println(tf.size()+"pppppppp");
       //for(int i = 0; i<tf.size(); i++){
        for(int i = 0; i<2; i++){
          int number = tf.get(i).getSeqId();
          Date da= tf.get(i).getChangeDate();
          String time = da.toLocaleString();
          time = time.substring(0, 8);
          sb.append("{");
          sb.append("number:\"" + tf.get(i).getSeqId()).append("\"");
          sb.append(",name:\"" + tf.get(i).getName()).append("\""); 
          sb.append(",size:\"" + tf.get(i).getSize() + "\"");
          sb.append(",type:\"" + tf.get(i).getType()+ "\"");
          sb.append(",date:\"" + time+ "\"");
          sb.append(",dept:\"" + tf.get(i).getDept()+ "\"");
          sb.append(",oper:\"" + tf.get(i).getOper()+ "\"");
          sb.append(",count:\""+countDate.size()+"\"");
          sb.append("},"); 
        }
        if(tf.size() > 0)
          sb.deleteCharAt(sb.length() - 1);
       sb.append("]");
       //System.out.println(sb);
       YHAjaxUtil.ajax(sb.toString(), response);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/subsys/inforesource/tree/Content.jsp";
    return null;
  }
  /**
   * 点击go 查询搜索的数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
     String findDate = request.getParameter("searchDate");
    Connection dbConn = null;
    List<TempFile> searchDate =null;
    List<TempFile> countDate =null; //搜索的内容共有多少条数
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        StringBuffer sb = new StringBuffer("[");
        FileContainer fc = new FileContainer();
        if(!"".equals(findDate) && findDate!=null){
        countDate =  fc.countDate(findDate); //搜索内容共有的条数
        }
         if(findDate!="" && findDate!=null &&findDate!="undefined"){
          searchDate = fc.findDate(findDate);
          }
        if(searchDate!=null && searchDate.size()>0){
          //for(int i=0; i<searchDate.size(); i++){
          for(int i=0; i<2&&i<searchDate.size(); i++){ //因为每页显示2条数据 默认首页显示2条    如果默认首页显示全部用上面注释的for循环
          Date da= searchDate.get(i).getChangeDate();
          String time = da.toLocaleString();
          time = time.substring(0, 8); 
          sb.append("{");
          sb.append("name:\""+searchDate.get(i).getName()+"\"");
          sb.append(",size:\""+searchDate.get(i).getSize()+"\"");
          sb.append(",type:\""+searchDate.get(i).getType()+"\"");
          sb.append(",date:\""+time+"\"");
          sb.append(",dept:\""+searchDate.get(i).getDept()+"\"");
          sb.append(",oper:\""+searchDate.get(i).getOper()+"\"");
          sb.append(",count:\""+countDate.size()+"\"");
          sb.append("},");
        }
       }
        if(searchDate!=null && searchDate.size()>0)
          sb.deleteCharAt(sb.length()-1);
        
        sb.append("]");
        //System.out.println(sb+"ddddddddddddddddd");
       YHAjaxUtil.ajax(sb.toString(), response);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/subsys/inforesource/tree/Content.jsp";
    return null;
  }
  
  public String searchDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
     String findDate = request.getParameter("searchDate");
     String num = request.getParameter("num");
     //System.out.println(num);
     int   number=0;
     if(num==null || num.equals("")||num.equals("null")){
      number = 0;
     }else{
    number = Integer.parseInt(num);
     }
     //System.out.println(findDate);
    Connection dbConn = null;
    List<TempFile> searchDate =null;
    List<TempFile> countDate =null; //搜索内容共有的条数
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        StringBuffer sb = new StringBuffer("[");
        FileContainer fc = new FileContainer();
        
       // List<TempFile> search =   fc.findpage(findDate,number);
       
        if(findDate!="" && findDate!=null &&findDate!="undefined"){
          searchDate = fc.findpage(findDate,number);
          countDate =  fc.countDate(findDate); //搜索内容共有的条数
          //System.out.println(countDate.size()+"bbbbbbbbbbbbbb");
        }else{
          searchDate =fc.findpage(findDate,number);
          countDate= fc.countNumber(); //获得集合假数据总条数
          //System.out.println(countDate.size()+"aaaaaaaaaaaaaaa");
        } 
        if(searchDate.size()>0){
          for(int i=0; i<searchDate.size(); i++){
          Date da= searchDate.get(i).getChangeDate();
          String time = da.toLocaleString();
          time = time.substring(0, 8); 
          sb.append("{");
          sb.append("name:\""+searchDate.get(i).getName()+"\"");
          sb.append(",size:\""+searchDate.get(i).getSize()+"\"");
          sb.append(",type:\""+searchDate.get(i).getType()+"\"");
          sb.append(",date:\""+time+"\"");
          sb.append(",dept:\""+searchDate.get(i).getDept()+"\"");
          sb.append(",oper:\""+searchDate.get(i).getOper()+"\"");
          sb.append(",count:\""+countDate.size()+"\"");
          sb.append("},");
        }
       } 
        if(searchDate.size()>0)
          sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        //System.out.println(sb+"ddddddddddddddddd");
       YHAjaxUtil.ajax(sb.toString(), response);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/subsys/inforesource/tree/Content.jsp";
    return null;
  }
  /**
   * 鼠标划上去排列顺序
   */
  public String alignOder(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
  //YHOut.println("ssssssssssssssssssssss");
   try{ 
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     String aa ="";
     int sum=4;
     YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     FileContainer fc = new FileContainer();
     StringBuffer sb = new StringBuffer("[");
     List<TempFile> oder = fc.alignOder(aa,sum);
     if(oder.size()>0){
       for(int i=0; i<2; i++){
         Date da= oder.get(i).getChangeDate();
         String time = da.toLocaleString();
         time = time.substring(0, 8);
         sb.append("{");
         sb.append("name:\""+oder.get(i).getName()+"\"");
         sb.append(",size:\""+oder.get(i).getSize()+"\"");
         sb.append(",type:\""+oder.get(i).getType()+"\"");
         sb.append(",date:\""+time+"\"");
         sb.append(",dept:\""+oder.get(i).getDept()+"\"");
         sb.append(",oper:\""+oder.get(i).getOper()+"\"");
         sb.append(",count:\""+oder.size()+"\"");
         sb.append("},");
       }
     }
     if(oder.size()>0)
       sb.deleteCharAt(sb.length()-1);
     sb.append("]");
     //System.out.println(sb.toString());
     YHAjaxUtil.ajax(sb.toString(), response);
   }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
   return null;
  }
  
  public String alignOder1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
     String num = request.getParameter("num");
     //System.out.println(num);
     int   number=0;
     if(num==null || num.equals("")||num.equals("null")){
      number = 0;
     }else{
    number = Integer.parseInt(num);
     }
    Connection dbConn = null;
    List<TempFile> searchDate =null;
    List<TempFile> countDate =null; //搜索内容共有的条数
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        StringBuffer sb = new StringBuffer("[");
        FileContainer fc = new FileContainer();
         
        searchDate =fc.alignOder1(number);
          countDate= fc.countNumber(); //获得集合假数据总条数
          
        if(searchDate.size()>0){
          for(int i=0; i<searchDate.size(); i++){
          Date da= searchDate.get(i).getChangeDate();
          String time = da.toLocaleString();
          time = time.substring(0, 8); 
          sb.append("{");
          sb.append("name:\""+searchDate.get(i).getName()+"\"");
          sb.append(",size:\""+searchDate.get(i).getSize()+"\"");
          sb.append(",type:\""+searchDate.get(i).getType()+"\"");
          sb.append(",date:\""+time+"\"");
          sb.append(",dept:\""+searchDate.get(i).getDept()+"\"");
          sb.append(",oper:\""+searchDate.get(i).getOper()+"\"");
          sb.append(",count:\""+countDate.size()+"\"");
          sb.append("},");
        }
       } 
        if(searchDate.size()>0)
          sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        //System.out.println(sb+"ddddddddddddddddd");
       YHAjaxUtil.ajax(sb.toString(), response);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/subsys/inforesource/tree/Content.jsp";
    return null;
  } 
  
  /**
   * 点击节点传递参数
   */
  public static String passParameter(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String vsplit="";
    String para = request.getParameter("para");
  
    try{
    String psplit[]= para.split(",");
    for(int i=0; i<psplit.length; i++){
    vsplit  = psplit[i];
    
    }
    //System.out.println("split:::::"+vsplit);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
    return null;
  }
}
