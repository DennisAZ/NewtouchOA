<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>mysql</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script type="text/javascript">

function doInit(){
    var url = contextPath + "/yh/core/funcs/connswitch/act/YHConnSwitchAct/getMysqlConnInfo.act";
      var rtJson = getJsonRs(url);
      if(rtJson.rtState == "0"){
        var data=rtJson.rtData;
        if(data!=""){
            bindJson2Cntrl(data);
         }
    
      }else{
        alert( rtJson.rtMsrg);
      }


    }


function testDbConnect(){
    var url = contextPath + "/yh/core/funcs/connswitch/act/YHConnSwitchAct/testMysqlConnect.act";
    var param="";
    param="hostName="+$("hostName").value+"&socket="+$("socket").value+"&dbase="+$("dbase").value+"&uid="+$("uid").value+"&pwd="+$("pwd").value;

     var rtJson = getJsonRs(url,param);
      if(rtJson.rtState == "0"){
        var data=rtJson.rtData.test;
        if(data=="1"){
           alert("Mysql连接成功！");
            }else{
           alert("连接失败，请查看配置信息是否正确，确认数据库是否启动！");
                }
    
      }else{
        alert( rtJson.rtMsrg);
      }

    }

function saveDbConnect(){
    if(window.confirm("确认是否要保存Mysql配置信息！")){
      var url = contextPath + "/yh/core/funcs/connswitch/act/YHConnSwitchAct/saveMysqlConnect.act";
      var param="";
      param="hostName="+$("hostName").value+"&socket="+$("socket").value+"&dbase="+$("dbase").value+"&uid="+$("uid").value+"&pwd="+$("pwd").value;
       var rtJson = getJsonRs(url,param);
        if(rtJson.rtState == "0"){ 
             alert("Mysql数据库配置成功保存！");
        }else{
          alert( rtJson.rtMsrg);
        }
      }
    }

</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> mysql连接配置信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<form method="post" name="form1" id="form1" >
<table class="TableBlock" width="70%" align="center">
  <tr>
    <td nowrap class="TableContent">主机地址：</td>
      <td class="TableData">
        <input type="text" name="hostName" id="hostName" class="BigInput"  size="15" >
      </td>
    <td nowrap class="TableContent">端口号：</td>
    <td class="TableData">
      <input type="text" name="socket" id="socket" class="BigInput"  size="15" >
    </td>  
  </tr>
  <tr>
    <td nowrap  class="TableContent">用户名：</td>
      <td class="TableData" colspan="3">
        <input type="text" name="uid" id="uid" class="BigInput"  size="30" >
      </td>

  </tr>
    <tr>
   
    <td nowrap class="TableContent" >密    码：</td>
    <td class="TableData" colspan="3">
      <input type="password" name="pwd" id="pwd" class="BigInput"  size="30" >
    </td>  
  </tr>
   <tr>
   
    <td nowrap class="TableContent" >连接数据库：</td>
    <td class="TableData" colspan="3">
      <input type="input" name="dbase" id="dbase" class="BigInput"  size="30" >
    </td>  
  </tr>
  <tr>
  <td colspan="2" class="TableContent" align="right"><input type="submit" class="BigButton" onclick="saveDbConnect();" value="保 存"></td>  
  <td colspan="2" class="TableContent" align="left"><input type="button" onclick="testDbConnect()" class="BigButton" value="测试连接"></td>
  </tr>
</table>
</form>

</body>
</html>