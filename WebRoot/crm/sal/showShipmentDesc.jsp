<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示发货单详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    #updShipNumForm { padding:0; margin:0; }
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
		function initPage(){
			if("${psh}"!=""){
				removeTime("deliveryDate","${psh.pshDeliveryDate}",2);
			}
		}
		
		function toUpdShipNum(){
			if($("doSaveLayer").style.display=="none"){
				if(rspIds.length>0){
					for(var i=0; i<rspIds.length; i++){
						$("prodNumTxt"+rspIds[i]).hide();
						$("prodNum"+rspIds[i]).show();
						$("prodOutNumTxt"+rspIds[i]).hide();
						$("prodOutNum"+rspIds[i]).show();
						$("shipNumATxt"+rspIds[i]).hide();
						$("shipNumA"+rspIds[i]).show();
						$("shipNumBTxt"+rspIds[i]).hide();
						$("shipNumB"+rspIds[i]).show();
					}
					$("doSaveLayer").show();
				}
				else{
					alert("此发货单无发货明细，无法进行调整！");
				}
			}
		}
		function updShipNum(){
			waitSubmit("doSaveBtn");
			waitSubmit("doCancelBtn");
			var url = "orderAction.do";
			var pars = $("updShipNumForm").serialize(true);
			pars.op="updShipNum";
			pars.rspIds = rspIds.join(",");
			new Ajax.Request(url, {
				method: 'post',
				parameters: pars,
				onSuccess: function(transport) {
					var saveRs = transport.responseText;
					if(saveRs==1){
						loadRspList();
						$("doSaveLayer").hide();
					}
					else{
						alert("保存失败，请重新保存");
					}
					restoreSubmit("doSaveBtn");
					restoreSubmit("doCancelBtn");
				},
				onFailure: function(response){
					if (response.status == 404)
						alert("您访问的地址不存在！");
					else
						alert("Error: status code is " + response.status);
				}
			});
		}
		function cancelUpdShipNum(){
			if($("doSaveLayer").style.display!="none"){
				if(rspIds.length>0){
					for(var i=0; i<rspIds.length; i++){
						$("prodNumTxt"+rspIds[i]).show();
						$("prodNum"+rspIds[i]).hide();
						$("prodOutNumTxt"+rspIds[i]).show();
						$("prodOutNum"+rspIds[i]).hide();
						$("shipNumATxt"+rspIds[i]).show();
						$("shipNumA"+rspIds[i]).hide();
						$("shipNumBTxt"+rspIds[i]).show();
						$("shipNumB"+rspIds[i]).hide();
					}
					$("doSaveLayer").hide();
				}
			}
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			rspIds.push(obj.rshpId);
			var shipCount = "<span id='prodNumTxt"+obj.rshpId+"'>"+obj.rshpCount+"</span><input type='text' style='display:none;width:90%;' id='prodNum"+obj.rshpId+"' name='prodNum"+obj.rshpId+"' value='"+obj.rshpCount+"' />";
			var shipOutCount = "<span id='prodOutNumTxt"+obj.rshpId+"'>"+obj.rshpOutCount+"</span><input type='text' style='display:none;width:90%;' id='prodOutNum"+obj.rshpId+"' name='prodOutNum"+obj.rshpId+"' value='"+obj.rshpOutCount+"' />";
			var rspUnit = (obj.rshpUnit1?obj.rshpUnit1:"<span class='gray'>无</span>")+"/"+(obj.rshpUnit2?obj.rshpUnit2:"<span class='gray'>无</span>");
			var rspPackCount = "<span id='shipNumATxt"+obj.rshpId+"'>"+obj.rshpPackCount1+"</span><input type='text' style='display:none;width:40%;' id='shipNumA"+obj.rshpId+"' name='shipNumA"+obj.rshpId+"' value='"+obj.rshpPackCount1+"' />+<span id='shipNumBTxt"+obj.rshpId+"'>"+obj.rshpPackCount2+"</span><input type='text' style='display:none;width:40%;' id='shipNumB"+obj.rshpId+"' name='shipNumB"+obj.rshpId+"' value='"+obj.rshpPackCount2+"' />";
			var rspAmt = parseFloat(obj.rshpPackCount1>0?(obj.rshpAmt1*obj.rshpPackCount1):0)+parseFloat(obj.rshpPackCount2>0?(obj.rshpPackCount2*obj.rshpAmt2):0);
			datas = [obj.rshpProd?obj.rshpProd.wprName:"",obj.rshpPrice, shipCount,shipOutCount, obj.rshpProdAmt, obj.rshpSalBack, rspUnit, rspPackCount, rspAmt];
			return [datas,className,dblFunc,dataId];
		}
		function loadRspList(sortCol,isDe,pageSize,curP){
			rspIds = [];
			var url = "orderAction.do";
			var pars = [];
			pars.op = "listPshProds";
			pars.pshId = "${psh.pshId}";
			var loadFunc = "loadRspList";
			var cols=[
				{name:"名称",align:"left",width:"10%"},
				{name:"订单价格",align:"right",renderer:"money",width:"10%"},
				{name:"发货数",width:"10%"},
				{name:"外购件数量",width:"10%"},
				{name:"发货金额",align:"right",renderer:"money",width:"10%"},
				{name:"提成",align:"right",renderer:"money",width:"10%"},
				{name:"计价单位",width:"20%"},
				{name:"包装数量",width:"10%"},
				{name:"运费",align:"right",renderer:"money",width:"10%"}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var rspIds = [];
		var gridEl = new MGrid("rspListTab","dataList");
		gridEl.config.hasPage=false;
		gridEl.config.sortable=false;
		gridEl.config.isResize=false;
		createProgressBar();	
		window.onload=function(){
			initPage();
			loadRspList();
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
      	<c:if test="${!empty psh}">
		<div id="contentbox">
        	<div class="descInf">
            	<div id="descTop">
                发货单详细信息<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th class="descTitleL">发货单编号：</th>
							<th class="descTitleR" colspan="3">${psh.pshNum}&nbsp;</th>
						</tr>
					</thead>
					<tbody>
                    	<tr>
                            <th class="blue">对应订单：</th>
                            <td class="blue mlink">
                                <c:if test="${!empty psh.pshOrder}">
                                    <a href="javascript:void(0)" onClick="descPop('orderAction.do?op=showOrdDesc&code=${psh.pshOrder.sodCode}')"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${psh.pshOrder.sodTil}</a>
                                </c:if>&nbsp;
                            </td>
							<th class="blue">对应客户：</th>
							<td class="blue">
								<c:if test="${!empty psh.pshOrder.cusCorCus}">
                                    ${psh.pshOrder.cusCorCus.corName}
                                </c:if>&nbsp;
							</td>
						</tr>
						<tr>
							<th>收货方：</th>
							<td colspan="3">${psh.pshConsignee}&nbsp;</td>
						</tr>
                        <tr>
                        	<th>收货地址：</th>
							<td colspan="3">${psh.pshAddr}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>联系电话：</th>
							<td>${psh.pshPho}&nbsp;</td>
							<th>邮编：</th>
							<td>${psh.pshPostCode}&nbsp;</td>
						</tr>
                        <tr>
                        	<th>发货总金额：</th>
							<td>
                            	<c:if test="${!empty psh.pshProdAmt}">￥<fmt:formatNumber value="${psh.pshProdAmt}" pattern="#,##0.00"/></c:if>&nbsp;
                            </td>
                            <th>提成合计：</th>
                            <td><c:if test="${!empty psh.pshSalBack}">￥<fmt:formatNumber value="${psh.pshSalBack}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        </tr>
                        <tr> 
							<th>物流公司：</th>
							<td>${psh.pshExpress}&nbsp;</td>
                        	<th>总运费：</th>
                            <td colspan="3"><c:if test="${!empty psh.pshAmt}">￥<fmt:formatNumber value="${psh.pshAmt}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>发货人：</th>
                            <td>${psh.pshShipper}&nbsp;</td>
                            <th>发货日期：</th>
                            <td><span id="deliveryDate"></span>&nbsp;</td>
                        </tr>
						<tr>
                            <th>备注：</th>
                            <td colspan="3">${psh.pshRemark}&nbsp;</td>
                        </tr>
                  	</tbody>
                    <tbody>
						<tr class="noBorderBot">
							<th>发货明细：</th>
                            <td colspan="3"><a href="javascript:void(0)" onClick="toUpdShipNum()">[调整发货数量]</a></td>
						</tr>
                        <tr>
                            <td colspan="4" class="subTab">
                            <form id="updShipNumForm">
                            	<div id="dataList" class="dataList" style="height:auto"></div>
                                <div id="doSaveLayer" class="submitLayer" style="display:none">
                                <button id="doSaveBtn" onClick="updShipNum()">保存</button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button id="doCancelBtn" onClick="cancelUpdShipNum()">取消</button></div>
                            </form>
                            </td>
                        </tr>
					</tbody>
                 </table>
            </div>
	    </div>
  	 	</c:if>
	 	<c:if test="${empty psh}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该发货记录已被删除</div>
		</c:if>
	</div>
  </body>

</html>
