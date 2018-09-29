<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.salary.submit.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<html>
<head>
<%
	String deptId = request.getParameter("deptId");
	if(deptId == null){
	  deptId = "";
	}
	List<YHSalItem> listSalItem = (List<YHSalItem>)request.getAttribute("listSalItem");
	List<YHSalPerson> listPerson = (List<YHSalPerson>)request.getAttribute("listPerson");
	String title = "";
  for(int i=0; i<listSalItem.size(); i++){
    title += "S" + listSalItem.get(i).getSlaitemId() + ",";
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
function doInit(){
	$('flowId').value = window.parent.flowId;
  $$('input').each(function(e, i) {
    if (e.type != 'text') {
      return;
    }
    e.onkeyup = function() {
      var data = e.value;
      if(isNaN(data)){
        e.value = '';
        return;
      }
    return;
    }
  });
}

function doSubmit(){
	var pars = Form.serialize($('form1')); 
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/submit/act/YHHrSubmitAct/setSubmitInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == "0"){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/submit/run/newRemind.jsp?deptId=<%=deptId%>";
	}else{ 
	  alert("信息修改失败"); 
	}
}
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<form id="form1" name="form1" method="post" action="">
<table align="center" id="cal_table" class="TableBlock" >
  <input type="hidden" id="flowId" name="flowId">
  <input type="hidden" id="total" name="total" value="<%=listPerson.size()%>">
  <input type="hidden" id="title" name="title" value="<%=title%>">
	<tbody>
	  <tr align="center" class="TableHeader">
	    <td nowrap="" width="100px"><b>姓名</b></td>
	    <%if(listSalItem!=null && listSalItem.size()>0){
	    	 for(int i=0; i<listSalItem.size(); i++){%>  
	    <td nowrap="" width="100px" align="center" style="cursor:pointer" onclick=""><b><%=listSalItem.get(i).getItemName() %></b></td>
	    <%}
	    }%>
	  </tr>
	    <%if(listPerson!=null && listPerson.size()>0){ 
	        for(int i = 0; i < listPerson.size(); i++){%>
	  <tr align="center" class="TableLine1">
      <td nowrap="" ><%=listPerson.get(i).getUserName() %>
        <input type="hidden" id="sdId_<%=i %>" name="sdId_<%=i %>" value="<%=listPerson.get(i).getSdId() %>">
        <input type="hidden" id="hsdId_<%=i %>" name="hsdId_<%=i %>" value="<%=listPerson.get(i).getHsdId() %>">
        <input type="hidden" id="userId_<%=i %>" name="userId_<%=i %>" value="<%=listPerson.get(i).getUserId() %>">
      </td>
        <%  List<String> slist = listPerson.get(i).getSlist();
            Map<String,Double> smap = listPerson.get(i).getSmap();
            for(int j = 0; j < slist.size(); j++){
              String s = (String)slist.get(j);
              double value = smap.get(s);
              String valueStr = YHUtility.getFormatedStr(value,1);%>
      <td nowrap="" align="center"><input type="text" size="10" value="<%=valueStr %>" class="SmallInput" name="<%=s %>_<%=i %>" id="<%=s %>_<%=i %>" style="text-align: right;"></td>
          <%}%>
	  </tr>
	      <%} 
      }%>    
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="确 定" class="BigButton" onclick="doSubmit()">
</div>
</form>
</body>
</html>