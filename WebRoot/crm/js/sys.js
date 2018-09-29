/* 
	弹出层
*/
function addDiv(n,code){
	var width=680;
	var height=510;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var isDel = false;
	var hasTitle = true;
	var hasScroll = false;
	switch(n){	
		case 1:
			titleTxt="添加下级账号"+tips;
			height=280;
			width=450;
			url="userAction.do?op=goAddUser&curUserCode="+code;
			break;	
			
		case 2:
			titleTxt="新建账号"+tips;
			height=280;
			width=450;
			url="userAction.do?op=goAddUser";
			break;
				
        case 3:
			titleTxt="编辑账号信息"+tips;
			height=300;
			width=450;
			url="userAction.do?op=goUpdate&curUserCode="+code;
			break;	
			
		case 4:
			titleTxt="关闭账号";
			url="userAction.do?op=delConfirm&code="+code+"&delType=1";
			isDel = true;
			break;
			
		//case 5:
			//titleTxt="添加同级账号"+tips;
			//height=350;
			//width=500;
			//url="userAction.do?op=goAddEmp&userCode="+code+"&sub=sub";
			//break;	
	    
		//------------------职位管理------------------------

		case 6:
			titleTxt="修改职位"+tips;
			height=240;
			width=420;
			url="userAction.do?op=goUpdateRole&rolId="+code;
			break;
		case 7:
			titleTxt="删除职位";
			url="userAction.do?op=delConfirm&code="+code+"&delType=2";
			isDel = true;
			break;	
		
		case 8:
			titleTxt="添加职位"+tips;
			height=240;
			width=420;
			url="system/newRole.jsp";
			break;
			
		case 9:
			url="system/roleLev.jsp";
			width=258;
			height=52;
			hasTitle = false;
			break;
			//修改授权信息
		case 10:
			titleTxt="导入授权信息"+tips;
			height=150;
			width=300;
			url="system/updateSystem.jsp";
			break;
			
	}
	if(isDel){
		width=250;
		height=136;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll,hasTitle);
}

/* 判断登录名*/
function checkLoginName(oldName){
	var name=$("login").value;
	var url = "userAction.do?op=checkLoginName&loginName="+name+"&oldName="+oldName;
	request.open("GET", url, true);
	request.onreadystatechange =getInfo;
	request.send(null);
} 
function getInfo() {
	 if (request.readyState == 4)
	  if (request.status == 200)
	       {
	           var response = request.responseText;
	           response=response.replace(/[\r\n]/g,"");
	           var span=$("info");
	           if(response!=null&&response!="")
	           {
	             var rpc_str="<span class='red'>"+response+"</span>";
	             span.innerHTML=rpc_str;
	             $("Submit").disabled=true;
	           }
	           else
	           {
	              span.innerHTML="";
	              $("Submit").disabled= false;
	           }
	     }
	       
	    else if (request.status == 404)
	         alert("您访问的url地址不存在！");
	    else
	         alert("Error: status code is " + request.status);
}


