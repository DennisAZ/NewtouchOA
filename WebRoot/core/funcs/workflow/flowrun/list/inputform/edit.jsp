<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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

<link rel="stylesheet" href ="<%=cssPath %>/workflow.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath %>/root.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
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
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/dateUtil.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/praserUtil.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
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
<form style="padding-bottom:80px" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHFormEditAct/uploadFile.act" name="workFlowForm" target="returnPage" id="workFlowForm"  method="post" enctype="multipart/form-data">
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
<form style="padding-bottom:10px" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHAttachmentAct/uploadFile.act" name="formFile" target="returnPage" id="formFile"  method="post" enctype="multipart/form-data">
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
    	  var URL = "http://"+ host +"/yh/core/funcs/workflow/act/YHFlowFormViewAct/getSeal.act?id=" + seal_id; 
    	  DWebSignSeal.AddSeal(URL, item+"_seal");
      }
    }
    DWebSignSeal.SetSealSignData(item+"_seal",str); 
    DWebSignSeal.SetMenuItem(item+"_seal",261);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
function handWrite(item , color) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    if(DWebSignSeal.FindSeal(item+"_hw",2)!="") {
      alert("您已经签章，请先删除已有签章！");
      return;
    }
    var str=SetStore(item);
    if(typeof(color) == "string" && color)
    {
      fontColor = parseInt(color);
    }
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    DWebSignSeal.HandWrite(0,fontColor,item+"_hw");
  
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