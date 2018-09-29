<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String dept=request.getParameter("deptId");
  String dutyType=request.getParameter("dutyType");
  String startDate=request.getParameter("startDate");
  String endDate=request.getParameter("endDate");

%>
<html>
<head>
<title>考勤情况查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
</head>

<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/attendance/manage/query/js/util.js"></script>
<script type="text/javascript">
		 var dept='<%=dept%>';
		 var dutyType='<%=dutyType%>';
		 var startDate='<%=startDate%>';
		 var endDate='<%=endDate%>';
		 
  function doInit(){
    getDuty();
	  getEvection();
	  getLeave();
	  getOut();
	  getOvertime();
	  
	  }
function createTd(str) {
  var td = document.createElement("td");
  td.align = 'center';
  td.innerHTML = str;
  return td;
}
  function getDuty(){
	  var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
	  var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getDeptDuty.act";
	        var rtJson = getJsonRs(url,param);
	        if(rtJson.rtState == "0"){
	          var data=rtJson.rtData;
	          for(var i=0;i<data.length;i++){
              var tr = new Element('tr', { "class" : "TableData" });
              $('tbody').appendChild(tr);
              tr.appendChild(createTd(data[i].deptName));
              tr.appendChild(createTd(data[i].userName));
              tr.appendChild(createTd(data[i].perfectCount));
              tr.appendChild(createTd(data[i].allHoursMinites));
              tr.appendChild(createTd(data[i].lateCount));
              tr.appendChild(createTd(data[i].on));
              tr.appendChild(createTd(data[i].earlyCount));
              tr.appendChild(createTd(data[i].off));
              tr.appendChild(createTd(opts1(data[i].userId,data[i].date1,data[i].date2)));
	        }
	        }else{
	          alert(rtJson.rtMsrg); 
	        }
	  }
 
  function getEvection(){
	    var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
		    var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getEvection.act";
	          var rtJson = getJsonRs(url,param);
	          if(rtJson.rtState == "0"){
	            var data=rtJson.rtData.data;
	            var priv=rtJson.rtData.userPriv;
	              for(var i=0;i<data.length;i++){
	                  var tr = new Element('tr', { "class" : "TableData" });
	                  $('evection').appendChild(tr);
                     
	                    if(data[i].status=="1"){
                         data[i].status="在外";
                        }else{
                        	 data[i].status="归来";
                            }
	                    tr.appendChild(createTd(data[i].deptName));
	                    tr.appendChild(createTd(data[i].userName));
	                    tr.appendChild(createTd(data[i].evectionDest));
	                    tr.appendChild(createTd(data[i].registerIp));
	                    tr.appendChild(createTd(data[i].evectionDate1));
	                    tr.appendChild(createTd(data[i].evectionDate2));
	                    tr.appendChild(createTd(data[i].leaderId ));
	                    tr.appendChild(createTd( data[i].status));
	                    tr.appendChild(createTd(opts(priv,data[i].seqId)));
	             }
              if(data.length<= 0){ 
                  $("evectionData").style.display='none';
                  $("noEvection").style.display='block';
               }
		             
	          }else{
	            alert(rtJson.rtMsrg); 
	          }
	    }
        function opts1(userId , date1 , date2) {
          return "<a href='javascript:void(0)' onclick='openDetail("+userId+",\""+date1+"\",\""+date2+"\")'>详细信息</a>" + "&nbsp;<a href='javascript:void(0)' onclick='Exptest("+userId+")'>导出个人明细</a>";
        }
		function opts(priv,seqId){
		   if(priv!="1"){
          return "";
			   }
		   return "<a href=\"javascript:delEvection('"+seqId+"')\">删除</a>";
		}

		function delEvection(seqId){
			 if(window.confirm("确认是否删除该出差记录！")){
			       var param="seqId="+seqId;
			       var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/delEvection.act"
			       var rtJson = getJsonRs(url,param);
			         if(rtJson.rtState == "0"){
			        	 window.location.reload();
			         }
			       }
			}
  function evectionExp(){
	  var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
    var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getExeclEvection.act?";
    window.location.href=url+param;
	  }
  function leaveExp(){
	    var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
	    var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getExeclLeave.act?";
	    window.location.href=url+param;
	    }

  function getLeave(){
      var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
        var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getLeave.act";
            var rtJson = getJsonRs(url,param);
            if(rtJson.rtState == "0"){
              var data=rtJson.rtData.data;
              var priv=rtJson.rtData.userPriv;
                for(var i=0;i<data.length;i++){
                    var tr = new Element('tr', { "class" : "TableData" });
                    $('leave').appendChild(tr);
                     
                    if(data[i].status==1)
                      data[i].status="现行";
                     else
                        data[i].status="已销假";

                    tr.appendChild(createTd(data[i].deptName));
                    tr.appendChild(createTd(data[i].userName));
                    tr.appendChild(createTd(data[i].leaveType));
                    tr.appendChild(createTd(data[i].annualLeave));
                    tr.appendChild(createTd(data[i].registerIp));
                    tr.appendChild(createTd(data[i].leaveDate1));
                    tr.appendChild(createTd(data[i].leaveDate2  ));
                    tr.appendChild(createTd( data[i].leaderId));
                    tr.appendChild(createTd( data[i].status));
                    tr.appendChild(createTd(optsLeave(priv,data[i].seqId)));
               }
              if(data.length<= 0){ 
                  $("leave").style.display='none';
                  $("noLeave").style.display='block';
               }
                 
            }else{
              alert(rtJson.rtMsrg); 
            }
      }
 
function openDetail(userId , date1 , date2) {
  window.open(contextPath + "/subsys/oa/hr/salary/report/detail/selectduty.jsp?userId="+userId+"&startTime="+encodeURIComponent(date1)+"&endTime="+encodeURIComponent(date2));
}
function optsLeave(priv,seqId){
     if(priv!="1"){
   return "";
       }
     return "<a href=\"javascript:delLeave('"+seqId+"')\">删除</a>";
  }

  function delLeave(seqId){
     if(window.confirm("确认是否删除该请假记录！")){
           var param="seqId="+seqId;
           var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/delLeave.act"
           var rtJson = getJsonRs(url,param);
             if(rtJson.rtState == "0"){
               window.location.reload();
             }
           }
    }



  function outExp(){
      var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
      var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getExeclOut.act?";
      window.location.href=url+param;
      }

  function getOut(){
      var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
        var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getOut.act";
            var rtJson = getJsonRs(url,param);
            if(rtJson.rtState == "0"){
              var data=rtJson.rtData.data;
              var priv=rtJson.rtData.userPriv;
                for(var i=0;i<data.length;i++){
                    var tr = new Element('tr', { "class" : "TableData" });
                    $('out').appendChild(tr);
                     var statusDesc="";
                    if(data[i].status=="0")
                    	statusDesc="外出";
                     else if(data[i].status=="1")
                    	 statusDesc="已归来";
                     if(data[i].allow=='0')
                    	 statusDesc="待批";
                     else if(data[i].allow=='2')
                    	 statusDesc="不批准";
                     tr.appendChild(createTd(data[i].deptName));
                     tr.appendChild(createTd(data[i].userName));
                     tr.appendChild(createTd(data[i].createDate));
                     tr.appendChild(createTd(data[i].outType));
                     tr.appendChild(createTd(data[i].registerIp));
                     tr.appendChild(createTd(data[i].submitTime));
                     tr.appendChild(createTd( data[i].outTime1 ));
                     tr.appendChild(createTd( data[i].outTime2 ));
                     tr.appendChild(createTd( data[i].leaderId));
                     tr.appendChild(createTd( statusDesc ));
                     tr.appendChild(createTd(optsOut(priv,data[i].seqId)));
               }
              if(data.length<= 0){ 
                  $("out").style.display='none';
                  $("noOut").style.display='block';
               }
                 
            }else{
              alert(rtJson.rtMsrg); 
            }
      }
 

function optsOut(priv,seqId){
     if(priv!="1"){
   return "";
       }
     return "<a href=\"javascript:delOut('"+seqId+"')\">删除</a>";
  }

  function delOut(seqId){
     if(window.confirm("确认是否删除该外出记录！")){
           var param="seqId="+seqId;
           var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/delOut.act"
           var rtJson = getJsonRs(url,param);
             if(rtJson.rtState == "0"){
               window.location.reload();
             }
           }
    }



  function overtimeExp(){
      var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
      var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getExeclOvertime.act?";
      window.location.href=url+param;
      }

  function getOvertime(){
      var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
        var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getOvertime.act";
            var rtJson = getJsonRs(url,param);
            if(rtJson.rtState == "0"){
              var data=rtJson.rtData.data;
              var priv=rtJson.rtData.userPriv;
                for(var i=0;i<data.length;i++){
                    var tr = new Element('tr', { "class" : "TableData" });

                    $('overtime').appendChild(tr);
                    if(data[i].status=="0")
                    	data[i].status="未确认";
                     else if(data[i].status=="1")
                    	 data[i].status="已确认";

                    tr.appendChild(createTd(data[i].deptName));
                    tr.appendChild(createTd(data[i].userName));
                    tr.appendChild(createTd(data[i].overtimeTime));
                    tr.appendChild(createTd(data[i].overtimeDesc));
                    tr.appendChild(createTd(data[i].beginTime));
                    tr.appendChild(createTd(data[i].endTime));
                    tr.appendChild(createTd( data[i].hour ));
                    tr.appendChild(createTd( data[i].leaderId ));
                    tr.appendChild(createTd( data[i].status));
                    tr.appendChild(createTd(optsOvertime(priv,data[i].seqId)));
               }
              if(data.length<= 0){ 
                  $("overtime").style.display='none';
                  $("noOvertime").style.display='block';
               }
                 
            }else{
              alert(rtJson.rtMsrg); 
            }
      }
 

function optsOvertime(priv,seqId){
     if(priv!="1"){
   return "";
       }
     return "<a href=\"javascript:delOvertime('"+seqId+"')\">删除</a>";
  }

  function delOvertime(seqId){
     if(window.confirm("确认是否删除该加班记录！")){
           var param="seqId="+seqId;
           var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/delOvertime.act"
           var rtJson = getJsonRs(url,param);
             if(rtJson.rtState == "0"){
               window.location.reload();
             }
           }
    }

  function test(userId){
      var url= contextPath + "/core/funcs/attendance/manage/query/selectduty.jsp?userId="+userId+"&startTime=<%=startDate%>&endTime=<%=endDate%>";
      window.open(url);
      }
  function Exptest(userId){
      var url= contextPath + "/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getExeclDutyInfo.act?userId="+userId+"&startTime=<%=startDate%>&endTime=<%=endDate%>";
        window.location.href=url;
      }
  function Exp(){
    var param="dept=<%=dept%>&dutyType=<%=dutyType%>&startDate=<%=startDate%>&endDate=<%=endDate%>";
    var url= contextPath + "/yh/core/funcs/attendance/manage/act/YHManageQueryAct/expDeptDuty.act?" + param;
      window.location.href=url;
    }
</script>

<body onLoad="doInit();" class="bodycolor" topmargin="5">




<!------------------------------------- 上下班 ------------------------------->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 上下班统计
    （从 <%=startDate%> 至 <%=endDate%> 天）
    </span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出Excel" class="BigButton" onClick="Exp()" title="导出下班统计信息" name="button">
    </td>
  </tr>
</table>

<table class="TableList" width="95%">
  <tr class="TableHeader">
    <td nowrap align="center">部门</td>
    <td nowrap align="center">姓名</td>
    <td nowrap align="center">全勤(天)</td>
    <td nowrap align="center">时长</td>
    <td nowrap align="center">迟到</td>
    <td nowrap align="center">上班未登记</td>
    <td nowrap align="center">早退</td>
    <td nowrap align="center">下班未登记</td>
    <!--  <td nowrap align="center">加班上班登记</td> -->
    <!--  <td nowrap align="center">加班下班登记</td> -->
    
    <td nowrap align="center">操作</td>
  </tr>
   <tbody id="tbody"></tbody>
</table>

<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<!------------------------------------- 外出记录 ------------------------------->

<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 外出记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onClick="outExp();" title="导出外出记录" name="button">
    </td>
  </tr>
</table>


    <table class="TableList" id="out" width="95%">

    <thead class="TableHeader">
      <td nowrap align="center">部门</td>
      <td nowrap align="center">姓名</td>
      <td nowrap align="center">申请时间</td>
      <td nowrap align="center">外出原因</td>
      <td nowrap align="center">登记IP</td>
      <td nowrap align="center">外出日期</td>
      <td nowrap align="center">外出时间</td>
      <td nowrap align="center">归来时间</td>
      <td nowrap align="center">审批人员</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
    </thead>
 </table>

<div align="center" style="display:none" id="noOut">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无外出记录！</div></td>
  </tr>
  </table>   
</div>

<!------------------------------------- 请假记录 ------------------------------->

<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 请假记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onClick="leaveExp()" title="导出请假记录" name="button">
    </td>
  </tr>
</table>


    <table class="TableList" id="leave"  width="95%">

    <thead class="TableHeader">
      <td nowrap align="center">部门</td>
      <td nowrap align="center">姓名</td>
      <td nowrap align="center">请假原因</td>
      <td nowrap align="center">占年休假</td>
      <td nowrap align="center">登记IP</td>
      <td nowrap align="center">开始日期</td>
      <td nowrap align="center">结束日期</td>
      <td nowrap align="center">审批人员</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
    </thead>
  
 </table>

<div align="center" style="display:none" id="noLeave">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无请假记录！</div></td>
  </tr>
  </table>   
</div>


<!------------------------------------- 出差记录 ------------------------------->

<br><br>
<table border="0" width="100%" cellspacing="0"  cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 出差记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onClick="evectionExp();" title="导出出差记录" name="button">
    </td>
  </tr>
</table>


    <table class="TableList" id="evectionData" width="95%">

    <tbody class="TableHeader" id="evection">
      <td nowrap align="center">部门</td>
      <td nowrap align="center">姓名</td>
      <td nowrap align="center">出差地点</td>
      <td nowrap align="center">登记IP</td>
      <td nowrap align="center">开始日期</td>
      <td nowrap align="center">结束日期</td>
      <td nowrap align="center">审批人员</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
    </tbody>
 </table>



<div align="center" style="display:none" id="noEvection">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无出差记录！</div></td>
  </tr>
  </table>   
</div>

<!------------------------------------- 加班记录 ------------------------------->

<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 加班记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onClick="overtimeExp();" title="导出加班记录" name="button">
    </td>
  </tr>
</table>


    <table class="TableList" id="overtime" width="95%">

    <thead class="TableHeader">
      <td nowrap align="center">部门</td>
      <td nowrap align="center">姓名</td>
      <td nowrap align="center">申请时间</td>
      <td nowrap align="center">加班内容</td>
      <td nowrap align="center">开始日期</td>
      <td nowrap align="center">结束时间</td>
      <td nowrap align="center">时长</td>
      <td nowrap align="center">审批人员</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
    </thead>
    </table>
    
<div align="center" style="display:none" id="noOvertime">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无加班记录！</div></td>
  </tr>
  </table>   
</div>



<div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="window.history.go(-1);">
</div>

</body>
</html>