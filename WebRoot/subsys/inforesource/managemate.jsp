<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<html>
<head>
<%
	List<YHMateType> typeList = (List<YHMateType>)request.getAttribute("types");//获取所有的
  YHMateShow show = (YHMateShow)request.getAttribute("idStr");//获取已定义好的元素
  String subs = "";
  String pIds = "";  
  if(show != null){
	  if(show.getIDSTR()!=null && show.getIDSTR()!=""){
	    subs = show.getIDSTR();
	  } 
	  if(show.getPR_ID()!=null && show.getPR_ID()!=""){
	    pIds = show.getPR_ID();
	  }
  }
  String saveOk = (String)request.getAttribute("saveOk");
  String ftype = (String)request.getAttribute("ftype");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<!-- 
<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css"/>
 -->
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>



<script type="text/javascript">
  window.onscroll=window.onresize=function(){
	   op_btn=document.getElementById("OP_BTN");
	   if(!op_btn) return false;	   
	   op_btn.style.left=document.body.clientWidth+document.body.scrollLeft-160;
	   op_btn.style.top =document.body.scrollTop +5;	 
	}   
</script>
<script type="text/javascript">    

  function doInit(){    
		var pds = "<%=pIds%>";
		var subs = "<%=subs%>";		
		var splitpds = pds.split(",");
		var splitsubs = subs.split("|");		
		$("show").value = pds;
		for(var i=0; i<splitpds.length; i++){
			var obj = document.getElementsByName(splitpds[i]);
			var second = splitsubs[i].split(",");
			for(var j=0; j<obj.length; j++){
				for(var k=0; k<second.length; k++){
					if(obj[j].value == second[k]){
						obj[j].checked = true;	
					}else{
						continue;
					}
				}
			}
		}

    var saveOk = "<%=saveOk%>";     
    if(saveOk != ""){
      if(saveOk == "ok"){
         window.opener.findTypeMenu();
         window.opener.freshContent();
         window.close();
      }
    }		
  }
    
		Array.prototype.remove=function(flag){		   
			var arr = new Array();			
			for(var i=0; i<this.length; i++){			 
			  if(flag != this[i]){
				arr.push(this[i]);
			  }
			}
		 return arr;
		};       
		
	 Array.prototype.add=function(flag){
		var arr = new Array();	
		if(this.length == 0){
		   this.push(flag);
		   return this;
		}else{			   
		  if(this.contain(flag)==false){//如果没有包含这个元素，则加入
			this.push(flag);
			return this;
		  }			  
		}
		 return this;
	}
		
		Array.prototype.contain=function(flag){
			if(this.length == 0){
			  return false;
			}
			for(var i=0; i<this.length; i++){
			     if(flag == this[i]){
				   return true;
				 }
			 }
		return false;
	}

		
		var array = new Array();
		function selectAll(field){
		  var selObj = document.getElementById(field);
			var ischeck = selObj.checked;		    
			var obj = document.getElementsByName(field);		
			if(ischeck == true){			    			
				for(var i=0; i<obj.length; i++){ //
					obj[i].checked = true;
					
				}				
				array = array.add(field);//把选中的都添加到数组中				
			}else{			    			
				for (i = 0; i < obj.length; i++) {
					obj[i].checked = false; 					
				}				
				array = array.remove(field);	//没有选中的都去掉
			}		
			checkArr(array);
		}
   //此方法是选中后 ，再次点击父节点又没有选中，最后过滤选中的放进数组中
		function checkArr(array){		
		  var arr = new Array();		   
		   for(var i=0; i<array.length-1; i++){
		      for(var j=i+1; j<array.length; j++){
			     if(array[i] != array[j]){
					arr.push(array[i]);
				 }
			  }
		   }
		   var va = $("show").value;//如果show的值为空，则直接把串加入		   if(va == ""){//如果第一次登陆进来为空（没有选中的），就把本次选中的连接起来 
		     $("show").value = array.join(",");
			 }else{	                    //如果show不为空，则进行筛选，需要遍历父节点，把选中的拼成串填入show中			
				 var newIds = "";
				 $$(".parent").each(function(obj){//否侧有选中的（以及本次选中的）追加父节点的值
						if(obj.checked){
						  newIds += obj.value +",";
						}
				 });
		    $("show").value = newIds.substring(0, newIds.lastIndexOf(",")==-1?0:newIds.lastIndexOf(","));
			 }
		}

		function onSave(){
			$("save").submit();
		}

		function closewin(){
       window.close();
		}

		
</script>
</head>
<body>
<body class="bodycolor" topmargin="5" onload = "doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">编辑</span>&nbsp;&nbsp;
    <div id="OP_BTN" style="width:150px;top:5px;right:20px;position:absolute;">   
      <input type="button" value="确定" class="BigButton" onclick="javascript:onSave();return false;">&nbsp;&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:closewin();return false;">     
    </div>
    </td>
  </tr>
</table>
<form id="save" name="save" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateShowAct/saveAjax.act?ftype=<%=ftype%>" method="post">
<input type="hidden" id="show" name="show" style="width:500px;" value=""/>
<table border="0" cellspacing="2" class="small" cellpadding="3" align="center">
<tr class="">    
   <%
   		if(typeList != null && typeList.size()>0){
   		  int preId = 999999999;
   		  String inputName = "";
   		  for(int i=0; i<typeList.size(); i++){   		      
   		         if("0".equalsIgnoreCase(typeList.get(i).getParentId())){   		 //父元素         
   		             preId = typeList.get(i).getSeqId();//获取父元素的seq_id
   		            inputName = typeList.get(i).getNumberId() + "-" + typeList.get(i).getSeqId()+"_";
   		            if(i==0){//这个判断是判断的样式
   		              %>
   		              <td valign="top">
   		              <table class="TableBlock" align="center">
   		              <%
   		            }else{   		              
   		              %>
   		              	</td></table>
	              		  <td valign="top"><table class="TableBlock" align="center">
   		              <%
   		            }
   		           %>  
   		            <tr class="TableHeader" title="<%=typeList.get(i).getcNname() %>"> <!--父元素 的名字 -->   
	   		             <td nowrap>
		                   <input type="checkbox" name="<%=inputName%>" class="parent" id="<%=inputName%>" value="<%=inputName%>" onclick="selectAll('<%=inputName%>')">
		                   <img src="<%=contextPath%>/subsys/inforesource/tree/imgs/node_lv1.jpg" width=15 height=15> <label><b><%=typeList.get(i).getcNname()%></b></label>
		                 </td>
	                </tr>
	                <%
	                	if(typeList.get(i).getValues()!=null && typeList.size()>0){
	                	   for(int j=0; j<typeList.get(i).getValues().size(); j++){
	                	     String pvalue = inputName +"V" + typeList.get(i).getValues().get(j).getSeqId();
	                	  //  System.out.println(typeList.get(i).getValues().get(j).getSeqId()+"父元素下的值域");??如何取得 是取得主表 还是值域表
	                	  %>
	                	  	<tr class="TableData" title="<%=typeList.get(i).getValues().get(j).getValueName() %>"><!-- 父元素值域  -->  
			   		             <td nowrap>
				                   &nbsp;<input type="checkbox" name="<%=inputName%>" id="<%=pvalue%>" value="<%=pvalue%>" onClick="">
				                   <img src="<%=contextPath%>/subsys/inforesource/tree/imgs/node_lv3.jpg" width=15 height=15> <label><%=typeList.get(i).getValues().get(j).getValueName() %></label>
				                 </td>
	                     </tr>
	                	  <%
	                	}
	                	}
	                %>
	              <%} if((preId+"").equalsIgnoreCase(typeList.get(i).getParentId())){//seq_Id等于mate_type表中的parentid 说明是子元素，等于0的话是父元素
	              		String subName = inputName + typeList.get(i).getSeqId();
	              %>   <!-- 子元素  -->  
	                <tr title="<%= typeList.get(i).getcNname()%>">
					          <td class="TableData" nowrap>
						          &nbsp;<input type="checkbox" name="<%=inputName%>" id="<%=subName%>" value="<%=subName%>" ><img src="<%=contextPath%>/subsys/inforesource/tree/imgs/node_lv2.jpg" width=15 height=15><label for=""><%= typeList.get(i).getcNname()%></label>							          					          
					          </td>
	               </tr>
	               <%
	                	if(typeList.get(i).getValues()!=null && typeList.size()>0){
	                	   for(int j=0; j<typeList.get(i).getValues().size(); j++){
	                	     String subValue = subName + "_V" + typeList.get(i).getValues().get(j).getSeqId();
	                	  %>
	                	  	<tr class="TableData" title="<%=typeList.get(i).getValues().get(j).getValueName() %>">
			   		             <td nowrap>
				                   &nbsp;&nbsp;&nbsp;<input type="checkbox" name="<%=inputName%>" id="<%=subValue%>" value="<%=subValue%>" onClick="">
				                   <img src="<%=contextPath%>/subsys/inforesource/tree/imgs/node_lv3.jpg" width=15 height=15> <label><%=typeList.get(i).getValues().get(j).getValueName() %></label>
				                 </td>
	                     </tr>
	                	  <%
	                	}
	                	}
	                %>
	              <%} if(i==typeList.size()-1){%>
	              		</td></table>
	              <%}%>
   		  <%}
   		}
   %>       
    </table>      
</form>
</body>
<script type="text/javascript">
	//给每个checkbox加入监听，父节点不加
	var obj = document.getElementsByTagName("input");
	for(var i=0; i<obj.length; i++){
    var name = obj[i].className;				
    if(obj[i].type =="checkbox"){//给checkbox加入监听 
			if(obj[i].className == "parent"){//父节点不加监听
			  continue;
			}
      if(window.addEventListener){        
        obj[i].addEventListener("click", myFunc, false);
      }else if(window.attachEvent){       
        obj[i].attachEvent("onclick", myFunc);   
      }			
	}
}

function myFunc(event){//兼容浏览器 
  event = window.event ||event;
  var k =event.srcElement || event.target;	
  var v = k.value;
  var pname = k.name;   
  document.getElementById(pname).checked = true;//选中顶级父节点，遍历所有的父节点，
     //把父节点加入show中
		  var newIds = "";
			 $$(".parent").each(function(obj){
					if(obj.checked){
					  newIds += obj.value +",";
					}
			 });
		 $("show").value = newIds.substring(0, newIds.lastIndexOf(",")==-1?0:newIds.lastIndexOf(","));		
	  var pvalue = v.substring(0,v.lastIndexOf("_"));
	  var p = document.getElementById(pvalue);
	  if(p){
	    p.checked = true;
	  }
	  var n = k.name;          //子元素的name属性值
	  var ischeck = k.checked; //true标示操作是选择，false标示操作时不选
	  var boj = document.getElementsByName(n);			   
	   if(boj){
		   for(var j=0; j<boj.length; j++){
				var sn = boj[j].value;
				var juge = sn.indexOf(v);						
				if(juge != -1){
				  if(ischeck){  //如果是选择，全选子元素下的元素
				    boj[j].checked = true;
				  }else{        //如果是不选择，删除子元素下的元素
				    boj[j].checked = false;
				  }
				}
		   }
	   }
}
</script>
</html>