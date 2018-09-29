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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
var  selfdefMenu = {
  	office:["downFile","dump","read","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],  
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
	}

function doOnloadFile(seqId,attachmentName,attachmentId){
  var attr = $("attr");
  attachMenuSelfUtil(attr,"vehicle",attachmentName ,attachmentId.value, '','',seqId,selfdefMenu);
}
function doOnload(){
  //getVehicleType();
  doInit();
}
function doInit(){
  var seqId = document.getElementById('seqId').value;
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
    var insuranceDate=prc.insuranceDate; 
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
    document.getElementById('vModel').innerHTML=vModel; 
    document.getElementById('vNum').innerHTML=vNum; 
    document.getElementById('vDriver').innerHTML=prc.vDriverName; 
    document.getElementById('vType').innerHTML=classDesc; 
    if (vDate && vDate.length > 10) {
      document.getElementById('vDate').innerHTML=vDate.substr(0,10); 
    }
    if (insuranceDate && insuranceDate.length > 10) {
      document.getElementById('insuranceDate').innerHTML=insuranceDate.substr(0,10); 
    }
    document.getElementById('vStatus').innerHTML=vStatusDesc; 
    document.getElementById('vRemark').innerHTML=vRemark;
    document.getElementById('vEngineNum').innerHTML=vEngineNum; 
    document.getElementById('vPrice').innerHTML=insertKiloSplit(vPrice,2);
    //shwoDetailSize(seqId);
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
        attr = "<a href='<%=contextPath %>/subsys/oa/vehicle/manage/vehiclePicture.jsp?attachmentId=" + attachmentId + "&attachmentName=" + encodeURIComponent(attachmentName) +"&filePath="+filePath+"&seqId="+seqId + "' title='点击查看放大图片' target='_blank'><img src=<%=contextPath %>/getFile?uploadFileNameServer="+filePath+"/"+attachmentId + "_" + encodeURIComponent(attachmentName) + " width='220' height='220'> </img></a>";
      } else{
        attr = "<a href='#' onclick=\"downLoadFile('"+attachmentName+"','" + attachmentIdDate + "_" +attachmentId+"','vehicle');\" title='点击打开文件'>文件名：" + attachmentName + "</a>";
      }
    }

    $("attr").innerHTML= attr; 
  }
}
function toOpenPicture(seqId,attachmentId,attachmentName,filePath){
  alert(filePath);
  var requestURL = "<%=contextPath %>/subsys/oa/vehicle/manage/vehiclePicture.jsp?attachmentId=" + attachmentId + "&attachmentName=" + attachmentName +"&filePath="+filePath+"&seqId="+seqId;
  window.location.href = requestURL;
}
function shwoDetailSize(seqId) {
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/showDetail.act?seqId=" + seqId; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if(parseInt(prc.length,10)>0){
    $("usage").update("<a href='javascript:openOrder(" + seqId + ")'>共" + prc.length + "条预定信息</a>&nbsp;&nbsp;");
  }else{
    $("usage").update("无预定信息！");
  }
}
function openOrder(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/orderDetail.jsp?seqId=' + seqId,'','height=500,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
function openRemind(seqId){
  window.open('<%=contextPath%>/subsys/oa/vehicle/repairremind.jsp?vId=' + seqId,'','height=300,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=300,top=200,resizable=yes');
}


</script>
</head>
<body  onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" ><span class="big3"> 车辆信息</span>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" align="center" width="90%">
    <tr>
      <td nowrap class="TableContent" width="80"  > 车牌号：</td>
      <td class="TableData" id = "vNum"  >

      </td>
      <td class="TableData" width="250" rowspan="7" >
<center id="attr"></center>	
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 厂牌型号：</td>
      <td class="TableData" id = "vModel"  >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 发动机号：</td>
      <td class="TableData"  id = "vEngineNum">
 </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" > 车辆类型：</td>
      <td class="TableData" id="vType" >        
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 司机：</td>
      <td class="TableData" id = "vDriver" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"  > 购买价格：</td>
      <td class="TableData"  id = "vPrice"  >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 购买日期：</td>
      <td class="TableData"  id = "vDate">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 保险到期时间：</td>
      <td class="TableData"  id = "insuranceDate" colspan="2" >
      </td>
    </tr>    
     <tr>
      <td nowrap class="TableContent"> 预约情况：</td>
      <td class="TableData"  id ="usage" colspan="2"  >
      </td>
    </tr>
    <tr>
    <tr>
      <td nowrap class="TableContent"> 当前状态：</td>
      <td class="TableData"  id ="vStatus" colspan="2" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 备注：</td>
        <td class="TableData" colspan="2"   id = "vRemark">
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="3" align="center" class="TableData">
        <input type="hidden" value="<%=seqId %>" name="seqId" id="seqId">
            <input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;
        <input type="button" value="车辆保养提醒" class="BigButtonC" onClick="openRemind(<%=seqId %>);">&nbsp;&nbsp;
        
          <input type="button" value="关闭" class="BigButton" onclick="window.close();">&nbsp;&nbsp;
      </td>
    </tr>
    </table>
</body>
</html>
