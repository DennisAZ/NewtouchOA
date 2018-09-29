<%@ page language="java" import="java.util.*,java.sql.*,yh.core.funcs.attendance.manage.logic.*,yh.subsys.oa.finance.*,yh.core.data.YHRequestDbConn,yh.core.global.YHBeanKeys" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  Calendar cl =Calendar.getInstance();
  int curYear = cl.get(Calendar.YEAR);
  int nextYear = curYear - 1;
  YHManageOutLogic tmol = new YHManageOutLogic();
  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
  Connection dbConn = requestDbConn.getSysDbConn();
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  YHPersonLogic pl = new YHPersonLogic();
  int userId = user.getSeqId();
  int deptId = user.getDeptId();
  String deptName = tmol.selectByUserIdDept(dbConn,String.valueOf(user.getDeptId()));
  String type = (String)request.getAttribute("type");
  if(type==null){
    type = "";
  }
  YHDeptLogic dl = new YHDeptLogic();
  YHBudgetApply ba =(YHBudgetApply)request.getAttribute("budgetApply");
  

  
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.finance.data.YHBudgetApply"%>
<%@page import="yh.core.funcs.person.logic.YHPersonLogic"%>
<%@page import="yh.core.funcs.dept.data.YHDepartment"%>
<%@page import="yh.core.funcs.dept.logic.YHDeptLogic"%><html>
<head>
<title>查看预算信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}  
 </style>  
 
</head>
 
<body  style="margin:0;padding:0;">
 <center  class = " Noprint ">  
 <p>  
 <OBJECT id = WebBrowser classid = CLSID:8856F961 - 340A - 11D0 - A96B - 00C04FD705A2 height = 0  width = 0>  
 </OBJECT>  
 <span style="font-family:'楷体';font-size:16px;" >打印时:右键->打印预览->页面设置->页眉页脚全设置为空->打印（此行文字不会显示）或</span><input type="button" value="打印" style="FONT-SIZE: 9pt;height: 20px; text-decoration: none;  border: 1px solid #6E91C7; background: #F4FBE1 url('button_bg.gif');" onclick="window.print();">
 <hr align = "center"  width = " 90% "  size = " 1 "  noshade>  
 </center>  
 
<div id="edit_body" style="overflow:auto;" SCROLL=auto align='center'>
<H3 style="TEXT-ALIGN: center; COLOR: black"><SPAN style="FONT-SIZE: 32px">北京外办预算情况明细表(试行)</SPAN></H3>

 <%if(ba!=null){
   String applyName = ba.getBudgetProposer();
   if(applyName!=null&&!applyName.equals("")){
     applyName = pl.getNameBySeqIdStr(applyName,dbConn);
   }else{
     applyName = "";
   }
   String deptIdName = ba.getDeptId();
   if(deptIdName!=null&&!deptIdName.equals("")){
     deptIdName = dl.getDeptName(dbConn,Integer.parseInt(deptIdName));
   }else{
     deptIdName = "";
   }
   java.util.Date useDate = ba.getBudgetAvailablein();
   String budgetAvailabein = "0000-00-00";
   if(useDate!=null&&!useDate.equals("")){
     budgetAvailabein = YHUtility.getDateTimeStr(useDate).substring(0,10);
   }
   String deptAudit = ba.getDeptAuditDirector();
   if(deptAudit!=null&&!deptAudit.equals("")){
     deptAudit = pl.getNameBySeqIdStr(deptAudit,dbConn);
   }else{
     deptAudit = "";
   }
   java.util.Date deptAuditDate = ba.getDeptAuditDate();
   String deptDateStr = "0000-00-00";
   if(deptAuditDate!=null&&!deptAuditDate.equals("")){
     deptDateStr =YHUtility.getDateTimeStr(deptAuditDate).substring(0,10);
   }
   java.util.Date budgetDate = ba.getBudgetDate();
   String budgetDateStr = "0000-00-00";
   if(budgetDate!=null&&!budgetDate.equals("")){
     budgetDateStr =YHUtility.getDateTimeStr(budgetDate).substring(0,10);
   }
   String financeDir  = ba.getFinanceDirector();
   if(financeDir!=null&&!financeDir.equals("")){
     financeDir = pl.getNameBySeqIdStr(financeDir,dbConn);
   }else{
     financeDir = "";
   }
   java.util.Date financeDate = ba.getFinanceAuditDate();
   String financeDateStr = "0000-00-00";
   if(financeDate!=null&&!financeDate.equals("")){
     financeDateStr =YHUtility.getDateTimeStr(financeDate).substring(0,10);
   }
   %>
<table width=85%>
<tr>
<td>
<SPAN style="COLOR: #000000"><STRONG>&nbsp;填表日期：<%=budgetDateStr %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </STRONG></td>
<td><STRONG>编号：<%=ba.getSeqId() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </STRONG></td>
<td><STRONG>单位：人民币（元）</STRONG></SPAN>
</tr>
</table>
<table border="1" bordercolor="#000000" style="border:1px solid #000;margin:auto;width:85%;border-collapse:collapse;">
<tr height="34">
<td nowrap width="25%" colspan=2>团名：<%=ba.getBudgetItem() %></td>
<td nowrap width="10%">部门：</td>
<td nowrap width="15%"><%=deptIdName%></td>
<td nowrap width="18%">陪同：</td>
<td nowrap width="32%">预收金额：<%=YHUtility.getFormatedStr(ba.getBudgetInMoney(),0) %></td>
</tr>
<tr height="34">
<td nowrap colspan=5>备注：<%=ba.getMemo() %></td>
<td nowrap width="32%">到帐方式：</td>
</tr>
<tr height="34">
<td nowrap colspan=6 align='center'>收入预算明细</td>
</tr>
<tr height="34">
<td nowrap width="15%">项目</td>
<td nowrap width="10%">人数</td>
<td nowrap width="10%">天数</td>
<td nowrap width="15%">单价(标准)</td>
<td nowrap width="18%">金额</td>
<td nowrap width="32%">备注</td>
</tr>
  <%
   if(ba.getDetailContentIn()!=null&&!ba.getDetailContentIn().equals("")){
     String detailContentIn = ba.getDetailContentIn();
     String dcs[] = detailContentIn.split("\n");
       for(int i = 0;i<dcs.length;i++){
         String dc= dcs[i];
         String[] temp = dc.split("`~");
         String item = "";
         String person = "0";
         String days = "0";
         String price ="0.00";
         String money = "0.00";
         String memo = "";
          for(int j = 0; j<temp.length;j++){
            if(j==0){
              item = temp[0];
            }
            if(j==1){
              person = temp[1];
            }
            if(j==2){
              days = temp[2];
            }
            if(j==3){
              price = temp[3];
            }
            if(j==4){
              money = temp[4];
            }
            if(j==5){
              memo = temp[5];
            }
          }
   %>
       
        <tr align="center">
            <td><%=item%>&nbsp;</td>
            <td><%=person%>&nbsp;</td>
            <td><%=days%>&nbsp;</td>
            <td><%=price%>&nbsp;</td>
            <td><%=money%>&nbsp;</td>
            <td><%=memo%>&nbsp;</td>
         </tr>
   <%
       }
       int temp = 0 ;
       if(dcs.length<3){
         temp = 3 - dcs.length;
       }
       for(int i = 0;i<temp;i++){
   %>
      <tr align="center">
         <td>&nbsp;</td>
         <td>&nbsp;</td>
         <td>&nbsp;</td>
         <td>&nbsp;</td>
         <td>&nbsp;</td>
         <td>&nbsp;</td>
       </tr>
   <%
       }
   }else{ %>
   <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
   
   <%} %>
	         
	<tr height="34">
<td nowrap colspan=6 align='center'>支出预算明细</td>
</tr>
<tr height="34">
<td nowrap width="15%">项目</td>
<td nowrap width="10%">人数</td>
<td nowrap width="10%">天数</td>
<td nowrap width="15%">单价(标准)</td>
<td nowrap width="18%">金额</td>
<td nowrap width="32%">备注</td>
</tr>
  <%if(ba.getDetailContent()!=null&&!ba.getDetailContent().equals("")){
     String detailContent = ba.getDetailContent();
     String dcs[] = detailContent.split("\n");
     for(int i = 0; i<dcs.length;i++){
       String dc= dcs[i];
       String[] temp = dc.split("`~");
       String item = "";
       String person = "0";
       String days = "0";
       String price ="0.00";
       String money = "0.00";
       String memo = "";
        for(int j = 0; j<temp.length;j++){
          if(j==0){
            item = temp[0];
          }
          if(j==1){
            person = temp[1];
          }
          if(j==2){
            days = temp[2];
          }
          if(j==3){
            price = temp[3];
          }
          if(j==4){
            money = temp[4];
          }
          if(j==5){
            memo = temp[5];
          }
        }
     %>
  <tr align="center">
            <td><%=item%>&nbsp;</td>
            <td><%=person%>&nbsp;</td>
            <td><%=days%>&nbsp;</td>
            <td><%=price%>&nbsp;</td>
            <td><%=money%>&nbsp;</td>
            <td><%=memo%>&nbsp;</td>
         </tr>
     <%}
     int temp = 0;
     if(dcs.length<5){
       temp = 5-dcs.length;
     }
     for(int i = 0; i<temp;i++){
     %>
     
          <tr align="center">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
     <%
       }
     %>
	      <tr align="center">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>合计：</td>
            <td colspan=2><%=YHUtility.getFormatedStr(ba.getBudgetMoney(),0) %></td>
         </tr>
     <%
     
     }else{ %>
	     <tr align="center">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
	 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
         </tr>
	         <tr align="center">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>合计：</td>
            <td colspan=2>0.00</td>
         </tr>	
         <%} %>
<tr height="34">
<td nowrap colspan=3>领取支票张数：</td>
<td nowrap colspan=2>预借现金金額：</td>
<td nowrap colspan=1>领取签单张数：</td>
</tr>
<tr height="34">
<td nowrap colspan=4>主管业务会领导：</td>
<td nowrap colspan=2>财务主管：</td>
</tr>
<tr height="34">
<td nowrap colspan=3>财务人员：<%=financeDir %></td>
<td nowrap colspan=2>部门领导：<%=deptAudit %></td>
<td nowrap>经手人：<%=applyName %></td>
</tr> 
</table>
<%}else{ %>
 <table class="MessageBox" align="center" width="350">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">此记录已被删除！</div>
    </td>
  </tr>
</table>
 <br>
<div align="center">
 <input type="button"  value="关闭" class="BigButton" onClick="window.close();">
</div>
   

<%} %>
</div>
</body>
</html>
