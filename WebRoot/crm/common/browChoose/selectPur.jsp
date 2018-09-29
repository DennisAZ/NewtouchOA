<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>选择采购单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/jscript">
	window.onload=function(){
		//表格内容省略
		loadTabShort("tab");
		//增加清空按钮
		createCancelButton('sear',-60,5);
	}
  </script>
  </head>
  
  <body>
  	<div class="browLayer">
        <div class="listSearch">
            <form action="salPurAction.do" id="sear" method="get">
                <input type="hidden" name="op" value="showSpo" />
                <input type="hidden" name="ischoose" value="1" />
                <input type="hidden" name="isok" value="1" />
                <input type="hidden" name="isAll" id="isAll" value="${isAll}" />
                 主题/采购单号：<input class="inputSize2 inputBoxAlign" type="text" id="purtil" name="spoTil" value="${spoTil}"/>				
                <input class="inputBoxAlign butSize3" type="button" value="查询" onClick="formSubmit('sear')"/>&nbsp;
            </form>
        </div>
        <div class="dataList">		
            <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                <tr>
                    <th style="width:30%">采购单主题</th>
                    <th style="width:20%">采购单号</th>
                    <th style="width:10%">类别</th>
                    <th style="width:25%">对应供应商</th>
                    <th style="width:15%;border-right:0px">负责账号</th>
                </tr>
                <logic:notEmpty name="salPurOrd">	
                <logic:iterate id="purList" name="salPurOrd" indexId="count">
                <tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)">
                    <td><a href="javascript:void(0)" onClick="choosePurComplete('${purList.spoId}','${purList.spoTil}','${purList.salEmp.seName}','${purList.salEmp.seNo}','${purList.salSupplier.ssuName}');return false;">${purList.spoTil}</a>&nbsp;</td>
                    <td>${purList.spoCode}&nbsp;</td>
                    <td>${purList.typeList.typName }&nbsp;</td>
                    <td>
                        <logic:notEmpty name="purList" property="salSupplier">
                            ${purList.salSupplier.ssuName}
                        </logic:notEmpty>&nbsp;
                    </td>
                    <td>
                    	<logic:notEmpty name="purList" property="limUser">
                        	${purList.limUser.userSeName}
                        </logic:notEmpty>&nbsp;
                    </td>
                </tr>
                <script type="text/javascript">
                    rowsBg('tr','${count}');
                </script>
                </logic:iterate>
                </logic:notEmpty>
                <logic:empty name="purList">
                    <tr>
                        <td colspan="5" class="noDataTd">未找到相关数据!</td>
                    </tr>
                </logic:empty>
           </table> 
           <logic:notEmpty name="purList">	
                <script type="text/javascript">
                    createPage('salPurAction.do?op=showSpo&isok=1&ischoose=1&isAll=${isAll}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}',false);
                </script>
           </logic:notEmpty>
           
        </div>  
    </div>
  </body>
</html>
