<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应联系人</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sup.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>联系人</th>
                <td><a href="javascript:void(0)" onClick="parent.supPopDiv(21,'${id}','${name}');return false;" class="newBlueButton">新建联系人</a></td>
            </tr>
        </table>
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
             <tr>
                        <th style="width:10%">姓名</th>
						<th style="width:10%">部门</th>
                        <th style="width:10%">职务</th>
                        <th style="width:5%">性别</th>
                        <th style="width:15%">手机</th>
                        <th style="width:15%">办公电话</th>
                        <th style="width:15%">电子邮件</th>
                        <th style="width:14%">QQ</th>
                        <th style=" width:6%;border-right:0px">编辑</th>
                    </tr>
                    <logic:notEmpty name="conList">
                    <logic:iterate id="supContact" name="conList">
                    <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salSupplyAction.do?op=showSupConInfo&conId=${supContact.scnId}')">
                        <td>
                        <a href="salSupplyAction.do?op=showSupConInfo&conId=${supContact.scnId}" target="_blank" style="cursor:pointer">${supContact.scnName}</a>
                        </td>
						<td>${supContact.scnDep}&nbsp;</td>
                        <td>${supContact.scnService}&nbsp;</td>
                        <td>${supContact.scnSex}&nbsp;</td>
                        <td>${supContact.scnPhone}&nbsp;</td>
                        <td>${supContact.scnWorkPho}&nbsp;</td>
                        <td><logic:notEmpty name="supContact" property="scnEmail"><img src="crm/images/content/email.gif"  title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('${supContact.scnEmail}');return false;">${supContact.scnEmail}</a></logic:notEmpty>&nbsp;</td>
                        <td><logic:notEmpty name="supContact" property="scnQq"><img src="crm/images/content/qq.gif"  title="点击开始qq对话"/><a href="javascript:void(0)" onClick="qqTo('${supContact.scnQq}');return false;">${supContact.scnQq}</a></logic:notEmpty>&nbsp;</td>
                        <td>&nbsp;
                            <img onClick="parent.supPopDiv(22,'${supContact.scnId}',2)" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;
                         </td>
                    </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count%>);
                        </script>
                        <% count++; %>
                    </logic:iterate>
                    </logic:notEmpty>
            <logic:empty name="conList">
                <tr>
                    <td colspan="9" class="noDataTd">暂未添加联系人...</td>
                </tr>
            </logic:empty>
        </table>
			<logic:notEmpty name="conList">	
                 	<script type="text/javascript">
						createPage('salSupplyAction.do?op=getSupCon&id=${id}&name='+encodeURIComponent('${name}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
           </logic:notEmpty>
   </div>
  </body>

</html>
