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
    <title>员工档案</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
     <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    
    <script type="text/javascript" src="js/hr.js"></script>
    <script type="text/javascript">
		//标签跳转
		function tabReload(){
			self.location.href="empAction.do?op=toListEmps&workstate=${workstate}";
		}
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc = "descPop('empAction.do?op=salEmpDesc&seNo="+obj.seNo+"')";
			var seName = "<a href=\"javascript:void\" onclick=\""+dblFunc+";return false;\">"+obj.seName+"</a>";
			var funcCol = "<img onClick=\""+dblFunc+"\" style=\"cursor:pointer\" src=\"images/content/detail.gif\" border=\"0\" alt=\"查看详细\"/>&nbsp;&nbsp;<img onClick=\"descPop('empAction.do?op=toUpdEmp&seNo="+obj.seNo+"')\" style=\"cursor:pointer\" src=\"images/content/edit.gif\" border=\"0\" alt=\"编辑\"/>&nbsp;&nbsp;<img onClick=\"hrDelDiv(1,'"+obj.seNo+"')\" style=\"cursor:pointer\" src=\"images/content/del.gif\" border=\"0\" alt=\"删除\"/>";
			datas = [obj.seCode, seName, obj.seSex, obj.seEdu, obj.seJobDate, obj.limRole?obj.limRole.rolName:"", obj.seType, obj.salOrg?obj.salOrg.soName:"", funcCol ];
			if("${workstate}"=="1"){
				datas.splice(5,0,obj.seEndDate);
			}
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "empAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listEmps";
			pars.workstate="${workstate}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"员工号",align:"left"},
				{name:"员工姓名"},
				{name:"性别"},
				{name:"最高学历"},
				{name:"入职日期",renderer:"date"},
				{name:"职位"},
				{name:"员工类型"},
				{name:"所属部门"},
				{name:"操作",isSort:false}
			];
			if("${workstate}"=="1"){
				cols.splice(5,0,{name:"离职日期",renderer:"date"});
			}
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("empTab${workstate}","dataList");
		createProgressBar();
   		window.onload=function(){
			loadList();
            //增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
    </script>
 </head>
  
  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;人员管理 > <c:if test="${workstate == 0}">在职</c:if><c:if test="${workstate == 1}">离职</c:if>员工</div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                   <th>
                        <div id="tabType">
                             <div id="tabType1" class="tabTypeWhite" onClick="tabReload()"><c:if test="${workstate == 0}">在职</c:if><c:if test="${workstate == 1}">离职</c:if>员工</div>                   
                        </div>
                    </th>
                  	<td>
                        <a href="javascript:void(0)" onClick="descPop('empAction.do?op=toNewSalEmp');return false;" class="newBlueButton">添加员工</a>
                  	</td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
			<div id="listContent">
            	<div class="listSearch">
                    <form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                         员工号：<input style="width:100px" class="inputSize2 inputBoxAlign" type="text" name="seCode"/>&nbsp;&nbsp;
                         姓名：<input type="text" name="seName" style="width:100px" class="inputSize2 inputBoxAlign"/>&nbsp;&nbsp;
                         <input  type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                    </form>
              	</div>
          
               	<div id="dataList" class="dataList"></div> 
			</div>
        </div>
  	</div>
	</body>
</html>
