<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签章管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sealmanage/js/sealManageUtil.js"></script>
<script type="text/javascript">
/**
 * 盖章获取可以盖的公章
 */
 
function getSeal(){
   var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getPrivSealByDeptId.act";
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
     var data = rtJson.rtData;
     return data;
   }else{
     return ;
   }
 }
 

 function doInit(){
   var prcs = getSeal();
   prcs.each(function(prc, i) {
     var prc = prcs[i];
     var div = new Element('div',{"float":"left","style":"padding-left:30px;float:left;","align":"center"});

     var objStr = "<OBJECT"
       + " id=DMakeSealV61" + i  
       + " style='left: 0px; top: 0px'" 
      +" classid=\"clsid:3F1A0364-AD32-4E2F-B550-14B878E2ECB1\"" 
      +" VIEWASTEXT "
      +" width=200"
      +" height=\"200\""
       + "codebase='<%=contextPath%>/subsys/jtgwjh/sealmanage/sealmaker/MakeSealV6.ocx#version=1,0,2,8'>"
      +"<PARAM NAME=\"_Version\" VALUE=\"65536\">"
      +"<PARAM NAME=\"_ExtentX\" VALUE=\"2646\">"
     + "<PARAM NAME=\"_ExtentY\" VALUE=\"1323\">"
      +"<PARAM NAME=\"_StockProps\" VALUE=\"0\"></OBJECT>";
      
      div.insert(objStr);
      var button = new Element("input",{
              "type":"button"
              ,"value":"选择盖章"
              ,"class":"BigButton"
              
      });
      button.onclick = function(){
        window.opener.Stamp(prc.seqId,prc.sealId);
        window.close();
      }

  
     $("apply").insert(div);
     div.insert("<br>");
     div.insert(button);
     if(i%2 != 0 ){
       $("apply").insert("<div></div>");
      }
     var result = prc.sealData;
     var obj = document.getElementById("DMakeSealV61" + i);
     showSealInfo(obj,result)
   });
 }
 
 
 function showSealInfo(obj,result){
   if(!obj){
     alert("控件加载失败!");
    return false;
  }
  if(0 == obj.LoadData(result)){
    var vID = 0; 
    vID = obj.GetNextSeal(0);
    if(!vID){
     return true;
    }
    if(obj.SelectSeal(vID)) return false;
    var vSealID = obj.strSealID;
    var vSealName = obj.strSealName;
    var vSealWidth = obj.fSealWidthMM
    var vSealHeight = obj.fSealHeightMM;
    var vCertCtrlNum = 0;
    var vID = 0 ;
    var vSignInfo = "无签名" ;
    var vCertInfo = "" ;
    var vTempFilePath = "" ;
    while(vID = obj.GetNextCtrlCert(vID)){
     //obj.GetCtrlCert(vID,""); 
     //alert(0);
     vTempFilePath = obj.GetTempFilePath(); 
     if(0 == obj.GetCtrlCert(vID,vTempFilePath)){
      if(0 == obj.CertGetInfo(vTempFilePath,"CERTDATAFILE")){
        vCertInfo += "用户："+ obj.SubjectName+ "  序列号:" + obj.SerialNumber+"<br>";
      }
      obj.DelLocalFile(vTempFilePath);
     }
     vCertCtrlNum ++;
    }
    if(vCertCtrlNum>0)
      vCertInfo = "绑定的证书数量:"+vCertCtrlNum+"<br>"+vCertInfo;
    else
     vCertInfo = "无绑定证书";
    
    vTempFilePath = obj.GetSignCert(0); 
    if("" != vTempFilePath){
     if(0 == obj.CertGetInfo(vTempFilePath,"CERTDATAFILE") ){
       vSignInfo +="用户:" + obj.SubjectName+"  序列号:" + obj.SerialNumber+"<br>";
     }
     obj.DelLocalFile(vTempFilePath);
    }
  }
 }

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;印章查询</span>&nbsp;
    </td>
    
    <td>    <input class="BigButton" onclick="window.close()" type="button" value="关闭"/></td>
  </tr>
</table>

<div id="apply" class=""  align="">

  </div>

</body>
</html>