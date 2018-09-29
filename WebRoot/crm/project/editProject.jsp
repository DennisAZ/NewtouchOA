<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>编辑项目</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
     <meta http-equiv="cache-control" content="no-cache"/>
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <link rel="stylesheet" type="text/css" href="css/style.css"/>
     <script type="text/javascript" src="js/prototype.js"></script>
	 <script type="text/javascript" src="js/pro.js"></script>
	 <script type="text/javascript" src="js/common.js"></script>
	 <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
     <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript"> 
   	
	  function check(){
			var errStr = "";
			if(isEmpty("proTitle")){   
				errStr+="- 未填写项目名称！\n"; 
			}
			else if(checkLength("proTitle",300)){
				errStr+="- 项目名称不能超过300个字！\n";
			}
			if(isEmpty("seName"))
			{
				errStr+="- 未选择项目负责账号！\n";
			}
			if(isEmpty("pCreDate"))
			{
				errStr+="- 未选择立项日期！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosend","保存中...");
				waitSubmit("doCancel");
				return $("change").submit();	
			}		  
		}

		function showSelect()
		{
			if($("pType")!=null)
			   $("pType").value="${project.proType.typId}"//项目类别
			var proState='${project.proState}';
			if(proState=="1")
			   $("pState1").checked=true;
			if(proState=="0")
			   $("pState0").checked=true;
			if(proState=="2")
			   $("pState2").checked=true;
			if(proState=="3")
			   $("pState3").checked=true;
			var proPeriod='${project.proPeriod}';
			if(proPeriod=="1")
			   $("period1").checked=true;
			if(proPeriod=="0")
			   $("period0").checked=true;
			if(proPeriod=="2")
			   $("period2").checked=true;
			if(proPeriod=="3")
			   $("period3").checked=true;
		} 
		function chooseCus(){
			parent.addDivBrow(1);
		}
		window.onload=function(){
			if('${project}'!=null&&'${project}'!=""){
				showSelect();
			}
		}
  </script> 
</head>
  <body>
  	<logic:notEmpty name="project">
 	<div class="inputDiv">
  		<form action="projectAction.do" method="post"  id="change">
            <input type="hidden" name="op" value="modProInfo">
            <input type="hidden" name="proId" value="${project.proId}">
            <input type="hidden" name="userCode" value="${project.limUser.userCode}" id="userCode"/>
            <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                            <th class="descTitleL">项目名称：<span class='red'>*</span></th>
                            <th class="descTitleR" colspan="3"><input name="project.proTitle" id="proTitle" type="text" value="${project.proTitle}" class="inputSize2L" onBlur="autoShort(this,300)"></th>				
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>对应客户：</th>
                        <td>
                            <input id="cusName" class="inputSize2S lockBack" type="text" value="${project.cusCorCus.corName}" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                            <button class="butSize2" onClick="chooseCus()">选择</button>
                            <input type="hidden" name="cusCode" id="cusCode" value="${project.cusCorCus.corCode}" />
                        </td>
                        <th class="required">负责账号：<span class='red'>*</span></th>
                        <td>
                            <input readonly class="inputSize2S lockBack" type="text" id="seName" value="${project.limUser.userSeName}-${project.limUser.userCode}" ondblClick="clearInput(this,'userCode')" title="此处文本无法编辑，双击可清空文本">&nbsp;<button class="butSize2" onClick="parent.addDivBrow(13)">选择</button>
                        </td>
                    </tr>
                    <tr>
                        <th>项目类别：</th>
                        <td colspan="3">
                            <logic:notEmpty name="projectType">
                            <select name="pType" id="pType" class="inputSize2 inputBoxAlign">
                                <option  value="">请选择</option>
                                <logic:iterate id="projectType" name="projectType" >
                                <option value="${projectType.typId}">${projectType.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="projectType">
                                <select id="pType" class="inputSize2 inputBoxAlign" disabled>
                                    <option  value="">未添加项目类别</option>
                                </select>
                            </logic:empty>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                        <th>项目状态：</th>
                        <td colspan="3" class="longTd">
                        <input type="radio" name="project.proState" id="pState1" value="1" checked="checked"><label for="pState1">有效&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proState" id="pState0" value="0"><label for="pState0">失败&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proState" id="pState2" value="2"><label for="pState2">搁置&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proState" id="pState3" value="3"><label for="pState3">放弃&nbsp;&nbsp;</label>
                        
                        </td>
                    </tr>
                    <tr>
                        <th>项目阶段：</th>
                        <td colspan="3" class="longTd">
                        <input type="radio" name="project.proPeriod" id="period0" value="0" checked="checked"><label for="period0">项目跟踪&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proPeriod" id="period1" value="1" ><label for="period1">签约实施&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proPeriod" id="period2" value="2" ><label for="period2">结项验收&nbsp;&nbsp;</label>
                        <input type="radio" name="project.proPeriod" id="period3" value="3" ><label for="period3">完成归档&nbsp;&nbsp;</label>
                        </td>
                    </tr>
                    <tr>
                        <th class="required">立项日期：<span class='red'>*</span></th>
                        <td><input name="pCreDate" id="pCreDate" type="text"  readonly="readonly" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand" ondblClick="clearInput(this)"/></td>
                        <th>预计完成日期：</th>
                        <td>
                        <input name="pFinDate" id="pFinDate" type="text" ondblClick="clearInput(this)"  readonly="readonly" value="" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand"/></td>
                    </tr>						
                    <tr>
                        <th>项目概要：</th>
                        <td  colspan="3"><textarea class="inputSize2L" rows="4" name="project.proDesc" onBlur="autoShort(this,4000)">${project.proDesc}</textarea></td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="project.proRemark" onBlur="autoShort(this,4000)">${project.proRemark}</textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input type="button" class="butSize1" id="dosend" value="保存" onClick="check()">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" type="button" class="butSize1" value="取消" onClick="cancel()"></td>
                    </tr>
                </tbody>		
            </table>
        </form>
        <script type="text/javascript">
              $("pCreDate").value="${project.proCreDate}".substring(0,10);
              $("pFinDate").value="${project.proFinDate}".substring(0,10);
        </script>
    </div>
    </logic:notEmpty>
    <logic:empty name="project">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目已被删除					</div>
    </logic:empty>
	</body>
</html>
