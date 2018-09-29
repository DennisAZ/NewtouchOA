/* ------------------------ */
/* ------- 财务管理 -------- */
/* ------------------------ */
function loadTabType(isAll){
			switch(isAll){
				//我的采购单
				case '0':
					$("tabType1").className="tabTypeWhite";
					setOtherStyle(1,3);
					break;
				//下属采购单
				case '1':
					$("tabType2").className="tabTypeWhite";
					setOtherStyle(2,3);
					break;
				//全部采购单
				case '2':
					$("tabType3").className="tabTypeWhite";
					setOtherStyle(3,3);
					break;
			}
		}
/* 
	弹出层录入,编辑
*/
function addDivNew(n,arg1,arg2){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	
	switch(n){
		case 1:
			height=470;
			titleTxt="新建采购单"+tips;
			url="salPurAction.do?op=goNewSpo&pInfo=py";
			break;
		case 2:
			height=470;
			titleTxt="编辑采购单"+tips;
			url="salPurAction.do?op=goUpdateSpo&spoId="+arg1;
			break;
		case 3:
			titleTxt="付款计划录入"+tips;
			url="salPurAction.do?op=forwardCreate&type=1";
			height=240;
			break;
	    /*case 4:
			titleTxt="付款记录录入"+tips;
			url="salPurAction.do?op=forwardCreate&type=2";
			height=170;
			width=460;
			break;*/
		//case 5:
			//执行回款操作时弹出(无用)
			//titleTxt="付款记录录入"+tips;
			//url="salPurAction.do?op=forwardCreate&type=2&paidId="+arg1;
			//height=170;
			//width=550;
			//break;
		case 6:
			titleTxt="编辑付款计划"+tips;
			height=240;
			url="salPurAction.do?op=forwardMod&paidId="+arg1+"&type=1";
			break;
		
		/*case 7:
			titleTxt="编辑付款记录"+tips;
			height=170;
			width=460;
			url="salPurAction.do?op=forwardMod&paidId="+arg1+"&type=0";
			break;*/
	    case 8:
			titleTxt="查看付款计划";
			height=240;
			url="salPurAction.do?op=getPaidPlan&planId="+arg1;
			hasScroll = true;
			break;
	    case 9:
			width=580;
			height=380;
			titleTxt="查看付款记录";
			url="accountAction.do?op=getAccOut&accOutId="+arg1;
			hasScroll = true;
			break;
	    case 10:
			titleTxt="查看收票记录";
			height=260;
			width=520;
			url="salPurAction.do?op=showInv&id="+arg1;
			hasScroll = true;
			break;
		case 11:
			height=270;
			titleTxt="新建询价单"+tips;
			url="salPurAction.do?op=goNewInp&sInfo=sy&pInfo=py";
			break;
			
		case 12:
			height=270;
			titleTxt="编辑询价单"+tips;
			url="salPurAction.do?op=goUpdateInq&inqId="+arg1;
			break;
			
		/*case 13:
			titleTxt="查看入库单";
			height=350;
			url="wmsManageAction.do?op=rwiDesc&wwiId="+arg1;
			break;*/
		/*case 14:
			height=450;
			titleTxt="审核入库单";
			url="wmsManageAction.do?op=goAppWwi&wwiId="+arg1;
			break;*/
		case 15:
			titleTxt="编辑入库单"+tips;
			height=300;
			width=600;
			url="wmsManageAction.do?op=toUpdWwi&wwiId="+arg1+"&wmsCode="+arg2;
			break;
	}

	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}

/* 
	弹出层删除
*/
function delDiv(n,code,agrs){
	width=250;
	height=136;
	var idPre="";
	var titleTxt="";
	
	switch(n){
		case 1:
			titleTxt="删除采购单";
			url="supply/delSupConfirm.jsp?code="+code+"&delType=11";
			break;
	    case 2:
			titleTxt="删除付款计划";
			url="common/delConfirm.jsp?delType=6&code="+code;
			break;
	    case 3:
			titleTxt="删除付款记录";
			url="common/delConfirm.jsp?delType=7&code="+code;
			break;
		case 4:
			titleTxt="撤销审核";
			url="supply/removeApp.jsp?code="+code;
			break;
		case 5:
			titleTxt="删除询价单";
			url="supply/delSupConfirm.jsp?code="+code+"&delType=12";
			break;
		case 6:
			titleTxt="删除入库单";
			url="wmsManageAction.do?op=delConfirm&code="+code+"&delType=4";
			break;
		case 7:
			titleTxt="撤销入库";
			url="wmsManageAction.do?op=delWwi&wwiId="+code;
			break;
		case 8:
			titleTxt="修改交付状态";
			url="supply/spoState.jsp?code="+code+"&state="+agrs;
			break;
		
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,false);
}

