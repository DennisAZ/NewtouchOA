<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
String flag = request.getParameter("flag") == null ? "" : request.getParameter("flag") ;
String flagClose = request.getParameter("flagClose") == null ? "" : request.getParameter("flagClose") ;
String hostIp = request.getLocalAddr();
String serviceName = request.getServerName();
int port = request.getLocalPort();

String filePath = YHSysProps.getAttachPath() + "/jtgw/" ;
filePath = filePath.replace("\\" ,"\\\\");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>公文打印</title>
<style type="text/css">
</style>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/jhDocReceive.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/designer.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript">
//function document.onkeydown(){
	//if(event.keyCode==13){
		//event.keyCode=9;
	//}
//}
var mainDocId;
var mainDocName;
var attachPath = "<%=filePath%>";
var serviceName = '<%=serviceName %>';
var port = '<%=port %>';
var argObj = null;
var _userName = "sys_admin";
var seqId = '<%=seqId%>';
var charactersType = '';//大小写类型
function doOnload(printCountValue,printStratNoValue,printEndNoValue){
//  getModulField(printCountValue,printStratNoValue,printEndNoValue);
  setObjHeight();
}
/*
 * 初始化打印份数
 */
var printCountName = "printCount";
var printStratName = "printStratNo";
var printEndNoName = "printEndNo";
function getModulField(printCountValue,printStratNoValue,printEndNoValue){
  var obj = $("HWPostil1");
  var printCount = obj.GetValue(printCountName);//打印份数
  var printStratNo = obj.GetValue(printStratName);//开始编号
  var printEndNo = obj.GetValue(printEndNoName);//结束编号
  if(!printCount){

    var vRet = obj.InsertNote(printCountName,0,3,1000,1000,3000,1000);　
    
    obj.SetValue(printCountName,printCountValue);
    obj.SetValue(printCountName,":PROP:BORDER:0");//无边框
    obj.SetValue(printCountName,":PROP:FRONTCOLOR:-1");//背景透明
    obj.SetValue(printCountName, ":PROP::LABEL:3");//不可点击
    obj.SetValue(printCountName, ":PROP:FONTSIZE:1");//字体为1
    obj.SetValue(printCountName,":PROP:FRONTCOLOR:16777215");//败诉
  }
  if(!printStratNo){
    var vRet = obj.InsertNote(printStratName,0,3,4000,1000,3000,1000);
    obj.SetValue(printStratName,printStratNoValue)　;
    obj.SetValue(printStratName,":PROP:BORDER:0");//无边框
    obj.SetValue(printStratName,":PROP:FRONTCOLOR:-1");//背景透明
    obj.SetValue(printStratName, ":PROP::LABEL:3");//不可点击
    obj.SetValue(printStratName, ":PROP:FONTSIZE:1");//字体为1
    obj.SetValue(printStratName,":PROP:FRONTCOLOR:16777215");//败诉
  }
  if(!printEndNo){
    var vRet = obj.InsertNote(printEndNoName,0,3,7000,1000,3000,1000);
    obj.SetValue(printStratName,printEndNoValue)　;
    obj.SetValue(printEndNoName,":PROP:BORDER:0");//无边框
    obj.SetValue(printEndNoName,":PROP:FRONTCOLOR:-1");//背景透明
    obj.SetValue(printEndNoName, ":PROP::LABEL:3");//不可点击
    obj.SetValue(printEndNoName, ":PROP:FONTSIZE:1");//字体为1
    obj.SetValue(printEndNoName,":PROP:FRONTCOLOR:16777215");//败诉　
  }
  
  obj.Logout();
}
/*
 * 改变窗口大小的时候更改控件的大小
 */
function setObjHeight(){
  var height = ((document.documentElement || document.body).scrollHeight - 63) + 'px';
  $('HWPostil1').style.height = height ;
  //$("fieldList").style.height =((document.documentElement || document.body).scrollHeight - 63) + 'px';
}



/*
 * 把aip文件上传到服务器去
 */
function updateAipToService1() {
  var filePath = $("filePath").value ;
  var  attachmentName = "";

  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        var port = '<%=port%>';
        returnValue = webObj.HttpPost("http://<%=serviceName%>:" +port+ "/yh/subsys/jtgwjh/receiveDoc/saveFile/saveAip.jsp?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName));
        if("ok" == returnValue){
          //alert("保存成功！"); 
          var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendStampAct/mainStamps.act?seqId=<%=seqId%>";
          var rtJson = getJsonRs(url);
          if(rtJson.rtState == "0"){
            alert("保存成功！");
            $('sendButton').style.display = '';
            
          }
        }else if("failed" == returnValue){
          alert("上传失败！");
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
}

function save(){
  updateAipToService1();
}

function otherStamps(){
  if($('otherStamps').value){
	  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendStampAct/otherStamps.act?seqId=<%=seqId%>&otherStamps="+$('otherStamps').value;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert("协办盖章人员添加成功！");
	  }
  }
}

function pageClose(){
  if(!window.confirm(" 确认关闭页面吗？")) {
    return ;
  } 
  window.close();
}




/**
 * 盖章获取可以盖的公章
 */
 
function getSeal(){
   var url = "<%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/getPrivSealByDeptId.act"
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
     var data = rtJson.rtData;
     return data;
   }else{
     return ;
   }
 }
 
 /**
盖章 
 **/
 
function isStamp(){
  var oAIP = $("HWPostil1");
 	AIP_Login();
 	var AIP_FILE_TYPE = "";
 	if( AIP_FILE_TYPE != 1 ){
     oAIP.SetValue("RESET_PAGE_SEALINAREA", "0");
   }else{
     oAIP.SetValue("SET_PAGE_SEALINAREA", "0");
  }
 var prcs = getSeal();
 if(prcs.length > 0){
   var prc = prcs[0];
   if(prcs.length == 1 ){
     //oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/stampSeal.act?BASE64DATA=" + prc.sealData);
     //oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/subsys/jtgwjh/test.sel");
     oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/stampSeal.act?seqId=" + prc.seqId);
     oAIP.CurrAction = 2568;
   }else{
     //alert("http://localhost<%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/stampSeal.act?BASE64DATA=" + prc.sealData);
    // oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/stampSeal.act?seqId=" + prc.seqId);
    // oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/subsys/jtgwjh/test.sel");
     
     var URL = contextPath + "/core/funcs/system/sealmanage/readseal.jsp"; 
     var loc_x = screen.availWidth/2-200;
     var loc_y = screen.availHeight/2-90;
       //var kkcc=encodeURIComponent(URL);
       //alert(URL);
     window.open(URL,null,"height=400,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
     
   }
  // oAIP.SetValue("SET_PREVSEAL_PATH","http://<?=$_SERVER['HTTP_HOST']?>/inc/utility_cngc.php?action=getUserSeal&SEAL_ID=<?=$LOGIN_UID?>&SEAL_NAME=<?=urlencode($LOGIN_USER_NAME)?>");

 }else{
   alert("您没有盖章的权限，请与管理员联系！");
   return;
 }
}

 function Stamp(seqId){
   var oAIP = $("HWPostil1");
   oAIP.SetValue("SET_PREVSEAL_PATH","http://<%=serviceName%>:<%=port%><%=contextPath%>/yh/subsys/jtgwjh/task/act/YHJhTaskLogAct/stampSeal.act?seqId=" +seqId);
   oAIP.CurrAction = 2568;
 }

 function AIP_Login()
 {
   var oAIP = $("HWPostil1");
     if(!oAIP.IsLogin() || oAIP.GetCurrUserId=="sys_admin")
     {
         //var dtrer=oAIP.Login("HWSEALDEMO**", 4, 65535, "DEMO", "");
         var dtrer=oAIP.Login("abc",1,65535,"","");
         if(dtrer==-200){
 	        alert("未发现智能卡设备,您只有浏览权限!");
 	        return true;
         }
         else if(0!=dtrer)
         {
 	        alert("登录失败！");  	
         }
     }
 }

</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
//var content = loadAIPById(modulId);
function getDocById(seqId){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=<%=seqId%>&stamp=1";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data
  }else{
    return ;
  }
}
var prc = getDocById(seqId) ;

if (prc.seqId) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作

  var obj = $("HWPostil1");
  //obj.ShowDefMenu = false; //隐藏菜单
  //obj.ShowToolBar = false; //隐藏工具条

  obj.JSEnv = 1;
  obj.ShowScrollBarButton = 1;

  var attachmentId = prc.mainDocId;
  var attachmentName = prc.mainDocName;
  mainDocId = prc.mainDocId;
  mainDocName = prc.mainDocName;

 //var webPort = prc.port;//服务端口号
  var webPort  = '<%=port%>';

  filePath = prc.filePath;
  var attachmentIdArra = attachmentId.split("_");
 var attachmentIdDate = attachmentIdArra[0];
 var fileName =attachmentName;
// var serviceName = prc.serviceName ;
 var serviceName = '<%=serviceName%>';
 //var filePath = prc.filePath;
 var filePath = '<%=filePath%>';
 var attachmentIdDate = attachmentId.substr(0,4);
 var fileName = attachmentIdArra[1] + "_" + attachmentName;
 filePath = filePath + attachmentIdDate + "/" + fileName;
 
 //var vRet = obj.Login( _userName , 5, 32767, "", "");//编辑模式

// var t = "http://" + serviceName + ":" + webPort+  "<%=contextPath %>/getFile?uploadFileNameServer=" + filePath ;
 obj.LoadFile("http://" + serviceName + ":" + webPort+  "<%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePath) );
  
 //obj.SilentMode =1 ;//1：安静模式。安静模式下不显示下载提示对话框、文档转化提示对话框和部份提示信息
 // obj.SaveTo(localFilePath + "\\" + fileName ,"aip",0);

  //obj.LoadFileBase64(content);

 // obj.InTabSortMode  =1;  
 // obj.SilentMode =1  ;
  obj.AppendTipInfo = "aaaa";//显示属性

  obj.CurrPenColor = 255;
  //obj.CurrXYMode=0;//设置返回页面位置的类型1为像素

  obj.SetPageMode(8,222);
  $("filePath").value = filePath;
  var printCountValue = "";
  var printStratNoValue = "";
  var printEndNoValue = "";
  doOnload(printCountValue,printStratNoValue,printEndNoValue);
 
	var data4 = prc.data4;
	var otherStamps = '';
	var otherStampsDesc = '';
	for(var i = 0; i < data4.length; i++){
	  otherStamps += data4[i].user + ',';
	  otherStampsDesc += data4[i].userName + ',';
	}
	if(otherStamps.length > 1){
	  otherStamps = otherStamps.substring(0, otherStamps.length - 1);
	  otherStampsDesc = otherStampsDesc.substring(0, otherStampsDesc.length - 1);
	}
  $('otherStamps').value = otherStamps;
  $('otherStampsDesc').innerHTML = otherStampsDesc;
}

</SCRIPT>
</head>
<body>
<table class="" align="center" style="width:100%;height:100%;">
   <tr>
     <td class="TableData" align="center">
       <%if("1".equals(flagClose)){ %>
       <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/jtgwjh/sendDoc/modify.jsp?seqId=<%= seqId%>';"/>
       <%} 
       else if("2".equals(flagClose)){%>
       <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/jtgwjh/sendDoc/modify.jsp?seqId=<%= seqId%>';"/>
       <%} 
       else{%>
       <input type="button" value="关闭" class="BigButton" onclick="pageClose()"/>
       <%} %>
       
       <input type="button" value="盖章" class="BigButton" onclick="isStamp();"></input>&nbsp;&nbsp;
			 <input type="button" value="盖章确认" class="BigButton" onclick="save();"></input>&nbsp;&nbsp;
			 <input type="hidden" name="otherStamps" id="otherStamps" value=""/>
			 <textarea cols="40" name="otherStampsDesc" id="otherStampsDesc" rows="2" style="overflow-y:auto;display:none;" class="BigStatic" wrap="yes" readonly></textarea>
			 <%if("1".equals(flag)){ %>
			 <!-- input type="button" value="协办盖章" class="BigButton" onclick="selectUser(['otherStamps', 'otherStampsDesc']);otherStamps();"/>&nbsp;&nbsp; -->
			 <%} %>
			 <input type="button" id="sendButton" value="发送" class="BigButton" onclick="sendSingle(<%=seqId%>,1)" style="display:none;"/>
    </td>
  </tr>
  <tr style="width:100%;height:100%;">
    <td class="TableData"  style="width:100%;height:100%;">
      <OBJECT id=HWPostil1 style="WIDTH:100%;HEIGHT:550px;" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='/yh/subsys/jtgwjh/setting/aip/HWPostil.cab#version=3.0.9.4' ></OBJECT>
    </td>
  </tr>
</table>
<div id="11">
  <input type="hidden" name="filePath" id="filePath"></input>
</div>
<div id="AIPDIV"></div>
</body>
</html>