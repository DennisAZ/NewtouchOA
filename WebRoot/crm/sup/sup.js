/*************************** 供应商 **************************************/

/* 
	新建、编辑弹出层
*/
function supPopDiv(n,arg,arg1){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasTitle = true;
	var hasScroll = false;
	var hasCover = true;
	var url;			
			
	switch(n){
		case 1:
		   	titleTxt="添加供应商";
		   	url="supAction.do?op=toSaveSup";
			height=520;
		   	break;
		case 11:
		    titleTxt="修改供应商";
		    url="supAction.do?op=toSaveSup&supId="+arg;
			height=520;
		    break;
		case 2:
			titleTxt="添加采购单";
			url="supAction.do?op=toSavePurOrd";
			height=420;
			break;
		case 3:
			titleTxt="修改采购单";
			url="supAction.do?op=toSavePurOrd&puoId="+arg;
			height=420;
			break;
		case 4:
			titleTxt="修改采购单状态";
			url="supAction.do?op=toUpdState&puoId="+arg;
			height=110;
			width=290;
			break;
		case 5:
			titleTxt="添加库存";
			url="supAction.do?op=toSaveProdStore";
			height=145;
			break;
		case 6:
			titleTxt="编辑库存";
			url="supAction.do?op=toSaveProdStore&pstId="+arg;
			height=145;
			break;
		case 7:
			titleTxt="添加入库单";
			url="supAction.do?op=toSaveProdIn";
			height=270;
			break;
		case 8:
			titleTxt="编辑入库单";
			url="supAction.do?op=toSaveProdIn&pinId="+arg;
			height=270;
			break;
		case 9:
			titleTxt="添加出库单";
			url="supAction.do?op=toSaveProdOut";
			height=270;
			break;
		case 10:
			titleTxt="修改出库单";
			url="supAction.do?op=toSaveProdOut&pouId="+arg;
			height=270;
			break;
		case 12:
			titleTxt="添加供应商产品";
			url="supAction.do?op=toSaveSupProd&supId="+arg+"&isIfrm=1";
			height=300;
			break;
		case 13:
			titleTxt="修改供应商产品";
			url="supAction.do?op=toSaveSupProd&supId="+arg+"&isIfrm=1&spId="+arg1;
			height=300;
			break;	
		case 14:
			titleTxt="添加付款记录";
			url="supAction.do?op=toSaveSupPaidPast&supId="+arg+"&isIfrm=1";
			height=290;
			break;
		case 15:
			titleTxt="修改付款记录";
			url="supAction.do?op=toSaveSupPaidPast&supId="+arg+"&isIfrm=1&sppId="+arg1;
			height=290;
			break;
		case 16:
			titleTxt="添加收票记录";
			url="supAction.do?op=toSaveSupInvoice&supId="+arg+"&isIfrm=1";
			height=390;
			break;
		case 17:
			titleTxt="修改收票记录";
			url="supAction.do?op=toSaveSupInvoice&supId="+arg+"&isIfrm=1&suiId="+arg1;
			height=290;
			break;	
	}
	createPopWindow(idPre,titleTxt,url,width,height,hasCover,hasScroll,hasTitle);
}

function supDelDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	switch(n){
		case 1:
			titleTxt="删除供应商";
			break;
		case 2:
			titleTxt="删除采购单";
			break;
		case 3:
			titleTxt="删除库存";
			break;
		case 4:
			titleTxt="删除入库单";
			break;
		case 5:
			titleTxt="删除出库单";
			break;
		case 6:
			titleTxt="删除供应商产品";
			break;
		case 7:
			titleTxt="删除付款记录";
			break;
		case 8:
			titleTxt="删除收票记录";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}
//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该供应商";
			delObj[1]="supAction.do?op=deleteSup&supId="+id;
			break;
		case 2:
			delObj[0]="该采购单";
			delObj[1]="supAction.do?op=deleteContRec&puoId="+id;
			break;
		case 3:
			delObj[0]="该库存";
			delObj[1]="supAction.do?op=deleteProdStore&pstId="+id;
			break;
		case 4:
			delObj[0]="该入库单";
			delObj[1]="supAction.do?op=delProdIn&pinId="+id;
			break;
		case 5:
			delObj[0]="该出库单";
			delObj[1]="supAction.do?op=delProdOut&pouId="+id;
			break;
		case 6:
			delObj[0]="该供应商产品";
			delObj[1]="supAction.do?op=delSupProd&spId="+id;
			break;
		case 7:
			delObj[0]="该付款记录";
			delObj[1]="supAction.do?op=delSupPaidPast&sppId="+id;
			break;
		case 8:
			delObj[0]="该收票记录";
			delObj[1]="supAction.do?op=delSupInvoice&suiId="+id;
			break;
	}
	return delObj;
}

function supCancleDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	switch(n){
		case 1:
			titleTxt="撤销该入库单";
			break;
		case 2:
			titleTxt="撤销该出库单";
			break;	
	}
	createCancleConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}

//返回撤销实体名称，url数组
function getCanObj(canType,id){
	var canObj = new Array();
	switch(parseInt(canType)){
		case 1:
			canObj[0]="该入库单";
			canObj[1]="supAction.do?op=cancleProdIn&pinId="+id;
			break;
		case 2:	
			canObj[0]="该出库单";
			canObj[1]="supAction.do?op=cancleProdOut&pouId="+id;
			break;
	}
	return canObj;
}

