<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,yh.core.funcs.dept.data.YHDepartment" %>
<%
  request.setCharacterEncoding("UTF-8");
  ArrayList<YHDepartment> deptList = (ArrayList<YHDepartment>)request.getAttribute("DEPT_LIST");
  if(deptList == null)
  {
  	deptList = new ArrayList<YHDepartment>();
	}
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String USER_PRIV = request.getParameter("USER_PRIV");
  String PRIV_NAME = request.getParameter("PRIV_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style>
.menulines{}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script Language="JavaScript">
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
function getOpenner()
{
   if(navigator.appName.indexOf("Microsoft") == 0)
   {
     return self.parent.dialogArguments.document;
   }
   else
   {
     return self.parent.opener.document;
   }
}
function trim(str)
{
  if(!str)
  {
    return "";
  }
  var regEmptyStr = /^(\s|\u3000)*$/;
  if(regEmptyStr.test(str))
  {
    return "";
  }
  var reg = /^\s*(\S|\S[\S\s]*\S)\s*$/;
  return str.replace(reg, "$1");
}
function doInit()
{
  var parentDocumentObj = getOpenner();
  var nameString = trim(parentDocumentObj.getElementById(TO_NAME).value);
  var idString = trim(parentDocumentObj.getElementById(TO_ID).value);
  var nameList = nameString.split(',');
  var idList = idString.split(',');
  var tableObj = document.getElementById("userTable");
  if(nameString != "")
  {
	  for(var i = 3; i < tableObj.rows.length; i++)
	  {
	    var tableValue = tableObj.rows[i].cells[0].firstChild.data;
	    var tableId = tableObj.rows[i].cells[0].lastChild.value;
	    //alert(tableIdValue);
	    for(var j = 0; j < idList.length; j++)
	    {
	  	  if(idList[j] == trim(tableId))
	    	{
					tableObj.rows[i].cells[0].style.backgroundColor = "rgb(0, 255, 0)";
					break;
			  }
			}
	  }
 	}
}
function click_user(userName)
{
  var tdName = userName.firstChild.data;
  var tdId = userName.lastChild.value;
  var parentDocumentObj = getOpenner();
  var nameString = trim(parentDocumentObj.getElementById(TO_NAME).value);
  var idString = trim(parentDocumentObj.getElementById(TO_ID).value);
  if(!userName.isChecked)
  {
    userName.style.backgroundColor = "rgb(0, 255, 0)";
    if(nameString == "")
    {
    	parentDocumentObj.getElementById(TO_NAME).value = trim(tdName);
    	parentDocumentObj.getElementById(TO_ID).value = trim(tdId);
    }
    else
		{
    	parentDocumentObj.getElementById(TO_NAME).value = trim(nameString + "," + trim(tdName));
    	parentDocumentObj.getElementById(TO_ID).value = trim(idString + "," + trim(tdId));
    }
    userName.isChecked = true;
  }
  else
  {
    userName.style.backgroundColor = "rgb(255, 255, 255)";
    var nameList = nameString.split(',');
    var idList = idString.split(',');
    nameString = "";
    idString = "";
    var index = nameList.indexOf(trim(tdName));
    if(index >= 0)
    {
    	nameList.remove(index);
    }
    for(var i = 0; i < nameList.length; i++)
    {
        if(nameString)
        {
          nameString += ",";
        }
        nameString += nameList[i];
    }
    for(var j = 0; j < idList.length; j++)
    {
      if(trim(tdId) != idList[j] && idList[j] != "")
      {
        if(idString)
        {
          idString += ",";
        }
        idString += idList[j];
      }
    }
    parentDocumentObj.getElementById(TO_NAME).value = trim(nameString);
    parentDocumentObj.getElementById(TO_ID).value = trim(idString);
    userName.isChecked = false;
  }
}
function add_all()
{
  var tableObj = document.getElementById("userTable");
  var parentDocumentObj = getOpenner();
  var nameString = trim(parentDocumentObj.getElementById(TO_NAME).value);
  var idString = trim(parentDocumentObj.getElementById(TO_ID).value);
  var nameList = nameString.split(',');
  var idList = idString.split(',');
  for(var i = 3; i < tableObj.rows.length; i++)
  {
    var tablevalue = tableObj.rows[i].cells[0].firstChild.data;
    var tableIdValue = tableObj.rows[i].cells[0].lastChild.value;
  	var isHave = false;
    for(var j = 0; j < nameList.length; j++)
    {
  		if(nameList[j] == trim(tablevalue) && nameList[j])
    	{
  	    isHave = true;
  			break;
	  	}
		}
    if(!isHave)
    {
      if(nameString)
      {
        nameString += ",";
      }
  	  nameString += trim(tablevalue);
  	}
    for(var k = 0; k < idList.length; k++)
    {
  	  if(idList[k] == trim(tableIdValue) && idList[k])
    	{
  	    isHave = true;
  			break;
	  	}
		}
  	if(!isHave)
		{
      if(idString)
      {
      	idString += ",";
      }
      idString += trim(tableIdValue);
  	}
		tableObj.rows[i].cells[0].style.backgroundColor = "rgb(0, 255, 0)";
		tableObj.rows[i].cells[0].isChecked = true;
  }
  parentDocumentObj.getElementById(TO_NAME).value = nameString;
  parentDocumentObj.getElementById(TO_ID).value = idString;
}
function del_all()
{
  var tableObj = document.getElementById("userTable");
  var parentDocumentObj = getOpenner();
  var nameString = trim(parentDocumentObj.getElementById(TO_NAME).value);
  var idString = trim(parentDocumentObj.getElementById(TO_ID).value);
  var pramArray = null;
  var prayArrId = null;
  var idList = idString.split(',');
  var nameList = nameString.split(',');     
  for(var i = 3; i < tableObj.rows.length; i++)
  {
    var tablevalue = tableObj.rows[i].cells[0].firstChild.data; 
    var tableIdValue = tableObj.rows[i].cells[0].lastChild.value;
    pramArray = new Array();
    prayArrId = new Array();
    if(tableObj.rows[i].cells[0].isChecked)
    {
			var index = nameList.indexOf(trim(tablevalue));
      if(index >= 0)
      {
      	nameList.remove(index);
      }           
        for(var j = 0; j < nameList.length; j++)
        {
            pramArray.push(nameList[j]);
        }
        nameList = pramArray;
        for(var k = 0; k < idList.length; k++)
        {
          if(trim(tableIdValue) != idList[k])
          {
            prayArrId.push(idList[k]);
          }
        }
        idList = prayArrId;
    }
    tableObj.rows[i].cells[0].style.backgroundColor = "rgb(255, 255, 255)";
  	tableObj.rows[i].cells[0].isChecked = false;
  }
  parentDocumentObj.getElementById(TO_NAME).value = trim(nameList.join(","));
  parentDocumentObj.getElementById(TO_ID).value = trim(idList.join(","));
}
function add_all_dept()
{
  var parentDocumentObj = getOpenner();
  parentDocumentObj.getElementById(TO_NAME).value = "全体部门";
  parentDocumentObj.getElementById(TO_ID).value = "ALL_DEPT";
  parent.close();
}
/*
function click_dept(dept_id)
{
  TO_VAL=to_id.value;
  targetelement=$(dept_id);
  dept_name=targetelement.title;

  if(TO_VAL.indexOf(","+dept_id+",")>0 || TO_VAL.indexOf(dept_id+",")==0)
  {
    if(TO_VAL.indexOf(dept_id+",")==0)
    {
       to_id.value=to_id.value.replace(dept_id+",","");
       to_name.value=to_name.value.replace(dept_name+",","");
       borderize_off(targetelement);
    }
    if(TO_VAL.indexOf(","+dept_id+",")>0)
    {
       to_id.value=to_id.value.replace(","+dept_id+",",",");
       to_name.value=to_name.value.replace(","+dept_name+",",",");
       borderize_off(targetelement);
    }
  }
  else
  {
    to_id.value+=dept_id+",";
    to_name.value+=dept_name+",";
    borderize_on(targetelement);
  }
}
function borderize_on(targetelement)
{
   if(targetelement.className.indexOf("TableRowActive") < 0)
      targetelement.className = "TableRowActive " + targetelement.className;
}
function borderize_off(targetelement)
{
   if(targetelement.className.indexOf("TableRowActive") >= 0)
      targetelement.className = targetelement.className.substr(15);
}
*/
</script>
</head>
<body topmargin="1" leftmargin="0" class="" onload="doInit()">
<!--
if($OPTION_TEXT=="")
{
   Message("提示","未定义或无可管理部门","blank");
}
-->

<div id="user_DIV">
<table class="TableBlock" width="95%" name="userTable" id="userTable">
	<tr class="TableHeader">
		<td onclick="javascript:add_all_dept();" style="cursor:pointer" align="center">全体部门</td>
	</tr>
  <%
  if(deptList.size() <= 0)
  {
  %>
	  <tr class="TableData" id="nullDIV">
	    <td align="center">未定义部门</td>
	  </tr>
	<%
	}
  else
  {
	%>
	  <tr class="TableControl" id="Divtr">
	    <td onclick="add_all();" style="cursor:pointer" align="center">全部添加</td>
	  </tr>
	  <tr class="TableControl" id="Divtr1">
	    <td onclick="del_all();" style="cursor:pointer" align="center">全部删除</td>
	  </tr>
	  <%
	  for(Iterator it = deptList.iterator(); it.hasNext();)
	  {
	  	YHDepartment dept = (YHDepartment)it.next();
	  %> 
	  <tr class="TableData" style="cursor:pointer" id="TableDataDIV">
	    <td class="menulines1" id="userName" title="userName" style="background-color:rgb(255, 255, 255)" align="center" isChecked="" onclick="click_user(this)">
	      <%=dept.getDeptName()%>
	      <span id="spanstate" title="userName" style="color:#FF0000;"></span>
	      <input type="hidden"  name="userNameID" value="<%=dept.getSeqId()%>"></td>
	  </tr>
	  <%
		}
  }
  %>
</table>
<tbody id="tbody">
</tbody>
</table>
</div>
</body>
</html>