<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    
    <title>订单明细</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
	#contentbox {
		padding:10px;
	}
	#leftProdTree {
		float:left;
		width:25%;
		border:#fba01f 1px solid;
		padding:1px;
		margin-right:5px;
	}
	#leftProdTreeTop {
		background-color:#fba01f;
		padding:5px;
		color:#FFF;
		text-align:left;
		font-weight:bold;
	}
	#topOrdInf {
		padding:6px;
		width:100%;
		text-align:left;
	}
	#ordDesLayer {
		text-align:center;
		padding-top:5px;
	}
	#ordDesLayer table {
		font-size:12px;
		width:100%;
	}
	#ordDesLayer th,#ordDesLayer td {
		padding:4px 2px 2px 2px;
	}
	#ordInfTab th {
		text-align:right;
		width:100px;
		background:#d9e0e9;
	}
	#ordInfTab td {
		text-align:left;
		border-bottom:1px solid #d9e0e9;
	}
	#cusProdTil {
		text-align:left;
		padding:4px;
		/*margin:5px 0 0 0;*/
		background-color:#617ca1;
		color:#fff;
	}
	#cusProdTil span {
		font-weight:bold;
		padding:0 4px ;
	}
	#cusProdTab {
		text-align:center;
		border:#666 1px solid;
	}
	#cusProdTab th {
		background:#666;
		color:#fff;
	}
	#cusProdTab td {
		padding:4px;
	}
	#ordDesc td {
		text-align:center;
	}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/choosePro.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
		function showLeftTree(obj){
			if($("leftProdTree").style.display=="none"){
				$("leftProdTree").show();
				$("showLeftBtn").hide();
			}
			else{
				$("leftProdTree").hide();
				$("showLeftBtn").show();
			}
		}
		
		function changePrice(n,obj,type){ 
			var isChange=false;
			
			//检查输入格式
			if(obj.value==""){
				isChange=true;
			}
			else {
				switch(type){
				case 'mon': if(checkPrice(obj)){ isChange=true; } break;
				case 'num': if(checkIsNum(obj)){ isChange=true; } break;
				}
			}
			if(isChange){
				var price=$("price"+n).value;
				var num=$("num"+n).value;
				var total=(price*num);
				if(parseInt(total)>=999999999)
					total=999999999;
				total=formatFloat(total);
				$("totalPrc"+n).innerHTML=total;
				$("allPrice"+n).value=total;
			}
		}
		
		function setTaxRate(n){
			var hasTax=$("hasTax"+n).checked;
			var taxRate=getSalTaxRate();
			$("taxRate"+n).value=hasTax?taxRate:"";
		}
		
		/*function changeTotal(n,obj){
			if(checkPrice(obj)){
				var taxPer=document.getElementById("taxPer"+n).value;
				var num=document.getElementById("num"+n).value;
				var allPrice=document.getElementById("allPrice"+n).value;
				var newzk=(allPrice/num);
				document.getElementById("price"+n).value=newzk;
				document.getElementById("tax"+n).innerHTML=allPrice*(taxPer/100);
			}
		}
		function changeTax(n,obj){
			if(checkPrice(obj)){
				var allPrice=document.getElementById("allPrice"+n).value;
				var taxPer=document.getElementById("taxPer"+n).value;
				document.getElementById("tax"+n).innerHTML=allPrice*(taxPer/100);
			}
			else{
				document.getElementById("tax"+n).innerHTML="";
			}
		}*/
		
    	function saveRop(){
			waitSubmit("dosave","保存中...");
			return $("ropForm").submit();
		}
		
		createProgressBar();	
		window.onload=function(){
			closeProgressBar();
		}
    </script>
  </head>
  <body>
      <div id="mainbox">
    	<div id="contentbox">
            <div id="listContent">
            	<div id="leftProdTree">
                    <div id="leftProdTreeTop">从所有产品中选择</div>
                    <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                    <iframe frameborder="0" scrolling="auto" width="100%" onload="loadAutoH(this,'ifmLoad')" src="prodAction.do?op=searchProType&ord=ord&cusId=${order.cusCorCus.corCode}"></iframe>
                </div>
                <div>
                	<div id="topOrdInf" class="grayBack">
                        <img id="hideImg" src="crm/images/content/hide.gif" onClick="showHide('ordDesLayer','hideImg')" style="cursor:pointer;" class="imgAlign" alt="点击收起"/>&nbsp;<span class="blue">订单信息</span>
                        <div id="ordDesLayer">
                            <table id="ordInfTab" cellpadding="0" cellspacing="0">
                                <tr>
                                    <th><nobr>订单主题：</nobr></th>
                                    <td colspan="3"> ${order.sodTil}&nbsp;</td>
                                </tr>
                                <tr>
                                    <th>订单号：</th>
                                    <td style="width:40%">${order.sodNum}&nbsp;</td>
                                    <th>总金额：</th>
                                    <td><c:if test="${!empty order.sodSumMon}">￥<fmt:formatNumber value="${order.sodSumMon}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                                </tr>
                            </table>
                            <div id="cusProdTil">订单客户<span>${order.cusCorCus.corName}</span>的产品策略</div>
                            <table id="cusProdTab" cellpadding="0" cellspacing="0">
                            	<tr><th style="width:20%">产品名称</th><th style="width:20%">产品别名</th><th style="width:20%">产品编号</th><th style="width:20%">销售单价</th><th style="width:20%">备注</th></tr>
                                <c:if test="${!empty cusProdList}">
                            	<c:forEach var="cusProd" items="${cusProdList}" varStatus="cpIndex">
                                <tr id="cpRow${cpIndex.index}" onMouseOver="focusTr('cpRow','${cpIndex.index}','1')" onMouseOut="focusTr('cpRow','${cpIndex.index}','0')">
                                <td><a href="javascript:void(0)" onClick="tbladdrow('ordDesc','${cusProd.wmsProduct.wprId}','<c:out value="${cusProd.wmsProduct.wprName}"/>','<c:out value="${cusProd.wmsProduct.wprCode}"/>','<c:out value="${cusProd.wmsProduct.typeList.typName}"/>','<fmt:formatNumber value="${cusProd.wmsProduct.wprSalePrc}" pattern="#0.00"/>','<fmt:formatNumber value="${cusProd.cpPrice}" pattern="#0.00"/>','${cusProd.cpHasTax}')">${cusProd.wmsProduct.wprName}</a>&nbsp;</td>
                                <td>${cusProd.cpOtherName}&nbsp;</td>
                                <td>${cusProd.wmsProduct.wprCode}&nbsp;</td>
                                <td>
                                <fmt:formatNumber value="${cusProd.cpPrice}" pattern="#0.00"/>&nbsp;
                                <c:if test="${cusProd.cpHasTax=='1'}"><span class='deepGreen'>[含税价]</span></c:if>
                                <c:if test="${cusProd.cpHasTax=='0'}"><span class='blue'>[不含税价]</span></c:if>
                                </td>
                                <td>${cusProd.cpRemark}&nbsp;</td></tr>
                                <script type="text/javascript">rowsBg("cpRow","${cpIndex.index}")</script>
                                </c:forEach>
                                </c:if>
                                <c:if test="${empty cusProdList}"><tr><td colspan="4" style="text-align:center; background:#fff;">无产品策略</td></tr></c:if>
                            </table>
                        </div>
                    </div>
                    <div class="dataList">
                        <form id="ropForm" action="orderAction.do" method="post" name="register" >
                        <input type="hidden" name="wprId" id="wprCode" value=""/>
                        <input type="hidden" name="sodCode" value="${order.sodCode}"/>
                        <input type="hidden" name="op" value="updRop"/>
                        <table id="ordDesc" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <th style="width:20%">产品名称</th>
                                <th style="width:10%">编号</th>
                                <th style="width:10%">标准单价</th>
                                <th style="width:10%">订单单价</th>
                                <th style="width:6%">数量</th>
                                <th style="width:6%">单位</th>
                                <th style="width:3%">不含税</th>
                                <th style="width:10%">总价</th>
                                <th style="width:20%">备注</th>
                                <th style="border-right:0px; width:5%" colspan="2">操作</th>
                            </tr>
                             <c:if test="${!empty order.ROrdPros}">
                             <c:forEach var="rop" items="${order.ROrdPros}">
                              <tr>
                                 <td>${rop.wmsProduct.wprName}&nbsp;</td>
                                 <td>${rop.wmsProduct.wprCode}&nbsp;</td>
                                 <td><fmt:formatNumber value="${rop.wmsProduct.wprSalePrc}" pattern="#0.00"/>&nbsp;</td>
                                 <td>
                                    <input class="inputSize2" style="width:95%;" type="text" id="price${rop.wmsProduct.wprId}" name="price${rop.wmsProduct.wprId}" value="<fmt:formatNumber value='${rop.ropPrice}' pattern='#0.00'/>" onKeyUp="changePrice('${rop.wmsProduct.wprId}',this,'mon')" />
                                 </td>
                                 <td>
                                    <input class="inputSize2" style="width:95%;" type="text" id="num${rop.wmsProduct.wprId}" name="num${rop.wmsProduct.wprId}"  value="<fmt:formatNumber value='${rop.ropNum}' pattern='#0.00'/>" onKeyUp="changePrice('${rop.wmsProduct.wprId}',this,'num')" />
                                 </td>
                                 <td>
                                    <c:if test="${!empty rop.wmsProduct.typeList}">${rop.wmsProduct.typeList.typName}</c:if>&nbsp;
                                 </td>
                                 <td>
                                    <input type="checkbox" id="hasTax${rop.wmsProduct.wprId}" onClick="setTaxRate('${rop.wmsProduct.wprId}')" <c:if test="${!empty rop.ropZk}">checked</c:if> /><input type="hidden" id="taxRate${rop.wmsProduct.wprId}" name="taxRate${rop.wmsProduct.wprId}" value="${rop.ropZk}" />
                                 </td>
                                 <td>
                                    <span id="totalPrc${rop.wmsProduct.wprId}"><fmt:formatNumber value='${rop.ropRealPrice}' pattern='#0.00'/>&nbsp;</span><input type="hidden" id="allPrice${rop.wmsProduct.wprId}" name="allPrice${rop.wmsProduct.wprId}" value="${rop.ropRealPrice}" />
                                 </td>
                                 <td>
                                    <textarea rows="1" name="remark${rop.wmsProduct.wprId}" style="width:95%;" onBlur="autoShort(this,500)">${rop.ropRemark}</textarea>
                                 </td>
                                 <td style="text-align:center;border-right:0px;">
                                    <img src="crm/images/content/del.gif" onClick="delTable(this,'ordDesc')" alt="删除" style="cursor:pointer"/>
                                    <input style="display:none;" type="checkbox" name="wprId" checked="checked" value="${rop.wmsProduct.wprId}" />
                                 </td>			
                              </tr>
                                </c:forEach>
                                <tr>
                              </tr>
                            </c:if>
                            </table>
                            <div style="text-align:center; margin-top:8px;">
                                <input type="button" onClick="saveRop()" id="dosave" class="butSize3" value="保存明细"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onClick="window.close()" value="取消" />
                            </div>
                        </form>
                    </div>
                </div>
             </div>
		</div>
	</div>	
  </body>
</html>
