<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
    <title>员工档案</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/rec.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	
    <script type="text/javascript" src="js/rec.js"></script>
    <script type="text/javascript">
        function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.seNo;
			var dblFunc="descPop('empAction.do?op=salEmpDesc&seNo="+obj.seNo+"')";
			var seName = "<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.seName+"</a>";
			var funcCol = "<img onClick=\"recAddDiv(26,'rec','"+obj.seNo+"')\" style='cursor:pointer' src='images/content/restore.gif' alt='恢复'/>&nbsp;&nbsp;<img onClick=\"recAddDiv(26,'del','"+obj.seNo+"')\" style='cursor:pointer' src='images/content/del.gif' alt='彻底删除'/>";			
			datas = [obj.seCode, seName, obj.seSex, obj.limRole?obj.limRole.rolName:"", obj.seType, obj.salOrg?obj.salOrg.soName:"", funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "empAction.do";
			var pars = [];
			pars.op = "findDelSalEmp";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"员工号",align:"left"},
				{name:"员工姓名"},
				{name:"性别"},
				{name:"职位"},
				{name:"员工类型"},
				{name:"所属部门"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("recEmpListTab","dataList");
		gridEl.config.sortable=false;
		gridEl.config.hasCheckBox=true;
		createProgressBar();
   		window.onload=function(){
			//表格内容省略
			loadList();
			loadMenuIfm();
		}
    </script>
	
</head>

<body>
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 回收站 </div>
			    <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">回收站</div>
                    </div>
                    </th>
                </tr>
            </table>
			
			<div id="listContent">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%;">
                    <tr>
                        <td id="recMenu">
                        	<input type="hidden" id="typeId" value="41"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" id="menuIfrm" src="" frameborder="0" scrolling="no"></iframe>
                        </td>
                        <td style="vertical-align:top">
                        	<div id="recTil"><span>员工档案</span></div>
                	        <div class="bottomBar">
                       				批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(20,'员工档案')">批量删除</span>
               				</div>
               				<div id="dataList" class="dataList"></div>
						</td>
					</tr>
				</table>
			</div>
	   </div>
    </div>
</body>
</html>
