<%@ page language="java" pageEncoding="UTF-8"%>
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
    
    <title>选择项目</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/jscript">
	window.onload=function(){
		//表格内容省略
		loadTabShort("tab");
		//增加清空按钮
		createCancelButton('sear',-50,5);
	}
  </script>
  </head>
  
  <body>
  	<div class="browLayer">
        <div class="listSearch">
            <form action="projectAction.do" id="sear" method="get">
                <input type="hidden" name="op" value="searProject" />
                项目名称：<input class="inputSize2 inputBoxAlign" type="text"name="proTitle" id="proTitle" value="${proTitle}" />
                <input class="inputBoxAlign butSize3" type="button" value="查询" onClick="formSubmit('sear')"/>&nbsp;
            </form>
        </div>
        <div class="dataList">		
            <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                <tr>
                	<th style="width:8%">项目状态</th>
                    <th style="width:32%">项目名称</th>
                    <th style="width:14%">项目阶段</th>
                    <th style="width:14%">项目类别</th>
                    <th style="width:16%">立项日期</th>
                    <th style="width:16%;border-right:0px">预计完成日期</th>
                </tr>
                <logic:notEmpty name="allProList">	
                <logic:iterate id="project" name="allProList" indexId="count">
                <tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)">
                	<td>
                    <logic:equal value="0" name="project" property="proState">失败</logic:equal>
                    <logic:equal value="1" name="project" property="proState">有效</logic:equal>
                    <logic:equal value="2" name="project" property="proState">搁置</logic:equal>
                    <logic:equal value="3" name="project" property="proState">放弃</logic:equal>
                    </td>
                    <td><a href="javascript:void(0)" onClick="chooseProComplete('${project.proId}','${project.proTitle}');return false;">${project.proTitle}</a>&nbsp;</td>
                    <td><logic:equal value="0" name="project" property="proPeriod">项目跟踪</logic:equal>
                    <logic:equal value="1" name="project" property="proPeriod">签约实施</logic:equal>
                    <logic:equal value="2" name="project" property="proPeriod">结项验收</logic:equal>
                    <logic:equal value="3" name="project" property="proPeriod">完成归档</logic:equal></td>
                    <td>${project.proType.typName}&nbsp;</td>
                    <td><span id="proCreDate${count}"></span></td>
                    <td><logic:notEmpty  name="project" property="proFinDate">
                         <span id="proFinDate${count}>"></span>&nbsp;
                       </logic:notEmpty>
                      <logic:empty  name="project" property="proFinDate">
                      <span class="gray">未指定</span>
                      </logic:empty></td>
                </tr>
                <script type="text/javascript">
                    dateFormat('proCreDate','${project.proCreDate}','${count}');
                    dateFormat('proFinDate','${project.proFinDate}','${count}');
                    rowsBg('tr','${count}');
                </script>
                 </logic:iterate>
                 </logic:notEmpty>
                 <logic:empty name="allProList">
                    <tr>
                        <td colspan="6" class="noDataTd">未找到相关数据!</td>
                    </tr>
                </logic:empty>
           </table> 
           <logic:notEmpty name="allProList">
				<script type="text/javascript">
                    var url;
                    switch('${pageType}'){
                        case 'default':
                            url = 'projectAction.do?op=getAllProject&forwardType=selPro';
                            break;
                        case 'searPro':
                            url = 'projectAction.do?op=searProject&proTitle='+ encodeURIComponent('${proTitle}') +'&pType=${protId}';
                            break;
                    }
                    createPage(url,'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}',false);
                </script>
            </logic:notEmpty>
        </div>   
    </div>
  </body>
</html>
