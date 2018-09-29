<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = loginUser.getSeqId();
String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作办理</title>

<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath %>/root.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<style>
input.BigStatic1,select.BigStatic1,textarea.BigStatic1{ border: 1px solid #C0BBB4; background: #E6E6E6 top left repeat-x; COLOR: #000066;  FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal;  LINE-HEIGHT: normal}
.AUTO{ border: 1px solid #C0BBB4; background: #F9FBD5 top left repeat-x; color:blue; FONT-STYLE: normal; FONT-VARIANT: normal;  LINE-HEIGHT: normal}
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:10px}
.websign{display:inline;margin:0;padding:0;}

.navTab{list-style-type:none;margin:0px;padding-left:5px;height:33px;float:left;}
.navTab li{float:left;cursor:pointer;font-size:14px;text-align:center;font-weight:bold;width:63px;padding-top:5px;*padding-top:8px!important;}

.navTab_On{background:url(bg.gif) repeat-x 0px -33px; display:block;float:left;color:#333;height:33px;}
#form_title{background:url(bg.gif) repeat-x 0px 0px;height:24px;padding:0px;margin:0px;color:#fff;}
#info_title {float:left;font-size:14px;font-weight:bold;margin-top:3px;line-height:21px; vertical-align:middle;text-align:center;}
#form_control {height:30px;padding-top:3px;}
input.BigInput,textarea.BigInput {font-size: 10pt;padding: 1px 5px;  border: 1px solid #C0BBB4; background: white url('bg_input_text.png') top left repeat-x; background-position:0 -1px;}
input.BigInput:hover,textarea.BigInput:hover {font-size: 10pt;border: 1px solid #99CC00; background: white url('bg_input_text_hover.png') top left repeat-x;}

input.SmallInput,textarea.SmallInput {font-size: 9pt;padding: 1px 5px;  border: 1px solid #C0BBB4; background: white url('bg_input_text.png') top left repeat-x; background-position:0 -1px;}
input.SmallInput:hover,textarea.SmallInput:hover {font-size: 9pt;border: 1px solid #99CC00; background: white url('bg_input_text_hover.png') top left repeat-x;}

input.BigInputMoney{COLOR:#006; BACKGROUND: #F8F8F8; text-align: RIGHT; border:solid 1px black; BORDER-BOTTOM:1px double; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 22px; LINE-HEIGHT: normal}

input.BigStatic,textarea.BigStatic {font-size: 10pt;padding: 1px 5px;  border: 1px solid #C0BBB4; background: #E0E0E0;}

input.SmallStatic,textarea.SmallStatic {font-size: 9pt;padding: 1px 5px;  border: 1px solid #C0BBB4; background: #E0E0E0;}

select.BigSelect  { COLOR: #000066;  border: 1px solid #C0BBB4; background: white url('bg_input_text.png') top left repeat-x; BORDER-BOTTOM:1px double; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 22px; LINE-HEIGHT: normal}
select.BigSelect:hover  { COLOR: #000066;  border: 1px solid #C0BBB4; background: white url('bg_input_text_hover.png') top left repeat-x; BORDER-BOTTOM:1px double; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 22px; LINE-HEIGHT: normal}

select.SmallSelect{ COLOR: #000066;  border: 1px solid #C0BBB4; background: white url('bg_input_text.png') top left repeat-x; BORDER-BOTTOM:1px double; FONT-SIZE: 9pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 18px; LINE-HEIGHT: normal}
select.SmallSelect:hover{ COLOR: #000066;  border: 1px solid #C0BBB4; background: white url('bg_input_text_hover.png') top left repeat-x; BORDER-BOTTOM:1px double; FONT-SIZE: 9pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 18px; LINE-HEIGHT: normal}

select.BigStatic  { COLOR: #000066; BACKGROUND: #E0E0E0; border:solid 1px black; BORDER-BOTTOM:1px double; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 22px; LINE-HEIGHT: normal}
select.SmallStatic{ COLOR: #000066; BACKGROUND: #E0E0E0; border:solid 1px black; FONT-SIZE: 9pt; FONT-STYLE: normal; FONT-VARIANT: normal; FONT-WEIGHT: normal; HEIGHT: 18px; LINE-HEIGHT: normal}

</style>
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="js/tab.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%><%=moduleContextPath %>/flowform/util/dateUtil.js" ></script>
<script type="text/Javascript" src="<%=contextPath%><%=moduleContextPath %>/flowform/util/praserUtil.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="<%=YHSysProps.getLimitUploadFiles()%>";
var oa_upload_limit="<%=YHSysProps.getLimitUploadFiles()%>";
var attachUrl = "core/funcs/workflow/flowrun/workFlow/";//附件路径
var swfupload;
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var userId = '<%=userId%>';
var userName = '<%=loginUser.getUserName()%>'
var host = "<%=host%>";

</script>
</head>
<body onload="doInit()"   style="background:#FFF">
<form style="padding-bottom:80px" action="<%=contextPath %><%=moduleSrcPath %>/act/YHFormEditAct/uploadFile.act" name="workFlowForm" target="returnPage" id="workFlowForm"  method="post" enctype="multipart/form-data">
<div id="workHandleDiv"></div>
<div style="margin-top:42px;"></div>
<div id="formDiv">
<div id="noFormData" align=center style='display:none'>
<table class="MessageBox" width="300" >
    <tbody>
        <tr>
            <td class="msg info">表单内容为空！</td>
        </tr>
    </tbody>
</table>
</div>
</div>

<div id="attachDiv"  style="margin-top:20px" align=center>
<div id="hasData">
<table width="90%" cellspacing="0" cellpadding="3" class="TableList">
    <tbody><tr class="TableHeader">
      <td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> 公共附件区</div></td>
    </tr>
<tr class="TableData" id="noAttachments"><td align="left">无附件</td></tr>
<tbody id="attachmentsList"></tbody>
<tbody id="attachmentTbody">
<tr height="25" id="">
      <td class="TableData" align=left>
         新建附件：
         <input type="radio" id="NEW_TYPE1" name="DOC_TYPE" onclick="$('newType').value='doc'"/><label for="NEW_TYPE1">Word文档</label>
         <input type="radio" id="NEW_TYPE2" name="DOC_TYPE" onclick="$('newType').value='xls'"/><label for="NEW_TYPE2">Excel文档</label>
         <input type="radio" id="NEW_TYPE3" name="DOC_TYPE" onclick="$('newType').value='ppt'"/><label for="NEW_TYPE3">PPT文档</label>  
         <b>附件名：</b><input type="text" value="新建文档" class="SmallInput" size="20" name="newName" id="newName"/>
         <input type="button" onclick="newAttach();" value="新建附件" class="SmallButtonW"/>
         <input type="hidden" name="newType" id="newType" value=""/>
      </td>
    </tr>

<tr>
   <td class="TableContent"   align=left>
<script>ShowAddFile();</script> 
<script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:uploadAttach();">上传附件</a>'</script>
</td>
 </tr>
 </tbody>
</table>
</div>
<div id="noData" style='display:none'>
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="noAttachMsg" class="msg info">无公共附件,并且您无权上传附件!</td>
        </tr>
    </tbody>
</table></div>
</div>

<input type="hidden" id="flowId" name="flowId" value="<%=flowId %>"/>
<input type="hidden" id="runId" name="runId" value="<%=runId %>"/>
</form>
<div class="clearfix" id="wpiroot">
<div class="m-chat">
<div class="chatnote"/>
<div class="operateLeft" style="font-weight: bold;">
<span style="vertical-align: middle;"><img src="<%=imgPath %>/green_arrow.gif"/></span>&nbsp;相关操作</div>
<div class="operateRight">&nbsp;&nbsp;&nbsp;&nbsp;
<span>
<input type=button class="SmallButtonW" onclick="saveForm()" value="保存"> 
<input type=button onclick="window.close()"  class="SmallButtonW"  value="关闭"> 
</span>
</div>
</div>
</div></div>
<iframe id="returnPage" name="returnPage" style="display:none"></iframe>
<form style="padding-bottom:10px" action="<%=contextPath %><%=moduleSrcPath %>/act/YHAttachmentAct/uploadFile.act" name="formFile" target="returnPage" id="formFile"  method="post" enctype="multipart/form-data">
<input type="hidden"  name="flowId" value="<%=flowId %>"/>
<input type="hidden"  name="runId" value="<%=runId %>"/>
<input type="hidden" name="isEdit" value="true"/>
 <input type="hidden"  name="isFeedAttach" value="false"/>
</form>


<%@ include file="/core/funcs/workflow/websign/ver.jsp" %>
<script>
var sign_str = "";
var sign_check={};
var sign_arr = [];
var sealForm = 1; 
function addSeal(item,seal_id) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    if(DWebSignSeal.FindSeal(item+"_seal",2)!="") {
  	alert("您已经签章，请先删除已有签章！");
      return;
    }
    var str = SetStore(item);
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    if (sealForm == 1 ) {
      DWebSignSeal.addSeal("", item+"_seal");
    } else {
      
      if(typeof seal_id == "undefined") {
        show_seal(item,"addSeal");
      } else {
    	  var URL = "http://"+ host +"<%=moduleSrcPath %>/act/YHFlowFormViewAct/getSeal.act?id=" + seal_id; 
    	  DWebSignSeal.AddSeal(URL, item+"_seal");
      }
    }
    DWebSignSeal.SetSealSignData(item+"_seal",str); 
    DWebSignSeal.SetMenuItem(item+"_seal",261);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
function handWrite(item) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    if(DWebSignSeal.FindSeal(item+"_hw",2)!="") {
      alert("您已经签章，请先删除已有签章！");
      return;
    }
    var str=SetStore(item);
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    DWebSignSeal.HandWrite(0,255,item+"_hw");
  
    DWebSignSeal.SetSealSignData(item+"_hw",str);
    DWebSignSeal.SetMenuItem(item+"_hw",261);
  } catch (err) {
   // alert('websign控件没有加载成功！')
  }
}


function GetDataStr(item) {
  if(typeof item == 'undefined') {
    return; 
  }
  var str="";
  var separator = "::";  // 分隔符
  var TO_VAL = sign_check[item];
  if(TO_VAL!="") {
    var item_array = TO_VAL.split(",");
    for (i=0; i < item_array.length; i++) {
      var MyValue="";
      if (item_array[i] && $(item_array[i])){
        var obj =  $(item_array[i]);
        if(obj.type=="checkbox"){
          if(obj.checked==true) {
            MyValue="on";
          } else {
            MyValue="";
          }
        } else {
          MyValue=obj.value;
        }
        str += obj.name + "separator" + MyValue + "\n";
      }
    }
  }
  
  return str;
}

function SetStore(item)
{ 
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
	var str= GetDataStr(item);
    DWebSignSeal.SetSignData("-");
    DWebSignSeal.SetSignData("+DATA:" + str);
	return str;
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}

function LoadSignData() {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    for(var i=0;i<sign_arr.length;i++) {
      if(sign_arr[i] && $(sign_arr[i])){
        DWebSignSeal.SetStoreData($(sign_arr[i]).value);
      }
    }
    DWebSignSeal.ShowWebSeals();
   
    var str= "";
    var strObjectName ;
    strObjectName = DWebSignSeal.FindSeal("",0);
    while(strObjectName  != "")
    {
      if(strObjectName.indexOf("_hw")>0)
         item_arr = strObjectName.split("_hw");
      else if(strObjectName.indexOf("_seal")>0)
         item_arr = strObjectName.split("_seal");
      if(item_arr)
      {
        str = GetDataStr(item_arr[0]);
        DWebSignSeal.SetSealSignData(strObjectName,str);
        var tmp = "4";
        if (opFlag == 1) {
          tmp = '261';
        }
        DWebSignSeal.SetMenuItem(strObjectName , tmp);
      }
      strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
    }
    DWebSignSeal.SetCurrUser("<%=loginUser.getSeqId()%>[<%=loginUser.getUserName()%>]");
    //加载完成标识
    SignLoadFlag = true;
  } catch (err) {
   // alert('websign控件没有加载成功！')
  }
}

function WebSign_Submit()
{ 
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    var sign_val;
    for(var i=0;i<sign_arr.length;i++)
    {
      if(sign_arr[i]!="")
      {
        var oldstr="";
        var objName_hw = DWebSignSeal.FindSeal(sign_arr[i]+"_hw",2);
        var objName_seal = DWebSignSeal.FindSeal(sign_arr[i]+"_seal",2);
        //保存兼容老数据，老数据存在本次可写的第一个字段里。
        if(i==0)
        {
          var strObjectName = DWebSignSeal.FindSeal("",0);
          while(strObjectName !="")
          {
            if(strObjectName.indexOf("_hw")<0 && strObjectName.indexOf("_seal")<0 && strObjectName.indexOf("SIGN_INFO")<0)
               oldstr += strObjectName+";";
            strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
          }
  
          if(objName_hw=="" && objName_seal=="" && oldstr=="")
            sign_val="";
          else
            sign_val=DWebSignSeal.GetStoreDataEx(oldstr+sign_arr[i]+"_hw"+";"+sign_arr[i]+"_seal");
        }
        else
        {
          if(objName_hw=="" && objName_seal=="")
            sign_val="";
          else
            sign_val=DWebSignSeal.GetStoreDataEx(sign_arr[i]+"_hw"+";"+sign_arr[i]+"_seal");
        }
        $(sign_arr[i]).value=sign_val ;
      }
    }
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
</script>

</body>
</html>