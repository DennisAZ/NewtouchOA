	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ page  import="java.util.List"%>
  <%@ page  import="java.lang.*"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<%
	List<YHSalItem> SalItemList = (List<YHSalItem>)request.getAttribute("SalItemList");
	//System.out.println(SalItemList);
	List itemSetList = (List)request.getAttribute("itemSetList");
  int salCount =	(Integer)request.getAttribute("salCount");
	
	%>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
	<title>薪酬项目定义</title>
<script type="text/javascript">
	function sel_change(input){

	  	if(form1.ITEM_TYPE.value=="2")
	     {
	      	document.all("FORMU").style.display="";
	      }
	    else
	    	{//id=FORMU 的隐藏起来，并把id为FORMULA 和FORMULANAME 的值清空
	    		document.all("FORMU").style.display="none";
	    		document.form1.FORMULA.value="";
	    		document.form1.FORMULANAME.value="";
	    	}

	}
	function LoadWindow2()
	{
	  URL="<%=contextPath%>/subsys/oa/hr/salary/insurancePara/salItem/formulaEdit.jsp";
	  myleft=(screen.availWidth-650)/2;
	  window.open(URL,"formulEdit","height=450,width=750,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
	}

	function showFormul(slaItemId){
		  URL="<%=contextPath%>/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/findBJInfo.act?slaItemId="+ slaItemId;
		  myleft=(screen.availWidth-650)/2;
		  window.open(URL,"formulEdit","height=450,width=750,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
		
		// var url  = contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/findBJInfo.act?slaItemId="+ slaItemId;
	//	  window.location.href=url;
	}
	 /** 
	 * 替换s1为s2 
	 */ 
	 String.prototype.replaceAll = function(s1,s2){ 
	    return this.replace(new RegExp(s1,"gm"),s2); 
	 }
// 添加薪酬项目 
function doSubmit(){
	var itemName = $("ITEM_NAME").value;
	if(itemName.replaceAll(" ","") == "" || itemName == "null"){
	      alert("薪酬项目名称不能为空");
	      return false;
  }
	$("form1").submit();
}
function delSalItemInfo(deleteSeqId){
   var msg ="确定要删除该信息吗？";
   if(!window.confirm(msg)){
   }else{
     if(deleteSeqId){
       window.location.href = contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/delSlaItemInfo.act?HiddenId="+deleteSeqId;  
       return true;
      }
    }
 }

function delete_all(){
	   var msg ="确定删除所有信息吗？";
	   if(!window.confirm(msg)){
	   }else{
	       window.location.href = contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/delAllSlaItemInfo.act";  
	       return true;
	    }
	 }
function updateSalItemInfo(slaItemId){
	 var url  = contextPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/findSlaItemInfo.act?slaItemId="+ slaItemId;
	  window.location.href=url;
}
</script>
</head>
<body>
<table class="small" border="0" cellpadding="3" cellspacing="0" width="100%">
  <tbody><tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 添加薪酬项目</span>
    </td>
  </tr>
</tbody></table>

<div  align="center">
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/addSalaryItemAct.act" method="post" name="form1" id="form1">
	<table class="TableBlock" align="center" width="450">
  
   <tbody><tr>
    <td class="TableContent" nowrap>薪酬项目名称：<font color="red">*</font></td>
    <td class="TableData" align="left">
       <input name="ITEM_NAME" id="ITEM_NAME" type="text" >
       <input name="ITEM_ID" id="ITEM_ID" value="7" type="hidden">
    </td>
   </tr>
   <tr>
    <td class="TableContent">项目类型： </td>
    <td class="TableData" align="left">
    	 <select name="ITEM_TYPE" id="ITEM_TYPE" onchange="sel_change()">
       <option value="0">财务录入项</option>
       <option value="1">部门上报项</option>
       <option value="2">计算项</option>
       </select>
    </td>
   </tr>
   <tr id="FORMU" style="display: none;">
    <td class="TableContent" nowrap="nowrap">计算公式：</td>
    <td class="TableData" nowrap="nowrap">
    	<input name="FORMULA" id="FORMULA" type="hidden">
    	<textarea cols="37" name="FORMULANAME" id="FORMULANAME" rows="4" class="BigStatic" readonly="readonly" wrap="yes"></textarea>&nbsp;
    	<input value="编辑公式" class="BigButton" onclick="LoadWindow2()" title="编辑公式" name="button" type="button">
    </td>
   </tr>
     </tbody><tfoot class="TableFooter" align="center">
      <td colspan="2" nowrap="nowrap">
         <input type="button" value="添加" title="添加薪酬项目" class="BigButton" name="button" onclick="doSubmit();">
               <!--<input value="添加" class="BigButton" title="添加薪酬项目" name="button" type="submit">  -->	
      </td>
    </tfoot>
  </table>
  </form>
</div>
<br>


<table class="small" border="0" cellpadding="3" cellspacing="0" width="100%">
 <tbody>
   <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 已定义的薪酬项目（最多50条）</span>
    </td>
  </tr>
 </tbody>
</table>
<br>
<%if(SalItemList!=null && SalItemList.size()!=0){ %>
<div align="center">
    <table class="TableList" width="450">
      <thead class="TableHeader">
      <td align="center" nowrap="nowrap">编号</td>
      <td align="center" nowrap="nowrap">名称</td>
      <td align="center" nowrap="nowrap">项目类型</td>
      <td align="center" nowrap="nowrap">操作</td>
    </thead>
<%for(int i=0; i<SalItemList.size()&&SalItemList.size()>0; i++){ %>
    <tr class="TableData">
      <td align="center" width="50" nowrap="nowrap"><%=SalItemList.get(i).getSlaitemId()==0?0:SalItemList.get(i).getSlaitemId()%></td>
      <td align="center" nowrap="nowrap"><%=SalItemList.get(i).getItemName()==null?"":SalItemList.get(i).getItemName() %></td>
      <td align="center" nowrap="nowrap"><%=itemSetList.get(i)==null?"":itemSetList.get(i)%> </td>
      <td align="center" nowrap="nowrap">
      &nbsp;<a href="javascript:void(0);" onclick="updateSalItemInfo(<%=SalItemList.get(i).getSlaitemId() %>);return false;">编辑</a>
      <%if(!YHUtility.isNullorEmpty(SalItemList.get(i).getIscomputer())&&SalItemList.get(i).getIscomputer().equals("1")){ %>
     &nbsp;<a href="javascript:void(0);" onclick="showFormul(<%=SalItemList.get(i).getSlaitemId()%>);return false;"> 公式编辑</a>
      <%} %>
      <%if(SalItemList.get(i).getSlaitemId()==salCount ){ %>
     &nbsp;<a href="javascript:void(0);" onclick="delSalItemInfo(<%=SalItemList.get(i).getSlaitemId()%>);return false;">删除 </a>
      <%} %>
      </td>
    </tr>
<%} %>
    <tr class="TableFooter">
      <td colspan="4" align="center">
        <input value="全部删除" class="BigButton" onclick="delete_all();" type="button">
      </td>
    </tr>
    </table>
</div>
<%}else{ %>
 <table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">"提示","无薪酬项目信息"！</div>
				    </td>
          </tr>
        </tbody>
      </table> 
<%} %>
</body>
	</html>