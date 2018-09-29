<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.*" %>
<%@ include file="/core/inc/header.jsp" %>
<%
	request.setCharacterEncoding("UTF-8");
	ArrayList<YHUserPriv> userPrivList = (ArrayList<YHUserPriv>)request.getAttribute("USER_PRIV");

	String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.menulines{}
</style>
<title>选择角色</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
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

function click_priv(userName)
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
  for(var i = 3; i < tableObj.rows.length - 1; i++)
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
  for(var i = 3; i < tableObj.rows.length - 1; i++)
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
</script>
</head>

<body topmargin="1" leftmargin="0" class="" onload="doInit()">

<!--
include_once("inc/my_priv.php");

if($LOGIN_USER_PRIV!="1" && $PRIV_OP)
{
   $ROLE_PRIV="0";
   if($MY_PRIV_NO=="")
   {
      $query1 = "select PRIV_NO from USER_PRIV where USER_PRIV='$LOGIN_USER_PRIV'";
      $cursor1= exequery($connection,$query1);
      if($ROW=mysql_fetch_array($cursor1))
         $MY_PRIV_NO=$ROW["PRIV_NO"];
   }
}

$PRIV_ID_STR = td_trim($PRIV_ID_STR);

$query = "SELECT * from USER_PRIV where 1=1";
if($ROLE_PRIV=="0")
   $query .= " and PRIV_NO>$MY_PRIV_NO";
else if($ROLE_PRIV=="1")
   $query .= " and PRIV_NO>=$MY_PRIV_NO";
else if($ROLE_PRIV=="3" && $PRIV_ID_STR != "")
   $query .= " and USER_PRIV in ($PRIV_ID_STR)";

if($LOGIN_USER_PRIV!="1" && $PRIV_OP)
   $query .= " and USER_PRIV!=1";
$query .= " order by PRIV_NO ";

$COUNT=0;
$cursor= exequery($connection,$query);
while($ROW=mysql_fetch_array($cursor))
{
   $USER_PRIV=$ROW["USER_PRIV"];
   $PRIV_NAME=$ROW["PRIV_NAME"];
   $COUNT++;

   if($COUNT==1)
   {
-->
 <table id="userTable" class="TableBlock" width="95%">
  <tr class=TableHeader>
    <td align="center">选择角色 <input type="button" class="SmallButton" value="确定" onclick="window.close()"></td>
  </tr>
   <tr class="TableControl">
     <td onclick="javascript:add_all();" style="cursor:pointer" align="center">全部添加</td>
   </tr>
   <tr class="TableControl">
     <td onclick="javascript:del_all();" style="cursor:pointer" align="center">全部删除</td>
   </tr>
<!--
   }
-->
<%
	if(userPrivList != null && userPrivList.size() <= 0)
	{
		out.println("<tr class=\"TableData\"><td align=\"center\">未定义用户角色</td></tr>");
	}
	else if(userPrivList != null)
	{
		Iterator it = userPrivList.iterator();
		while(it.hasNext())
		{
			YHUserPriv userPriv = (YHUserPriv)it.next();
			%>
	    <tr class="TableData">
  	    <td align="center" onclick="click_priv(this)" style="cursor:pointer" isChecked="">
	  	    <%=userPriv.getPrivName()%>
		      <span id="spanstate" title="userName" style="color:#FF0000;"></span>
		      <input type="hidden"  name="userNameID" value="<%=userPriv.getSeqId()%>">
	      </td>
	    </tr>
	    <%
		}
	}
%>
<!--
}//while

if($COUNT==0)
   Message("提示","无可选择角色","blank");
else
{
-->
  <tr class=TableControl>
    <td align="center"><input type="button" class="BigButton" value="确定" onclick="window.close()"></td>
  </tr>
<!--
}
-->
</body>
</html>