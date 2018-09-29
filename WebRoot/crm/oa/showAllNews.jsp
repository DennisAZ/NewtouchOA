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
    
    <title>我的新闻公告</title>
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
			var dblFunc="descPop('messageAction.do?op=showNewsInfo&newCode="+obj.newCode+"&isEdit=0')";
			var newTitle = "";
			if(obj.newIstop == '0'){
				newTitle +="<img src='crm/images/content/upButton.gif' alt='置顶'/>";
			}
			newTitle +="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.newTitle+"</a>";
			datas = [obj.newType, newTitle, obj.newInsUser, obj.newDate];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getAllNews";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"类型"},
				{name:"主题",align:"left"},
				{name:"发布人"},
				{name:"日期",renderer:"date"}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("allNewsTab","dataList");
		createProgressBar();
		window.onload=function(){
		//loadTabType('${category}');
			 //表格内容省略
		    loadList();
			//closeProgressBar();
		}
	 </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 新闻公告
             </div>
            <input type="hidden" id="today" value="${TODAY}"/>
            <input type="hidden" id="tomorrow" value="${TMR}"/>
            <input type="hidden" id="afTom" value="${AF_TMR}"/>
            <input type="hidden" id="yesterday" value="${YDAY}"/>
            <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListAllNews'">新闻公告</div>     
                            <div id="tabType2" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListAllSendNews'" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" >新闻公告管理</div>
                        </div>
                     </th>
                    <!-- <td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(2);return false;" class="newBlueButton">发布新闻公告</a>
                    </td>-->
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
            	<div style="margin-bottom:5px;">
					<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                    <iframe onload="loadEnd('ifmLoad')" src="messageAction.do?op=getLastNews" frameborder="0" scrolling="no" height="235px" width="100%"></iframe>
                </div>
                <div id="dataList" class="dataList"></div>
             </div>
        </div>
  	</div>
  </body>
</html>
