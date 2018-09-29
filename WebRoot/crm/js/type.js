/* 提交表单 */
function doSubmit(){
	var types = document.getElementsByName("typeName");
	for(var i=0;i<types.length;i++){
		if(types[i].value==""){
			alert("类别名称不能为空！");
			types[i].focus();
			return false;
		}
	}
	waitSubmit("doSave","保存中...");
	$("typeSaveForm").submit();
}

function nameCheck(obj){
	autoShort(obj,50);
}

/* 
	弹出层
*/
function addDivNew(n,arg1,arg2){
	var width=300;
	var height=140;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";

	switch(n){	
		case 1:
			titleTxt="添加"+arg1;
			url="typeManage/addType.jsp";
			break;	
		case 2:	
			height=110;
			titleTxt="添加产品类别";
			url="typeManage/addProType.jsp";
			break;
		case 3:	
			titleTxt="添加国家或地区";
			url="typeManage/addCountry.jsp";
			break;
		case 4:	
			height=200;
			titleTxt="添加城市";
			if(arg1!=null){
				url="customAction.do?op=toAddCity&id="+arg1;
			}
			else{
				url="customAction.do?op=toAddCity";
			}
			break;
		case 5:	
			height=170;
			titleTxt="添加省份";
			if(arg1!=null){
				url="customAction.do?op=toAddPro&id="+arg1;
			}
			else{
				url="customAction.do?op=toAddPro";
			}
			break;
	}
	
	createPopWindow(idPre,titleTxt,url,width,height);
}