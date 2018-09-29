<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>   
<%
  String fileCode = request.getParameter("fileCode")== null ? "" : YHUtility.encodeSpecial(request.getParameter("fileCode"));
  String fileSubject = request.getParameter("fileSubject")== null ? "" : YHUtility.encodeSpecial(request.getParameter("fileSubject"));
  String fileTitle = request.getParameter("fileTitle") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileTitle"));
  String fileTitleo = request.getParameter("fileTitleo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileTitleo")) ;
  String sendUnit = request.getParameter("sendUnit") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendUnit"));
  
  String sendTimeMin = request.getParameter("sendTime_Min") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Min"));
  String sendTimeMax = request.getParameter("sendTime_Max") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Max"));
  String secret = request.getParameter("secret") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("secret"));
  String urgency = request.getParameter("urgency") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("urgency"));
  
  String fileType = request.getParameter("fileType") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileType")) ;
  String fileKind = request.getParameter("fileKind") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileKind")) ;
  String filePage1 = request.getParameter("filePage1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("filePage2")) ;
  String filePage2 = request.getParameter("filePage2") == null ? "" : YHUtility.encodeSpecial(request.getParameter("catalogNo")) ;
  String printPage1 = request.getParameter("printPage1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("printPage1"));  
  String printPage2 = request.getParameter("printPage2") == null ? "" : YHUtility.encodeSpecial(request.getParameter("printPage2"));  
  String remark = request.getParameter("remark") == null ? "" : YHUtility.encodeSpecial(request.getParameter("remark"));
  String handlerTime = request.getParameter("handlerTime") == null ? "" : YHUtility.encodeSpecial(request.getParameter("handlerTime"));
  String issueNum = request.getParameter("issueNum") == null ? "" : YHUtility.encodeSpecial(request.getParameter("issueNum"));  
  String fileYear = request.getParameter("fileYear") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileYear"));
  String fileWord = request.getParameter("fileWord") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileWord"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.util.YHUtility"%>
<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件查询结果</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
	 var param = "";
	 param = "fileCode=" + encodeURIComponent("<%=fileCode%>") ;
	 param += "&fileSubject=" + encodeURIComponent("<%=fileSubject%>") ;
	 param += "&fileTitle=" + encodeURIComponent("<%=fileTitle%>") ;	 
	 param += "&fileTitleo=" + encodeURIComponent("<%=fileTitleo%>") ;
	 param += "&sendUnit=" + encodeURIComponent("<%=sendUnit%>") ;
	 
	 param += "&sendTimeMin=" + encodeURIComponent("<%=sendTimeMin%>") ;
	 param += "&sendTimeMax=" + encodeURIComponent("<%=sendTimeMax%>") ;
	 
	 param += "&secret=" + encodeURIComponent("<%=secret%>") ;
	 param += "&urgency=" + encodeURIComponent("<%=urgency%>") ;
	 param += "&fileType=" + encodeURIComponent("<%=fileType%>") ;
	 param += "&fileKind=" + encodeURIComponent("<%=fileKind%>") ;
	 param += "&filePage1=" + encodeURIComponent("<%=filePage1%>") ;
	 param += "&filePage2=" + encodeURIComponent("<%=filePage2%>") ;
	 param += "&printPage1=" + encodeURIComponent("<%=printPage1%>") ;
	 param += "&printPage2=" + encodeURIComponent("<%=printPage2%>") ;
	 param += "&remark=" + encodeURIComponent("<%=remark%>") ;
	 param += "&handlerTime=" + encodeURIComponent("<%=handlerTime%>") ;
	 param += "&fileWord=" + encodeURIComponent("<%=fileWord%>") ;
	 param += "&fileYear=" + encodeURIComponent("<%=fileYear%>") ;
	 param += "&issueNum=" + encodeURIComponent("<%=issueNum%>") ;
   
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/queryRmsFileJosn.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 6,
      sortDirect: "desc",
      colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '10%', text:"文件标题", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '10%', text:"发文单位", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"rollName",  width: '15%', text:"所属案卷", render:rmsRollNameFunc}, 
         {type:"data", name:"fileWord",  width: '5%', text:"公文字" ,render: fileWordFunc},
         {type:"data", name:"fileYear",  width: '5%', text:"公文年号",render: fileYearFunc},
         {type:"data", name:"fileNum",  width: '5%', text:"期号",render: issueNumFunc},
         {type:"selfdef", text:"操作", width: '15%',render:optsFile}]
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
      checkSelectBox();
    }else{
      $("msrg").show();
    }
}



</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="middle"><span class="big3"> 文件查询结果
</span>&nbsp;
    </td>
  </tr>
</table>

<br>
<div id="listContainer" style="display:none;width:100%;"></div>

<div id="delOpt" style="display:none">
<form id="form1" name="form1">
	<table class="TableList" width="100%">
		<tr class="TableControl">
			<td colspan="9"> 
				&nbsp;&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
				<b>文件操作：</b>组卷至：
				<select name="rollId" id="rollId" onChange="change_roll();">
					<option value="">请选择案卷</option>
				</select> 
	    	<input type="button"  value="批量销毁" class="SmallButton" onClick="destroyAll()" title="销毁已选中文件">
	    	 <input type="button"  value="批量导出" class="SmallButton" onClick="export_all()" title="批量导出">
			</td>
		</tr>
	</table>
</form>
	<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/rollmanage/rollfile/queryFile.jsp'"></center>
</div>


<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的文件</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/rollmanage/rollfile/queryFile.jsp'"></center>

</div>




</body>
</html>