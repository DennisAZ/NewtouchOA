<%@ page language="java" import="java.util.*, yh.core.funcs.person.data.YHPerson,java.text.SimpleDateFormat,yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId");
  String dateStr = YHUtility.getCurDateTimeStr();
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>上下班登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function remark(seqId){
  var URL="<%=contextPath%>/core/funcs/attendance/personal/dutyRemark.jsp?seqId=" + seqId ;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=250,width=450,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top=150,left="+myleft+",resizable=yes");
}
function updateDuty(seqId,registerType){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/updateRegisterTimeById.act?registerType=" + registerType + "&seqId=" + seqId;
  window.location = requestURL;
}
function doOnload(){
  Time();
  var requestURLName = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectDutyByUserIdName.act";
  var rtJsonName = getJsonRs(requestURLName);
  if(rtJsonName.rtState == "1"){
    alert(rtJsonName.rtMsrg); 
    return ;
    }
  var prcsName = rtJsonName.rtData;
  if(prcsName.seqId){
    document.getElementById("dutyName").innerHTML = prcsName.dutyName ;
  }
  //判断是否受IP权限设置的人员

  if(isNoIp()){
    //判断是否为请假时间段
    var requestURLLeave = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/checkIsLeave.act";
    var rtJson = getJsonRs(requestURLLeave);
    var leaveJson = rtJson.rtData;
    var isLeave = leaveJson.isLeave;
    if(isLeave==1){
      var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"300"}).update("<tr>"
          + "<td class='msg info'>"
          + "<div class='content' style='font-size:12pt'>今天您已请假不允许登记</td></tr>");
        $('IntervalTable').appendChild(table);
    }else{
    //判断是否为 节假日 
      var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/checkHoliday.act";
      var rtJson = getJsonRs(requestURLHoliday);
      var holidayJson = rtJson.rtData;
      var holiday = holidayJson.status;
      if(holiday == 1){
        showTableInterval();
        showTableDuty(holiday);
        }
      if(holiday == 0 ){
        var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"300"}).update("<tr>"
            + "<td class='msg info'>"
            + "<div class='content' style='font-size:12pt'>节假日不允许登记</td></tr>");
          $('IntervalTable').appendChild(table);
      }
    }
  }else{
    //判断Ip
    var ipRule = visitIp();
    if(ipRule.isIp=="1"){ //1允许登记2为不允许
      //判断是否为请假时间段
      var requestURLLeave = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/checkIsLeave.act";
      var rtJson = getJsonRs(requestURLLeave);
      var leaveJson = rtJson.rtData;
      var isLeave = leaveJson.isLeave;
      if(isLeave==1){
        var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"300"}).update("<tr>"
            + "<td class='msg info'>"
            + "<div class='content' style='font-size:12pt'>申请销假前不允许登记</td></tr>");
          $('IntervalTable').appendChild(table);
      }else{
      //判断是否为 节假日 
        var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/checkHoliday.act";
        var rtJson = getJsonRs(requestURLHoliday);
        var holidayJson = rtJson.rtData;
        var holiday = holidayJson.status;
        if(holiday == 1){
          showTableInterval();
          showTableDuty(holiday);
          }
        if(holiday == 0 ){
          var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"300"}).update("<tr>"
              + "<td class='msg info'>"
              + "<div class='content' style='font-size:12pt'>节假日不允许登记</td></tr>");
            $('IntervalTable').appendChild(table);
        }
      }
    }else{
    
      var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"440"}).update("<tr>"
          + "<td class='msg info'>  <h4 class='title'>警告</h4>"
          + "<div class='content' style='font-size:12pt'>您无权限从该IP("+ipRule.ip+")考勤!</div></td></tr>");
        $('IntervalTable').appendChild(table);
    }

  }
}
//判断是否受IP访问权限
function visitIp(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getIpRuleByType.act";
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  return data;
}   
//判断是否不受Ip限制的

function isNoIp(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getNoIpUser.act";
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.isNoIp=="1"){
    return true;
  }
  return false;
}
//判断是否为出差时间

function evection(){
  var date = '<%=dateStr%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isEvection.act?date="+date;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为出差日
    return false;
  }
  return true;
}
//判断是否为公休日
function generalIs(general){
  var generals = '';
  if(general!=''){
    generals = general.split(',');
  }
  var d=new Date();   
  var week = d.getDay();
  if(week==0){
    week = 7;
  }
  for(var i = 0;i<generals.length;i++){
    if(week==generals[i]){
      return true;
    }
  }
  return false; 
}
//判断是否为请假时间段
function IsLeave(date){
  var userId = '<%=userId%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isLeave.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.leave=="1"){//0为请假

    return false;
  }
  return true;
}
function showTableInterval(){
  var requestURLInteval = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHSysParaAct/selectParaInteval.act"; 
  var json = getJsonRs(requestURLInteval); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var dutyIntervalBefore1 = prcsJson.dutyIntervalBefore1;
  var dutyIntervalAfter1 = prcsJson.dutyIntervalAfter1;
  var dutyIntervalBefore2 = prcsJson.dutyIntervalBefore2;
  var dutyIntervalAfter2 = prcsJson.dutyIntervalAfter2;
  var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"500"}).update("<tr>"
    + "<td class='msg info'>"
    + "<div class='content' style='font-size:12pt'>规定时间之前"
    + dutyIntervalBefore1 +" 分钟到之后 "
    + dutyIntervalAfter1 + " 分钟这段时间可进行上班登记，规定时间之前 "
    + dutyIntervalBefore2 + "  分钟到之后"
    + dutyIntervalAfter2+ " 分钟这段时间可进行下班登记</div></td></tr>");
  $('IntervalTable').appendChild(table); 
}
function showTableDuty(holiday){
  var requestURLConfig = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectDutyByUserId.act"; 
  var json = getJsonRs(requestURLConfig); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  if(prcsJson.seqId){
    var seqId = prcsJson.seqId;
    $("seqId").value = seqId;
    var dutyName = prcsJson.dutyName;
    var dutyTime1 = prcsJson.dutyTime1;
    var dutyTime2 = prcsJson.dutyTime2;
    var dutyTime3 = prcsJson.dutyTime3;
    var dutyTime4 = prcsJson.dutyTime4;
    var dutyTime5 = prcsJson.dutyTime5;
    var dutyTime6 = prcsJson.dutyTime6;
    var dutyType1 = prcsJson.dutyType1;
    var dutyType2 = prcsJson.dutyType2;
    var dutyType3 = prcsJson.dutyType3;
    var dutyType4 = prcsJson.dutyType4;
    var dutyType5 = prcsJson.dutyType5;
    var dutyType6 = prcsJson.dutyType6;
    var dutyStatus1 = prcsJson.dutyStatus1;
    var dutyStatus2 = prcsJson.dutyStatus2;
    var dutyStatus3 = prcsJson.dutyStatus3;
    var dutyStatus4 = prcsJson.dutyStatus4;
    var dutyStatus5 = prcsJson.dutyStatus5;
    var dutyStatus6 = prcsJson.dutyStatus6;
    var c_zStatus1 = prcsJson.c_zStatus1;
    var c_zStatus2 = prcsJson.c_zStatus2;
    var c_zStatus3 = prcsJson.c_zStatus3;
    var c_zStatus4 = prcsJson.c_zStatus4;
    var c_zStatus5 = prcsJson.c_zStatus5;
    var c_zStatus6 = prcsJson.c_zStatus6;
    var general = prcsJson.general;
    var requestURLDuty = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectDuty.act"; 
    var rtjson = getJsonRs(requestURLDuty); 
    //alert(rsText);
    if(rtjson.rtState == '1'){ 
      alert(rtjson.rtMsrg); 
      return ; 
    }
    var dutyJson = rtjson.rtData;
    if(prcsJson != "undefined"){
      var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>登记次序</td>"
          + "<td nowrap align='center'>登记类型</td>"
          + "<td nowrap align='center'>规定时间</td>"
          + "<td nowrap align='center'>登记时间</td>"
          + "<td nowrap align='center'>操作</td></tr></tbody>");
        $('dutyTable').appendChild(table); 
        var  registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;

        //第一次做判断 
        if(dutyTime1 != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        $('tboday').appendChild(tr);
        if(dutyJson.length>0){
        for(var i = 0; i< dutyJson.length; i++){
          var duty = dutyJson[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
        
          if(registerType =="1"){break;}
         }
          if(registerType == "1"){
              if(dutyType1 == "1"){
                var c = "";
                if((c_zStatus1 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                  c = " <span class=big4>&nbsp;迟到</span>";
                }
                tr.update("<td nowrap align='center'>第1次登记</td>"
                    + " <td nowrap align='center'>上班登记</td>"
                    + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                    + "  <td nowrap align='center'>" + registerTime.substr(11,19) +c+ "</td>"
                    + " <td nowrap align='center'>"
                    + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                    );
            
                }else{
                  var z = "";
                  if((c_zStatus1 == "2")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                    z =  "<span class=big4>&nbsp;早退</span>";
                  }


                  
                  if(dutyStatus1 == "1"){                   
                        tr.update("<td nowrap align='center'>第1次登记</td>"
                          + " <td nowrap align='center'>下班登记</td>"
                          + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                          + "  <td nowrap align='center'>" + registerTime.substr(11,19) +z+ "</td>"
                          + " <td nowrap align='center'>"
                          + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                          );

                   }else{
                          tr.update("<td nowrap align='center'>第1次登记</td>"
                            + " <td nowrap align='center'>下班登记</td>"
                            + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                            + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                            + " <td nowrap align='center'>"
                            + "已考勤    <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a>   "
                            + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                            );
                     }
                }          
            }else{
              if(dutyType1 == "1"){
                if(dutyStatus1 == "1"){
                  tr.update("<td nowrap align='center'>第1次登记</td>"
                      + " <td nowrap align='center'>上班登记</td>"
                      + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                      + " <td nowrap align='center'>未登记</td>"
                      + " <td nowrap align='center'><a href='#' onclick = 'addDuty(1,"+dutyType1+",\""+dutyTime1+"\");'>上班登记</a> </td>"
                      );         
                   }else{
                     tr.update("<td nowrap align='center'>第1次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + "<td nowrap align='center'><a href='#' onclick = 'addDuty(1,"+dutyType1+",\""+dutyTime1+"\");'>下班登记</a> </td>"
                    );
                 }
              }
            }
         }else{
              if(dutyType1 == "1"){
                tr.update("<td nowrap align='center'>第1次登记</td>"
                    + " <td nowrap align='center'>上班登记</td>"
                    + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                    + " <td nowrap align='center'>未登记</td>"
                    + " <td nowrap align='center'><a href='#' onclick = 'addDuty(1,"+dutyType1+",\""+dutyTime1+"\");'>上班登记</a> </td>"
                   );         
               }else{
                 tr.update("<td nowrap align='center'>第1次登记</td>"
                     + " <td nowrap align='center'>下班登记</td>"
                     + " <td nowrap align='center'>" + dutyTime1 +"</td>"
                     + " <td nowrap align='center'>未登记</td>"
                     + " <td nowrap align='center'><a href='#' onclick = 'addDuty(1,"+dutyType1+",\""+dutyTime1+"\");'>下班登记</a> </td>"
               );
            }
        }
      }


        
  //第二次做判断
        if(dutyTime2.trim() != ""){ 
          var tr = new Element('tr',{"class":"TableData"}); 
          $('tboday').appendChild(tr);
          if(dutyJson.length>0){
            for(var i = 0; i< dutyJson.length; i++){
              var duty = dutyJson[i];
              registerTime = duty.registerTime;
              registerType = duty.registerType;
              seqId = duty.seqId;
              userId = duty.userId;
              registerIp = duty.registerIp;
              remark = duty.remark;
              if(registerType =="2"){break;}
             }
           
              if(registerType == "2"){ 
                  if(dutyType2 == "1"){    
                    var c = "";                 
                    if((c_zStatus2 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                      c = " <span class=big4>&nbsp;迟到</span>";
                     } 
                
                    tr.update("<td nowrap align='center'>第2次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                        + "  <td nowrap align='center'>" + registerTime.substr(11,19) + c+"</td>"
                        + " <td nowrap align='center'>"
                        + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                        );             
                 
                    }else{
                      var z = "";
                      if((c_zStatus2 == '2')&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                         z =  "<span class=big4>&nbsp;早退</span>";
                      }
                      if(dutyStatus2 == "1"){  
                          tr.update("<td nowrap align='center'>第2次登记</td>"
                            + " <td nowrap align='center'>下班登记</td>"
                            + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                            + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                            + " <td nowrap align='center'>"
                            + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                           );

                       }else{
                           tr.update("<td nowrap align='center'>第2次登记</td>"
                                + " <td nowrap align='center'>下班登记</td>"
                                + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                                + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                                + " <td nowrap align='center'>"
                                + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a> "
                                + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                            );
                         }
                    }          
                }else{
                  if(dutyType2 == "1"){
                    tr.update("<td nowrap align='center'>第2次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(2,"+dutyType2+",\""+dutyTime2+"\");'>上班登记</a> </td>"
                        );         
                       }else{
                         tr.update("<td nowrap align='center'>第2次登记</td>"
                             + " <td nowrap align='center'>下班登记</td>"
                             + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                             + " <td nowrap align='center'>未登记</td>"
                             + "<td nowrap align='center'><a href='#' onclick = 'addDuty(2,"+dutyType2+",\""+dutyTime2+"\");'>下班登记</a> </td>"
                        );
                     }
                }
             }else{
                  if(dutyType2 == "1"){
                  
                    tr.update("<td nowrap align='center'>第2次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(2,"+dutyType2+",\""+dutyTime2+"\");'>上班登记</a> </td>"
                       );        
                   }else{
                     tr.update("<td nowrap align='center'>第2次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime2 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + " <td nowrap align='center'><a href='#' onclick = 'addDuty(2,"+dutyType2+",\""+dutyTime2+"\");'>下班登记</a> </td>"
                   );
                }
            }
          }
        //第三次做判断
        if(dutyTime3 != ""){ 
          var tr = new Element('tr',{"class":"TableData"}); 
          $('tboday').appendChild(tr); 
          if(dutyJson.length>0){
            for(var i = 0; i< dutyJson.length; i++){
              var duty = dutyJson[i];
              registerTime = duty.registerTime;
              registerType = duty.registerType;
              seqId = duty.seqId;
              userId = duty.userId;
              registerIp = duty.registerIp;
              remark = duty.remark;
            
              if(registerType =="3"){break;}
             }
              if(registerType == "3"){
                  if(dutyType3 == "1"){
                    var c = "";
                    if((c_zStatus3 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                      c = " <span class=big4>&nbsp;迟到</span>";
                    }
                    tr.update("<td nowrap align='center'>第3次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                        + "  <td nowrap align='center'>" + registerTime.substr(11,19) + c+"</td>"
                        + " <td nowrap align='center'>"
                        + "已考勤   <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                        );
                
                    }else{
                      var z = "";
                      if((c_zStatus3== "2")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                        z =  "<span class=big4>&nbsp;早退</span>";
                      }
                      if(dutyStatus3 == "1"){  
                            tr.update("<td nowrap align='center'>第3登记</td>"
                              + " <td nowrap align='center'>下班登记</td>"
                              + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                              + "  <td nowrap align='center'>" + registerTime.substr(11,19) +z+ "</td>"
                              + " <td nowrap align='center'>"
                              + "已考勤   <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a>  </td>"
                              );
                       }else{
                              tr.update("<td nowrap align='center'>第3次登记</td>"
                                + " <td nowrap align='center'>下班登记</td>"
                                + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                                + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                                + " <td nowrap align='center'>"
                                + "已考勤   <a href='#' onclick ='remark(" + seqId + ");'> &nbsp;&nbsp;说明情况</a> "
                                + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                                );
                         }
                    }          
                }else{
                  if(dutyType3 == "1"){
                    tr.update("<td nowrap align='center'>第3次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(3,"+dutyType3+",\""+dutyTime3+"\");'>上班登记</a> </td>"
                        );          
                       }else{
                         tr.update("<td nowrap align='center'>第3次登记</td>"
                             + " <td nowrap align='center'>下班登记</td>"
                             + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                             + " <td nowrap align='center'>未登记</td>"
                             + "<td nowrap align='center'><a href='#' onclick = 'addDuty(3,"+dutyType3+",\""+dutyTime3+"\");'>下班登记</a> </td>"
                        );
                     }
                }
             }else{
                  if(dutyType3 == "1"){
                    tr.update("<td nowrap align='center'>第3次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(3,"+dutyType3+",\""+dutyTime3+"\");'>上班登记</a> </td>"
                       );        
                   }else{
                     tr.update("<td nowrap align='center'>第3次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + " <td nowrap align='center'><a href='#' onclick = 'addDuty(3,"+dutyType3+",\""+dutyTime3+"\");'>下班登记</a> </td>"
                   );
                }
            }
          }
        //第四次判断 
        if(dutyTime4 != ""){ 
          var tr = new Element('tr',{"class":"TableData"}); 
          $('tboday').appendChild(tr); 
          if(dutyJson.length>0){
            for(var i = 0; i< dutyJson.length; i++){
              var duty = dutyJson[i];
              registerTime = duty.registerTime;
              registerType = duty.registerType;
              seqId = duty.seqId;
              userId = duty.userId;
              registerIp = duty.registerIp;
              remark = duty.remark;
            
              if(registerType =="4"){break;}
             }
              if(registerType == "4"){
                  if(dutyType4 == "1"){
                    var c = "";
                    if((c_zStatus4 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                      c = " <span class=big4>&nbsp;迟到</span>";
                    }
                    tr.update("<td nowrap align='center'>第4次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime4 +"</td>"
                        + "  <td nowrap align='center'>" + registerTime.substr(11,19)+c + "</td>"
                        + " <td nowrap align='center'>"
                        + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                        );
                 
                    }else{
                      var z = "";
                      if((c_zStatus4== "2")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                        z =  "<span class=big4>&nbsp;早退</span>";
                      }
                      if(dutyStatus4 == "1"){  
                            tr.update("<td nowrap align='center'>第4登记</td>"
                              + " <td nowrap align='center'>下班登记</td>"
                              + " <td nowrap align='center'>" + dutyTime3 +"</td>"
                              + "  <td nowrap align='center'>" + registerTime.substr(11,19) +z+ "</td>"
                              + " <td nowrap align='center'>"
                              + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                              );
                       }else{
                              tr.update("<td nowrap align='center'>第4次登记</td>"
                                + " <td nowrap align='center'>下班登记</td>"
                                + " <td nowrap align='center'>" + dutyTime4+"</td>"
                                + "  <td nowrap align='center'>" + registerTime.substr(11,19) +z+ "</td>"
                                + " <td nowrap align='center'>"
                                + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a> "
                                + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                                );
                         }
                    }          
                }else{
                  if(dutyType4 == "1"){
                    tr.update("<td nowrap align='center'>第4次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime4 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(4,"+dutyType4+",\""+dutyTime4+"\");'>上班登记</a> </td>"
                        );         
                       }else{
                         tr.update("<td nowrap align='center'>第4次登记</td>"
                             + " <td nowrap align='center'>下班登记</td>"
                             + " <td nowrap align='center'>" + dutyTime4 +"</td>"
                             + " <td nowrap align='center'>未登记</td>"
                             + "<td nowrap align='center'><a href='#' onclick = 'addDuty(4,"+dutyType4+",\""+dutyTime4+"\");'>下班登记</a> </td>"
                        );
                     }
                }
             }else{
                  if(dutyType4 == "1"){
                    tr.update("<td nowrap align='center'>第4次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime4 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(4,"+dutyType4+",\""+dutyTime4+"\");'>上班登记</a> </td>"
                       );          
                   }else{
                     tr.update("<td nowrap align='center'>第4次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime4 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + " <td nowrap align='center'><a href='#' onclick = 'addDuty(4,"+dutyType4+",\""+dutyTime4+"\");'>下班登记</a> </td>"
                   );
                }
            }
          }
       //第五次判断


        if(dutyTime5 != ""){ 
          var tr = new Element('tr',{"class":"TableData"}); 
          $('tboday').appendChild(tr); 
          if(dutyJson.length>0){
            for(var i = 0; i< dutyJson.length; i++){
              var duty = dutyJson[i];
              registerTime = duty.registerTime;
              registerType = duty.registerType;
              seqId = duty.seqId;
              userId = duty.userId;
              registerIp = duty.registerIp;
              remark = duty.remark;
            
              if(registerType =="5"){break;}
             }
              if(registerType == "5"){
                  if(dutyType5 == "1"){
                    var c = "";
                    if((c_zStatus5 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                      c = " <span class=big4>&nbsp;迟到</span>";
                    }
                    tr.update("<td nowrap align='center'>第5次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                        + "  <td nowrap align='center'>" + registerTime.substr(11,19) +c+ "</td>"
                        + " <td nowrap align='center'>"
                        + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                        );
            
                    }else{
                      var z = "";
                      if((c_zStatus5== "2")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                        z =  "<span class=big4>&nbsp;早退</span>";
                      }
                      if(dutyStatus5 == "1"){  
                            tr.update("<td nowrap align='center'>第5登记</td>"
                              + " <td nowrap align='center'>下班登记</td>"
                              + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                              + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                              + " <td nowrap align='center'>"
                              + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                              );
                       }else{
                              tr.update("<td nowrap align='center'>第5次登记</td>"
                                + " <td nowrap align='center'>下班登记</td>"
                                + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                                + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                                + " <td nowrap align='center'>"
                                + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a> "
                                + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                                );
                            }
                    }          
                }else{
                  if(dutyType5 == "1"){
                    tr.update("<td nowrap align='center'>第5次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(5,"+dutyType5+",\""+dutyTime5+"\");'>上班登记</a> </td>"
                        );         
                       }else{
                         tr.update("<td nowrap align='center'>第5次登记</td>"
                             + " <td nowrap align='center'>下班登记</td>"
                             + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                             + " <td nowrap align='center'>未登记</td>"
                             + "<td nowrap align='center'><a href='#' onclick = 'addDuty(5,"+dutyType5+",\""+dutyTime5+"\");'>下班登记</a> </td>"
                        );
                     }
                }
             }else{
                  if(dutyType5 == "1"){
                    tr.update("<td nowrap align='center'>第5次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(5,"+dutyType5+",\""+dutyTime5+"\");'>上班登记</a> </td>"
                       );        
                   }else{
                     tr.update("<td nowrap align='center'>第5次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime5 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + " <td nowrap align='center'><a href='#' onclick = 'addDuty(5,"+dutyType5+",\""+dutyTime5+"\");'>下班登记</a> </td>"
                   );
                }
            }
          }
   //第六次判断

        if(dutyTime6 != ""){ 
          var tr = new Element('tr',{"class":"TableData"}); 
          $('tboday').appendChild(tr); 
          if(dutyJson.length>0){
            for(var i = 0; i< dutyJson.length; i++){
              var duty = dutyJson[i];
              registerTime = duty.registerTime;
              registerType = duty.registerType;
              seqId = duty.seqId;
              userId = duty.userId;
              registerIp = duty.registerIp;
              remark = duty.remark;
            
              if(registerType =="6"){break;}
             }
              if(registerType == "6"){
                  if(dutyType6 == "1"){
                    var c = "";
                    if((c_zStatus6 == "1")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                      c = " <span class=big4>&nbsp;迟到</span>";
                    }
                    tr.update("<td nowrap align='center'>第6次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                        + "  <td nowrap align='center'>" + registerTime.substr(11,19) +c+ "</td>"
                        + " <td nowrap align='center'>"
                        + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                        );
            
                    }else{
                      var z = "";
                      if((c_zStatus6== "2")&&(holiday!=0)&&!generalIs(general)&&!evection()&&!IsLeave(registerTime)){
                        z =  "<span class=big4>&nbsp;早退</span>";
                      }
                      if(dutyStatus6 == "1"){  
                            tr.update("<td nowrap align='center'>第6登记</td>"
                              + " <td nowrap align='center'>下班登记</td>"
                              + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                              + "  <td nowrap align='center'>" + registerTime.substr(11,19) +z+ "</td>"
                              + " <td nowrap align='center'>"
                              + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a></td>"
                              );
                       }else{
                              tr.update("<td nowrap align='center'>第6次登记</td>"
                                + " <td nowrap align='center'>下班登记</td>"
                                + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                                + "  <td nowrap align='center'>" + registerTime.substr(11,19) + z+"</td>"
                                + " <td nowrap align='center'>"
                                + "已考勤 <a href='#' onclick ='remark(" + seqId + ");'>&nbsp;&nbsp;说明情况</a> "
                                + "<a href='#' onclick = 'updateDuty("+ seqId +"," + registerType + ")'>&nbsp;&nbsp;重新进行下班登记</a></td>"
                                );
                         }
                    }          
                }else{
                  if(dutyType6 == "1"){
                    tr.update("<td nowrap align='center'>第6次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(6,"+dutyType6+",\""+dutyTime6+"\");'>上班登记</a> </td>"
                        );          
                       }else{
                         tr.update("<td nowrap align='center'>第6次登记</td>"
                             + " <td nowrap align='center'>下班登记</td>"
                             + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                             + " <td nowrap align='center'>未登记</td>"
                             + "<td nowrap align='center'><a href='#' onclick = 'addDuty(6,"+dutyType6+",\""+dutyTime6+"\");'>下班登记</a> </td>"
                        );
                     }
                }
             }else{
                  if(dutyType6 == "1"){
                    tr.update("<td nowrap align='center'>第6次登记</td>"
                        + " <td nowrap align='center'>上班登记</td>"
                        + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                        + " <td nowrap align='center'>未登记</td>"
                        + " <td nowrap align='center'><a href='#' onclick = 'addDuty(6,"+dutyType6+",\""+dutyTime6+"\");'>上班登记</a> </td>"
                       );         
                   }else{
                     tr.update("<td nowrap align='center'>第6次登记</td>"
                         + " <td nowrap align='center'>下班登记</td>"
                         + " <td nowrap align='center'>" + dutyTime6 +"</td>"
                         + " <td nowrap align='center'>未登记</td>"
                         + " <td nowrap align='center'><a href='#' onclick = 'addDuty(6,"+dutyType6+",\""+dutyTime6+"\");'>下班登记</a> </td>"
                   );
                }
            }
        }
     }
  
  }
}
function addDuty(registerType,dutyType,dutyTime){
  var seqId = $("seqId").value;
  // var timestr = new Date().toLocaleString()
   //var curtime = timestr.substr(10,9);
   //var curTimeInt = strInt(curtime);
 // var dutyTimeInt = strInt(dutyTime);
  //var prc = getDutyInterval();
 // var before1 = prc.before1;
 // var after1 = prc.after1;
 // var before2 = prc.before2;
  //var after2 = prc.after2;
  //var dutyIntervalBefore1 = prc.dutyIntervalBefore1;
 // var dutyIntervalAfter1 = prc.dutyIntervalAfter1;
  //var dutyIntervalBefore2 = prc.dutyIntervalBefore2;
 // var dutyIntervalAfter2 = prc.dutyIntervalAfter2;
  //if(dutyType==1){
   // if((dutyTimeInt-curTimeInt)>=before1||(curTimeInt-dutyTimeInt)>=after1){
    //  var alertStr = "警告: \n\n规定时间之前 "+dutyIntervalBefore1+"分钟，之后 "+dutyIntervalAfter1+" 分钟起可进行上班登记!";
    //  alert(alertStr);
    //  return;
  //  }
//  }else{
   // if((dutyTimeInt-curTimeInt)>=before2||(curTimeInt-dutyTimeInt)>=after2){
    //  var alertStr = "警告: \n\n规定时间之前 "+dutyIntervalBefore2+"分钟，之后 "+dutyIntervalAfter2+" 分钟起可进行下班登记!";
    //  alert(alertStr);
     // return;
   // }
 // }
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/addDuty.act?registerType=" + registerType+"&dutyType=" + dutyType  + "&configId="+seqId ;
  window.location = requestURL;
}
//得到四个时间段

function getDutyInterval(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getDutyInterval.act"; 
  var rtjson = getJsonRs(requestURL); 
  //alert(rsText);
  if(rtjson.rtState == '1'){ 
    alert(rtjson.rtMsrg); 
    return ; 
  }
  var prc = rtjson.rtData;
  return prc;
}
function strInt(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;

  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2+parseInt(strArray[2]);
  return strInt;
}
function TimeSet(){
  var timestr = new Date().toLocaleString();
  document.getElementById("timetable").innerHTML = timestr;
}
function Time(){
  TimeSet();
  setInterval(TimeSet,1000);
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload()">
<!----  上下班登记 ---->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;今日上下班登记 (<span id="dutyName"></span>    	 当前时间: &nbsp;<span id="timetable"></span>)</span><br>
    </td>
  </tr>
</table>
<div id = "IntervalTable"></div>
<div id = "dutyTable"></div>
<input type="hidden" id="seqId" name="seqId" value=""></input>
</body>
</html>