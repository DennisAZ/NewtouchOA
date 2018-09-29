<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>收到的工作</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crn/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script type="text/javascript">
		function loadFilter(){
			setCurItemStyle($("mark").value,["0","1","2","3"]);
		}
		
		function changeMark(n){
			$("mark").value=n;
			loadList();
		}
		function getUserListHTML(userList){
			var str="<ul>";
			for(var i=0;i<userList.length;i++){
				str+="<li>"+userList[i].getAttribute("userName")+"&nbsp;"
				if(userList[i]!=null){
					switch(userList[i].getAttribute("taskSta")){
						case '0':
							str+="<img style='vertical-align:middle;' src='crm/images/content/doing.gif' alt='执行中'/>";
							break;
						case '1':
							str+="<img style='vertical-align:middle;' src='crm/images/content/tofinish.gif' alt='已提交'/>";
							break;
						case '2':
							str+="<img style='vertical-align:middle;' src='crm/images/content/alert.gif' alt='被退回'/>";
							break;
						case '3':
							str+="<img style='vertical-align:middle;' src='crm/images/content/finish.gif' alt='已确认提交'/>";
							break;
					}
					str+="</li>";
				}	
			}
			str+="</ul>";
			return str;
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var dblFunc="descPop('salTaskAction.do?op=salTaskDesc&stId="+obj.salTask.stId+"')";
			var taskName="[";
			if(obj.salTask.stLev == '0'){
				taskName +="<span class='deepGray'>低</span>";
			}
			else if(obj.salTask.stLev == '1'){
				taskName +="<span class='orange'>中</span>";
			}
			else if(obj.salTask.stLev == '2'){
				taskName +="<span class='red'>高</span>";
			}
			taskName +="]<a href='salTaskAction.do?op=salTaskDesc&stId="+obj.salTask.stId+"' style='cursor:pointer' target='_blank'>"+obj.salTask.stTitle+"</a>";
			var startTime="";
			if(obj.salTask.stStartDate != undefined){
				startTime= obj.salTask.stStartDate.substring(0,10); 
			}
			else{
				startTime= "<span class='gray'>未设定</span>";
			}
			var endTime = "";
			if(obj.salTask.stFinDate != undefined){
				endTime = obj.salTask.stFinDate.substring(0,10);
			}
			else{
				endTime= "<span class='gray'>未设定</span>";
			}
			var taskUser="<a href=\"javascript:void(0)\" onClick=\"getUserDiv('salTaskAction.do?op=getTaskUsers&taskId="+obj.salTask.stId+"','taskUsers')\">查看执行情况</a>";
			var state = "";
			switch(obj.salTask.stStu){
				case '0': state="<img class='imgAlign' src='crm/images/content/time.gif' alt='执行中'/>"; break;
				case '1': state="<img class='imgAlign' src='crm/images/content/suc.gif' alt='已完成'/>"; break;
				case '2': state="<img class='imgAlign' src='crm/images/content/fail.gif' alt='已取消'/>"; break;
			}
			var funcCol = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\"><img style='cursor:pointer' src='crm/images/content/detail.gif' border='0' alt='查看详细'/></a>";
			datas = [state, obj.salTask.salTaskType?obj.salTask.salTaskType.typName:"", taskName, startTime, endTime, taskUser, obj.salTask?obj.salTask.stName:"", obj.salTask?obj.salTask.stRelDate:"" ,funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "salTaskAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "myTaskSearch";
			pars.tip = "${tip}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态",width:"5%"},
				{name:"类别"},
				{name:"[优先级]工作名称",align:"left"},
				{name:"工作时间"},
				{name:"结束时间"},
				{name:"执行情况",isSort:false},
				{name:"发布人"},
				{name:"发布时间",renderer:"time"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
			loadFilter();
		}
		
    	var gridEl = new MGrid("mySalTaskTab","dataList");
		createProgressBar();
		window.onload=function(){
			//加载头部标签样式
			loadTabType('0');
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
	</script>
  </head>
  
  <body>
      <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 工作安排</div>
  	     	<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" onClick="self.location.href='salTaskAction.do?op=toListMyTaskSearch'">收到的工作</div> 
                            <div id="tabType2" onClick="self.location.href='salTaskAction.do?op=toListSalTaskSearch&range=1'">我发布的工作</div>  
                        </div>
                    </th>
                    <td><a href="javascript:void(0)" onClick="oaPopDiv(5);return false;" class="newBlueButton">发布工作安排</a></td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
		 	<div id="listContent">     
            	<div class="listSearch">
                	<form id="searchForm" onSubmit="loadList();return false;">
                			<input type="text" id="mark" name="mark" style="display:none"/>
                         工作名称：<input class="inputSize2 inputBoxAlign" type="text" name="stTitle"/>&nbsp;&nbsp;
                       状态：<select id="stStu" name="stStu" class="inputBoxAlign" style="width:90px">
                              <option value="">全部</option>
                              <option value="0">执行中</option>
                              <option value="1">已完成</option>
                              <option value="2">已取消</option>
                          </select>
					    &nbsp;&nbsp;   
                        <input type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                    </form>
                </div>
                <div id="topChoose" class="listTopBox">
                    <a href="javascript:void(0)" onClick="changeMark(0)";>&nbsp;我执行中的工作&nbsp;</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeMark(1)";>&nbsp;我已提交的工作&nbsp;</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeMark(2)";>&nbsp;我被退回的工作&nbsp;</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeMark(3)";>&nbsp;我已确认完成的工作&nbsp;</a>&nbsp;
                </div>	
                <div id="dataList" class="dataList"></div>
       	  	</div>
  		</div> 
	</div>
  </body>
</html>
