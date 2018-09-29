<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
  //专网IP
  String privateNetIp = YHSysProps.getString("privateNetIp") == null ? "200.200.50.105" :    YHSysProps.getString("privateNetIp") ;
//专网端口号
  String privateNetPort = YHSysProps.getString("privateNetPort") == null ? "133.133.50.105" :   YHSysProps.getString("privateNetPort") ;
  
  //拨号网IP
  String telNetIp = YHSysProps.getString("telNetIp") == null ? "80" :  YHSysProps.getString("telNetIp") ;
  //拨号网端口号
  String telNetPort = YHSysProps.getString("telNetPort") == null ? "80" :YHSysProps.getString("telNetPort");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统初始化参数</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">



function submitForm(index){
  if($("EsbUser").value.trim() == "" ){
    alert('ESB账户是必填项！');
    $("EsbUser").select();
    $("EsbUser").focus();
    return;
  }

  if(index == '0'){
    
    getESBInfo();

  }else if (index == 1){
    if($("unitInfo").value.trim() == "" ){
      alert('单位名称是必填项！');
      $("unitInfo").select();
      $("unitInfo").focus();
      return;
    }
    if($("EsbServiceIp").value.trim() == "" ){
      alert('ESB服务器地址是必填项！');
      $("EsbServiceIp").select();
      $("EsbServiceIp").focus();
      return;
    }
    if($("EsbServicePort").value.trim() == "" ){
      alert('ESB服务器端口号是必填项！');
      $("EsbServicePort").select();
      $("EsbServicePort").focus();
      return;
    }
    
    if($("recvFilePath").value.trim() == "" ){
      alert('接收文件目录是必填项！');
      $("recvFilePath").select();
      $("recvFilePath").focus();
      return;
    }
    
 	   
    $("frist").style.display = "none";
    $("second").style.display = "none";
    $("third").style.display = "";
  }else if (index == 2){
    if($("dataFile").value.trim() == "" ){
      alert('数据文件是必填项！');
      $("dataFile").select();
      $("dataFile").focus();
      return;
    }
    form1.submit();
  }else{

  }

}

function returnPre(index){
  if(index == "0"){
    $("frist").style.display = "";
    $("second").style.display = "none";
    $("third").style.dosplay = "none";
  }else if(index == "1"){
    $("frist").style.display = "none";
    $("second").style.display = "";
    $("third").style.display = "none";
  }
}
/**
 * 获取ESB账户信息
 */
 
 
function getESBInfo(){
  var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/setting/act/YHJhSysParaAct/selectESBInfo.act";
  var json = getJsonRs(requestURL,{EsbUser:$("EsbUser").value});
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } else {
    var prc = json.rtData;
    if(prc.seqId){
      $("frist").style.display = "none";
      $("second").style.display = "";
      $("third").style.dosplay = "none";
      $("unitInfo").value = prc.deptName;
      var deptTelLine = prc.deptTelLine;
      onChangNetType(deptTelLine);

    }else{
      alert("您输入的ESB账户不存在，请再次确认！如还不存在，请与相关人员联系！");
      $("EsbUser").select();
      $("EsbUser").focus();
      return;
    }
  }
}

var privateNetIp = "<%=privateNetIp%>";
var privateNetPort = "<%=privateNetPort%>";
var telNetdIp = "<%=telNetIp%>";
var telNetPort = "<%=telNetPort%>";
function onChangNetType(deptTelLine){
  var NetIp = privateNetIp;
  var netPort = privateNetPort;
  if(deptTelLine == '1'){//拨号
    $("netType1").checked = true;
    NetIp = telNetdIp;
    netPort = telNetPort;
  }else{

    $("netType0").checked = true;
    NetIp = privateNetIp;
    netPort = privateNetPort;

  } 
  $("EsbServiceIp").value = NetIp;
  $("EsbServicePort").value = netPort;
  
  
}
</script>

<style type="text/css">




</style>
</head>
<body class="" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr> 
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle">
    <span class="big1" style="color:;"> &nbsp;系统初始化参数,请仔细按照说明书进行配置！</span><br>
    </td>
  </tr>
</table>
<br>

<div style="padding-top:70px;" align="center" >
    <form enctype="multipart/form-data"  action="<%=contextPath %>/yh/subsys/jtgwjh/setting/act/YHJhSysParaAct/importData.act" method="post" action="" name="form1" id="form1"  >
    
    
    <div style="widtd:400px;height:100px;border:1px;" id="frist">
    
    <table class="TableBlock" width="400" align="center">
        <tr>
			   <td align="right" class="TableContent" >ESB账号：<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			      <input type="text" name="EsbUser"  class="BigInput" size="20" maxlength="20" id="EsbUser"><label for="PASS1" id="PASS1_LABEL"></label>
			
			     </td>
			     
			   </tr>
		
  		  </table>
  		  
  		  <div align="center" style="padding-top:50px;">
         <input type="button" value="下一步"  class='BigButton' onClick="submitForm(0);"></div>
    </div>



   
    <div style="display:none;" id="second">
    
    <table class="TableBlock" width="600" align="center">
        <tr>
			   <td  class="TableContent" >单位信息：<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			      <input type="text" name="unitInfo"  class="BigInput" size="20" maxlength="20" id="unitInfo"><label for="PASS1" id="PASS1_LABEL"></label>
			     </td>
			   </tr>
			   <tr>
			   <td  class="TableContent" >网络类型：<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			      <input type="radio" name="netType" checked id="netType0" value="0">专网
			      &nbsp;&nbsp;
			       <input type="radio" name="netType" value="1" id="netType1">拨号
			      &nbsp;&nbsp;
			      
			      <font color="red">注：修改此信息与服务器地址、端口号时请与相关人员联系！ </font>
			     </td>
			   </tr>
     <tr>
			   <td  class="TableContent" >ESB服务器地址：端口号<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			       <input type="text" name="EsbServiceIp"  class="BigInput" size="20" maxlength="20" id="EsbServiceIp">
			     ：<input type="text" name="EsbServicePort"  class="BigInput" size="5" maxlength="10" id="EsbServicePort">
			     
			     </td>
			   </tr>
			 
			   
			  
			 
		   <tr>
			   <td  class="TableContent" >接收数据目录<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			       <input type="text" name="recvFilePath"  class="BigInput" size="20" maxlength="20" id="recvFilePath" value="D:\cache">
			     
			     </td>
			   </tr>
  		  </table>
  		  
  		  <div align="center" style="padding-top:50px;">
  		  <input type="button" value="返回"  class='BigButton' onClick="returnPre(0);">
         <input type="button" value="下一步"  class='BigButton' onClick="submitForm(1);"></div>
    </div>
    
    
    
    
   
    <div style="display:none;" id="third">
    
    <table class="TableBlock" width="600" align="center">
        <tr>
			   <td  class="TableContent" >机要A角、B角、公章数据文件：<font color="red">*</font></td>
			     <td class="TableData" align="left" >
			      <input type="file" name="dataFile"  class="BigInput" size="20" maxlength="20" id="dataFile">
			     </td>
			   </tr>
		
			   
   
  		  </table>
  		  
  		  <div align="center" style="padding-top:50px;">
  		  <input type="button" value="返回"  class='BigButton' onClick="returnPre(1);">
         <input type="button" value="初始化数据"  class='BigButtonC' onClick="submitForm(2);"></div>
    </div>
    
    
</form>
    
</div>
<br></br>
</html>