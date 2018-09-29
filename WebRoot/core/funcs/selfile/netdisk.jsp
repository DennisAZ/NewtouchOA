<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>


<%
	String seqId=request.getParameter("seqId");
	String diskPath=request.getParameter("diskPath");
	String returnFlag=request.getParameter("returnFlag");	
	String headNameStr=(String)request.getParameter("headName");
	if(seqId==null){
	  seqId="0";
	}
	if(diskPath==null){
	  diskPath="";
	}
	if(headNameStr==null){
	  headNameStr="";
	}
	
	if(returnFlag==null){
	  returnFlag="";
	}

%>

<%
	String ext_filter=request.getParameter("EXT_FILTER");
	if(ext_filter==null){
		ext_filter="";
	}
	
	String div_id=request.getParameter("DIV_ID");
	if(div_id==null){
		div_id="";
	}
	String dir_field=request.getParameter("DIR_FIELD");
	if(dir_field==null){
		dir_field="";
	}
	String name_field=request.getParameter("NAME_FIELD");
	if(name_field==null){
		name_field="";
	}
	String type_field=request.getParameter("TYPE_FIELD");
	if(type_field==null){
		type_field="";
	}
	String multi_select=request.getParameter("MULTI_SELECT");
	if(multi_select==null){
		multi_select="";
	}
	

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网络硬盘</title>
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
var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";
//alert("netdisk.jsp>>>ext_filter:"+'<%=ext_filter%>' +"  div_id:"+'<%=div_id%>' +"  dir_field:"+ '<%=dir_field%>' +"  name_field:"+ '<%=name_field%>' +"  type_field:"+ '<%=type_field%>' +"  multi_select:"+ '<%=multi_select%>');

var headerTitle="网络硬盘";
var headerTitleNext="";

var ext_filter="";
var div_id="";
var dir_field="";
var name_field="";
var type_field="";
var multi_select="";

var ParentWindow=parent.opener.window;
var seqId;
var fileCount=0;
var fileArray = new Array();
var diskPathFlag="";



function doInit(){
	//输出列表信息
	var url=requestURL + "/selectFile.act?seqId=<%=seqId%>&diskPath=<%=diskPath%>&returnFlag=<%=returnFlag%>&headName=<%=headNameStr%>&EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>";
	var json=getJsonRs(encodeURI(url));
	//alert("prcsJson>>"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}	
	prcsJson=json.rtData;
	var count=prcsJson.length;

	
	if(prcsJson.length>0){
	var table=new Element('table',{ "width":"98%","class":"TableList","align":"center"}).update("<tbody id='tbody'><tbody>");
	
	$('listDiv').appendChild(table);
		
		var imgStr="";
		var diskNameStr="";
		var nextFlag="";
		var parentPath="";		
		var dirFlag="";
		var counter=0;

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			seqId=prcs.seqId;
			var diskName=prcs.diskName;
			var diskPath=prcs.diskPath;		
			dirFlag=prcs.dirFlag;
			parentPath=prcs.parentPath;
			nextFlag=prcs.netFlag;
			headerStr=prcs.headerTitle;
			diskPathFlag=prcs.diskPathFlag

			ext_filter=prcs.ext_filter;
			div_id=prcs.div_id;
			dir_field=prcs.dir_field;
			name_field=prcs.name_field;
			type_field=prcs.type_field;
			multi_select=prcs.multi_select;



			
			if(dirFlag == "isDir"){
				//alert("diskPath>>"+diskPath);
				
				imgStr="<img src='<%=imgPath%>/dossier.gif' align='absMiddle'></img>";
				diskNameStr +="<a href='#' onclick='forward("+ seqId + ",\""+diskPath + "\",\""+headerStr +"\")'  >" + imgStr +  diskName + "</a><br>";
				
			}else if(dirFlag == "isFile"){
				
				imgStr = "<img src='<%=imgPath%>/defaut.gif' align='absMiddle'></img>";
				diskNameStr +="<a id='FILE_"+counter +"' href='javascript:selFile("+ counter + ",\"" +diskName +"\",\"" + diskPath +"\")' onclick=''  >" + imgStr +  diskName + "</a><br>";
				counter++;

				fileArray[fileCount] = diskName;
				fileCount++;
			}		
			
		}

		
		if(nextFlag == "next"){
			headerTitle=headerStr;
			headerTitleNext=headerTitle
			var toBackImg="<img src='<%=imgPath%>/parent.gif' align='absMiddle' ></img>";
			var toBack="<a href='#' onclick='returnStr("+ seqId + ",\"" + parentPath +"\")' >" + toBackImg + "返回上级目录</a>";
			diskNameStr = toBack + "<br>" + diskNameStr
		}				

		var headStr=new Element('tr', {'class':'TableHeader'}).update("<td><img src='<%=imgPath%>/green_arrow.gif' align='absMiddle'></img>" + headerTitle + "</td>");
		$("tbody").appendChild(headStr);
		
		var tr=new Element('tr',{'class': 'TableData', 'height':'60'});	
		$("tbody").appendChild(tr);	
		var std = "<td align='left'>" + diskNameStr + "</td>"
			tr.update(std);

		var lastTr=new Element('tr');	
		$("tbody").appendChild(lastTr);	
		var lastTd = "<td align='center' class='TableControl' >" + "<input type='button' value='关闭'  class='BigButton' onClick='parent.window.close();'>" + "</td>"
		lastTr.update(lastTd);		

		
	}else{
		//alert("没有数据");
		//var tr=new Element();
		$("nothingDiv").show();
	}
	
	doLoad();
}
function selFile(i,diskName,diskPath){
	
	//alert("selFile>>>ext_filter:"+ext_filter +"  div_id:"+div_id +"  dir_field:"+ dir_field +"  name_field:"+ name_field +"  type_field:"+ type_field +"  multi_select:"+ multi_select);

	//var parentWinDir=ParentWindow.document.getElementById(div_id);
	//alert(parentWinDir);
	//alert("$DIR_FIELD:"+dir_field);	
  

	if(!ParentWindow.document.all(dir_field) || !ParentWindow.document.all(name_field) || !ParentWindow.document.all(type_field) || !ParentWindow.document.getElementById(div_id)){
		return;
	}	  

	var DirArray = new Array();
	var NameArray = new Array();
	var TypeArray = new Array();

	var dir_fieldStr = ParentWindow.document.getElementById(dir_field);
	//alert("dir_fieldStr.value>>>"+dir_fieldStr.value);
	if(dir_fieldStr) {
    //var value  = divInput.innerHTML.trim();
    DirArray=dir_fieldStr.value.split("*");
    //alert("DirArray>>"+DirArray);
	}
		
	var name_fileldStr=ParentWindow.document.getElementById(name_field);	
	if(name_fileldStr){
		//alert("name_fileldStr>>"+name_fileldStr.value);
		NameArray = name_fileldStr.value.split("*");
		//alert("NameArray>>"+NameArray);
	}
	var type_fieldStr= ParentWindow.document.getElementById(type_field);		
	if(type_fieldStr){
		//alert("type_fieldStr_Value>>"+type_fieldStr.value);
		TypeArray = type_fieldStr.value.split("*");
		//alert("TypeArray>>>"+TypeArray);
	}

	var DirValue="";
	var NameValue="";
	var TypeValue="";
	var DivInnerHTML="";
	var flag=0;
	for(j=0;j<NameArray.length;j++){
		//alert("NameArray[j]>>"+NameArray[j]);
		if(NameArray[j]==""){
	  	continue;			
		}
		//alert("DirArray[j]>>"+DirArray[j] + "   diskPath>>"+diskPath);
		//alert("NameArray[j]>>"+NameArray[j] + "   unescape(diskName)>>"+unescape(diskName));
		//alert("TypeArray["+j+"]>>"+TypeArray[j] + "   seqId>>"+seqId);
		if(DirArray[j]==diskPathFlag && NameArray[j]==unescape(diskName) && TypeArray[j]==seqId){
			flag=1;
			//alert("flag>>"+flag);
	    continue;
	  }	      
	   DirValue+=DirArray[j]+"*";
	   NameValue+=NameArray[j]+"*";
	   TypeValue+=TypeArray[j]+"*";
	   DivInnerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>" + NameArray[j] + ";";
		
	}

	if(multi_select){	  
		if(flag==1)	   {
	  	ParentWindow.document.all(dir_field).value=DirValue;
	    ParentWindow.document.all(name_field).value=NameValue;
	    ParentWindow.document.all(type_field).value=TypeValue;
	    ParentWindow.document.getElementById(div_id).innerHTML=DivInnerHTML;
	    document.getElementById("FILE_"+i).style.color="#0000FF";
	   }else{		
	   	 ParentWindow.document.all(dir_field).value+=diskPathFlag + "*";
	   	 // ParentWindow.document.all(dir_field).value+=unescape(diskPath) + "*";
	   	 //alert(ParentWindow.document.all(dir_field).value);
	     ParentWindow.document.all(name_field).value+=unescape(diskName)+"*";
	     ParentWindow.document.all(type_field).value+=seqId + "*";
	     ParentWindow.document.getElementById(div_id).innerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+unescape(diskName)+";";
	     document.getElementById("FILE_"+i).style.color="gray";
	     //alert("ccc:"+document.getElementById("FILE_"+i));
	   }
	
	}	else{
	   if(flag==1) {
	   	ParentWindow.document.all(dir_field).value="";
	    ParentWindow.document.all(name_field).value="";
	    ParentWindow.document.all(type_field).value="";
	    ParentWindow.document.getElementById(div_id).innerHTML="";
	    document.getElementById("FILE_"+i).style.color="#0000FF";
	   }else {
	    ParentWindow.document.all(dir_field).value=diskPathFlag;
	    ParentWindow.document.all(name_field).value=unescape(diskName);
	    ParentWindow.document.all(type_field).value=seqId;
	    ParentWindow.document.getElementById(div_id).innerHTML="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+unescape(diskName)+";";
	    document.getElementById("FILE_"+i).style.color="gray";
	   }
	
	}
	
}

function doLoad(){  
   //alert("doLoad()_fileArray.length>>"+fileArray.length);
  var DirArray = new Array();
	var NameArray = new Array();
	var TypeArray = new Array();

	var dir_fieldStr = ParentWindow.document.getElementById(dir_field);
	if(dir_fieldStr) {
		//alert("divInput>>"+divInput.name);
   //var value  = divInput.innerHTML.trim();
   DirArray=dir_fieldStr.value.split("*"); //d:/aa/22.jpg,e:/cc/55.jpg
   //alert("DirArray>>"+DirArray);
	}
		
	var name_fileldStr=ParentWindow.document.getElementById(name_field);	
	if(name_fileldStr){
		//alert("name_fileldStr>>"+name_fileldStr.value);
		NameArray = name_fileldStr.value.split("*");  //aa.01.jpg,bb.jpg
		//alert("NameArray>>"+NameArray);
	}
	var type_fieldStr= ParentWindow.document.getElementById(type_field);		
	if(type_fieldStr){
		//alert("type_fieldStr_Value>>"+type_fieldStr.value);
		TypeArray = type_fieldStr.value.split("*");  //69,69
		//alert("TypeArray>>>"+TypeArray);  
	}
	
	for(i=0;i<fileArray.length;i++) {
  	//alert("fileArray["+i+"]>>>>"+fileArray[i]);
  	for(j=0;j<NameArray.length;j++){
	    if(NameArray[j]==""){
	    	continue;						
	    }
    	if(DirArray[j] == diskPathFlag && NameArray[j]==unescape(fileArray[i]) && TypeArray[j] == seqId){
      	document.getElementById("FILE_"+i).style.color="gray";
        break;
      }
    }
	}
}
//doLoad();





function forward(seqId,diskPath,headerStr){
	
	//alert("diskPath>>"+diskPath);
	//diskPathFlag = diskPath
	//alert("diskPathFlag>>"+diskPathFlag);
	//alert(seqId);
	//var url="netdisk.jsp?seqId="+seqId+"&diskPath="+diskPath +"&headName=" +headerTitleNext  ;	
	var url="netdisk.jsp?seqId="+seqId+"&diskPath="+diskPath +"&headName=" +headerTitleNext +"&EXT_FILTER=" +ext_filter +"&DIV_ID=" +div_id +"&DIR_FIELD=" +dir_field +"&NAME_FIELD=" +name_field +"&TYPE_FIELD=" +type_field +"&MULTI_SELECT=" +multi_select ;	
	//alert(url);
	var urlStr=encodeURI(url);
	//alert(urlStr);
	location.href=urlStr;
}
function returnStr(seqId,parentPath){
	//alert(seqId);
	//alert(parentPath);
	var url="netdisk.jsp?seqId="+seqId+"&diskPath="+parentPath+"&returnFlag=back" +"&EXT_FILTER=" +ext_filter +"&DIV_ID=" +div_id +"&DIR_FIELD=" +dir_field +"&NAME_FIELD=" +name_field +"&TYPE_FIELD=" +type_field +"&MULTI_SELECT=" +multi_select ;	
	//var urlStr=encodeURI(url);
	//alert(url);
	location.href=url;
	//history.go(-1);
}


</script>
</head>
<body onload="doInit();">
<div id="listDiv"></div>

<div id="nothingDiv" style="display: none">

<table class="TableBlock" width="90%" align="center">
    <tr class="TableData" height="60">
      <td valign="top">
<table class="MessageBox" align="center" width="190">
  <tr>
    <td class="msg blank">
      <div class="content" style="font-size:12pt">无可访问目录</div>
    </td>
  </tr>
</table>
      </td>
    </tr>
    <tr>
      <td nowrap align="center" class="TableControl">
        <input type="button"  value="关闭" class="BigButton" onClick="parent.window.close();">
      </td>
    </tr>
    <thead>
      <td class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif" align="absMiddle" WIDTH="20" HEIGHT="18"> 网络硬盘      </td>
    </thead>
</table>

</div>


</body>
</html>