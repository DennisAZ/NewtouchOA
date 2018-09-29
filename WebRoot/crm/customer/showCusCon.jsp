<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应联系人</title>
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
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	
    <script type="text/javascript">
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.conId;
			var dblFunc="descPop('customAction.do?op=showContactInfo&id="+dataId+"')";
			var conMark = "<span class='"+(obj.conIsCons=="1"?"conShipMark":"conGrayMark")+"' id='trMark"+dataId+"' style='cursor:hand' onClick=\"addConMark("+dataId+")\" title='收货人标记'></span>&nbsp;";
			var conName="";
			if(obj.conLev == "失效"){
				className = "gray";
				conName = conMark+"<span class='mGray'><a href=\"javascript:void(0)\" onclick=\""+dblFunc+"\">"+obj.conName+"</a></span>";
			}else{
				conName = conMark+"<a href=\"javascript:void(0)\" onclick=\""+dblFunc+"\">"+obj.conName+"</a>";
			}

			var conEm=""; 
			if(obj.conEmail != undefined && obj.conEmail !=""){
				conEm="<img src='crm/images/content/email.gif'  title='点击发送电子邮件'/><a href='javascript:void(0)' onClick=\"mailTo('"+obj.conEmail+"');return false;\">"+obj.conEmail+"</a>";
			}
			var conQq="";
			if(obj.conQq != undefined && obj.conQq !=""){
				conQq="<img src='crm/images/content/qq.gif'  title='点击开始qq对话'/><a href='javascript:void(0)' onClick=\"qqTo('"+obj.conQq+"');return false;\">"+obj.conQq+"</a>";
			}
			var funcCol = "<img src='crm/images/content/detail.gif'  onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(21,["+dataId+",'1'])\" style='cursor:pointer' src='crm/images/content/edit.gif' border='0' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(2,"+dataId+",'1')\" style='cursor:pointer' src='crm/images/content/del.gif' border='0' alt='删除'/>";
			datas = [conName, obj.conSex, obj.conLev, obj.conDep, obj.conPos, obj.conPhone, obj.conWorkPho, conEm, conQq, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = [];
			pars.op = "getCusCon";
			pars.cusId="${cusId}";
			var sortFunc = "loadList";
			var cols=[
				{name:"姓名"},
				{name:"性别"},
				{name:"分类"},
				{name:"部门"},
				{name:"职务"},
				{name:"手机"},
				{name:"办公电话"},
				{name:"电子邮件"},
				{name:"QQ"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusConTab","dataList");
    	createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//closeProgressBar();
			//loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>联系人</th>
                <td><a href="javascript:void(0)" onClick="parent.cusPopDiv(2,'${cusId}');return false;" class="newBlueButton">新建联系人</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>
