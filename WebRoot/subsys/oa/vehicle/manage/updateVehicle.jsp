<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String seqId = request.getParameter("seqId");
  String type = request.getParameter("type");
  if(seqId == null){
    seqId = "";
  }
  if(type==null){
    type = "";
  }
%>
<head>
<title>编辑车辆信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var type = '<%=type%>';
function CheckForm(){
  if(document.form1.vNum.value==""){
    alert("车牌号不能为空！"); 
    $("vNum").focus();
    $("vNum").select();
    return false;
  }
  if(document.form1.vPrice.value!=""){
    if(!isNumber(document.form1.vPrice.value) || document.form1.vPrice.value < 0){
      alert("你输入的价格应是数字！");
      $("vPrice").focus();
      $("vPrice").select();
      return false;
    }
   }
  if(document.form1.vDate.value!=""&&!isValidDateStr($("vDate").value) ){
    alert("购买日期格式不对，形如：1999-01-02！");
    $("vDate").focus();
    $("vDate").select();
    return false;
  }
  if(document.form1.beforeDay.value == "" || document.form1.beforeDay.value < 0){
	  document.form1.beforeDay.value = 0;
  }
  if(document.form1.beforeDay.value > 100 ){
    alert("保险最多提前100天提醒！");
    $("beforeDay").focus();
    $("beforeDay").select();
    return false;
  }

    return true;
}

function doOnload(){
  if(type!=1){
    getVehicleType();
    doInit();
    var date1Parameters = {
        inputId:'vDate',
        property:{isHaveTime:false}
        ,bindToBtn:'date'
    };
    new Calendar(date1Parameters);

    var date2Parameters = {
        inputId:'insuranceDate',
        property:{isHaveTime:false}
        ,bindToBtn:'date2'
    };
    new Calendar(date2Parameters);
  }
}
function doInit(){
  var seqId = document.getElementById('seqId').value;
  var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehicleById.act?seqId="+seqId; 
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
    var vType=prc.vType; 
    var vDate=prc.vDate; 
    var vStatus = prc.vStatus;
    var vDriver = prc.vDriver;
    var vRemark = prc.vRemark;
    var attachmentId = prc.attachmentId;
    var attachmentName = prc.attachmentName;
    var useingFlag = prc.useingFlag;
    document.getElementById('vModel').value=vModel; 
    document.getElementById('vNum').value=vNum; 
    document.getElementById('vDriver').value=vDriver; 
    document.getElementById('vType').value=vType; 
    document.getElementById('vDate').value=vDate.substr(0,10); 
    document.getElementById('vStatus').value=vStatus; 
    document.getElementById('vRemark').value=vRemark;
    document.getElementById('vEngineNum').value=vEngineNum; 
    document.getElementById('vPrice').value=vPrice; 
    $("vDriverName").value = prc.vDriverName;
    $("insuranceDate").value = prc.insuranceDate.substring(0,10);
    $("beforeDay").value = prc.beforeDay;
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
        attr = "<a href='#' onclick=\"downLoadFile('"+attachmentName+"','" + attachmentIdDate + "_"+attachmentId+"','vehicle');\" title='点击打开文件'>文件名：" + attachmentName + "</a>";
      }
    }
    $("attr").innerHTML= attr;
  }
}
function getVehicleType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/getCodeItem.act"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("vType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function returnBack(){
  window.location.href = "<%=contextPath%>/subsys/oa/vehicle/manage/vehicleInfo.jsp";
}

</script>
</head>
<body class="" topmargin="5" onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" HEIGHT="20"><span class="big3"> 车辆信息</span>
    </td>
  </tr>
</table>
<% if(type!=null&&type.equals("1")){
  
  %>
 <table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">车辆信息保存成功！</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="history.go(-1);"></center>
 
  <%
} else{%>
<form action="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/updateVehicle.act" enctype="multipart/form-data" method="post" name="form1" id = "form1" onsubmit="return CheckForm();">
<table class="TableBlock" align="center" width="70%">
    <tr>
      <td nowrap class="TableContent" width="80"> 车牌号：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" id = "vNum" name="vNum" size="20" maxlength="100" class="BigInput" value="">
      </td>
      <td class="TableData" width="250" rowspan="6">
<center id="attr"></center>	
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 厂牌型号：</td>
      <td class="TableData">
        <input type="text" id = "vModel" name="vModel" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 发动机号：</td>
      <td class="TableData">
        <input type="text" id = "vEngineNum" name="vEngineNum" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 车辆类型：</td>
      <td class="TableData">        
        <select id = "vType" name="vType" >
        </select>&nbsp;车辆类型可在<br>“系统管理”->“系统代码设<br>置”模块设置
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 司机：</td>
      <td class="TableData">
           <input type="hidden" name="vDriver" id="vDriver" size="12" maxlength="100" class="BigInput" value="">
                <input type="text" name="vDriverName"  id="vDriverName" size="12" maxlength="100" class="BigStatic" value=""  readOnly>
               <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vDriver','vDriverName']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('vDriver').value='';$('vDriverName').value='';">清空</a>
	 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 购买价格：</td>
      <td class="TableData">
        <input type="text" id = "vPrice" name="vPrice" size="12" maxlength="25" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 车辆照片上传：</td>
      <td class="TableData" colspan="2">
        <input type="hidden" id = "attachmentId" name="attachmentId" size="40" class="BigInput" title="选择附件文件">
           <input type="file" id = "attachmentName" name="attachmentName" size="40" class="BigInput" title="选择附件文件">
   
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 购买日期：</td>
      <td class="TableData" colspan="2">
        <input type="text" id = "vDate" name="vDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer">
        日期格式形如 1999-1-2
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 保险到期时间：</td>
      <td class="TableData" colspan="2">
        <input type="text" id="insuranceDate" name="insuranceDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" >&nbsp;&nbsp;&nbsp;&nbsp;
                     提前<input type="text" id="beforeDay" name="beforeDay" style="width:20px;" value="30">天 开始提醒给调度员
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 当前状态：</td>
      <td class="TableData" colspan="2">
        <select id ="vStatus" name="vStatus" >
          <option value="0" >正常</option>
          <option value="1" >损坏</option>
          <option value="2" >维修中</option>
          <option value="3" >报废</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 备注：</td>
      <td class="TableData" colspan="2">
        <textarea id = "vRemark" name="vRemark" class="BigInput" cols="57" rows="3"></textarea>
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="3" align="center">
        <input type="hidden" value="<%=seqId %>" name="seqId" id="seqId">
        <input type="submit" value="保存" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
          <input type="button" value="返回" class="BigButton" onclick="history.go(-1);">&nbsp;&nbsp;
      </td>
    </tr>
    </table>
</form>
<%} %>
</body>
</html>
