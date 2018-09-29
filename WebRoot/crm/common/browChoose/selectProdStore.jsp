<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择库存</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.pstId;
			dblFunc="chooseProdStore('"+obj.pstId+"','"+escape(obj.pstName)+"')";
			var pstName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.pstName+"</a>";
			datas = [pstName, obj.wmsProduct.wprName, obj.storeType?obj.storeType.typName:"",obj.pstCount];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listProdStore";
			var loadFunc = "loadList";
			var cols=[
				{name:"库存名称",align:"left"},
				{name:"商品名称",align:"left"},
				{name:"仓库类型"},
				{name:"库存数量"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selProdStoreListTab","dataList");
    	createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-130,5);
		}
	  </script>
  </head>
    <body>
  	<div class="browLayer">
    	<div class="listSearch">
        	<form id="searchForm" onSubmit="loadList();return false;">
                        商品名称：<input class="inputSize2 inputBoxAlign" type="text" name="wprName" onBlur="autoShort(this,100)"/>&nbsp;&nbsp;&nbsp;
                        仓库：<c:if test="${!empty typList}">
                      			<select name="storeType" class="inputBoxAlign inputSize2" style="width:100px;">
	                      			<option value="">请选择</option>
	                      			<c:forEach var="storeType" items="${typList}">
	                      				<option value="${storeType.typId}">${storeType.typName}</option>
	                      			</c:forEach>
                      			</select>
                      	     </c:if>
                      	     <c:if test="${empty typList}">
                      	        <select id="storeType" class="inputBoxAlign"  disabled>
                             		<option value="">未添加</option>
                             	</select>
                      	     </c:if>&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>
