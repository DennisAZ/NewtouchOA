<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示CODE_CLASS的信息</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script>
function delCodeClass(itemId) {
  var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/deleteCodeClass.act";
  var rtJson = getJsonRs(url, "sqlId=" + itemId );
  if (rtJson.rtState == "0") {
    window.location.reload();
    parent.contentFrame.location.href = "<%=contextPath %>/core/codeclass/codeiteminput.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function confirmDel() {
  if(confirm("确认删除！")) {
    return true;
  }else {
    return false;
  }
}
function doOnload() {
  var url = "<%=contextPath %>/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCode.act";
  var rtJson = getJsonRs(url );
  if (rtJson.rtState != "0") {
    alert(rtJson.rtMsrg);
  }
  var prcs = rtJson.rtData;
    var table = new Element('table',{"class":"TableBlock","align":"center","width":"100%"}).update("<tbody id='tbody'><tr><td colspan='3'>"
        +"<img src='<%=contextPath %>/core/styles/imgs/green_plus.gif'></img>"
        +"<a href='<%=contextPath%>/subsys/oa/hr/setting/hrCode/newCode.jsp' target='contentFrame'>添加代码主分类</a></td></tr>"
        +"<tr class='TableHeader'><td align='center'>编码</td><td align='center'>描述</td><td align='center'>操作</td></tr></tbody>");
    $("codeList").appendChild(table);
    for(var i  = 0; i <prcs.length ; i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var codeFlag = prc.codeFlag;
      var TableLine = "TableLine1";
      if(i%2==0){
        TableLine = "TableLine2";
      }
      //alert(prc.codeNo);
      var re1 = /\'/gi;
      var re2 = /\"/gi;
      var re3 = /\\/gi;
      var re4 = / /gi;
      var codeNo = prc.codeNo.replace(re1,"&lsquo;");
      codeNo = codeNo.replace(re2,"&quot;");
      codeNo = codeNo.replace(re3,"\\\\");
      codeNo = encodeURIComponent(codeNo);
      var optStr = "<a href='javascript:updateCode("+seqId+");'>编辑 </a>"
                  +"<a href=\"<%=contextPath%>/subsys/oa/hr/setting/hrCode/childCode/index.jsp?seqId="+seqId+"&codeNo="+ codeNo + "&codeName="+encodeURIComponent(prc.codeName)+"\" target='contentFrame'> 下一级 </a>";

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
function updateCode(seqId){
  parent.contentFrame.location.href = "<%=contextPath%>/subsys/oa/hr/setting/hrCode/updateCode.jsp?seqId="+seqId;
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
function childCode(seqId,codeNo,codeName){
  alert(codeNo);
  parent.contentFrame.location.href = "<%=contextPath%>/subsys/oa/hr/setting/hrCode/childCode/index.jsp?seqId="+seqId+"&codeNo="+encodeURIComponent(codeNo) + "&codeName="+encodeURIComponent(codeName);
}
</script>
</head>
  <body onload="doOnload();">
    <div>
    <img src="<%=imgPath %>/system.gif"></img>
    <font size="3">HRMS代码主分类设置</font>
    </div>
     <br>
     <div id="codeList"></div>
  </body>
</html>