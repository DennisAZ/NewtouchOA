<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.officeProduct.person.data.*"%>
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String transName = (String)request.getAttribute("transName");
String borrowerName = (String)request.getAttribute("borrowerName");
String typeName = (String)request.getAttribute("typeName");
String proName = (String)request.getAttribute("proName");
String operatorName = (String)request.getAttribute("operatorName");
String transDate ="";
int transQty = 0;
String remark = "";
List<YHOfficeTranshistory> officeList = (List<YHOfficeTranshistory>)request.getAttribute("onefindOffices");
YHOfficeTranshistory office = officeList.get(0);
if(office!=null){
	transDate = String.valueOf(office.getTransDate());
	transQty = office.getTransQty();
	remark = office.getRemark();
}

%>
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function goto(){ 
		window.location.href= contextPath + "/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/findofficeRecordInfo.act";
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;登记信息详情</span>
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="trans_handle.php"  method="post" name="form1">
<table width="70%" align="center" class="TableBlock">
    <tr>
      <td nowrap class="TableData" width="30%">申请类型：</td>
      <td class="TableData"><%=transName==null?"":transName %></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="30%">申请人：</td>
      <td class="TableData"><%=borrowerName==null?"":borrowerName %></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="30%">申请时间：</td>
      <td class="TableData"><%=transDate==null?"":transDate %></td>
    </tr>
    <tr>
      <td nowrap class="TableData">办公用品类别：</td>
      <td class="TableData"><%=typeName==null?"":typeName %></td>
    </tr>
    <tr>
      <td nowrap class="TableData">办公用品名称：</td>
      <td class="TableData"><%=proName==null?"":proName %></td>
      </tr>
    <tr>
     <tr>
      <td nowrap class="TableData">发放数量：</td>
      <td class="TableData"><%=transQty %></td>
    </tr>
    <tr>
      <td nowrap class="TableData">审批人：</td>
      <td class="TableData"><%=operatorName==null?"":operatorName %></td>
    </tr>
     <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData"><%=remark==null?"":remark %></td>
    </tr>
    <input type="hidden" name="OPERATOR_ID" value="">
    <input type="hidden" name="TRANS_ID" value="">
    <input type="hidden" name="TRANS_FLAG" value="">
    <input type="hidden" name="PRO_ID" value="">
    <input type="hidden" name="PRO_STOCK" value="">
    <tr align="center" class="TableControl">
    <td colspan="4">
    <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">
    </td>
  </tr>
  </table>
</form>
</body>
</html>