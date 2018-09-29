<%@ page language="java" contentType="text/html;charset=utf-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>   
    
<script language="javascript">AC_FL_RunContent = 0;</script>
<script src="Scripts/AC_RunActiveContent.js" language="javascript"></script>


  </head>

  <body onLoad="doInit();" leftmargin="0" topmargin="0">
  <%
  String userName=request.getParameter("userName");
  String roomId=request.getParameter("roomId");
  String seqId=request.getParameter("seqId");
  String role=request.getParameter("role");
  String server="rtmp://"+request.getServerName();
  String connStr ="userName="+userName+"&realName="+userName+"&password=123&mediaServer="+server+"&role="+role+"&roomID="+roomId+"&scriptType=jsp";
// String connStr="userName=www&realName=www*&password=&mediaServer=rtmp://localhost&role=2&roomID=DEMOROOM&scriptType=jsp";  
   //System.out.println(connStr);
%>
<script language="javascript">

 var userName='<%=userName%>';
 var seqId=<%=seqId%>;
 var role=<%=role%>;
function doInit(){
 if(role!="2"){
//  $("opera").style.display="block";
   document.getElementById("opera").style.display="none";
  }
 }
function add_user(){
   var url="/yh/subsys/oa/vmeet/addUser.jsp?userName="+userName+"&seqId="+seqId;
   window.open(url,"","height=300,width=550,status=0,toolbar=no,menubar=no",false);

  
}

</script>

    <script language="javascript">
  if (AC_FL_RunContent == 0) {
    alert("此页缺少JavaScript");
  } else {
    AC_FL_RunContent(
      'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0',
      'width', '100%',
      'height', '100%',
      'src', 'preloader?<%=connStr %>',
      'quality', 'high',
      'pluginspage', 'http://www.macromedia.com/go/getflashplayer',
      'align', 'middle',
      'play', 'true',
      'loop', 'true',
      'scale', 'showall',
      'wmode', 'window',
      'devicefont', 'false',
      'id', 'preloader',
      'bgcolor', '#ffffff',
      'name', 'preloader',
      'menu', 'true',
      'allowFullScreen', 'false',
      'allowScriptAccess','sameDomain',
      'movie', 'preloader?<%=connStr %>',
      'salign', ''
      ); //end AC code
  }
</script>

<div  >
    <input type="button"  id="opera"   value="邀请" class="SmallButton" onClick="add_user()" title="邀请更多人参会">
  <input type="button" value="刷新" class="SmallButton" onClick="  window.location.reload();">
  <input type="button" value="返回" class="SmallButton" onClick="window.history.go(-2)"><br>
</div>
    <noscript>
  <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0"  width="100%" height="100%" id="preloader" align="middle">
  <param name="allowScriptAccess" value="sameDomain" />
  <param name="allowFullScreen" value="false" />
  <param name="movie" value="preloader.swf?<%=connStr %>" />
  <param name="quality" value="high" />
  <param name="bgcolor" value="#ffffff" />
  <embed src="preloader.swf?<%=connStr %>" quality="high" bgcolor="#ffffff" 
  width="100%" height="100%" name="preloader" align="middle" 
  allowScriptAccess="sameDomain" allowFullScreen="false" 
  type="application/x-shockwave-flash" 
  pluginspage="http://www.macromedia.com/go/getflashplayer" />
  </object>
</noscript>
  </body>
</html>
