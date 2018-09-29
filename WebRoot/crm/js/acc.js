/* ------------------------ */
/* ------- 财务管理 -------- */
/* ------------------------ */

/* 
	弹出层录入,编辑
*/
function addDivNew(n,arg1,arg2){
	var width=580;
	var height=510;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	
	switch(n){
		case 1:
			height=300;
			titleTxt="添加账户"+tips;
			url="account/newAcc.jsp";
			break;
		case 2:
			height=260;
			width=500;
			titleTxt="查看账户";
			hasScroll = true;
			url="accountAction.do?op=accDesc&acoId="+arg1;
			break;
		case 3:
			height=255;
			titleTxt="编辑账户"+tips;
			url="accountAction.do?op=goUpdateAcc&acoId="+arg1;
			break;
		case 4:
			height=330;
			titleTxt="添加入账记录"+tips;
			url="accountAction.do?op=gotoAddAccIn";
			break;
		case 5:
			height=350;
			titleTxt="确认入账"+tips;
			url="accountAction.do?op=gotoAddAccIn&paidId="+arg1;
			break;
		case 6:
			height=380;
			titleTxt="查看入账记录";
			hasScroll = true;
			url="accountAction.do?op=getAccIn&accInId="+arg1;
			break;
			
		case 7:
			height=330;
			titleTxt="添加出账记录"+tips;
			url="accountAction.do?op=gotoAddAccOut";
			break;
		case 8:
			height=380;
			titleTxt="确认出账"+tips;
			url="accountAction.do?op=gotoAddAccOut&paidId="+arg1;
			break;
		case 9:
			height=380;
			titleTxt="查看出账记录";
			hasScroll = true;
			url="accountAction.do?op=getAccOut&accOutId="+arg1;
			break;
		case 12:
			height=290;
			width=500;
			titleTxt="添加转账记录"+tips;
			url="accountAction.do?op=gotoAddAccTrans";
			break;
		case 13:
			height=260;
			width=500;
			titleTxt="查看转账记录";
			hasScroll = true;
			url="accountAction.do?op=getAccTransDesc&atrId="+arg1;	
			break;
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>待修改>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
		case 10:
			height=330;
			titleTxt="开票记录录入"+tips;
			url="invAction.do?op=forwardCreate&invoiceType=0";
			width=580;
			break;
		
		case 11:
			height=330;
			titleTxt="收票记录录入"+tips;
			url="invAction.do?op=forwardCreate&invoiceType=1";
			width=580;
			break;
		case 19:
			titleTxt="查看开票记录";
			height=250;
			width=500;
			hasScroll = true;
			url="invAction.do?op=showInv&id="+arg1;
			break;
		case 20:
			titleTxt="查看收票记录";
			height=260;
			width=520;
			hasScroll = true;
			url="salPurAction.do?op=showInv&id="+arg1;
			break;
		case 21:
			height=330;
			titleTxt="编辑开票记录"+tips;
			url="invAction.do?op=forwardMod&invoiceType=0&invId="+arg1;
			width=580;
			break;
		
		case 22:
			height=330;
			titleTxt="编辑收票记录"+tips;
			url="invAction.do?op=forwardMod&invoiceType=1&invId="+arg1;
			width=580;
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}

/* 
	弹出层删除
*/
function delDiv(n,code){
	width=250;
	height=136;
	var idPre="";
	var titleTxt="";
	
	switch(n){
		case 1:
			titleTxt="删除账户";
			url="account/delConfirm.jsp?code="+code+"&delType=1";
			break;
		case 2:
			titleTxt="撤销入账";
			url="account/delConfirm.jsp?code="+code+"&delType=2";
			break;
		case 3:
			titleTxt="撤销出账";
			url="account/delConfirm.jsp?code="+code+"&delType=3";
			break;
		 case 5:
			titleTxt="撤销转账";
			url="account/delConfirm.jsp?code="+code+"&delType=5";
			break;
		
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>待修改>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		case 3:
			titleTxt="删除开票记录";
			url="common/delConfirm.jsp?delType=3&code="+code;
			break;
		case 12:
			titleTxt="删除收票记录";
			url="common/delConfirm.jsp?delType=12&code="+code;
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true);
}

