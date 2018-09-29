<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>订单统计(双列)(销售额人员月度)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="StyleSheet" href="css/dtree.css" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/sta.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/FusionCharts.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
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
		
		function dataMapper(obj){
			var datas = [],className,dblFunc,dataId;
			var empId = "", empName = "", tdTxt = "";
			for(var i=0; i<obj.length; i++){
				var statName = "";
				if(i==0){
					if(obj[i]==null){ className = "sumTr"; }
					else{ empId = obj[i]; }
				}
				if(i>0){
					if(i==1){
						datas.push(obj[i]);
						empName=obj[i];
					}
					else{
						statName = gridEl.tempColArr[i];
						if(i%2!=0){
							tdTxt = obj[i];
						}
						else{
							if(parseInt(obj[i])>0){
								tdTxt = "<a href=\"statAction.do?op=toListStatOrd&statType=${statType}&statItem="+encodeURIComponent(statName)+"&empId="+empId+"&empName="+encodeURIComponent(empName)+"&nodeIds="+$("nodeIds").value+"\" target='_blank'>"+obj[i]+"</a>";
							}
							else{ tdTxt = "<span class='gray'>"+obj[i]+"</span>"; }
						}
						datas.push(tdTxt);
					}
				}
			}
			return [datas,className,dblFunc,dataId];
		}
		function columnCreator(jsonData,colArr){
			var topArr = jsonData.topList;
			gridEl.tempColArr = jsonData.topList;//临时保存列头
			var thWidth = 100/(topArr.length-1);
			var dateCol = [];
			dateCol.push("<tr><th rowspan='2' style='height:50px; width:"+thWidth+"%;'>负责人</th>");
			for(var i=0; i<topArr.length; i++){
				if(topArr[i]!=null){
					if(i%2==0){
						dateCol.push("<th colspan='2'>"+topArr[i]+"</th>");
					}
				}
			}
			dateCol.push("</tr><tr>");
			colArr.push({html:dateCol.join("")});
			for(var j=0; j<topArr.length; j++){
				if(topArr[j]!=null){
					if(j%2==0){
						colArr.push({html:"<th style='width:"+thWidth+"%;'>数量</th>"});
					}
					else{
						colArr.push({html:"<th style='width:"+thWidth+"%;'>金额</th>",renderer:"money"});
					}
				}
			}
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
			var cols=[];
			gridEl.init(url,pars,cols,loadFunc);
			gridEl.loadData(dataMapper,columnCreator,statCallBack);
			if($("rightTitle").style.display=="none"){
				$("rightTitle").show();
			}
		}
		var gridEl = new MGrid("statOrdDColTab${statType}","dataList");
		gridEl.config.hasPage = false;
		gridEl.config.sortable = false;
		gridEl.config.isShort = false;
		gridEl.config.isResize = false;
		gridEl.config.listType = "staTab";
		gridEl.config.className = "noBr";
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
                                    	<tr>
                                			<th style="width:10%">签订月度：</th>
                                			<td colspan="3" style="width:90%">
                                            	<input name="startMon" value="${startMon}" id="startMon" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('endMon');WdatePicker({skin:'default',dateFmt:'yyyy-MM',onpicked:function(){pid1.focus();}})"/>
                          到&nbsp;<input name="endMon" value="${endMon}" id="endMon" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({skin:'default',dateFmt:'yyyy-MM'})"/>&nbsp;&nbsp;
                                			</td>
                                		</tr>
                						<tr class="noBorderBot">
                                			<th style="width:10%"><a title="点击选择员工" href="javascript:void(0)" onClick="addDivBrow(12,'getNames');return false;">签单人</a>：</th>
                                			<td colspan="3" style="width:90%">
                                    			<a id="nodeNames" class="nodeNamesLayer" href="javascript:void(0)" title="点击选择员工" onClick="addDivBrow(12,'getNames');return false;" style="width:100%"></a>
                                    			<input type="hidden" id="nodeIds" name="nodeIds"/>
                                			</td>
                                		</tr>
                                		<tr class="noBorderBot">	
                                			<td colspan="4">
                                				<input class="butSize3 inputBoxAlign" type="button" id="stButton" onClick="check()" value="开始统计" />
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
