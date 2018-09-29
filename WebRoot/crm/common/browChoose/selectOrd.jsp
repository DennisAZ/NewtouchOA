<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>选择订单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript">    
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="chooseOrd('"+obj.orderId+"','"+escape(obj.orderTitle)+"','"+escape("${LOGIN_USER.userName}")+"','"+"${LOGIN_USER.seqId}"+"','"+escape(obj.erpOrderCus.cusName)+"','"+"${cusId}"+"')";
			var ordTil="<a href='javascript:void(0)' onclick="+dblFunc+">"+obj.orderTitle+"</a>";
           	var ordCus="";
           	if(obj.erpOrderCus != undefined){
                 ordCus="<span title='"+obj.erpOrderCus.cusName+"'>"+obj.erpOrderCus.cusName+"</span>";
            }
			datas = [ordTil, obj.orderCode, ordCus,obj.signDate ,obj.salesPerson];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderCusAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listOrdersToChoose";
			pars.cusId = "${cusId}";
			pars.cusName="${cusName}";
			var loadFunc = "loadList";
			var cols=[
				{name:"订单主题",align:"left"},
				{name:"订单号",align:"left"},
				{name:"客户名称",align:"left"},
				{name:"签单日期",renderer:"date"},
				{name:"签单人"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selOrdListTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-60,5);
		}
  	</script>
  </head>
  
  <body>
  	<div class="browLayer">
        <div class="listSearch">
            <form id="searchForm" onSubmit="loadList();return false;">
                 主题：<input class="inputSize2 inputBoxAlign" type="text" id="ordtil" name="ordTil" onBlur="autoShort(this,50)"/>&nbsp;&nbsp;
                 订单号：<input class="inputSize2 inputBoxAlign" type="text" id="ordNum" name="ordNum" onBlur="autoShort(this,50)"/>				
                <input class="inputBoxAlign butSize3" type="submit" value="查询"/>&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>
