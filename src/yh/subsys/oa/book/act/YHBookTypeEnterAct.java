package yh.subsys.oa.book.act;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.logic.YHBookTypeEnterLogic;

public class YHBookTypeEnterAct { 
  private static Logger log = Logger.getLogger(YHBookTypeEnterAct.class);
  public String addBookTypeEnter(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map<String, String> attr = null;
    String attrId = "";
    String attrName = "";
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> paramMap = fileForm.getParamMap();
        String deptId = paramMap.get("deptId");
        String bookName =  paramMap.get("bookName");
        String bookNo =  paramMap.get("bookNo");
        String typeId =  paramMap.get("typeId");
        int booktypeId = Integer.valueOf(typeId);
        String author =  paramMap.get("author");
        String isbn =  paramMap.get("isbn");
        String pubHouse =  paramMap.get("pubHouse");
        String statrTime = paramMap.get("statrTime");
        String area = paramMap.get("area");
        String amt = paramMap.get("amt");
        String price = paramMap.get("price");
        String brief = paramMap.get("brief");
        String deptDesc = paramMap.get("deptDesc");
        String dept = paramMap.get("dept");
        String lend = paramMap.get("lend");
        String borrPerson = paramMap.get("borrPerson");
        String memo = paramMap.get("memo");
  
        String attachmentName = paramMap.get("attachment");
        String seqId = paramMap.get("seqId");

        fileForm.getParamMap().put("fileName", attachmentName);
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHBookTypeEnterLogic newsManageLogic = new YHBookTypeEnterLogic();
        if(fileForm!=null){
            attr = newsManageLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
            Set<String> keys = attr.keySet();
            for (String key : keys){
              String value = attr.get(key);
              
              attrId += key ;
           
              attrName += value ;
           }
        }
        YHBookInfo bi = new YHBookInfo();
        bi.setDept(Integer.parseInt(deptId));
        bi.setBookName(bookName);
        bi.setBookNo(bookNo);
        bi.setTypeId(booktypeId);
        bi.setAuthor(author);
        bi.setIsbn(isbn);
        bi.setPubHouse(pubHouse);
        bi.setPubDate(statrTime);
        bi.setArea(area);
        bi.setAmt(Integer.parseInt(amt.trim()));
        if(YHUtility.isNullorEmpty(price))
          price = "0.0";
        bi.setPrice(Double.parseDouble(price));
        bi.setBrief(brief);
        bi.setOpen(dept);
        bi.setLend(lend);
        bi.setBorrPerson(borrPerson);
        bi.setMemo(memo);
        bi.setAttachmentId(attrId);
        bi.setAttachmentName(attrName);
        
        List<YHBookInfo> bookInfo = YHBookTypeEnterLogic.isBookNameRepeat(dbConn, person,bookName);
        String bookNameIsRepeat="";
        for(int i=0; i<bookInfo.size(); i++){
          bookNameIsRepeat = String.valueOf(bookInfo.get(i));
        }
        if(!YHUtility.isNullorEmpty(bookNameIsRepeat)){
          return "/subsys/oa/book/manage/bookNameIsRepeat.jsp?bookName="+bookName;
        }
        List<YHBookInfo> bookNoInfo = YHBookTypeEnterLogic.isBookNoRepeat(dbConn, person,bookNo);
        String bookNoIsRepeat="";
        for(int i=0; i<bookNoInfo.size(); i++){
          bookNoIsRepeat = String.valueOf(bookNoInfo.get(i));
        }
        if(!YHUtility.isNullorEmpty(bookNoIsRepeat)){
          return "/subsys/oa/book/manage/bookNoIsRepeat.jsp?bookNo="+bookNo;
        }
        
        int ok = YHBookTypeEnterLogic.addBookTypeEnter(dbConn, person,bi);
        //String url = "typeId="+typeId+"&lend="+lend+"&bookName="+bookName+"&bookNo="+bookNo+"&author="+author+"&isbn="+isbn+"&pubHouse="+pubHouse+"&area="+area;
        if(ok!=0){
          return "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookType.act";
        }
        /*if(ok!=0){
         // return "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookType.act?url"+url;
          return "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+url;
        }*/
       return "";
  }
 /**
  * 编辑图书基本信息
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String editBookTypeInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      Map<String, String> attr = null;
      String attrId = "";
      String attrName = "";
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
       
      Map<String, String> paramMap = fileForm.getParamMap();
      
      String deptId = paramMap.get("deptId");
      String bookName =  paramMap.get("bookName");
      String bookNo =  paramMap.get("bookNo");
      String typeId =  paramMap.get("typeId");
      int booktypeId =0; //没有图书类型默认为0
      if(!YHUtility.isNullorEmpty(typeId)){
          booktypeId = Integer.valueOf(typeId);
      }
      String author =  paramMap.get("author");
      String isbn =  paramMap.get("isbn");
      String pubHouse =  paramMap.get("pubHouse");
      String statrTime = paramMap.get("statrTime");
      String area = paramMap.get("area");
      String amt = paramMap.get("amt");
      String price = paramMap.get("price");
      String brief = paramMap.get("brief");
      String deptDesc = paramMap.get("deptDesc");
      String dept = paramMap.get("dept");
      String lend = paramMap.get("lend");
      String borrPerson = paramMap.get("borrPerson");
      String memo = paramMap.get("memo");
      String attachmentName = paramMap.get("attachment");
      //String seqId = paramMap.get("seqId");
      String seqId = request.getParameter("seqId");
          //String attachmentName = request.getParameter("attachment");
         // String seqIds = request.getParameter("seqId");
          //int seqId = Integer.parseInt(seqIds);
          fileForm.getParamMap().put("fileName", attachmentName);
          //YHOut.println(fileForm.getParamMap().get("fileName"));
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          //YHNewsManageLogic newsManageLogic = new YHNewsManageLogic();
          YHBookTypeEnterLogic newsManageLogic = new YHBookTypeEnterLogic();
          if(fileForm!=null){
              attr = newsManageLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
              Set<String> keys = attr.keySet();
              for (String key : keys){
                String value = attr.get(key);
                
                attrId += key ;
             
                attrName += value ;
             }
          }
          YHBookInfo bi = new YHBookInfo();
            
          bi.setDept(Integer.parseInt(deptId));
          bi.setBookName(tsziFu(bookName));
          bi.setBookNo(tsziFu(bookNo));
          bi.setTypeId(booktypeId);
          bi.setAuthor(tsziFu(author));
          bi.setIsbn(tsziFu(isbn));
          bi.setPubHouse((pubHouse));
          bi.setPubDate(tsziFu(statrTime));
          bi.setArea(tsziFu(area));
          bi.setAmt(Integer.parseInt(amt));
          if(YHUtility.isNullorEmpty(price))
            price = "0.0";
          bi.setPrice(Double.parseDouble(price));
          bi.setBrief(tsziFu(brief));
          bi.setOpen(tsziFu(dept));
          bi.setLend(tsziFu(lend));
          bi.setBorrPerson(tsziFu(borrPerson));
          bi.setMemo(tsziFu(memo));
          bi.setAttachmentId(attrId);
          bi.setAttachmentName(attrName);
          bi.setSeqId(Integer.parseInt(seqId));
          int ok = YHBookTypeEnterLogic.editBookTypeInfo(dbConn, person,bi);
          String url = "typeId="+"0"+"&lend="+""+"&bookName="+""+"&bookNo="+""+"&author="+""+"&isbn="+""+"&pub_house="+""+"&area="+"";
          if(ok!=0){
           // return "/yh/subsys/oa/book/act/YHBookTypeAct/selectBookType.act?url"+url;
            return "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+url;
          }
         return "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+url;
    }
  
  public String tsziFu(String zf) throws Exception {
    String newStr = zf.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");

    return newStr;
  }
  public String findBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        String str = "";
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHBookTypeEnterLogic bte = new YHBookTypeEnterLogic();
        str = bte.findBookType(dbConn, person);
        //System.out.println(str);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"图书查询成功");
        request.setAttribute(YHActionKeys.RET_DATA, str);
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String blurFindBookType(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
      Connection dbConn = null;
      String bookNo = request.getParameter("bookNo");
      try{
        String str = "";
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
         YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
         YHBookTypeEnterLogic tte = new YHBookTypeEnterLogic();
         str = tte.blurFindBookType(dbConn, person,bookNo);
         
         request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);//RET_STATE返回状态  RETURN_OK正确返回
         request.setAttribute(YHActionKeys.RET_MSRG, "图书模糊查询成功");//RET_MSRG 返回消息
         request.setAttribute(YHActionKeys.RET_DATA, str);//RET_DATA 返回数据
         request.setAttribute("data", str);
      }catch(Exception ex){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
    return "/subsys/oa/book/borrow_manage/borrow/bookno_select/bookno_info.jsp";
  } 
}
