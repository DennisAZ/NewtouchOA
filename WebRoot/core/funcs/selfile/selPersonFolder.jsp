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
<title>个人文件柜</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
//alert('<%=seqId%>');
var ParentWindow=parent.opener.window;
var ext_filter="";
var div_id="";
var dir_field="";
var name_field="";
var type_field="";
var multi_select="";

var fileCount=0;
var fileArray = new Array();

function doInit(){

//输出列表信息
	var url=requestURL + "/getPersonContentsById.act?seqId=<%=seqId%>&EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	//alert("rsText>>"+rsText);
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"98%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+"<td nowrap align='center' width='50%'>文件</td>"
			+"<td nowrap align='center' width='50%'>选择附件</td>"				
			+"</tr><tbody>";
		table.update(strTable);
		$('listDiv').appendChild(table);

		var counter=0;
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var contentId=prcs.contentId;
			var sortId=prcs.sortId;
			var subject=prcs.subject;
			var attachmentId=prcs.attachmentId;			
			var attachmentNames=prcs.attachmentName;


			ext_filter=prcs.ext_filter;
			div_id=prcs.div_id;
			dir_field=prcs.dir_field;
			name_field=prcs.name_field;
			type_field=prcs.type_field;
			multi_select=prcs.multi_select;

			

			var arry=attachmentNames.split("*");
			var attachmentName="";
			for(var k=0;k<arry.length-1;k++){
				//alert(k+":"+arry[k]);
				attachmentName+=arry[k]+"<br>";
			}

			var arryAttId = attachmentId.split(",");			
			var attachmentIdStr = "";							
			for (var t=0 ; t<arryAttId.length ; t++) {
				if (arryAttId[t]) {


				  
					//alert("arryAttId[t]>>>"+arryAttId[t] +"   arry[t]>>"+arry[t]);

					

					var urlStr=requestURL +"/isHaveFile.act?attIdStr=" + arryAttId[t] + "&attNameStr=" +  arry[t];
					//alert(urlStr);
					var jsonStr=getJsonRs(encodeURI(urlStr));
					if(jsonStr.rtState == '1'){
						alert(json.rtMsrg);
						return ;				
					}

					var prcsJsonStr=jsonStr.rtData;
					//alert("rsText>>"+rsText);
					var isHaveFile=prcsJsonStr.isHaveFile;		
					//alert("isHaveFile>>>"+isHaveFile);		


					
					///fileArray[fileCount].value = diskName;
					//fileArray[fileCount].name = diskName;					
					fileArray[fileCount] = {attId : arryAttId[t] , attName:arry[t]};

					if(isHaveFile == 1){
					  attachmentIdStr += "<a id='FILE_"+counter +"' href='javascript:selFile("+ counter + ")' >" + arry[t] + "</a><br>";
					}else{
					  attachmentIdStr += arry[t] + "<br>";
					}
					
					
					counter++;
					fileCount++;
				 }
			}
			
			if(attachmentNames!=""){
				var className = (i % 2 == 0) ? "TableLine1" : "TableLine2"; 
				var tr=new Element('tr',{'class': className ,'font-size':'10pt'});	
				table.firstChild.appendChild(tr);	
				var str = "<td align='left'>" + subject + "</td>"
						+ "<td align='left'>" + attachmentIdStr + "</td>";
				tr.update(str);
				$("closeDiv").show();
			}
		}
		
  
	}else{
		$("nothingDiv").show();
		$("closeDiv").show();
		
	}
	doLoad();
}

function selFile(i){
	//alert("selFile>>>ext_filter:"+ext_filter +"  div_id:"+div_id +"  dir_field:"+ dir_field +"  name_field:"+ name_field +"  type_field:"+ type_field +"  multi_select:"+ multi_select);
	
	if(!ParentWindow.document.all(dir_field) || !ParentWindow.document.all(name_field) || !ParentWindow.document.all(type_field) || !ParentWindow.document.getElementById(div_id)){
		return;
	}	  

	var DirArray = new Array();
	var NameArray = new Array();
	var TypeArray = new Array();

	var dir_fieldStr = ParentWindow.document.getElementById(dir_field);
	if(dir_fieldStr) {
    DirArray=dir_fieldStr.value.split("*");
	}
		
	var name_fileldStr=ParentWindow.document.getElementById(name_field);	
	if(name_fileldStr){
		NameArray = name_fileldStr.value.split("*");
	}
	var type_fieldStr= ParentWindow.document.getElementById(type_field);		
	if(type_fieldStr){
		TypeArray = type_fieldStr.value.split("*");
	}

	var DirValue="";
	var NameValue="";
	var TypeValue="";
	var DivInnerHTML="";
	var flag=0;

	for(j=0;j<NameArray.length;j++){
		if(NameArray[j]==""){
	  	continue;			
		}
		if(DirArray[j]==fileArray[i].attId && NameArray[j]==unescape(fileArray[i].attName) ){
			flag=1;
			//alert("DirArray[j]>>"+DirArray[j] +"  NameArray>>"+NameArray[j]);
	    continue;
	  }	      
	   DirValue+=DirArray[j]+"*";
	   NameValue+=NameArray[j]+"*";
	   TypeValue+=TypeArray[j]+"*";
	   DivInnerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>" + NameArray[j] + ";";
		
	}
	if(multi_select){	  
		if(flag==1){
	  	ParentWindow.document.all(dir_field).value=DirValue;
	    ParentWindow.document.all(name_field).value=NameValue;
	    ParentWindow.document.all(type_field).value=TypeValue;
	    ParentWindow.document.getElementById(div_id).innerHTML=DivInnerHTML;
	    document.getElementById("FILE_"+i).style.color="#0000FF";
	   }else{		
	   	 ParentWindow.document.all(dir_field).value+=fileArray[i].attId + "*";
	   	 // ParentWindow.document.all(dir_field).value+=unescape(diskPath) + "*";
	   	 //alert(ParentWindow.document.all(dir_field).value);
	     ParentWindow.document.all(name_field).value+=unescape(fileArray[i].attName) + "*";
	     ParentWindow.document.all(type_field).value+= "*";
	     ParentWindow.document.getElementById(div_id).innerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>" + unescape(fileArray[i].attName) + ";";
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
	    ParentWindow.document.all(dir_field).value= fileArray[i].attId;
	    ParentWindow.document.all(name_field).value=unescape(fileArray[i].attName);
	    ParentWindow.document.all(type_field).value="";
	    ParentWindow.document.getElementById(div_id).innerHTML="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+unescape(fileArray[i].attName)+";";
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
   	if(DirArray[j] == fileArray[i].attId && NameArray[j]==unescape(fileArray[i].attName)){
     	document.getElementById("FILE_"+i).style.color="gray";
       break;
     }
   }
	}
}


</script>
</head>
<body onload="doInit();">
<div id="listDiv"></div>
<br>

<div id="nothingDiv" style="display: none">
	<table class="MessageBox" align="center" width="170">
	  <tr>
	    <td class="msg blank">
	      <div class="content" style="font-size:12pt">无可选文件</div>
	    </td>
	  </tr>
	</table>	
</div>

<div id="closeDiv" style="display: none">
	<div align="center">
	  <input type="button"  value="关闭" class="BigButton" onClick="parent.window.close();">
	</div>
</div>


</body>
</html>