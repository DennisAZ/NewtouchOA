//生成项目执行人列表
function getUserListHTML(userList){
	var str="<ul>";
	for(var i=0;i<userList.length;i++){
		str+="<li>"+userList[i].getAttribute("userName")+"&nbsp;"
		if(userList[i]!=null){
			switch(userList[i].getAttribute("taskSta")){
				case '0':
					str+="<img style='vertical-align:middle;' src='images/content/doing.gif' alt='执行中'/>";
					break;
				case '1':
					str+="<img style='vertical-align:middle;' src='images/content/tofinish.gif' alt='已提交'/>";
					break;
				case '2':
					str+="<img style='vertical-align:middle;' src='images/content/alert.gif' alt='被退回'/>";
					break;
				case '3':
					str+="<img style='vertical-align:middle;' src='images/content/finish.gif' alt='已完成'/>";
					break;
			}
			str+="</li>";
			
		}	
		
	}
	str+="</ul>";
	return str;
}


/*
 选择项目类型跳转
*/
function selectProject(n){
	switch(n){
		case 1:
			self.location.href="projectAction.do?op=getMyProject";
			break;
		case 2:
			self.location.href="projectAction.do?op=getAllProject";
			break;
	}
}
/* 选择工作安排类型跳转 */
function selectProTask(n){
	switch(n){
		case 1:
			self.location.href="projectAction.do?op=getMyProTask";
			break;
		case 2:
			self.location.href="projectAction.do?op=getMySenTask";
			break;
		case 3:
			self.location.href="projectAction.do?op=allSenTaskSearch";
			break;
	}
}
/*
 选择行动历史类型跳转
*/
function selectHisTask(n){
	switch(n){
		case 1:
			self.location.href="projectAction.do?op=findMyHisTask";
			break;
		case 2:
			self.location.href="projectAction.do?op=findAllHisTask";
			break;
	}
}
/* 
	弹出层（新建）	
*/
function projPopDiv(n,arg1,arg2,arg3){
	var height;
	var width;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	var url;
	
	switch(n){
		case 1:	
			height=410;
			titleTxt="新建项目"+tips;
			url="projectAction.do?op=toNewProject";
			break;
		case 11:
		 	titleTxt="编辑项目"+tips;
		 	height=410;
		 	url="projectAction.do?op=toUpdProj&proId="+arg1;
		 	break;
		case 12:	
			height=235;
			width='half';
			titleTxt="新建子项目"+tips;
			url="projectAction.do?op=toNewSubPro";
			break;
		case 13:
			titleTxt="编辑子项目"+tips;
			height=235;
			width='half';
			url="projectAction.do?op=toUpdSubPro&staId="+arg1;
			break;
			
		case 2:	
			height=305;
			titleTxt="新建项目日志"+tips;
			url="projectAction.do?op=toNewHisProTask&proId="+arg1;
			break;
		case 21:
			titleTxt="查看项目日志";
			url="projectAction.do?op=showHisProTaskDesc&prtaId="+arg1;
			height=245;
			hasScroll = true;
			break;
	  /* case 3:	
			height=400;
			titleTxt="新建项目工作"+tips;
			hasScroll = true;
			url="project/newProTask.jsp";
			break;*/
	  /* case 4:	
			height=305;
			titleTxt="新建项目日志"+tips;
			url="project/newHisProTask.jsp";
			break;*/
		case 3:
			height=300;
			titleTxt="新建询价单"+tips;
			url="salPurAction.do?op=goNewInp&sInfo=sy&pInfo=pn&proId="+arg1+"&proName="+encodeURIComponent(arg2);
			break;
		case 31:
			height=300;
			titleTxt="编辑询价单"+tips;
			url="salPurAction.do?op=goUpdateInq&inqId="+arg1+"&isIfrm=1";
			break;
			
		case 4:
			height=325;
			titleTxt="新建报价记录"+tips;
			url="cusServAction.do?op=toNewQuote&pInfo=pn&oppId="+arg1+"&oppTitle="+encodeURIComponent(arg2);
			break;
		case 41:
			height =325;
			titleTxt="编辑报价记录"+tips;
			url="cusServAction.do?op=toUpdQuo&id="+arg1;
			break;
			
		/*case 23:
			height=470;
			titleTxt="编辑采购单"+tips;
			url="salPurAction.do?op=goUpdateSpo&spoId="+code;
			break;*/
		/*case 6:
			height=500;
			titleTxt="新建采购单"+tips;
			url="salPurAction.do?op=goNewSpo&pInfo=pn&proId="+arg1+"&proName="+encodeURIComponent(arg2);
			break;*/
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}
/* 
	弹出层（编辑删除）	
*/
function projDelDiv(n,id,isIfrm){
	var width;
	var	height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var url;
	
	switch(n){
		case 1:
			titleTxt="删除项目";
			break;		
		case 2:
			titleTxt="删除子项目";
			break;
		case 3:
			titleTxt="删除询价单";
			break;
		case 4:
			titleTxt="删除报价记录";
			break;
		case 5:
			titleTxt="删除项目成员";
			break;
		
		/*case 24:
			titleTxt="删除采购单";
			url="supply/delSupConfirm.jsp?code="+code+"&delType=11";
			break;*/
		
		//---------------项目工作------------------
		/*case 5:
			titleTxt="编辑项目工作"+tips;
			height=400;
			url="projectAction.do?op=editProTask&prtaId="+code;
			hasScroll = true;
			break;	*/
		
		//删除
		/*case 6:
			titleTxt="删除项目工作";
			url="projectAction.do?op=delProConfirm&code="+code+"&delType=3";
			isDel = true;
			break;
		case 7:
			titleTxt="完成情况";
			height=350;
			width=400;
			url="projectAction.do?op=showMyTask&ptlId="+code;
			hasCover = false;
			hasScroll = true;
			break;
		case 8:	
			height=400;
			titleTxt="新建项目工作"+tips;
			url="projectAction.do?op=forWardNewProTask&proId="+code;
			hasScroll = true;
			break;
		
			//查看项目工作（补）
		case 16:
			titleTxt="查看项目工作";
			height=350;
			url="salTaskAction.do?op=salTaskDesc&stId="+code+"&sal=sal";
			hasScroll = true;
			break;
		case 17:
			titleTxt="完成情况";
			height=350;
			cusUpdateDiv.style.width="400px";
			url="salTaskAction.do?op=showMyTask&taLimId="+code;
			hasCover = false;
			hasScroll = true;
			break;	
		case 18:
			titleTxt="编辑项目日志"+tips;
			height=300;
			width=480;
			url="projectAction.do?op=editHisProTask&prtaId="+code;
			break;	
		case 19:
			titleTxt="删除项目日志";
			url="projectAction.do?op=delProConfirm&code="+code+"&delType=4";
			isDel = true;
			break;*/
	}
	createConfirmWindow(idPre,titleTxt,url,width,height,id,n,isIfrm);
}

//返回删除实体名称，url数组
function getDelObj(delType,id){
	var delObj = new Array();
	switch(parseInt(delType)){
		case 1:
			delObj[0]="该项目";
			delObj[1]="projectAction.do?op=delProject&proId="+id;
			break;
		case 2:
			delObj[0]="该子项目";
			delObj[1]="projectAction.do?op=delSubPro&id="+id;
			break;
		case 3:
			delObj[0]="该询价单";
			delObj[1]="salPurAction.do?op=deleteInq&inqId="+id;
			break;
		case 4:
			delObj[0]="该报价记录";
			delObj[1]="cusServAction.do?op=delQuote&quoId="+id;
			break;
	   	case 5:
			delObj[0]="该项目成员";
			delObj[1]="projectAction.do?op=delProMember&actId="+id;
			break;
	}
	return delObj;
}
