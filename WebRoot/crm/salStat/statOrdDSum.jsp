<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>销售月度统计</title>   
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
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="js/sta.js"></script>
    
	<script language="javascript">
		function check(){
			var errStr = "";
			if(isEmpty("startMon")){
				errStr+="未选择起始月份！\n";
			}
			if(isEmpty("endMon")){
				errStr+="未选择结束月份！";
			}
			if(errStr!=""){
				alert(errStr);
				return false;
			}
			else{
				loadList();
			}
	   	}
		function reCreateSumRow(sumValues){
			sumValues[0] = "<a href=\"statAction.do?op=toListStatPaid&statType=${statType}\" target='_blank'>￥"+changeTwoDecimal(sumValues[0])+"</a>";
			sumValues[1] = "<a href=\"statAction.do?op=toListStatOrd&statType=${statType}\" target='_blank'>￥"+changeTwoDecimal(sumValues[1])+"</a>";
		}
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var statPaidSum, statOrdSum;
			var dataUrl_1="statAction.do?op=toListStatPaid&statType=${statType}&statItem="+encodeURIComponent(obj.month);
			var dataUrl_2="statAction.do?op=toListStatOrd&statType=${statType}&statItem="+encodeURIComponent(obj.month);
			if(parseFloat(obj.paidSum)>0){
				statPaidSum = "<a href=\""+dataUrl_1+"\" target='_blank'>￥"+changeTwoDecimal(obj.paidSum)+"</a>";
			}
			else{
				statPaidSum = "<span class='gray'>￥0.00</span>";
			}
			if(parseFloat(obj.ordSum)>0){
				statOrdSum = "<a href=\""+dataUrl_2+"\" target='_blank'>￥"+changeTwoDecimal(obj.ordSum)+"</a>";
			}
			else{
				statOrdSum = "<span class='gray'>￥0.00</span>";
			}
			datas = [obj.month, [statPaidSum,obj.paidSum], [statOrdSum,obj.ordSum]];
			return [datas,className,dblFunc,dataId];
		}
		function statCallBack(){
			if(gridEl.dataXML != undefined && gridEl.dataXML != ""){
				$("chartType").show();
				$("chartDiv").show();
				changeChar(10);
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
				{name:"统计月份",isSort:false},
				{name:"回款金额",isSort:false},
				{name:"订单金额",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc);
			gridEl.loadData(dataMapper,"",statCallBack);
			if($("rightTitle").style.display=="none"){
				$("rightTitle").show();
			}
		}
		var gridEl = new MGrid("statOrdDSumTab${statType}","dataList");
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
                                <input type="hidden" name="op" id="opMethod" />
                                 <table class="dashTab" style="width:90%" cellpadding="0" cellspacing="0">
                					<tbody>
                						<tr class="noBorderBot">
                                			<th style="width:10%">统计月份：</th>
                                			<td colspan="3" style="width:90%">
                                            	<input name="startMon" value="${startMon}" id="startMon" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('endMon');WdatePicker({skin:'default',dateFmt:'yyyy-MM',onpicked:function(){pid1.focus();}})"/>
                          到&nbsp;<input name="endMon" value="${endMon}" id="endMon" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({skin:'default',dateFmt:'yyyy-MM'})"/>&nbsp;&nbsp;&nbsp;&nbsp;<input class="butSize3 inputBoxAlign" type="button" id="stButton" onClick="check()" value="开始统计" />
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
