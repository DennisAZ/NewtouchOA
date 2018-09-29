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
  shwoDetailSize(seqId);
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
    var vStatusDesc = "<font color='#00AA00'><b>正常</b></font>";
    if(vStatus=='0'){
      vStatusDesc = "<font color='#00AA00'><b>正常</b></font>";
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
    document.getElementById('vDriver').innerHTML=prc.vDriverName; 
    document.getElementById('vType').innerHTML=classDesc; 
    if (vDate && vDate.length > 10) {
      document.getElementById('vDate').innerHTML=vDate.substr(0,10); 
    } 
    document.getElementById('vStatus').innerHTML=vStatusDesc; 
    document.getElementById('vRemark').innerHTML=vRemark;
    var attr = "暂无照片";
    var path = json.rtMsrg;
    var pictureStr = ["png","gif","jpg","bmp","PNG","GIF","JPG","BMP"];
    if(attachmentId!=''){
      var attachmentIdDate = attachmentId.substr(0,4);
      var fileName =attachmentName.lastIndexOf("\.");
      fileName = attachmentName.substring(fileName+1,attachmentName.length);
      var pType = "2";//不是图片
      for(var i =0 ; i< pictureStr.length ; i++){
        if(pictureStr[i]==fileName){
          pType = "1";
          break;
        }
      }
      attachmentId = attachmentId.substr(5);
      var filePath = path+"/vehicle/"+attachmentIdDate ;
      if(pType=='1'){
        attr = "<a href='<%=contextPath %>/subsys/oa/vehicle/manage/vehiclePicture.jsp?attachmentId=" + attachmentId + "&attachmentName=" + encodeURIComponent(attachmentName) +"&filePath="+filePath+"&seqId="+seqId + "' title='点击查看放大图片' target='_blank'><img src=<%=contextPath %>/getFile?uploadFileNameServer="+filePath+"/"+attachmentId + "_" + encodeURIComponent(attachmentName) + " width='160' height='160'> </img></a>";
      } else{
        attr = "<a href='#' onclick=downLoadFile('"+attachmentName+"','" + attachmentIdDate + "_" +attachmentId+"','vehicle'); title='点击打开文件'>文件名：" + attachmentName + "</a>";
      }
    }
    $("attr").innerHTML= attr; 
  }
} 
function shwoDetailSize(seqId) {
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/showDetail.act?seqId=" + seqId; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if (prc.length > 0) {
    $("listName").update("<a href='javascript:openOrder(" + seqId + ")'>共" + prc.length + "条预定信息</a>&nbsp;&nbsp;<a href='javascript:openAllOrder()'>更多 …… </a>");
  }
  if (prc.length <= 0 ) {
    $("listName").update("无预定信息&nbsp;&nbsp;<a href='javascript:openAllOrder()'>更多 …… </a>");
  }
}
function openOrder(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/orderDetail.jsp?seqId=' + seqId,'','height=430,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
function openAllOrder(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/allOrderDetail.jsp','','height=430,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
</script>
</head>
<body  onload = "doOnload();">
<table class="TableBlock" width="100%">
    <tr>
      <td nowrap class="TableContent"  > 车牌号：</td>
      <td class="TableData" id = "vNum" width="60%">

      </td>
      <td class="TableData" width="300" rowspan="6">
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
      <div id="listName"></div>
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
