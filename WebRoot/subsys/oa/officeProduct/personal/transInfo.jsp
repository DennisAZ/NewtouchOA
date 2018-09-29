<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*,java.util.Map"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<%@ page  import="yh.subsys.oa.officeProduct.person.data.*"%> 
<html>
<head>
<%
	YHPage pages = (YHPage)request.getAttribute("page");
  Map map = (Map)request.getAttribute("transhistoryList");
  
   List<YHOfficeTranshistory> officeTs = (List<YHOfficeTranshistory>)map.get("list");
   Map map2 =(Map) map.get("flow");
   //System.out.println(officeTs.size()+"===========");
%>
<title>办公用品登记信息 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script Language="JavaScript">
   function findOfficeXxInfo(officeSeqId){
    var url  = contextPath + "/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/findOfficeXxInfo.act?officeId="+ officeSeqId;
    window.location.href=url;
   
   }

   function gotoPage(currNo){
     $("currNo").value = currNo;
     $("form1").submit();
   }

   function jumpToPage(){
     var start = 1;
     var end = <%=pages.getTotalPageNum()%>;
     var pageNo = $("currentNo").value;
     if(!isInteger(pageNo)&&pageNo!=0){
       alert("请填写整数");
       $("currentNo").focus();
       return false;
     }
     else if(pageNo > end || pageNo <start){
       alert("页号应在"+start+"-"+end +"之间");
       $("currentNo").focus();
       return false;
     }else{
       gotoPage(pageNo);
     }
   }

  function deleteOfficeInfo(deleteSeqId){
   var msg ="确定要删除该数据吗？";
   if(!window.confirm(msg)){
   }else{
     var noHiddenId = document.getElementById("noHiddenId").value;
     if(noHiddenId && noHiddenId!="null"){
       window.location.href = contextPath + "/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/deleteofficeRecordInfo.act?HiddenId="+deleteSeqId;  
       return true;
      }
    }
   
  }
  //全选
  function checkAll(field){
	   var allSelect = document.getElementsByName("bookSelect");
	   for(var i = 0; i<allSelect.length; i++){
	      allSelect[i].checked = field.checked;
	   }
  }
  //单选
  function checkOne(one){
    if(!one.checked){   
	     document.all("allbox").checked=false;
	   }
  }
  // 删除所选办公用品登记信息
  function deleteCheckBook(){
    var allSelect = document.getElementsByName("bookSelect");
    var deleteStr = "";
    for(var i = 0; i<allSelect.length; i++){
       if(allSelect[i].checked){
         deleteStr += allSelect[i].value +",";
       }
    }  
    if(deleteStr == ""){
       alert("要删除办公用品登记信息，请至少选择其中一条");
       return ;
    }
    msg = '确定要删除所选办公用品登记信息吗?';
    if(window.confirm(msg)){
       var par = 'deleteStr='+deleteStr;
       var url = contextPath + '/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/deleteLicenseInfo.act';
       var json = getJsonRs(url,par);
       if (json.rtState == "0"){//删除后 重新提交查询页面
				 $("form1").submit();
				 return true;
		  } else{
         alert(json.rtMsrg);
      }
      }
   }
 function updateLicense(licenseId){
	 var url  = contextPath + "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/upLicenseInfo.act?licenseId="+ licenseId;
	  window.location.href=url;
 }
  
</script>
</head>

<body class="bodycolor" topmargin="5" >
<%
  if(pages.getTotalRowNum() > 0 && officeTs!=null){%>    

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">办公用品登记信息</span><br>
    </td>
    <td valign="bottom" align="right">
    <span class="small1">当前为第<b><%=pages.getFirstResult()+1 %></b>至<b><%=pages.getLastResult()%></b>条 (第<%=pages.getCurrentPageIndex()%>页，共<%=pages.getTotalPageNum()%>页，每页最多<%=pages.getPageSize() %>条)</small>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
    </tr>
</table>
<form method="post" name="from2" id ="from2" action="#">
<table class="TableList"  width="95%" align="center">

  <tr class="TableHeader">
    <!--    <td nowrap align="center">选择</td> -->
      <td nowrap align="center">办公用品名称</td>
      <td nowrap align="center">登记类型</td>
      <td nowrap align="center">申请数量</td>
      <td nowrap align="center">操作日期</td>
      <td nowrap align="center">备注</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
  </tr>
 <%
 if(officeTs!=null){
 	for(int i=0; i<officeTs.size() &&officeTs.size()>0; i++){%>
 	  <tr class="TableLine1">
 	 <!--   <td nowrap align="center"><input type="checkbox" id="bookSelect" name="bookSelect" value="" onClick="checkOne(this);">&nbsp;</td>
   -->
   <%if(YHUtility.isNullorEmpty(officeTs.get(i).getOfficeProductName())){%>
    <td nowrap align="center">此物品已删除&nbsp;</td>
    <%}else{ %>
        <td nowrap align="center"><%=officeTs.get(i).getOfficeProductName()==null?"":officeTs.get(i).getOfficeProductName() %>&nbsp;</td>
    
    <%} %>
    
    <td nowrap align="center"><%=officeTs.get(i).getTransName() ==null?"":officeTs.get(i).getTransName() %>&nbsp;</td>
    <td nowrap align="center"><%=officeTs.get(i).getTransQty()==0?"":officeTs.get(i).getTransQty() %>&nbsp;</td>
    <td nowrap align="center"><%=officeTs.get(i).getTransDate()==null?"":officeTs.get(i).getTransDate() %>&nbsp;</td>
    <td nowrap align="center"><%=officeTs.get(i).getRemark()==null?"":officeTs.get(i).getRemark() %>&nbsp;</td>
    <% if(officeTs.get(i).getTransStateName().equals("2")){ %>
     <td nowrap align="center" style="cursor:pointer" title="理由：<%=officeTs.get(i).getReason()==null?"":officeTs.get(i).getReason() %>"><a href="#"><%=officeTs.get(i).getTransStateName()==null?"":officeTs.get(i).getTransStateName() %></a></td>
    <%}else{ %>
    <td nowrap align="center"><%=officeTs.get(i).getTransStateName()==null?"":officeTs.get(i).getTransStateName() %>&nbsp;</td>
    <%} %>
    <% 
    Map m = (Map)map2.get(officeTs.get(i).getSeqId());
    int runId = (Integer)m.get("runId");
    int flowId = (Integer)m.get("flowId");
    %>
  <td nowrap align="center" width="80">
  <% if (runId == 0) { %>
   <%if(!YHUtility.isNullorEmpty(officeTs.get(i).getTransState())){ %>
    <%if(officeTs.get(i).getTransState().equals("0")){ %>
        <a href="javascript:void(0);" onclick="deleteOfficeInfo(<%=officeTs.get(i).getSeqId() %>);">撤销 </a>
    <% }%>
       <a href="javascript:void(0);" onclick="deleteOfficeInfo(<%=officeTs.get(i).getSeqId() %>);">删除 </a>	
    <%if(officeTs.get(i).getTransState().equals("1") && officeTs.get(i).getTransFlag().equals("2")){ %>
       <a href="javascript:void(0);" onclick="findOfficeXxInfo(<%=officeTs.get(i).getSeqId() %>);">详情 </a>
    <% }%>
    <%if(officeTs.get(i).getTransState().equals("1") && officeTs.get(i).getTransFlag().equals("1")){ %>
       <a href="javascript:void(0);" onclick="findOfficeXxInfo(<%=officeTs.get(i).getSeqId() %>);">详情 </a>
    <% }%>
    <%if(officeTs.get(i).getTransState().equals("3") && officeTs.get(i).getTransFlag().equals("2")){ %>
       <a href="javascript:void(0);" onclick="findOfficeXxInfo(<%=officeTs.get(i).getSeqId() %>);">详情 </a>
    <% } 
   }else{
    %>    
    <a href="javascript:void(0);" onclick="deleteOfficeInfo(<%=officeTs.get(i).getSeqId() %>);">删除 </a>	
    <%} %>
    <% } else {%>
     <a href="javascript:;" onclick="formView(<%=runId %>,<%=flowId %>)">查看流程</a>
    <% } %>
   </td>
   
  </tr>
  <input type="hidden" value="<%= officeTs.get(i).getSeqId()%>" id="noHiddenId" name="noHiddenId"/>
 <%} 
 }%> 
 <%
   if(pages.getTotalPageNum() >1){%>
     <tr class="TableControl">
			<td colspan="3" align="left">
			<!--  	<input type="checkbox" name="allbox" id="allbox" onClick="checkAll(this);"><label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label>&nbsp;
        <a href="javascript:deleteCheckBook();" title="删除所选办公用品信息"><img src="<%=imgPath %>/delete.gif">删除</a>
        -->
	    	 </td>
			 <td colspan="7" align="right">
			   <%if(pages.getCurrentPageIndex() >1){%>
			      <input type="button"  value="首页" class="BigButton"   onclick="gotoPage(1)"> &nbsp;&nbsp;
			   	  <input type="button"  value="上一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()-1 %>)"> &nbsp;&nbsp;
			   	<%}%>
			   <%if(pages.getCurrentPageIndex() < pages.getTotalPageNum()){%>
			   	  <input type="button"  value="下一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()+1 %>)"> &nbsp;&nbsp;
			   	  <input type="button"  value="末页" class="BigButton"   onclick="gotoPage(<%=pages.getTotalPageNum() %>)"> &nbsp;&nbsp;
			   	<%}%>
			   页数
			   <input type="text" name="currentNo" id="currentNo" value="<%=pages.getCurrentPageIndex()%>" class="SmallInput" size="2"> <input type="button"  value="转到" class="SmallButton" onclick="jumpToPage();" title="转到指定的页面">&nbsp;&nbsp;
			</td>
   </tr>
  <%}%> 
</table>
 </form>
<%} else{%>
 <table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">无办公用品登记信息！</div>
				    </td>
          </tr>
        </tbody>
      </table>  
<%}%>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/findofficeRecordInfo.act" method="get" name="form1" id="form1">

  <input type="hidden" name="currNo" id="currNo" value=""/> 
</form>
</body>
</html>
