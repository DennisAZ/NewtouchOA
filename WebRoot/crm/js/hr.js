/* --------------------- */
/* ------- 人事 -------- */
/* -------------------- */
/* 
	弹出层录入,编辑
*/
function addDivNew(n,arg1){
	var width=580;
	var height=320;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	
	switch(n){
		case 1:
			titleTxt="添加部门"+tips;
			url="empAction.do?op=newSalOrg";
			break;
		case 2:
			titleTxt="添加部门"+tips;
			url="empAction.do?op=newSalOrg&upOrg="+arg1;
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height);
}


function hrDelDiv(n,id,isIfrm){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除员工资料";
			break;
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}
//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该员工";
			delObj[1]="empAction.do?op=delSalEmp&seNo="+id;
			break;
	}
	return delObj;
}

//员工信息录入判断必填项，试用期是否是数字
function check(){
	var errStr = "";
	if(isEmpty("seName")){
		errStr+="- 未填写员工的姓名！\n";
	}
	else if(checkLength("seName",25)){
		errStr+="- 员工的姓名不能超过25个字！\n";
	}
	if(isEmpty("seCode")){
		errStr+="- 未填写员工号！\n";
	}
	else if(checkLength("seCode",25)){
		errStr+="- 员工号不能超过25位！\n";
	}
	if($("isRepeat").value==1){
		errStr+="- 此员工号已存在！\n";
	}
	
	if(isEmpty("soCode")){
		errStr+="- 未选择员工所属部门！\n";
	}
	if(isEmpty("jobLev")){
		errStr+="- 未选择员工所在职位！\n";
	}
	/*if(isNaN($("prob").value)){
		errStr+="- 试用期请填写数字！\n";
	}*/
	if(errStr!=""){
		errStr+="\n请返回修改...";
		alert(errStr);
		return false;
	}
	else{
		waitSubmit("sub1","保存中...");
		waitSubmit("sub2","保存中...");
		return $("creSalEmp").submit();
	}
}