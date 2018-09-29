<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
		seqId="0";
	}

%>
<%
	String pageIndex = request.getParameter("pageNo");
	if(pageIndex == null){
	  pageIndex = "0";
	}
	String pageSize = request.getParameter("pageSize");
	if(pageSize == null){
	  pageSize = "20";
	}
	String managerPriSeqId=request.getParameter("managerPriSeqId");
	if(managerPriSeqId==null){
	  managerPriSeqId="0";
	}
	//String managePriv="0";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>共享文件</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/pagePilot.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript">

var pageSize = "<%=pageSize%>";
var totalRecord = 0;
var pageIndex = "<%=pageIndex%>";
var cfgs;


var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
var requestFolderURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFolderAct";
function doInit(){
  getFileFolderName();



//获取权限信息
	var managePriv;
	var folderUrl=requestFolderURL + "/getManagePrivteById.act?seqId=<%=managerPriSeqId%>";
	//alert(url2);
	var json=getJsonRs(folderUrl);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return ;
	}
	prcsJson=json.rtData;
	//alert("rsText>>:"+rsText);
	managePriv=prcsJson.managePriv;
	//alert(managePriv);

  

//输出列表信息
	var url=requestURL + "/getShareFileContentInfo.act?seqId=<%=seqId%>&shareFlag=share&pageNo=" + pageIndex + "&pageSize=" + pageSize;
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}

	//alert("rsText>>>"+rsText);
	prcsJson=json.rtData;


	if(prcsJson.length>0){
		totalRecord=prcsJson[0].totalRecord;	
		pageIndex = prcsJson[0].pageNo;
		pageSize = prcsJson[0].pageSize;   

		//分页处理
		 cfgs = {
		    dataAction: "",
		    container: "pageInfo",
		    pageSize:pageSize,
		    loadData:loadDataAction,
		    totalRecord:totalRecord,
		    pageIndex:pageIndex
		  };
		//alert("totalRecord>>"+totalRecord +" pageIndex>>>"+pageIndex +"  pageSize>>"+pageSize);	
		 showPage();
	}
	
	if(prcsJson.length>0){
		var length=prcsJson.length;
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+"<td nowrap align='center'>文件名称</td>"				
			+"<td nowrap align='center'>附件</td>"				
			+"<td nowrap width='120' align='center'>发布时间</td>"				
			+"<td nowrap align='center'>排序号</td>"	;	
		//var managePriv=1;					
		if( managePriv=="1"){
			strTable+="<td width='50' align='center'>操作</td>";
		}
		strTable+="</tr><tbody>";
		table.update(strTable);
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
			var contentId=prcs.contentId;
			var sortId=prcs.sortId;
			var subject=prcs.subject;
			var content=prcs.content;
			var sendTime=prcs.sendTime;
			var attachmentId=prcs.attachmentId;			
			var attachmentNames=prcs.attachmentName;
			var attachmentDesc=prcs.attachmentDesc;
			var userId=prcs.userId;
			var contentNo=prcs.contentNo;
			var newPerson=prcs.newPerson;
			var readers=prcs.readers;
			var creater=prcs.creater;
			var logs=prcs.logs;

			var imgStr="";
			if(attachmentNames!=""){
				imgStr="<img src='"+ contextPath +"/core/funcs/netdisk/images/defaut.gif' >";
			}
			
			var arry=attachmentNames.split("*");
			var attachmentName="";
			for(var k=0;k<arry.length-1;k++){
				//alert(k+":"+arry[k]);
				attachmentName+=arry[k]+"<br>";
			}
			var attachmentNameString = "";
			
			var arryAttId = attachmentId.split(",");			
			//var attachmentIdStr = "";							
		
			var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
									+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
									+ "<div id='attr_" + i + "'></div>";

			var spanCount=5;
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});			
			table.firstChild.appendChild(tr);	
			var str = "<td align='left'>"
				+ "<a href='<%=contextPath%>/core/funcs/personfolder/personRead.jsp?sortId=<%=seqId%>&managerPriSeqId=<%=managerPriSeqId%>&shareFlag=1&contentId="+contentId +"'>"
				+ subject + "</a></td><td align='left'>"					
				+ hiddenDiv + "</td><td align='center'>"					
				+ sendTime + "</td><td align='center'>"					
				+ contentNo + "</td>";
			if (managePriv=="1") {
				//alert("managePriv:"+managePriv);
				spanCount=6
				str+="<td align='center'>"					
					+ "<a href='<%=contextPath%>/core/funcs/personfolder/shareEdit.jsp?seqId=<%=seqId%>&managerPriSeqId=<%=managerPriSeqId%>&contentId="+contentId +"'> 编辑</a>&nbsp;&nbsp;";
				str += "</td>"	;	
			}
		
			tr.update(str);
			$("returnAttId_"+i).value= attachmentId;
			$("returnAttName_"+i).value= attachmentNames;
			//attachMenuUtil("attr_"+i,"file_folder",null,$('returnAttName_'+i).value ,$('returnAttId_'+i).value,false, i,"","","","",contentId);

			var  selfdefMenu = {
        	office:["downFile","dump","read","edit"], 
	        img:["downFile","dump","play"],  
	        music:["downFile","dump","play"],  
			    video:["downFile","dump","play"], 
			    others:["downFile","dump"]
				}
			if(managePriv != "1"){
			  selfdefMenu.office = selfdefMenu.office.without("edit");		
			}
		attachMenuSelfUtil("attr_"+i,"file_folder",$('returnAttName_'+i).value ,$('returnAttId_'+i).value, i,'',contentId,selfdefMenu);
		

			
		}
		
		
	}else{
		warnDiv();
	}
  
}

function warnDiv(){
	var table=new Element('table',{ "width":"300","class":"MessageBox","align":"center"})
		.update("<tbody id='tbody'><tr >"
			+"<td align='center' class='msg info'><div class='content' style='font-size:12pt'>该文件夹尚无文件</div></td>"
			+"</tr><tbody>");
	$('nothingDiv').appendChild(table);
}

function getFileFolderName(){
  var url = requestFolderURL + "/getShareFolderNameById.act?seqId=<%=seqId%>";
	var json=getJsonRs(url);
	//alert("根目录rsText>>:"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;	
	
	var sortParentStr = json.rtMsrg;
	//document.getElementById("sortParentStr").value = sortParentStr;
	//alert("sortParentStr>>>"+sortParentStr);
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
		if(prcs.sortName){
	  	$("sortName").innerHTML=prcs.sortName;			
		}else{				
			$("sortName").innerHTML="根目录";
		}
	}
}

var pageInfoS = null;
function showPage() {
  //alert('test');
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  $('pageDiv').style.display = "";

}

function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  //alert("pageNo.F>>"+pageNo +"  pageSize>>"+pageSize);
  window.location.href ="shareFolder.jsp?seqId=<%=seqId%>&pageNo=" + pageNo + "&pageSize=" + pageSize ;
}

//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName + "&contentId=" + attrchIndex;
  //alert("attachId>>>"+attachId  +"  attachName>>>" +attachName +"   attrchIndex>>" +attrchIndex);
  //alert(url);
	var json=getJsonRs(encodeURI(url));
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag){
			//alert(updateFlag);
		  return true;
		 
		}else{
			return false;
		}
  	
	}
  
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif" align="middle"><b>&nbsp;<span class="Big1" id="sortName"> </span></b><br>
   </td>
   <td id="pageDiv" align="right" valign="bottom" class="small1">
	   <div id="pageInfo" style="float:right;"></div>
  	</td>
  </tr>
</table>

<div id="listDiv" align="center"></div>
<div id="nothingDiv" align="center"></div>




</body>
</html>