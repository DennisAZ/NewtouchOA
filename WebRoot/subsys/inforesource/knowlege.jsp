
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<html>
 <%
   YHKengine ki = (YHKengine)request.getAttribute("ki");
   List<String> list0 = ki.getUserNameList();
   List<String> list1 = ki.getAreaNameList();
   List<String> list2 = ki.getOrgNameList();
   List<String> list3 = ki.getSubJectList();
   List<String> list4 = ki.getKeyWordList();
   
   String files = (String)request.getAttribute("files");
  
  %>
 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="tree/css/css.css"  type="text/css" />
<link rel="stylesheet" href="tree/css/css-content.css"  type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree/js/jquery.js"></script>

<script><!--
 var files = <%=files%> ;
 function relatedFiles(){
   var relateFile ="";
  if(files){
    if(files.rtState == '0'){
      var fl = files.rtData.Rows;
      var len  = fl.length;
      for(var i = 0 ; i<len; i++){
        relateFile +="<b><span>"+fl[i].TITLE+"</span></b>";
        var fileId = fl[i].FILE_ID;
        
        relateFile += zhaiYao(fileId) +"<br>";
        relateFile += "--------------------------<br>";
        
      }
      //alert(relateFile);
       document.getElementById("dataa").innerHTML =relateFile;
      //$("dataa").append(relateFile);
    }else{
      document.getElementById("dataa").innerHTML ="没有相关文档";
    }
   }
 }
  function zhaiYao(fileId){//通过sign_files表 中的 file_id查询摘要内容(ABSTRACT)
    var zhaiyaoFile ="";
    var vale= "";
    $.ajax({
     url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/getzhaiYao.act",
 	   type:"post",
 		 data:"fileId="+fileId,	 
 		 async:false,   	
 		 success:function(data){
  		 var obj = eval("(" + data +")");
  		 vale= obj.rtData; 
     }		
   });  
    return vale;
 }
</script>
<style>
#table-b1 {
	width: 586px;
	padding: 0px;
	text-align: left;
	margin-top: 10px;
}

#table-b #Related-bg1 {
	background-image: url(images/Related-b.gif);
	background-repeat: repeat-y;
	height: 400px;
	width: 580px;
}


</style>

<script>
function showTouchGrap(data) {
  var url = contextPath + "/core/module/touchgraph/main.jsp?data="+encodeURIComponent(data);
  window.open(url);
}

</script>
</head>
  <body onload="relatedFiles();">
  <div>
    <table border="0" cellspacing="2" class="TableList" cellpadding="3" align="center" style="width:589px;">
      <tr>
        <td class="TableContent" align="left">人名:</td> 
        <% 
         if(list0!=null){%>
           <td align="left">
         <%
        for(int i = 0; i < list0.size(); i ++){ %>
         <a href="javascript:void(0);" onclick="showTouchGrap('<%=list0.get(i)%>')"><%=list0.get(i)%></a>&nbsp;    
         <%} %>
          </td> 
         <%
        } else{%>
        <td  align="left">无&nbsp;</td>
        <%} %>
        <td class="TableContent" align="left" >&nbsp;地名:</td> 
        <% if(list1 != null){%>
         <td align="left">
        <%
          for(int j = 0; j < list1.size(); j ++){
         %>
        <a href="javascript:void(0);" onclick="showTouchGrap('<%=list1.get(j)%>')"><%=list1.get(j)%></a>&nbsp;
        <%}%>
        </td> 
        <%
        }else{ %>
         <td align="left">无</td> 
        <%} %>
      </tr>
      
      
      <tr >
      <td class="TableContent" align="left">主题词:</td> 
        <%
        if(list3!=null){%>
           <td align="left" >
        <%
         for(int m = 0; m<list3.size(); m++){ %>
         <a href="javascript:void(0);" onclick="showTouchGrap('<%=list3.get(m)%>')"><%=list3.get(m)%></a>&nbsp;  
         <%} %>
          </td>   
         <%
        }else{%>
        <td align="left" >无</td>
        <%} %>
         <td class="TableContent" align="left">&nbsp;&nbsp;组织机构:</td> 
         <%
        if(list2!=null){
         for(int n = 0; n<list2.size(); n++){ %>
         <td align="left"><a href="javascript:void(0);" onclick="showTouchGrap('<%=list2.get(n)%>')"><%=list2.get(n)%></a>&nbsp;</td>      
         <%} 
         }else{%>
          <td align="left">无</td>   
         <%} %>
      </tr>
      
      <tr >
      
        <td class="TableContent" align="left">关键词:</td> 
        
        <%if(list4!=null){%>
          <td class="TableContent" colspan=3 align=left>
        <%for(int z = 0; z<list4.size(); z++){ %>
        <a href="javascript:void(0);" onclick="showTouchGrap('<%=list4.get(z)%>')"><%=list4.get(z)%></a>
        <%}%>
         </td>     
        <%        
        }else{
        %>
       <td colspan=3 align=left>无</td>     
        
        <%
        }%>
      </tr>
      <tr>
      <td colspan=4 rowspan=100>
         <div id="table-a">
               <div id="tab-a-con" class="hiddiv">
               <div id="dataa">相关文件</div> 
              </div> 
			        <div id="page-a">   
					    <ul id="pageNumber">
					    </ul>     
			       </div>
        </div>
         </td>
      </tr>
    </table>
   </div>   
</body>
</html>
