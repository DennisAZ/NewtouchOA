<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%> 
<%@ page  import="yh.core.funcs.doc.receive.data.YHDocConst,yh.core.funcs.doc.receive.data.YHDocReceive,yh.core.util.YHUtility"%>
<html>
<%
  String webroot = request.getRealPath("/");
  List<YHDocReceive>docs  = (List<YHDocReceive>)request.getAttribute("docs");
%>
<head>
<title>信息打印&nbsp;&nbsp;</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}  
 </style> 
<script type="text/javascript">
var hkey_root,hkey_path,hkey_key
hkey_root="HKEY_CURRENT_USER"
hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"
//设置网页打印的页眉页脚为空
 
function pagesetupNull(){
	try{
	var RegWsh = new ActiveXObject("WScript.Shell")
	hkey_key="header" 
	RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
	hkey_key="footer"
	RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
	}catch(e){}
}


  function closeWin(){
    try{ 
      var wb = document.getElementById("wb");
      wb.Execwb(45,1);//打印预览
    }catch(e){
      alert("如不能打印或打印预览，请调整安全级别！具体方法如下：\n根据当前页面所属安全区域，自定义安全设置，找到设置项“对未标记为可安全执行的脚本的Activex控件进行初始化并执行脚本”，将其设置为“提示”即可。");
    }
  }

 
  function userPrint(num){	 
    try{ 
      var wb = document.getElementById("wb");
      wb.Execwb(num,1);//打印预览
    }catch(e){
      alert("如不能打印或打印预览，请调整安全级别！具体方法如下：\n根据当前页面所属安全区域，自定义安全设置，找到设置项“对未标记为可安全执行的脚本的Activex控件进行初始化并执行脚本”，将其设置为“提示”即可。");
    }
  }
</script>
 

</head>
<body  style="margin:0;padding:0;">
<input type="hidden" id="listSize" name="listSize" value="1">
<br><br>

<div style="overflow:auto;" SCROLL=auto align='center'>
 
<%
  if(docs != null){%>
    <table border="1" bordercolor="#000000" style="border:1px solid #000;margin:auto;width:55%;border-collapse:collapse;">
  <tr height=10>
    <td width='3%' align='center' nowrap>序<br>号    </td>
    <td width='10%'align='center' nowrap>收文类型    </td>
    <td width='14%' align='center' nowrap>标题    </td>
    <td width='8%' align='center' nowrap>密级    </td>    
    <td width='8%' align='center' nowrap>文号    </td>    
    <td width='12%' align='center' nowrap>承办处    </td>    
    <td width='8%'  align='center' nowrap>签收人签字   </td>
  </tr>
  <%
    for(int i=0; i<docs.size(); i++){%>
	   <tr height=20>
		    <td width='3%'  align='center' nowrap><%= (i+1) %></td>
		    <td width='10%'  align='center' nowrap><%=docs.get(i).getDocTypeName(webroot) %> &nbsp;</td>
		    <td width='14%'  align='center' nowrap><%=docs.get(i).getTitle() %>&nbsp;</td>
		    <td width='8%'  align='center' nowrap><%=docs.get(i).getConfLevelName(webroot) %>&nbsp;</td>	    
		    <td width='8%'  align='center' nowrap><%=docs.get(i).getDocNo() %>&nbsp;</td>
		    <td width='12%'  align='center' nowrap><%=docs.get(i).getSponsorName() %> &nbsp;</td>
		    <td width='8%'  align='center' nowrap> &nbsp;</td>
	  </tr>
  <%}%>
</table>
<center class="Noprint">
<div id=MyWebBrowser><OBJECT id=wb classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0 VIEWASTEXT></OBJECT></div>
   <span><input title="关闭" class="BigButton" onclick="closeWin();return false;" type="button" value="关闭 "/></span>
   <span><input title="打印" class="BigButton" onclick="userPrint('6');" type="button" value="打印"/></span>
   <span><input title="打印预览" class="BigButton"  onclick="userPrint('7');" type="button" value="打印预览 "/></span>
</center>
 <%}%>
</div>
</body>
</html>