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
    <title>工作安排</title>
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
			dataId = obj.stId;
			var dblFunc="descPop('salTaskAction.do?op=salTaskDesc&stId="+obj.stId+"')";
	        var state ="";
			switch(obj.stStu){
                case '0': state = "<img class='imgAlign' src='images/content/time.gif' alt='执行中'/>"; break;
                case '1': state = "<img class='imgAlign' src='images/content/suc.gif' alt='已完成'/>"; break;
                case '2': state = "<img class='imgAlign' src='images/content/fail.gif' alt='已取消'/>"; break;
            }
           	var taskName = "[";
           	if(obj.stLev =='0'){
           		taskName = "<span class='deepGray'>低</span>";
           	}
           	if(obj.stLev =='1'){
           		taskName ="<span class='orange'>中</span>";
           	}
           	if(obj.stLev =='2'){
           		taskName ="<span class='red'>高</span>";
           	}
           	taskName +="]<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.stTitle+"</a>";
			var taskTime="";
            if(obj.stStartDate != undefined || obj.stFinDate != undefined){
            	if(obj.stStartDate != undefined){
            		taskTime =obj.stStartDate.substring(0,10);
            	}
            	taskTime +="&gt;";
            	if(obj.stFinDate != undefined){
            		taskTime += obj.stFinDate.substring(0,10); 
            	}
            }
            else{
                taskTime="<span class='gray'>未设定工作时间</span>";
            }
			var funcCol = "<img onClick=\"recAddDiv(15,'rec','"+obj.stId+"')\" style='cursor:pointer' src='images/content/restore.gif' alt='恢复'/>&nbsp;&nbsp;<img onClick=\"recAddDiv(15,'del','"+obj.stId+"')\" style='cursor:pointer' src='images/content/del.gif' alt='彻底删除'/>";			
			datas = [state, obj.salTaskType?obj.salTaskType.typName:"", taskName, taskTime, obj.stName, obj.stRelDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "salTaskAction.do";
			var pars = [];
			pars.op = "findDelSalTask";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"类别"},
				{name:"[优先级]工作名称",align:"left"},
				{name:"工作时间"},
				{name:"发布人"},
				{name:"发布日期",renderer:"date"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("recTaskListTab","dataList");
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
                        	<input type="hidden" id="typeId" value="22"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" id="menuIfrm" src="" frameborder="0" scrolling="no"></iframe>
                        </td>
                        <td style="vertical-align:top">
                        	<div id="recTil"><span>工作安排</span></div>
                        	<div class="bottomBar">
                               	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(19,'工作安排')">批量删除</span>
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
