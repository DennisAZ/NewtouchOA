<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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
<title>已选文件</title>
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
//alert("netdisk.jsp>>>ext_filter:"+'<%=ext_filter%>' +"  div_id:"+'<%=div_id%>' +"  dir_field:"+ '<%=dir_field%>' +"  name_field:"+ '<%=name_field%>' +"  type_field:"+ '<%=type_field%>' +"  multi_select:"+ '<%=multi_select%>');

var ParentWindow=parent.opener.window;
var DirArray = new Array();
var NameArray = new Array();
var TypeArray = new Array();

var dir_fieldStr = ParentWindow.document.getElementById('<%=dir_field%>');
if(dir_fieldStr) {
	DirArray=dir_fieldStr.value.split("*");
	//alert("DirArray>>"+DirArray);
}

var name_fileldStr = ParentWindow.document.getElementById('<%=name_field%>');	
if(name_fileldStr.value){
	//alert("name_fileldStr>>"+name_fileldStr.value);
	NameArray = name_fileldStr.value.split("*");
  
	//alert("NameArray>>"+NameArray);
	//alert("NameArray.length>>"+NameArray.length);

}

var type_fieldStr= ParentWindow.document.getElementById('<%=type_field%>');		
if(type_fieldStr){
	//alert("type_fieldStr_Value>>"+type_fieldStr.value);
	TypeArray = type_fieldStr.value.split("*");
	//alert("TypeArray>>>"+TypeArray);
}

function doLoad(){
	if(NameArray.length==0 && document.getElementById("BodyDiv")){
		//alert("NameArray.length>>"+NameArray.length);
  	document.getElementById("BodyDiv").innerHTML="<br><div align=center><span style='BACKGROUND:#EEEEFF;COLOR:#FF6633;margin: 10px;border:1px dotted #FF6633;font-weight:bold;padding:8px;width=140'>尚无选择文件</span></div>";
    return;
  }
   
	var BodyDivInnerHTML="<table class=TableBlock width=100%><thead class=TableHeader align=center><td width=40>选择</td><td>文件名</td></thead>";
  for(i=0;i<NameArray.length;i++){
  	if(NameArray[i]==""){
    	continue;				
  	}
   	BodyDivInnerHTML+="<tr class=TableData><td align=center><input type=checkbox id=email_select name=email_select onclick='SelFile()' checked></td><td>"+NameArray[i]+"</td></tr>";
	}
  BodyDivInnerHTML+="</table>";
  document.getElementById("BodyDiv").innerHTML=BodyDivInnerHTML;
   //alert(document.getElementById("BodyDiv").innerHTML)
}

function SelFile(){
	if(!ParentWindow.document.all('<%=dir_field%>') || !ParentWindow.document.all('<%=name_field%>') || !ParentWindow.document.all('<%=type_field%>') || !ParentWindow.document.getElementById('<%=div_id%>')){
      return;		
	}
   
  ParentWindow.document.all('<%=dir_field%>').value="";
  ParentWindow.document.all('<%=name_field%>').value="";
  ParentWindow.document.all('<%=type_field%>').value="";
  ParentWindow.document.getElementById('<%=div_id%>').innerHTML="";
  var emailStr=document.getElementsByName("email_select");
   //alert("email_select.length>>"+emailStr.length);
  for(i=0;i<emailStr.length;i++){
  	var el=document.getElementsByName("email_select").item(i);
  	//alert("e1.item>>>"+el);
    if(!el.checked){
    	continue;
    }
		if('<%=multi_select%>'){
      ParentWindow.document.all('<%=dir_field%>').value+=DirArray[i]+"*";
      ParentWindow.document.all('<%=name_field%>').value+=NameArray[i]+"*";
      ParentWindow.document.all('<%=type_field%>').value+=TypeArray[i]+"*";
      ParentWindow.document.getElementById('<%=div_id%>').innerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+NameArray[i]+";";
		}else{
      ParentWindow.document.all('<%=dir_field%>').value=DirArray[i];
      ParentWindow.document.all('<%=name_field%>').value=NameArray[i];
      ParentWindow.document.all('<%=type_field%>').value=TypeArray[i];
      ParentWindow.document.getElementById('<%=div_id%>').innerHTML="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+NameArray[i]+";";
		}
  }
  if(i==0 && document.all("email_select").checked) {
		if('<%=multi_select%>'){
	  	ParentWindow.document.all('<%=dir_field%>').value+=DirArray[i]+"*";
	    ParentWindow.document.all('<%=name_field%>').value+=NameArray[i]+"*";
	    ParentWindow.document.all('<%=type_field%>').value+=TypeArray[i]+"*";
	    ParentWindow.document.getElementById('<%=div_id%>').innerHTML+="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+NameArray[i]+";";
		}else{
	  	ParentWindow.document.all('<%=dir_field%>').value=DirArray[i];
	    ParentWindow.document.all('<%=name_field%>').value=NameArray[i];
	    ParentWindow.document.all('<%=type_field%>').value=TypeArray[i];
	    ParentWindow.document.getElementById('<%=div_id%>').innerHTML="<img src='<%=imgPath%>/attach.png' align='absMiddle'>"+NameArray[i]+";";
		}
	}
}


</script>

</head>
<body onload="doLoad();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3">&nbsp;已选择文件</span>
    </td>
  </tr>
</table>
<br>

<div id="BodyDiv" width="100%"></div>
<br>

<div align="center">
  <input type="button"  value="关闭" class="BigButton" onClick="parent.window.close();">
</div>


</body>
</html>