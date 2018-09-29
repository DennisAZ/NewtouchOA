<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int notEmpty=0;
int count=0;
int count2=0;
int count3=0;

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>订单明细-出库管理</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	 <%   
  response.setHeader("Pragma","No-cache");//HTTP   1.1   
  response.setHeader("Cache-Control","no-cache");//HTTP   1.0   
  response.setHeader("Expires","0");//防止被proxy，以上三个是设置禁止缓存   
  %>   
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/ord.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">
		function init(){
			var proCode=$N("wprId1");//头部产品Id
			var wms=$N("wms");//头部仓库编号
			var noneCount=0;//无需出库的数量
			for(var i=0; i<proCode.length;i++){
				var allold=0;//单个产品库存总数量
				for(var j=0; j<wms.length;j++){
				 	var oldnum=$(proCode[i].value+"nl"+wms[j].value);//库存数量
					//库存的总数量
					if(oldnum!=null&&oldnum.value!=0){
						allold+=parseFloat(oldnum.value);
					}
				}
				if(parseFloat(allold)==0){
					$("name"+proCode[i].value).className="red";
					$("isempty"+proCode[i].value).src="images/content/reddatabase.gif";
					$("isempty"+proCode[i].value).alt="无库存";
				}
			}
			//没有可出库的产品 （无计算库存或库存为空）
			
			if($("proE")==null){
				$("outButton").style.display="none";
				$("emptyOut").style.display="block";
			}
			else{
				//需要出库的产品数量
				var proCode1=$N("wprId");//底部产品Id
				for(var i=0; i<proCode1.length;i++){
					var total=$("xc"+proCode1[i].value).innerText;//未安排的数量
					if(parseFloat(total)<=0){
						//屏蔽没有未安排数量的产品
						for(var j=0; j<wms.length;j++){
							$(proCode1[i].value+"n"+wms[j].value).value="无";
							$(proCode1[i].value+"n"+wms[j].value).disabled=true;
						}
						//灰化
						$("ropNa"+proCode1[i].value).className="gray";
						$("ropNm"+proCode1[i].value).className="gray";
						$("xc"+proCode1[i].value).className="gray";
						noneCount++;
					}
				}
				//没有需要出库的产品（未安排数量为0）
				if(proCode1.length==noneCount){
					$("outButton").style.display="none";
					$("noOut").style.display="block";
				}
			}
		}
		
		function check(){
			var errStr = "";
			var savable=true;
			var proCode=$N("wprId");//底部产品Id
			var wms=$N("wms");//头部仓库编号
			for(var i=0; i<proCode.length;i++){
				var allnum=0;//填写的总数量
				var total=$("xc"+proCode[i].value).innerText;//未安排的数量
				var wprName=$("wprName"+proCode[i].value).value;//未安排的产品名称
				if(parseFloat(total)>0){
					for(var j=0; j<wms.length;j++){
						 var oldnum=$(proCode[i].value+"nl"+wms[j].value);//库存数量
						 var num=$(proCode[i].value+"n"+wms[j].value)//填写的数量
						 var wmsName=$("wmsName"+wms[j].value).value//仓库名称
						if(num!=null && num.value!="" ){
							allnum+=parseFloat(num.value);//填写的总数量
						}
						//填写的出库量与库存比较
						if(num.value!=""&&num.value!=0){
							if(parseFloat(num.value)>parseFloat(oldnum.value)){
								errStr+="- 产品 '"+wprName+"' 的出库量超过仓库 '"+wmsName+"' 的库存量！\n"; 
							}
						}
					}
					if(parseFloat(allnum)<=0){
						errStr+="- 未填写产品 '"+wprName+"' 的出库量！\n";
					}
					//填写的出库量与未安排数量比较
					if(parseFloat(total)<parseFloat(allnum)){
						errStr+="- 产品 '"+wprName+"' 的出库量超过未安排数量！\n";
					}
				}
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				return $("register").submit();
			}
		}
		
		
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab1");
			loadTabShort("tab2");
			loadTabShort("tab3");
			init();
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">订单管理 > 订单详细信息 > 生成出库单</div>
            <div class="descInf" style="width:98%">
			<form action="orderAction.do" method="post" id="register">
			<input type="hidden" name="op" value="newRwmsPro">
			<input type="hidden" name="sodCode" value="${sodCode}">
			<table cellpadding="0" cellspacing="0"  class="normal" align="center" width="100%">
				<tr>
					<td colspan="2"><div class="blue bold" style="padding:5px;">产品库存情况</div></td>
				</tr>
				<tr>
					<td style="width:30%">
                        <table id="tab1" cellpadding="0" cellspacing="0" class="normal rowstable">
                            <tr>
                                <th style="width:60%">产品名称/型号</th>
                                <th style="width:40%; border-right:0px">订单数量[单位]</th>
                            </tr>
                            <logic:notEmpty name="rordPro">
                                <logic:iterate id="rop" name="rordPro" scope="request">
                                <tr id="tr1<%=count%>">
                                    <logic:equal value="1" name="rop" property="wmsProduct.wprIscount">
                                        <td><input style="display:none" type="checkbox" name="wprId1" checked="checked" value="${rop.wmsProduct.wprId}"><img id="isempty${rop.wmsProduct.wprId}" src="images/content/database.gif" class="imgAlign" alt="计算库存"/><span id="name${rop.wmsProduct.wprId}">${rop.wmsProduct.wprName}<logic:notEmpty name="rop" property="wmsProduct.wprModel">/${rop.wmsProduct.wprModel}</logic:notEmpty></span></td>	
                                        <td><bean:write name="rop" property="ropNum" format="###,##0.00"/><logic:notEmpty name="rop" property="wmsProduct.typeList"><span class="brown">[${rop.wmsProduct.typeList.typName}]</span></logic:notEmpty>&nbsp;</td>
                                    </logic:equal>
                                    <logic:notEqual value="1" name="rop" property="wmsProduct.wprIscount">
                                        <td class="gray"><span id="name${rop.wmsProduct.wprId}">${rop.wmsProduct.wprName}<logic:notEmpty name="rop" property="wmsProduct.wprModel">/${rop.wmsProduct.wprModel}</logic:notEmpty></span></td>
                                        <td class="gray">${rop.ropNum}<logic:notEmpty name="rop" property="wmsProduct.typeList"><span class="brown">[${rop.wmsProduct.typeList.typName}]</span></logic:notEmpty>&nbsp;<span id="isempty${rop.wmsProduct.wprId}"></span></td>	
                                    </logic:notEqual>
                                </tr>
                                <script type="text/javascript">
                                	rowsBg('tr1',<%=count%>);
                                </script>
                                <%count++;%>
                                </logic:iterate>
                           </logic:notEmpty>
                        </table>
					</td>
					<td style="width:70%">
						<table id="tab2" cellpadding="0" cellspacing="0" class="normal rowstable" style=" width:100%;border-left-width:0px">
							<tr>
                             	<logic:notEmpty name="wmsStro">
                                	<logic:iterate id="wms" name="wmsStro" scope="request">
                                    <th style="width:10%">${wms.wmsName}</th>
                                    </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="wmsStro">
                                	<th style="border-right:0px; text-align:center" class="gray">暂未添加仓库</th>
                                </logic:empty> 
                            </tr>
							<logic:iterate id="rop" name="rordPro" scope="request">
							<tr id="tr2<%=count2%>">
							 <logic:notEmpty name="rstroPro">
					 			<logic:iterate id="wms" name="wmsStro" scope="request">
					 				<logic:iterate id="rsp" name="rstroPro" scope="request">
					 					<logic:equal name="rsp" property="wmsProduct.wprId" value="${rop.wmsProduct.wprId}">
									 		<logic:equal value="${rsp.wmsStro.wmsCode}" name="wms" property="wmsCode">
											 	<td>&nbsp;
												<logic:notEmpty name="rsp" property="rspProNum"><logic:greaterEqual value="${rop.ropNum}" name="rsp" property="rspProNum"><span class="blue bold"><bean:write name="rsp" property="rspProNum" format="###,##0.00"/></span>
                                                    </logic:greaterEqual><logic:lessThan value="${rop.ropNum}" name="rsp" property="rspProNum"><logic:equal value="0" name="rsp" property="rspProNum"><span class="gray"><bean:write name="rsp" property="rspProNum" format="###,##0.00"/></span>
                                                        </logic:equal>
                                                        <logic:notEqual value="0" name="rsp" property="rspProNum"><bean:write name="rsp" property="rspProNum" format="###,##0.00"/></logic:notEqual>
                                                    </logic:lessThan>
                                                    
												 	<input type="hidden" id="${rop.wmsProduct.wprId}nl${rsp.wmsStro.wmsCode}" value="${rsp.rspProNum}">
												 </logic:notEmpty>
												</td>
												<%notEmpty=1; %>
										 	</logic:equal>
										 </logic:equal>
									 </logic:iterate>
									  <%if(notEmpty==0){ %>
										<td><span title="无此产品" class="gray">&nbsp;—</span><input type="hidden" id="${rop.wmsProduct.wprId}nl${wms.wmsCode}" value="0"></td>
										<%}
									notEmpty=0; %>
								 </logic:iterate>
						  	</logic:notEmpty>
							<logic:empty name="rstroPro">
								<logic:iterate id="wms" name="wmsStro" scope="request">
								<td><span title="无此产品" class="gray">&nbsp;—</span></td>
								</logic:iterate>
							</logic:empty>
							</tr>
                            <script type="text/javascript">
								rowsBg('tr2',<%=count2%>);
							</script>
							<%count2++;%>
						</logic:iterate>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2"><div style="padding:5px; padding-top:15px;" class="blue bold">填写出库量<span class="gray" style="font-weight:500">(不计算库存和库存为空的产品将不显示在以下列表中)</span></div></td>
				</tr>
				<tr>
					<td colspan="2">
                        <table id="tab3" cellpadding="0" cellspacing="0" class="normal rowstable" style="width:100%;">
                            <tr>
                                <th style="width:10%">产品名称/型号</th>
                                <th style="width:10%">订单数量[单位]</th>
                                <th style="width:10%">未安排数量[单位]</th>
                                <logic:notEmpty name="wmsStro">
                                    <logic:iterate id="wms" name="wmsStro" scope="request">
                                        <th style="width:10%"><input style="display:none" type="checkbox" name="wms" checked="checked" value="${wms.wmsCode}"><input type="hidden" name="wmsName${wms.wmsCode}"  value="${wms.wmsName}">${wms.wmsName}&nbsp;</th>
                                    </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="wmsStro">
                                	<th style="border-right:0px; text-align:center" class="gray">暂未添加仓库</th>
                                </logic:empty> 
                        	</tr>
        			 		<logic:notEmpty name="rordPro1">
                  			<logic:iterate id="rop" name="rordPro1" scope="request">
                  			<logic:equal value="1" name="rop" property="wmsProduct.wprIscount">
                            <input type="hidden"  id="proE" value=""/>
                            <tr id="tr3<%=count3%>" onMouseOver="focusTr('tr3',<%= count3%>,1)" onMouseOut="focusTr('tr3',<%= count3%>,0)">
                            <input style="display:none" type="checkbox" name="wprId" checked="checked" value="${rop.wmsProduct.wprId}"><input type="hidden" id="wprName${rop.wmsProduct.wprId}" name="wprName${rop.wmsProduct.wprId}" value="${rop.wmsProduct.wprName}">
                            	<td id="ropNa${rop.wmsProduct.wprId}">${rop.wmsProduct.wprName}<logic:notEmpty name="rop" property="wmsProduct.wprModel">/${rop.wmsProduct.wprModel}</logic:notEmpty>&nbsp;</td>		
                            	<td id="ropNm${rop.wmsProduct.wprId}"><bean:write name="rop" property="ropNum" format="###,##0.00"/><logic:notEmpty name="rop" property="wmsProduct.typeList"><span class="brown">[${rop.wmsProduct.typeList.typName}]</span></logic:notEmpty><input type="hidden" id="${rop.wmsProduct.wprId}m" value="${rop.ropNum}"/></td>
                                <td style="border-right:#b8b7b7 1px solid;"><span id="xc${rop.wmsProduct.wprId}">&nbsp;<input type="hidden" id="${rop.wmsProduct.wprId}on" value="${rop.ropOutNum}"></span><logic:notEmpty name="rop" property="wmsProduct.typeList"><span class="brown">[${rop.wmsProduct.typeList.typName}]</span></logic:notEmpty></td>
                                <logic:iterate id="wms" name="wmsStro" scope="request">
                                <td><input type="text" id="${rop.wmsProduct.wprId}n${wms.wmsCode}" name="${rop.wmsProduct.wprId}n${wms.wmsCode}" value="" style="width:98%" onBlur="checkIsNum(this)"/></td>
                                </logic:iterate>
                           	</tr>
							<script type="text/javascript">
                                var on=$('${rop.wmsProduct.wprId}'+"on").value;
                                var p=$('${rop.wmsProduct.wprId}'+"m").value;
                                if(on==0&&on==null){
                                    $("xc"+'${rop.wmsProduct.wprId}').innerText=p;
                                }else{
                                    $("xc"+'${rop.wmsProduct.wprId}').innerText=parseInt(p)-parseInt(on);
                                }	
								rowsBg('tr3',<%=count3%>);
                            </script>
                            <%count3++;%>
                            </logic:equal>
                        	</logic:iterate>
           					</logic:notEmpty>
    					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center; padding-top:10px">
						<div id="outButton" class="orangeBack">
                            <input type="button" value="确认生成出库单" class="butSize3" id="dosave" onClick="check()"/><!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="submit" name="yulan" class="butSize3"   value="出库单预览"/>-->
						</div>
                        <div id="emptyOut" class="orangeBox" style="padding:10px; display:none">
                            <span class="brown">没有可以出库的产品...</span>
                        </div>
                        <div id="noOut" class="grayBack" style="padding:10px; display:none">
                            <span class="blue">没有未安排出库的产品或者产品已出库...</span>
                        </div>
					</td>
				</tr>
            </table>	
			</form>
        	</div>
  		</div>
	</div>
  </body>
</html>
