<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsBoard,java.sql.ResultSet" %>
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
<title>讨论区设置</title>
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
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tbody><tr>
    <td class="Big"><img src="/yh/core/funcs/system/netdisk/images/notify_new.gif" align="middle"><span class="big3"> 新建讨论区</span><br>
    </td>
  </tr>
</tbody></table>

<div align="center">
<input type="button" value="新建讨论区" class="BigButtonC" onclick="location='bbsBoard.jsp';" title="新建讨论区">
</div>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tbody><tr>
   <td background="/yh/core/funcs/system/netdisk/images/dian1.gif" width="100%"></td>
 </tr>
</tbody></table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tbody><tr>
    <td class="Big"><img src="/yh/core/funcs/system/netdisk/images/notify_open.gif" align="middle"><span class="big3"> 管理讨论区</span>
    </td>
  </tr>
</tbody></table>
<br>
<div id="listDiv"><table width="95%" class="TableList" align="center"><tbody id="tbody">

<tr class="TableHeader" align="center" style="font-size:10pt">
	<td nowrap align="center">序号</td>
      <td nowrap align="center">讨论区</td>
      <td nowrap align="center" width="30%">开放范围</td>
      <td nowrap align="center">匿名发帖</td>
      <td nowrap align="center" width="20%">版主</td>
      <td nowrap align="center">操作</td>
</tr>

<%
 String flag=request.getParameter("flag");
BbsService bs= new BbsService();
 if(flag!=null && flag.equals("d")){
  bs.deleteBoard(request,false);
 }
List bdlist=bs.getAllBoard(request);
 for(int i=0;i<bdlist.size();i++){
 BbsBoard bbsB=(BbsBoard)bdlist.get(i);
 %>
 
<tr width="90%" class="TableLine1" font-size="10pt">
<td align="center"><%=bbsB.getBoardNo()%></td>
      <td align="center"><%=bbsB.getBoardName()%></td>
    <td align="left">
      <%if(!bbsB.getDeptName().equals("")){ %>
      <font color=#0000FF>
      <b>部门：</b></font>
     <%=bbsB.getDeptName()%><br>
     <%} %>
      <%if(!bbsB.getPrivName().equals("")){ %>
          <font color=#0000FF>
      <b>角色：</b></font>
      <%=bbsB.getPrivName()%><br>
      <%} %>
      
      <%if(!bbsB.getUserName().equals("")){ %>
                <font color=#0000FF>
      <b>用户：</b></font>
      <%=bbsB.getUserName()%>
      <%} %>
      </td>
      <td align="center">
      <%if(bbsB.getAnonymity().equals("1")){ %>
      允许
      <%}else{ %>
      禁止
      <%} %>
      </td>
      <td align="center"><%=bbsB.getBoaderHosterName()%></td>
      <td nowrap align="center">
     
          <a href="editBoard.jsp?boardId=<%=bbsB.getBoarderId()%>">编辑</a>
          <a href="index.jsp?boardId=<%=bbsB.getBoarderId()%>&&flag=d">删除</a>
      </td>
    </tr>
<%} %>

</body></html>