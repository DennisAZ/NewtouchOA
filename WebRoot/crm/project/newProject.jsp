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
    <title>新建项目</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
     <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <link rel="stylesheet" type="text/css" href="css/style.css"/>
     <script type="text/javascript" src="js/prototype.js"></script>
	 <script type="text/javascript" src="js/pro.js"></script>
	 <script type="text/javascript" src="js/common.js"></script>
     <script type="text/javascript" src="js/formCheck.js"></script>
	 <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
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
			if(isEmpty("creDate"))
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
				return $("create").submit();		
			}	  
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}
		
  </script> 
</head>
  <body>
  <div class="inputDiv">
    <form action="projectAction.do" method="post"  id="create">
        <input type="hidden" name="op" value="saveProject">
        <input type="hidden" name="userCode" value="${limUser.userCode}" id="userCode"/>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th class="descTitleL">项目名称：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input name="project.proTitle" id="proTitle" type="text" class="inputSize2L" onBlur="autoShort(this,300)"/></th>				
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th>对应客户：</th>
                    <td>
                    <input id="cusName" class="inputSize2S lockBack" type="text" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                    <button class="butSize2" onClick="chooseCus()">选择</button>
                    <input type="hidden" name="cusCode" id="cusCode" value="" />
                    </td>
                    <th class="required">负责账号：<span class='red'>*</span></th>
                    <td>
                        <input readonly class="inputSize2S lockBack" type="text" id="seName" value="${limUser.userSeName}-${limUser.userCode}" ondblClick="clearInput(this,'userCode')" title="此处文本无法编辑，双击可清空文本">&nbsp;<button class="butSize2" onClick="parent.addDivBrow(13)">选择</button>
                    </td>
                </tr>
                <tr>
                    <th>项目类别：</th>
                    <td colspan="3">
                        <logic:notEmpty name="projectType">
                        <select name="pType" class="inputSize2 inputBoxAlign" id="pType">
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
                    <input type="radio" name="project.proState" id="sta1" value="1" checked="checked"><label for="sta1">有效&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proState" id="sta2" value="0"><label for="sta2">失败&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proState" id="sta3" value="2"><label for="sta3">搁置&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proState" id="sta4" value="3"><label for="sta4">放弃&nbsp;&nbsp;</label>
                    
                    </td>
                </tr>
                <tr>
                    <th>项目阶段：</th>
                    <td colspan="3" class="longTd">
                    <input type="radio" name="project.proPeriod" id="per1" value="0" checked="checked"><label for="per1">项目跟踪&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proPeriod" id="per2" value="1" ><label for="per2">签约实施&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proPeriod" id="per3" value="2" ><label for="per3">结项验收&nbsp;&nbsp;</label>
                    <input type="radio" name="project.proPeriod" id="per4" value="3" ><label for="per4">完成归档&nbsp;&nbsp;</label>
                    </td>
                </tr>
                <tr>
                    <th class="required">立项日期：<span class='red'>*</span></th>
                    <td><input name="pCreDate" type="text" id="creDate" readonly="readonly" value="${curDate}" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand" ondblClick="clearInput(this)"/></td>
                    <th>预计完成日期：</th>
                    <td>
                    <input name="pFinDate" type="text" readonly="readonly" value="" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand" ondblClick="clearInput(this)"/></td>
                </tr>
                <tr>
                    <th>项目概要：</th>
                    <td  colspan="3"><textarea class="inputSize2L" rows="4" name="project.proDesc" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" name="project.proRemark" rows="3" onBlur="autoShort(this,4000)"></textarea></td>
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
  </div>
  </body>
</html>
