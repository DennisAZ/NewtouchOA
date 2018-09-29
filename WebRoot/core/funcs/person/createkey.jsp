<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  String USER_ID = request.getParameter("USER_ID");
  if (USER_ID == null) {
    USER_ID = "";
  }
%>
<head>
<title>初始化USB用户KEY</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
var deptId = "<%=deptId%>";
var USER_ID = "<%=USER_ID%>";
var KEY_USERINFO;
var KEY_SN;
var timeoutID;

function doInit(){
  GET_USERKEY();
}

function GetKey(va){
  var a = new VBArray(va);
  return a.toArray().toString();
}

function READ_KEYSN(){
  var theDevice=$("tdPass");
  var bOpened = OpenDevice(theDevice);
  if(!bOpened)return false;
  //读取设备序列号
  try{
    KEY_SN=theDevice.GetStrProperty(7, 0, 0);
  }
  catch(ex){
   theDevice.CloseDevice();
   alert("USB用户KEY初始化失败!");
   location = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/addUsbKeyLog.act";
   location="<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
 }
}
function GET_USERKEY(){
  READ_KEYSN();
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/updateUserKey.act";
  var rtJsons = getJsonRs(urls , "seqId=" + USER_ID +"&keySn="+KEY_SN);
  if(rtJsons.rtState == "0"){
    KEY_USERINFO = getUserInfo();
    CREAT_KEY();
  }else{
    alert("获取用户信息超时，请重新初始化"); 
  }

}

function getUserInfo(){
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getUserInformation.act";
  var rtJsons = getJsonRs(urls , "seqId=" + USER_ID);
  if(rtJsons.rtState == "0"){
    return rtJsons.rtData;
  }
}

function OpenDevice(theDevice){
  try{
    theDevice.GetLibVersion();
  }
  catch(ex){
    alert("您没有下载并正确安装USB用户KEY驱动程序");
    location="<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
  }
  try{
    theDevice.OpenDevice(1, "");
  }
  catch(ex){
    alert("您没有插人合法的USB用户KEY");
    location="<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
  }
  return true;
}
function CREAT_KEY(){
  var KEY_USERINFO_ARRY=KEY_USERINFO.split(",");
  var theDevice=$("tdPassSC");
   //打开设备
  var bOpened = OpenDevice(theDevice);
  if(!bOpened)
    return false;
  try{
      //写用户信息
    var USER_INFO=KEY_USERINFO_ARRY[0];
    var USER_CERTINFO=KEY_USERINFO_ARRY[1];
    var userInfoLength = USER_INFO.length;
    var sign=OPEN_FILE(3);
    if(sign==1)theDevice.DeleteFile(0,3);
    theDevice.CreateFile(0,3,userInfoLength,2,0,0,7,2);
    theDevice.write(0,0,0,USER_INFO,userInfoLength);//
    theDevice.CloseFile();
    var key1;
    var key2;
    key1 = GetKey(VBGetKey(0,USER_CERTINFO,theDevice));
    key2 = GetKey(VBGetKey(1,USER_CERTINFO,theDevice));

     //写个人私钥
    sign=OPEN_FILE(5);
    if(sign==1)theDevice.DeleteFile(0,5);
    theDevice.CreateFile(0,5,16,4,7,0,0,0);
    theDevice.write(1,0,0,key1,16);//
    theDevice.CloseFile();
    sign=OPEN_FILE(6);
    if(sign==1)theDevice.DeleteFile(0,6);
    theDevice.CreateFile(0,6,16,4,7,0,0,0);
    theDevice.write(1,0,0,key2,16);//
    theDevice.CloseFile();
  }
  catch(ex){
    theDevice.CloseDevice();
    alert("USB用户KEY初始化失败!请重新初始化KEY!\n"+ex.description);
    location="<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
    return false;
  }
  theDevice.CloseDevice();
  alert("USB用户KEY初始化成功!");
  location="<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
}

function OPEN_FILE(fileid){
  var theDevice=$("tdPass");
  var bOpened = OpenDevice(theDevice);
  if(!bOpened)return -1;
  //读取设备序列号
  try{
   theDevice.OpenFile (0,fileid);
   theDevice.CloseFile();
   return 1;
  }
  catch(ex){
   theDevice.CloseDevice();
   return 0;
 }
}
</script>
<script language="VBScript">
//用VB函数获取到Key1和Key2
function VBGetKey(WhichKey,CertKey,theDevice)
  On Error Resume Next
  dim key
  theDevice.Soft_MD5HMAC WhichKey,0,CertKey,key
  If Err Then
    MsgBox ("VBGetKey:No.1\nError#" & Hex(Err.number and &HFFFF) & " \nDescription:" & Err.description)
    ePass.CloseDevice
    Exit function
  End If
    VBGetKey = Array(key)
End function
</script>
</head>
<body topmargin="5" onload="doInit()">
<object id="tdPassSC" name="tdPassSC" CLASSID="clsid:C7672410-309E-4318-8B34-016EE77D6B58" CODEBASE='<%=contextPath%>/core/cntrls/tdPass.cab#Version=1,00,0000'
 BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0"></object>
<object id="tdPass" name="tdPass" CLASSID="clsid:0272DA76-96FB-449E-8298-178876E0EA89" CODEBASE='<%=contextPath%>/core/cntrls/tdPass.cab#Version=1,00,0000'
 BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0"></object>
<div class=big1>正在初始化USB用户KEY，请稍候...</div>
</body>
</html>