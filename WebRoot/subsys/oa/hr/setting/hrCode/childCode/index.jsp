<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
  String codeNo = request.getParameter("codeNo") == null ? "" : request.getParameter("codeNo");
  String codeName = request.getParameter("codeName") == null ? "" : request.getParameter("codeName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示CODE信息</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/subsys/oa/hr/setting/codeJs/hrCodeJs.js" ></script>
<script>
function doOnload() {
  var seqId = <%=seqId%>;
  var parentId = seqId;
  var codeNo = "<%=codeNo%>";
  var codeName = '<%=codeName%>';
  var code = getCodeById(seqId);
  if(code.seqId){
    codeNo = code.codeNo;
    codeName = code.codeName;
  }
  var url = "<%=contextPath %>/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?seqId="+seqId+"&parentNo="+encodeURIComponent(codeNo);
  var rtJson = getJsonRs(url );
  if (rtJson.rtState != "0") {
    alert(rtJson.rtMsrg);
  }
  var prcs = rtJson.rtData;
    var table = new Element('table',{"class":"TableBlock","align":"center","width":"300"}).update("<tbody id='tbody'>"
                +"<tr class='TableHeader'><th align='center' colspan='3'>"+ codeNo + ":" + codeName + "</th></tr>"
                +"<tr class='TableHeader'><td colspan='3'>"
                +"<img src='<%=contextPath %>/core/styles/imgs/green_plus.gif'></img>"
                +"<a href='<%=contextPath%>/subsys/oa/hr/setting/hrCode/childCode/newCode.jsp?seqId=" + seqId + "' target='contentFrame'>添加代码项</a></td></tr></tbody>");
    $("codeList").appendChild(table);
    for(var i  = 0; i <prcs.length ; i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var codeFlag = prc.codeFlag;
      var TableLine = "TableLine1";
      if(i%2==0){
        TableLine = "TableLine2";
      }
      var optStr = "<a href='javascript:updateCode("+seqId+","+parentId+");'>编辑 </a>";
      if(codeFlag == '1'){
        optStr = optStr + "<a href='javascript:delCode("+seqId+")'>删除</a>";
      }
      var tr = new Element('tr',{"class":TableLine});
      $("tbody").appendChild(tr);
      tr.update("<td width='80px;' style='word-break : break-all;'>"+prc.codeNo+"</td>"
          +"<td >"+prc.codeName+"</td>"
          +"<td align='center'>"+optStr+"</td>" );
    }
 
}
function updateCode(seqId,parentId){
  parent.contentFrame.location.href = "<%=contextPath%>/subsys/oa/hr/setting/hrCode/childCode/updateCode.jsp?seqId="+seqId+"&parentId="+parentId;
}
function delCode(seqId){
  if(confirm("确认删除！")) {
    var url = "<%=contextPath %>/yh/subsys/oa/hr/setting/act/YHHrCodeAct/deleteCode.act?seqId="+seqId;
    var rtJson = getJsonRs(url );
    if (rtJson.rtState != "0") {
      alert(rtJson.rtMsrg);
    }
    window.location.reload();
  } 
}
function childCode(seqId){
  parent.contentFrame.location.href = "<%=contextPath%>/subsys/oa/hr/setting/hrCode/childCode/index.jsp?seqId="+seqId;
}
</script>
</head>
  <body onload="doOnload();">
    <div>
    <img src="<%=imgPath %>/system.gif"></img>
    <font size="3">代码项设置</font>
    </div>
     <br>
     <div id="codeList"></div>
  </body>
</html>