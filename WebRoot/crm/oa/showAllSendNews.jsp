<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0);  
	

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>所有已发的新闻公告</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
    
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var dblFunc="descPop('messageAction.do?op=showNewsInfo&newCode="+obj.newCode+"&isEdit=1')";
			var newTitle ="";
			if(obj.newIstop =='0'){
				newTitle +="<img src='crm/images/content/upButton.gif' alt='置顶'/>";
			}
			newTitle += "<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.newTitle+"&nbsp;</a>";
			var funcCol = "<img src=\"crm/images/content/detail.gif\" style=\"cursor:pointer\" alt=\"查看详细\" onclick=\""+dblFunc+"\"/>&nbsp;&nbsp;<img onClick=\"oaPopDiv(21,"+obj.newCode+")\" style=\"cursor:pointer\" src=\"crm/images/content/edit.gif\" border=\"0\" alt=\"编辑\"/>&nbsp;&nbsp;<img onClick=\"oaDelDiv(1,"+obj.newCode+")\" style=\"cursor:pointer\" src=\"crm/images/content/del.gif\" border=\"0\" alt=\"删除\"/>";
			datas = [obj.newType, newTitle, obj.newInsUser, obj.newDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getAllSendNews";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"类型"},
				{name:"主题",align:"left"},
				{name:"发布人"},
				{name:"日期",renderer:"date"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("allSendNewsTab","dataList");
		createProgressBar();
		window.onload=function(){
			 //表格内容省略
		    loadList();
			//closeProgressBar();
		}
	 </script>
  </head>
  
  <body  >
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 新闻公告管理
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListAllNews'" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" >新闻公告</div>     
                            <div id="tabType2" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListAllSendNews'">新闻公告管理</div>
                        </div>
                     </th> 
                     <td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(2);return false;" class="newBlueButton">发布新闻公告</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
            	<div id="dataList" class="dataList"></div>
	             </div>
             
        </div>
  	</div>
  </body>
</html>
