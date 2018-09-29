<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>财务预算管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript"> 
function pri_budget(BUDGET_ID) {
  myleft=(screen.availWidth-800)/2;
  window.open("../plan/new/print.php?BUDGET_ID="+BUDGET_ID,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
 
function budget_detail(BUDGET_ID) {
  myleft=(screen.availWidth-800)/2;
  window.open("../plan/new/budget_detail.php?BUDGET_ID="+BUDGET_ID,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
 
</script>
 
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/styles/imgs/menuIcon/@finance.gif" HEIGHT="20"><span class="big3"> 财务预算管理(2010年度)
</span>
    </td>
  </tr>
</table>
<br>
<div align='center'>
  <table class="TableList" width="850">
  <tr class="TableHeader">
      <td nowrap align="center" width='5%'>年份</td>
      <td nowrap align="center" width='10%'>本次预算金额</td>
      <td nowrap align="center" width='6%'>申请人</td>
      <td nowrap align="center" width='8%'>所属部门</td>
      <td nowrap align="center" width='8%'>申请日期</td>
      <td  align="center" width='24%'>项目</td>
      <td  align="center" width='24%' >备注</td>
      <td nowrap align="center" width='15%'>详细</td>
    </tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">10,000.00</td>
      <td nowrap align="center">徐锦秀</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">赴泰国参加第11届泰国国际风筝节代表团</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('9')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('9')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">45,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">赴英国传统书画展团</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('11')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('11')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">110,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">“挪威杯”青少年足球赛</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('13')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('13')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">85,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">市友协代表团出访俄罗斯/乌克兰</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('15')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('15')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">30,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">赴德国/荷兰摄影展</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('16')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('16')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">0.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2010-01-11</td>
      <td  align="center">赴英国传统书画展撤展团</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('100')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('100')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">110,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2010-07-19</td>
      <td  align="center">“挪威杯”青少年足球赛</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('130')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('130')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">110,000.00</td>
      <td nowrap align="center">支建军</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2010-07-19</td>
      <td  align="center">“挪威杯”青少年足球赛</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('134')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('134')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">40,000.00</td>
      <td nowrap align="center">王健</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">世博会访华团</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('18')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('18')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
 
<tr class='TableLine2'>
      <td nowrap align="center" >2010</td>
      <td nowrap align="center">60,000.00</td>
      <td nowrap align="center">王健</td>
      <td nowrap align="center">业务一部</td>
      <td nowrap align="center">2009-12-25</td>
      <td  align="center">挪威杯组委会代表团出席世博会</td>
      <td  align="center"></td>
      <td nowrap align="center">
      <a href="javascript:;" onClick="budget_detail('19')">查看详细</a>&nbsp;&nbsp;
      <a href="javascript:;" onClick="pri_budget('19')">打印预览</a>&nbsp;&nbsp;
    </td>
</tr>
</table>
</div>
<br/>
<div align='center'>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.window.close()">&nbsp;
</div>
</body>
</html>
