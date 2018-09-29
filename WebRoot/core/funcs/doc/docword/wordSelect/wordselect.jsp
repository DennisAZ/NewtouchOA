<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">

<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript">
var wordRetNameArray = null;
function selectWord(retArray) {
	wordRetNameArray = retArray;
	  var url = contextPath + "/core/funcs/doc/docword/wordSelect/MultiUserSelect.jsp";
	  openDialogResize(url,  520, 400);
	}

</script>

</head>
<body class="" topmargin="5" >
 <div id="formDiv">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
   <tr>
    <td nowrap class="TableData">部门主管(选填)：</td>
    <td nowrap class="TableData">
        <textarea cols="40" name="managerDesc" id="managerDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectWord('managerDesc');">添加</a>
    </td>
   </tr>
   </table>
 </div>
</body>
</html>