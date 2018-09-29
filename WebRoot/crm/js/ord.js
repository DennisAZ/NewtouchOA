/* ----------------------------- */
/* -------   销售管理   --------- */
/* ----------------------------- */

/* 
	弹出层录入,编辑
	arg1:执行回款新建记录是传入回款id
		 编辑回款时回款id
		 
	arg2:编辑产品时传入count
*/
function ordPopDiv(n,arg1,arg2){
	var width;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	var url;
	
	switch(n){
		case 1:
			titleTxt="订单信息录入"+tips;
			height=680;
			url="orderAction.do?op=toNewOrder";
			break;
		case 11:
			titleTxt="编辑订单"+tips;
			height=730;
			url="orderAction.do?op=toUpdOrder&ordId="+arg1;
			break;
		case 12:
			titleTxt="修改交付信息";
			width=220;
			height = 180;
			url="orderAction.do?op=altSodState&code="+arg1;
			break;
			
		case 3:
			titleTxt="回款计划录入"+tips;
			url="paidAction.do?op=toNewPaidPlan";
			height=280;
			break;
		case 31:
			titleTxt="编辑回款计划"+tips;
			height=280;
			url="paidAction.do?op=toUpdPaidPlan&paidId="+arg1;
			break;
		case 32:
			titleTxt="查看回款计划";
			height=240;
			hasScroll = true;
			url="paidAction.do?op=getPaidPlan&planId="+arg1;
			break;
			
		case 4:
			titleTxt="回款记录录入"+tips;
			url="paidAction.do?op=toNewPaidPast";
			height=170;
			break;
		case 41:
			//执行回款操作时弹出(未使用)
			titleTxt="回款记录录入"+tips;
			url="paidAction.do?op=toNewPaidPast&paidId="+arg1;
			height=170;
			break;
		case 42:
			titleTxt="查看回款记录";
			height=300;
			hasScroll = true;
			url="accountAction.do?op=getAccIn&accInId="+arg1;
			break;
			
		case 5:
			height=685;
			titleTxt="产品信息录入"+tips;
			url="wmsManageAction.do?op=toNewPro";
			break;
		case 51:
			height=800;
			titleTxt="编辑产品信息"+tips;
			url="wmsManageAction.do?op=toUpdPro&wprId="+arg1+"&count="+arg2;
			break;

		case 6:
			height=298;
			titleTxt="编辑出库单"+tips;
			url="wwoAction.do?op=toUpdateWwo&wwoId="+arg1;
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}

/* 
	弹出层删除
*/
function ordDelDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除回款计划";
			break;
		case 2://未使用
			titleTxt="删除回款记录";
			break;
		case 4:
			titleTxt="删除订单";
			break;
		case 6:
			titleTxt="删除产品信息";
			break;
		case 7:
			titleTxt="删除出库单";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}
//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该回款计划";
			delObj[1]="paidAction.do?op=deletePaidPlan&paidCode="+id;
			break;
		case 2://未使用
			delObj[0]="该回款记录";
			delObj[1]="customAction.do?op=delContact&id="+id;
			break;
		case 4:
			delObj[0]="该订单";
			delObj[1]="orderAction.do?op=deleteOrd&id="+id;
			break;
		case 6:
			delObj[0]="该产品";
			delObj[1]="wmsManageAction.do?op=delWmsPro&wprId="+id;
			break;
	   	case 7:
			delObj[0]="该出库单";
			delObj[1]="wwoAction.do?op=delWwo&wwoId="+id;
			break;
	}
	return delObj;
}

function completePaid(id){
	if(confirm("是否确定已回款？")){
		var url = "paidAction.do";
		var pars = "op=completePaid&paidId="+id+"&ran=" + Math.random();
		new Ajax.Request(url, {
			method: 'get',
			parameters: pars,
			onSuccess: function(transport) {
				 var response = transport.responseText;
				 if(response == "1"){//更新成功
					$("toPayDiv"+id).hide();
					$("paidDiv"+id).show();
				 }
			},
			
			onFailure: function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		});
	}
	else{
		return;
	}
}

