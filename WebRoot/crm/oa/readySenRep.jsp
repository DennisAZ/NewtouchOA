<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
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
    
    <title>待发报告</title>
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
			var title="<a style=\"cursor:pointer\" href=\"javascript:void(0)\" onClick=\"oaPopDiv(41,"+obj.repCode+");return false;\">"+obj.repTitle+"</a>";
			var funcCol="<img src=\"crm/images/content/green_execute.gif\" alt=\"发送\" style=\"cursor:pointer\" onClick=\"oaPopDiv(41,"+obj.repCode+")\"/>&nbsp;<img src=\"crm/images/content/del.gif\" alt=\"删除\" style=\"cursor:pointer\" onClick=\"oaDelDiv(5,"+obj.repCode+")\"/>";							
			datas = [obj.repType?obj.repType.typName:"", title, obj.repDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getReadySenRep";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"类别"},
				{name:"报告主题",align:"left"},
				{name:"保存日期",renderer:"date"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("readySenRepTab","dataList");
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 报告管理</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                        	<div id="tabType1" class="tabTypeBlue2" onClick="self.location.href='messageAction.do?op=toListHaveSenRep'" onMouseOver="changeTypeBg(this,1,2)" onMouseOut="changeTypeBg(this,0,2)">我的报告</div>
                            <div id="tabType2" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListApproRep'" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)">已收报告</div>     
                            <div id="tabType3" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListReadySenRep'">待发报告</div>
                        </div>
                     </th>
                     <td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(4);return false;" class="newBlueButton">写报告</a>
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
