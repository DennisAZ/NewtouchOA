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
    
    <title>添加产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/js/sal.js"></script>
	
    <script type="text/javascript">
	  	//更改是否计算库存
	  	/*function changeIsCount(n){
			if(n==4){
				$("isC2").checked=true;
			}else{
				$("isC1").checked=true;
			}
		}*/
		//判断产品名或型号重复
	  	function checkNameModel(){
			autoShort($('pname'),100);
			//autoShort($('pmodel'),100);
			var arr=new Array($('pname'));
			checkIsRepeat(arr,'prodAction.do?op=checkPro&wprName='+ encodeURIComponent($('pname').value),null,1);
		}
		
		function checkWprCode(obj){
			autoShort(obj,50);
			checkIsRepeat(obj,'prodAction.do?op=checkWprCode&wprCode=',null,2);
		}

		//保存表单
		function toSavePro(){
			autoShort($('pname'),100);
			//autoShort($('pmodel'),100);
			autoShort($('wprCode'),50);
			var arr=new Array($('pname'));
			var check1 = new Array('prodAction.do?op=checkPro&wprName='+ encodeURIComponent($('pname').value),arr,null,1);
			var check2 = new Array('prodAction.do?op=checkWprCode&wprCode=','wprCode',null,2);
			checkRepeatForm(check1,check2);
		}
	  
		function check(){
			var errStr = "";
			if(isEmpty("pname")){   
				errStr+="- 未填写产品名称！\n"; 
			}
			else if(checkLength("pname",100)){
				errStr+="- 产品名称不能超过100个字！\n";
			}
			if(checkLength("wprCode",50)){
				errStr+="- 产品编号不能超过50位！\n";
			}
			if($("isRepeat2").value==1){
				errStr+="- 此产品编号已存在！\n";
			}
			if($("isRepeat1").value==1){
				errStr+="- 同名产品已存在！\n";
			}
			/*if($("upLim").value!=""){
				if(isNaN($("upLim").value)){
					errStr+="- 最大库存只能填写数字！\n";
				}
			}
			if($("lowLim").value!=""){
				if(isNaN($("lowLim").value)){
					errStr+="- 最小库存只能填写数字！\n";
				}
			}*/
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("register").submit();
			}				  
		}
  </script> 
  </head>
  
  <body>
  	<div class="inputDiv">
  		<form action="prodAction.do" method="post" id="register">
  		<input type="hidden" name="op" value="newProduct"/>
		<input type="hidden" name="wptId" value="" id="newWptId"/>
        <input type="hidden" id="isRepeat1"/>
        <input type="hidden" id="isRepeat2"/>
        <div id="errDiv1" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;同名产品在系统(包括回收站)中已存在</div>
        <div id="errDiv2" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此产品编号在系统(包括回收站)中已存在</div>
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<th class="descTitleL">产品名称：<span class='red'>*</span></th>
					<th class="descTitleR" colspan="3"><input name="wmsProduct.wprName" class="inputSize2L" id="pname" type="text" onBlur="checkNameModel()" />
					</th>			
				</tr>
            </thead>
			<tbody>
                <tr>
                    <th>产品编号：</th>
                    <td><input type="text" class="inputSize2" id="wprCode" name="wmsProduct.wprCode" onBlur="checkWprCode(this)"/>
                    </td>
                    <!--<th>型号：</th>
                    <td><input type="text" id="pmodel" class="inputSize2" name="wmsProduct.wprModel" onBlur="checkNameModel()"/></td> 
                    -->
                    <th>产品类别：</th>
                    <td><input readonly type="text" id="wptName" class="inputSize2S inputBoxAlign lockBack" ondblClick="clearInput(this,'wptId')" title="此处文本无法编辑，双击可清空文本" name="wptName"/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(16,1)">选择</button>
                    <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
                <tr>	
			        <th>标准价格：</th>
                    <td><input type="text" class="inputSize2" name="wmsProduct.wprSalePrc"onblur="checkPrice(this)"></td>
                    <th>单位：</th>
                    <td>
                        <logic:notEmpty name="typeList">
                        <select name="wprUnit" id="wu" class="inputSize2 inputBoxAlign">
                            <option value="">&nbsp;</option>
                             <logic:iterate id="tl" name="typeList" scope="request">
                             <option value="${tl.typId}">${tl.typName }</option>
                             </logic:iterate>
                        </select>
                        </logic:notEmpty>
                        <logic:empty name="typeList">
                             <select id="wu" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                         </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
                <tr>
                	<th>普通提成率：</th>
                    <td><input type="text" class="inputSize2" name="wmsProduct.wprNormalPer" onBlur="checkIsNum(this)"></td>
                    <th>超额提成率：</th>
                    <td><input type="text" class="inputSize2" name="wmsProduct.wprOverPer" onBlur="checkIsNum(this)"></td>
                </tr>
                <!--<tr>
                	<th>未达标提成率：</th>
                    <td colspan="3"><input type="text" class="inputSize2" name="wmsProduct.wprLowPer" onBlur="checkIsNum(this)"></td>
                </tr>-->
                <!-- <tr>
                    <th>产品类型：</th>
                    <td colspan="3" class="longTd">
                        <input type="radio" name="wmsProduct.wprStates" id="sx" value="1" checked="checked"><label for="sx">产成品&nbsp;&nbsp;</label>
                        <input type="radio" name="wmsProduct.wprStates" id="sx2" value="2"><label for="sx2">原材料&nbsp;&nbsp;</label>
                        <input type="radio" name="wmsProduct.wprStates" id="sx3" value="3"><label for="sx3">固定资产&nbsp;&nbsp;</label>
                        <input type="radio" name="wmsProduct.wprStates" id="sx4" value="4"><label for="sx4">服务类&nbsp;&nbsp;</label>
                    </td>
                </tr> -->
                <!-- <tr>
		
                    <th>折扣规范：</th>
                    <td><input type="text" class="inputSize2" name="wmsProduct.wprRange" onBlur="autoShort(this,1000)"/></td>
                </tr>
                 -->
                <tr>
                    <th>产品详情：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="9" name="wmsProduct.wprDesc" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <!--<tr>
                    <th>技术参数：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="3" name="wmsProduct.wprTechnology" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr>
                    <th>常见问题：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="3" name="wmsProduct.wprProblem" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>-->
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="6" name="wmsProduct.wprRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
            <!--<thead>
            	<tr>
                	<td colspan="4"><div>库存信息</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>计算库存：</th>
                    <td colspan="3">
                        <input type="radio" name="wmsProduct.wprIscount" id="isC1" value="1" checked="checked"/><label for="isC1">是&nbsp;&nbsp;</label>
                        <input type="radio" name="wmsProduct.wprIscount" id="isC2" value="0"/><label for="isC2">否</label>
                    </td>
                </tr>
                <tr class="noBorderBot">				
                    <th>最大库存：</th>
                    <td><input type="text" id="upLim" class="inputSize2"  onblur="checkIsNum(this)" name="wmsProduct.wprUpLim"></td>
                    <th>最小库存：</th>
                    <td><input type="text" id="lowLim" class="inputSize2" onBlur="checkIsNum(this)" name="wmsProduct.wprLowLim"></td>
                </tr>-->
                <tr class="submitTr">
                    <td colspan="4">
                    <input type="Button" id="dosave" class="butSize1" value="保存" onClick="toSavePro()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" onClick="cancel()" value="取消"></td>
                </tr>	
                <tr>
                    <td class="tipsTd" colspan="4">
                        <div class="tipsLayer">
                            <ul>
                                <li>&nbsp;添加产品成功后，可在编辑产品页面上传产品图片。</li>
                            </ul>
                        </div>
                    </td>
                </tr>
			</tbody>	
	  </table>
	</form>
  </div>
  </body>
</html>
