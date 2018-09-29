package yh.subsys.oa.profsys.logic.in;

import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class YHInProjectMemLogic {
  private static Logger log = Logger.getLogger(YHInProjectMemLogic.class);
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String projId,String projStatus) throws Exception{
    String sql = "select pm.SEQ_ID,pm.MEM_NUM,pm.MEM_POSITION,pm.MEM_NAME,pm.MEM_SEX,pm.MEM_BIRTH,pm.MEM_ID_NUM,"
      + "pm.MEM_PHONE,pm.MEM_MAIL,pm.MEM_FAX,pm.MEM_ADDRESS,pm.ATTACHMENT_ID,pm.ATTACHMENT_NAME"
      +" from oa_project_member pm "
      +" where pm.PROJ_ID = " + projId  + " and pm.PROJ_MEM_TYPE = '0'" ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
}
