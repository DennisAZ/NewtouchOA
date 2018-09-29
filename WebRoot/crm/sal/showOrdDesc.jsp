<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int c=0;
	int j=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示订单详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
		function reCreateSumRow(sumValue){
			//sumValue[4]="￥"+changeTwoDecimal(sumValue[4]);
			var shipNumSum = 0;
			<c:if test="${!empty rsfList}">
			<c:forEach var="rsf" items="${rsfList}">
			if($("ropShip${rsf.prodId}")!=null){
				$("ropShip${rsf.prodId}").innerHTML = "${rsf.shipNum}";
				shipNumSum += parseFloat("${rsf.shipNum}");
			}
			</c:forEach>
			</c:if>
			sumValue[5] = shipNumSum;
		}
		
		function initPage(){
			if("${order}"!=""){
				if($("sodCon")!=null){
					//条件条款收缩层
					if(shortDiv("sodCon",150)){
						$("conMoreButton").show();
					}
				}
				if($("appDesc")!=null){
					//审核记录收缩层
					if(shortDiv("appDesc",150)){
						$("appMoreButton").show();
					}
				}
				var ordToPaid=0;//订单应收款
				if("${order.sodPaidMon}"==""){
					ordToPaid=${order.sodSumMon};
					$("paidMon").innerHTML="暂未回款";
				}
				else{
					ordToPaid=parseFloat("${order.sodSumMon}")-parseFloat("${order.sodPaidMon}");
					$("paidMon").innerHTML="￥"+changeTwoDecimal("${order.sodPaidMon}");
				}
				if(ordToPaid<0){ ordToPaid=0; }
				$("ordToPaid").innerHTML="￥"+changeTwoDecimal(ordToPaid);
			
				removeTime("startDate","${order.sodOrdDate}",1);
				removeTime("endDate","${order.sodEndDate}",1);	
				removeTime("deadLine","${order.sodDeadline}",1);
				removeTime("conDate","${order.sodConDate}",1);
				removeTime("comiteDate","${order.sodComiteDate}",1);
				removeTime("inpDate","${order.sodInpDate}",2);
				removeTime("changeDate","${order.sodChangeDate}",2);
				removeTime("sodAppDate","${order.sodAppDate}",2);
				loadXpTabSel();
			}
		}
		
		function toShipProd(){
			if(ropIds.length>0){
				descPop("orderAction.do?op=toSaveProdShipment&ordId=${order.sodCode}");
			}
			else{
				alert("尚未添加订单明细，无法进行发货！");
			}
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			if(obj.wmsProduct){
				ropIds.push(obj.ropId);
				dblFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.wmsProduct.wprId+"')";
				var proName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.wmsProduct.wprName+"</a>";
				var proUnit=obj.wmsProduct.typeList?"("+obj.wmsProduct.typeList.typName+")":"";
				var prodPrc = "￥"+(obj.wmsProduct.wprSalePrc>0?(changeTwoDecimal(obj.wmsProduct.wprSalePrc)+"&nbsp;<span class='deepGray'>(不含税"+changeTwoDecimal(obj.wmsProduct.wprSalePrc/(1+getSalTaxRate()))+")</span>"):"0.00");
				var ropPrc="￥"+changeTwoDecimal(obj.ropPrice)+(obj.ropZk>0?"&nbsp;<span class='blue'>[不含税价]</span>":"&nbsp;<span class='deepGreen'>[含税价]</span>");	
				var shipNum = "<span id='ropShip"+obj.wmsProduct.wprId+"'>0</span>";
				/*var shipAmt = obj.ropPrice*shipNum*(obj.ropZk>0?(1+parseFloat(obj.ropZk)):1);
				var returnNum = obj.ropReturnNum>0?obj.ropReturnNum:0;*/
				/*var returnAmt = obj.ropPrice*returnNum*(obj.ropZk>0?(1+parseFloat(obj.ropZk)):1);*/
				/*if(obj.ropPrice>0&&obj.ropNum>0&&shipNum>=returnNum){
					if(obj.ropPrice>=proPrc){
						salBack=(shipAmt-returnAmt)*(obj.wmsProduct.wprNormalPer?obj.wmsProduct.wprNormalPer:0)+(obj.ropPrice-proPrc)*(shipNum-returnNum)*(obj.ropZk>0?(1+parseFloat(obj.ropZk)):1)*(obj.wmsProduct.wprOverPer?obj.wmsProduct.wprOverPer:0);
					}
					else{
						salBack=(shipAmt-returnAmt)*(obj.wmsProduct.wprLowPer?obj.wmsProduct.wprLowPer:0);
					}
				}*/
				datas = [ obj.wmsProduct.wprCode, proName, prodPrc, ropPrc, [obj.ropNum+proUnit,obj.ropNum], [obj.ropRealPrice,obj.ropRealPrice],shipNum, obj.ropRemark ];
				return [datas,className,dblFunc,dataId];
			}
			else{
				return [];
			}
		}
		function loadRopList(sortCol,isDe,pageSize,curP){
			ropIds = [];
			var url = "orderAction.do";
			var pars = [];
			pars.op = "listOrdPro";
			pars.ordId = "${order.sodCode}";
			var loadFunc = "loadRopList";
			var cols=[
				{name:"编号",width:"8%"},
				{name:"名称",align:"left",width:"10%"},
				{name:"标准价格",width:"10%"},
				{name:"订单价格",width:"10%"},
				{name:"订单数量",width:"14%"},
				{name:"总价",renderer:"money",align:"right",width:"10%"},
				{name:"发货数量",width:"12%"},
				{name:"备注",align:"left",width:"16%"}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var ropIds = [];//明细产品ID集合
		var gridEl = new MGrid("ordProdListTab","dataList");
		gridEl.config.hasPage=false;
		gridEl.config.sortable=false;
		gridEl.config.isResize=false;
		gridEl.config.hasSumRow=true;
		gridEl.config.sumRowCallBack=reCreateSumRow;
		createProgressBar();	
		window.onload=function(){
			initPage();
			loadRopList();
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
      	<c:if test="${!empty order}">
		<div id="contentbox">
        	<div class="descInf">
            	<div id="descTop">
                <span class="descOp"><c:if test="${order.sodAppIsok==0}">[<a href="javascript:void(0);" onClick="ordPopDiv(11,'${order.sodCode}');return false;" title="编辑">编辑</a>]</c:if>&nbsp;&nbsp;&nbsp;<!--[<a href="orderAction.do?op=toPrintOrd&sodCode=${order.sodCode}" target="_blank"  title="打印订单">打印订单</a>]--></span>
                订单详细信息<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th class="descTitleL">订单主题：</th>
							<th class="descTitleR" colspan="3">${order.sodTil}&nbsp;</th>
						</tr>
					</thead>
					<tbody>
                    	<tr>
                            <th class="blue">对应客户：</th>
                            <td class="blue mlink">
                                <c:if test="${!empty order.cusCorCus}">
                                    <a href="javascript:void(0)" onClick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${order.cusCorCus.corCode}')"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${order.cusCorCus.corName}</a>
                                </c:if>&nbsp;
                            </td>
							<th>客户电话：</th>
							<td>
								<c:if test="${!empty order.cusCorCus}">
                                    ${order.cusCorCus.corPhone}&nbsp;
                                </c:if>
							</td>
						</tr>
						<tr>
							<th>客户邮编：</th>
							<td>
								<c:if test="${!empty order.cusCorCus}">
                                    ${order.cusCorCus.corZipCode}&nbsp;
                                </c:if>
							</td>
							<th>客户地址：</th>
							<td>
								<c:if test="${!empty order.cusCorCus}">
                                    ${order.cusCorCus.corAddress}&nbsp;
                                </c:if>
							</td>
						</tr>
                        <!--<tr>
                            <th>付款方式：</th>
                        	<td>${order.sodPaidMethod}&nbsp;</td>
                            <th>客户签约人：</th>
                            <td>&nbsp;${order.sodCusCon}</td>
                        </tr>-->
                        <tr> 
							<th>订单号：</th>
							<td>${order.sodNum}&nbsp;</td>
                        	<th>总金额：</th>
                            <td colspan="3"><c:if test="${!empty order.sodSumMon}">￥<fmt:formatNumber value="${order.sodSumMon}" pattern="#,##0.00" /></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>签订日期：</th>
                            <td><span id="conDate"></span>&nbsp;</td>
                            <th>签单人：</th>
                            <td>${order.salEmp.seName}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>交货期：</th>
                            <td colspan="3"><span id="comiteDate"></span>&nbsp;</td>
                        </tr>
                        <tr>
							<th>条件与条款：</th>
							<td colspan="3">
								<div id="allSodCon" style="display:none;">
                            	${order.sodContent}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someSodCon','allSodCon')">-&nbsp;点击收起</a>
								</div>
								<div id="someSodCon">
	                                <div id="sodCon">${order.sodContent}</div>
	                                <a id="conMoreButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allSodCon','someSodCon')">+&nbsp;点击展开</a>
	                            </div>
							</td>
						</tr>
						<tr>
                            <th>备注：</th>
                            <td colspan="3">${order.sodRemark}&nbsp;</td>
                        </tr>
                  	</tbody>
					<thead>
					</thead>
						<tr class="noBorderBot">
							<th><c:if test="${order.sodAppIsok==1}"><img class="imgAlign" src="crm/images/content/lock.gif" alt="订单已审核通过，无法修改明细"/>&nbsp;</c:if>订单明细：
							</th>
							<td>
								<span style="padding:4px; font-size:14px;">
									<c:if test="${order.sodAppIsok!=1}">
		                            	<span class="blue">[<a href="orderAction.do?op=toUpdRop&sodCode=${order.sodCode}" target="_blank">编辑订单明细</a>]</span>
									</c:if>
                                    <c:if test="${order.sodAppIsok==1}">
                                        <span class="blue">[<a href="javascript:void(0)" onClick="toShipProd()">发货</a>]</span>
                                    </c:if>
	                            </span>
							</td>
						</tr>
					<tbody>
                        <tr>
                            <td colspan="4" class="subTab"><div id="dataList" class="dataList" style="height:auto"></div></td>
                        </tr>
					</tbody>
                    <thead>
                        <tr>
                            <td colspan="4"><div>回款信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                        	<th class="orangeBack" style="border:0">已回款金额：</th>
                            <td><span id="paidMon"></span>&nbsp;</td>
                            <th>提成金额：</th>
                            <td><c:if test="${!empty salBackSum}">￥<fmt:formatNumber value="${salBackSum}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                        	<th>订单应收款：</th>
                            <td><span id="ordToPaid"></span>&nbsp;</td>
                            <th>发货应收款：</th>
                            <td><c:if test="${!empty realToPaid}">￥<fmt:formatNumber value="${realToPaid}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                        <tr>
                            <td colspan="4"><div>辅助信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                        	<th>订单状态：</th>
                            <td>${order.sodShipState.typName}&nbsp;
                                <c:if test="${order.sodAppIsok==1}">
                                 <span class="blue">&nbsp;[<a href="javascript:void(0)" onClick="ordPopDiv(12,'${order.sodCode}');return false;" >修改</a>]</span>
                                </c:if>
                            </td>
                            <th>交付日期：</th>
                            <td><span id="deadLine"></span>&nbsp;</td>
                        </tr>
                    	<tr>
							<th>订单类别：</th>
							<td>${order.salOrderType.typName}&nbsp;</td>
                            <th>订单来源：</th>
                            <td>${order.salOrderSou.typName}&nbsp;</td>
						</tr>
                    	<tr>
                            <th>开始日期：</th>
                            <td><span id="startDate"></span>&nbsp;</td>
                            <th>结束日期：</th>
                            <td><span id="endDate"></span>&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                <c:if test="${!empty order.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${order.sodCode}','ord','reload')"/>
                                 </c:if>
                                 <c:if test="${empty order.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${order.sodCode}','ord','reload')"/>
                                 </c:if>
                                 附件：
                            </th>
                            <td colspan="3">
                                 <c:forEach var="attList" items="${order.attachments}" varStatus="fIndex">
                                 	<span id="fileDLLi${fIndex.index}"></span>
                    				<script type="text/javascript">createFileToDL("${fIndex.index}","${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                </c:forEach>&nbsp;
                            </td>
                        </tr>
                 	</tbody>
					<thead>
						<tr>
							<td colspan="4"><div>审核信息</div></td>
						</tr>
					</thead>
					<tbody>
                        <tr>
                            <th>审核状态：</th>
                            <td>
                                <c:if test="${order.sodAppIsok==0}"><img class="imgAlign" src="crm/images/content/fail.gif" alt="未通过">&nbsp;未通过</c:if>
                            	<c:if test="${order.sodAppIsok==1}"><img class="imgAlign" src="crm/images/content/suc.gif" alt="已通过">&nbsp;已通过</c:if>&nbsp;
                            </td>
                            <th>审核人：</th>
                            <td>${order.sodAppMan}&nbsp;<label id="sodAppDate"></label></td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>审核记录：</th>
                            <td colspan="3" style="font-weight:normal;">
                            <c:if test="${!empty order.sodAppDesc}">
							<div id="allAppDesc" style="display:none;">
                            	${order.sodAppDesc}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someAppDesc','allAppDesc')">-&nbsp;点击收起</a>
							</div>
							<div id="someAppDesc">
                                <div id="appDesc">${order.sodAppDesc}</div>
                                <a id="appMoreButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allAppDesc','someAppDesc')">+&nbsp;点击展开</a>
                            </div>
                            </c:if>
                            <c:if test="${empty order.sodAppDesc}">&nbsp;</c:if>
							</td>
                        </tr>
                        <tr class="noBorderBot">
                            <td colspan="4" style="text-align:center; padding-bottom:5px">
                                <c:if test="${order.sodAppIsok==1}">
                                	<div id="cancelAppLayer" class='grayBg' style="display:none">
                                    	<button class='butSize3' onClick="ordPopDiv(13,'${order.sodCode}')">撤销审核</button>
                                    </div>
                                	<script type="text/javascript">displayLimAllow("s030","cancelAppLayer");</script>
                                </c:if>
                                <c:if test="${order.sodAppIsok!=1}">
                                	<div id="appOrdLayer" style="display:none">
                                		<div class="orangeBack"><input type="button" class="butSize3" value="审核订单" onClick="$('appOrd').show()"/></div>
                                        <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                            <form action="orderAction.do" method="post" id="app">
                                                <input type="hidden" name="op" value="appOrd"/>
                                                <input type="hidden" name="code" value="${order.sodCode}"/>
                                                <input type="hidden" name="type" value="1"/>
                                                <div class="appLayer">
                                                    <div><input type="radio" name="sodAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                            <input type="radio" name="sodAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                    </div>
                                                    <div class="appLayerTitle" style="padding:5px; padding-top:10px;">审核内容</div>
                                                    <div style="text-align:center"><textarea rows="4" style="width:100%" name="sodAppDesc" onBlur="autoShort(this,4000)"></textarea></div>
                                                    <div style="text-align:center">
                                                        <input type="button" class="butSize1" id="dosave" value="提交">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <input type="button" class="butSize1" id="hiddenApp" value="取消">
                                                    </div>
                                                </div>
                                            </form>
                                            <script type="text/javascript">
                                                $("dosave").onclick=function(){
                                                    waitSubmit("dosave","保存中...");
                                                    return $("app").submit();
                                                }
                                                $("hiddenApp").onclick=function(){
                                                    $("appOrd").hide();
                                                }
                                            </script>
                                        </div>
                                    </div>
                                	<script type="text/javascript">displayLimAllow("s030","appOrdLayer");</script>
                                </c:if>
                                
                            </td>
                        </tr>
                    </tbody>
                    <c:if test="${order.sodAppIsok==1}">
                    <thead>
                        <tr>
                            <td colspan="4"><div>关联数据</div></td>
                        </tr>
                    </thead>
                    </c:if>
                 </table>
				<c:if test="${order.sodAppIsok==0}">
                    <div class="orangeBox" style="padding:10px; text-align:center;">
                        <span class="brown">请等待审核通过...</span>
                    </div>
                 </c:if>
                
                <c:if test="${order.sodAppIsok==1}">
                    <!-- 新建回款的时候传入回款页面 -->
                    <input type="hidden" id="ordCode" value="${order.sodCode}"/>
                    <input type="hidden" id="ordTil" value="<c:out value="${order.sodTil}"/>"/>
                    <input type="hidden" id="ordCusName" value="<c:out value="${order.cusCorCus.corName}"/>"/>
                    <input type="hidden" id="ordCusCode" value="${order.cusCorCus.corCode}"/>
                    <input type="hidden" id="ordSeId" value="${order.salEmp.seNo}"/>
                    <input type="hidden" id="ordSeName" value="<c:out value="${order.salEmp.seName}"/>"/>
                    <div class="xpTab">
                        <!--<span id="xpTab1" class="xpTabGray" onClick="swapTab(1,4,'paidAction.do?op=toListOrdSpp&ordId=${order.sodCode}')">
                          &nbsp;回款计划&nbsp;
                        </span>-->
                        <span id="xpTab1" class="xpTabGray" onClick="swapTab(1,2,'orderAction.do?op=toListOrdPShipment&ordId=${order.sodCode}')">
                          &nbsp;发货记录&nbsp;
                        </span>
						<span id="xpTab2" class="xpTabGray" onClick="swapTab(2,2,'paidAction.do?op=toListOrdSps&ordId=${order.sodCode}')">
                          &nbsp;回款记录&nbsp;
                        </span>
                        
						<!--<span id="xpTab3" class="xpTabGray" style="display:none" onClick="swapTab(3,4,'invAction.do?op=listOrdInv&ordId=${order.sodCode}')">
                          &nbsp;开票记录&nbsp;
                        </span>-->
                        <!--<span id="xpTab4" class="xpTabGray" style="display:none" onClick="swapTab(4,4,'orderAction.do?op=getWwoList&sodCode=${order.sodCode}')">
                          &nbsp;出库安排&nbsp;
                        </span>-->
                        <script type="text/javascript">
							/*var rigs = "b036|b037|af024";
							var ids = new Array("xpTab1","xpTab2","xpTab3","xpTab4");*/
							/*var rigs = "b036|b037";
							var ids = new Array("xpTab1","xpTab2");
							displayLimAllow(rigs,ids,loadXpTabSel);*/
                        </script>
                    </div>
                    <div class="HackBox"></div>
                    <div id="ifrContent" class="tabContent" style="display:none">
                   		 <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
               		</div>
                 </c:if>
                <div class="descStamp">
                    由
                    <span>${order.sodInpCode}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<c:if test="${!empty order.sodChangeUser}">，最近由
                    <span>${order.sodChangeUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </c:if>
                </div>
            </div>
	    </div>
  	 	</c:if>
	 	<c:if test="${empty order}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该订单已被删除</div>
		</c:if>
    
	</div>
  </body>

</html>
