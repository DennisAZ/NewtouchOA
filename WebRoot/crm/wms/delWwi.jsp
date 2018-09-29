<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>撤销操作</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="cache-control" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
		.reTxt{
			padding:2px 4px 14px 38px;
			text-align:left;
			background:url(images/content/bigAlert.gif) no-repeat;
		}
		.resConfirm {
			padding:12px; 
			padding-top:18px;
			padding-right:0px;
		}
		.resConfirm form{
			margin:0px;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
		function check(){
			waitSubmit("dosave","正在执行...");
			waitSubmit("cancel2");
			return $("form").submit();
		}
	</script>
  </head>
  
  <body>
  	<logic:notEmpty name="wwiId">
  	<div class="resConfirm"> 
      	<form action="wmsManageAction.do" id="form" method="post">
          	<input type="hidden" name="op" value="delWwiDesc">
          	<input type="hidden" name="wwiId" value="${wwiId}">
          	<div class="reTxt">
                <div style="padding-bottom:5px">确认撤销入库吗？</div>
                产品库存将会恢复到入库前状态
          	</div>
          
          	<button class ="butSize1" id="dosave" type="button" onClick="check()">确认撤销</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class ="butSize1" id="cancel2" onClick="cancel()">取消</button>
	  	</form>
    </div> 
    </logic:notEmpty>
    <logic:notEmpty name="wwoId">
  	<div class="resConfirm"> 
    	<form action="wwoAction.do" id="form" method="post">
	  		<input type="hidden" name="op" value="cancelWwoDesc">
	  		<input type="hidden" name="wwoId" value="${wwoId}">
          	<div class="reTxt">
            	<div style="padding-bottom:5px">确认撤销出库吗？</div>
            	产品库存将会恢复到出库前状态
         	</div>
	  		<button class ="butSize1" id="dosave" type="button" onClick="check()">确认撤销</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class ="butSize1" id="cancel2" onClick="cancel()">取消</button>
	  </form>
    </div> 
    </logic:notEmpty>
	<logic:notEmpty name="wchId">
  	<div class="resConfirm"> 
      	<form action="wwoAction.do" id="form" method="post" name="">
	  		<input type="hidden" name="op" value="cancelWchDesc">
	  		<input type="hidden" name="wchId" value="${wchId}">
	  		<logic:equal value="3" name="wmsChange" property="wchState">
	  			<input type="hidden" name="wchState" value="${wmsChange.wchState}">
	  		</logic:equal>
	  		<div class="reTxt">
                <div style="padding-bottom:5px">确认撤销调拨吗？</div>
            	产品库存将会恢复到调拨前状态
         	</div>
	  		<button class ="butSize1" id="dosave" type="button" onClick="check()">确认撤销</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<button class ="butSize1" id="cancel2" onClick="cancel()">取消</button>
	  </form>
    </div> 
    </logic:notEmpty>
	<logic:notEmpty name="wmcId">
  	<div class="resConfirm"> 
     	<form action="wwoAction.do" id="form" method="post" name="">
	  		<input type="hidden" name="op" value="cancelWmc">
	  		<input type="hidden" name="wmcId" value="${wmcId}">
            <div class="reTxt">
                <div style="padding-bottom:5px">确认撤销盘点吗？</div>
            	产品库存将会恢复到盘点前状态
         	</div>
	  		<button class ="butSize1"  id="dosave" type="button" onClick="check()">确认撤销</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<button class ="butSize1" id="cancel2" onClick="cancel()">取消</button>
	  	</form>
    </div> 
    </logic:notEmpty>
	<logic:notEmpty name="wchOut">
  	<div class="resConfirm"> 
      	<form action="wwoAction.do" id="form" method="post" name="">
	  		<input type="hidden" name="op" value="cancelWchOut">
	  		<input type="hidden" name="wchId" value="${wchOut}">
	  		<div class="reTxt">
            	<div style="padding-bottom:5px">确认撤销出库确认吗？</div>
            	产品库存将会恢复到出库前状态
         	</div>
	  		<button class ="butSize1"  id="dosave" type="button" onClick="check()">确认撤销</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<button class ="butSize1" id="cancel2" onClick="cancel()">取消</button>
	  	</form>
    </div> 
    </logic:notEmpty>
	</body>
</html>
