<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>公司通讯录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
		#empContacts {
			 text-align:center; 
			 margin:10px;
			 border:#98abbc 1px solid;
			 background-color:#fff;
		}
		#searchForm {
			margin:0px; 
			padding:0px;
			text-align:left;
			padding-left:10px;
		}
		.imgDiv {
			border:#999 1px solid; 
			width:90px; 
			text-align:center; 
			margin-right:5px; 
			height:90px; 
			background-color:#fff; 
			text-align:center;
		}
		.imgDiv img {
			width:90px;
			height:90px;
		}
		ul {
			margin:0px; 
			padding:0px; 
			list-style:none;
		}
		li{
			padding:4px;
			padding-left:10px;
		}
		.empList {
			width:90%;
		}
		.empList th{
			font-size:12px;
			font-weight:500;
			color:#000000;
			padding-top:1px;
			text-align:right;
			
		}
		.empList td{
			padding-top:1px;
			border-bottom:#666666 1px dotted;
		}
		
		.pageList table {
			width:96%;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
		/*function setImgSize(id,count){
			id=id+count;
			if($(id)!=null){
				//压缩图片
				if($(id).width>90){
					$(id).width=90;
				}
				if($(id).height>90){
					$(id).height=90;
				}
			}
		}*/
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var qq="";
			var eMail="";
			if(obj.seQq !=""){
				qq="<img src='images/content/qq.gif'  title='点击开始qq对话'/><a href='javascript:void(0)' onClick=\"qqTo('"+obj.seQq+"');return false;\">"+obj.seQq+"</a>";
			}
			if(obj.seEmail !=""){
				eMail = "<img src='images/content/email.gif'  title='点击发送电子邮件'/><a href='javascript:void(0)' onClick=\"mailTo('"+obj.seEmail+"');return false;\">"+obj.seEmail+"</a>";
			}
			datas = [obj.seName, obj.seSex, obj.salOrg?obj.salOrg.soName:"", obj.limRole?obj.limRole.rolName:"", obj.sePhone, obj.seTel, qq, obj.seMsn, eMail];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "empAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "showAddBook";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"姓名"},
				{name:"性别"},
				{name:"部门"},
				{name:"职位"},
				{name:"手机"},
				{name:"电话"},
				{name:"QQ"},
				{name:"MSN"},
				{name:"电子邮件"}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("empTabBook","dataList");
		createProgressBar();
   		window.onload=function(){
   			//表格内容省略
		    loadList();
		}
    </script>
    </head>
  
  <body>
  	<div class="mainbox">
  		<div id="contentbox">
  				<div id="listContent">
  					<div>
  					  <ul>
	                	<li style="background:url(images/content/blueTableTh.gif) repeat-x; height:28px;">	
                          	<form id="searchForm" onSubmit="loadList();return false;">
                                   姓名:
                                   <input type="text" name="seName" style="width:80px" class="inputSize3 inputBoxAlign"/>			
                                   &nbsp;<img style="vertical-align:middle; cursor:pointer;" src="images/content/search.gif" type="submit"  onClick="loadList(gridEl.sortCol, gridEl.isDe, gridEl.pageSize)" onMouseOver="this.src='images/content/search2.gif'"  onMouseOut="this.src='images/content/search.gif'" alt="查询" />&nbsp;
                             </form>
	                	</li>
	                  </ul>
                    </div>
                    <div class="dataList" id="dataList"></div>
                 </div>   
             </div>
        </div>
  </body>
</html>
