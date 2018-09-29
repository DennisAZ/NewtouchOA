<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr = request.getParameter("seqId");
	int seqId=0;
	if(!YHUtility.isNullorEmpty(seqIdStr)){
	  seqId = Integer.parseInt(seqIdStr);
	}
	String subject = request.getParameter("SUBJECT") == null ? "" : YHUtility.encodeSpecial(request.getParameter("SUBJECT"));// 标题包含文字
	String contentNo = request.getParameter("CONTENT_NO") == null ? "" : YHUtility.encodeSpecial(request.getParameter("CONTENT_NO"));// 排序号
	String key1 = request.getParameter("KEY1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("KEY1"));// 关键字
	String key2 = request.getParameter("KEY2") == null ? "" : YHUtility.encodeSpecial(request.getParameter("KEY2"));// 关键字
	String key3 = request.getParameter("KEY3") == null ? "" : YHUtility.encodeSpecial(request.getParameter("KEY3"));// 关键字

	String attachmentDesc = request.getParameter("ATTACHMENT_DESC") == null ? "" : YHUtility.encodeSpecial(request.getParameter("ATTACHMENT_DESC"));// 附件说明包含文字
	String attachmentName = request.getParameter("ATTACHMENT_NAME") == null ? "" : YHUtility.encodeSpecial(request.getParameter("ATTACHMENT_NAME"));// 附件文件名包含文字
	String attachmentData = request.getParameter("ATTACHMENT_DATA") == null ? "" : YHUtility.encodeSpecial(request.getParameter("ATTACHMENT_DATA"));// 附件内容包含文字

	String sendTimeMin = request.getParameter("sendTime_Min") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Min"));// 最小日期
	String sendTimeMax = request.getParameter("sendTime_Max") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Max"));// 最大日期
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>全局搜索结果</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/js/queryFileLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
	//getFolderName("<%=seqId%>");
	 var param = "";
	 param = "subject=" + encodeURIComponent("<%=subject%>") ;
	 param += "&contentNo=" + encodeURIComponent("<%=contentNo%>") ;
	 param += "&key1=" + encodeURIComponent("<%=key1%>") ;	 
	 param += "&key2=" + encodeURIComponent("<%=key2%>") ;
	 param += "&key3=" + encodeURIComponent("<%=key3%>") ;
	 param += "&attachmentDesc=" + encodeURIComponent("<%=attachmentDesc%>") ;
	 param += "&attachmentName=" + encodeURIComponent("<%=attachmentName%>") ;
	 param += "&attachmentData=" + encodeURIComponent("<%=attachmentData%>") ;
	 param += "&sendTimeMin=" + encodeURIComponent("<%=sendTimeMin%>") ;
	 param += "&sendTimeMax=" + encodeURIComponent("<%=sendTimeMax%>") ;
	 param += "&seqId=<%=seqId%>";
	
  var url = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/getGlobalFileListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      moduleName:"confidential", 
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"sortId",  width: '10%', text:"文件夹", align:"left",render:shwoFolderFunc},      
         {type:"data", name:"subject",  width: '20%', text:"文件名称", align:"center",sortDef:{type: 0, direct:"desc"}},      
         {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
         {type:"text", name:"attach", text:"附件文件",align:"left",width:"20%",dataType:"attach"},     
         {type:"data", name:"attachmentDesc",  width: '10%', text:"附件说明", render:alignFunc}, 
         {type:"data", name:"sendTime",  width: '5', text:"发布时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"fileSend",  width: '1', text:"报送状态", render:fileSendFunc},
         {type:"selfdef", text:"操作", width: '2',render:globalSearchOptsList}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    count = total;
    showCntrl('listContainer');
    //var mrs = " 共 "+ total + " 条记录 ！";
    var mrs = "<b>共找到 <span class='big4'>&nbsp;" + total + "</span>&nbsp;个符合条件的文件！</b>";
    showCntrl('backDiv');
    $('fileCount').update(mrs);
  }else{
    $("msrg").show();
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/folder_search.gif" align="middle"><b><span class="Big1" id="folderName"></span>&nbsp;全局搜索结果</b>
    </td>
  </tr>
</table>
<div id="fileCount" align="center"></div>
<div id="listContainer" style="display:none;width:100%;"></div>
<br>

<div align="center" id="backDiv" style="display: none">
  <input type="button" value="返回" class="BigButton" onclick="location.href='<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/queryFile/globalQuery.jsp?seqId=<%=seqId%>'">
</div>
<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">未找到符合条件的文件</div>
    </td>
  </tr>
</table>
<center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/confidentialFile/showConfidentialFile/queryFile/globalQuery.jsp?seqId=<%=seqId%>'"></center>
</div>
</body>
</html>