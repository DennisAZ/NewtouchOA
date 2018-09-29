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
    <title>客户统计</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/sta.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/FusionCharts.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/sta.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    
	<script language="javascript">
		function dataMapper(obj){
			var datas = [],className,dblFunc,dataId;
			var empId = "", empName = "", tdTxt = "";
			if("${statType}" != 'cusOnDate'){
				for(var i=0; i<obj.length; i++){
					var statName = "";
					if(i==0){
						if(obj[0]==null){ 
							if(obj[2]=="合计"){
								className = "sumTr";
							}
							else{
								obj[2] = "";
								empId = null;
							} 
						}
						else{ empId = obj[0]; }
					}
					if(i>1){
						if(i==2){
							empName=(obj[2]==""?"无":obj[2]);
							datas.push(empName);
						}
						else{
							if(gridEl.colArr[i-2].name!="合计"){ statName = gridEl.colArr[i-2].name; }
							if(parseInt(obj[i])>0){
								tdTxt = "<a href=\"statAction.do?op=toListStatCus&statType=${statType}&statItem="+encodeURIComponent(statName)+"&empId="+empId+"&empName="+encodeURIComponent(empName)+"&nodeIds="+$("nodeIds").value+"\" target='_blank'>"+obj[i]+"</a>";
							}
							else{ tdTxt = "<span class='gray'>"+obj[i]+"</span>"; }
							datas.push(tdTxt);
						}
					}
				}
			}else{
				datas = [obj.cusName,obj.date1,obj.empName];
			}
			
			return [datas,className,dblFunc,dataId];
		}
		function columnCreator(jsonData,colArr){
			var topArr = jsonData.topList;
			for(var i=0; i<topArr.length; i++){
				if(topArr[i]!=null){
					colArr.push({name:topArr[i],isSort:false});
				}
			}
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
			var cols;
			if("${statType}" != 'cusOnDate'){
				cols=[{name:"负责人",isSort:false}];
			}else{
				cols = [
					{name:"客户",isSort:false},
					{name:"到期日期",renderer:"date",isSort:false},
					{name:"所属人",isSort:false}
				];
			}
			gridEl.init(url,pars,cols,loadFunc);
			gridEl.loadData(dataMapper,columnCreator,statCallBack);
			if($("rightTitle").style.display=="none"){
				$("rightTitle").show();
			}
		}
		var gridEl = new MGrid("statCusTab${statType}","dataList");
		gridEl.config.hasPage = false;
		gridEl.config.sortable = false;
		gridEl.config.isShort = false;
		if("${statType}" != 'cusOnDate'){
			gridEl.config.listType = "staTab";
		}
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
                            <div id="tabType1" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="topForward(2)">销售分析</div>
                            <div id="tabType2" class="tabTypeWhite" onClick="topForward(1)">客户分析</div>                       
                        </div>
                     </th>
                </tr>
            </table>
			<div id="listContent">
				<table id="statContainer" class="normal" cellpadding="0" cellspacing="0">
                	<tr>
						<td id="leftTd">
						   <input type="hidden" id="statMenuType" value="cus"/>
                           <input type="hidden" id="statType" value="${statType}"/>
                           <input type="hidden" id="typTxt"/>
                           <script type="text/javascript">createIfmLoad('ifmLoad');</script>
               				<iframe onload="loadAutoH(this,'ifmLoad')" src="salStat/typeList.jsp" frameborder="0" style="height:200px;"></iframe>
						</td>
                        <td id="rightTd">
                        	<div class="staFormTop" id="typTxt_2"></div>
                            <form id="statForm" style="margin:0; padding:5px;">
                                 <input type="hidden" id="opMethod" name="op" />
                                 <table class="dashTab" style="width:90%" cellpadding="0" cellspacing="0">
                					<tbody>
                						
	                						<tr class="noBorderBot">
	                                			<c:if test="${statType != 'cusOnDate'}">
		                                			<th style="width:10%"><a title="点击选择员工" href="javascript:void(0)" onClick="addDivBrow(12,'getNames');return false;">负责人</a>：</th>
		                                			<td colspan="3" style="width:90%">
		                                    			<a id="nodeNames" class="nodeNamesLayer" href="javascript:void(0)" title="点击选择员工" onClick="addDivBrow(12,'getNames');return false;" style="width:100%"></a>
		                                    			<input type="hidden" id="nodeIds" name="nodeIds"/>
		                                			</td>
	                                			</c:if>            				
		                           				<c:if test="${statType == 'cusOnDate'}">
			                                			<th style="width:10%">到期日期：</th>
			                                			<td colspan="4">
			                                            	<input name="startDate" id="startDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate').focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
			                          						到&nbsp;<input name="endDate" id="endDate" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"/>
			                                			</td>
		                           				</c:if>
	                                		</tr>
	                                		<tr class="noBorderBot">	
	                                			<td colspan="4">
	                                				<input class="butSize3 inputBoxAlign" type="button" id="stButton" onClick="loadList()" value="开始统计" />
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
