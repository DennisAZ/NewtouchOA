<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
	<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
YHSalItem SalItemList = (YHSalItem)request.getAttribute("findSlaItemList");
//System.out.println(SalItemList.getIscomputer()=="1");

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
		window.location.href= contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act";
	}
function sel_change(input){

  	if(form1.ITEM_TYPE.value=="2")
     {
      	document.all("FORMU").style.display="";
      }
    else
    	{
    		document.all("FORMU").style.display="none";
    		document.form1.FORMULA.value="";
    		document.form1.FORMULANAME.value="";
    	}

}

function LoadWindow2()
{
  URL="<%=contextPath%>/subsys/oa/hr/salary/insurancePara/salItem/formulaEdit.jsp";
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formulEdit","height=350,width=650,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function doSubmit(){
	 var itemName = $("ITEM_NAME").value;
	 if(itemName.replaceAll(" ","") == "" || itemName == "null"){
	      alert("薪酬项目名称不能为空");
	      return false;
   }
	 $("form1").submit();
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 薪酬项目编辑</span>
    </td>
  </tr>
</table>

<div align="center" class="big1">
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/upSalaryItemAct.act" method="post" name="form1" id="form1">
	<table width="450" class="TableBlock" align="center" >
   <tr>
    <td nowrap class="TableContent">薪酬项目名称：<font color="red">*</font> </td>
    <td nowrap class="TableData" align="left">
       <input type="text" name="ITEM_NAME" id="ITEM_NAME" class="BigInput" size="30" maxlength="100" value="<%=SalItemList.getItemName()==null?"":SalItemList.getItemName() %>"></input>
       <input type="hidden" name="ITEM_ID" id="ITEM_ID" value="<%=SalItemList.getSlaitemId()==0?"":SalItemList.getSlaitemId() %>">
       <input type="hidden" name="seqId" id="seqId" value="<%=SalItemList.getSeqId()==0?"":SalItemList.getSeqId() %>">
    </td>
   </tr>

   <tr>
    <td nowrap class="TableContent">项目类型： </td>
    <td nowrap class="TableData" align="left">
    	 <select name="ITEM_TYPE" id="ITEM_TYPE" onChange="sel_change()">
	       <option value="0" <%if(!SalItemList.getIsreport().equals("1")&&!SalItemList.getIscomputer().equals("1")){%> selected <%}%>>财务录入项</option>
	       <option value="1" <%if(SalItemList.getIsreport().equals("1")){%> selected  <%} %>>部门上报项</option>
	       <option value="2" <%if(!SalItemList.getIsreport().equals("1")&&SalItemList.getIscomputer().equals("1")){%> selected <%}%>>计算项</option>
       </select>
    </td>
   </tr>
   <tr id="FORMU" style="<%if(SalItemList.getIscomputer().equals("1")){%>"";<%}else {%>display:none;<%} %>">
    <td nowrap class="TableContent">计算公式：</td>
    <td nowrap class="TableData">
    	<input type="hidden" name="FORMULA" id="FORMULA" value="<%=SalItemList.getFormula()==null?"":SalItemList.getFormula() %>">
    	<textarea cols=37 name="FORMULANAME" id="FORMULANAME" rows="4" class="BigStatic" readonly  wrap="yes"><%=SalItemList.getFormulaname()==null?"":SalItemList.getFormulaname() %></textarea>&nbsp;
    	<input value="编辑公式" class="BigButton" onclick="LoadWindow2()" title="编辑公式" name="button" type="button">
    </td>
   </tr>
     <tr align="center" class="TableFooter">
      <td colspan="2" nowrap>
      	<input type="button" value="修改" class="BigButton" title="修改薪酬项目"  name="button" onclick="doSubmit();">
      	 &nbsp;&nbsp;<input type="button" value="返回" class="BigButton" onClick="goto();return false;">
      </td>
    </tr>

  </table>
</form>
</div>
</body>
</html>