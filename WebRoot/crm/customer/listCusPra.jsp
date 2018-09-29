<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>来往记录(客户机会详情)</title>
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
    <script type="text/javascript">
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.praId;
			var dblFunc = "descPop('cusServAction.do?op=showSalPraInfo&id="+obj.praId+"')";
            var relFunc = "descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var remark = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.praRemark+"</a>";
            var exeDate = obj.praExeDate.substring(0,10);
            var seName = "<span id='"+obj.praId+"PraEmp'>"+obj.person.userName+"&nbsp;</span>";
            
            var conName = "";
            if(obj.cusContact != null){
            	var dblFunc1="descPop('customAction.do?op=showContactInfo&id="+obj.cusContact.conId+"')";
				conName="<a href=\"javascript:void(0)\" onclick=\""+dblFunc1+"\">"+obj.cusContact.conName+"</a>";
            }

			var funcCol = "<a><img src='crm/images/content/detail.gif' class='hand' onClick=\""+dblFunc+"\" alt='查看详细'/></a>&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(41,["+dataId+",'1','1'])\" class='hand' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(4,"+dataId+",'1')\" class='hand' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [getCusTxtValue("t_praType",obj.praType), remark, exeDate,conName, seName, obj.praCost, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = [];
			pars.op = "getCusPra";
			pars.cusId="${cusId}";
			pars.range="${range}";
			var sortFunc = "loadList";
			var cols=[
				{name:"联系类型"},
				{name:"联系内容",align:"left"},
				{name:"联系日期"},
				{name:"联系人"},
				{name:"执行人"},
				{name:"费用",align:"right",renderer:"money"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("cusPraListTab","dataList");
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
                <th>来往记录</th>
                <td>
                <a href="javascript:void(0)" onClick="parent.cusPopDiv(4,['${cusId}','1']);return false;" class="newBlueButton">新建来往记录</a>
                </td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   		</div>
  </body>
</html>

