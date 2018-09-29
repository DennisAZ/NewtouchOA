<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String dayTime = sf.format(new Date());

String serviceName = request.getServerName();
int port = request.getLocalPort();
String filePath = YHSysProps.getAttachPath() + "/docReceive" ;
filePath = filePath.replace("\\" ,"\\\\");

String isConnection = request.getParameter("isConnection");
%>
<html>

<head>
<title>处理公文</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/jhDocReceive.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/designer.js"></script>
<script type="text/javascript" >
var _userName = "sys_admin";
var isConnection = '<%=isConnection %>';
function checkForm(){
  if($("sendDate").value != '' && !isValidDateStr($("sendDate").value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    document.getElementById("sendDate").focus();
    document.getElementById("sendDate").select();
    return false;
   }
  if($("sendDate2").value != '' && !isValidDateStr($("sendDate2").value)){
    alert("结束日期格式不对,应形如 2010-02-01");
    document.getElementById("sendDate2").focus();
    document.getElementById("sendDate2").select();
    return false;
  }
  return true;
}
var pageMgr = null;
var cfgs = null;

function selectGroupManage(){
  pYxTotal = 0;
  pAllTotal = 0;
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/queryGroupManage.act?status=1";
   cfgs = {
    dataAction: url,
    container: "projectDiv",
    paramFunc: getParam,
    afterShow: getTotal,
    colums: [
       {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"3%",render:toCheck},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"docTitle", text:"文件标题", width: "20%",align:"left",render:toDocTitleInfo},
       {type:"text", name:"docCode", text:"文号", width: "6%",align:"center"},
       {type:"text", name:"docType", text:"文件类型", width: "5%",align:"center",render:toType},
       {type:"text", name:"sendDeptName", text:"发送单位", width: "10%",align:"center"},
       {type:"text", name:"sendDatetime", text:"发送时间", width: "10%",align:"center",render:toDate},
       {type:"hidden", name:"returnReason", text:"退回原因", width: "8%",align:"center"},
       {type:"hidden", name:"mainDocId", text:"正文Id", width: "8%",align:"center"},
       {type:"hidden", name:"mainDocName", text:"正文名称", width: "8%",align:"center"},
     {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"10%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total>0){

  }else{
    $("projectDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function getGroup(){
  if(checkForm()){
    if(!pageMgr){
      pageMgr = new YHJsPage(cfgs);
      pageMgr.show();
    }else{
      pageMgr.search();
    }
    var total = pageMgr.pageInfo.totalRecord;
    if(total>0){
      $("projectDiv").style.display = "";
      $('returnNull').style.display = "none";
     }else{
       $("projectDiv").style.display = "none";
       var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
           + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
           );
       $('returnNull').style.display = "";
       $('returnNull').update(table); 
     }
  }
}
function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}


function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var mainDocId = this.getCellData(recordIndex,"mainDocId");
  var mainDocName = this.getCellData(recordIndex,"mainDocName");
  var printStr = "";
  if(mainDocId !="" && mainDocName != "" ){

    //printStr = "<a href='javascript:toPrint("+seqId+");'>打印</a>&nbsp;";
  }
  
  var returnOA = "";
  if(isConnection == "1"){
    returnOA = "<a href='javascript:toOA("+seqId+");' title='流转至内部公文处理系统'>内部流转</a>&nbsp;";
  }
  return "<a href=\"javascript:print("+seqId+");\" >转发</a>&nbsp;"
         + printStr + returnOA ;
        //  +"<a href=\"javascript:update("+seqId+");\" >编辑</a>&nbsp";
          
          
}
function print(seqId){
  var URL = "<%=contextPath%>/subsys/jtgwjh/receiveDoc/manage/reciveToSend.jsp?seqId="+seqId;
  if(parent){
    parent.location.href = URL;
  }
  else{
	  window.location.href = URL;
  }
}
function update(seqId){
  var URL = "<%=contextPath%>/subsys/jtgwjh/receiveDoc/manage/edit.jsp?seqId="+seqId;
  window.location.href = URL;
}

function toOA(seqId){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/util/YHNetFileAct/uploadFile.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    //alert(json.rtMsrg); 
    alert("内转流转失败，可能网络繁忙，请稍后重试！");
    return ; 
  } else{
    alert("内部流转成功！");
    var prc = json.rtData;
    var returnURL = prc.returnURL;
    window.parent.location.href = returnURL;
  }
}
/**
 * 回退
 */
 var returnReason = null;
function ReTurnAll(){
	var idStrs = get_checked().trim(); 

  if(!idStrs) { 
    alert("请至少选择其中一个。"); 
   return; 
  } 
	if(idStrs){
		idStrs = idStrs.substr(0,idStrs.length -1);
	}
  var URL= contextPath + "/subsys/jtgwjh/receiveDoc/manage/returnReason.jsp?seqIds=" + idStrs;
  newWindow(URL,420,140,"retrunReason");

}



/**
 * 文件转存
 */
function FileChangeSave(){
	var idStrs = get_checked().trim(); 

  if(!idStrs) { 
    alert("请至少选择其中一条记录。"); 
   return; 
  } 
	if(idStrs){
		idStrs = idStrs.substr(0,idStrs.length -1);
	}
	var 	msg = "确定要归档所选的记录吗？";
  if(window.confirm(msg)) {
    //alert("id>>>"+attachId +"  attachName>>"+attachName + "  moudle>>"+moudle);
  //  var URL = contextPath + "/subsys/jtgwjh/receiveDoc/js/index.jsp?attachId=" + attachId + "&attachName=" + encodeURIComponent(attachName) +"&module=" + moudle;
   
  var URL = contextPath + "/subsys/jtgwjh/receiveDoc/saveFile/index.jsp?docReceiveSeqIds=" + idStrs + "&module=docReceive"; 
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
    //var kkcc=encodeURIComponent(URL);
    //alert(URL);
    window.open(URL,null,"height=250,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
  }

 }



/**
 * 脱密下载
 */
function TuoMIload(){
  //加载Object
  loadAIPObject($("objectAIP"),0,0);
  var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("请至少选择其中一条记录。"); 
   return; 
  } 
	if(idStrs){
		idStrs = idStrs.substr(0,idStrs.length -1);
	}
	
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/selectByIdsMian.act?seqIds=" + idStrs;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var unloadPath = json.rtMsrg;//下载路径
  var obj = $("HWPostil1");
  obj.SilentMode =1 ;//1：安静模式。安静模式下不显示下载提示对话框、文档转化提示对话框和部份提示信息
  var prcs =  json.rtData;
  var type = "0"
  for(var i =0 ;i<prcs.length ; i++){
    var prc = prcs[i] ;
    if(prcs.length -1 == i){
      type =1;
    }
    var attachmentId = prc.mainDocId;
    var attachmentName = prc.mainDocName;
    var attachmentIdArra = attachmentId.split("_");
    var attachmentIdDate = attachmentIdArra[0];
    var fileName =attachmentName;
    var serviceName = prc.serviceName ;
    var filePath = "<%=filePath%>";
    var attachmentIdDate = attachmentId.substr(0,4);
    var fileName = attachmentIdArra[1] + "_" + attachmentName;
    filePath = filePath +"/" +attachmentIdDate + "/" + fileName;
    obj.LoadFile("http://<%=serviceName%>:<%=port%><%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePath) );
    //文件变灰
    obj.grayData(31);
    //去掉章
    getAllUserNoteInfo(obj);
  
    updateAipToService(unloadPath,type);
    
    // 记录安全日志
    addSelLog(prc.seqId,"2");
    obj.CloseDoc(0);
  }

}

/*
 * 把aip文件上传到服务器去
 */
function updateAipToService(unloadPath,type) {
  var filePath = "qq" ;
  var  attachmentName = "";

  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        var port = '<%=port%>';
        returnValue = webObj.HttpPost("http://<%=serviceName%>:" +port+ "/yh/yh/subsys/jtgwjh/docReceive/logic/YHAipToJNI/saveAIP.act?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName) + "&unloadPath=" + encodeURIComponent(unloadPath) + "&type=" + type);
        if( returnValue == "succeed"&& type == '1'){
         // alert();
        $("unloadPath").value = unloadPath;
          // window.location.href = <%=contextPath%>/yh/subsys/jtgwjh/docReceive/logic/YHAipToJNI/uloadFile.act?unloadPath=" + encodeURIComponent(unloadPath);
          //alert("上传成功！"); 
          $("btnFormFile").click();
        }else if("failed" == returnValue){
          alert("上传失败！");
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
}

function doOnload(){
  if(checkForm()){
    selectGroupManage();
  }
  //时间
  var parameters = {
      inputId:'sendDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'sendDate2',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  
}
function getTotal(){
  var table = pageMgr.getDataTableDom();
  insertTr(table);
}
function insertTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行


  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列


  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列


  mynewcell.colSpan="6";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label> &nbsp;&nbsp;"
    +"<input type='button' value='归档' title='将选中的公文转存到公共文件柜并存档' class='BigButton' onClick='javascript:FileChangeSave();;'>&nbsp;"
  +"<input type='button' value='下载正文' title='转化下载' class='BigButton' onClick='javascript:TuoMIload();;'>&nbsp;";
 // +"<input type='button' value='归档' title='归档' class='BigButton' onClick='javascript:updateHandStatus();'>&nbsp;";
  //+"<input type='button' value='退回' title='退回' class='BigButton' onClick='javascript:ReTurnAll();;'>&nbsp;";

}


/*
 * 导出法国名单表

 */
function updateHandStatus(){
  var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("要归档记录，请至少选择其中一个。"); 
   return; 
  } 
  if(idStrs){
    idStrs = idStrs.substr(0,idStrs.length -1);
  }
  if(window.confirm("确认要归档所选记录")){ 
    var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/updateHandStatus.act?seqIds=" + idStrs + "&handstatus=1" ;
    var json = getJsonRs(url);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    alert("归档成功!");
    window.location.reload();
  }
}

</script> 
</head>
<body class="" topmargin="5" onload="doOnload();">
<form action="" id="form1" name="form1">
<table border="0" width="90%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif"
      align="middle">&nbsp;<span class="big3">已签收文件</span></td>
  </tr>
</table>
<br>
<table class="TableBlock" align="center" >
<tr>
          <td nowrap class="TableContent">文件标题： </td>
      <td class="TableData">
        <input type="text" name="docTitle" id="docTitle" value="" size="25">
 
      </td>
         <td nowrap class="TableContent">文号： </td>
      <td class="TableData">
        <input type="text" name="docNo" id="docNo" value="" size="8">
 
      </td>
   
     <td nowrap class="TableContent">发送日期： </td>
    <td nowrap class="TableData" >
        <input type="text" name="sendDate" id="sendDate" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
       <input type="text" name="sendDate2" id="sendDate2" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
        
        
    </td>
   </tr>
   <tr class="TableControl">
   	<td colspan="6" align="center" >  &nbsp;   &nbsp;  &nbsp;
   		<input type="button"  value="查询" class="BigButton" onClick="getGroup();"> &nbsp;  &nbsp; &nbsp;
   		<input type="button"  value="刷新" class="BigButton" onClick="location.reload();">
   	</td>
   </tr>
 </table>
 </form>
 <br>
<div id="projectDiv"></div>
<br>
<div id="returnNull"></div>
     <div id="objectAIP"></div>
     
     
   <form id="formFile" action="<%=contextPath%>/yh/subsys/jtgwjh/docReceive/logic/YHAipToJNI/uloadFile.act" method="post" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
   <input type="hidden" id="unloadPath" name="unloadPath" value="">
  </form>
  <iframe width="0" height="0" name="commintFrame" id="commintFrame"></iframe>
     
</body>
 
</thml>