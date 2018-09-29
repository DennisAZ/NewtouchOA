package yh.rad.devmgr.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHProps;
import yh.core.dto.YHCodeLoadParam;
import yh.core.dto.YHCodeLoadParamSet;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.rad.devmgr.global.YHRadDevMgrConst;
import yh.rad.devmgr.util.YHDocInfoUtility;

public class YHSubSysListAct {
  /**
   * 加载下拉框数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHCodeLoadParamSet paramSet = (YHCodeLoadParamSet)YHFOM.build(request.getParameterMap());
      String ctxPath = (String)request.getAttribute(YHActionKeys.ACT_CTX_PATH);
      
      YHCodeLoadParam param = paramSet.getParam();
      if (param == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递必要的参数");
        return "/core/inc/rtjson.jsp";
      }
      String basePath = request.getParameter("basePath");
      if (YHUtility.isNullorEmpty(basePath)) {
        basePath = "rad\\devmgr\\";
      }else {
        basePath = basePath.replaceAll("/", "\\");
      }
      String subsysPath = ctxPath + basePath;
      List<YHProps> subsysList = YHDocInfoUtility.loadInfoList(subsysPath);
      if (YHUtility.isNullorEmpty(param.getValue()) && subsysList.size() > 0) {
        param.setValue(subsysList.get(0).get(YHRadDevMgrConst.ENTRY_DIR));
      }
      String rtJson = YHDocInfoUtility.toSelectJson(subsysList,
          param.getCntrlId(), param.getValue());
      
      request.setAttribute(YHActionKeys.RET_DATA, rtJson);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载子系统列表失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
