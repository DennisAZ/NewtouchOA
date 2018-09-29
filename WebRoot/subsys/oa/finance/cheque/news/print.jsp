<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.finance.data.YHFinanceApplyRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
List<YHFinanceApplyRecord> records = (List<YHFinanceApplyRecord>)request.getAttribute("records");
YHFinanceApplyRecord applyRecord = new YHFinanceApplyRecord();
%>

<html>
<head>
<title>支票信息打印</title>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}  
 </style>  
<script language="JavaScript"> 
  var hkey_root,hkey_path,hkey_key
  hkey_root="HKEY_CURRENT_USER"
  hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"
//设置网页打印的页眉页脚为空
function pagesetupNull() {
  try {
    var RegWsh = new ActiveXObject("WScript.Shell");
    hkey_key = "header";
    RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
    hkey_key = "footer";
    RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
  }catch (e) {
  }
}
</script>
</head>
<body class="bodycolor" style="margin:0;padding:0;" onload="pagesetupNull()">
 <center class="Noprint">  
 <p>  
 <OBJECT id = WebBrowser classid = CLSID:8856F961 - 340A - 11D0 - A96B - 00C04FD705A2 height = 0  width = 0>  
 </OBJECT>  
 <font color='red'>打印此界面时请先打印预览-》页面设置-》页眉页脚全设置为空（此行文字不会显示在打印效果中）</font>
 <hr align="center"  width = " 90% "  size = " 1 "  noshade>  
 </center> 
 <%for (int i = 0 ; i < records.size(); i++) {
   applyRecord = records.get(i);
 %>
<div style="overflow:auto;" scroll='auto' align='center'>
<table width='88%' cellspacing="1"   cellpadding="1">
<tr>
  <td nowrap align=left width='20%'>
  <font size='3'>存根&nbsp;</font><font color="red" size="4">No. 0000<%=applyRecord.getSeqId()%></font>
  </td>
  <td width="15%">&nbsp;</td>
  <td align=center><font size='5'>&nbsp;支&nbsp;票&nbsp;使&nbsp;用&nbsp;申&nbsp;请&nbsp;单&nbsp;</font></td>
  <td align=right width='25%'><font color="red" size="4">No.0000<%=applyRecord.getSeqId()%></font></td>
</tr>
</table>
 
<table border="1" bordercolor="#000000" style="border:1px solid #000;margin:auto;width:89%;border-collapse:collapse;">
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期:&nbsp;<%=applyRecord.getApplyDate().toString().substring(0,4)%>年<%=applyRecord.getApplyDate().toString().substring(5,7)%>月<%=applyRecord.getApplyDate().toString().substring(8,10)%>日</font>
    </td>
    <td width='4%' style="border:none;">
    </td>
    <td width='14%' align='center' nowrap>领用部门
    </td>
    <td width="17%" align='center' nowrap><%if(applyRecord.getDeptDirectorContent()!=null){%><%=applyRecord.getDeptDirectorContent()%><%} %></td>
    <td width='14%' align='center' nowrap>领用日期
    </td>
    <td width='23%' align='center' nowrap><%=applyRecord.getApplyDate().toString().substring(0,4)%>年<%=applyRecord.getApplyDate().toString().substring(5,7)%>月<%=applyRecord.getApplyDate().toString().substring(8,10)%>日 </td>
  </tr>
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-top:none;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门:&nbsp;<%if(applyRecord.getDeptDirectorContent()!=null){%><%=applyRecord.getDeptDirectorContent()%><%} %></font>
    </td>
    <td width='4%' style="border:none;">
    </td>
    <td width='14%' align='center' nowrap>支票用途
    </td>
    <td align='left' nowrap colspan='3'>&nbsp;&nbsp; <%if(applyRecord.getApplyItem()!=null){%><%=applyRecord.getApplyItem() %><%} %></td>
  </tr>
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-top:none;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;用&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;途:&nbsp;<%if(applyRecord.getApplyItem()!=null){%> <%=applyRecord.getApplyItem() %><%} %></font>
    </td>
    <td width='4%' style="border:none;">
    </td>
    <td width='14%' align='center' nowrap>预计金额
    </td>
    <td width="17%" align='center' nowrap><%=applyRecord.getApplyMoney() %></td>
    <td width='14%' align='center' nowrap>支出途径
    </td>
    <td width='23%' align='center' nowrap>
    </td>
  </tr>
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-top:none;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;支出途径:&nbsp;</font>
    </td>
    <td width='4%' style="border:none;">
  
    </td>
    <td width='14%' align='center' nowrap>支票张数
    </td>
    <td width="17%" align='center' nowrap>1
    </td>
    <td width='14%' align='center' nowrap>支票号码
    </td>
    <td width='23%' align='center' nowrap><%if(applyRecord.getChequeAccount()!=null){%><%=applyRecord.getChequeAccount()%> <%} %></td>
  </tr>
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-top:none;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;部门主管:&nbsp;<%if(applyRecord.getDeptDirector()!=null){%><%=applyRecord.getDeptDirector() %><%} %></font>
    </td>
    <td width='4%' style="border:none;">
    </td>
    <td width='14%' align='center' nowrap>批&nbsp;准&nbsp;人
    </td>
    <td width="17%" align='center' nowrap><%if(applyRecord.getFinanceDirector()!=null){%><%=applyRecord.getFinanceDirector() %> <%} %> </td>
    <td width='14%' align='center' nowrap>申&nbsp;请&nbsp;人
    </td>
    <td width='23%' align='center' nowrap><%if(applyRecord.getApplyClaimer()!=null){%><%=applyRecord.getApplyClaimer() %><%} %></td>
  </tr>
  <tr height=33>
    <td width='28%' nowrap style="border-right:1px dashed #000;border-top:none;border-bottom:none;text-align:left;">
    <font size='2'>&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:&nbsp;<%if(applyRecord.getApplyMemo()!=null){%><%=applyRecord.getApplyMemo() %><%}%></font>
    </td>
    <td width='4%' style="border:none;">
    </td>
    <td width='14%' align='center' nowrap>备&nbsp;&nbsp;&nbsp;&nbsp;注
    </td>
    <td colspan='3' align='left' nowrap>&nbsp;&nbsp;<%if(applyRecord.getApplyMemo()!=null){%><%=applyRecord.getApplyMemo()%><%}%></td>
  </tr>
</table>
</div>
<br>
<br>
<%}%>
</body>
</html>