package yh.subsys.jtgwjh.setting.act;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.setting.data.YHJhSysPara;
import yh.subsys.jtgwjh.setting.logic.YHJhSysParaLogic;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;

public class YHJhSysParaAct {

  /**
   * 添加-修改
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addAndUpdate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHJhSysPara hall = (YHJhSysPara) YHFOM.build(request.getParameterMap());

      String paraName = request.getParameter("paraName");
      String paraValue = request.getParameter("paraValue");
      YHJhSysPara jhSysPara = YHJhSysParaLogic.jhHallObj(dbConn, paraName);
      YHSysPara sysPara = YHJhSysParaLogic.hallObj(dbConn, paraName);//
      if (sysPara == null) {
        YHJhSysParaLogic.addHall(dbConn, sysPara);
      } else {
        YHJhSysParaLogic.updateHall(dbConn, sysPara);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 添加-修改,团组号设置
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String NOaddAndUpdate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysPara hall = (YHSysPara) YHFOM.build(request.getParameterMap());
      String paraName = request.getParameter("paraName");
      YHSysPara sysHall = YHJhSysParaLogic.hallObj(dbConn, paraName);
      if (sysHall == null) {
        YHJhSysParaLogic.addHall(dbConn, hall);
      } else {
        YHJhSysParaLogic.updateHall(dbConn, hall);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectObj(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String paraName = request.getParameter("paraName");
      YHSysPara sysHall = null;
      String data = "";
      if (!YHUtility.isNullorEmpty(paraName)) {
        sysHall = YHJhSysParaLogic.hallObj(dbConn, paraName);
        // 定义数组将数据保存到Json中
        if (sysHall != null) {
          data = data + YHFOM.toJson(sysHall);
        }
      }
      if (data.equals("")) {
        data = "{seqId:0,paraName:\"" + paraName + "\",paraValue:\"0\"}";
      }
      // 保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }

    return "/core/inc/rtjson.jsp";
  }

  /**
   * 字符串前面补0
   * 
   * @param number
   * @param i
   * @return
   */
  public static String getAutoStr(int number, int i) {
    // 待测试数据

    String str = "0";
    // 得到一个NumberFormat的实例

    NumberFormat nf = NumberFormat.getInstance();
    // 设置是否使用分组
    nf.setGroupingUsed(false);
    // 设置最大整数位数

    nf.setMaximumIntegerDigits(i);
    // 设置最小整数位数
    nf.setMinimumIntegerDigits(i);
    str = nf.format(number);
    return str;
  }

  /**
   * 查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectESBInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String EsbUser = request.getParameter("EsbUser") == null ? "" : request
          .getParameter("EsbUser");
      String data = "";
      if (!YHUtility.isNullorEmpty(EsbUser)) {
        EsbUser = EsbUser.replace("'", "''");
        String[] str = { "dept_code = '" + EsbUser + "'" };
        List<YHExtDept> list = YHDeptTreeLogic.select(dbConn, str);
        if (list.size() > 0) {
          data = YHFOM.toJson(list.get(0)) + "";
        }
      }
      if (data.equals("")) {
        data = "{}";
      }
      // 保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }

    return "/core/inc/rtjson.jsp";
  }

  /**
   * 导入部门人员、公章数据
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String importData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    InputStream rarFile = null;
    OutputStream ops = null;
    String webrootPath = YHSysProps.getAttachPath() + "/PDF";// 接收附件存放路径
    String personDataFile = "";
    String sealDataFile = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String EsbUser = fileForm.getParameter("EsbUser");//单位代码
      String unitInfo = fileForm.getParameter("unitInfo");//单位名称
      String netType = fileForm.getParameter("netType");//网络类型
      String EsbServiceIp = fileForm.getParameter("EsbServiceIp");//ESB服务器IP、
      String EsbServicePort = fileForm.getParameter("EsbServicePort");//ESB端口号
      String recvFilePath = fileForm.getParameter("recvFilePath");//接收附件目录
      rarFile = fileForm.getInputStream("dataFile");//数据包
      if (rarFile == null) {// 如果没有附件了跳出
        return "";
      }
      CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
      System.out.println(zip.getNextEntry());
      ZipEntry entry;
      while ((entry = zip.getNextEntry()) != null) {//循环zip下的所有文件和目录
        String attName = entry.getName();
        if (YHUtility.isNullorEmpty(attName)) {
          ;
          continue;
        }
        if (attName.endsWith("PERSON_DATA.xml") ||  attName.endsWith("SEAL_DATA.xml")) {// 人员数据
         
        }else{
          continue;
        }

        String newFile = "";
        if (attName.endsWith("PERSON_DATA.xml")) {// 人员数据
          personDataFile = attName;
        } else if (attName.endsWith("SEAL_DATA.xml")) {// 公章
          sealDataFile = attName;
        }

        File outFile = new File(webrootPath + "/" + attName);
        File outPath = outFile.getParentFile();
        if (!outPath.exists()) {
          outPath.mkdirs();
        }
        if (outFile.exists()) {
          YHFileUtility.deleteAll(outFile);
        }
            try {
              outFile.createNewFile();
              InputStream in = null;
              FileOutputStream out = new FileOutputStream(outFile);
              int len = 0;
              byte[] buff = new byte[4096];
              while ((len = zip.read(buff)) != -1) {
                out.write(buff, 0, len);
              }
              zip.closeEntry();
              out.close();
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }


      String path = request.getContextPath();
      String url = path + "/subsys/jtgwjh/setting/init/loginRemind.jsp?type=1";
      if (YHUtility.isNullorEmpty(personDataFile)
          || YHUtility.isNullorEmpty(sealDataFile)) {
        url = path + "/subsys/jtgwjh/setting/init/loginRemind.jsp?type=0";
        response.sendRedirect(url);
        return "";
      }
      YHJhSysParaLogic.parseSealXML( webrootPath  + "/" + sealDataFile, dbConn,user);
      
      YHJhSysParaLogic.parsePersonXML( webrootPath  + "/" + personDataFile, dbConn, user);
     
      YHSysPara sysPara = YHJhSysParaLogic.hallObj(dbConn, "INIT_DONE");//
      if (sysPara != null) {
        sysPara.setParaValue("1");
        YHJhSysParaLogic.updateHall(dbConn, sysPara);
      } else {
        sysPara = new YHSysPara();
        sysPara.setParaName("INIT_DONE");
        sysPara.setParaValue("1");
        YHJhSysParaLogic.addHall(dbConn, sysPara);
      }
      response.sendRedirect(url);
      
    } catch (Exception ex) {
      ex.printStackTrace();
      // System.out.println(ex.getMessage());
      // throw ex;
    }

    return "";
  }

}
