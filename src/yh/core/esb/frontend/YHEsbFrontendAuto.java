package yh.core.esb.frontend;

import java.io.File;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.esb.common.YHUploadTask;
import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.frontend.data.YHEsbUploadTask;
import yh.core.esb.frontend.logic.YHEsbFrontendLogic;

public class YHEsbFrontendAuto extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yh.core.esb.frontend.YHEsbFrontendAuto");

  public void doTask() {
    try {
      final HttpHost host = ClientPropertiesUtil.getHttpHost();
      if (host == null) {
        
      }
      
      YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
      List<YHEsbUploadTask> list  = logic.getUploadFieldTaskByStatus( "1,-1");
      for (YHEsbUploadTask t : list) {
          File file = new File(t.getFileName());
          if (!file.exists()) 
            continue;
          try {
            YHUploadTask upload = new YHUploadTask(host, file, t.getToId(), t.getGuid() , t.optGuid , t.message);
            boolean flag =  upload.initialize(YHEsbFrontend.getHc());
            //表示失败
            if (!flag) {
              continue;
            }
            //是否已经上传中
            synchronized(YHEsbFrontend.loc) {
              boolean uploading  =  YHEsbFrontend.nowTask.contains(t.getGuid());
              if (t.getStatus() == 1 && uploading) {
                continue;
              }
            }
            //修改传送状态为传送中
            upload.transfer(YHEsbFrontend.getHc());
            upload.complete(YHEsbFrontend.getHc() );
          } catch (Exception e) {
            e.printStackTrace();
            logic.updateStatus(t.getGuid(), "-1");
          }
        }
    } catch (Exception e) {
      log.debug(e.getMessage(),e);
    } 
  }
}
