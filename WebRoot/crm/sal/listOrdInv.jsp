<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>开票记录列表(订单详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
		var gridEl = new boGrid("ordInvListTab");//表格对象
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			//表格内容省略
			gridEl.shortTab();
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>开票记录</th>
                <td><a href="javascript:void(0)" class="newBlueButton"  onClick="parent.ordPopDiv(7);return false;">新建开票记录</a></td>
            </tr>
        </table>
        <logic:notEmpty name="invList">
		<script type="text/javascript">
            var sortUrl = "invAction.do?op=listOrdInv&ordId=${ordId}&pageSize=${page.pageSize}";//排序url
            var pageUrl = sortUrl + "&orderCol=${orderCol}&isDe=${isDe}";//分页url
            gridEl.init(false,true);
            var ths = new Array("是否已回款","开票内容","票据类别","编号","金额","开票日期","开票人",["备注",false],["操作",false]);
            gridEl.addTh(ths,sortUrl,"${orderCol}","${isDe}");
        </script>
        <logic:iterate id="inv" name="invList" indexId="count">
        <script type="text/javascript">
            var tds;
            var invCon="${inv.sinCon}";
            var invMon=["${inv.sinMon}","money","right"];
			var invType="${inv.salInvType.typName}";
            var invDate=["${inv.sinDate}","date","center"];
            var invIsPaid=["","","center"];
            if("${inv.sinIsPaid}"=="1"){
                invIsPaid[0]="已回款";
            }
            else {
                invIsPaid[0]="<span class='gray'>未回款</span>";
            }
			var invCode="${inv.sinCode}";
			var invUser="${inv.sinResp}";
			var invRemark="${inv.sinRemark}";
            var funcCol = ["<img class='imgAlign' onClick=\"parent.ordPopDiv(71,'${inv.sinId}')\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.ordDelDiv(3,'${inv.sinId}')\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>","","center"];
            tds = [invIsPaid,invCon,invType,invCode,invMon,invDate,invUser,invRemark,funcCol];
            gridEl.addTd(tds,'',"${count}");
        </script>
        </logic:iterate>
        <script type="text/javascript">
            gridEl.writeGrid();
            //生成分页
            createPage(pageUrl,'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
        </script>
        </logic:notEmpty>
        <logic:empty name="invList">
            <div class="noDataTd">暂无数据</div>
        </logic:empty>	
   </div>
  </body>

</html>
