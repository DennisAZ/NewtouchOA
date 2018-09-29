<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>选择图书</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
	<link rel="stylesheet" href ="<%=cssPath %>/style.css">
	<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
	 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
	  <script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript">
var menu_enter="";

function borderize_on(e)
{
 color="#708DDF";
 source3=event.srcElement

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
 source3=event.srcElement

 if(source3.className=="menulines")
 { source3.style.borderColor="black";
   source3.style.backgroundColor=color;
   source3.style.color="white";
   source3.style.fontWeight="bold";
 }

 menu_enter=source3;
}

var parent_window = parent.dialogArguments;

function add_bookno(bookNo,bookName){
	parent_window.form1.bookNo.value="";
  parent_window.form1.bookNo.value=bookNo;
  parent.close();
}



function borderize_off(e)
{
 source4=event.srcElement

 if(source4.className=="menulines" && source4!=menu_enter)
    {source4.style.backgroundColor="";
     source4.style.borderColor="";
    }
}


  function bookType(){ //查询图书编号与名称
    var url = contextPath+'/yh/subsys/oa/book/act/YHBookTypeEnterAct/findBookType.act'; 
		var rtJson = getJsonRs(url);
		//alert(rsText);
		if(rtJson.rtState == '0'){
			var rtData = rtJson.rtData;
			var listData = rtData.listData;
			//alert(listData);
		  if(listData.length > 0){
		     for(var i = 0; i<listData.length; i++){
           var data = listData[i];
           addRow(data, i);
          } 
			} 
		}else{
      alert("检索图书编号和名称为空");
	  }
	  blurFindBook();
  }
  function addRow(temp, i){
    var td ="<td nowrap align=center class='menulines' onClick=\"javascript:add_bookno('"+ temp.bookNo+"','"+ temp.bookName +"')\" >" + temp.bookNo+"("+ temp.bookName +")" +"</td>";
    var tr = new Element("tr", {"class": 'TableControl'});
   $('dataBody').appendChild(tr);  
    tr.update(td);
  }
  //模糊查询 返回data
  function blurFindBook(){
    data = <%= request.getAttribute("data")%>;
    if(data.listData.length > 0){  
			for(var i = 0; i < data.listData.length; i ++){
			  var data1 = data.listData[i];
			  addRow(data1, i);
			  }
   } 
  }
</script>

</head>
<body class="bodycolor" onMouseover="borderize_on(event)" onMouseout="borderize_off(event)" onclick="borderize_on1(event)" topmargin="5" onload="bookType()">
	<table class="TableList"  width="95%" align="center">
	  <tr class="TableControl">
       <td class="menulines" align="center" onClick=""></td>
    </tr>
		<thead class="TableControl">
		  <th bgcolor="#d6e7ef" align="center"><b>选择图书编号（最多显示50条）</b></th>
		</thead>
	</table>
	<table class="TableList"  width="95%" align="center">
	<tbody id ="dataBody"></tbody>
	</table>
	
</body>
</html>