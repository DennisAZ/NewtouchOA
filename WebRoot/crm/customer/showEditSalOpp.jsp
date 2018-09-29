<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>修改销售机会</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
	<script type="text/javascript" >
		function initForm(){
			if('${salOpp.oppLev}'=="低热度"&&'${salOpp.oppLev}'!=""){
				$("oppLev1").checked=true;
		   	}
			if('${salOpp.oppLev}'=="中热度"&&'${salOpp.oppLev}'!=""){
			  	$("oppLev2").checked=true;
		   	}
		    if('${salOpp.oppLev}'=="高热度"&&'${salOpp.oppLev}'!=""){
			  	$("oppLev3").checked=true;
		   	}
		    if('${salOpp.oppState}'=="跟踪"&&'${salOpp.oppLev}'!=""){
			  	$("oppState1").checked=true;
		   	}
			if('${salOpp.oppState}'=="成功"&&'${salOpp.oppLev}'!=""){
			  	$("oppState2").checked=true;
		   	}
		    if('${salOpp.oppState}'=="搁置"&&'${salOpp.oppLev}'!=""){
			  	$("oppState3").checked=true;
		   	}
		    if('${salOpp.oppState}'=="失效"&&'${salOpp.oppLev}'!=""){
			  	$("oppState4").checked=true;
		   	}
		   	if($("stage")!=null){
		       	$("stage").value='${salOpp.oppStage.typId}';
			}
		   	$("possible").value='<c:out value="${salOpp.oppPossible}"/>';
		   	dateInit('pid','${salOpp.oppExeDate}');
			dateInit('sigDate','${salOpp.oppSignDate}');
			dateInit('findDate','${salOpp.oppFindDate}');
		}
		
    	function check(){
			var errStr = "";
			var sel=$("stage");
			if(sel!=null)
			  $("stageName").value=sel.options[sel.selectedIndex].text;
			if(isEmpty("oppTitle")){
				errStr+="- 未填写销售机会主题！\n";
			 }
			else if(checkLength("oppTitle",300)){
				errStr+="- 销售机会主题不能超过300个字！\n";
			 }
			 if(isEmpty("cusId")){   
				errStr+="- 未选择客户！\n"; 
			 }
			 if(isEmpty("seNo")){   
				errStr+="- 未选择负责账号！\n"; 
			 }
			 if(isEmpty("findDate")){   
				errStr+="- 未填写发现日期！\n"; 
			 }
			 if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			 else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("edit").submit();
			}
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}

		window.onload=function(){
			if('${salOpp}'!=null&&'${salOpp}'!=""){
				initForm();
			}
		}
  	</script>
  </head>
  
  <body>

  <div class="inputDiv">
	<logic:notEmpty name="salOpp">
	<form id="edit" action="cusServAction.do" method="post">
   		<input type="hidden" name="op" value="changeSalOpp" />
        <input type="hidden" name="salOppId" value="${salOpp.oppId}"/>
        <input type="hidden" name="stageName" id="stageName" value=""/>
        <input type="hidden" id="isIfrm" name="isIfrm" value="${isIfrm}"/>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                	<th class="descTitleL">机会主题：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="oppTitle" class="inputSize2L" type="text" name="salOpp.oppTitle" value="<c:out value="${salOpp.oppTitle}"/>" onBlur="autoShort(this,300)"/></th>
                </tr>
            </thead>
            <tbody>
            	<tr class="noBorderBot">
                    <th class="required">对应客户：<span class='red'>*</span></th>
                    <td>
                        <input id="cusName" class="inputSize2S lockBack" type="text" value="<c:out value="${salOpp.cusCorCus.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2" onClick="chooseCus()">选择</button>
                        <input type="hidden" name="cusId" id="cusId" value="${salOpp.cusCorCus.corCode}" />
                    </td>
                    <th class="required">负责人：<span class='red'>*</span></th>
                    <td><input type="hidden" name="seNo" id="seNo" value="${salOpp.salEmp.seNo}"/>
	                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${salOpp.salEmp1.seName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
	                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                    </td>
                </tr>
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>基本信息</div></td>
                </tr>
            </thead>
        	<tbody>
            	 <tr>
                   	<th class="required">发现日期：<span class='red'>*</span></th>
                    <td><input name="oppFindDate" id="findDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
					<th>发现人：</th>
                    <td><span class="textOverflow3a" title="<c:out value="${salOpp.salEmp.seName}"/>">${salOpp.salEmp.seName}&nbsp;</span></td>
				</tr>
                <tr>
                    <th>关注日期：</th>
                    <td><input name="oppExeDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                    <th>机会热度：</th>
                    <td>
                     <input type="radio" id="oppLev1" name="salOpp.oppLev" value="低热度" checked="checked"><label for="oppLev1">低&nbsp;&nbsp;</label>
                     <input type="radio" id="oppLev2" name="salOpp.oppLev" value="中热度"><label for="oppLev2">中&nbsp;&nbsp;</label>
                     <input type="radio" id="oppLev3" name="salOpp.oppLev" value="高热度"><label for="oppLev3">高&nbsp;</label>
                    </td>
                </tr>
                 <tr class="noBorderBot">
                    <th>机会描述：</th>
                    <td colspan="6"><textarea class="inputSize2L" rows="6" name="salOpp.oppDes" onBlur="autoShort(this,4000)">${salOpp.oppDes}</textarea></td>
                </tr>
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>当前状态</div></td>
                </tr>	
            </thead>
            <tbody>
            	<tr>
                    <th>预计签单日期：</th>
                    <td><input name="oppSigDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" id="sigDate" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                    <th>预期金额：</th>
                    <td>
                    <input type="text" class="inputSize2" name="salOpp.oppMoney" id="oppMon" onBlur="checkPrice(this)" value="<bean:write name='salOpp' property='oppMoney' format='0.00'/>"/>	
                    </td>
                </tr>
            	<tr>
                	<th class="orangeBack">机会状态：</th>
                    <td colspan="3"  class="longTd">
                      <input type="radio" id="oppState1" name="salOpp.oppState" value="跟踪" checked="checked"><label for="oppState1">跟踪&nbsp;&nbsp;&nbsp;</label>
                      <input type="radio" id="oppState2" name="salOpp.oppState" value="成功"><label for="oppState2">成功&nbsp;&nbsp;&nbsp;</label>
                      <input type="radio" id="oppState3" name="salOpp.oppState" value="搁置"><label for="oppState3">搁置&nbsp;&nbsp;&nbsp;</label>
                      <input type="radio" id="oppState4" name="salOpp.oppState" value="失效"><label for="oppState4">失效&nbsp;</label>
                    </td>
                </tr>
                <tr>
                    <th class="orangeBack">阶段：</th>
                    <td>
                        <logic:notEmpty name="oppStageList">
                        <select id="stage" name="stage" class="inputSize2 inputBoxAlign">
                            <option  value=""></option>
                            <logic:iterate id="oppStage" name="oppStageList" >
                            <option value="${oppStage.typId}">${oppStage.typName}</option>
                            </logic:iterate>
                        </select>
                        </logic:notEmpty>
                        <logic:empty name="oppStageList">
                            <select id="stage" class="inputSize2 inputBoxAlign" disabled>
                                <option  value="">未添加</option>
                            </select>
                        </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                    <th class="orangeBack">可能性：</th>
                    <td>
                    <select name="salOpp.oppPossible" id="possible" class="inputSize2">
                        <option value="0">0%</option>
                        <option value="10">10%</option>
                        <option value="20">20%</option>
                        <option value="30">30%</option>
                        <option value="40">40%</option>
                        <option value="50">50%</option>
                        <option value="60">60%</option>
                        <option value="70">70%</option>
                        <option value="80">80%</option>
                        <option value="90">90%</option>
                        <option value="100">100%</option>
                    </select>	
                    </td>
                </tr>		
                <tr>
                    <th class="orangeBack">阶段备注：</th>
                    <td colspan="3">
                    	<textarea id="oppTitle" class="inputSize2L" rows="2" name="salOpp.oppStaRemark" onBlur="autoShort(this,100)">${salOpp.oppStaRemark}</textarea>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>推进历史：</th>
                    <td colspan="3" class="longTd">
                    	<div class="divWithScroll2" style=" width:410px;height:100px; font-weight:normal;">${salOpp.oppStaLog}&nbsp;</div>
                    </td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                    </td>
                </tr>
                <tr>
                	<td colspan="4" class="tipsTd">
                    	<div class="tipsLayer">
                            <ul>
                                <li>&nbsp;请及时更新橙色区域数据，有助于对销售机会进行有效分析。</li>
                            </ul>
                        </div>
                    </td>
                </tr>	
            </tbody>
        </table>
	</form>
    </logic:notEmpty>
	 <logic:empty name="salOpp">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该销售机会已被删除</div>
	</logic:empty>
	</div>
  </body>
</html>
