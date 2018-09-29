//载入左侧菜单
function loadMenuIfm(){
	loadIfmSrc("menuIfrm","crm/recycle/recycleMenu.jsp");
}

/*
	删除,恢复数据
	type:标识是删除还是恢复
*/
function recAddDiv(n,type,code){
	var width=250;
	var height=136;
	var idPre="";
	var titleTxt="";
	switch(n){
		case 1:
			if(type=="del")
				titleTxt="删除客户";
			if(type=="rec")
			   	titleTxt="恢复客户";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=1";
			break;
			
		case 3:
			if(type=="del")
			   titleTxt="删除联系人";
			if(type=="rec")
			   titleTxt="恢复联系人";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=3";
			break;
			
		case 4:
			if(type=="del")
			   titleTxt="删除销售机会";
			if(type=="rec")
			   titleTxt="恢复销售机会";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=4";
			break;
		
		case 5:
			if(type=="del")
			   titleTxt="删除报价记录";
			if(type=="rec")
			   titleTxt="恢复报价记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=5";
			break;
		case 6:
			if(type=="del")
			   titleTxt="删除来往记录";
			if(type=="rec")
			   titleTxt="恢复来往记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=6";
			break;
		case 7:
			if(type=="del")
			   titleTxt="删除客户服务";
			if(type=="rec")
			   titleTxt="恢复客户服务";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=7";
			break;
		case 8:
			if(type=="del")
			   titleTxt="删除订单";
			if(type=="rec")
			   titleTxt="恢复订单";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=8";
			break;
		case 9:
			if(type=="del")
			   titleTxt="删除合同";
			if(type=="rec")
			   titleTxt="恢复合同";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=9";
			break;
		case 10:
			if(type=="del")
			   titleTxt="删除回款计划";
			if(type=="rec")
			   titleTxt="恢复回款计划";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=10";
			break;
		case 11:
			if(type=="del")
			   titleTxt="删除回款记录";
			if(type=="rec")
			   titleTxt="恢复回款记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=11";
			break;
		case 12:
			if(type=="del")
			   titleTxt="删除开票记录";
			if(type=="rec")
			   titleTxt="恢复开票记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=12";
			break;
	   	case 13:
			if(type=="del")
			   titleTxt="删除产品";
			if(type=="rec")
			   titleTxt="恢复产品";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=13";
			break;
	   	case 14:
			if(type=="del")
			   titleTxt="删除报告";
			if(type=="rec")
			   titleTxt="恢复报告";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=14";
			break;
	   	case 15:
			if(type=="del")
			   titleTxt="删除工作安排";
			if(type=="rec")
			   titleTxt="恢复工作安排";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=15";
			break;
		   
	   	case 16:
			if(type=="del")
			   titleTxt="删除消息";
			if(type=="rec")
			   titleTxt="恢复消息";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=16";
			break;
	   	case 17:
			if(type=="del")
			   titleTxt="删除入库单";
			if(type=="rec")
			   titleTxt="恢复入库单";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=17";
			break;
	   	case 18:
			if(type=="del")
			   titleTxt="删除出库单";
			if(type=="rec")
			   titleTxt="恢复出库单";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=18";
			break;
	  	case 19:
			if(type=="del")
			   titleTxt="删除仓库调拨";
			if(type=="rec")
			   titleTxt="恢复仓库调拨";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=19";
			break;
	   	case 20:
			if(type=="del")
			   titleTxt="删除盘点记录";
			if(type=="rec")
			   titleTxt="恢复盘点记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=20";
			break;
	   	case 21:
			if(type=="del")
			   titleTxt="删除库存流水";
			if(type=="rec")
			   titleTxt="恢复库存流水";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=21";
			break;
	  	case 22:
			if(type=="del")
			   titleTxt="删除项目";
			if(type=="rec")
			   titleTxt="恢复项目";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=22";
			break;
	  	case 23:
			if(type=="del")
			   titleTxt="删除子项目";
			if(type=="rec")
			   titleTxt="恢复子项目";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=23";
			break;
	  	case 24:
			if(type=="del")
			   titleTxt="删除项目工作";
			if(type=="rec")
			   titleTxt="恢复项目工作";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=24";
			break;
		 case 25:
			if(type=="del")
			   titleTxt="删除行动历史";
			if(type=="rec")
			   titleTxt="恢复行动历史";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=25";
			break;
	 	case 26:
			if(type=="del")
			   titleTxt="删除员工档案";
			if(type=="rec")
			   titleTxt="恢复员工档案";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=26";
			break;
	    case 27:
			if(type=="del")
				titleTxt="删除供应商";
			if(type=="rec")
			   	titleTxt="恢复供应商";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=27";
			break;
		case 28:
			if(type=="del")
				titleTxt="删除供应商联系人";
			if(type=="rec")
			   	titleTxt="恢复供应商联系人";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=28";
			break;	
		case 29:
			if(type=="del")
				titleTxt="删除询价单";
			if(type=="rec")
			   	titleTxt="恢复询价单";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=29";
			break;	
		case 30:
			if(type=="del")
				titleTxt="删除采购单";
			if(type=="rec")
			   	titleTxt="恢复采购单";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=30";
			break;	
		 case 32:
			if(type=="del")
			   titleTxt="删除付款计划";
			if(type=="rec")
			   titleTxt="恢复付款计划";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=32";
			break;
		case 33:
			if(type=="del")
			   titleTxt="删除付款记录";
			if(type=="rec")
			   titleTxt="恢复付款记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=33";
			break;
		case 34:
			if(type=="del")
			   titleTxt="删除收票记录";
			if(type=="rec")
			   titleTxt="恢复收票记录";
			url="customAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=34";
			break;
		case 35:
			if(type=="del")
			   titleTxt="删除联系电话";
			url="phoneListAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=35";
			break;	
		case 36:
			if(type=="del")
			   titleTxt="删除联系地址";
			url="caseAction.do?op=recConfirm&type="+type+"&code="+code+"&delType=36";
			break;	
		case 37:
		   if(type=="del")
			   titleTxt="批量删除联系电话";
			url="phoneListAction.do?op=recBatchConfirm&type="+type+"&code="+code+"&delType=37";
			break;			
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,false);
}