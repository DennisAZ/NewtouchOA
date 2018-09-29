/* --------------------- */
/* ------- 供应商 -------- */
/* -------------------- */

/* 点击搜索后保持选项卡选中 */
//function loadSearchTab(){
	//$("searchTab2").className="xpTabSelected";
	//$("searchTab1").className="xpTabGray";
	//$("search2").style.display="block";
	//$("search1").style.display="none";
//}

/* 
	新建
*/
function supPopDiv(n,arg1,arg2){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var url;
	
	switch(n){
		case 1:
			height=510;
			titleTxt="供应商资料录入"+tips;
			url="salSupplyAction.do?op=toNewSup";
			break;
		case 11:
			height=510;
			titleTxt="编辑供应商资料"+tips;
			url="salSupplyAction.do?op=toUpdSup&supId="+arg1;
			break;	
			
		case 2:
		    //info=y表示需要选择供应商，info=n表示新建时不需要选择供应商（在供应商详情中点击新建）
			height=475;
			titleTxt="联系人信息录入"+tips;
			url="salSupplyAction.do?op=findForward&info=y";
			break;
		case 21:
			height=475;
			titleTxt="联系人信息录入"+tips;
			url="salSupplyAction.do?op=findForward&info=n&supId="+arg1+"&supName="+encodeURIComponent(arg2);
			break;
		case 22:
			titleTxt="编辑联系人资料"+tips;
			height = 475;
			url="salSupplyAction.do?op=goUpdSupInfo&conId="+arg1+"&isEdit="+arg2;
			break;
			
		case 3:
			height=270;
			titleTxt="新建询价单"+tips;
			url="salPurAction.do?op=goNewInp&sInfo=sn&pInfo=py&supId="+arg1+"&supName="+encodeURIComponent(arg2);
			break;
		//供应商详情中编辑
		case 31:
			height=270;
			titleTxt="编辑询价单"+tips;
			url="salPurAction.do?op=goUpdateInq&isEdit=2&inqId="+arg1;
			break;
			
	}
	createPopWindow(idPre,titleTxt,url,width,height);
}

/* 
	删除
*/
function supDelDiv(n,code,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除供应商资料";
			break;
		case 2:
			titleTxt="删除联系人资料";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,code,n,isIfrm);
}

//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该供应商资料";
			delObj[1]="salSupplyAction.do?op=deleteSup&supId="+id;
			break;
		case 2:
			delObj[0]="该联系人资料";
			delObj[1]="salSupplyAction.do?op=delSupCon&id="+id;
			break;
	}
	return delObj;
}