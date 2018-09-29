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
    
    <title>待发消息</title>
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
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId=obj.meCode;
			/*
			var files="";
			if(obj.attachments !=""){
                files="<img id=\""+obj.meCode+"y\" style='vertical-align:middle; cursor:pointer' src='crm/images/content/attach.gif' border='0' alt='附件管理' onClick=\"commonPopDiv(1,"+obj.meCode+",'mes','doc')\"/>";
			}
			else{
				files="<img id=\""+obj.meCode+"n\" style='vertical-align:middle; cursor:pointer' src='crm/images/content/attachNo.gif' border='0' alt='附件管理' onClick=\"commonPopDiv(1,"+obj.meCode+",'mes','doc')\"/>";
			}*/
            var title="<a href='javascript:void(0)' onClick=\"oaPopDiv(13,"+obj.meCode+");return false;\">"+obj.meTitle+"</a>";
			var funcCol = "<img src='crm/images/content/sendMes.gif' alt='发送消息' style='cursor:pointer' onClick=\"oaPopDiv(13,"+obj.meCode+")\"/>&nbsp;&nbsp;<img src='crm/images/content/del.gif' alt='删除' style='cursor:pointer' onClick=\"oaDelDiv(8,"+obj.meCode+")\"/>";
			datas = [title, obj.meDate,funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getReadySenMess";
			
			var loadFunc = "loadList";
			var cols=[
				/*{name:"附件",align:"left",isSort:false},*/
				{name:"消息主题",align:"left"},
				{name:"保存日期",renderer:"date"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("readyMessTab","dataList");
    	gridEl.config.hasCheckBox =true;
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//closeProgressBar();
		}
	 </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 内部消息
           	</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListAllMess'"  onMouseOut="changeTypeBg(this,0,1)" onMouseOver="changeTypeBg(this,1,1)">已收消息</div>     
                        <div id="tabType2" class="tabTypeBlue2" onClick="self.location.href='messageAction.do?op=toListHaveSenMess'" onMouseOver="changeTypeBg(this,1,2)" onMouseOut="changeTypeBg(this,0,2)" >已发消息</div>
                        <div id="tabType3" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListReadySenMess'" >草稿箱</div>
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
                	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(6,'消息','send')">批量删除</span>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
</html>
