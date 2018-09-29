<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count2=0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>订单统计(类别、来源)</title>   
    <link rel="shortcut icon" href="favicon.ico"/>  
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="stylesheet" type="text/css" href="css/sta.css"/>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/FusionCharts.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/sta.js"></script>
    
	<script language="javascript">
		function reCreateSumRow(sumValues){
			sumValues[0] = "<a href=\"statAction.do?op=toListStatOrd&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"&statType=${statType}\" target='_blank'>"+sumValues[0]+"</a>";
		}
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var statCount;
			var dataUrl="statAction.do?op=toListStatOrd&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"&statType=${statType}&statItem="+encodeURIComponent(obj.typName);
			if(parseInt(obj.count)>0){
				statCount = "<a href=\""+dataUrl+"\" target='_blank'>"+obj.count+"</a>";
			}
			else{
				statCount = "<span class='gray'>"+obj.count+"</span>";
			}
			datas = [obj.typName, [statCount,obj.count], [obj.sum,obj.sum]];
			return [datas,className,dblFunc,dataId];
		}
		function statCallBack(){
			if(gridEl.dataXML != undefined && gridEl.dataXML != ""){
				$("chartType").show();
				$("chartDiv").show();
				changeChar(20);
			}
			else{
				$("chartType").hide();
				$("chartDiv").hide();
			}
		}
		function changeChar(n){
			renderCharByXML(n,gridEl.dataXML,'chartDiv');
		}
		function loadList(){
			var url = "statAction.do";
			var pars = $("statForm").serialize(true);
			var loadFunc = "loadList";
			var cols=[
				{name:"",isSort:false},
				{name:"数量",isSort:false},
				{name:"金额",renderer:"money",isSort:false}
			];
			switch("${statType}"){
				case "ordType":
					cols[0].name="类别";
					break;
				case "ordSou":
					cols[0].name="来源";
					break;
			}
			gridEl.init(url,pars,cols,loadFunc);
			gridEl.loadData(dataMapper,"",statCallBack);
			if($("rightTitle").style.display=="none"){
				$("rightTitle").show();
			}
		}
		var gridEl = new MGrid("statOrdSimTab${statType}","dataList");
		gridEl.config.hasPage = false;
		gridEl.config.sortable = false;
		gridEl.config.isShort = false;
		gridEl.config.listType = "simpleStat";
		gridEl.config.hasSumRow = true;
		gridEl.config.sumRowCallBack = reCreateSumRow;
		createProgressBar();
		window.onload=function(){
			initStatPage();
			closeProgressBar();
		}
     </script>
</head>
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;统计报表 > <span id="typTxt_1"></span> </div>
			 <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="topForward(1)">客户分析</div>
                            <div id="tabType2" class="tabTypeWhite" onClick="topForward(2)">销售分析</div>
                        </div>
                    </th>
                </tr>
            </table>
			<div id="listContent">
				<table id="statContainer" class="normal" cellpadding="0" cellspacing="0">
                	<tr>
						<td id="leftTd">
                        	<input type="hidden" id="statMenuType" value="ord"/>
                       		<input type="hidden" id="statType" value="${statType}"/>
                       		<input type="hidden" id="typTxt"/>
                          	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
               				<iframe onload="loadAutoH(this,'ifmLoad')" src="salStat/typeList.jsp" frameborder="0" style="height:260px;"></iframe>
						</td>
                        <td id="rightTd">
                        	<div class="staFormTop" id="typTxt_2"></div>
                            <form id="statForm" style="margin:0; padding:5px;">
                            	<input type="hidden" id="opMethod" name="op" />
                                <table class="dashTab" style="width:90%" cellpadding="0" cellspacing="0">
                					<tbody>
                						<tr class="noBorderBot">
                                			<th style="width:10%">签订日期：</th>
                                			<td colspan="3" style="width:90%">
                                            	<input name="startDate" id="startDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate').focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                          到&nbsp;<input name="endDate" id="endDate" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"/>&nbsp;&nbsp;&nbsp;&nbsp;<input class="butSize3 inputBoxAlign" type="button" id="stButton" onClick="loadList()" value="开始统计" />
                                			</td>
                                		</tr>
                           			</tbody>
                           		</table>
                            </form>
                            <div id="rightTitle" class="rightTitle" style="padding:10px; display:none">数据统计结果</div>
                            <div id="dataList" class="dataList"></div>
                    	</td> 
                    </tr>
                </table>
               	<div id="chartType" class="disType" style="display:none">
                    <span class="bold">显示类型:</span>
                    <a href="javascript:void(0)" onClick="changeChar(21);return false;">2D双Y轴柱图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(20);return false;">3D双Y轴柱图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(9);return false;">2D柱图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(10);return false;">3D柱图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(11);return false;">折线图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(12);return false;">面积图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(13);return false;">2D条形图</a>&nbsp;
                    <a href="javascript:void(0)" onClick="changeChar(14);return false;">3D条形图</a>&nbsp;
                </div>
                <div id="chartDiv" class="chartDiv" style="display:none"></div>
        	</div>
  		</div> 
	 </div>
  </body>
</html>
