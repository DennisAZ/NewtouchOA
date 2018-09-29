<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>询价明细</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/choosePro.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/spo.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
		function changePrice(n,obj,isPrice){ 
			var isN=0;
			
			if(obj.value==""){
				isN=1;
			}
			//是否价格格式
			else if(isPrice==1){
				if(checkPrice(obj)){
					isN=1;
				}
			}
			else{
				if(checkIsNum(obj)){
					isN=1;
				}
			}
			if(isN==1){
				var price=document.getElementById("price"+n).value;
				var num=document.getElementById("num"+n).value;
				var total=price*num;
				if(parseInt(total)>=999999999)
					total=999999999;
				document.getElementById("allPrice"+n).value=total;
			}
		}
		
		function changeTotal(n,obj){
			if(checkPrice(obj)){
				var num=document.getElementById("num"+n).value;
				var allPrice=document.getElementById("allPrice"+n).value;
				var price=allPrice/(num);
				document.getElementById("price"+n).value=price;
			}
		}
		
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
        	<div id="title">采购管理 > 询价明细</div>
			<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="width:35%; text-align:center; vertical-align:top; padding-left:10px;">
                    	<div class="orgBorder">
                        	<div class="orgBorderTop">选择产品</div>
                            <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                    		<iframe frameborder="0" scrolling="auto" width="100%" onload="loadAutoH(this,'ifmLoad')" src="wmsManageAction.do?op=searchProType&ord=spo"></iframe>
                        </div>
                    </td> 
					<td style="vertical-align:top; padding-top:6px;">
             			<div id="listContent" style="height:550px;">
							<div class="grayBack" style="margin-bottom:10px; padding:6px;">
                            	<img id="hideImg" src="crm/images/content/hide.gif" onClick="showHide('ordDesHide','hideImg')" style="cursor:pointer;" class="imgAlign" alt="点击收起"/>&nbsp;<span class="blue">询价单信息</span>
                            	<div id="ordDesHide" style="display:block; text-align:center">
                                	<table class="normal lineborder" style="width:95%" cellpadding="0px" cellspacing="0px">
                                    	<tr>
                                        	<td><nobr>询价主题：</nobr></td>
                                            <th colspan="3"> ${inquiry.inqTitle}&nbsp;</th>
                                        </tr>
                                    	<tr>
                                            <td>供应商：</td>
                                            <th style="width:40%">${inquiry.salSupplier.ssuName}&nbsp;</th>
                                            <td><nobr>询价日期：</nobr></td>
                                            <th style="width:40%"><span id="inqDate"></span>&nbsp;</th>
                                        </tr> 
                                        <tr>
                                            <td style="border:0px"><nobr>询价金额：</nobr></td>
                                            <th style="border:0px"><span id="ordMon"></span>&nbsp;</th>
                                            <td style="border:0px">询价人：</td>
                                            <th style="border:0px">${inquiry.salEmp.seName}&nbsp;</th>
                                        </tr>
                                    </table>
                                    <script type="text/javascript">
										removeTime("inqDate","${inquiry.inqDate}",1)
                                    	$("ordMon").innerHTML="￥"+changeTwoDecimal("${inquiry.inqPrice}");
                                    </script>
                                </div>
                            </div>
							<div class="dataList">
                                <form id="ropForm" action="salPurAction.do" method="post" name="register" >
                                <input type="hidden" name="wprId" id="wprCode" value=""/>
                                <input type="hidden" name="inqId" value="${inquiry.inqId}"/>
                                <input type="hidden" name="op" value="newRinq"/>
                                <table id="ordDesc" class="normal rowstable noBr" cellpadding="0" cellspacing="0" style="width:100%">
                                    <tr>
                                        <th style="width:20%">产品名称[型号]</th>
                                        <th style="width:18%">单价(RMB)</th>
                                        <th style="width:10%">数量</th>
                                        <th>单位</th>
                                        <th style="width:18%">总价(RMB)</th>
                                        <th style="width:20%">备注</th>
                                        <th style="border-right:0px; width:4%" colspan="2">操作</th>
                                    </tr>
                                     <logic:notEmpty name="inquiry" property="rinqPros">
                                     <logic:iterate id="rpp" name="inquiry" property="rinqPros">
                                      <tr>
                                         <td style="white-space:normal; word-break : break-all;">
                                         	${rpp.wmsProduct.wprName}<logic:notEmpty name="rpp" property="wmsProduct.wprModel">[${rpp.wmsProduct.wprModel}]</logic:notEmpty>
                                         </td>
                                         <td>
                                         	<input class="inputSize2" style="width:95%;" type="text" name="price${rpp.wmsProduct.wprId}" value="<bean:write name='rpp' property='rqpPrice' format='0.00'/>" onKeyUp="changePrice('${rpp.wmsProduct.wprId}',this,1)">
                                         </td>
                                         <td>
                                         	<input class="inputSize2 inputBoxAlign" style="width:95%;" type="text" name="num${rpp.wmsProduct.wprId}"  value="<bean:write name='rpp' property='rqpNum' format='0.00'/>" onKeyUp="changePrice('${rpp.wmsProduct.wprId}',this,0)">
                                         </td>
                                         <td>
                                         	<logic:notEmpty name="rpp" property="wmsProduct.typeList">${rpp.wmsProduct.typeList.typName}</logic:notEmpty>&nbsp;
                                         </td>
                                         <td>
                                         	<input class="inputSize2" style="width:95%;" type="text" name="allPrice${rpp.wmsProduct.wprId}" value="<bean:write name='rpp' property='rqpAllPrice' format='0.00'/>" onKeyUp="changeTotal('${rpp.wmsProduct.wprId}',this)">
                                         </td>
                                         <td>
                                         	<textarea rows="1" name="remark${rpp.wmsProduct.wprId}" style="width:95%;" onBlur="autoShort(this,500)">${rpp.rqpRemark}&nbsp;</textarea>
                                         </td>
                                         <td style="text-align:center">
                                         	&nbsp;&nbsp;<img src="crm/images/content/del.gif" onClick="delTable(this,'ordDesc')" alt="删除" style="cursor:pointer"/>
                                        	<div style="display:none;"><input type="checkbox" name="wprId" checked="checked" value="${rpp.wmsProduct.wprId}"></div>&nbsp;
                                         </td>			
                                      </tr>
                                        </logic:iterate>
                                        <tr>
                                      </tr>
                                    </logic:notEmpty>
                                    </table>
                                    <div style="text-align:center; margin-top:8px;">
                                    	<input type="button" onClick="saveRop()" id="dosave" class="butSize3" value="保存明细"/>
                                    </div>
                                </form>
							</div>
                         </div>
					</td>
				</tr>
			</table>
		</div>
	</div>	
  </body>
</html>
