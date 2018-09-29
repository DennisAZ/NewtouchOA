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
    <title>销售机会列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
		//传入ajax的回调方法
		/*function toLoadTab(){
			//加载头部标签样式
			loadListTab('${range}',['0','1']);
		}*/
		
		function chooseCus(){
			addDivBrow(2);
		}
		//标签跳转
		function tabReload(){
			self.location.href="cusServAction.do?op=toListSalOpps&range=${range}";
		}
	 	
	 	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.oppId;
	        var dblFunc="descPop('cusServAction.do?op=showSalOppInfo&id="+dataId+"')";
			var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var oppTil="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.oppTitle+"</a>";
			var oppCus="<span class='mLink'><a href='javascript:void(0)' onclick=\""+relFunc+"\"><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a></span>";
			var oppExeDate = "";
			if(obj.oppExeDate !=undefined){
				oppExeDate = obj.oppExeDate.substring(0,10);
			}
			else{
				oppExeDate = "<span class='gray'>未设定</span>";
			}
			var oppPos="";
			if(obj.oppPossible != undefined){
				oppPos=obj.oppPossible+"%";
			}
			var oppDCount="";
			if(obj.oppState =="跟踪"){
				oppDCount=obj.oppDayCount+"天";
			}
			var oppStage = "";
			if(obj.oppStage != undefined){
				oppStage = obj.oppStage.typName;
			}
			var funcCol = "<img src='crm/images/content/detail.gif' class='hand' onClick=\""+dblFunc+"\"alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"cusPopDiv(31,"+dataId+")\" style='cursor:pointer' src='crm/images/content/edit.gif' border='0' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(3,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [obj.oppState, oppTil, obj.oppLev, oppCus, oppExeDate,obj.oppFindDate,oppStage ,oppPos, oppDCount, obj.salEmp1?obj.salEmp1.seName:"", funcCol];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listSalOpps";
			pars.range="${range}";
			var sortFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"机会主题",align:"left"},
				{name:"机会热度"},
				{name:"对应客户",align:"left"},
				{name:"关注日期"},
				{name:"发现日期",renderer:"date"},
				{name:"阶段"},
				{name:"可能性"},
				{name:"停留阶段",isSort:false},
				{name:"负责人"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("oppListTab","dataList");
    	//gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5,'searButton','after');
			//loadIfmSrc("oppIfm","cusServAction.do?op=getOppByExeDate&range=${range}");
		}
		

  </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;客户管理 > 销售机会</div>
            <input type="hidden" id="today" value="${TODAY}"/>
            <input type="hidden" id="tomorrow" value="${TMR}"/>
            <input type="hidden" id="afTom" value="${AF_TMR}"/>
            <input type="hidden" id="yesterday" value="${YDAY}"/>
            <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" onClick="tabReload()">销售机会</div>
                   	</div>
                    </th>
                    <td>
                    <a href="javascript:void(0)" onClick="cusPopDiv(3);return false;" class="newBlueButton">新建销售机会</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
            	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;" >
                        主题：<input class="inputSize2 inputBoxAlign" type="text" name="oppTil" onBlur="autoShort(this,300)"/>&nbsp;
                        对应客户：<input id="cusId" name="cusName" class="inputSize2 inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" style="width:142px" type="text" readonly ondblClick="clearInput(this,'cusCode')" />&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;
                        机会热度：<select class="inputBoxAlign " id="oppLev" name="oppLev">
                            <option value="">请选择</option>
                            <option value="低热度">低热度</option>
                            <option value="中热度">中热度</option>
                            <option value="高热度">高热度</option>
                        </select>&nbsp;
                        机会状态：<select class="inputBoxAlign " id="oppState" name="oppState">
                            <option value="">请选择</option>
                            <option value="成功">成功</option>
                            <option value="跟踪">跟踪</option>
                            <option value="搁置">搁置</option>
                            <option value="失效">失效</option>
                        </select>&nbsp;
                        <input id="searButton" class="inputBoxAlign butSize3" type="submit" value="查询"/>&nbsp;&nbsp;
                         <!--<a class="supSearButton inputBoxAlign" href="customAction.do?op=toCusSupSearch&range=${range}" target="_blank" style="float:none">高级查询</a>-->
                    </form>
                </div>
                <!--<div class="bottomBar">
                   <span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(3,'销售机会')">批量删除</span>
                </div> -->
                <div id="dataList" class="dataList"></div> 
            </div>
        </div>
  	</div>
  </body>
  
</html>
