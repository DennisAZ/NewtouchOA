<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr=request.getParameter("seqId");
	int seqId=0;
	if(seqIdStr!=""){
	  seqId=Integer.parseInt(seqIdStr);
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片浏览</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
//alert("seqId:"+'<%=seqId%>');
var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
function doInit(){
	var url=requestURL + "/getPicInfoById.act?seqId=<%=seqId%>&currNo=";
	var json=getJsonRs(url);
	//alert("rsText>>:"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	//alert("bbbb");
	var table=new Element('table',{ "border":"0", "cellspacing":"1", "cellpadding":"3", "bgcolor":"#000000",  "width":"700", "class":"small", "align":"center"})
	.update("<tbody id='tbody'><tr class='TableHeader'width='700' >"
			+"<td colspan='7' align='left'><span style='width:80%'><b>当前位置:</b></span> <span style='width:18%;text-align:right;'><a href='index.jsp'><img src='<%=contextPath%>/core/funcs/picture/images/parent.gif' border=0 align=middle>返回上级目录</a></span>  </td>"				
			+"</tr><tbody>");
	$('listDiv').appendChild(table);
	if(prcsJson.length>0){
		var lang=prcsJson.length;
		//alert("lang:"+lang);
		//var isPath=prcsJson.isPath;
		if(lang>0){
			var count = parseInt((lang-1)/7+1,10);
			//alert("count:"+count);
			for(var i=0;i<count;i++){
				var tr=new Element('tr',{"bgcolor":"#FFFFFF", "width":"7*100" }).update("<td align='center' valign='top' width='100' height='100' id='td_"+i+"_1'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_2'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_3'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_4'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_5'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_6'></td>"
	          +"<td align='center' valign='top' width='100' width='100' id='td_"+i+"_7'></td>"
					);
					$("tbody").appendChild(tr);
			}			
		}

		for(var i =0;i<count;i++){
      for(var j=1;j<=7;j++){
        if(lang>=(i*7+j)){
        	var prcs=prcsJson[i*7+j-1];
        	//alert(prcs);
    			var picId=prcs.seqId;
    			var fileName=prcs.fileName;
    			var filePath=prcs.filePath;
    			var isDir=prcs.isDir;
    			var flagImg="<img src='<%=contextPath%>/core/funcs/picture/images/folder.gif' border='0'></img>";
    			if(isDir=="isFile"){
    				//flagImg="<img src='"+filePath+ "\\"+fileName + "' border='0'></img>";
    				flagImg="<img src='D:/cc/J0146142.JPG' border='0'></img>";
      		}
    			//alert(flagImg);
    			var div = new Element('div',{"align":"center", "width":"100" }).update("<a href='javascript:open_pic(" + picId +",\"" +  fileName + "\")'>"+ flagImg +"<br>" + fileName +"</a>" );
          $("td_"+i+"_"+j).appendChild(div);
         }
      }
		}
		
		
		
	}else{
		warnDiv();
	}	
}

function warnDiv(){
	var table=new Element('table',{ "width":"220","class":"MessageBox","align":"center"})
		.update("<tbody id='tbody'><tr >"
			+"<td align='' class='msg info'><h4 class='title'>提示</h4><div class='content' style='font-size:12pt'>目录为空</div></td>"
			+"</tr><tbody>");
	$('nothingDiv').appendChild(table);
}

function open_pic(picId,fileName){
	alert("picId:"+picId+"  fileName:"+fileName);
}

function newFolder(){
	alert("new");
	loc_x=(screen.availWidth-300)/2;
  loc_y=(screen.availHeight-300)/2;
  URL = "newfolder.jsp?CUR_DIR_RELAT=&PIC_ID=9";
  window.open(URL,"picture","height=230,width=380,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
	
}

</script>

</head>
<body onload="doInit();">

<form action="picture.php?PIC_ID=2&SUB_DIR=" method="post" name="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/picture/images/picture.gif" align="middle"><span class="big3"> 图片浏览</span>
    &nbsp;&nbsp;
    <select class="BigSelect" name="VIEW_TYPE" id="VIEW_TYPE" onChange="set_view_type();">
    <option value="NAME" selected>按名称排序</option>
    <option value="TYPE" >按类型排序</option>
    <option value="TIME" >按修改时间</option>
    <option value="SIZE" >按大小排序</option>
    </select>
    <select class="BigSelect" name="ASC_DESC" id="ASC_DESC" onChange="set_view_type();">
    <option value=4 selected>升序</option>
    <option value=3 >降序</option>
    </select>
    </td>
  </tr>
</table>
</form>

<div id="listDiv"></div>
<div id="nothingDiv"></div>






<br><div id="fsUploadArea" class="flash" style="width:749px;text-align:center;" align="center">
  <div id="fsUploadProgress"></div>
  <div id="totalStatics"></div>
  <div>
    <input type="button" id="btnStart" class="SmallButton" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
    <input type="button" id="btnCancel" class="SmallButton" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
    <input type="button" class="SmallButton" value="刷新页面" onclick="window.location.reload();">
  </div>
</div>
<table class="TableBlock" width="749" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="100"><b>操作：</b></td>
    <td class="TableData">
    	<div style="display:inline;">
    		<div style="float:left;padding-top:4px;">
        <input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();"><label for="allbox_for" id="allboxlabel">全选</label>
    	  &nbsp;<a href="javascript:window.location='picture.php?PIC_ID=2&SUB_DIR='"><img src="<%=contextPath%>/core/funcs/picture/images/refresh.gif" align="middle" border="0">&nbsp;刷新</a>
    	  &nbsp;<a href="javascript:picdelete('2','','F:/pkSoft');"><img src="<%=contextPath%>/core/funcs/picture/images/delete.gif" align="middle" border="0">&nbsp;删除</a>
    	  &nbsp;<span id="spanButtonUpload" title="批量上传"></span>
    	  &nbsp;<a href="javascript:newFolder();"><img src="<%=contextPath%>/core/funcs/picture/images/notify_new.gif" align="middle" border="0">新建文件夹</a>
    </div>
    </div>
    </td>
  </tr>
  <tr>
    <td class="TableContent" nowrap align="center" width="100"><b>上传单个图片：</b></td>
    <td class="TableData">
    <form name="form2" method="post" action="picture.php" enctype="multipart/form-data" onSubmit="return CheckForm()">
      <input type="file" name="ATTACHMENT" size="30" class="BigInput">
      <INPUT TYPE="hidden" name="ACTION" value="upload">
      <INPUT TYPE="hidden" name="PIC_ID" value="2">
      <INPUT TYPE="hidden" name="SUB_DIR" value="">
      <input type="button" class="SmallButton" value="上传单个图片" onClick="upload_pic();">
    </form>
    </td>
  </tr>
</table>
<br>
<br>


</body>
</html>