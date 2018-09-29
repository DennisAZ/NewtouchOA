<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String flag = request.getParameter("flag");
  if(flag == null) {
    flag = "";
  }
  String data = request.getParameter("data");
  if(data == null) {
    data = "";
  }
  String isCount = request.getParameter("isCount");
  if(isCount == null) {
    isCount = "";
  }
  String dataObj = (String)request.getAttribute("contentList");
  if(dataObj == null) {
    dataObj = "";
  }
%>
<head>
<title>导入主题词</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/docmgr/docword/js/wordlogic.js"></script>
<script type="text/javascript">
var isCount = "<%=isCount%>";
var flag = "<%=flag%>";
var dataObj = '<%=dataObj%>';

function doInit(){
  if(flag != "1"){
    var count = 0;
    //alert(dataObj);
    dataObj = dataObj.evalJSON();
    if(dataObj){
      var table=new Element('table',{ "width":"540","class":"TableList","align":"center"})
		  .update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='90'>主题词</td>	"					
				+"<td nowrap align='center' width='90'>类别</td>"
				+"<td nowrap align='center' width='90'>排序号</td>"
				+"<td nowrap align='center' width='90'>类型</td>"
				+"<td nowrap align='center' width='90'>是否成功</td></tr><tbody>");
		  $('listDiv').appendChild(table);
  	  for(var i = 0; i < dataObj.length; i++){
  	    count++;
  	    var word = dataObj[i].word;
  	    var parentId = dataObj[i].parentId;
  	    var sortNo=dataObj[i].sortNo;
  	    var typeFlag=dataObj[i].typeFlag;
  	    var info=dataObj[i].info;
  	    var color = dataObj[i].color;
        var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
  	    var tr=new Element('tr',{'class':trColor});			
			  table.firstChild.appendChild(tr);
			  tr.update("<td align='center'><font color="+color+">"					
				  + word + "</font></td><td align='center'><font color="+color+">" 	
				  + parentId + "</font></td><td align='center'><font color="+color+">"
					+ sortNo + "</font></td><td align='center'><font color="+color+">"
					+ typeFlag+ "</font></td><td align='center'><font color="+color+">"
			    + info+ "</font></td>"
				);
  	  }
    }
  }
  if(count > 0){
    $("cionDiv").style.display = 'none';
  }else{
    $("turnDiv").style.display = 'none';
  }
  if(flag != "1"){
    //if(isCount != "0"){
    //  parent.deptListTree.location.reload();
    //}
    showCntrl('listContainer');
    var mrs = " 共 "+ isCount + " 条数据导入!";
    WarningMsrg(mrs, 'msrg');
  }
}

</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="cionDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3"> 导入主题词</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>
  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form name="form1" id="form1" action="<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/importWord.act" method="post" enctype="multipart/form-data">
    <input type="file" id="csvFile" name="csvFile" class="BigInput" size="30">
    <input type="hidden" id="FILE_NAME" name="FILE_NAME">
    <input type="hidden" id="GROUP_ID" name="GROUP_ID" value="">
    <input type="button" value="导入" class="BigButton" onClick="importWord2();">
  </form>
  <br>
  </div>
  </div>
  <div id="listDiv" align="center"></div>
  <br>
  <div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
  <br>
  <div align="center" id="turnDiv">
   <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/inforesource/docmgr/docword/importword.jsp?flag=1';" title="返回">
</div>
</body>
</html>