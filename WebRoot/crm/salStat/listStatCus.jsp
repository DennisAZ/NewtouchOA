<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户统计数据列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
	<script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.corCode+"')";
			var cusName="<a href='javascript:void(0)' onclick=\""+dblFunc+";return false;\">"+obj.corName+"</a>";
			datas = [getCusLev(obj.corHot), cusName, obj.cusType?obj.cusType.typName:'', obj.corAddress, obj.corPhone, obj.corRemark, obj.salEmp?obj.salEmp.seName:'', obj.corLastDate ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "statAction.do";
			var pars = [];
			pars.op="listStatCus";
			pars.statType="<c:out value="${statType}"/>";
			pars.statItem="<c:out value="${statItem}"/>";
			pars.empId="${empId}";
			pars.nodeIds="${nodeIds}";
			var loadFunc = "loadList";
			var cols=[
				{name:"客户级别"},
				{name:"客户名称",align:"left"},
				{name:"客户类型"},
				{name:"地址",align:"left"},
				{name:"电话",align:"left"},
				{name:"备注",isSort:false},
				{name:"负责人"},
				{name:"最近联系日期",renderer:"date"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var gridEl = new MGrid("cusStatListTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
		}
  	</script>
  </head>
  
  <body>
  <div id="mainbox">
    <div id="contentbox" style="padding:10px;">
        <div style="background-color:#2D5188; color:#FFF; padding:5px 5px 5px 10px; text-align:left">
            <span class="bold bigger" title="统计对象">
            	<c:if test="${!empty statItem}">${statItem}</c:if>  
                <c:if test="${empty statItem}">合计</c:if> 
            </span>&nbsp;&nbsp;&nbsp;<span title="负责人">[${empName}]</span>&nbsp;&nbsp;&nbsp;
        </div>
    	<div id="dataList" class="dataList"></div>
    </div>
    </div>
  </body>
</html>
