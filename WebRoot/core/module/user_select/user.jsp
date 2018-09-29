<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%
  request.setCharacterEncoding("UTF-8");
  ArrayList<YHPerson> personList = (ArrayList<YHPerson>)request.getAttribute("PERSON_LIST");
  if(personList == null)
  {
  	personList = new ArrayList<YHPerson>();
  }
  String LOCAL = request.getParameter("LOCAL");
  if(LOCAL == null)
  {
  	LOCAL = "";
  }
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<style>
.menulines{}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script>
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

</script>
</head>

<body class="" topmargin="1" leftmargin="0" onload="doInit()">

<!--
include_once("inc/my_priv.php");
if($MODULE_ID=="2")
   $EXCLUDE_UID_STR=my_exclude_uid();

 $EXCLUDE_UID_STR = td_trim($EXCLUDE_UID_STR);
 if($DEPT_ID=="")
 { 	  
    if(is_dept_priv($LOGIN_DEPT_ID, $DEPT_PRIV, $DEPT_ID_STR, 1))
    {
       $DEPT_ID=$LOGIN_DEPT_ID;
    }
    else
    {
       Message("","请选择部门");
       exit;
    }
 }

 //============================ 显示人员信息 =======================================
 if($USER_PRIV=="")
 {
    $query = "SELECT UID,USER_ID,USER_NAME,PRIV_NO from USER,USER_PRIV where (DEPT_ID='$DEPT_ID' or find_in_set('$DEPT_ID',DEPT_ID_OTHER)) and USER.USER_PRIV=USER_PRIV.USER_PRIV";
    if(!$MANAGE_FLAG)
        $query .= " and NOT_LOGIN!='1'";
    if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
       $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
    if($EXCLUDE_UID_STR!="")
       $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
    $query .= " order by PRIV_NO,USER_NO,USER_NAME";
    
    $TITLE=$SYS_DEPARTMENT[$DEPT_ID]["DEPT_NAME"];
 }
 else
 {
    $query = "SELECT UID,USER_ID,USER_NAME,DEPT_ID from USER where USER_PRIV='$USER_PRIV' and DEPT_ID!=0";
    if(!$MANAGE_FLAG)
        $query .= " and NOT_LOGIN!='1'";
    if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
       $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
    if($EXCLUDE_UID_STR!="")
       $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
    $query .= " order by USER_NO,USER_NAME";
    
    $query1 = "select PRIV_NAME from USER_PRIV where USER_PRIV='$USER_PRIV'";
    $cursor1= exequery($connection,$query1);
    if($ROW=mysql_fetch_array($cursor1))
       $TITLE=$ROW["PRIV_NAME"];
 }
-->

<table id="userTable" class="TableBlock" width="100%">
<tr class="TableHeader">
  <td colspan="2" align="center"><b><%=LOCAL%></b></td>
</tr>

<!--
 $cursor= exequery($connection,$query);
 $USER_COUNT=0;
 while($ROW=mysql_fetch_array($cursor))
 {
    $UID=$ROW["UID"];
    $USER_ID=$ROW["USER_ID"];
    $USER_NAME=$ROW["USER_NAME"];
    $DEPT_ID_I=$ROW["DEPT_ID"];
    $PRIV_NO_I=$ROW["PRIV_NO"];
    
    if($USER_PRIV=="" && ($ROLE_PRIV=="0" && $PRIV_NO_I<=$MY_PRIV_NO || $ROLE_PRIV=="1" && $PRIV_NO_I< $MY_PRIV_NO || $ROLE_PRIV=="3" && !find_id($PRIV_ID_STR, $PRIV_NO_I)))
       continue;
    else if($USER_PRIV!="" && !is_dept_priv($DEPT_ID_I, $DEPT_PRIV, $DEPT_ID_STR, true))
       continue;

    $USER_COUNT++;
    if($USER_COUNT==1)
    {
<tr class="TableControl">
  <td onclick="add_all('1');" style="cursor:pointer" align="center">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="del_all('1');" style="cursor:pointer" align="center">全部删除</td>
</tr>
    }
 }
-->

  <%
  if(personList.size() <= 0){
  %>
  <tr class="TableData" id="nullDIV">
    <td align="center">未定义用户</td>
  </tr>
  <%}else{%>
  <tr class="TableControl" id="Divtr">
    <td onclick="add_all();" style="cursor:pointer" align="center">全部添加</td>
  </tr>
  <tr class="TableControl" id="Divtr1">
    <td onclick="del_all();" style="cursor:pointer" align="center">全部删除</td>
  </tr>
  <%
  for(Iterator it = personList.iterator(); it.hasNext();){
    YHPerson person = (YHPerson)it.next();
  %> 
  <tr class="TableData" style="cursor:pointer" id="TableDataDIV">
    <td class="menulines1" id="userName" title="userName" style="background-color:rgb(255, 255, 255)" align="center" onclick="click_user(this)">
      <%=person.getUserName()%><span id="spanstate" title="userName" style="color:#FF0000;"></span><input type="hidden"  name="userNameID" value=<%=person.getSeqId()%>></td>
  </tr>
  <%
     }
    }
  %>



<!--

//============================ 显示辅助角色 =======================================
if($USER_PRIV!="")
{
   $query = "SELECT UID,USER_ID,USER_NAME,DEPT_ID from USER where (USER_PRIV_OTHER like '$USER_PRIV,%' or USER_PRIV_OTHER like '%,$USER_PRIV,%') and USER_PRIV!='$USER_PRIV' and DEPT_ID!=0";
   if(!$MANAGE_FLAG)
       $query.= " and NOT_LOGIN!='1'";
    if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
       $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
   if($EXCLUDE_UID_STR!="")
      $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
   $query.= " order by USER_NO,USER_NAME";
  
   $cursor= exequery($connection,$query);
   $USER_COUNT1=0;
   while($ROW=mysql_fetch_array($cursor))
   {
      $UID=$ROW["UID"];
      $USER_ID=$ROW["USER_ID"];
      $USER_NAME=$ROW["USER_NAME"];
      $DEPT_ID_I=$ROW["DEPT_ID"];
      
      if(!is_dept_priv($DEPT_ID_I, $DEPT_PRIV, $DEPT_ID_STR, true))
         continue;
      
      $USER_COUNT++;
      $USER_COUNT1++;
      if($USER_COUNT1==1)
      {
<tr class="TableHeader">
  <td colspan="2" align="center"><b>辅助角色</b></td>
</tr>
<tr class="TableControl">
  <td onclick="add_all('2');" style="cursor:pointer" align="center">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="del_all('2');" style="cursor:pointer" align="center">全部删除</td>
</tr>
      }
   }//while
}

if($USER_COUNT==0)
{
<tr class="TableData">
  <td align="center">未定义用户</td>
</tr>
}
-->

</table>
</body>
</html>