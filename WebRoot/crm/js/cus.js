
//客户字段
var CUS_COLOR = [ ["无","红","绿","蓝"], ["0","1","2","3"] ];
var CUS_STATE = [ ["待开发","开发中","已归属"], ["0","1","2"] ];
var CARD_TYPE = [ ["营业执照","身份证","组织结构代码证"], ["1","2","3"] ];
var CUS_REL = [ "较好","密切","较差" ];
//联系人
var CON_SHIP = [ ["是","否"], ["1","0"] ];
var CON_SEX = [ "男","女" ];
var CON_TYPE = [ "特别重要", "重要", "普通", "不重要", "失效" ];
//来往记录字段
var PRA_TYPE = [ ["电话","邮寄","传真","网络","拜访","来访接待","会议","培训","其他"],["1","2","3","4","5","6","7","8","9"]];
//客户产品
var CP_HAS_TAX = [ ["是","否"], ["1","0"] ];

function createCusCb(type,id,cbName,defaultValue){
	var showValues = null;
	var DBValues = null;
	switch(type){
	case "t_color":
		showValues = CUS_COLOR[0];
		DBValues = CUS_COLOR[1];
		break;
	case "t_conShip":
		showValues = CON_SHIP[0];
		DBValues = CON_SHIP[1];
		if(defaultValue==undefined||defaultValue==""){ defaultValue=CON_SHIP[1][1]; }
		break;
	case "t_sex":
		showValues = CON_SEX;
		break;
	case "t_hasTax":
		showValues = CP_HAS_TAX[0];
		DBValues = CP_HAS_TAX[1];
		break;
	}
	createRadio(id,cbName,showValues,DBValues,defaultValue);
}

function getCusColorClass(color){
	var className = "";
	switch(color){
		case '0': className="customerNormal";break;
		case '1': className="customerRed";break;
		case '2': className="customerGreen";break;
		case '3': className="customerBlue";break;
	}
	return className;
}

function createCusSel(id,type,defaultValue){
	var showValues = null;
	var DBValues = null;
	switch(type){
	case 't_state':
		showValues = CUS_STATE[0];
		DBValues = CUS_STATE[1];
		break;
	case 't_hot':
		showValues=[];
		DBValues=[];
		for(var i=0; i<5; i++){
			showValues[i]=(i+1)+"星";
			DBValues[i] = i+1;
		}
		break;
	case 't_cardType':
		showValues = CARD_TYPE[0];
		DBValues = CARD_TYPE[1];
		break;
	case 't_relation':
		showValues = CUS_REL;
		break;
	case 't_color':
		showValues = CUS_COLOR[0];
		DBValues = CUS_COLOR[1];
		break;
	case 't_praType':
		showValues = PRA_TYPE[0];
		DBValues = PRA_TYPE[1];
		break;
	case 't_conType':
		showValues = CON_TYPE;
		break;
	}
	createSelector(id,showValues,DBValues,defaultValue);
}

function getCusTxtValue(type,DBValue){
	var showValue = "";
	var mapArray = null;
	switch(type){
	case 't_state': mapArray = CUS_STATE; break;
	case 't_cardType': mapArray = CARD_TYPE; break;
	case 't_praType': mapArray = PRA_TYPE; break;
	}
	for(var i=0; i<mapArray[0].length; i++){
		if(DBValue==mapArray[1][i]){
			showValue = mapArray[0][i];
			break;
		}
	}
	return showValue;
}

function getCusLev(cusHot){
	var starArray = new Array();
	if(cusHot!=""){
		for(var i=0; i<cusHot; i++){
			starArray.push("★");　
		}
	}
	return starArray.join("");
}



/* 选择用户弹出层 */
function popLayer(step){
	var popLayer=document.createElement("div");
	var x = event.clientX; 
	var y = event.clientY; 
	popLayer.style.position="absolute";
	popLayer.style.left=x;
	popLayer.style.top=y;
	//popLayer.style.height="500";
	//popLayer.style.width="600";
	popLayer.id='popDiv'+step;
	popLayer.innerHTML="<iframe src='messageAction.do?op=findFoward&mark=accUser&step="+step+"' width='400'height='220'></iframe>"
	document.body.appendChild(popLayer);
}
function isExistDiv(step)
{
 var div=$("popDiv"+step);
 if(div!=null&&div.style.display=="none")
 	div.style.display="block";
 if(div==null)
    popLayer(step);
}
/* 点击搜索后保持选项卡选中 */
function loadSearchTab(){
	$("searchTab2").className="xpTabSelected";
	$("searchTab1").className="xpTabGray";
	$("search2").style.display="block";
	$("search1").style.display="none";
}

/* 
	新建、编辑弹出层
*/
function cusPopDiv(n,arg){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasTitle = true;
	var hasScroll = false;
	var url;
	
	switch(n){
		case 1:
			height=900;
			titleTxt="新建客户";
//			url="customAction.do?op=toSaveCus";
			url="customAction.do?op=toSaveMyManageCus"; //管辖区域下的客户
			break;
		case 11:
			height = 870;
			titleTxt="编辑客户";
//			url="customAction.do?op=toUpdCus&corCode="+arg;
			url="customAction.do?op=toUpdMyManageCus&corCode="+arg; //管辖区域下的客户
			break;
		case 12:
			titleTxt ="修改客户状态";
			height=140;
			width='half';
			url = "customAction.do?op=toAssignCus";
			break;	
		case 13:
			titleTxt = "选择导出字段";
			height=400;
			url="customAction.do?op=toOutCus&cusState"+arg[0]+"&range="+arg[1]+"&corName="+encodeURIComponent(arg[2])+"&cType="+arg[3]+"&corAdd="+encodeURIComponent(arg[4])+"&seName="+encodeURIComponent(arg[5])+"&startTime="+arg[6]+"&endTime="+arg[7]+"&cusInd="+arg[8]+"&type="+arg[9]+"&filter="+arg[10]+"&color="+arg[11];
			break;
		case 15:
			titleTxt = "导入客户";
			height=230;
			width=360;
			url="fileAction.do?op=toImportCus";
			break;
		case 16:
			titleTxt = "批量标色";
			height=110;
			width=290;
			url="customAction.do?op=toBatchColor";
			break;
		case 17:
			height=330;
			titleTxt="新建日程";
			url="messageAction.do?op=fowardSch&cusId="+arg+"&isIfrm=1";
			break;	
		case 18:
			height=330;
			titleTxt="查看日程";
			url="messageAction.do?op=showScheduleInfo&id="+arg;
			hasScroll = true;
			break;
		case 19:
			height=330;
			titleTxt="编辑日程";
			url="messageAction.do?op=goUpdSchInfo&id="+arg+"&isIfrm=1";
			break;
		case 99:
			titleTxt="新建开票记录";
			url="customAction.do?op=toNewSalInvoice&cusId="+arg;
			height=330;
			break;
		case 100:
			titleTxt="编辑开票记录";
			url="customAction.do?op=toUpdSalInvoice&sinId="+arg;
			height=330;
			break;
		case 101:
			titleTxt="新建回款记录";
			url="paidAction.do?op=toNewPaidPast&cusId="+arg;
			height=330;
			break;
		case 102:
			titleTxt="编辑回款记录";
			url="paidAction.do?op=toUpdPaidPast&paidId="+arg;
			height=330;
			break;
		case 103:
			titleTxt="新建订单合同";
			height=610;
			url="orderAction.do?op=toNewOrder&cusId="+arg;
			break;
		case 104:
			titleTxt="编辑订单"+tips;
			height=610;
			url="orderAction.do?op=toUpdOrder&ordId="+arg+"&isIfrm=1";
			break;
		case 105:
			height =330;
			titleTxt = "新建客户产品";
			url="customAction.do?op=toNewCusProd&cusId="+arg+"&isIfrm=1";
			break;
		case 106:
			height =330;
			titleTxt = "编辑客户产品";
			url="customAction.do?op=toUpdCusProd&cpId="+arg+"&isIfrm=1";
			break;
		case 107:
			height=110;
			width=290;
			titleTxt = "修改最低月销售额";
			url="customAction.do?op=toUpdCorSalNum&cusId="+arg;
			break;
		case 110:
			height=110;
			width=290;
			titleTxt = "修改发货期初余额";
			url="customAction.do?op=toUpdCorBeginBal&cusId="+arg;
			break;
		case 111:
			height=110;
			width=290;
			titleTxt = "修改开票期初余额";
			url="customAction.do?op=toUpdCorTicketBal&cusId="+arg;
			break;	
		case 108:
			height =200;
			titleTxt = "新建客户地址";
			url="customAction.do?op=toSaveCusAddr&cusId="+arg;
			break;
		case 109:
			height =200;
			titleTxt = "编辑客户地址";
			url="customAction.do?op=toUpdCusAddr&cadId="+arg;
			break;
			
		case 2:
			height=610;
			titleTxt="新建联系人";
			url="customAction.do?op=toNewContact";
			if(arg != undefined){//客户详情新建
				url += "&cusId="+arg;
			}
			break;
		case 21:
			height = 610;
			titleTxt="编辑联系人";
			url="customAction.do?op=toUpdCon&id="+arg[0];
			if(arg.length>1 != undefined){
				url+= "&isIfrm="+arg[1];
			}
			break;	
		case 23:
			height=210;
			titleTxt="新建纪念日";
			url="customAction.do?op=toSaveMemDate&conId="+arg;
			break;
		case 24:
			height=210;
			titleTxt="编辑纪念日";
			url="customAction.do?op=toUpdMemDate&mdId="+arg;
			break;	
		case 25:
			titleTxt = "选择导出字段";
			height = 325;
			url = "customAction.do?op=toOutCont&range="+arg[0]+"&conName="+encodeURIComponent(arg[1])+"&cusName="+encodeURIComponent(arg[2])+"&conLev="+arg[3]+"&seName="+encodeURIComponent(arg[4])+"&type="+arg[5];
			break;
		case 26:
			titleTxt ="分配负责人";
			height=120;
			width='half';
			url = "customAction.do?op=toAssignCon";
			break;
		/*case 3:
			height=565;
			titleTxt="新建销售机会";
			url="cusServAction.do?op=toNewOpp";
			if(arg1 != undefined){//客户详情新建
				url += "&cusId="+arg1;
			}
			break;
		case 31:
			height = 650;
			titleTxt="编辑销售机会";
			url="cusServAction.do?op=toUpdOpp&id="+arg1+"&isIfrm="+arg2;
			break;*/
		
		case 4:
			titleTxt="新建来往记录";
			url="cusServAction.do?op=toNewPra";
			if(arg.length ==2 && arg!=undefined){//从客户（列表或详情）进入进行来往记录
				height=330;
				url += "&cusCode="+arg[0]+"&isDesc="+arg[1];
			}
			if(arg.length == 3 && arg!=undefined){//从联系人（列表或详情）进入进行来往记录
				height=300;
				url += "&conId="+arg[0]+"&isDesc="+arg[1];
			}
			break;
		case 41:
			titleTxt="编辑来往记录";
			url="cusServAction.do?op=toUpdPra&id="+arg[0];
			if(arg[2] == "1"){
				height = 330;
			}else{
				height = 300;
			}
			if(arg.length>1){
				url+= "&isIfrm="+arg[1]+"&type="+arg[2];
			}
			break;
		
		case 42:// 修改来往记录执行日期
			width = 220;
			height = 110;
			url="customAction.do?op=modifyExeDate&code="+arg[0]+"&praExeDate="+[1];
			hasTitle = false;
			break;


		/*case 5:
			height=485;
			titleTxt="新建客户服务";
			url="cusServAction.do?op=toNewServ";
			if(arg1 != undefined){//客户详情新建
				url += "&cusId="+arg1;
			}
			break;
		case 51:
			height = 475;
			titleTxt="编辑客户服务";
			url="cusServAction.do?op=toUpdServ&serCode="+arg1+"&isIfrm="+arg2;
			break;*/
		
		/*case 6:
			height=325;
			titleTxt="新建报价记录"+tips;
			url="cusServAction.do?op=toNewQuote&pInfo=py&oppTitle="+encodeURIComponent(arg1)+"&oppId="+arg2;
			break;
		case 61:
			height =325;
			titleTxt="编辑报价记录"+tips;
			url="cusServAction.do?op=toUpdQuo&id="+arg1;
			break;*/		
		/*case 7:
			height = 250;
			titleTxt ="处理客户服务"; 	
			url = "cusServAction.do?op=toExeServ&serId="+arg1;
			break;*/
		
		
			
		/*case 14:
			titleTxt = "选择导出字段";
			height=460;
			width = 555;
			hasScroll = true;
			url="customAction.do?op=toOutSupCus&range="+arg1+"&limCode="+arg2+"&allName="+arg3+"&allMne="+arg4+"&allCode="+arg5+"&allTemp="+arg6
			+"&industryId="+arg7+"&allStar="+arg8+"&souId="+arg9+"&sArea="+arg10+"&allHot="+arg11+"&relLev="+arg12+"&corPerSize="+arg13+"&startTime="+arg14
			+"&endTime="+arg15+"&allCountry="+arg16+"&allProvince="+arg17+"&allCity="+arg18+"&update="+arg19+"&address="+arg20+"&contact="+arg21
			+"&phone="+arg22+"&fex="+arg23+"&remark="+arg24+"&type="+arg25;
			break;	*/
		
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll,hasTitle);
}




/* 
	新建、编辑弹出层
*/
function manageCusPopDiv(n,arg){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasTitle = true;
	var hasScroll = false;
	var url;
	
	switch(n){
		case 1:
			height=900;
			titleTxt="新建客户";
			url="customAction.do?op=toSaveMyManageCus";
			break;
		case 11:
			height = 870;
			titleTxt="编辑客户";
			url="customAction.do?op=toUpdMyManageCus&corCode="+arg;
			break;
		case 12:
			titleTxt ="修改客户状态";
			height=140;
			width='half';
			url = "customAction.do?op=toAssignCus";
			break;	
		case 13:
			titleTxt = "选择导出字段";
			height=400;
			url="customAction.do?op=toOutCus&cusState"+arg[0]+"&range="+arg[1]+"&corName="+encodeURIComponent(arg[2])+"&cType="+arg[3]+"&corAdd="+encodeURIComponent(arg[4])+"&seName="+encodeURIComponent(arg[5])+"&startTime="+arg[6]+"&endTime="+arg[7]+"&cusInd="+arg[8]+"&type="+arg[9]+"&filter="+arg[10]+"&color="+arg[11];
			break;
		case 15:
			titleTxt = "导入客户";
			height=230;
			width=360;
			url="fileAction.do?op=toImportCus";
			break;
		case 16:
			titleTxt = "批量标色";
			height=110;
			width=290;
			url="customAction.do?op=toBatchColor";
			break;
		case 17:
			height=330;
			titleTxt="新建日程";
			url="messageAction.do?op=fowardSch&cusId="+arg+"&isIfrm=1";
			break;	
		case 18:
			height=330;
			titleTxt="查看日程";
			url="messageAction.do?op=showScheduleInfo&id="+arg;
			hasScroll = true;
			break;
		case 19:
			height=330;
			titleTxt="编辑日程";
			url="messageAction.do?op=goUpdSchInfo&id="+arg+"&isIfrm=1";
			break;
		case 99:
			titleTxt="新建开票记录";
			url="customAction.do?op=toNewSalInvoice&cusId="+arg;
			height=330;
			break;
		case 100:
			titleTxt="编辑开票记录";
			url="customAction.do?op=toUpdSalInvoice&sinId="+arg;
			height=330;
			break;
		case 101:
			titleTxt="新建回款记录";
			url="paidAction.do?op=toNewPaidPast&cusId="+arg;
			height=330;
			break;
		case 102:
			titleTxt="编辑回款记录";
			url="paidAction.do?op=toUpdPaidPast&paidId="+arg;
			height=330;
			break;
		case 103:
			titleTxt="新建订单合同";
			height=610;
			url="orderAction.do?op=toNewOrder&cusId="+arg;
			break;
		case 104:
			titleTxt="编辑订单"+tips;
			height=610;
			url="orderAction.do?op=toUpdOrder&ordId="+arg+"&isIfrm=1";
			break;
		case 105:
			height =330;
			titleTxt = "新建客户产品";
			url="customAction.do?op=toNewCusProd&cusId="+arg+"&isIfrm=1";
			break;
		case 106:
			height =330;
			titleTxt = "编辑客户产品";
			url="customAction.do?op=toUpdCusProd&cpId="+arg+"&isIfrm=1";
			break;
		case 107:
			height=110;
			width=290;
			titleTxt = "修改最低月销售额";
			url="customAction.do?op=toUpdCorSalNum&cusId="+arg;
			break;
		case 110:
			height=110;
			width=290;
			titleTxt = "修改发货期初余额";
			url="customAction.do?op=toUpdCorBeginBal&cusId="+arg;
			break;
		case 111:
			height=110;
			width=290;
			titleTxt = "修改开票期初余额";
			url="customAction.do?op=toUpdCorTicketBal&cusId="+arg;
			break;	
		case 108:
			height =200;
			titleTxt = "新建客户地址";
			url="customAction.do?op=toSaveCusAddr&cusId="+arg;
			break;
		case 109:
			height =200;
			titleTxt = "编辑客户地址";
			url="customAction.do?op=toUpdCusAddr&cadId="+arg;
			break;
			
		case 2:
			height=610;
			titleTxt="新建联系人";
			url="customAction.do?op=toNewContact";
			if(arg != undefined){//客户详情新建
				url += "&cusId="+arg;
			}
			break;
		case 21:
			height = 610;
			titleTxt="编辑联系人";
			url="customAction.do?op=toUpdCon&id="+arg[0];
			if(arg.length>1 != undefined){
				url+= "&isIfrm="+arg[1];
			}
			break;	
		case 23:
			height=210;
			titleTxt="新建纪念日";
			url="customAction.do?op=toSaveMemDate&conId="+arg;
			break;
		case 24:
			height=210;
			titleTxt="编辑纪念日";
			url="customAction.do?op=toUpdMemDate&mdId="+arg;
			break;	
		case 25:
			titleTxt = "选择导出字段";
			height = 325;
			url = "customAction.do?op=toOutCont&range="+arg[0]+"&conName="+encodeURIComponent(arg[1])+"&cusName="+encodeURIComponent(arg[2])+"&conLev="+arg[3]+"&seName="+encodeURIComponent(arg[4])+"&type="+arg[5];
			break;
		case 26:
			titleTxt ="分配负责人";
			height=120;
			width='half';
			url = "customAction.do?op=toAssignCon";
			break;
		/*case 3:
			height=565;
			titleTxt="新建销售机会";
			url="cusServAction.do?op=toNewOpp";
			if(arg1 != undefined){//客户详情新建
				url += "&cusId="+arg1;
			}
			break;
		case 31:
			height = 650;
			titleTxt="编辑销售机会";
			url="cusServAction.do?op=toUpdOpp&id="+arg1+"&isIfrm="+arg2;
			break;*/
		
		case 4:
			titleTxt="新建来往记录";
			url="cusServAction.do?op=toNewPra";
			if(arg.length ==2 && arg!=undefined){//从客户（列表或详情）进入进行来往记录
				height=330;
				url += "&cusCode="+arg[0]+"&isDesc="+arg[1];
			}
			if(arg.length == 3 && arg!=undefined){//从联系人（列表或详情）进入进行来往记录
				height=300;
				url += "&conId="+arg[0]+"&isDesc="+arg[1];
			}
			break;
		case 41:
			titleTxt="编辑来往记录";
			url="cusServAction.do?op=toUpdPra&id="+arg[0];
			if(arg[2] == "1"){
				height = 330;
			}else{
				height = 300;
			}
			if(arg.length>1){
				url+= "&isIfrm="+arg[1]+"&type="+arg[2];
			}
			break;
		
		case 42:// 修改来往记录执行日期
			width = 220;
			height = 110;
			url="customAction.do?op=modifyExeDate&code="+arg[0]+"&praExeDate="+[1];
			hasTitle = false;
			break;


		/*case 5:
			height=485;
			titleTxt="新建客户服务";
			url="cusServAction.do?op=toNewServ";
			if(arg1 != undefined){//客户详情新建
				url += "&cusId="+arg1;
			}
			break;
		case 51:
			height = 475;
			titleTxt="编辑客户服务";
			url="cusServAction.do?op=toUpdServ&serCode="+arg1+"&isIfrm="+arg2;
			break;*/
		
		/*case 6:
			height=325;
			titleTxt="新建报价记录"+tips;
			url="cusServAction.do?op=toNewQuote&pInfo=py&oppTitle="+encodeURIComponent(arg1)+"&oppId="+arg2;
			break;
		case 61:
			height =325;
			titleTxt="编辑报价记录"+tips;
			url="cusServAction.do?op=toUpdQuo&id="+arg1;
			break;*/		
		/*case 7:
			height = 250;
			titleTxt ="处理客户服务"; 	
			url = "cusServAction.do?op=toExeServ&serId="+arg1;
			break;*/
		
		
			
		/*case 14:
			titleTxt = "选择导出字段";
			height=460;
			width = 555;
			hasScroll = true;
			url="customAction.do?op=toOutSupCus&range="+arg1+"&limCode="+arg2+"&allName="+arg3+"&allMne="+arg4+"&allCode="+arg5+"&allTemp="+arg6
			+"&industryId="+arg7+"&allStar="+arg8+"&souId="+arg9+"&sArea="+arg10+"&allHot="+arg11+"&relLev="+arg12+"&corPerSize="+arg13+"&startTime="+arg14
			+"&endTime="+arg15+"&allCountry="+arg16+"&allProvince="+arg17+"&allCity="+arg18+"&update="+arg19+"&address="+arg20+"&contact="+arg21
			+"&phone="+arg22+"&fex="+arg23+"&remark="+arg24+"&type="+arg25;
			break;	*/
		
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll,hasTitle);
}

/* 
	删除弹出确认层
	n:删除类型，在delConfirm方法中的对应序号
*/
function cusDelDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除客户资料";
			break;
		case 2:
			titleTxt="删除联系人资料";
			break;
		case 3:
			titleTxt="删除销售机会";
			break;
		case 4:
			titleTxt="删除来往记录";
			break;
		case 5:
			titleTxt="删除客户服务";
			break;
		case 7:
			titleTxt="删除订单";
			break;
		case 8:
			titleTxt="删除报价记录";
			break;
		case 9:
			titleTxt = "删除日程安排";
			break;
		case 10:
			titleTxt = "删除回款记录";
			break;
		case 11:
			titleTxt = "删除客户产品";
			break;
		case 12:
			titleTxt = "删除纪念日";
			break;
		case 13:
			titleTxt = "删除客户地址";
			break;
		case 14:
			titleTxt = "删除开票记录";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}
//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该客户";
			delObj[1]="customAction.do?op=deleteCus&cusCode="+id;
			break;
		case 2:
			delObj[0]="该联系人";
			delObj[1]="customAction.do?op=delCusContact&id="+id;
			break;
		case 3:
			delObj[0]="该销售机会";
			delObj[1]="cusServAction.do?op=delSalOpp&id="+id;
			break;
		case 4:
			delObj[0]="该来往记录";
			delObj[1]="cusServAction.do?op=delSalPra&id="+id;
			break;
		case 5:
			delObj[0]="该客户服务";
			delObj[1]="cusServAction.do?op=delServ&serCode="+id;
			break;
		case 7:
			delObj[0]="该订单";
			delObj[1]="orderAction.do?op=deleteOrd&id="+id;
			break;
	   	case 8:
			delObj[0]="该报价记录";
			delObj[1]="cusServAction.do?op=delQuote&quoId="+id;
			break;
		case 9:
			delObj[0]="该日程安排";
			delObj[1]="messageAction.do?op=delSchedule&schId="+id;
			break;
		case 10:
			delObj[0]="该回款记录";
			delObj[1]="paidAction.do?op=deletePaidPast&paidId="+id;
			break;
		case 11:
			delObj[0] = "该客户产品";
			delObj[1] = "customAction.do?op=delCusProd&cpId="+id;
			break;
		case 12:
			delObj[0] = "该纪念日";
			delObj[1] = "customAction.do?op=delMemDate&mdId="+id;
			break;
		case 13:
			delObj[0] = "该客户地址";
			delObj[1] = "customAction.do?op=delCusAddr&cadId="+id;
			break;
		case 14:
			delObj[0] = "该开票记录";
			delObj[1] = "customAction.do?op=delCusSalInvoice&sinId="+id;
			break;
	}
	return delObj;
}


/* 搜索条件切换 */
function swapDiv(i,n){
	for(var j=1;j<=n;j++){
		if(j==i){
			$("searchTab"+j).className="xpTabSelected";
			$("search"+j).style.display="block";
		}
		else{
			$("searchTab"+j).className="xpTabGray";
			$("search"+j).style.display="none";
		}
	}
}

/* 加标签 */
function addTemp(code){
	var url = "customAction.do";
	var pars = "op=addTempTag&code="+code+"&ran=" + Math.random();
	new Ajax.Request(url, { 
		method: 'post',
		parameters: pars,
		onSuccess: function(transport) {
			var response = transport.responseText.split(",");
           	$(response[1]).className=response[0];
		},
		
		onFailure: function(transport){
			if (transport.status == 404)
				alert("您访问的url地址不存在！");
			else
				alert("Error: status code is " + transport.status);
		}
	}); 
}

/* 联系人是否为收货人标记 */
function addConMark(id){
	var url = "customAction.do";
	var pars = "op=addConMark&conId="+id+"&ran=" + Math.random();
	new Ajax.Request(url, { 
		method: 'post',
		parameters: pars,
		onSuccess: function(transport) {
			var response = transport.responseText.split(",");
			if(response[0]==1){
				$("trMark"+response[1]).className="conShipMark";
			}
			else{
				$("trMark"+response[1]).className="conGrayMark";
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