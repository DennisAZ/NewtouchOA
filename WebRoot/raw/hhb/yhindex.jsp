<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <!--  
 <script type="text/javascript" src="js/index1.js"></script> 
 -->
 <script type="text/javascript" src="js/index2.js"></script>
<script type="text/javascript"><!--
//解析数据方法
//模拟添加 var data = [{sunId:'dd' , runName :'ddd' , beginTime: 'ddd' , endTime :'ddd'},{sunId:'dddd' , runName :'ddd' , beginTime: 'ddd' , endTime :'ddd'},{sunId:'dd' , runName :'ddd' , beginTime: 'ddd' , endTime :'ddd'}];
function addRow(tmp,i){
  
  var td = "<td nowrap align=center><input type=checkbox name=run_select></td>"
            + "<td nowrap align=center class=TableContent>" + tmp.sun_id +"</td>"
            + "<td class=auto align=center>" + tmp.runName +"</td>"
            + "<td align=center>" + tmp.begin_time +"</td>";
      //判断公众附件是否为空      
      if(tmp.attachmentName != 'null' ){
        td += "<td class=auto align=center>" + tmp.attachmentName +"</td>";
      }else{
        td += "<td class=auto align=center>无  </td>";
      }
      //判断状态是否为 ALL,0,1
      if(tmp.flow_status == "ALL"){
           if(tmp.end_time == "null"){
            td += "<td align=center nowrap><font color=red>执行中</font></td>";
            }
           else{ 
            td += "<td align=center nowrap>已结束</td>";
             }
      }else if(tmp.flow_status == "0"){
        td += "<td align=center nowrap><font color=red>执行中</font></td>";  
          
      } else {
        td += "<td align=center nowrap>已结束</td>";
      }
        td+= "<td nowrap><a class=op><font color=blue>流程图&nbsp;</a><a class=op><font color=blue>&nbsp;&nbsp"+ tmp.state +"</a><a class=op><font color=blue>&nbsp;&nbsp;&nbsp;更多&nbsp;</a></td>";

        //   <a class="op" title="添加点评意见" href="javascript:comment(<?=$RUN_ID?>,<?=$FLOW_ID?>)">点评&nbsp;</a>
       // td+= "</td>";
          
          
  	var className = "TableLine2" ;    
    if(i%2 == 0){
      className = "TableLine1" ;
    }
    var tr = new Element("tr" , {"class" : className});
    $('dataBody').appendChild(tr);  
    tr.update(td);
}
//开始查询函数
function checkForm(){ 
 var queryParam = $("form1").serialize();
 var rtJson =  getJsonRs("<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowTypesAct/getFlowTypeJson.act", queryParam);
 /*alert(rtJson.rtData.listData.length);
 for(var i=0; i<rtJson.rtData.listData.length; i++){
   alert(rtJson.rtData.listData[i].sun_id+"---"+rtJson.rtData.listData[i].runName +"-----"+rtJson.rtData.listData[i].begin_time );
 } */
 alert(rsText);
 //var json = getJsonRs(rtJson);
 if(rtJson.rtState == "0"){  
   var rtData = rtJson.rtData;
   alert(rsText);
   var listData = rtData.listData;
   
   if(listData.length > 0){
     for(var i = 0 ;i < listData.length ;i ++){
       var data = listData[i];
       addRow(data, i);  //解析数据方法
     }
   }  
 }
 //alert(rtJson); 
 //alert(rsText); 
 /* if (rtJson.state == "0") {
  alert(rtJson.rtData.name);
 }else {
    alert(rtJson.rtMsrg);
  }*/

  
 /* for (var i = 0 ;i <　data.length;i++){
    addRow(data[i] , i);
  }*/
}



function loadFlowType(){
 // alert("333");
  var url = contextPath+'/yh/core/funcs/workflow/act/YHFlowTypeAct/getFlowTypeJson.act';
  var json = getJsonRs(url);
  var rtData = json.rtData;   
  for(var i=0;i<rtData.length;i++) {      
    var opt=document.createElement("option");      
	  opt.value=rtData[i].seqId;      
	  opt.innerHTML = rtData[i].flowName;      
	  $('flowList').appendChild(opt);                        
  }    
} 
function empty_date()
{
	 document.form1.statrTime.value="";
	 document.form1.endTime.value="";
}

function doInit(){
  loadFlowType();
  //doInit2();
  var beginParameters = {
       inputId:'statrTime',
       property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
   var url = "<%=contextPath%>/yh/core/funcs/system/diary/act/YHDiaryAct/getDiary.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     var str = document.getElementById("paraValue").value.split(',');
     for(var i = 0; i < str.length; i++){
        var paraValue = str[0];
        var paraValue1 = str[1];
        var paraValue2 = str[2];
     }
     document.getElementById("endTime").value = paraValue1;
    // document.getElementById("days").value = paraValue2;
     if (paraValue=="") {
       document.getElementById("statrTime").value="";
     } else {
       document.getElementById("statrTime").value = "";
     }
     if (document.getElementById("endTime").value=="") {
       document.getElementById("endTime").value="";
     } else {
       document.getElementById("endTime").value = "";
     }
    /* if (document.getElementById("days").value=="") {
       document.getElementById("days").value="";
     } else {
       document.getElementById("days").value = paraValue2;
     } */
   }else {
     alert(rtJson.rtMsrg); 
   }
}

function isDate(dataValue){
  var year = "";
  var month = "";
  var day = "";
  var offset = 0;
  var len = dataValue.length;
  var i = dataValue.indexOf("-");
  alert(i);
  year = dataValue.substr(offset,(i-offset));
  offset = i + 1;
  if(offset > len){
    return false;
  }
  if(i){
    i = dataValue.indexOf("-",offset);
    
    day = dataValue.substr(sum,(len-offset));
  }
  if(year==""||month==""||day==""){
    return false;
  }
  return true;
}

function check(){
  if(document.getElementById("statrTime").value!=""){
    var da = isDate(document.getElementById("statrTime").value);
    if(!da){
      alert("错误 开始时间格式不对，应形如 1999-1-2");
      }
  }
}

function commit(){
  if(document.getElementById("statrTime").value > document.getElementById("endTime").value){
    alert("错误 开始时间不能小于结束时间！");
    return false;
  }
  if (document.getElementById("days").value != "" && (document.getElementById("days").value < 0 || document.getElementById("days").value != parseInt(document.getElementById("days").value))){
    alert("锁定日志天数应为正整数！");
    document.getElementById("days").focus();
    return false;
  }
  var url = null;
  var rtJson = null;
  var paraName = document.getElementById("paraName").value;
  var seqId = document.getElementById("seqId").value;
  var statrTime = document.getElementById("statrTime").value;
  var endTime = document.getElementById("endTime").value;
  var days = document.getElementById("days").value;
  url = "<%=contextPath%>/yh/core/funcs/system/diary/act/YHDiaryAct/";
  if (paraName=="LOCK_TIME") {
    url += "updateDiary.act?statrTime="+statrTime+"&endTime="+endTime+"&days="+days+"&seqId="+seqId;
  }else{
    url += "addDiary.act?statrTime="+statrTime+"&endTime="+endTime+"&days="+days+"&seqId="+seqId;
  }
  rtJson = getJsonRs(url, mergeQueryString($("form1")));
  //alert(rtJson.rtMsrg);
  location = "<%=contextPath %>/core/funcs/system/diary/insert.jsp";
}
--></script>
</head>
<body onload="doInit()">
<form  name="form1" id="form1">
<table id="flow_table" border="0" width="100%" class="TableList"  style="clear:both;table-layout:fixed;" >
<input type="hidden" name="seqId" id="seqId" value="111">
<input type="hidden" name="paraName" id="paraName" value="222">
<input type="hidden" name="paraValue" id="paraValue" value="33">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.diary.data.YHDiary.java"/>
  <tr class=TableTr>
  <td nowrap align="left" width=140> 流程
   <select name="flowList" id="flowList" style="width:100px">
   <option value="0">所有流程类型</option>
   </select>
   </td> 
      <td align="center" width=120>状态<select name="FLOW_STATUS" id="FLOW_STATUS">
      <option value="ALL">所有状态</option>
      <option value="0">正在执行</option>
      <option value="1">已经结束</option>
    </select>
     </td>
      <td>
       范围<select name="FLOW_QUERY_TYPE" id="FLOW_QUERY_TYPE">
      <option value="ALL">所有范围</option>
      <option value="1">我发起的</option>
      <option value="2" selected>我经办的</option>
      <option value="3" >我管理的</option>
      <option value="4" >我关注的</option>
      <option value="5" >指定发起人</option>
    </select>
      </td> 
    </tr>
   <tr>
   <td class="" colspan=3>从
      <input type="text" id="statrTime" name="statrTime" size="10" maxlength="19" class="BigInput" value="444">
      <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
            到
    <input type="text" id="endTime" name="endTime" size="10" maxlength="19" class="BigInput">
    <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
    <a href="javascript:empty_date()">清空</a>
           流水号<input type="text" id="RUN_ID" name="RUN_ID" size="6" class="SmallInput" value="777">&nbsp;
          名称/文号<input type="text" id="RUN_NAME" name="RUN_NAME" size="20" class="SmallInput" value="666">
    </td> 
    <td align="center">
    <input id="queryBtn" type="button" onclick="checkForm();" value="开始查询">
   </td> 
   </tr>
   <!--   <input type="hidden" name="dddd" value=""/>  --> 
 </table>
 </form>
 <table id="flow_table" width="100%" class="TableList" align="center" style="border-bottom:0px;table-layout:fixed;">
   
    <tbody>
      <tr class=TableHeader>
      <td nowrap align="center" width=40>选择</td>
      <td nowrap align="center" width=60><a href="#">流水号</a></td>
      <td nowrap align="center"><a href="#">工作名称/文号</a></td>
      <td nowrap align="center" width=140><a href="#">开始时间</a></td>
      <td nowrap align="center" width=200><a href="#">公共附件</a></td>
      <td nowrap align="center" width=60><a href="#">状态</a></td>
      <td nowrap align="center" width=170>操作</td>
       </tr>
    </tbody>
    <tbody id="dataBody"></tbody>
    <% //控制表格相邻行之间的背景色不同
   String  className = "";
    className = "TableLine2" ;
    %>
    </table>
 <!--  
 <form action="<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowTypesAct/getFlowTypeJson.act" name="form2" id="form2">
 	<input type="text" name="myname" value="1111111111"/>
 	<input type="submit" value="click"/>
 </form>
 <script type="text/javascript">
 	  function clickme(){
 	   var queryParam = $("form2").serialize();
 	  queryParam = mergeQueryString(queryParam);// 这个方法可以传无限参数  //在后台类接受相对应的参数
 	  alert(queryParam);
 	  }
 </script>
 <input type="button" value="click" onclick="clickme();"/>
 -->
</body>
</html>