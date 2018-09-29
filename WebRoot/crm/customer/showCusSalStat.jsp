<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>客户销售分析</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/sta.css"/>
    <style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript" src="crm/js/FusionCharts.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
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
			var dataUrl_1="statAction.do?op=toListStatPaid&statType=${statType}&cusId=${cusId}&statitem="+encodeURIComponent(obj.month);
			var dataUrl_2="statAction.do?op=toListStatOrd&statType=${statType}&cusId=${cusId}&statitem="+encodeURIComponent(obj.month);
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
			var url = "customAction.do";
			var pars = $("statForm").serialize(true);
			var loadFunc = "loadList";
			var cols=[
				{name:"统计月份",isSort:false},
				{name:"回款金额",isSort:false},
				{name:"订单金额",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc);
			gridEl.loadData(dataMapper,"",statCallBack);
		}
		var gridEl = new MGrid("cusSalStat","dataList");
		gridEl.config.hasPage = false;
		gridEl.config.sortable = false;
		gridEl.config.isShort = false;
		gridEl.config.listType = "simpleStat";
		gridEl.config.hasSumRow = true;
		gridEl.config.sumRowCallBack = reCreateSumRow;
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
        <table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
        	<tr>
                <th>销售分析</th>
            </tr>
        </table>
        <form id="statForm" style="margin:0; padding:5px;">
            <input type="hidden" name="op" id="opMethod" value="cusSalStat" />
            <input type="hidden" name="cusId" value="${cusId}" />
             <table class="dashTab" style="width:95%" cellpadding="0" cellspacing="0">
                <tbody>
                    <tr class="noBorderBot">
                        <th style="width:10%">统计月份：</th>
                        <td colspan="3" style="width:90%">
                            <input name="startMon" id="startMon" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('endMon');WdatePicker({skin:'default',dateFmt:'yyyy-MM',onpicked:function(){pid1.focus();}})"/>
      到&nbsp;<input name="endMon" id="endMon" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({skin:'default',dateFmt:'yyyy-MM'})"/>&nbsp;&nbsp;&nbsp;&nbsp;<input class="butSize3 inputBoxAlign" type="button" id="stButton" onClick="check()" value="开始统计" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
        <div id="dataList" class="dataList" style="height:auto"></div>
        <div id="chartType" class="disType" style="display:none">
            <span class="bold">显示类型:</span>
            <a href="javascript:void(0)" onClick="changeChar(9);return false;">2D柱图</a>&nbsp;
            <a href="javascript:void(0)" onClick="changeChar(10);return false;">3D柱图</a>&nbsp;
            <a href="javascript:void(0)" onClick="changeChar(11);return false;">折线图</a>&nbsp;
            <a href="javascript:void(0)" onClick="changeChar(12);return false;">面积图</a>&nbsp;
            <a href="javascript:void(0)" onClick="changeChar(13);return false;">2D条形图</a>&nbsp;
            <a href="javascript:void(0)" onClick="changeChar(14);return false;">3D条形图</a>&nbsp;
        </div>
        <div id="chartDiv" class="chartDiv" style=" width:98%;display:none"></div>
   </div>
  </body>

</html>
