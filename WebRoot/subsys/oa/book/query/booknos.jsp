<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<title>选择图书</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<style>
.menulines{}
</style>

<SCRIPT>
<!--

var menu_enter="";

function borderize_on(e)
{
 color="#708DDF";
 var event = e || window.event;
 //var source3=event.srcElement;
var source3=event.srcElement || event.target;	
 if(source3.className=="menulines" && source3!=menu_enter)
    source3.style.backgroundColor=color;
}

function borderize_on1(e)
{
 for (i=0; i<document.all.length; i++)
 { document.all(i).style.borderColor="";
   document.all(i).style.backgroundColor="";
   document.all(i).style.color="";
   document.all(i).style.fontWeight="";
 }

 color="#003FBF";
 var event = e || window.event;
 var source3=event.srcElement || event.target;	

 if(source3.className=="menulines")
 { source3.style.borderColor="black";
   source3.style.backgroundColor=color;
   source3.style.color="white";
   source3.style.fontWeight="bold";
 }

 menu_enter=source3;
}

function borderize_off(e)
{
	var event = e || window.event;
	//var source4=event.srcElement;
  var source4=event.srcElement || event.target;	
 if(source4.className=="menulines" && source4!=menu_enter)
    {source4.style.backgroundColor="";
     source4.style.borderColor="";
    }
}

//-->
</SCRIPT>
<script Language="JavaScript">
var parent_window = parent.dialogArguments;//获得父窗口对象

function add_bookno(bookNo,bookName){
	parent_window.document.getElementById("form1").bookNo.value="";
	parent_window.document.getElementById("form1").bookNo.value= bookNo;//给父窗口对象赋值
  parent.close();
}

function doinit(){
  var parent_window = window.parent.dialogArguments;
  
  //var userId =  parent_window.form1.toId.value;	
  
  var userId =  parent_window.document.getElementById("form1").toId.value;	
  
  if(userId){    
    bookAjax('', userId);
	}else{
	  bookAjax('', '');
	}	  
}

function bookAjax(param, userId){ 
  var condition = "";
  if(param){
    condition = toUtf8Uri(param);
  } 
  var url = contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/findBookNos.act?condition=" + condition +"&userId=" +userId;
  var rtjson = getJsonRs(url); 
  if(rtjson.rtState == '0'){
    
    var data = rtjson.rtData;
    var t = data.length; 
    if(t>0){
      var bookNos = "";
      $("msg").update("");
      $("TableControl").show();
      $("tableHead").show();
      for(var i=0; i<t; i++){
        bookNos += "<tr class=\"TableControl\">";
        bookNos += "<td class=\"menulines\" align=\"center\" onclick=\"javascript:add_bookno('"+ data[i].bookNo+"','"+ data[i].bookName +"')\" style=\"cursor:pointer\">";
        bookNos += data[i].bookNo +"("+ data[i].bookName +")"+"</td>";
        bookNos += "</tr>";   
      }
      $("tableList").update(bookNos);
    }else{ 
      var info="";
      info +='<table align="center" width="300" class="MessageBox">';
      info += '<tbody>';
      info +=    '<tr>';
      info +=    '<td class="msg info">';
      info +=       '<h4 class="title" align="left">提示</h4>';
      info +=       '<div style="font-size: 12pt;" class="content">没有符合条件的图书！</div>';
      info +=    '</td>';
      info += '</tr>';
      info += '</tbody>';
      info += '</table>'  ;   
      $("msg").update(info);
      $("TableControl").hide();
      $("tableHead").hide();
    }
    
  }
}
</script>
</head >
<body onload="doinit();" class="bodycolor" onMouseover="borderize_on(event)" onMouseout="borderize_off(event)" onclick="borderize_on1(event)" topmargin="5">
<table class="TableList"  id="tableHead" width="95%" align="center">
<thead class="TableControl" id="TableControl">
  <th bgcolor="#d6e7ef" align="center"><b>选择图书编号（最多显示50条）</b></th>
</thead>
<tbody id="tableList"> 
</tbody>
</table>
<div id="msg"></div>
</body>
</html>
