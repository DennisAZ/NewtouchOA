/* ------------------------ */
/* ------- 协同办公 -------- */
/* ------------------------ */
function modifySchState(schId){
	var url;
	if(confirm("该日程安排改为已完成后不可再更改状态，如要放弃修改请点取消！")){
		url = "messageAction.do";
		pars = "op=modifySchState&schId="+schId+"&ran=" + Math.random();
		new Ajax.Request(url,{
			method:'get',
			parameters: pars,
			onSuccess: function(transport) {
				var response = transport.responseText.split(",");
				if(response[0]=="1"){
					$(response[2]).style.display="inline";
					$(response[1]).style.display="none";
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
	else
	   return ;
}


/* 
	弹出层录入,编辑
*/
function oaPopDiv(n,arg1,arg2){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasParent = false;
	var hasScroll = false;
	var hasCover = true;
	var url;
	
	switch(n){
		case 1:
			height=510;
			titleTxt="写消息";
			url="messageAction.do?op=fowardMess";
			hasScroll = true;
			break;
		case 11:	
			height=480;
			titleTxt="回复消息";
			url="messageAction.do?op=reMessage&meCode="+arg1+"&tz=reply&rmlId="+arg2;
			hasScroll = true;
			break;
		case 12:
			height=510;
			titleTxt="转发消息";
			url="messageAction.do?op=reMessage&meCode="+arg1+"&tz=forward";
			hasScroll = true;
			break;
		case 13:
			height=510;
			titleTxt="发送消息";
			url="messageAction.do?op=showMessInfo&meCode="+arg1+"&mark=send";
			hasScroll = true;
			break;
			
		case 2:
			height=660;
			titleTxt="发布新闻公告";
			url="messageAction.do?op=forwardNews";
			hasScroll = true;
			break;
		case 21:
			height =660;
			titleTxt="编辑新闻公告";
			url="messageAction.do?op=goUpdNewsInfo&newCode="+arg1+"&isEdit=2";
			hasScroll = true;
			break;	
			
		case 3:
			height=330;
			titleTxt="新建日程";
			url="messageAction.do?op=fowardSch";
			break;
		case 31:
			height=330;
			titleTxt="编辑日程";
			url="messageAction.do?op=goUpdSchInfo&id="+arg1;
			break;
		case 32:
			height=330;
			titleTxt="查看日程";
			url="messageAction.do?op=showScheduleInfo&id="+arg1;
			if(arg2!=null&&arg2==1){
				hasParent=true;
			}
			hasScroll = true;
			break;
		case 4:
			height=545;
			titleTxt="写报告";
			url="messageAction.do?op=findFoward";
			hasScroll = true;
			break;
		case 41:
			height =545;
			titleTxt="待发报告";
			url="messageAction.do?op=showRepInfo&repCode="+arg1+"&mark=sendInfo";
			hasScroll = true;
			break;
		
		case 5:
			height =440;
			titleTxt="发布工作安排";
			url="salTaskAction.do?op=salTask";
			hasScroll = true;
			break;
		//编辑
		case 51:
			height=440;
			titleTxt="编辑工作安排";
			url="salTaskAction.do?op=update&stId="+arg1;
			hasScroll = true;
			break;
			
		case 52:
			titleTxt="完成情况";
			height=350;
			width=400;
			url="salTaskAction.do?op=showMyTask&taLimId="+arg1;
			hasCover = false;//不用遮罩层
			hasScroll = true;
			break;
	}
	if(hasParent){
		parent.createPopWindow(idPre,titleTxt,url,width,height,false,hasScroll);
	}
	else{
		createPopWindow(idPre,titleTxt,url,width,height,hasCover,hasScroll);
	}
}

/* 
	弹出层删除
*/
function oaDelDiv(n,code,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除新闻公告";
			break;
		case 2:
			titleTxt="删除日程安排";
			break;
		case 3:
			titleTxt="删除报告";
			break;
		case 4:
			titleTxt="删除报告";
			break;
		case 5:
			titleTxt="删除报告";
			break;
		case 6:
			titleTxt="删除消息";
			break;
		case 7:
			titleTxt="删除消息";
			break;
		case 8:
			titleTxt="删除消息";
			break;
		case 9:
			titleTxt="删除工作安排";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,code,n,isIfrm);
}

//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该新闻公告";
			delObj[1]="messageAction.do?op=delNews&newCode="+id;
			break;
		case 2:
			delObj[0]="该日程安排";
			delObj[1]="messageAction.do?op=delSchedule&schId="+id;
			break;
		case 3:
			delObj[0]="该报告";
			delObj[1]="messageAction.do?op=delReport&delRmark=sfdel&rrlId="+id;
			break;
		case 4:
			delObj[0]="该报告";
			delObj[1]="messageAction.do?op=delReport&delRmark=fdel&repCode="+id;
			break;
		case 5:
			delObj[0]="该报告";
			delObj[1]="messageAction.do?op=delReport&delRmark=tdel&repCode="+id;
			break;
		case 6:
			delObj[0]="该消息";
			delObj[1]="messageAction.do?op=delMess&delRmark=hcfdel&rmlId="+id;
			break;
		case 7:
			delObj[0]="该消息";
			delObj[1]="messageAction.do?op=delMess&delRmark=fdel&meCode="+id;
			break;
		case 8:
			delObj[0]="该消息";
			delObj[1]="messageAction.do?op=delMess&delRmark=tdel&meCode="+id;
			break;
	   	case 9:
			delObj[0]="该工作安排";
			delObj[1]="salTaskAction.do?op=delSalTask&stId="+id;
			break;
	}
	return delObj;
}