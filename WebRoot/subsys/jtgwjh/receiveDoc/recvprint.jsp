<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
String hostIp = request.getLocalAddr();
String serviceName = request.getServerName();
int port = request.getLocalPort();
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

<script type="text/javascript">
//function document.onkeydown(){
	//if(event.keyCode==13){
		//event.keyCode=9;
	//}
//}
var argObj = null;
var _userName = "sys_admin";
var seqId = '<%=seqId%>';
var charactersType = '';//大小写类型
function doOnload(printCountValue,printStratNoValue,printEndNoValue,printCountAll){
  getModulField(printCountValue,printStratNoValue,printEndNoValue,printCountAll);
  setObjHeight();
}
/*
 * 初始化打印份数
 */
var printCountName = "printCount";
var printStratName = "printStratNo";
var printEndNoName = "printEndNo";
function getModulField(printCountValue,printStratNoValue,printEndNoValue,printCountAll){
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
    obj.SetValue(printCountName, ":PROP:FONTSIZE:14");//字体为1
    obj.SetValue(printCountName,":PROP:FRONTCOLOR:16777215");//败诉
  }else{
     printCountValue =   obj.GetValueEx(printCountName,2,"",0,"");
  }
  /*
  *添加打印详情
  */
  $("printInfo").update("打印份数：" + (parseInt(printCountAll,10) - parseInt(printCountValue,10)) + " / " + printCountAll);
  if(!printStratNo){
    var vRet = obj.InsertNote(printStratName,0,3,4000,1000,3000,1000);
    obj.SetValue(printStratName,"")　;
    if(isNumber(printStratNoValue)){
      obj.SetValue(printStratName,printStratNoValue)　;
    }else{
      obj.SetValue(printStratName,"0")　;
    }
    obj.SetValue(printStratName,":PROP:BORDER:0");//无边框
    obj.SetValue(printStratName,":PROP:FRONTCOLOR:-1");//背景透明
    obj.SetValue(printStratName, ":PROP::LABEL:3");//不可点击
    obj.SetValue(printStratName, ":PROP:FONTSIZE:14");//字体为1
    obj.SetValue(printStratName,":PROP:FRONTCOLOR:16777215");//
  }
  if(!printEndNo){
    var vRet = obj.InsertNote(printEndNoName,0,3,7000,1000,3000,1000);
    obj.SetValue(printEndNoName,"")　;

    if(isNumber(printStratNoValue)){
      obj.SetValue(printEndNoName,printEndNoValue)　;
    }else{
      obj.SetValue(printEndNoName,"0")　;
    }
    obj.SetValue(printEndNoName,":PROP:BORDER:0");//无边框
    obj.SetValue(printEndNoName,":PROP:FRONTCOLOR:-1");//背景透明
    obj.SetValue(printEndNoName, ":PROP::LABEL:3");//不可点击
    obj.SetValue(printEndNoName, ":PROP:FONTSIZE:14");//字体为1
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
//全打与套打

function doInit(type){
  chekePrintCount(type);
  
}
var printCountValue = 0;
/*
 * 检查打印份数
 */
function chekePrintCount(index){
  type = 0;
  var obj = $("HWPostil1");
  var printCount = obj.GetValue(printCountName);//打印份数
  var value = obj.GetValueEx(printCountName,2,"",0,"");
  printCountValue = value;
  if(value<=0){
    alert("当前文件打印份数已全部打印！");
  }else{
    var obj = $("HWPostil1");
    if(index=='1'){
      obj.ForceSignType2 = 1;//设置套打
    }else {
      obj.ForceSignType2 = 0;
    }
    var ratInt = obj.PrintDoc(1,1);//设置不打印节点
    
    if(ratInt > 0){
      alert("aaa")
  
      //updateAipToService();
    }
  }
}


/*
 * 把aip文件上传到服务器去
 */
function updateAipToService() {
  var filePath = $("filePath").value ;
  var  attachmentName = "";

  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        var port = '<%=port%>';
        returnValue = webObj.HttpPost("http://<%=serviceName%>:" +port+ "/yh/subsys/jtgwjh/receiveDoc/saveFile/saveAip.jsp?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName));
        if("ok" == returnValue){
          //alert("上传成功！"); 
        }else if("failed" == returnValue){
          alert("上传失败！");
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
}

</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
//var content = loadAIPById(modulId);
var prc = loadMinAIP(seqId) ;
if (prc.seqId) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作
  $("seqId").value = seqId;
  var obj = $("HWPostil1");
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条

  obj.JSEnv = 1;
  obj.ShowScrollBarButton = 1;

  var attachmentId = prc.mainDocId;
  var attachmentName = prc.mainDocName;
  var printCountValue = prc.printCount;//数据库打印份数
  var printStratNoValue = prc.printNoStart;//数据打印开始编号
  var printEndNoValue = prc.printNoEnd;//数据库打印结束编号

  var webPort = prc.port;//服务端口号
  filePath = prc.filePath;
  var attachmentIdArra = attachmentId.split("_");
 var attachmentIdDate = attachmentIdArra[0];
 var fileName =attachmentName;
 var serviceName = prc.serviceName ;
 var filePath = prc.filePath;
 var printCountAll = prc.printCount;
 var attachmentIdDate = attachmentId.substr(0,4);
 var fileName = attachmentIdArra[1] + "_" + attachmentName;
 filePath = filePath + attachmentIdDate + "/" + fileName;
 
 var vRet = obj.Login( _userName , 5, 32767, "", "");//编辑模式
 var obj = $("HWPostil1");
// var t = "http://" + serviceName + ":" + webPort+  "<%=contextPath %>/getFile?uploadFileNameServer=" + filePath ;
 obj.LoadFile("http://" + serviceName + ":" + webPort+  "<%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePath) );
   // obj.SaveTo(localFilePath + "\\" + fileName ,"aip",0);

  //obj.LoadFileBase64(content);

 // obj.InTabSortMode  =1;  
 // obj.SilentMode =1  ;
  obj.AppendTipInfo = "aaaa";//显示属性

  obj.CurrPenColor = 255;
  //obj.CurrXYMode=0;//设置返回页面位置的类型1为像素

  obj.SetPageMode(8,222);
  $("filePath").value = filePath;

   
  // var tt1 = obj.ProtectDoc(3,"111",1);//控制打印，设置密码
  
 // var tt1 = obj.ProtectDoc(3,"",0);//控制打印，设置密码
  // var tt  =  obj. ProtectObject("",0,16,'','11111');
  // alert(tt1  +":"+ tt);
  
 doOnload(printCountValue,printStratNoValue,printEndNoValue,printCountAll);
}

</SCRIPT>


<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=JSNotifyBeforeAction(lActionType,lType,strName,strValue,plContinue);>

JSNotifyBeforeAction(lActionType,lType,strName,strValue,1);
</SCRIPT>


</head>
<body>

<table class="" align="center" style="width:100%;height:100%;">
     <tr>
      <td class="TableData" align="center">
<input type="button" value="打印" class="BigButton" onclick="doInit('0');"></input>&nbsp;
<input type="hidden" value="套打" class="BigButton" onclick="doInit('1');"></input>&nbsp;


&nbsp;&nbsp;<span id="printInfo"></span>


<input type="hidden" value="" id="seqId" name="seqId"></input>

<input type="hidden" value="" id="filePath" name="filePath"></input>	    </td>

    </tr>
    <tr  style="width:100%;height:100%;">
        <td class="TableData"  style="width:100%;height:100%;">


<OBJECT id=HWPostil1  style="WIDTH:100%;HEIGHT:550px;"  classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/subsys/jtgwjh/setting/aip/HWPostil.cab#version=3.0.9.4' >
    </td>
    </tr>
    
    
</table>
<div id="11">
<input type="hidden" name="seqId" id="seqId"></input>
  <input type="hidden" name="filePath" id="filePath"></input>
</div>



</body>
</html>