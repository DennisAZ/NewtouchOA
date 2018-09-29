<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>   
    <title>选择产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="StyleSheet" href="css/dtree.css" type="text/css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
			overflow-y:hidden;
		}
		#prodSFormLayer {
			text-align:left;
			padding-bottom:10px;
			border-bottom:#fba01f 1px solid
		}
		#prodSFormLayer table {
			border:0;
			width:90%;
			text-align:left;
			background:#FFFFCC;
		}
		#prodSFormLayer table th{
			padding:4px;
			color:#999;
			font-weight:normal;
		}
		#prodSFormLayer form {
			 margin:5px; 
			 padding:0 0 5px 0;
		}
		#searchResult ul {
			padding:0px;
			margin:0px;
			margin-left:-20px;
			*margin-left:15px; 
			list-style-type:none;
		}
		#prodTreeLayer {
			text-align:left;
			padding:15px 5px 5px 5px;
		}
		/*a {
			zoom:1;
		}*/
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="js/choosePro.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
	function check(){
		var obj=$("pName");
		if(obj==null||obj.value==""){
			alert("请输入查询条件");
			return false;
		}else{
			return $("prodSForm").submit();
		}
	}
	var prodTree = new dTree('prodTree');
	prodTree.config.folderLinks=true;
	prodTree.config.showRootIcon = false;
	//id, pid, name, url, title, target, icon, iconOpen, open		
	prodTree.add(0,-1,'所有产品','','所有产品','_parent');
	</script>
  </head>
 
  <body>
  	<div style="padding:5px; text-align:left;">
        <div id="prodSFormLayer">
            <form id="prodSForm" action="prodAction.do" method="post">
                <input type="hidden" name="op" value="prodSearch">
                <input type="hidden" name="type" value="${type}" />
                查询产品<span class="gray">&nbsp;(可按产品名称/编号查询)</span><br/>
                <input type="text" class="inputSize2 inputBoxAlign" style="width:70%" id="pName" name="pName" value="${pName}"/>
                <input type="button" style="width:50px" value="查询" class="butSize1 inputBoxAlign" onClick="check()">
            </form>
            <c:if test="${!empty searProdList}">
            <table class="normal" cellpadding="0" cellspacing="0" >
                <tr>
                    <th><img id="hideImg" class="imgAlign" src="images/content/hide.gif" onClick="showHide('searchResult','hideImg')" style="cursor:pointer;" alt="点击收起"/>&nbsp;查询结果：</th>
                </tr>
                <tr>
                    <td>
                    <div id="searchResult">
                        <ul>
                        <c:forEach var="prod" items="${searProdList}">
                            <li>
                            &nbsp;
                            <a href="javascript:void(0);" onClick="tbladdrow('ordDesc','${prod.wprId}','<c:out value="${prod.wprName}"/>','<c:out value="${prod.wprCode}"/>','<c:out value="${prod.typeList.typName}"/>','<fmt:formatNumber value="${prod.wprSalePrc}" pattern="#0.00"/>','','','${type}');return false;"><img src="images/content/ball.gif" style="vertical-align:middle; border:0px"/>&nbsp;${prod.wprName}<c:if test="${!empty prod.typeList}">[${prod.typeList.typName}]</c:if></a>
                            </li>
                        </c:forEach>
                        </ul>
                    </div>
                    </td>
                </tr>
            </table>
            </c:if>
        </div>
  		<div id="prodTreeLayer">
        <c:if test="${!empty prodListNoType}">
        <script type="text/javascript">		
        prodTree.add('noCode','','(未分类)','','未分类','_parent');
        </script>
            <c:forEach var="prodNoType" items="${prodListNoType}">
            <script type="text/javascript">	
                var id='noCode';
                var id1='pro${prodNoType.wprId}';
                var name='<c:out value="${prodNoType.wprName}"/>';
                var typeName='<c:out value="${prodNoType.typeList.typName}"/>';
                if(typeName!=""){ name=name+"["+typeName+"]"; }
                var aLink="javascript:tbladdrow('ordDesc','${prodNoType.wprId}','<c:out value="${prodNoType.wprName}"/>','<c:out value="${prodNoType.wprCode}"/>','<c:out value="${prodNoType.typeList.typName}"/>','<fmt:formatNumber value="${prodNoType.wprSalePrc}" pattern="#0.00"/>','','','${type}');";
                if(id1!=""){ prodTree.add(id1,id,name,aLink,name,'_parent'); }
            </script>
            </c:forEach>
        </c:if>
        <c:if test="${!empty prodTypeList}">
            <c:forEach var="pt" items="${prodTypeList}">
                <script type="text/javascript">
                var p='orgimg/folder.gif';
                var pi='orgimg/folderopen.gif';	
                prodTree.add('${pt.wptId}','${pt.wmsProType.wptId}','(<c:out value="${pt.wptName}"/>)','','<c:out value="${pt.wptName}"/>','_parent',p,pi);
                </script>
                <c:forEach var="wp" items="${pt.wmsProducts}">
                <script type="text/javascript">	
                    var id='${wp.wmsProType.wptId}';
                    var id1='pro${wp.wprId}';
                    var name='<c:out value="${wp.wprName}"/>';
                    var typeName='<c:out value="${wp.typeList.typName}"/>';
                    if(typeName!=""){ name=name+"["+typeName+"]"; }
                    var aLink="javascript:tbladdrow('ordDesc','${wp.wprId}','<c:out value="${wp.wprName}"/>','<c:out value="${wp.wprCode}"/>','<c:out value="${wp.typeList.typName}"/>','<fmt:formatNumber value="${wp.wprSalePrc}" pattern="#0.00"/>','','','${type}');";
                    if(id1!=""){ prodTree.add(id1,id,name,aLink,name,'_parent'); }
                </script>
                </c:forEach>
            </c:forEach>
        </c:if>
        <script type="text/javascript">
            document.write(prodTree);
            prodTree.openAll();
        </script>
        </div>
        <c:if test="${!empty prodTypeList}">
            <c:if test="${!empty prodListNoType}">
                <div class="dataList title blue bold" style="padding-top:30px">${msg}</div>
            </c:if>
        </c:if>
	</div>
  </body>
</html>
