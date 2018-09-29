<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header1.jsp" %>
<%
	String craftsId = request.getParameter("craftsId");
	String pro_name = request.getParameter("pro_name");
	String crafts_version = request.getParameter("crafts_version");
 %>
<html>
<head>
<title>生产工序列表</title>
<link type="text/css" rel="stylesheet" href="<%=contextPath %>/springViews/erp/produce/ECOTree.css" />

		<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
		<style>v\:*{ behavior:url(#default#VML);}</style> 			
		<style>
			.copy {
				font-family : "Verdana";				
				font-size : 10px;
				color : #CCCCCC;
			}
		</style>
		<script type="text/javascript" src="<%=contextPath %>/springViews/erp/produce/ECOTree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>


<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="prc.js"></script>

<script>
 function doInit99(){
    getProcess();
 }
 var arrPrc=new Array();
function getProcess(){
	var url = "<%=contextPath %>/SpringR/produce/getProcessById?craftsId=<%=craftsId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
				var  cfgs={
									id:item.id,
									name:item.name,
									processIndex:item.process_index
									}
						
						    var process=new Process(cfgs);
				arrPrc.push(process)
															
				});
			}
	   }
	 });
bubbleSort();
}

function bubbleSort (){
	for(var i=0;i<arrPrc.length-1;i++){
	for(var j=0;j<arrPrc.length-1-i;j++){
		if(arrPrc[j].processIndex>arrPrc[j+1].processIndex){
				var temp=arrPrc[j];
				arrPrc[j]=arrPrc[j+1];
				arrPrc[j+1]=temp;
			}
		}	
	}
	createEcoTree();
}
 var t = null;
function createEcoTree(){
			t = new ECOTree('t','sample2');						
				t.config.iRootOrientation = ECOTree.RO_LEFT;
				t.config.defaultNodeWidth = 130;//宽度
				t.config.defaultNodeHeight = 60;//高度
				t.config.iSubtreeSeparation = 25;
				t.config.iSiblingSeparation = 50;//上下之间的距离								
				t.config.linkType = 'B';
				t.config.useTarget = true;
				t.config.nodeFill = ECOTree.NF_GRADIENT;
				t.config.colorStyle = ECOTree.CS_LEVEL;
				t.config.levelColors = ["#966E00","#BC9400","#D9B100","#FFD700"];
				t.config.levelBorderColors = ["#FFD700","#D9B100","#BC9400","#966E00"];
				t.config.nodeColor = "#FFD700";
				t.config.nodeBorderColor = "#FFD700";
				t.config.linkColor = "#FFD700";
				for(var i=0;i<arrPrc.length;i++){
					if(i==0){
						t.add(arrPrc[i].processIndex,-1,arrPrc[i].name+"<div style='text-align:center;margin-top:10px;'><a href='processDetail.jsp?processId="+arrPrc[i].id+"'>查看</a>&nbsp;&nbsp;&nbsp;&nbsp<a href='processUpdate.jsp?processId="+arrPrc[i].id+"&craftsId=<%=craftsId%>&pro_name=<%=pro_name%>'>编辑</a>&nbsp;&nbsp<a href='#' onclick=\"deletesProcess('"+arrPrc[i].id+"')\">删除</a></div>",null,null,null,null);
					}else{
					if(i%6==0){
						t.add(arrPrc[i].processIndex,-1,arrPrc[i].name+"<div style='text-align:center;margin-top:10px;'><a href='processDetail.jsp?processId="+arrPrc[i].id+"'>查看</a>&nbsp;&nbsp;&nbsp;&nbsp<a href='processUpdate.jsp?processId="+arrPrc[i].id+"&craftsId=<%=craftsId%>&pro_name=<%=pro_name%>'>编辑</a>&nbsp;&nbsp<a href='#' onclick=\"deletesProcess('"+arrPrc[i].id+"')\">删除</a></div>",null,null,null,null);
					
					}
						t.add(arrPrc[i].processIndex,arrPrc[i-1].processIndex,arrPrc[i].name+"<div style='text-align:center;margin-top:10px;'><a href='processDetail.jsp?processId="+arrPrc[i].id+"'>查看</a>&nbsp;&nbsp;&nbsp;&nbsp<a href='processUpdate.jsp?processId="+arrPrc[i].id+"&craftsId=<%=craftsId%>&pro_name=<%=pro_name%>'>编辑</a>&nbsp;&nbsp<a href='#' onclick=\"deletesProcess('"+arrPrc[i].id+"')\">删除</a></div>",null,null,null,null);
					}
				}
								              		      																																																																																
																
				t.UpdateTree();

	}

 	function processAdd(){
	
		window.location.href='processAdd.jsp?craftsId=<%=craftsId%>&pro_name=<%=pro_name%>&crafts_version=<%=crafts_version%>';
	}
	function deletesProcess(processId){
	var url = "<%=contextPath %>/SpringR/produce/deleteProcess?processId="+processId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("删除成功");
		    window.location.reload();
		  }
		  else{
			   alert("删除失败"); 
		  }
	   }
	 });
	
	}
</script>
</head>
<body topmargin="5"  onload="doInit99()">

<div style="margin-top:40px;color:red;font-size:30px;text-align:center;">[<%=pro_name%>]的工序流程图</div>
<div><input  align="left" type="button" value="新建工序"  class="BigButton"   onclick="processAdd()"></div>
<div id="sample2" style="margin-top:100px;"></div>
</body>
</html>