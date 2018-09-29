<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>日程安排(客户详情下)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    
    <script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var schState ="";
			schState=obj.schState; 
			if(obj.schState == '已完成'){
				schState += "<span><img class='imgAlign' src='images/content/suc.gif' alt='已完成'/></span>";
			}
			if(obj.schState == '未完成'){
				schState +="<span id='y"+obj.schId+"' style='display:none'><img class='imgAlign' src='crm/images/content/suc.gif' alt='已完成'/></span>";
				if(obj.person&&obj.person.seqId=="${LOGIN_USER.seqId}"){
					schState +="<span id='"+obj.schId+"'><img class='imgAlign' src='crm/images/content/execute.gif' border='0' style='cursor:pointer' onClick=\"modifySchState('"+obj.schId+"')\" alt='点击完成'/></span>";
				}
			}
			
			var schTitle = "<a style='cursor:pointer' href='javascript:void(0)' onClick=\"parent.cusPopDiv(18,"+obj.schId+");return false;\">"+obj.schTitle+"</a>";
			var schTime = obj.schStartTime+"&nbsp;-&nbsp;"+obj.schEndTime;
			var funcCol = "<a href=\"javascript:void(0)\" onClick=\"parent.cusPopDiv(18,"+obj.schId+");return false;\"><img src=\"crm/images/content/detail.gif\" border=\"0\" alt=\"查看详细\"/></a>";
			if(obj.salEmp&&obj.salEmp.seNo=="${CUR_EMP.seNo}"){
				funcCol += "&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(19,"+obj.schId+")\" style=\"cursor:pointer\" src=\"crm/images/content/edit.gif\" border=\"0\" alt=\"编辑\"/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(9,"+obj.schId+",'1')\" style=\"cursor:pointer\" src=\"crm/images/content/del.gif\" border=\"0\" alt=\"删除\"/>";
			}
			datas = [schState, obj.schType?obj.schType.typName:"", schTitle, obj.schStartDate, schTime, funcCol];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "listCusSch";
			pars.cusId="${cusId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"类别"},
				{name:"日程内容",align:"left"},
				{name:"日期",renderer:"date"},
				{name:"时间",isSort:false},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusSch","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
		}
	  	
	 </script>
  </head>
  
  <body>
      <div class="divWithScroll2 innerIfm">
      	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
      		<tr>
      			<th>日程安排</th>
      			<td><a href="javascript:void(0)" onClick="parent.cusPopDiv(17,'${cusId}');return false;" class="newBlueButton">新建日程</a></td>
      		</tr>
      	</table>
          <div id="dataList" class="dataList"></div>
      </div>
  </body>
</html>