package yh.core.funcs.utilapps.info.train.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.utilapps.info.train.logic.YHTrainLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class YHTrainAct {
  
  public void searchTrain(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHTrainLogic logic = new YHTrainLogic();
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String train = (String)request.getParameter("train");
      String start = (String)request.getParameter("start");
      String end = (String)request.getParameter("end");
      
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      
      
      String sql = "";
      YHPageDataList pageDataList = null;
      if(train != null){
        sql = " select t.id seq_id,train,s1.station starts,s2.station ends,k.kind,ftime,etime,t.distance,day "
            + " ,(select min(p.distance) from oa_train_price p where p.distance >= t.distance ) as pdistance "
            + " ,(case k.id when 1 then 'kttkz' "
            + " when 2 then 'ktpkz' "
            + " when 3 then '' "
            + " when 4 then 'kttkz' "
            + " when 5 then 'tkz' "
            + " when 6 then 'pkz' "
            + " when 7 then '' "
            + " when 8 then 'tkz' "
            + " when 9 then 'tkz' "
            + " end) as type_price "
            + " ,'0' as price "
            + " from oa_train t "
            + " join oa_train_station s1 on t.fstation = s1.id "
            + " join oa_train_station s2 on t.estation = s2.id "
            + " join oa_train_type k on t.kind = k.id "
            + " where 1=1 "
            + " and train='" + train.toUpperCase().trim() +"'"
            + " order by seq_id asc ";
      }
      if(start != null && end != null){
        if (dbms.equals("sqlserver")) {
          sql = " select t2.seq_id, t2.train, s3.station starts, s4.station ends, t2.station pstart, s2.station pend, t2.kind, t2.depart ftime, p2.arrive etime, (cast(p2.distance as int)-cast(t2.distance as int)) as distance, (cast(p2.day as int) - cast(t2.day as int)) as day "
              + " ,(select min(p.distance) from oa_train_price p where cast(p.distance as int) >= (cast(p2.distance as int)-cast(t2.distance as int))) as pdistance "
              + " ,(case t2.id when 1 then 'kttkz' " 
              + " when 2 then 'ktpkz' " 
              + " when 3 then '' " 
              + " when 4 then 'kttkz' " 
              + " when 5 then 'tkz' " 
              + " when 6 then 'pkz' " 
              + " when 7 then '' " 
              + " when 8 then 'tkz' " 
              + " when 9 then 'tkz' " 
              + " end) as type_price  "
              + " ,'0' as price "
              + " from "
              + " (select t1.id seq_id,t1.train,s1.station,p1.distance,k1.kind,p1.depart,p1.day,k1.id from oa_train t1 "
              + " join TRAIN_PASS p1 on t1.id = p1.trainid "
              + " join oa_train_station s1 on p1.station = s1.id and s1.station like '%" + start.trim() + "%' "
              + " join oa_train_type k1 on t1.kind = k1.id ) t2 "
              + " join TRAIN_PASS p2 on t2.seq_id = p2.trainid and (cast(p2.distance as int) - cast(t2.distance as int)) > 0 "
              + " join oa_train_station s2 on p2.station = s2.id and s2.station like '%" + end.trim() + "%' "
              + " join oa_train t3 on t2.seq_id = t3.id "
              + " join oa_train_station s3 on t3.fstation = s3.id  "
              + " join oa_train_station s4 on t3.estation = s4.id  "
              + " order by seq_id asc ";
        }
        else{
          sql = " select t2.seq_id, t2.train, s3.station starts, s4.station ends, t2.station pstart, s2.station pend, t2.kind, t2.depart ftime, p2.arrive etime, (p2.distance-t2.distance) as distance, (p2.day - t2.day) as day "
              + " ,(select min(p.distance) from oa_train_price p where p.distance >= (p2.distance-t2.distance)) as pdistance "
              + " ,(case t2.id when 1 then 'kttkz' " 
              + " when 2 then 'ktpkz' " 
              + " when 3 then '' " 
              + " when 4 then 'kttkz' " 
              + " when 5 then 'tkz' " 
              + " when 6 then 'pkz' " 
              + " when 7 then '' " 
              + " when 8 then 'tkz' " 
              + " when 9 then 'tkz' " 
              + " end) as type_price  "
              + " ,'0' as price "
              + " from "
              + " (select t1.id seq_id,t1.train,s1.station,p1.distance,k1.kind,p1.depart,p1.day,k1.id from oa_train t1 "
              + " join TRAIN_PASS p1 on t1.id = p1.trainid "
              + " join oa_train_station s1 on p1.station = s1.id and s1.station like '%" + start.trim() + "%' "
              + " join oa_train_type k1 on t1.kind = k1.id ) t2 "
              + " join TRAIN_PASS p2 on t2.seq_id = p2.trainid and (p2.distance - t2.distance) > 0 "
              + " join oa_train_station s2 on p2.station = s2.id and s2.station like '%" + end.trim() + "%' "
              + " join oa_train t3 on t2.seq_id = t3.id "
              + " join oa_train_station s3 on t3.fstation = s3.id  "
              + " join oa_train_station s4 on t3.estation = s4.id  "
              + " order by seq_id asc ";
        }
      }
      
      
      
      
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      pageDataList = logic.queryPrice(dbConn, pageDataList);
      
      PrintWriter out = response.getWriter();
      out.println(pageDataList.toJson());
      out.flush();
      
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
  }
  
  public String searchTrainInfoShow(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHTrainLogic logic = new YHTrainLogic();
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map<String,String> map = YHFOM.buildMap(request.getParameterMap());
      String countStation = logic.countStation(dbConn, map);
      String allPrice = logic.allPrice(dbConn, map);
      
      String json = "{\"countStation\":\""+countStation+"\",\"allPrice\":\""+allPrice+"\"}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
      request.setAttribute(YHActionKeys.RET_DATA, json);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public void searchTrainInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHTrainLogic logic = new YHTrainLogic();
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map<String,String> map = YHFOM.buildMap(request.getParameterMap());
      
      String sql = " select '' url_no,s.station,p.arrive,p.depart,p.distance,p.day from TRAIN_PASS p "
                 + " join oa_train_station s on p.station = s.id "
                 + " where p.trainid =" + map.get("seqId");
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      
      PrintWriter out = response.getWriter();
      out.println(pageDataList.toJson());
      out.flush();
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
  }
}
