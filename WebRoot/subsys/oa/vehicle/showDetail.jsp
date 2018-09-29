<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
%>
<head>
<title>车辆信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var seqId = '<%=seqId%>';
function doOnload(){
  //getVehicleType();
  doInit(seqId);
}
function doInit(seqId){
  var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/queryVehicleById.act?seqId="+seqId; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if(prc.seqId){
    var seqId=prc.seqId; 
    var vModel=prc.vModel; 
    var vNum=prc.vNum; 
    var vPrice=prc.vPrice; 
    var vEngineNum = prc.vEngineNum;
    var classDesc=prc.classDesc; 
    var vDate=prc.vDate; 
    var vStatus = prc.vStatus;
    var vDriver = prc.vDriver;
    var vRemark = prc.vRemark;
    var attachmentId = prc.attachmentId;
    var attachmentName = prc.attachmentName;
    var useingFlag = prc.useingFlag;
    var vStatusDesc = "<font color='#00AA00'><b>可用</b></font>";
    if(vStatus=='0'){
      vStatusDesc = "<font color='#00AA00'><b>可用</b></font>";
    }
    if(vStatus=='1'){
      vStatusDesc = "<font color='#00AA00'><b>损坏</b></font>";
    }
    if(vStatus=='2'){
      vStatusDesc = "<font color='#00AA00'><b>维修中</b></font>";
    }
    if(vStatus=='3'){
      vStatusDesc = "<font color='#00AA00'><b>报废</b></font>";
    }
    document.getElementById('vNum').innerHTML=vNum; 
    document.getElementById('vDriver').innerHTML=vDriver; 
    document.getElementById('vType').innerHTML=classDesc; 
    document.getElementById('vDate').innerHTML=vDate.substr(0,10); 
    document.getElementById('vStatus').innerHTML=vStatusDesc; 
    document.getElementById('vRemark').innerHTML=vRemark;
    var attr = "暂无照片";
    var path = json.rtMsrg;
    if(attachmentId!=''){
      var attachmentIdDate = attachmentId.substr(0,4);
      attachmentId = attachmentId.substr(5);
      attr = "<img src=<%=contextPath %>/getFile?uploadFileNameServer="+path+"/vehicle/"+attachmentIdDate+"/"+attachmentId + "_" + attachmentName + "> </img>";
    }
    $("attr").innerHTML= attr; 
  }
}
</script>
</head>
<body  onload = "doOnload();">
<table class="TableBlock" width="100%">
    <tr>
      <td nowrap class="TableContent"  > 车牌号：</td>
      <td class="TableData" id = "vNum" width="60%">

      </td>
      <td class="TableData" width="250" rowspan="6">
<center id="attr"></center>	
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 车辆类型：</td>
      <td class="TableData" id="vType" >        
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 司机：</td>
      <td class="TableData" id = "vDriver" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 购买日期：</td>
      <td class="TableData"  id = "vDate" >
      </td>
    </tr>
       <tr>
      <td nowrap class="TableContent"> 预定情况：</td>
      <td class="TableData"  id ="" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 当前状态：</td>
      <td class="TableData"  id ="vStatus" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 备注：</td>
      <td class="TableData"   id = "vRemark" colspan="2">
      </td>
    </tr>

    </table>
</body>
</html>
