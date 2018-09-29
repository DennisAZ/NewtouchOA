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
    <title>对应询价记录</title>
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
                <th>询价记录</th>
                <td><a href="javascript:void(0)" onClick="parent.supPopDiv(3,'${id}','${name}');return false;" class="newBlueButton">新建询价记录</a></td>
            </tr>
        </table>
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                            <th style="width:18%">询价主题</th>
                            <th style="width:18%">对应项目</th>
                            <th style="width:12%">询价金额</th>
							<th style="width:16%">询价日期</th>
							<th style="width:12%">询价人</th>
                            <th style=" width:4%; border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="inqList">
                         <logic:iterate id="inq" name="inqList">
                         <tr id="trInq<%= count%>" onMouseOver="focusTr('trInq',<%= count%>,1)" onMouseOut="focusTr('trInq',<%= count%>,0)" onDblClick="descPop('salPurAction.do?op=inqDesc&inqId=${inq.inqId}')">
                          <td>
                          <a href="salPurAction.do?op=inqDesc&inqId=${inq.inqId}" target="_blank" style=" cursor:pointer">${inq.inqTitle}</a></td>  
                      
						  <td class="mLink">&nbsp;
                          	<logic:notEmpty name="inq" property="project">
                                <a title="${inq.project.proTitle}" href="projectAction.do?op=showProDesc&proId=${inq.project.proId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看项目详情" style="border:0px;">${inq.project.proTitle}</a></logic:notEmpty>&nbsp;
                          </td>
						  <td>
						   <logic:notEmpty name="inq" property="inqPrice">
						  ￥<bean:write name="inq" property="inqPrice" format="###,##0.00"/>
						  </logic:notEmpty>
						  <logic:empty name="inq" property="inqPrice">￥0.00</logic:empty>
						  </td>
						  <td><label id="inqDate<%=count%>"></label>&nbsp;</td>
                          <td>${inq.salEmp.seName}&nbsp;</td>
                          <td>&nbsp;
                        
                            <img onClick="parent.supPopDiv(31,'${inq.inqId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                           
                         </td>
                         </tr>
                        <script type="text/javascript">
							dateFormat('inqDate','${inq.inqDate}',<%=count%>);
                            rowsBg('trInq',<%=count%>);
                        </script>
                            <% count++; %>
                         </logic:iterate>
                         </logic:notEmpty>
            <logic:empty name="inqList">
                <tr>
                    <td colspan="6" class="noDataTd">暂未添加询价记录...</td>
                </tr>
            </logic:empty>
        </table>
			<logic:notEmpty name="inqList">	
                 	<script type="text/javascript">
						createPage('salSupplyAction.do?op=getSupInq&id=${id}&name='+encodeURIComponent('${name}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
           </logic:notEmpty>
   </div>
  </body>

</html>
