<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//清缓存
response.setHeader("Pragma","No-cache");   
response.setHeader("Cache-Control","no-cache");   
response.setDateHeader("Expires", 0); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title><logic:equal value="2" name="isAll">全部</logic:equal><logic:notEqual value="2" name="isAll">我及下属的</logic:notEqual>订单高级查询</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="StyleSheet" href="css/dtree.css" type="text/css">
    

    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
	<script type="text/javascript" src="js/ord.js"></script>
	<script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript">
     	function check(){
			return $("supSear").submit();
	   	}
		function cusEmpty(obj){
			obj.value="";
			$("cusCode").value="";	
		}

		function chooseCus(){
			addDivBrow(2);
		}
	
	 	function showSelect(){
			$("typeId1").value = "${typeId}";
			$("paidMethod").value="${sodPaidMethod}";
			$("sodShipState").value="${sodShipState}";
			$("sodAppIsok").value="${sodAppIsok}"
		}
		
		var treeName = 'ordSupUT';
		var treeName2 = 'ordSupET';
		var ordSupUT = new dTree(treeName);
		var ordSupET = new dTree(treeName2);
		ordSupUT.config.showRootIcon=false;//不显示root图标
		ordSupUT.config.hasCheckBox=true;
		ordSupUT.config.folderLinks=false;
		ordSupET.config.showRootIcon=false;//不显示root图标
		ordSupET.config.hasCheckBox=true;
		ordSupET.config.folderLinks=false;	
		createProgressBar();
		window.onload=function(){
			showSelect();
			showCheckBox('${limCode}',treeName);
			showCheckBox('${empCode}',treeName2);
			//表格内容省略
			loadTabShort("tab");
			//增加清空按钮
			createCancelButton('supSear',0,5,'searButton','after');
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title">销售管理 > 订单管理 > <logic:equal value="2" name="range">全部</logic:equal><logic:notEqual value="2" name="range">我及下属的</logic:notEqual>订单高级查询</div>
              	<div class="descInf">
                  	<form action="orderAction.do" name="supSear" id="supSear" method="get">
                        <input type="hidden" name="op" value="ordSupSearch" />
                        <input type="hidden" name="cusCode" id="cusCode" value="${cusCode}" />
						<input type="hidden" name="isAll" value="${isAll}"/>
                        <table class="supSearForm" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th>负责账号</th>
                                <td>
                                	<input type="text" style="display:none" name="userCBName" id="userCBName"/>
                                	<div>
										<script type="text/javascript">
											$("userCBName").value="nodes"+treeName;
											ordSupUT.add('${orgTopCode}',-1,'负责账号列表','','负责账号列表','_parent');
									 	</script>
										<logic:notEmpty name="orgList">
										<logic:iterate id="ol" name="orgList" scope="request">
											<script type="text/javascript">
												var id='${ol.soCode}'
												var pid='${ol.salOrg.soCode}';
												ordSupUT.add(id,pid,'${ol.soName}','','${ol.soName}','_parent','images/dtree/orgnode.gif','images/dtree/orgopen.gif');
											</script>
											<logic:notEmpty name="ol" property="limUsers">
												<logic:iterate id="lu" name="ol" property="limUsers">
													<logic:equal name="lu" property="userIsenabled" value="1">
													<script type="text/javascript">
														var fid='${lu.userCode}';
														var name='${lu.salEmp.seName}';
														var role='${lu.salEmp.limRole.rolName}';
														var name1;
														if(role!=null&&role!=""){
															name1=name+"-"+role;
														}									
														if(pid!=null&&pid!=""){	
															ordSupUT.add(fid,id,name1,null,name1,'_parent',null,null,null,'${lu.userCode}');
														}
													</script>
													</logic:equal>
												</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										<script type="text/javascript">
											document.write(ordSupUT);
										</script>
										</logic:notEmpty>
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<th>签单人</th>
                            	<td>
                            		<input type="text" style="display:none"  name="empCBName" id="empCBName"/>
                            		<div>
										<script type="text/javascript">
											$("empCBName").value="nodes"+treeName2;
                                            //id, pid, name, url, title, target, icon, iconOpen, open	
                                            ordSupET.add('${orgTopCode}',-1,'员工列表','','员工列表','_self');
                                        </script>
                                        <logic:notEmpty name="orgList2">
                                            <logic:iterate id="salOrg" name="orgList2">
                                                <logic:notEqual value="${orgTopCode}" name="salOrg" property="soCode">
                                                <script type="text/javascript">
                                                ordSupET.add('${salOrg.soCode}','${salOrg.salOrg.soCode}','${salOrg.soName}','','${salOrg.soName}','_self','images/dtree/orgnode.gif','images/dtree/orgopen.gif');
                                                </script>
                                                </logic:notEqual>
												<logic:notEmpty name="salOrg" property="salEmps">
													<logic:iterate id="emp" name="salOrg" property="salEmps">
														<logic:notEqual name="emp" property="seRap" value="离职">
														<script type="text/javascript">
														var empImg;
														if('${emp.seSex}'=='女'){
															empImg = 'images/dtree/empnode_w.gif';
														}
														else{
															empImg = 'images/dtree/empnode_m.gif';
														}
														ordSupET.add('${emp.seNo}','${emp.salOrg.soCode}','${emp.seName}[${emp.limRole.rolName}]','','${emp.seName}[${emp.limRole.rolName}]','_self',empImg,empImg,null,'${emp.seNo}');
														</script>
														</logic:notEqual>
													</logic:iterate>
											</logic:notEmpty>
                                            </logic:iterate>
                                        </logic:notEmpty> 
                                        <script type="text/javascript">
                                            document.write(ordSupET);
                                        </script>
                                     </div>
                            	</td>
                            </tr>
                            <tr>
                            	<th>基本信息</th>
                                <td>
                                	<table class="innerSearForm" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <th>订单主题：</th>
                                            <td><input type="text" class="inputSize2" name="sodTil" value="${sodTil}" /></td>
                                            <th>对应客户：</th>
                                            <td>
                                            	<input id="cusName" name="cusName" class="inputSize2S lockBack" type="text" value="${cusName}" readonly ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" />&nbsp;
                                        		<button class="butSize2" onClick="chooseCus()">选择</button>
                                            </td>
                              			</tr>
                              			<tr>
                              				<th>订单号：</th>
                              				<td><input type="text" class="inputSize2" name="sodNum" id="ordNum" value="${sodNum}"/></td>
                              				<th>订单类别：</th>
                              				<td>
                              					<select name="typeId" id="typeId1" class="inputSize2">
													<option value="">请选择</option>
													<logic:notEmpty name="OrdTypeList">
														<logic:iterate id="typeList" name="OrdTypeList">
															<option value="${typeList.typId}">${typeList.typName}</option>
														</logic:iterate>
													</logic:notEmpty>
												</select>
                              				</td>
                              			</tr>
                              			<tr>
                              				<th>总金额：</th>
                              				<td><input type="text" class="inputSize2" name="sodSumMon" id="sumMon" value="${sodSumMon}" onBlur="checkPrice(this)"/></td>
                              				<th>付款方式：</th>
                              				<td>
                              					<select name="sodPaidMethod"  id="paidMethod" class="inputSize2">
													<option value="">请选择</option>
													<option value="支票">支票</option>
													<option value="现金">现金</option>
													<option value="邮政汇款">邮政汇款</option>
													<option value="银行电汇">银行电汇</option>
													<option value="网上支付">网上支付</option>
													<option value="其他">其他</option>
												</select>
                              				</td>
                              			</tr>
                              			<tr>
                              				<th>签订时间：</th>
                              				<td colspan="3">
                              					<input name="conDate1" value="${conDate1}" id="conDate1" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" onFocus="var conDate2=$('conDate2');WdatePicker({onpicked:function(){conDate2.focus();},maxDate:'#F{$dp.$D(\'conDate2\')}'})" readonly="readonly"/>
                                    			&nbsp;到&nbsp;
                                    			<input name="conDate2" value="${conDate2}" id="conDate2" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'conDate1\')}'})" readonly="readonly"/>
                              				</td>
                              			</tr>
                              			<tr>
		                                    <th>开始日期：</th>
		                                    <td colspan="3">
											<input name="startDate1" value="${startDate1}" id="startDate1" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" onFocus="var startDate2=$('startDate2');WdatePicker({onpicked:function(){startDate2.focus();},maxDate:'#F{$dp.$D(\'startDate2\')}'})"readonly="readonly"/>
											&nbsp;到&nbsp;
											<input name="startDate2" value="${startDate2}" id="startDate2" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate1\')}'})"/></td>
										</tr>
										<tr>
		                                    <th>结束日期：</th>
		                                    <td colspan="3">
											<input name="endDate1" value="${endDate1}" id="endDate1" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" onFocus="var endDate2=$('endDate2');WdatePicker({onpicked:function(){endDate2.focus();},maxDate:'#F{$dp.$D(\'endDate2\')}'})"readonly="readonly"/>
											&nbsp;到&nbsp;
											<input name="endDate2" value="${endDate2}" id="shipDate2" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'endDate1\')}'})"/></td>
										</tr>
                          			</table>
								</td>
							</tr>
							<tr>
								<th>交付信息</th>
								<td>
									<table class="innerSearForm" cellpadding="0" cellspacing="0">
										<tr>
											<th>交付状态：</th>
											<td>
												<select name="sodShipState" id="sodShipState" class="inputSize2">
													<option value="">请选择</option>
													<option value="未交付">未交付</option>
													<option value="部分交付">部分交付</option>
													<option value="全部交付">全部交付</option>
												</select>
											</td>
											<th>交付日期：</th>
											<td>
												<input name="shipDate1" value="${shipDate1}" id="shipDate1" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" onFocus="var shipDate2=$('shipDate2');WdatePicker({onpicked:function(){shipDate2.focus();},maxDate:'#F{$dp.$D(\'shipDate2\')}'})" readonly="readonly"/>
												&nbsp;到&nbsp;
												<input name="shipDate2" value="${shipDate2}" id="shipDate2" type="text" class="Wdate shortDate" ondblClick="clearInput(this)" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'shipDate1\')}'})"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<th>审核信息</th>
								<td>
									<table class="innerSearForm" cellpadding="0" cellspacing="0">
										<tr>
											<th>审核状态：</th>
											<td colspan="3">
												<select name="sodAppIsok"  id="sodAppIsok" class="inputSize2">
													<option value="">请选择</option>
													<option value="0">待审核</option>
													<option value="1">已审核</option>
												</select>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr class="searSubmitArea">
                            	<th>&nbsp;</th>
                            	<td><input class="butSize1 inputBoxAlign" id="searButton" type="button" onClick="check()" value="开始搜索" /></td>
                            </tr>
                    	</table>
                	</form>
	
                   <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0" style="width:100%">
                        <tr>
                            <!--<th style="width:2%">&nbsp;</th>-->
                            <th style="width:4%">状态</th>
                            <th style="width:18%">订单主题</th>
                            <th style="width:10%">订单号</th>
                            <th style="width:8%">类别</th>
                            <th style="width:10%">对应客户</th>
							<th style="width:8%">已回款</th>
                            <th style="width:8%">总金额</th>
							<th style="width:8%">签订时间</th>
                            <th style="width:10%">交付日期</th>
                            <th style="width:6%">签单人</th>
                            <th style="width:10%; border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="ordList">
                         <logic:iterate id="ordList" name="ordList" indexId="count">
                         <tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)" onDblClick="descPop('orderAction.do?op=showOrdDesc&code=${ordList.sodCode}')">
                          <!--<td style="text-align:center"><input name="" type="checkbox" value=""></td>-->
						   <td style="text-align:center">
						   		<logic:notEmpty name="ordList" property="sodAppIsok">
                                	<logic:equal value="0" name="ordList" property="sodAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                	<logic:equal value="1" name="ordList" property="sodAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal>
                            	</logic:notEmpty>
								<logic:empty name="ordList" property="sodAppIsok">
									<img src="images/content/time.gif" alt="未审核(未添加产品)">
								</logic:empty>
							</td>
                          <td>
                          <a href="orderAction.do?op=showOrdDesc&code=${ordList.sodCode}" target="_blank" style=" cursor:pointer">${ordList.sodTil}</a></td>  
                          <td>${ordList.sodNum}&nbsp;</td>
                          <td>${ordList.salOrderType.typName }&nbsp;</td>
                          <td class="mLink">
                          	<logic:notEmpty name="ordList" property="cusCorCus">
                                <a title="${ordList.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${ordList.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${ordList.cusCorCus.corName}</a>
                            </logic:notEmpty>&nbsp;
                          </td>
						  <td>
						   <logic:notEmpty name="ordList" property="sodPaidMon">
						  ￥<bean:write name="ordList" property="sodPaidMon" format="###,##0.00"/>
						  </logic:notEmpty>
						  <logic:empty name="ordList" property="sodPaidMon">￥0.00</logic:empty>
						  </td>
                          <td>￥<bean:write name="ordList" property="sodSumMon" format="###,##0.00"/></td>
						  <td><label id="sodConDate${count}>"></label>&nbsp;</td>
                          <td><label id="deadLine${count}"></label>
                          <logic:empty name="ordList" property="sodDeadline">
                          	<span class="gray">未设定</span>
                          </logic:empty>
                          	<logic:equal value="未交付" name="ordList" property="sodShipState">
                            	<img src="images/content/tofinish.gif" alt="未交付" style="vertical-align:middle"/>
                            </logic:equal>
							<logic:equal value="部分交付" name="ordList" property="sodShipState">
                            	<img src="images/content/doing.gif" alt="部分交付" style="vertical-align:middle"/>
                            </logic:equal>
                            <logic:equal value="全部交付" name="ordList" property="sodShipState">
                            	<img src="images/content/finish.gif" alt="全部交付" style="vertical-align:middle"/>
                            </logic:equal>
                          </td>
                          <td>${ordList.salEmp.seName}&nbsp;</td>
                          <td>&nbsp;
                            <a href="orderAction.do?op=showOrdDesc&code=${ordList.sodCode}" target="_blank">
                            <img src="images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
							<logic:notEqual value="1" name="ordList" property="sodAppIsok">
                            <img onClick="ordPopDiv(11,'${ordList.sodCode}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                            <img onClick="ordDelDiv(4,'${ordList.sodCode}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>&nbsp;
                            </logic:notEqual>
                         </td>
                         </tr>
                        <script type="text/javascript">
                            dateFormat('deadLine','${ordList.sodDeadline}','${count}');
							dateFormat('sodConDate','${ordList.sodConDate}','${count}');
                            rowsBg('tr','${count}');
                        </script>
                         </logic:iterate>
                         </logic:notEmpty>
                         <logic:empty name="ordList">
                            <tr>
                                <td class="noDataTd" colspan="11">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                    </table>
					<logic:notEmpty name="ordList">
					<script type="text/javascript">
                                createPage('orderAction.do?op=ordSupSearch&isAll=${isAll}&limCode=${limCode}&empCode=${empCode}&cusCode=${cusCode}&sodAppIsok=${sodAppIsok}&sodTil='+encodeURIComponent('${sodTil}')+'&sodNum='+encodeURIComponent('${sodNum}')+'&typeId=${typeId}&sodSumMon=${sodSumMon}&sodPaidMethod='+ encodeURIComponent('${sodPaidMethod}')+'&conDate1=${conDate1}&conDate2=${conDate2}&shipDate1=${shipDate1}&shipDate2=${shipDate2}&startDate1=${startDate1}&startDate2=${startDate2}&endDate1=${endDate1}&endDate2=${endDate2}&sodShipState='+ encodeURIComponent('${sodShipState}')+'&cusName='+ encodeURIComponent('${cusName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                   </script>
				  </logic:notEmpty>
                </div>
        	</div>
		</div>
  </body>
</html>
