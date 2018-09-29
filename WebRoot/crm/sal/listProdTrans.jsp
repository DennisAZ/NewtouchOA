<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>产品运费</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var funcCol = "<img class='imgAlign' onClick=\"parent.ordPopDiv(53,'"+obj.ptrId+"','${prodId}')\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.ordDelDiv(8,"+obj.ptrId+",'1')\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>";
			datas = [obj.ptrExpTypeList?obj.ptrExpTypeList.typName:"",obj.ptrUnit, obj.ptrProv?obj.ptrProv.areName:"", obj.ptrCity?obj.ptrCity.prvName:"", obj.ptrDistrict?obj.ptrDistrict.cityName:"", obj.ptrAmt, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "prodAction.do";
			var pars = [];
			pars.op = "listProdTrans";
			pars.prodId="${prodId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"物流公司"},
				{name:"计价单位"},
				{name:"省份"},
				{name:"城市"},
				{name:"区县"},
				{name:"运费单价",renderer:"money",align:"right"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("prodTransListTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>运费标准</th>
                <td><a href="javascript:void(0)" class="newBlueButton"  onClick="parent.ordPopDiv(52,'${prodId}');return false;">新建运费标准</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>
