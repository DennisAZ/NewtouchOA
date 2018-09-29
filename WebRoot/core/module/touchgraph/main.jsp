<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%
 String contextPath = request.getContextPath();
 %>
<%
String data = request.getParameter("data");
%>
<HTML>
<HEAD>
<title>关系搜索图</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var contextPath = "<%=contextPath%>";
function openFileWindow(fileId){
  var url1 = contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getDoc.act"
  var json = getJsonRs(url1 , "fileId=" + fileId);
  if (json.rtState != "0") {
    return ;
  }
  if (!json.rtData) {
    return ;
  }
  var path =  json.rtData;
  var name = json.rtState;
  var param = "fileName="+name+"&path="+ path +"&op=7&signKey=&print="; 
  var url = contextPath + "/core/funcs/office/netDiskNtko/index.jsp?" + param;
  url = encodeURI(url);
  openWindow(url,'在线编辑',900,600);
}
</script>
</HEAD>
<BODY style="padding:0px;margin:0px">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="touchgraph" width="100%" height="100%"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="touchgraph.swf?data=<%=data %>" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#000000" />
            <param name="data" value="ddd"/>
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="touchgraph.swf" quality="high" bgcolor="#000000"
        width="100%" height="100%" name="touchgraph" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
</BODY>
</HTML>