<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String count = request.getParameter("count");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签章制作</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/AssistInput1.0.js" ></script>

<script type="text/javascript">
var certdata_array = new Array(); //添加的证书绑定的数量
function doInit(){
  //deptFunc();
 //  outDeptFunc();
  var d = new Date();
  $("SealID_prefix").value = d.getYear();
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getCounter.act";
  var seal_id = "20103"
  var rtJsons = getJsonRs(url, "SEAL_ID=" + seal_id);
  if(rtJsons.rtState == '0'){
    var result = rtJsons.rtData;
    //alert(result);
  }
  
  selectDept("deptName","dept");
}


function getCounter(){
  var prefix = $("SealID_prefix").value;//印章前缀，目前只读，用当前年份，如：2010
  var mid = $("SealID_mid").value;//印章ID的中间位置部分

  if(mid==""){//印章ID中间部分不能为空
   alert("印章ID填写不完整!");
   return;
  }
  var seal_id = prefix + mid;//印章ID的前缀 加上 中间部分
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getCounter.act";
  var rtJsons = getJsonRs(url, "SEAL_ID=" + seal_id);
  if(rtJsons.rtState == '0'){
    var result = rtJsons.rtData;
    if(result != ""){
      $("SealID_counter").value = result;//SealID_counter是界面上的一个hidden项

      $("SealID").value = prefix + mid + result;//SealID是合成后的、完整的印章ID
    }
  }
}


function SelBMPFile_onclick(){
  var obj = document.getElementById("DMakeSealV61");
  if(!obj) {
   return false;
  }
  if(0 != obj.SelectBmpFile()){
   return false;
  }

  
  var vBmpHeightMM = parseInt(obj.fBmpHeightMM*100)/100; //图片高度（毫米）
  var vBmpWidthMM = parseInt(obj.fBmpWidthMM*100)/100;   //图片宽度（毫米）
  if(vBmpHeightMM == 0  || vBmpWidthMM == 0){            //宽度和高度都为0则中断程序执行

    return false;                                        //alert("BMP图片错误");
  }
  
  //像素 obj.lBmpWidth
  var  lBmpWidth = obj.lBmpWidth;
  var  lBmpHeight = obj.lBmpHeight;
  var vMM = parseInt(lBmpWidth*100/(100*400) * 2.54,10);
  var vHM = parseInt(lBmpHeight*100/(100*400) * 2.54,10);
  $("SealWidth").value = vMM;                    //界面上的input框：图片宽度
  $("SealHeight").value = vHM;                  //界面上的input框：图片高度
}

function SealWriteToKey_onclick(key_flag) {
  if(checkForm()){
    var returnF = getDeptByName();
    if(!returnF){
      return;
    }
    var obj = $("DMakeSealV61");                           //获取activex控件对应的OBJECT对象
    if(!obj){
      alert("尚未安装控件");
      return false;
    }

   //获取证书信息（证书暂不考虑，此处代码略掉30行，2010-3-31 14:10:43）
   //不写key的情况，先清空当前activex对象里的印章数据 add by lx 20090420
    if (key_flag == 0) {                                   //对应按钮“生成印章”（印章写入数据库）
      var vID = 0;
      do {
        vID = obj.GetNextSeal(vID);                       //遍历activex控件，找到里面的返回值是1、2、……，还是印章节点的名称？（代表第一、二、……个印章节点）

        if(!vID)
          break;
        obj.DelSeal(vID);                                //删除每个印章节点，vID是印章的顺序编号，还是名称？
      } while(vID);
    }
    obj.NewSealStart();                                  //准备制章
    SaveSeal();                                          //设置印章数据
    if(0 != obj.NewSealEnd()) {                          //制章收尾代码（具体含义？）
     //alert("载入BMP图片失败");
     return false;
    }
    
    //----- 证书相关，略掉 begin -----
    var vLen =  certdata_array.length;
    //for (i = vLen - 1; i >= 0 ; i--) {                   //设置绑定证书
     // obj.AddCtrlCert(4, certdata_array[i], 15, 7); 
   // }
    var flag = false;
    //----- 证书相关，略掉 end -----
    //保存印章数据到activex控件？同时把印章的base64数据赋值给隐藏域SEAL_DATA
    $("SEAL_DATA").value = obj.SaveData();
    
    
    if(key_flag == 1){                                   //对应按钮“写入Ukey”，这个分支yh移植暂不考虑
      obj.AddSealData($("SEAL_DATA").value);
      if(0 != obj.SaveSeal(2,"",0)){
       alert("制作印章失败");
       return false;
      }else{
       //印章制作成功，写入后台数据库数据
       //_post("create.php","SealID="+$("SealID").value+"&SealName="+$("SealName").value+"&CERT_STR="+$("COPY_TO_ID").value+"&SEAL_DATA="+$("SEAL_DATA").value.replace(/\+/g, '%2B')+"&DEPT_ID="+$("CompName").value,function(){alert("制作印章成功!");});
      }
    }else{   //对应按钮“生成印章”（印章写入数据库）
      //印章制作成功，写入后台数据库数据
      //_post("create.php","SealID="+$("SealID").value+"&SealName="+$("SealName").value+"&CERT_STR="+$("COPY_TO_ID").value+"&SEAL_DATA="+$("SEAL_DATA").value.replace(/\+/g, '%2B')+"&DEPT_ID="+$("CompName").value,function(req){alert("制作印章成功!");});   
      flag = insertSeal();
      if (flag) {
        insertSealLog();
      }
    }
    if (flag) {
    //清空activex里边的数据（可能是生成印章时用到的ID、名称等信息，这些可能是临时数据，用完需要清掉）
      obj.ResetContent();

      //清空界面上每一个input框的内容，每一个checkbox设置为不勾选
      clear_option();
      window.location.reload();
    }
    
  }
  
}

function insertSealLog(){
  var sealId = $('SealID').value;
  var sealName = $('SealName').value;
  //var cetrStr = $('user').value;
  var sealData = $('SEAL_DATA').value.replace(/\+/g, '%2B');
  var deptId = $('dept').value;
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealLogAct/addSealLog.act";
  var rtJsons = getJsonRs(url, "sealId="+sealId+"&sealName="+encodeURIComponent(sealName)+"&deptId="+deptId);
  if(rtJsons.rtState == '0'){
    //alert("制作印章成功!");
  } else {
    //alert(rtJsons.rtMsrg);
  }
}

function insertSeal(){
  var sealId = $('SealID').value;
  var sealName = $('SealName').value;
  //var cetrStr = $('user').value;
  var sealData = $('SEAL_DATA').value.replace(/\+/g, '%2B');
  var deptId = $('dept').value;
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/addSeal.act";
  var rtJsons = getJsonRs(url, "sealId="+sealId+"&sealName="+encodeURIComponent(sealName)+"&sealData="+sealData+"&deptId="+deptId);
  if(rtJsons.rtState == '0'){
    alert("制作印章成功!");
    return true;
  }else {
    alert(rtJsons.rtMsrg);
    return false;
  }
}

//清空界面上每一个input框的内容，每一个checkbox设置为不勾选
function clear_option(){
  var obj = document.getElementsByTagName("input");
  for(var i = 0;i < obj.length; i++){
    if(obj[i].type == "text") 
      obj[i].value = "";
    else if(obj[i].type == "checkbox") 
      obj[i].checked = false;
  }
}

function SaveSeal() {
  var obj = $("DMakeSealV61");
  if(!obj) {
     return false;
  }

  if(!isNumber($("SealWidth").value)){
    alert("图片宽度和高度只能是数字");
    return false;
  }
 //检查图片高度和宽度是否为0，如果是则中断程序执行
  var vBmpHeightMM = parseInt($("SealWidth").value*100)/100; 
  var vBmpWidthMM = parseInt($("SealHeight").value*100)/100;
  if(vBmpHeightMM == 0  || vBmpWidthMM == 0){
    return false;
  }
  //设置印章数据
  obj.fSealWidthMM = $("SealWidth").value/$("SealZoom").value;//SealZoom是“印章大小”，实际上是印章放大倍数
  obj.fSealHeightMM = $("SealHeight").value/$("SealZoom").value;//
  obj.strSealName = $("SealName").value;//印章名称
  obj.strSealID = $("SealID").value;//印章ID（三部分组和而成的ID）
  obj.strOpenPwd = $("SealPwd").value;//印章密码
  obj.strCompName = $("dept").value;//印章单位（实际上存的是部门ID）
  obj.lBitCount = $("SealBitCount").value;//图片色彩深度
  //安全选项相关，这部分yh移植时暂不考虑
//  if($("checkboxSign").checked){
//    obj.blNeedSign = 1;
//  }
}


function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("dept");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}


/**
 * 获取外部组织机构
 */
function outDeptFunc(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getExpt.act";
  var rtJson = getJsonRs(url);
  //alert(rsText);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("dept");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}


/*function selectDept(){
  var deptName = $("deptName").value;
  var selects = document.getElementById("dept");
  for(var i=0;i<selects.options.length;i++){
    var option = selects.options[i];
    // alert(option.innerHTML +":"+ "&nbsp;|-" + deptName)
    //if(option.innerHTML.substring(7,option.innerHTML.length) == deptName){
      if(option.innerHTML.substring(7,option.innerHTML.length).indexOf(deptName)>=0){
        option.selected = true;
      }
      
   // }
   // break;
  }
}*/
//自动补全得取国家
function selectDept(deptName,deptId) {
  var url ="<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/selectDeptByName.act?deptName=" + encodeURIComponent($("deptName").value) ;
  var par = {bindToId:deptName,requestUrl:url,showLength:80,bindToHidden:deptId};
  new AssistInuput(par);
}

/**
 * 根据名称反查
 */
function getDeptByName(){
  var url ="<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/selectDeptName.act?deptName=" + encodeURIComponent($("deptName").value) ;
  var rtJson = getJsonRs(url);
  //alert(rsText);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return false;
  }
  var prcs = rtJson.rtData;
  if(prcs.length >0){
    $("dept").value = prcs[0].id;
    return true;
  }else{
    alert("请确认是否有此单位！");
    $("deptName").select();
    $("deptName").focus();
    return false ;
  }
  return true;
}


function checkForm(){
  if($("SealName").value.trim() == ''){
    $("SealName").select();
    $("SealName").focus();
    alert("印章名称是必填项！");
    return false;
  }


  if(!isNumber($("SealWidth").value.trim())){
    $("SealWidth").select();
    $("SealWidth").focus();
    alert("印章宽度必须是数字类型！");
    return false;
  }
  
  if(!isNumber($("SealHeight").value.trim())){
    $("SealHeight").select();
    $("SealHeight").focus();
    alert("印章高度必须是数字类型！");
    return false;
  }
  return true;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><b>
    
    
    <span class="big1">&nbsp;印章制作</big> &nbsp;&nbsp;<font color='red'>制作公章请确认安装是否安装点聚控件！</font></b>
    </td>
  </tr>
</table>
<form name="form1">
<table class="TableBlock" border=0 align="center" width=80%>
  <tr>
    <td class="TableContent">印章ID</td>
    <td class="TableData">
    	<input name="SealID_prefix" id="SealID_prefix" class="BigStatic" readonly size=6 maxlength="6" value="">-<input name="SealID_mid" id="SealID_mid" onblur="getCounter();" class="BitInput" size=2 maxlength="2">-<input name="SealID_counter" id="SealID_counter" class="BigStatic" readonly size=6 maxlength="6">
    	<input type="hidden" name="SealID" id="SealID" class="BitInput" size=4 maxlength="4">	
    </td>
    <td align="center" class="TableData" width="250" rowspan="9" colspan=2>
       <OBJECT 
          id=DMakeSealV61 
          style="left: 0px; top: 0px" 
          classid="clsid:3F1A0364-AD32-4E2F-B550-14B878E2ECB1" 
          VIEWASTEXT 
          width="200"
          height="200"
          codebase='<%=contextPath%>/subsys/jtgwjh/sealmanage/sealmaker/MakeSealV6.ocx#version=1,0,3,4'>
  
  <PARAM NAME="_Version" VALUE="65536">
  <PARAM NAME="_ExtentX" VALUE="2646">
  <PARAM NAME="_ExtentY" VALUE="1323">
  <PARAM NAME="_StockProps" VALUE="0">
</OBJECT>
       <br><br>
       <div align="center"><input name="SelBMPFile" type="button" class="BigButtonC" id="SelBMPFile" value="选择BMP文件" onclick="return SelBMPFile_onclick()"></div>
    </td>
  </tr>
  <tr>
    <td  class="TableContent" width=80>印章名称</td>
    <td class="TableData">
    	<input type="text" name="SealName" id="SealName" class="BitInput" maxlength="32">	
    <br></td>
  </tr>
  <tr>
    <td  class="TableContent" nowrap>印章密码</td>
    <td class="TableData">
    	<input type="password" name="SealPwd" id="SealPwd" class="BitInput" maxlength="32">	
    <br></td>
  </tr>
  <tr>
    <td class="TableContent">印章单位</td>
    <td class="TableData" nowrap>
    
   <input type="text" name="deptName" id="deptName" class="BitInput" maxlength="" size="50" >	<br>
   <input type="hidden"  id="dept" name="dept" >
  
    </td>
  </tr>
  
    <tr>
    <td class="TableContent">是否启用</td>
    <td class="TableData">
   <select id="isFlag" name="isFlag" style="height:22px;FONT-SIZE: 12pt;">
   <option value="0">是</option>
      <option value="1">否</option>
        </select>
    </td>
  </tr>
  <tr>
    <td  class="TableContent">图片宽度</td>
    <td class="TableData"><input name="SealWidth" id="SealWidth" maxlength="32"> mm</td>
  </tr>
  <tr>
    <td class="TableContent">图片高度</td>
    <td class="TableData"><input name="SealHeight" id="SealHeight" maxlength="32"> mm</td>
  </tr>
  <tr>
    <td class="TableContent">印章大小</td>
    <td class="TableData"><select name="SealZoom" id="SealZoom">
        <option value="1" selected>图片于印章等大</option>
        <option value="2" >图片2倍于印章</option>
        <option value="3">图片3倍于印章</option>
      </select>图片大，则印章打印清晰</td>
  </tr>

  <tr>
    <td class="TableContent">图片深度</td>
    <td class="TableData">
    	<select name="SealBitCount" id="SealBitCount">
      <option value="1">单色显示</option>
      <option value="4" selected>16色显示</option>
      <option value="8">256色显示</option>
      <option value="24">24位真彩色</option>
      </select>
      色彩位数越高，打印越清晰</td>
	</tr>
  <tr>
    <td align="center" colspan=4 class="TableFooter">
    	<input type="hidden" name="SEAL_DATA" id="SEAL_DATA">
    	<input type="hidden" name="KeyID" id="KeyID">
    	<input class="BigButton" type="button" value="生成印章" LANGUAGE=javascript onclick="return SealWriteToKey_onclick('0')">&nbsp;
      </td>
  </tr>
</table>
<table style="display:none" id="seallisttable" class="TableList" border=0 align="center" width=80%>
	<tr class="TableHeader">
        <td>ID</td>
        <td>名称</td>
        <td>规格(mm)</td>
        <td>签名</td>
        <td>证书数量</td>
        <td>操作</td>
	</tr>
</table>
</form>
</body>
</html>