<%@ page language="java" import="java.util.*,java.sql.*,yh.core.funcs.attendance.manage.logic.*,yh.subsys.oa.finance.*,yh.core.data.YHRequestDbConn,yh.core.global.YHBeanKeys" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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
  String attachmentId = "";
  String attachmentName = "";
  int seqId = 0;
  if(ba!=null){
   
   seqId = ba.getSeqId();
  }

  
%>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript">

var  selfdefMenu = {
  	office:["downFile","dump","read"], 
    img:["downFile","dump","play"],  
    music:["downFile","dump","play"],  
    video:["downFile","dump","play"], 
    others:["downFile","dump"]
	}

function doOnload(){
  var attr = $("attr");
  if('<%=seqId%>'!=0){
    attachMenuSelfUtil(attr,"finance",$('attachmentName').value ,$('attachmentId').value, '','','<%=seqId%>',selfdefMenu);
  }

}
</script>
</head>
 
<body style="margin:0;padding:0;" onload="doOnload();";>
<div id="edit_body" style="overflow:auto;" SCROLL=auto>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/edit.gif" WIDTH="32" HEIGHT="20" align="absmiddle"><span class="big3"> 新增预算信息 - 详细情况</span>&nbsp;<br>
    </td>
  </tr>
</table>
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
   if(ba.getAttachmentId()!=null){
     attachmentId = ba.getAttachmentId();
   }
   if(ba.getAttachmentName()!=null){
     attachmentName = YHUtility.encodeSpecial(ba.getAttachmentName());
   }
   %>
   <table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 新增预算信息  -  详细情况 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">预算申请人：</td>
      <td class="TableData">
      <%=applyName %>	  </td>
   </tr>    
   <tr>
      <td nowrap class="TableContent">项目名称：</td>
      <td class="TableData">
        <%=ba.getBudgetItem() %>  </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">处室名称：</td>
      <td class="TableData">
			<option value=3><%=deptIdName %></option>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">预算有效期：</td>
      <td class="TableData">
		   <%=budgetAvailabein %>    </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">支出金额：</td>
      <td class="TableData" >
			<%=YHUtility.getFormatedStr(ba.getBudgetMoney(),0) %>     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">预收入金额：</td>
      <td class="TableData" >
			<%=YHUtility.getFormatedStr(ba.getBudgetInMoney(),0) %>     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">预算备注：</td>
      <td class="TableData">
        <textarea cols=50 name="MEMO" rows="3" class="readonly" wrap="yes" readonly><%=ba.getMemo() %></textarea>
      </td>
    </tr>
  <tr>
    <td nowrap  class="TableHeader" colspan="4" align="left">
      描述：
    </td>
 </tr>
 <tr>
  <td nowrap class="TableData">资料附件：      </td>
  <td nowrap class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName" value="<%=attachmentName %>"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId" value="<%=attachmentId %>"></input>
     	<span id="attr"></span>
    </td>
  </tr>
   <%if(ba.getDetailContent()!=null&&!ba.getDetailContent().equals("")){
     String detailContent = ba.getDetailContent();
     String dcs[] = detailContent.split("\n");
     %>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 预算支出详情 </td>
    </tr>
   <tr>
    <td nowrap colspan="2">
    <table  id="MYTABLE" width="100%"  class="small">
        <tr  class="TableHeader"  align="center">
		   <td>项目</td>
           <td>人数</td>
           <td>天数</td>
           <td>（单价）标准</td>
           <td>金额</td>
           <td>备注</td>
           </tr>
           <%for(int i = 0; i<dcs.length;i++){
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
	         <tr class="TableControl" align="center">
            <td><%=item %></td>
            <td><%=person %></td>
            <td><%=days %></td>
            <td><%=YHUtility.getFormatedStr(price,0)%></td>
            <td><%=YHUtility.getFormatedStr(money,0) %></td>
            <td><%=memo %></td>
         </tr>
         <%} %>
	       
       </table>
	   </td>
	</tr>
             <%
             
           }
   
   if(ba.getDetailContentIn()!=null&&!ba.getDetailContentIn().equals("")){
     String detailContentIn = ba.getDetailContentIn();
     String dcs[] = detailContentIn.split("\n");
   
   %>

	<tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 预算收入详情 </td>
    </tr>
   <tr>
    <td nowrap colspan="2">
    <table  width="100%"  class="small">
        <tr  class="TableHeader"  align="center">
		   <td>项目</td>
           <td>人数</td>
           <td>天数</td>
           <td>（单价）标准</td>
           <td>金额</td>
           <td>备注</td>
           
           
        </tr>
        
         <%for(int i = 0; i<dcs.length;i++){
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
	         <tr class="TableControl" align="center">
            <td><%=item %></td>
            <td><%=person %></td>
            <td><%=days %></td>
            <td><%=YHUtility.getFormatedStr(price,0) %></td>
            <td><%=YHUtility.getFormatedStr(money,0) %></td>
            <td><%=memo %></td>
         </tr>
          <%} %>
	       
       </table>
	   </td>
	</tr>
	<% }%>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 部门主管审批  </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">部门主管审批：</td>
      <td class="TableData">
        <%=deptAudit %>	  </td>
   </tr>
	<tr>
		  <td nowrap class="TableContent"> 部门主管审批时间：</td>
		  <td class="TableData">
			<%=deptDateStr %>	  </td>
	</tr>
    <tr>
      <td nowrap class="TableContent">部门主管审批内容：</td>
      <td class="TableData">
        <textarea cols=50 name="DEPT_AUDIT_CONTENT" rows="3" class="BigInput" wrap="yes" readonly><%=ba.getDeptAuditContent() %></textarea>
      </td>
    </tr>
		<tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 财务审批  </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">财务审批人：</td>
      <td class="TableData">
        <%=financeDir %>  </td>
   </tr>
   <tr style="display:none">
    <td nowrap class="TableContent">审批结果：</td>
      <td class="TableData">
        <font color='#6c25ff'>待审核</font>	  </td>
   </tr>
	<tr>
		  <td nowrap class="TableContent" > 财务审批时间：</td>
		  <td class="TableData">
			<%=financeDateStr %></td>
	</tr>
    <tr>
      <td nowrap class="TableContent">财务审批内容：</td>
      <td class="TableData">
        <textarea cols=50 name="FINANCE_AUDIT_CONTENT" rows="3" class="BigInput" wrap="yes" readonly><%=ba.getFinanceAuditContent() %></textarea>
      </td>
    </tr>
 
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.window.close()">&nbsp;
	</td>
	</tr>
		</form>
   </table>
   <%
     }else{
   %>
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
   
   <%
    }
     %>

</div>
</body>
</html>
