<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="oa.spring.util.ContractCont"%>
<%
	String drawingId=request.getParameter("drawingId")==null?"":request.getParameter("drawingId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑生产图纸信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/erp/produce/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/erp/produce/attachMenu.js"></script>

<script type="text/javascript">
var drawingId="<%=drawingId%>";

   function doInit(){
	   initSwfUpload();
       getDrawingType(); //获取图纸类别
       showAttact();
   }
	function showAttact(){
		var url = "<%=contextPath %>/SpringR/produce/getDrawing?drawingId="+drawingId;
		jQuery.ajax({
			   type : 'POST',
			   url : url,
			   async : false,
			   success : function(jsonData){ 
					var data = JSON.stringify(jsonData);
					var jsonObj = eval("(" + data + ")");
					
					jQuery('#code').val(jsonObj.code);
					jQuery('#proId').val(jsonObj.pro_id);
					jQuery('#drawingType').val(jsonObj.dt_id);
					jQuery('#drawing_version').val(jsonObj.drawing_version);
					jQuery('#is_using').val(jsonObj.is_using);
					jQuery('#remark').val(jsonObj.remark);
					jQuery('#proName').val(jsonObj.pro_name);
					jQuery('#attachmentId').val(jsonObj.attachment_id);
					jQuery('#attachmentName').val(jsonObj.attachment_name);
			   }
			 });
	  queueCompleteOver();
	}
	
	function getDrawingType(){
	var selects = document.getElementById("drawingType");
	var urlStr = contextPath + "/SpringR/produce/getDrawingType/"+new Date().getTime();
	jQuery.ajax({
		type:'POST',
		url:urlStr,
		async:false,
		success:function(rtJson){
		var jsonObj = eval('('+ rtJson +')');
			for ( var i = 0; i < jsonObj.length; i++) {
				var prc = jsonObj[i];
				var option = document.createElement("option");
				option.value = prc.id;
				option.innerHTML = prc.name;
				selects.appendChild(option);
			}
		}
	});
}

   	 var oa_upload_limit=limitUploadFiles;//本句话不能删除，否则将无法显示文件
 function initSwfUpload() {
    var linkColor = "#0000ff";
    
    var settings = {
      flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
      upload_url: "<%=contextPath %>/SpringR/fileUpload/fileupload",
      post_params: {"PHPSESSID" : "<%=session.getId()%>"},
      file_size_limit : "100 MB",
      file_types : "*.*",
      file_types_description : "All Files",
      file_upload_limit : 100,
      file_queue_limit : 0,
      custom_settings : {
        uploadRow : "fsUploadRow",
        uploadArea : "fsUploadArea",
        progressTarget : "fsUploadProgress",
        startButtonId : "btnStart",
        cancelButtonId : "btnCancel"
      },
      debug: false,
      button_image_url: "<%=imgPath %>/uploadx4.gif",
      button_width: "65",
      button_height: "29",
      button_placeholder_id: "spanButtonUpload",
      button_text: '<span class=\"textUpload\">批量上传</span>',
      button_text_style: ".textUpload{color:" + linkColor + ";}",
      button_text_top_padding : 1,
      button_text_left_padding : 18,
      button_width: 80,
      button_height: 20,
      button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
      button_cursor: SWFUpload.CURSOR.HAND,
      
      file_queued_handler : fileQueued,
      file_queue_error_handler : fileQueueError,
      file_dialog_complete_handler : fileDialogComplete,
      upload_start_handler : uploadStart,
      upload_progress_handler : uploadProgress,
      upload_error_handler : uploadError,
      upload_success_handler : uploadSuccessOver,
      upload_complete_handler : uploadComplete,
      queue_complete_handler : queueCompleteOver
    };

    swfupload = new SWFUpload(settings);
    
    var attachmentIds = $("attachmentId").value;
    var attachmentNames = $("attachmentName").value;
    if(attachmentIds){
      $('attaTr').style.display = "";
      attachMenuUtil("showAtt","email",null,attachmentNames,attachmentIds,false);

    }
  }
   	 
   	 function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if(json.state=="1") {
       progress.setError();
       progress.setStatus("上传失败：" + serverData.substr(5));
       
       var stats=this.getStats();
       stats.successful_uploads--;
       stats.upload_errors++;
       this.setStats(stats);
    } else {
       if($('attachmentId').value!=null && $('attachmentId').value!=""){
    	 $('attachmentId').value += ',' + json.data.attachmentId;
       } 
       else{
    	   $('attachmentId').value = json.data.attachmentId;
       }
       if($('attachmentName').value!=null && $('attachmentName').value!=""){
    	 $('attachmentName').value += '*' + json.data.attachmentName;
       } 
       else{
    	   $('attachmentName').value = json.data.attachmentName;
       }
       var attachmentIds = $("attachmentId").value;
       var attachmentName = $("attachmentName").value;
       var ensize =  $('ensize').value;
       if(ensize){
       $('ensize').value =(json.data.size + parseInt(ensize));
       }else{
         $('ensize').value =json.data.size ;
       }// 附件大小
    }
  } catch (ex) {
    this.debug(ex);
  }
}

	 function showAttach(attrIds,attrNames,cntrId){
		  var reStr = "<div id='attrDiv'>";
		  var ym = "";
		  var attrId = ""
		  var attrIdArrays = attrIds.split(",");
		  var attrNameArrays = attrNames.split("*");
		  for(var i = 0 ; i <= attrIdArrays.length; i++){
		    if(!attrIdArrays[i]){
		      continue;
		    }
		    var key = attrIdArrays[i];
		    var value = attrNameArrays[i];
		    reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
		  }
		  reStr += "</div>";
		  if(cntrId){
		    $(cntrId).innerHTML = reStr;
		  }else{
		    document.write(reStr);
		  }
 	}
	 
	  function queueCompleteOver(){
		var attachmentIds = $("attachmentId").value;
	    var attachmentNames = $("attachmentName").value;
	    if(attachmentIds){
	      jQuery('#attaTr').show();
       	  jQuery('#fsUploadArea').hide();
	      attachMenuUtil("showAtt","<%=ContractCont.MODULE%>",null,attachmentNames,attachmentIds,false);
	    }
	 }
	  
	  function deleteAttachment(attachmentId,attachmentName){
		  var attrIds = $("attachmentId").value;
	    var attrNames = $("attachmentName").value;
	     var attrIdArrays = attrIds.split(",");
		  var attrNameArrays = attrNames.split("*");
		 var idArray = new Array();
		 var nameArray = new Array();
		 
		 for(var i=0;i<attrIdArrays.length;i++){
			 if(attrIdArrays[i] != attachmentId){
				 idArray.push(attrIdArrays[i]);
				 nameArray.push(attrNameArrays[i]);
			 }
		 }
		 $("attachmentId").value = idArray.join(",");
		 $("attachmentName").value = nameArray.join("*");
		 if($("attachmentId").value == ""){
			 jQuery('#attaTr').hide();
		 }
	  }
	  
	  //选择产品
 var nameArray="";
 function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('selectProduct.jsp',  520, 400);
 }
</script>
</head>
<body topmargin="5" onload="doInit();">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	编辑生产图纸信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="50%" align="center">
        <tr>
      <td nowrap class="TableData">图号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="code" id="code"  class="BigInput" />
      </td>
     
    </tr>
     <tr>
    	<td nowrap class="TableData">所属产品：</td>
 		 <td class="TableData" colspan="2">
 		<input type="text"  name="proName" id="proName"  class="BigInput" readOnly >
 		<input type="hidden" id="proId" name="proId">
 		<input type="button" value="选择产品" class="BigButton" onclick="chooseProduct(['products'],0);">
 		
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">所用版本：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="drawing_version" id="drawing_version"  class="BigInput" />
      </td>
     
    </tr>
     <tr>
    	 <td nowrap class="TableData">当前是否使用：</td>
       <td class="TableData" colspan="2">
 			<select id="is_using" name="is_using">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">图纸类别：</td>
       <td class="TableData" colspan="2">
 			<select id="drawingType" name="drawingType">
		   </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="2">
 		<textarea name="remark" id="remark" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
		<tr id="attaTr" style="display:none">
	      <td nowrap class="TableData">附件: </td>
	      <td class="TableData">
	        <input type="hidden" id="attachmentId" name="attachmentId">
	        <input type="hidden" id="attachmentName" name="attachmentName">
	        <input type="hidden" id="ensize" name="ensize">
	        <span id="showAtt">
	        </span>
	      </td>
   	    </tr>
			<tr>
		      <td nowrap class="TableData">附件选择：</td>
		      <td class="TableData" id="fsUploadRow">
		        	 <div id="fsUploadArea" class="flash" style="width:380px;">
					     <div id="fsUploadProgress"></div>
					     <div>
					       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
					       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="javascript:swfupload.cancelQueue();jQuery('#fsUploadArea').hide();" disabled="disabled">&nbsp;&nbsp;
					    </div>
				      </div>
				      
					    <div id="attachment1">
				          <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
				          <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">	 
				          <span id="spanButtonUpload" title="批量上传附件"></span>
				        </div>
		      </td>
   		   </tr>
  </table>
  <hr/>

<br>
	<div id="listContainer" style="display:none;width:100;">
</div>
   <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>