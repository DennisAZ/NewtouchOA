<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>收到的消息</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
    
    <script type="text/javascript">
		function changeMesType(){
			var mesType=$("mesType").value;
			self.location.href="messageAction.do?op=getAllMess&mesType="+mesType+"&pageSize=${page.pageSize}";
		}
		
		function loadMesType(){
			if($("mesType")!=null){
				$("mesType").value="${mesType}";
			}
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.rmlId;
			var dblFunc="descPop('messageAction.do?op=showMessInfo&meCode="+obj.message.meCode+"&rmlId="+obj.rmlId+"&mark=default')";
			var title ="";
			/**if(obj.message.attachments !=""){
				title+="<img style='vertical-align:middle;' src='crm/images/content/attIconS.gif' border='0px' alt='附件'/>";
			}*/
			title+="<a href='javascript:void(0)' onclick=\""+dblFunc+"\" >"+obj.message.meTitle+"</a>";
			var state ="";
			switch(obj.rmlState){
				case '0': 
					state="<img src='crm/images/content/no_read.gif' alt='未读'/>";
					className="bold";
					break;
				case '1':
					state="<img src='crm/images/content/read.gif' alt='已读'/>";
					break;
				case '2':
					state="<img src='crm/images/content/mesreply.gif' alt='已回'/>";
					break;
			}
			var funcCol = "<img src=\"crm/images/content/del.gif\" alt=\"删除\" style=\"cursor:pointer\" onClick=\"oaDelDiv(6,"+obj.rmlId+")\"/>";
			datas = [state, title, obj.message?obj.message.meInsUser:"", obj.rmlDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getAllMess";
			pars.mesType = "${mesType}"
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态",width:"5%"},
				{name:"消息主题",align:"left"},
				{name:"发送人"},
				{name:"发送日期",renderer:"date"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("allMessTab","dataList");
    	gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			loadMesType();
			//表格内容省略
			loadList();
			//closeProgressBar();
		}
	 </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 内部消息</div>
            	<table class="mainTab" cellpadding="0" cellspacing="0">
                   <tr>
                        <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListAllMess'">已收消息</div>     
                            <div id="tabType2" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListHaveSenMess'" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" >已发消息</div>
                            <div id="tabType3" class="tabTypeBlue2" onClick="self.location.href='messageAction.do?op=toListReadySenMess'" onMouseOver="changeTypeBg(this,1,2)" onMouseOut="changeTypeBg(this,0,2)" >草稿箱</div>
                        </div>
                     	</th>
                     	<td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(1);return false;" class="newBlueButton">写消息</a>
                    	</td>
                	</tr>
            	</table>
            	<script type="text/javascript">loadTabTypeWidth();</script>
            	<div id="listContent">
        	        <div class="bottomBar">
                    	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(6,'消息','ac')">批量删除</span>
                    </div>
                    <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
</html>
