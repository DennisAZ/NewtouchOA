<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BBSUtil,oa.core.funcs.bbs.act.BbsComment,java.sql.ResultSet" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html><head><script type="text/javascript">
/** 常量定义 **/
var TDJSCONST = {
  YES: 1,
  NO: 0
};
/** 变量定义 **/
var contextPath = "/yh";
var imgPath = "/yh/core/styles/style1/img";
var ssoUrlGPower = "";
var limitUploadFiles = "jsp,java,jspx,exe"
var signFileServiceUrl = "http://192.168.0.5:9000/BjfaoWeb/TitleSign";
var isOnlineEval = "0";
var useSearchFunc = "1";
var maxUploadSize = 500;
var isDev = "0";
var ostheme = "1";
</script> 




<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>讨论区</title>
<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js"></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/Javascript" src="/yh/core/js/cmp/select.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js"></script>
<script type="text/javascript">
var requestURL="/yh/yh/core/funcs/system/netdisk/act/YHNetdiskAct";	
function doInit(){
	var usePortalFunc = "0";
	var type="NETDISK";
	var url=requestURL + "/getNetdiskFolderInfo.act";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"95%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
				+"<td nowrap align='center'>序号</td>"				
				+"<td nowrap align='center'>目录名称</td>"				
				+"<td nowrap align='center'>目录路径</td>"				
				+"<td nowrap align='center'>限制容量</td>"				
				+"<td nowrap align='center'>默认排序</td>"				
				+"<td align='center'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var sqlId=prcs.sqlId;
			var diskNo=prcs.diskNo;
			var diskName=prcs.diskName;
			
			var diskPath=prcs.diskPath;
			var spaceLimit=prcs.spaceLimit;
			var orderBy=prcs.orderBy;
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
			var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});
			table.firstChild.appendChild(tr);
			var sendDeskTop = "";
			if(usePortalFunc == "1"){
				sendDeskTop = "<a href=\"/yh/core/funcs/portal/cfgPortal.jsp?type=1&&publicPath=" + encodeURIComponent(diskPath) + "\" >发布</a>&nbsp;&nbsp;";
			}
			tr.update("<td align='center'>"					
				+ diskNo + "</td><td align='left'>"					
				+ diskName + "</td><td align='left'>"					
				+ diskPath + "</td><td align='center'>"					
				+ spaceLimit + "</td><td align='center'>"					
				+ orderBy + "</td><td align='center'>"					
				+ "<a href='editNeskFolder.jsp?seqId="+sqlId +"&contentId="+"'> 编辑</a>&nbsp;"
				+ "<a href='#'onclick='delete_Proces(\""+ sqlId +"\")'>删除 </a>&nbsp;"
				+ "<a href='setPriv/index.jsp?seqId="+sqlId +"'> 权限设置</a>&nbsp;"
				+ "<a href='#' onclick='menuCode(\""+type +"\"," +"\"" +sqlId+ "\");'>菜单定义指南</a>&nbsp;&nbsp;"
				+ sendDeskTop
				+ "&nbsp;</td>"
			);	
		}		
	}	
}
//发布方法 
function publish(diskPath){ 
	window.location.href = contextPath + "/core/funcs/portal/cfgPortal.jsp?type=1&&publicPath="+diskPath; 
	return true; 
}
function menuCode(type, id){
  //alert(type +" ...."+id);
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  showModalWindow(URL , title , "menuWindow" ,600,350);
}
function delete_Proces(seqId){
	//alert(seqId);	
 	msg='确认要取消该共享目录吗？这不会删除该路径下的文件。';
 	if(window.confirm(msg)) {
		var url=requestURL+"/delNetdiskFolderById.act";
		//alert(url);
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
	    window.location.reload();	    		
    }else{
			alert(json.rtMsrg);
    }
	} 	
}


</script>
</head>
<body class="" topmargin="5">
<%
HttpSession s =request.getSession();

Object o=s.getAttribute("LOGIN_USER");
String boardId=request.getParameter("boardId");
List bdlist=new BbsService().getUserBBSCommentByBoardId(request,o,boardId);%>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tbody><tr>
   <td background="/yh/core/funcs/system/netdisk/images/dian1.gif" width="100%"></td>
 </tr>
</tbody></table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tbody><tr>
    <td class="Big"><img src="/yh/core/funcs/system/netdisk/images/notify_open.gif" align="middle"><span class="big3">讨论区>><%=request.getParameter("boardName")%></span>
    </td>
  </tr>
</tbody></table>
<br>
<div id="listDiv">
<table width="95%" class="TableList" align="center">
<tbody id="tbody">
<tr height="45px;">
      <td class="" align="right" colspan=6><span><input type="button" id="st" value="发贴" onclick="javascript:window.location.href='addBBS.jsp?boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回讨论区" class="BigButton" onclick="javascript:window.location.href='bbsIndex.jsp'"></span></td>
</tr>
<tr class="TableHeader" align="center" style="font-size:10pt">
<%
String hoster=(request.getSession().getAttribute(BBSUtil.bbsHoster)).toString();
if(hoster.equals("1"))
{%>
      <td nowrap align="center">选择</td>
 <%}%>
      <td nowrap align="center">标题</td>
       <td nowrap align="center" width="15%">作者</td>
      <td nowrap align="center">字节</td>
      <td nowrap align="center" width="15%">回/阅</td>
      <td nowrap align="center">最后发表</td>
</tr>

<%for(int i=0;i<bdlist.size();i++){
 BbsComment bbsB=(BbsComment)bdlist.get(i);
 %>
 
<tr width="90%" class="TableLine1" font-size="10pt">
<%if(hoster.equals("1"))
{%>
<td align="center">
<input type="checkbox" name="title_select" value="<%=bbsB.getCommentId()%>">
</td>
<%} %>
<td align="center">
<a href="commentDetail.jsp?commentId=<%=bbsB.getCommentId()%>&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>">
<%if(bbsB.getJing().equals("1")){ %><font color="red"><b><%=bbsB.getSubject()%></b></font><%}else{ %><b><%=bbsB.getSubject()==null||bbsB.getSubject().equals("")?"无标题":bbsB.getSubject()%></b><%} %>
<%if(bbsB.getHadRead()==0){ %><img src="new.gif" height="11" width="28"><%} %>
<%if(bbsB.getJing().equals("1")){ %><img src="jing.gif" height="11" width="28"><%} %>
</a>
</td>
      <td align="center"><%=bbsB.getAuthorName()%>
     </td>
    <td align="center">
<%=BBSUtil.getContentByte(bbsB.getContent())%>
      </td>
      <td align="center">
      <%=bbsB.getReplyCont()+"/"+bbsB.getReadCont()%>
      </td>
      <td align="center">
      <%if(bbsB.getLastAuthor().equals("")) {%>
        无
      <%}else{ %>
      <%=bbsB.getLastTime()%> by <br><%=bbsB.getLastAuthor()%>
      <%} %>
     </td>
    </tr>
<%} %>
</tbody>
</table>
<table>
<%
if(hoster.equals("1"))
{%>
<tr height="45px;">
      <td class="" align="left" colspan=6>
      <span>
      <input type="button" id="sc" value="删除主题" onclick="delComments()" class="BigButton">&nbsp;&nbsp;
      <input type="button" id="jj" value="加精/取消加精" onclick="addJing()" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="置顶/取消置顶" class="BigButton" onclick="addTop()"></span>
      </td>
</tr>
<%} %>
</table>
 <script type="text/javascript">
  var str="";
    function checkBoxCal(){
      var ches = document.getElementsByName("title_select");
            var j = 0;
            str="";
            for(var i = 0;i < ches.length;i++){
                if(ches[i].checked){
                    j++;
                    str+=ches[i].value+",";
                }
            }
    }
    function  delComments(){
      checkBoxCal();
      if(str==""){
      	alert("请选择要删除的帖子");
      	  return;
      }
     window.location.href="mOrUpdateComments.jsp?flag=d&&commentIds="+str+"&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>";
    }
    function  addJing(){
     checkBoxCal();
     if(str==""){
      	alert("请选择要处理的帖子");
      	  return;
      }
    
     window.location.href="mOrUpdateComments.jsp?flag=j&&commentIds="+str+"&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>";
    
    }
    function  addTop(){
     checkBoxCal();
     if(str==""){
      	alert("请选择要处理的帖子");
      	  return;
      }
     window.location.href="mOrUpdateComments.jsp?flag=t&&commentIds="+str+"&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>";
    
    }
    </script>
</body></html>