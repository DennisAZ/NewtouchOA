<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String type = request.getParameter("type");
  if(type==null){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加车辆信息</title>
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
  if(document.form1.vDate.value!=""&&!isValidDateStr($("vDate").value)){
    alert("购买日期格式不对，形如：1999-01-02！");
    $("vDate").focus();
    $("vDate").select();
    return false;
  }

    return true;
}
function doOnload(){
  if(type != 1){
    getVehicleType();
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
  window.location.href = "<%=contextPath%>/subsys/oa/vehicle/manage/addVehicle.jsp";
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">

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
<br><center><input type="button" class="BigButton" value="返回" onclick="returnBack();"></center>
 
  <%
} else{%>
 <form action="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/addVehicle.act" enctype="multipart/form-data" method="post" name="form1" id = "form1" onsubmit="return CheckForm()">
<table class="TableBlock" align="center" width="70%">
    <tr>
      <td nowrap class="TableContent" width="80"> 车牌号：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" id="vNum" name="vNum" size="20" maxlength="100" class="BigInput" value="">
      </td>
      <td class="TableData" width="250" rowspan="6">
<center>暂无照片</center>	
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 厂牌型号：</td>
      <td class="TableData">
        <input type="text" name="vModel" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 发动机号：</td>
      <td class="TableData">
        <input type="text" name="vEngineNum" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 车辆类型：</td>
      <td class="TableData">        
        <select name="vType" id="vType" >
          <option value="">请选择</option>
        </select>&nbsp;车辆类型可在“系统管理”->“分类码管理”模块设置

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
        <input type="text" name="vPrice" id="vPrice" size="12" maxlength="16" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 车辆照片上传：</td>
      <td class="TableData" colspan="2">
        <input type="hidden" name="attachmentId" size="40" class="BigInput" title="选择附件文件">
        <input type="file" name="attachmentName" size="40" class="BigInput" title="选择附件文件">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 购买日期：</td>
      <td class="TableData" colspan="2">
        <input type="text" id="vDate" name="vDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" >日期格式形如 1999-01-02
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
        <select name="vStatus" >
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
        <textarea name="vRemark" class="BigInput" cols="57" rows="3"></textarea>
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="3" align="center">
        <input type="hidden" value="" name="SEQ_ID">
        <input type="submit" value="保存" class="BigButton" >&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
    </table>
</form>
<%} %>
</body>
</html>
