<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>发货记录列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		//标签跳转链接
		function reloadTab(){
			self.location.href = "orderAction.do?op=toListProdShipment";
		}
		
		function chooseCus(){
			addDivBrow(2);
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.pshId;
			var dblFunc="descPop('orderAction.do?op=showShipmentDesc&pshId="+dataId+"')";

			var pshNum="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.pshNum+"</a>";
			var pshOrd="";
			if(obj.pshOrder != undefined){
				var relFunc="descPop('orderAction.do?op=showOrdDesc&code="+obj.pshOrder.sodCode+"')";
                pshOrd="<a href='javascript:void(0)' title=\""+obj.pshOrder.sodNum+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.pshOrder.sodTil+"</a>";
			}
			var funcCol = "<img src='crm/images/content/detail.gif' alt='查看详细' style='cursor:pointer' onclick=\""+dblFunc+"\"/>&nbsp;&nbsp;<img onClick=\"ordDelDiv(9,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [pshNum,pshOrd,obj.pshConsignee,obj.pshAddr, obj.pshProdAmt, obj.pshSalBack, obj.pshExpress, obj.pshAmt, obj.pshDeliveryDate, obj.pshShipper, obj.pshRemark, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listProdShipment";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"编号"},
				{name:"对应订单"},
				{name:"收货方",align:"left"},
				{name:"收货地址"},
				{name:"发货金额",align:"right",renderer:"money"},
				{name:"提成金额",align:"right",renderer:"money"},
				{name:"物流公司"},
				{name:"总运费",align:"right",renderer:"money"},
				{name:"发货日期",renderer:"date"},
				{name:"发货人"},
				{name:"备注",isSort:false},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("pshipmentListTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;销售管理 > 发货记录
            </div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                        	<div id="tabType1" onClick="reloadTab()" class="tabTypeWhite">发货记录</div>
                        </div>
                    </th>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
           	<div id="listContent">
			 	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                        <input type="text" id="filter" name="filter" style="display:none"/>
                        客户名称：<input name="corName" class="inputSize2 inputBoxAlign" type="text" onBlur="autoShort(this,100)"/>&nbsp;&nbsp;
                        订单号：<input name="ordNum" class="inputSize2 inputBoxAlign" type="text" onBlur="autoShort(this,150)"/>&nbsp;&nbsp;
                        收货方：<input id="cusName" name="cusName" class="inputSize2 inputBoxAlign" type="text"/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    		 <input id="cusId" name="cusId" type="text" style="display:none"/>
                        物流公司：<c:if test="${!empty expList}"><select name="expName" class="inputBoxAlign">
                         			<option  value="">请选择</option>
                                    <c:forEach var="express" items="${expList}">
                         				<option value="${express.typName}">${express.typName}</option>
                         			</c:forEach>
                             </select></c:if>
                          	<c:if test="${empty expList}"><select class="inputBoxAlign" disabled><option value="">未添加</option></select></c:if>&nbsp;
                         <input id="searButton" class="butSize3 inputBoxAlign" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;&nbsp;
                   </form>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
  		</div> 
	</div>
  </body>
</html>
