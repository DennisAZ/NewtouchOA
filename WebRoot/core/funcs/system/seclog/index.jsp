<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 

String  startDate=request.getParameter("startDate");
if(startDate==null){
  startDate="";
}
String endDate=request.getParameter("endDate");
if(endDate==null){
  endDate="";
}
String optDesc=request.getParameter("optDesc");
if(optDesc==null){
  optDesc="";
}
String userId=request.getParameter("userId");
if(userId==null){
  userId="";
}
String opType=request.getParameter("opType");
if(opType==null){
  opType="";
}

%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>安全日志</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/seclog/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/seclog/js/seclog.js"></script>
<script>  
var pageMgr = null;




function doInit(){
	 var userId="<%=userId%>";
	  var opType="<%=opType%>";
	  var startDate="<%=startDate%>";
	  var endDate="<%=endDate%>";
	  var optDesc="<%=optDesc%>";
	  
	  	  
	  var param="userId="+userId+"&opType="+opType+"&startDate="+startDate+"&endDate="+endDate+"&optDesc="+optDesc;

  var url = "<%=contextPath%>/yh/core/funcs/seclog/act/YHSeclogAct/getLogListJson.act?"+param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"userSeqId",  width: '8%', text:"操作人员" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"opTime",  width: '12%', text:"操作时间" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"clientIp",  width: '10%', text:"操作主机" ,align: 'center' },
       {type:"data", name:"opType",  width: '8%', text:"操作类型" ,align: 'center' ,render:secLogFunc},
       {type:"data", name:"opObject",  width: '10%', text:"被操作主体" ,align: 'center' },
       {type:"data", name:"opResult",  width: '5%', text:"操作结果" ,align: 'center',render:optResult},
       {type:"data", name:"opDesc",  width: '20%', text:"描述" ,align: 'center' }
       
   ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
   // showCntrl('delOpt');
  }else{
    WarningMsrg('无相关日志', 'msrg');
  }
  
  
  setDate();

  getSecretFlag("seclog","opType");
  
  //设置 初始值
  var userName=userNameFunc(userId);
  var bindData="{userId:'"+userId+"',userName:'"+userName+"',startDate:'"+startDate+"',endDate:'"+endDate+"',optDesc:'"+optDesc+"',opType:'"+opType+"'}";
 // bindJson2Cntrl(bindData); 
  $("userId").value=userId;
  $("userName").value=userName;
  $("startDate").value=startDate;
  $("endDate").value=endDate;
  setSelectData(optDesc,"optDesc");
  setSelectData(opType,"opType");
  
  
}

function setSelectData(data,cntrl){
	if(data==""){
		return;
	}
	var olen= $(cntrl).options.length;

   for(var i=0;i<olen;i++){
	  var sValue= $(cntrl).options[i].value;
	
	  if(data==sValue){
		  $(cntrl).options[i].selected=true;
		  break;
	  } 
	  
   }
   
}

function setDate(){
	  var date1Parameters = {
	    inputId:'startDate',
	    property:{isHaveTime:false}
	    ,bindToBtn:'date1'
	  };
	  new Calendar(date1Parameters);

	  var date2Parameters = {
	    inputId:'endDate',
	    property:{isHaveTime:false}
	    ,bindToBtn:'date2'
	  };
	  new Calendar(date2Parameters);
	}


function doSearch(){
  var startDate = $("startDate").value;
  if(startDate){
    if(!isValidDateStr(startDate)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("startDate").focus();
      $("startDate").select();
      return false;
    }
  }
  
  var endDate = $("endDate").value;
  if(endDate){
    if(!isValidDateStr(endDate)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }
  
  if(startDate > endDate){
    alert("开始日期不能大于结束日期！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
	var userId=$("userId").value;
	var opType=$("opType").value;
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var optDesc=$("optDesc").value;
	var param="userId="+userId+"&opType="+opType+"&startDate="+startDate+"&endDate="+endDate+"&optDesc="+optDesc;
	
	location.href="index.jsp?"+param;
	
}

function doExp(){
	  var userId=$("userId").value;
	  var opType=$("opType").value;
	  var startDate=$("startDate").value;
	  var endDate=$("endDate").value;
	  var optDesc=$("optDesc").value;
	  var param="userId="+userId+"&opType="+opType+"&startDate="+startDate+"&endDate="+endDate+"&optDesc="+optDesc;
	  var url = contextPath + "/yh/core/funcs/seclog/act/YHSeclogAct/doExport.act?"+param;
	  location.href=url;
	  
	}

//归档日志 
function doArchive(){
	if(!confirm("归档日志将转存在之外的数据表，原数据表清空，请确认是否要继续归档？")){
		return ;
	}
	  var url = contextPath + "/yh/core/funcs/seclog/act/YHSeclogAct/doArchive.act";
	    var rtJson = getJsonRs(url);
	    if (rtJson.rtState == "0") {
	    	var flag=rtJson.rtData ;
	    	if(flag=="1"){
	    		   alert("日志归档完成！");
	    		   location.href="index.jsp";
	    	}
	       
	    } else {
	      alert(rtJson.rtMsrg); 
	    }
	 
	
}

</script>

<style type="text/css">
#opt_control{
padding-top:10px;
padding-bottom:1px;
background-color:#CCCCCC;
padding-left:5px;
padding-right:5px;
}
 
#opt_control input,#opt_control select{
border: #FFFFFF  inset thin;
background-color:#EAEAEA;
 }
 

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
background-color:#FCFCFC;
}

#opt_control input#log_search{
border:outset #C4C4C4  thin;
height:27px;
width:50px;
padding-left:10px;


}

#opt_control input#log_export{
border:outset #C4C4C4  thin;
height:27px;
width:50px;
padding-left:10px;


}


#listContainer{
padding-left:4px;
padding-right:4px;
background-color:#E4E4E4;
}

#delOpt{
padding-left:4px;
padding-right:4px;
background-color:#E4E4E4
}

#delOpt .TableList .TableControl td{
background-color:#969696;

}

#listContainer table tr#dataHeader td.TableHeader{
background-color:#969696;

}

#listContainer .TableList tr.TableLine2 td{
background-color:#E4E4E4;
}
#listContainer .TableList tr.TableLine1 td{
background-color:#BCBCBC;
}

html{
background-color:#E4E4E4;
}


#msrg{
background-color:#E4E4E4
}
</style>


</head>
<body onLoad="doInit()">
<div id="opt_control">
  <input type="hidden" name="userId" id="userId" value="" >
 操作人
        <input type="text" name="userName" id="userName" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userName'],null,null,1);">添加</a>
    <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
    
 日志类型   
    <select id="opType" name="opType">
    <option value=""> 全部 </option>
    </select>
    
    
 时间范围   
      <input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" />
     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至

      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" />   
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     
    操作结果：  <select id="optDesc" name="optDesc">
    <option value=""> 全部 </option>
    <option value="1"> 操作成功 </option>
    <option value="0"> 操作失败 </option>
    </select>
	<input type="button" value="查询"  name="log_search" onClick="doSearch();" id="log_search">
	<input type="button" value="导出"  name="log_export" onClick="doExp();" id="log_export">
	
    <hr>
</div>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <input type="button" value="日志归档" title="归档日志将转存在之外的数据表，原数据表清空"  name="log_export" onClick="doArchive()" id="log_export">
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>