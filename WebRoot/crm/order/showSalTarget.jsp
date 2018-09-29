<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
int count1=0;
int count2=0;
int count3=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>销售指标分析</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
		.autoAddTh{
			background-color:#4e80c9;
			font-size:12px;
		 	font-weight:500;
		 	color: #FFFFFF;
			text-align:center !important;
			border-right:3px  #B6B6B6 solid !important;
		}
		.disType {
			text-align:left; 
			padding:5px; 
			padding-top:10px;
		}
		.disType {
			text-align:left; 
			padding:5px; 
			padding-top:10px;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/sta.js"></script>
	<script type="text/javascript" src="js/FusionCharts.js"></script>
    <script type="text/javascript" >
	var yea=parseInt('${year}');
	function addOption(){
           var obj=$('year');
		   for(var i=-3;i<4;i++){
		   	var text=(yea+i)+"年";
		   	obj.options.add(new Option(text,(yea+i)));
		   }  
     }
	 
		function check(){
			waitSubmit("dosave","分析中...");
			return $("countTr").submit();
		}
		function loadData(){
			//$('range').value='${range}';
			$('year').value='${tarYear}';
			$('tarName').value='${tarName}';
			$("nodeIds").value='${nodeIds}';
			$("nodeNamesInput").value='${nodeNamesInput}';
			$("nodeNames").innerHTML='${nodeNamesInput}';
		}
		function changeChar(n){
			var url='statAction.do?op=targetXML&tarName=${tarName}&tarYear=${tarYear}&nodeIds=${nodeIds}&isEmpty=${isEmpty}';
			renderChar(n,url,'chartDiv');
		}	
		
		 
		 createProgressBar();	
		 window.onload=function(){
		 	//表格内容省略
			loadTabShort("tab1");
			closeProgressBar();
			addOption();
			changeChar(10);
			loadData();
				
		 }
	</script>
</head>
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">销售管理 > 销售指标</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
							<div id="tabType1" class="tabTypeWhite"  onClick="window.location.href='statAction.do?op=showSalTarget&tarName=1&isEmpty=yes'">销售指标分析</div>
                            <div id="tabType2" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)"  onClick="window.location.href='statAction.do?op=salTarget&sat=sat'">销售指标设置</div>
                        </div>
                    </th>
                </tr>
            </table>
			<div id="listContent">
            	<div class="grayBack" style="width:100%; padding:10px; margin-bottom:10px;">
                    <form action="statAction.do" method="post" id="countTr" style="margin:0px;">
                        <input type="hidden" name="op" value="showSalTarget">
                        <table cellpadding="0" cellspacing="0" class="normal" style="width:100%">
                            <tr>
                                <td style="width:70px; vertical-align:top; text-align:right;"><nobr><a title="点击选择员工" href="javascript:void(0)" onClick="addDivBrow(12,'getNames');return false;">所属员工</a>：</nobr></td>
                                <td style="padding-bottom:8px;">
                                    <a id="nodeNames" class="nodeNamesLayer" href="javascript:void(0)" title="点击选择员工" onClick="addDivBrow(12,'getNames');return false;" style="width:100%"></a>
                                    <input type="hidden" id="nodeIds" name="nodeIds"/>
                                    <input type="hidden" id="nodeNamesInput" name="nodeNamesInput"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align:right;">年份：</td>
                                <td>
                                	<select id="year" name="tarYear" class="inputBoxAlign"></select>
                               	 	&nbsp;&nbsp;&nbsp;
                                	指标：<select id="tarName" name="tarName" class="inputBoxAlign">
                                        <option value="1">回款额</option>
                                        <option value="2">销售额</option>
                                        <option value="3">签单数</option>
                                    </select>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                               		<button id="dosave" class="butSize1 inputBoxAlign" onClick="check()">开始分析</button></td>
                            </tr>
                            </table>
                	</form>
                </div>
				<table class="normal rowstable" cellpadding="0" cellspacing="0" style="width:100%;">
					<tr>
						<td style="text-align:left; width:20%; vertical-align:top; padding:0px; border-right:#b8b7b7 1px solid">
                            <table id="tab1" class="normal" cellpadding="0" cellspacing="0" width="100%">
                            	<tr>
                                    <th style="border-right:0px; width:100px;">日期</th>
                                </tr>
                                <tr>
                                    <th style="border-right:0px;">用户</th>
                                </tr>
                                <logic:notEmpty name="user">
                                    <logic:iterate id="u" name="user">
                                    <tr id="tr1<%= count1%>">
                                    	<td>${u.seName}<span class="gray">[${u.limRole.rolName}]-${u.salOrg.soName}</span></td>
                                    </tr>
                                    <script type="text/javascript">
										rowsBg('tr1',<%=count1%>);
									</script>
									<%count1++; %>
                                    </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="user">
                                	<tr>
                                    	<td class="gray">没有相关数据!</td>
                                    </tr>
                                </logic:empty>
                            </table>
						</td>
						<td id="t" width="60%" style="text-align:left; vertical-align:top; padding:0px; border-right:#b8b7b7 1px solid">
							<div id="dateScrollDiv" class="divWithScroll2" style="overflow:scroll; overflow-y:hidden; padding:0px; padding-right:2px; background-color:#ECE9D8">
                                        <table class='normal rowstable' width='100%' cellpadding='0' cellspacing='0' style="background-color:#fff">
                                            <tr>
                                                <td colspan='3' class='autoAddTh' >${tarYear}-01</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-02</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-03</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-04</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-05</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-06</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-07</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-08</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-09</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-10</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-11</td>
												<td colspan='3' class='autoAddTh' >${tarYear}-12</td>
                                            </tr>
                                            <tr>
												<%
													for(int i=0;i<12;i++){
												%>
                                                <th style="text-align:center"><nobr>&nbsp;&nbsp;目标&nbsp;&nbsp;</nobr></th>
                                                <th style="text-align:center"><nobr>&nbsp;&nbsp;完成&nbsp;&nbsp;</nobr></th>
                                                <th style="border-right:3px #B6B6B6 solid;text-align:center"><nobr>&nbsp;&nbsp;%&nbsp;&nbsp;<nobr></th>
												<%}%>
                                            </tr>
                                            <logic:notEmpty name='user'>
                                            	<%
													if(count3%2!=0)
														count3+=1;
												%>
                                                <logic:iterate id='u' name='user'>
                                                <% 	int isNone=1;%>
												<tr id="tr3<%=count3%>">
                                                <logic:notEmpty name='salTarget'>
                                                <logic:iterate id='s' name='salTarget'>				 	          
                                                        <logic:equal value='${u.seNo}' name='s' property='name'>
														<% int cc=1;%>
															<logic:iterate id='sa' name='s' property='array'>
															<% if(cc%3==0){%>
															<td style="border-right:3px #B6B6B6 solid;"><nobr>${sa}&nbsp;</nobr></td>
															<%}else{%>
															<td style="border-right:1px #B6B6B6 solid;"><nobr>
															<script type="text/javascript">
																document.write(changeTwoDecimal('${sa}'));
															</script>
															</nobr></td>
															<%} cc++;%>
															</logic:iterate>
																
                                                            <%isNone=0; %>                
                                                        </logic:equal>
                                                    </logic:iterate>
                                                    <%
                                                    if(isNone==1){
														for(int i=0;i<12;i++){
													%>
													 		<td style="border-right:1px #B6B6B6 solid;">0</td>
													  	   	<td style="border-right:1px #B6B6B6 solid;">0</td>
															<td style="border-right:3px #B6B6B6 solid;">0%&nbsp;</td>
                                                    <%}}%>
                                                    </logic:notEmpty>
													<logic:empty name='salTarget'>
													 <%
														for(int i=0;i<12;i++){
													%>
                                                       <td style="border-right:1px #B6B6B6 solid;">0</td>
													   <td style="border-right:1px #B6B6B6 solid;">0</td>
													   <td style="border-right:3px #B6B6B6 solid;">0%&nbsp;</td>
                                                    <%}%>
													</logic:empty>
													
                                                    <script type="text/javascript">
														rowsBg('tr3',<%=count3%>);
													</script>
														</tr>
													<%count3++; %>						
                                                </logic:iterate>
                                            </logic:notEmpty>
                                    	</table>
                            </div>
						</td>
                      
						<td style="text-align:left; vertical-align:top; padding:0px; width:20%">
							<table cellpadding="0" class="normal" cellspacing="0" width="100%">
                            	<tr>
									<th style="border-right:0px; text-align:center" colspan="3">合计</th>
								</tr>
								<tr>
                                	<th style="text-align:center"><nobr>&nbsp;&nbsp;目标&nbsp;&nbsp;</nobr></th>
									<th style="text-align:center"><nobr>&nbsp;&nbsp;完成&nbsp;</nobr></th>
									<th style="border-right:0px;text-align:center"><nobr>&nbsp;&nbsp;%&nbsp;&nbsp;</nobr></th>
								</tr>
                                <logic:notEmpty name="user">
                                    <logic:iterate id="u" name="user">
										 <% int isNone2=1,isNone3=1;%>
                                            <tr id="tr2<%= count2%>">
													<logic:notEmpty name="ordStatistic2">
													 <logic:iterate id="sta" name="ordStatistic2">
													<logic:equal value='${u.seNo}' name='sta' property='id'>
													<td style="border-right:1px #B6B6B6 solid;">
														<nobr>
															<span>
																<logic:equal value='3' name='tarName'>${sta.lnum}
																	<input type="hidden" id="${u.seNo}at" value="${sta.lnum}">
																</logic:equal>
																<logic:notEqual value='3' name='tarName'>
																	 <bean:write name="sta" property="dnum" format="###,##0.00"/>&nbsp;
																	 <input type="hidden" id="${u.seNo}at" value="${sta.dnum}">
																</logic:notEqual>
															</span></nobr>
													</td>
													<% isNone2=0;%>
													</logic:equal>
													</logic:iterate>
													 <%
                                                    if(isNone2==1){%>
                                                       <td style="border-right:1px #B6B6B6 solid;"><span id="${u.seNo}at">0</span></td>
                                                    <%}%>
												</logic:notEmpty>
												<logic:empty name="ordStatistic2">
												<td style="border-right:1px #B6B6B6 solid;"><span id="${u.seNo}at">0</span></td>
												</logic:empty>
												
                                                <logic:notEmpty name="ordStatistic3">
													 <logic:iterate id="sta3" name="ordStatistic3">
													<logic:equal value='${u.seNo}' name='sta3' property='id'>
													<td style="border-right:1px #B6B6B6 solid;">
														<nobr>
															<span>
																<logic:equal value='3' name='tarName'>${sta3.lnum}
																	<input type="hidden" id="${u.seNo}af" value="${sta3.lnum}">
																</logic:equal>
																<logic:notEqual value='3' name='tarName'>
																	 <bean:write name="sta3" property="dnum" format="###,##0.00"/>&nbsp;
																	 <input type="hidden" id="${u.seNo}af" value="${sta3.dnum}">
																</logic:notEqual>
															</span></nobr>
													</td>
													<% isNone3=0;%>
													</logic:equal>
													</logic:iterate>
													<%
                                                    if(isNone3==1){%>
                                                       <td style="border-right:1px #B6B6B6 solid;"><span id="${u.seNo}af">0</span></td>
                                                    <%}%>
												</logic:notEmpty>
												<logic:empty name="ordStatistic3">
												<td style="border-right:1px #B6B6B6 solid;"><span id="${u.seNo}af">0</span></td>
												</logic:empty>
												
                                                <td style="border-right:1px #B6B6B6 solid;"><nobr><span id="${u.seNo}ap"></span></nobr></td>
												<script type="text/javascript">
														var at=$('${u.seNo}at').value;
														var af=$('${u.seNo}af').value;
														var pr1;
														if(at==null||parseInt(at)==0||af==null||parseInt(af)==0){
															pr1=0;
														}else{
															 pr1=(parseFloat(numFormat(af))/parseFloat(numFormat(at))*100).toFixed(2);
														}
														$('${u.seNo}ap').innerHTML=pr1+"%";
													</script>
                                                </tr>
                                        <script type="text/javascript">
											rowsBg('tr2',<%=count2%>);
										</script>
										<%count2++; %>
                                    </logic:iterate>
                                </logic:notEmpty>
							</table>
						</td>
                    </tr>
                </table>
				<div class="disType">
						<span class="bold">显示类型:</span>
						<a href="javascript:void(0)" onClick="changeChar(9);return false;">2D柱图</a>&nbsp;
						<a href="javascript:void(0)" onClick="changeChar(10);return false;">3D柱图</a>&nbsp;
						<a href="javascript:void(0)" onClick="changeChar(11);return false;">折线图</a>&nbsp;
						<a href="javascript:void(0)" onClick="changeChar(12);return false;">面积图</a>&nbsp;
						<a href="javascript:void(0)" onClick="changeChar(13);return false;">2D条形图</a>&nbsp;
						<a href="javascript:void(0)" onClick="changeChar(14);return false;">3D条形图</a>&nbsp;
					</div>
				 	<div id="chartDiv" style="text-align:left; width:100%; height:500px;"></div>
                <script type="text/jscript">
					$("dateScrollDiv").style.width = document.body.clientWidth*(3/5);
				</script>
        	</div>
  		</div> 
	 </div>
  </body>
</html>
