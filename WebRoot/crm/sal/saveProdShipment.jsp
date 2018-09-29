<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>保存发货记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    .deepBlueBg {
		padding:4px;
	}
	#savePshForm,#savePshProdForm {
		padding:0;
		margin:0;
	}
	#sucLayer, #failLayer {
		border:#e6db55 1px solid;
		background:#ffffe0;
		color:#333;
		padding:8px;
		text-align:center;
	}
	#failLayer { color:#FF0000; }
	#dataList td {
		height:55px;
	}
    </style>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		var addrListJSON = null;
		function initPage(){
			getAddrList();
		}
		function getAddrList(){
			if(addrListJSON==null){
				var url="customAction.do";
				var pars=[];
				pars.op="listCusAddrAll";
				pars.cusId="${order.cusCorCus.corCode}";
				new Ajax.Request(url,{
					method : 'post',
					parameters : pars,
					onSuccess : function(response){
						if(response.responseText!=""){
							var jsonData = response.responseText.evalJSON();
							if(jsonData!=""){
								addrListJSON = jsonData.list;
							}
							loadAddrSel();
							loadCusAddr($("pshAddrId"));
						}
					},
					onfailure : function(response){
						if (transport.status == 404) { alert("您访问的url地址不存在！"); }
						else { alert("Error: status code is " + transport.status); }
					}
				});
			}
		}
		function loadAddrSel(){
			if(addrListJSON!=null){
				var addrSel = $("pshAddrId");
				for(var i=0;i<addrListJSON.length;i++){
					addrSel.add(new Option(addrListJSON[i].cadAddr,addrListJSON[i].cadId));
				}
			}
		}
		function loadCusAddr(obj){
			if(addrListJSON!=null){
				for(var i=0;i<addrListJSON.length;i++){
					if(obj.value==addrListJSON[i].cadId){
						$("provId").value=addrListJSON[i].cadProv?addrListJSON[i].cadProv.areId:"";
						$("cityId").value=addrListJSON[i].cadCity?addrListJSON[i].cadCity.prvId:"";
						$("districtId").value=addrListJSON[i].cadDistrict?addrListJSON[i].cadDistrict.cityId:"";
						$("pshCnee").value=addrListJSON[i].cadContact;
						$("pshPhone").value=addrListJSON[i].cadPho;
						$("pshPostCode").value=addrListJSON[i].cadPostCode;
						break;
					}
				}
			}
		}
		
		function savePsh(){
			var errStr = "";
			if(isEmpty("pshNum")){
				errStr+="- 未填写发货单编号！\n"; 
			}
			if(isEmpty("pshCnee")){   
				errStr+="- 未填写收货方！\n"; 
			}
			if(isEmpty("pshAddrId")){   
				errStr+="- 未选择收货地址！\n"; 
			}
			if(isEmpty("expId")){   
				errStr+="- 未选择物流公司！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				var url = "orderAction.do";
				var pars = $('savePshForm').serialize(true);
				pars.op="saveProdShipment";
				new Ajax.Request(url, {
					method: 'post',
					parameters: pars,
					onSuccess: function(transport) {
						var saveRs = transport.responseText;
						if(saveRs>0){
							loadProdTrans();
							$("pshId").value=saveRs;
							var formInputs = $$("#savePshForm input");
							var formSels = $$("#savePshForm select");
							for(var i=0;i<formInputs.length;i++){
								formInputs[i].disabled=true;
							}
							for(var j=0;j<formSels.length;j++){
								formSels[j].disabled=true;
							}
							for(var k=0; k<prodIds.length; k++){
								$("num"+prodIds[k]).disabled=false;
								$("outNum"+prodIds[k]).disabled=false;
								$("selA"+prodIds[k]).disabled=false;
								$("numA"+prodIds[k]).disabled=false;
								$("selB"+prodIds[k]).disabled=false;
								$("numB"+prodIds[k]).disabled=false;
							}
							$("pshRemark").disabled=true;
							$("doSavePsh").hide();
							$("lockIcon").show();
							$("doUpdPsh").show();
							$("doSavePshProd").disabled=false;
						}
					},
					onFailure: function(response){
						if (response.status == 404)
							alert("您访问的地址不存在！");
						else
							alert("Error: status code is " + response.status);
					}
				});
			}
		}
		
		function toUpdPsh(){
			var formInputs = $$("#savePshForm input");
			var formSels = $$("#savePshForm select");
			for(var i=0;i<formInputs.length;i++){
				formInputs[i].disabled=false;
			}
			for(var j=0;j<formSels.length;j++){
				formSels[j].disabled=false;
			}
			for(var k=0; k<prodIds.length; k++){
				$("num"+prodIds[k]).disabled=true;
				$("selA"+prodIds[k]).disabled=true;
				$("selB"+prodIds[k]).disabled=true;
				$("numA"+prodIds[k]).disabled=true;
				$("numB"+prodIds[k]).disabled=true;
			}
			$("pshRemark").disabled=false;
			$("doSavePsh").show();
			$("lockIcon").hide();
			$("doUpdPsh").hide();
			$("doSavePshProd").disabled=true;
		}
		
		function loadProdTrans(){
			var url = "prodAction.do";
			var pars = $('savePshForm').serialize(true);
			pars.op = "listPtrByProdIds";
			pars.prodIds = prodIds.join(",");
			new Ajax.Request(url, {
				method: 'post',
				parameters: pars,
				onSuccess: function(response) {
					for(var i=0; i<prodIds.length; i++){
						$("selA"+prodIds[i]).options.length=1;
						$("selB"+prodIds[i]).options.length=1;
					}
					if(response.responseText!=""){
						jsonData = response.responseText.evalJSON();
						var listObj = jsonData.list;
						if(listObj!=undefined && listObj!=""){
							var obj,selObj;
							for(var i=0; i<listObj.length; i++){
								obj = listObj[i];
								selObj=$("selA"+obj.ptrProdId);
								if(selObj!=null) { selObj.add(new Option(changeTwoDecimal(obj.ptrAmt)+"（"+obj.ptrUnit+"）",obj.ptrAmt)); }
								selObj=$("selB"+obj.ptrProdId);
								if(selObj!=null) { selObj.add(new Option(changeTwoDecimal(obj.ptrAmt)+"（"+obj.ptrUnit+"）",obj.ptrAmt)); }
							}
							for(var i=0; i<prodIds.length; i++){ setAmtSum(prodIds[i]); }
						}
					}
				},
				onFailure: function(response){
					if (response.status == 404)
						alert("您访问的地址不存在！");
					else
						alert("Error: status code is " + response.status);
				}
			});
		}
		
		function setAmtSum(prodId){
			if(checkPrice($("num"+prodId))){
				var prodNum = $("num"+prodId).value;//发货数
				var prodPrc = $("prodPrc"+prodId).value;//订单价格
				if(prodNum==""||prodNum<0){ prodNum=0; }
				$("prodAmtStr"+prodId).innerHTML = changeTwoDecimal(prodPrc*prodNum);
				$("prodAmt"+prodId).value=prodPrc*prodNum;
			}
		}
		function setTransSum(prodId){
			if(checkPrice($("num"+prodId))){
				var transAmt1 = $("selA"+prodId).value;//运费单价1
				var prodNum1 = $("numA"+prodId).value;//包装数1
				var transAmt2 = $("selB"+prodId).value;//运费单价2
				var prodNum2 = $("numB"+prodId).value;//包装数2
				if(transAmt1==""||transAmt1<0){ transAmt1=0; }
				if(transAmt2==""||transAmt2<0){ transAmt2=0; }
				if(prodNum1==""||prodNum1<0){ prodNum1=0; }
				if(prodNum2==""||prodNum2<0){ prodNum2=0; }
				$("shipAmt"+prodId).innerHTML = (transAmt1*prodNum1+transAmt2*prodNum2);
				$("transUnitA"+prodId).value=$("selA"+prodId).options[$("selA"+prodId).selectedIndex].text;
				$("transUnitB"+prodId).value=$("selB"+prodId).options[$("selB"+prodId).selectedIndex].text;
				var totleAmt = 0;
				for(var i=0; i<prodIds.length; i++){
					if(parseFloat($("shipAmt"+prodIds[i]).innerHTML)>0){
						totleAmt+=parseFloat($("shipAmt"+prodIds[i]).innerHTML);
					}
				}
				$("totleTransAmt").innerHTML = "￥"+changeTwoDecimal(totleAmt);
				$("totalTransAmtInp").value = totleAmt;
			}
		}
		
		function savePshProd(){
			var errStr = "";
			var emptyCount = 0;
			for(var i=0; i<prodIds.length; i++){
				if($("num"+prodIds[i]).value!=""&&$("num"+prodIds[i]).value>0){ continue; }
				else{ emptyCount++; }
			}
			if(emptyCount==prodIds.length){
				alert("发货数不能为空"); 
			}
			else{
				waitSubmit("doSavePshProd");
				waitSubmit("doUpdPsh");
				var url = "orderAction.do";
				var pars = $('savePshProdForm').serialize(true);
				pars.op="savePshProd";
				pars.pshId = $("pshId").value;
				pars.prodIds = prodIds.join(",");
				new Ajax.Request(url, {
					method: 'post',
					parameters: pars,
					onSuccess: function(transport) {
						var saveRs = transport.responseText;
						if(saveRs==1){
							isSaved=true;
							$("sucLayer").show();
							$("failLayer").hide();
						}
						else{
							$("sucLayer").hide();
							$("failLayer").show();
							restoreSubmit("doSavePshProd");
							restoreSubmit("doUpdPsh");
						}
					},
					onFailure: function(response){
						if (response.status == 404)
							alert("您访问的地址不存在！");
						else
							alert("Error: status code is " + response.status);
					}
				});
			}
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			if(obj.wmsProduct){
				prodIds.push(obj.wmsProduct.wprId);
				dblFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.wmsProduct.wprId+"')";
				var proName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.wmsProduct.wprName+"</a>";
				var proUnit=obj.typeList?"("+obj.typeList.typName+")":"";
				datas = [
				obj.wmsProduct.wprCode, proName, obj.ropNum+proUnit, obj.ropShipNum>0?obj.ropShipNum:0, 
				"<input type='text' id='num"+obj.wmsProduct.wprId+"' style='width:90%' name='transNum"+obj.wmsProduct.wprId+"' onkeyup='setAmtSum("+obj.wmsProduct.wprId+")' disabled>",
				"<input type='text' id='outNum"+obj.wmsProduct.wprId+"' style='width:90%' name='outNum"+obj.wmsProduct.wprId+"' disabled>",
				"<span id='prodAmtStr"+obj.wmsProduct.wprId+"'></span><input type='hidden' id='prodAmt"+obj.wmsProduct.wprId+"' name='prodAmt"+obj.wmsProduct.wprId+"' /><input type='hidden' id='prodPrc"+obj.wmsProduct.wprId+"' name='prodPrc"+obj.wmsProduct.wprId+"' value='"+obj.ropPrice+"' /><input type='hidden' id='prodTax"+obj.wmsProduct.wprId+"' name='prodTax"+obj.wmsProduct.wprId+"' value='"+obj.ropZk+"' />",
				"<select style='width:95%' id='selA"+obj.wmsProduct.wprId+"' name='transAmtA"+obj.wmsProduct.wprId+"' onchange='setTransSum("+obj.wmsProduct.wprId+")' disabled><option></option></select><br/>&nbsp;<select style='width:95%' id='selB"+obj.wmsProduct.wprId+"' name='transAmtB"+obj.wmsProduct.wprId+"' onchange='setTransSum("+obj.wmsProduct.wprId+")' disabled><option></option></select><input type='hidden' id='transUnitA"+obj.wmsProduct.wprId+"' name='transUnitA"+obj.wmsProduct.wprId+"' /><input type='hidden' id='transUnitB"+obj.wmsProduct.wprId+"' name='transUnitB"+obj.wmsProduct.wprId+"' />",
				"<input type='text' id='numA"+obj.wmsProduct.wprId+"' style='width:90%' name='transNumA"+obj.wmsProduct.wprId+"' onkeyup='setTransSum("+obj.wmsProduct.wprId+")' disabled><br/>&nbsp;<input type='text' id='numB"+obj.wmsProduct.wprId+"' style='width:90%' name='transNumB"+obj.wmsProduct.wprId+"' onkeyup='setTransSum("+obj.wmsProduct.wprId+")' disabled>",
				"<span id='shipAmt"+obj.wmsProduct.wprId+"'></span>"
				];
				return [datas,className,dblFunc,dataId];
			}
			else{
				return [];
			}
		}
		function loadRopList(sortCol,isDe,pageSize,curP){
			prodIds = [];
			var url = "orderAction.do";
			var pars = [];
			pars.op = "listOrdPro";
			pars.ordId = "${order.sodCode}";
			var loadFunc = "loadRopList";
			var cols=[
				{name:"编号",width:"6%"},
				{name:"名称",align:"left",width:"16%"},
				{name:"订单数量",width:"8%"},
				{name:"已发货数",width:"8%"},
				{name:"发货数",width:"8%"},
				{name:"外购件数量",width:"8%"},
				{name:"发货金额",width:"10%"},
				{name:"运费单价(计价单位)",width:"22%"},
				{name:"包装数",width:"6%"},
				{name:"运费合计",align:"right",width:"8%"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var isSaved = false;
		var prodIds = [];
		var gridEl = new MGrid("prodShipListTab","dataList");
		gridEl.config.hasPage=false;
		gridEl.config.sortable=false;
		gridEl.config.isResize=false;
		createProgressBar();	
		window.onload=function(){
			initPage();
			loadRopList();
			closeProgressBar();
		}
		window.onbeforeunload = function(){ 
			if(!isSaved){ return "离开此页面将不能保存发货明细！"; }
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
      	<c:if test="${!empty order}">
		<div id="contentbox">
        	<div class="descInf" style="width:920px;">
            	<div class="deepBlueBg">发货信息<img id="lockIcon" class="imgAlign" src="crm/images/content/lock.gif" style="display:none" alt="锁定"/></div>
                <form id="savePshForm">
                    <input type="hidden" id="pshId" name="pshId"/>
                    <input type="hidden" name="ordId" value="${order.sodCode}"/>
                    <input type="hidden" id="provId" name="provId"/>
                    <input type="hidden" id="cityId" name="cityId"/>
                    <input type="hidden" id="districtId" name="districtId"/>
                    <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                        <tbody>
                            <tr>
                                <th>对应订单：</th>
                                <td colspan="3">${order.sodTil}&nbsp;</td>
                            </tr>
                            <tr> 
                                <th>发货单编号：<span class='red'>*</span></th>
                                <td><input type="text" style="width:95%" id="pshNum" name="prodShipment.pshNum" onBlur="autoShort(this,100)"></td>
                                <th>订单号：</th>
                                <td>${order.sodNum}&nbsp;</td>
                            </tr>
                            <tr>
                                <th>收货方：<span class='red'>*</span></th>
                                <td><input type="text" style="width:95%" id="pshCnee" name="prodShipment.pshConsignee" onBlur="autoShort(this,100)" value="${order.cusCorCus.corName}"></td>
                                <th>收货地址：<span class='red'>*</span></th>
                                <td><select class="inputSize2" style="width:95%" id="pshAddrId" name="pshAddrId" onChange="loadCusAddr(this)"></select></td>
                            </tr>
                            <tr> 
                                <th>联系电话：</th>
                                <td><input type="text" style="width:95%" id="pshPhone" name="prodShipment.pshPho" onBlur="autoShort(this,50)"></td>
                                <th>邮编：</th>
                                <td><input type="text" style="width:95%" id="pshPostCode" name="prodShipment.pshPostCode" onBlur="autoShort(this,50)"></td>
                            </tr>
                            <tr>
                                <th>物流公司：<span class='red'>*</span></th>
                                <td>
                                    <c:if test="${!empty expList}">
                                    <select id="expId" name="expId" class="inputSize2 inputBoxAlign">
                                        <option value="">请选择</option>
                                        <c:forEach var="expList" items="${expList}">
                                        <option value="${expList.typId}"/>${expList.typName}</option>
                                        </c:forEach>
                                    </select>
                                    </c:if>
                                    <c:if test="${empty expList}">
                                    <select id="expId" class="inputSize2 inputBoxAlign" disabled="disabled">
                                        <option>未添加</option>
                                    </select>
                                    </c:if>
                                    <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                                </td>
                                <th>总运费：</th>
                                <td><span id="totleTransAmt">&nbsp;</span></td>
                            </tr>
                            <tr>
                                <th>发货人：</th>
                                <td><input type="text" style="width:95%" name="prodShipment.pshShipper" onBlur="autoShort(this,100)"></td>
                                <th>发货日期：</th>
                                <td><input name="deliveryDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                            </tr>
                            <tr class="noBorderBot">
                                <th>备注：</th>
                                <td colspan="3"><textarea id="pshRemark" style="width:95%" name="prodShipment.pshRemark" rows="2" onBlur="autoShort(this,200)"></textarea>&nbsp;</td>
                            </tr>
                        </tbody>
                    </table>
                </form>
                <div class="submitLayer"><input id="doSavePsh" class="butSize3" type="button" value="保存" onClick="savePsh()" /><input id="doUpdPsh" class="butSize3" type="button" value="修改" onClick="toUpdPsh()" style="display:none" /></div>
                <div class="deepBlueBg">发货明细</div>
                <form id="savePshProdForm">
                <input type="hidden" id="totalTransAmtInp" name="totalAmt"/><div id="dataList" class="dataList" style="height:auto; margin:0;"></div><div class="submitLayer"><input id="doSavePshProd" class="butSize3" type="button" value="保存发货记录" onClick="savePshProd()" disabled /></div>
                </form>
                <div id="sucLayer" style="display:none">保存成功！ <a href="javascript:window.close()">关闭页面</a></div>
            	<div id="failLayer" style="display:none">保存失败！ <a href="javascript:window.close()">关闭页面</a></div>
            </div>
	    </div>
  	 	</c:if>
	 	<c:if test="${empty order}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该订单已被删除</div>
		</c:if>
	</div>
  </body>

</html>
