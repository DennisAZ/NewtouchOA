/* ----------------------------- */
/* -------   销售管理   --------- */
/* ----------------------------- */

/**
 *	提成计算
 */
/*function getSalBack(prodObj,ropPrc,ropTax,prodNum){
	var prodPrc = prodObj.wprSalePrc>0?prodObj.wprSalePrc:0;
	var shipAmt = ropPrc*prodNum*(ropTax>0?(1+parseFloat(ropTax)):1);
	var salBack = 0;
	if(ropPrc>0&&prodNum>0){
		if(ropPrc>=prodPrc){
			salBack=shipAmt*(prodOb.wprNormalPer?prodOb.wprNormalPer:0)+(ropPrc-prodPrc)*prodNum*(ropTax>0?(1+parseFloat(ropTax)):1)*(prodObj.wprOverPer?prodObj.wprOverPer:0);
		}
		else{
			salBack=shipAmt*(prodObj.wprLowPer?prodObj.wprLowPer:0);
		}
	}
	return salBack;
}*/

/* 
	弹出层录入,编辑
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
			titleTxt="新建订单合同";
			height=600;
			url="orderAction.do?op=toNewOrder";
			break;
		case 11:
			titleTxt="编辑订单合同";
			height=580;
			url="orderAction.do?op=toUpdOrder&ordId="+arg1;
			break;
		case 12:
			titleTxt="修改交付信息";
			width=220;
			height = 180;
			url="orderAction.do?op=altSodState&code="+arg1;
			break;
		case 13:
			titleTxt="撤销审核";
			width=250;
			height = 140;
			url="sal/unLock.jsp?ordId="+arg1;
			break;
			
		case 3:
			titleTxt="新建回款计划";
			url="paidAction.do?op=toNewPaidPlan";
			height=300;
			break;
		case 31:
			titleTxt="编辑回款计划";
			height=300;
			url="paidAction.do?op=toUpdPaidPlan&paidId="+arg1;
			break;
		case 32:
			titleTxt="查看回款计划";
			height=240;
			hasScroll = true;
			url="paidAction.do?op=getPaidPlan&planId="+arg1;
			break;
			
		case 4:
			titleTxt="新建回款记录";
			url="paidAction.do?op=toNewPaidPast";
			height=330;
			break;
		case 41:
			//执行回款操作时弹出(未使用)
			titleTxt="回款记录录入"+tips;
			url="paidAction.do?op=toNewPaidPast&paidId="+arg1;
			height=330;
			break;
		case 42:
			titleTxt="编辑回款记录";
			url="paidAction.do?op=toUpdPaidPast&paidId="+arg1;
			height=330;
			break;
		case 43:
			titleTxt="查看回款记录";
			height=300;
			hasScroll = true;
			url="accountAction.do?op=getAccIn&accInId="+arg1;
			break;
			
		case 5:
			height=510;
			titleTxt="添加产品";
			url="prodAction.do?op=toNewPro";
			break;
		case 51:
			height=580;
			titleTxt="编辑产品";
			url="prodAction.do?op=toUpdPro&wprId="+arg1;
			break;
		case 52:
			height=265;
			width='half';
			titleTxt="添加运费标准";
			url="prodAction.do?op=toSaveProdTrans&prodId="+arg1;
			break;
		case 53:
			height=265;
			width='half';
			titleTxt="编辑运费标准";
			url="prodAction.do?op=toUpdProdTrans&ptrId="+arg1+"&prodId="+arg2;
			break;
		case 54:
			height=150;
			width='half';
			titleTxt="添加提成率";
			url="prodAction.do?op=toSaveProdSalBack&prodId="+arg1;
			break;
		case 55:
			height=150;
			width='half';
			titleTxt="编辑提成率";
			url="prodAction.do?op=toUpdProdSalBack&psbId="+arg1+"&prodId="+arg2;
			break;

		case 6:
			height=298;
			titleTxt="编辑出库单"+tips;
			url="wwoAction.do?op=toUpdateWwo&wwoId="+arg1;
			break;
		
		case 7:
			height=300;
			titleTxt="开票记录录入"+tips;
			url="invAction.do?op=toNewInv";
			break;
		case 71:
			height=300;
			titleTxt="编辑开票记录"+tips;
			url="invAction.do?op=toUpdInv&invId="+arg1;
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
		case 2:
			titleTxt="删除回款记录";
			break;
		case 3:
			titleTxt="删除开票记录";
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
		case 8:
			titleTxt="删除运费标准";
			break;
		case 9:
			titleTxt="删除发货记录";
			break;
		case 10:
			titleTxt="删除提成率";
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
		case 2:
			delObj[0]="该回款记录";
			delObj[1]="paidAction.do?op=deletePaidPast&paidId="+id;
			break;
		case 3:
			delObj[0]="该开票记录";
			delObj[1]="invAction.do?op=deleteInv&invId="+id;
			break;
		case 4:
			delObj[0]="该订单";
			delObj[1]="orderAction.do?op=deleteOrd&id="+id;
			break;
		case 6:
			delObj[0]="该产品";
			delObj[1]="prodAction.do?op=delWmsPro&wprId="+id;
			break;
	   	case 7:
			delObj[0]="该出库单";
			delObj[1]="wwoAction.do?op=delWwo&wwoId="+id;
			break;
		case 8:
			delObj[0]="该运费标准";
			delObj[1]="prodAction.do?op=delProdTrans&ptrId="+id;
			break;
		case 9:
			delObj[0]="该发货记录";
			delObj[1]="orderAction.do?op=delProdShipment&pshId="+id;
			break;
		case 10:
			delObj[0]="该提成率";
			delObj[1]="prodAction.do?op=delProdSalBack&psbId="+id;
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