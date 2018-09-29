<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>所有的日程安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	.outSch {
			color:#999;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script type="text/javascript">
		function loadFilter(){
			setCuritemStyle($("filter").value,["expired"]);
		}
		//列表筛选按钮链接
		function filterList(filter){
			$("filter").value=filter;
			loadList();
		}
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var schState ="";
			if(obj.schState == '已完成'){
				schState += "<span><img class='imgAlign' src='images/content/suc.gif' alt='已完成'/></span>";
			}
			if(obj.schState == '未完成'){
				schState +="<span id='y"+obj.schId+"' style='display:none'><img class='imgAlign' src='images/content/suc.gif' alt='已完成'/></span><span id='"+obj.schId+"'><img class='imgAlign' src='images/content/execute.gif' border='0' style='cursor:pointer' onClick=\"modifySchState('"+obj.schId+"')\" alt='点击完成'/></span>";
			}
			var schTitle = "<a style='cursor:pointer' href='javascript:void(0)' onClick='oaPopDiv(32,"+obj.schId+");return false;'>"+obj.schTitle+"</a>";
			var schTime = obj.schStartTime+"&nbsp;-&nbsp;"+obj.schEndTime;
			var funcCol = "<a href=\"javascript:void(0)\" onClick=\"oaPopDiv(32,"+obj.schId+");return false;\"><img src=\"images/content/detail.gif\" border=\"0\" alt=\"查看详细\"/></a>&nbsp;&nbsp;<img onClick=\"oaPopDiv(31,"+obj.schId+")\" style=\"cursor:pointer\" src=\"images/content/edit.gif\" border=\"0\" alt=\"编辑\"/>&nbsp;&nbsp;<img onClick=\"oaDelDiv(2,"+obj.schId+")\" style=\"cursor:pointer\" src=\"images/content/del.gif\" border=\"0\" alt=\"删除\"/>";
			datas = [schState, obj.schType?obj.schType.typName:"", schTitle, obj.schStartDate, schTime, funcCol];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "showSchList";
			pars.listType="${listType}";
			
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
			loadFilter();
		}
		
    	var gridEl = new MGrid("mySchTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
			//loadIfmSrc("lastSchsIfm","messageAction.do?op=getSchedule");
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
	  	
	 </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 我的日程
            </div>
            <input type="hidden" id="today" value="${TODAY}"/>
            <input type="hidden" id="tomorrow" value="${TMR}"/>
            <input type="hidden" id="afTom" value="${AF_TMR}"/>
            <input type="hidden" id="yesterday" value="${YDAY}"/>
            <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toShowSchList'">我的日程</div>
                        </div>
                     </th> 
                     <td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(3);return false;" class="newBlueButton">新建日程</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                <!--<div>
                    <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                    <iframe id="lastSchsIfm" onload="loadEnd('ifmLoad')" src="" frameborder="0" scrolling="no" style=" width:100%;height:210px"></iframe>
                </div>-->
                <div class="listSearch">
                	<form id="searchForm" onSubmit="loadList();return false;" >
                    	<input type="text" id="filter" name="filter" style="display:none"/>
                       	日期：<input onFocus="var endTime=$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}'})" ondblClick="clearInput(this)" readonly style="width:100px; cursor:pointer" class="inputSize2 inputBoxAlign Wdate" type="text" name="searDate1" id="startTime">&nbsp;到&nbsp;<input onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})" readonly style="width:100px; cursor:pointer" class="inputSize2 inputBoxAlign Wdate" type="text" name="searDate2" id="endTime" ondblClick="clearInput(this)">&nbsp;&nbsp;
                        <input class="butSize3 inputBoxAlign"  type="submit" value="查询" />&nbsp;&nbsp;
                    </form>
                </div>
                <div id="topChoose" class="listTopBox">
                    <a href="javascript:void(0)" onClick="filterList('expired');return false;">&nbsp;过期的未办日程&nbsp;</a>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
</html>
