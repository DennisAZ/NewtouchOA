<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count2=0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>销售统计</title>   
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
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    
	<script language="javascript">
		//选择产品
		function chooseProd(){
			addDivBrow(20);
		}
		
		function reCreateSumRow(sumValues){
			sumValues[0] = "<a href=\"statAction.do?op=toListStatOrd&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"&statType=${statType}\" target='_blank'>"+sumValues[0]+"</a>";
		}
		function dataMapper(obj){
			var datas = [] ,className,dblFunc,dataId;
			var statCount;
			/*var dataUrl="statAction.do?op=toListStatOrd&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"&statType=${statType}&statItem="+encodeURIComponent(obj.typName);
			if(parseInt(obj.count)>0){
				statCount = "<a href=\""+dataUrl+"\" target='_blank'>"+obj.count+"</a>";
			}
			else{
				statCount = "<span class='gray'>"+obj.count+"</span>";
			}*/
			//datas = [obj.cusName, obj.empName, obj.monSum1, obj.monSum2, obj.monSum3];
			switch("${statType}"){
				case "salM":
					datas = [obj.cusName, obj.empName, [obj.monSum1,obj.monSum1], [obj.monSum2,obj.monSum2], [obj.monSum3,obj.monSum3]];
					break;
				case "lowestSals":
					datas = [obj.cusName, obj.empName, obj.monSum1, obj.monSum2];
					break;
				case "salBack":
					datas = [obj.cusName, obj.empName, [obj.monSum1,obj.monSum1]];
					break;
				case "spsAnalyse":
					var dblFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusId+"')";
					var corName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.cusName+"</a>";
					var txt = "<a href=\"statAction.do?op=toListStatSalPast&cusId="+obj.cusId+"&cusName="+encodeURIComponent(obj.cusName)+"&startDate="+$("startDate1").value+"&endDate="+$("endDate1").value+"\"  target='_blank'>"+ obj.monSum1+"</a>";
					datas = [corName,txt, [obj.monSum2,obj.monSum2],[obj.monSum5,obj.monSum5], [obj.monSum3,obj.monSum3], [obj.monSum4,obj.monSum4]];
					break;
				case "psalBack":
					var dataUrl = "<a href=\"statAction.do?op=toListStatPsalBack&empId="+obj.cusId+"&empName="+encodeURIComponent(obj.empName)+"&startDate="+$("startDate1").value+"&endDate="+$("endDate1").value+"&wprId="+$("wprId").value+"\"  target='_blank'>"+ obj.monSum1+"</a>";
					datas = [obj.empName, dataUrl];
					break;
				case "proAnalyse":
					var avg = 0;
					if(obj.monSum1 != 0){
						avg = obj.monSum3 / obj.monSum1;
					}
					datas = [obj.cusName, obj.empName, obj.monSum1, obj.monSum2, obj.monSum3 ,avg];
					break;
				case "recvAnalyse":
					datas = [obj.corName, obj.corRecvAmt, obj.corRecDate];
					break;
				case "saleAnalyse":
					var cusName = "", tdTxt = "",cusId = "";
					
					for(var i=0; i<obj.length; i++){
						var statName = "";
						if(i==0){
							if(obj[0]==null){
								className = "sumTr";
								obj[1] = "";
								cusId = null;
							}
							else{ cusId = obj[0]; }
						}
						if(i>0){
							if(i==1){
								cusName=(obj[1]==""?"合计":obj[1]);
								datas.push(cusName);
							}
							if(i > 2){
								if(parseInt(obj[i])>0){
									if(cusId == null){
										tdTxt = "<a href=\"statAction.do?op=toListStatProd&cusId=&cusName="+encodeURIComponent(cusName)+"&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"\" target='_blank'>"+obj[i]+"</a>";
									}else{
										tdTxt = "<a href=\"statAction.do?op=toListStatProd&cusId="+cusId+"&cusName="+encodeURIComponent(cusName)+"&startDate="+$("startDate").value+"&endDate="+$("endDate").value+"\" target='_blank'>"+obj[i]+"</a>";
									}
								}
								else{ tdTxt = "<span class='gray'>"+obj[i]+"</span>"; }
								datas.push(tdTxt);
							}
						}
					}
				  break;
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
		function loadList(sortCol,isDe){
			var url = "statAction.do";
			var pars = $("statForm").serialize(true);
			var loadFunc = "loadList";
			var cols=[];
			var sortFunc = "loadList";
			
			switch("${statType}"){
				case "salM":
					cols = [
						{name:"客户名称",isSort:false},
						{name:"业务员",isSort:false},
						{name:"订单额",renderer:"money",isSort:false},
						{name:"发货额",renderer:"money",isSort:false},
						{name:"回款额",renderer:"money",isSort:false}
					];
					break;
				case "lowestSals":
					cols = [
						{name:"客户名称",isSort:false},
						{name:"业务员",isSort:false},
						{name:"最低销售额",renderer:"money",isSort:false},
						{name:"实际月平均发货额",renderer:"money",isSort:false}
					];
					break;
				case "salBack":
					cols = [
						{name:"客户名称",isSort:false},
						{name:"业务员",isSort:false},
						{name:"提成金额",renderer:"money",isSort:false}
					];
					break;
				case "spsAnalyse":
					cols = [
						{name:"客户名称",isSort:false},
						{name:"回款金额",isSort:false},
						{name:"应收发货余额",renderer:"money",isSort:false},
						{name:"应收开票余额",renderer:"money",isSort:false},
						{name:"最高余额",renderer:"money",isSort:false},
						{name:"期初余额",renderer:"money",isSort:false}
					];
					break;	
				case "psalBack":
					cols = [
						{name:"员工",isSort:false},
						{name:"提成金额",isSort:false}
					];
					break;
				case "proAnalyse":
					cols = [
						{name:"销售客户",isSort:false},
						{name:"销售业务员",isSort:false},
						{name:"销售数量",isSort:false},
						{name:"销售价格",isSort:false,renderer:"money"},
						{name:"外购数量",isSort:false},
						{name:"比例",isSort:false,renderer:"float2"}
					];
					break;
				case "recvAnalyse":
					cols = [
						{name:"客户",isSort:false},
						{name:"应收款",renderer:"money"},
						{name:"收款日期",renderer:"date"}
					];
					break;
				case "saleAnalyse":
					cols = [
						{name:"客户",isSort:false}
					];
					break;
			}
			if("${statType}" != 'recvAnalyse'){
				gridEl.init(url,pars,cols,loadFunc);
			}else{
				gridEl.init(url,pars,cols,sortFunc,sortCol,isDe);
			}
			
			if("${statType}" == 'saleAnalyse' ){
				gridEl.loadData(dataMapper,columnCreator,statCallBack);
			}else{
				gridEl.loadData(dataMapper,"",statCallBack);
			}
			
			if($("rightTitle").style.display=="none"){
				$("rightTitle").show();
			}
		}
		var gridEl = new MGrid("statSalTab${statType}","dataList");
		if("${statType}" != 'recvAnalyse' ){
			gridEl.config.sortable = false;
			gridEl.config.isShort = false;
		}
		gridEl.config.hasPage = false;
		if("${statType}" == 'saleAnalyse'){
			gridEl.config.listType = "staTab";
		}else{
			gridEl.config.listType = "simpleStat";
		}
		if("${statType}" == 'spsAnalyse' || "${statType}" == 'psalBack' || "${statType}" == 'salM'|| "${statType}" == 'salBack'){
			gridEl.config.hasSumRow=true;
			//gridEl.config.sumRowCallBack = reCreateSumRow;
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
                            <div id="tabType1" class="tabTypeWhite" onClick="topForward(2)">销售分析</div>
                            <div id="tabType2" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="topForward(1)">客户分析</div>  
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
               				<iframe onload="loadAutoH(this,'ifmLoad')" src="salStat/typeList.jsp" frameborder="0" style="height:300px;"></iframe>
						</td>
                        <td id="rightTd">
                        	<div class="staFormTop" id="typTxt_2"></div>
                            <form id="statForm" style="margin:0; padding:5px;">
                            	<input type="hidden" id="opMethod" name="op" />
                                <table class="dashTab" style="width:90%" cellpadding="0" cellspacing="0">
                					<tbody>
                					<c:if test="${statType =='salM' || statType =='lowestSals' || statType =='salBack'}">
                						<tr>
                                			<th style="width:10%"><a title="点击选择员工" href="javascript:void(0)" onClick="addDivBrow(12,'getNames');return false;">负责人</a>：</th>
                                			<td colspan="3" style="width:90%">
                                    			<a id="nodeNames" class="nodeNamesLayer" href="javascript:void(0)" title="点击选择员工" onClick="addDivBrow(12,'getNames');return false;" style="width:100%"></a>
                                    			<input type="hidden" id="nodeIds" name="nodeIds"/>
                                			</td>
                                		</tr>
                                        <tr>
                                            <th>对应客户：</th>
                                            <td colspan="3">
                                                <input id="cusName" class="inputSize2 lockBack" type="text" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                                                <button class="butSize2" onClick="addDivBrow(2)">选择</button>
                                                <input type="hidden" name="cusIds" id="cusId" />
                                            </td>
                                        </tr>
                                        <tr class="noBorderBot">
                                			<th >统计日期：</th>
                                			<td colspan="3">
                                            	<input name="startDate" id="startDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate').focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                          到&nbsp;<input name="endDate" id="endDate" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                			</td>
                                		</tr>
                                		</c:if>
                                		<c:if test="${statType =='spsAnalyse'}">
                                			<tr>
                                				<th style="width:10%">回款日期：</th>
                                				<td colspan="3">
                                            		<input name="startDate1" id="startDate1" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate1').focus();},maxDate:'#F{$dp.$D(\'endDate1\')}'})"/>
                         						 到&nbsp;<input name="endDate1" id="endDate1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate1\')}'})"/>
                                				</td>
                                			</tr>
                                		</c:if>
                                		<c:if test="${statType =='psalBack'}">
                                			<tr>
                                				<th style="width:10%">发货日期：</th>
                                				<td colspan="3">
                                            		<input name="startDate1" id="startDate1" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate1').focus();},maxDate:'#F{$dp.$D(\'endDate1\')}'})"/>
                         						 到&nbsp;<input name="endDate1" id="endDate1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate1\')}'})"/>
                                				</td>
                                			</tr>
                                			<tr>
                                				<th>产品选择：</th>
							                    <td colspan="3" class="longTd">
							                    	<input type="hidden" name="wprId" id="wprId"/>
							                        <input id="wprName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'wprId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
							                        <button class="butSize2 inputBoxAlign" onClick="chooseProd()">选择</button>
							                    </td>
                                			</tr>
                                		</c:if>
                                		<c:if test="${statType =='proAnalyse'}">
                                			<tr>
                                				<th style="width:10%">产品选择：</th>
							                    <td colspan="3" class="longTd">
							                    	<input type="hidden" name="wprId" id="wprId"/>
							                        <input id="wprName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'wprId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
							                        <button class="butSize2 inputBoxAlign" onClick="chooseProd()">选择</button>
							                    </td>
                                			</tr>
                                			<tr>
                                				<th>签单日期：</th>
                                				<td colspan="3">
                                            		<input name="startDate" id="startDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate').focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                         						 到&nbsp;<input name="endDate" id="endDate" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                				</td>
                                			</tr>
                                			<tr>
                                				<th>发货日期：</th>
                                				<td colspan="3">
                                            		<input name="startDate1" id="startDate1" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate1').focus();},maxDate:'#F{$dp.$D(\'endDate1\')}'})"/>
                         						 到&nbsp;<input name="endDate1" id="endDate1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate1\')}'})"/>
                                				</td>
                                			</tr>
                                		</c:if>
                                	    <c:if test="${statType =='recvAnalyse'}">
                                	    	<tr>
                                	    		<th style="width:10%">收款日期：</th>
	                                	    	<td colspan="3">
												    <input name="corRecDate" id="corRecDate" type="text" class="inputSize2 Wdate" style="cursor:hand"
						                                 readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
												</td>
											</tr>
											<tr>
												<th>时间范围：</th>
												<td colspan="3">
													<select name="timeRage">
														<option value="">请选择</option>
														<option value="date7">7天内过期</option>
														<option value="date15">15天内过期</option>
														<option value="date30">30天内过期</option>
														<option value="dateAll">全部已过期</option>
													</select>
												</td>
											</tr>
                                	    </c:if>
                                	    <c:if test="${statType =='saleAnalyse'}">
                                	         <tr>
	                                			<th style="width:10%"><a title="点击选择销售员" href="javascript:void(0)" onClick="addDivBrow(12,'getNames');return false;">销售员</a>：</th>
	                                			<td colspan="3" style="width:90%">
	                                    			<a id="nodeNames" class="nodeNamesLayer" href="javascript:void(0)" title="点击选择销售员" onClick="addDivBrow(12,'getNames');return false;" style="width:100%"></a>
	                                    			<input type="hidden" id="nodeIds" name="nodeIds"/>
	                                			</td>
	                                		</tr>
	                                        <tr>
	                                            <th>客户：</th>
	                                            <td colspan="3">
	                                                <input id="cusName" class="inputSize2 lockBack" type="text" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
	                                                <button class="butSize2" onClick="addDivBrow(2)">选择</button>
	                                                <input type="hidden" name="cusIds" id="cusId" />
	                                            </td>
	                                        </tr>
                                	    	<tr>
                                	    		<th style="width:10%">发货日期：</th>
                                				<td colspan="3">
                                            		<input name="startDate" id="startDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({onpicked:function(){$('endDate').focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                         						 到&nbsp;<input name="endDate" id="endDate" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand;" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                				</td>
                                	    	</tr>
                                	    </c:if>
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
