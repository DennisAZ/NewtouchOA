//仓库切换
function changeStro(){
	self.location="wmsManageAction.do?op=wmsStroList";
}

function loadWmsName(wmsName){
	var innerHTML = "<span class='gray' style='font-size:12px;font-weight:normal;'>&nbsp;当前仓库：</span><span class='curStroName'>";
	var aSpan = "&nbsp;<span class='blue'>[<a href='javascript:void(0)' onClick='changeStro();return false;'>切换</a>]</span>";
	var innerTxt = "当前仓库：";
	if(wmsName!=""){
		var shortName = shortTextImpl(wmsName,10);
		if(shortName != null){
			innerHTML += shortName + "</span>" + aSpan;
		}
		else{
			innerHTML += wmsName + "</span>" + aSpan;
		}
		innerTxt += wmsName;
		$("sName").innerHTML = innerHTML;
		$("sName").title = innerTxt;
		$("wmsName").innerHTML=wmsName;
	}
	else {
		innerHTML += "全部仓库</span>" + aSpan;
		innerTxt += "全部仓库";
		$("sName").innerHTML = innerHTML;
		$("sName").title = innerTxt;
		$("wmsName").innerHTML="全部仓库";
	}
}

/* 
	弹出层录入,编辑
*/
function wmsPopDiv(n,arg1,arg2){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	var url;
	
	switch(n){
		//------------------ 仓库 --------------
		case 1:
			height=250;
			width='half';
			titleTxt="新建仓库"+tips;
			url="wmsManageAction.do?op=toNewWms";
			break;
		case 11:
			height=250;
			titleTxt="编辑仓库信息"+tips;
			width='half';
			url="wmsManageAction.do?op=toUpdWms&wmsCode="+arg1;
			break;
		case 12:
			height=220;
			titleTxt="查看仓库信息";
			url="wmsManageAction.do?op=wmsStroDesc&wmsCode="+arg1;
			hasScroll = true;
			break;
			
		//------------------ 入库单 --------------
		case 2:
			height=265;
			titleTxt="新建入库单"+tips;
			url="wmsManageAction.do?op=toNewWwi&wmsCode="+arg1;
			break;
		case 21:
			height=265;
			titleTxt="编辑入库单"+tips;
			url="wmsManageAction.do?op=toUpdWwi&wwiId="+arg1+"&wmsCode="+arg2;
			break;
		case 22:
			height=180;
			width='half';
			titleTxt="编辑入库产品"+tips;
			url="wmsManageAction.do?op=toUpdWip&rwiId="+arg1;
			break;
			
		//-------------------- 出库单 -------------------------------
		case 3:	
			height=298;
			titleTxt="新建出库单"+tips;
			url="wwoAction.do?op=toNewWwo&wmsCode="+arg1;
			break;
		case 31:
			height=298;
			titleTxt="编辑出库单"+tips;
			url="wwoAction.do?op=toUpdateWwo&wwoId="+arg1+"&wmsCode="+arg2;
			break;
		case 32:
			height=236;
			titleTxt="新建出库产品"+tips;
			width='half';
			url="wwoAction.do?op=toNewRWoutPro&wwoId="+arg1;
			break;
		case 33:	
			height=236;
			width='half';
			titleTxt="编辑出库产品"+tips;
			url="wwoAction.do?op=toUpdWop&rwoId="+arg1;
			break;
			
		//发货单
		/*case :
			titleTxt="发货单"+tips;
			height=450;
			url="";
			break;*/
		
		//-------------------- 调拨单 -------------------------------			
		case 4:
			height=320;
			titleTxt="新建调拨单"+tips;
			url="wwoAction.do?op=toNewWmsChange&wmsCode="+arg1;
			break;
		case 41:
			height=320;
			titleTxt="编辑调拨单"+tips;
			url="wwoAction.do?op=toUpdateWch&wchId="+arg1+"&wmsCode="+arg2;
			break;
		case 42:
			height=298;
			width='half';
			titleTxt="新建调拨产品"+tips;
			url="wwoAction.do?op=toNewRww&wchId="+arg1;
			break;
		case 43:	
			height=298;
			width='half';
			titleTxt="编辑调拨产品"+tips;
			url="wwoAction.do?op=toUpdateRww&rwwId="+arg1;
			break;
			
		//----------------------库存盘点------------------------------
		case 5:	
			height=265;
			titleTxt="新建库存盘点"+tips;
			url="wwoAction.do?op=toNewWmsCheck&wmsCode="+arg1;
			break;
		case 51:	
			height=265;
			titleTxt="编辑盘点记录"+tips;
			url="wwoAction.do?op=toUpdateWmc&wmcId="+arg1+"&wmsCode="+arg2;
			break;
		case 52:	
			height=292;
			width='half';
			titleTxt="新建盘点产品"+tips;
			url="wwoAction.do?op=toInputWmc&wmcId="+arg1;
			break;
		case 53:
			height=288;
			width='half';
			titleTxt="编辑盘点产品"+tips;
			url="wwoAction.do?op=toUpdateRwmc&rwcId="+arg1;
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}

/* 
	弹出层删除
*/
function wmsDelDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除仓库";
			break;
		case 2:
			titleTxt="删除入库单";
			break;
		case 3:
			titleTxt="撤销入库";
			url="wmsManageAction.do?op=delWwi&wwiId="+id;
			break;
		case 4:
			titleTxt="删除入库产品";
			break;
		case 5:
			titleTxt="数据整理";
			url="wms/dataOption.jsp?wmsCode="+id;
			break;
		case 6:
			titleTxt="删除出库单";
			break;
		case 7:
			titleTxt="撤销出库";
			url="wwoAction.do?op=cancelWwo&wwoId="+id;
			break;
		case 8:
			titleTxt="删除出库产品";
			break;
		case 9:
			titleTxt="删除调拨单";
			break;
		case 10:
			titleTxt="撤销调拨";
			url="wwoAction.do?op=cancelWch&wchId="+id;
			break;
		case 11:
			titleTxt="删除调拨产品";
			break;
		case 12:
			titleTxt="删除盘点产品";
			break;
		case 13:
			titleTxt="删除盘点单据";
			break;
		case 14:
			titleTxt="撤销盘点";
			url="wwoAction.do?op=goCancelWmc&wmcId="+id;
			break;
		case 15:
			titleTxt="撤销出库确认";
			url="wwoAction.do?op=cancelWch&wchId="+id+"&wm=wm";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}
//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该仓库";
			delObj[1]="wmsManageAction.do?op=delWmsStro&wmsCode="+id;
			break;
		case 2:
			delObj[0]="该入库单";
			delObj[1]="wmsManageAction.do?op=delWmi&wwiId="+id;
			break;
		case 4:
			delObj[0]="该入库产品";
			delObj[1]="wmsManageAction.do?op=delRwp&rwiId="+id;
			break;
	   	case 6:
			delObj[0]="该出库单";
			delObj[1]="wwoAction.do?op=delWwo&wwoId="+id;
			break;
		case 8:
			delObj[0]="该出库产品";
			delObj[1]="wwoAction.do?op=delRwp&rwoId="+id;
			break;
		case 9:
			delObj[0]="该调拨单";
			delObj[1]="wwoAction.do?op=delWch&wchId="+id;
			break;
		case 11:
			delObj[0]="该调拨产品";
			delObj[1]="wwoAction.do?op=delRww&rwwId="+id;
			break;
		case 12:
			delObj[0]="该盘点产品";
			delObj[1]="wwoAction.do?op=delRwmc&rwcId="+id;
			break;
		case 13:
			delObj[0]="该盘点单据";
			delObj[1]="wwoAction.do?op=delWmc&wmcId="+id;
			break;
	}
	return delObj;
}