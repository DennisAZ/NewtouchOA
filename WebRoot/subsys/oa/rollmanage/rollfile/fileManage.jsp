<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  boolean regist = YHUtility.isNullorEmpty(YHRegistProps.getProp(YHAuthKeys.REGIST_ORG + ".yh"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.global.YHRegistProps"%>
<%@page import="yh.core.data.YHAuthKeys"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">
var regist = "<%=regist%>";
var pageMgr = null;
function doInit(){
  if(!regist){
    $("registDiv").style.display = '';
  }else{
    $("registDiv").style.display = 'none';
  }
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/getRmsFileJosn.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '10%', text:"文件标题", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '10%', text:"发文单位", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"rollName",  width: '10%', text:"所属案卷", render:rmsRollNameFunc}, 
         {type:"data", name:"handlerTime",  width: '5%', text:"处理时长", render:rollFileFunc}, 
         {type:"data", name:"turnCount",  width: '5%', text:"流转次数", render:rollFileFunc}, 
         {type:"data", name:"fileWord",  width: '5%', text:"公文字" ,render: fileWordFunc},
         {type:"data", name:"fileYear",  width: '5%', text:"公文年号",render: fileYearFunc},
         {type:"data", name:"fileNum",  width: '5%', text:"期号",render: issueNumFunc},
         {type:"selfdef", text:"操作", width: '10%',render:optsFile}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
      //getRmsRollSelect("rollId");
      checkSelectBox2();
    }else{
      WarningMsrg('无文件', 'msrg');
    }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3"> 文件管理</span>&nbsp;
    </td>
  </tr>
</table>

<br>
<div id="listContainer" style="display:none;width:100%;"></div>

<div id="delOpt" style="display:none">
<form id="form1" name="form1">
	<table class="TableBlock no-top-border" width="100%">
		<tr class="TableData">
			<td colspan="9"> 
				&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
				<b>文件操作：</b>组卷至
				<select name="rollId" id="rollId" onChange="change_roll();" >
					<option value="">请选择案卷</option>
					
				</select> 
	    	<input type="button"  value="批量销毁" class="SmallButton" onClick="destroyAll()" title="销毁已选中文件">
			</td>
		</tr>
	</table>
</form>
</div>

<div id="msrg"></div>
<br><br><div id="registDiv" style="display:none" align=center></div>




</body>
</html>