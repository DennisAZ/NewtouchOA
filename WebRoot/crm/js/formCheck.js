//负责账号是否为空
function checkBoxIsE(){
	var checkBoxs=document.getElementsByTagName("input");
	var isEmpty=true;
	for (var i=0;i<checkBoxs.length;i++) { 
		var e = checkBoxs[i];
		if (e.type=="checkbox"&&e.id.indexOf("C")!=0&&e.checked==true){ 
			isEmpty=false; 
		}
   	} 
   	if(isEmpty)
   		return true;
	else
		return false;
}

/* ----------------------- 重复名称判断 start -------------------------- */
/*
	提交表格触发
	arguments:每个参数为一个数组，第一个元素是重复判断方法的url，
							   第二个元素是有重复值的输入框id或对象数组，
							   第三个元素是原字段值，
							   第四个元素是错误提示层序号
*/
function checkRepeatForm() {
	var count=0;
	var errDiv="errDiv";
	var checksArr = arguments;
	for(var i=0;i<checksArr.length;i++){
		if(checksArr[i].length>2&&checksArr[i][2]!=null&&isEqualOld(checksArr[i][1],checksArr[i][2],checksArr[i][3])){
			//表单值未改变
			count++;
			if(count==checksArr.length){//判断循环结束
				check();
			}
		}
		else{
			var url=checksArr[i][0];
			if(Object.isString(checksArr[i][1])){
				checksArr[i][1]=$(checksArr[i][1]);//id名
			}
			if(!isArray(checksArr[i][1])){
				url+=encodeURIComponent(checksArr[i][1].value);
			}
			if(checksArr[i][3]!=undefined){//多个重复判断
				url+= "&divCount=" + checksArr[i][3];
			}
			url+= "&ran=" + Math.random();
			new Ajax.Request(url,{ 
				method: 'get',
				onSuccess: function(transport) {
					var response="";
					if(transport.responseText.indexOf(',')!=-1){//多个重复判断
						response=transport.responseText.split(',');
						for(var j=0;j<checksArr.length;j++){
							if(response[1]==checksArr[j][3]){
								showRepeatDiv(transport.responseText,checksArr[j][1]);
								j=checksArr.length;//跳出循环
							}
						}
					}
					else{
						response=transport.responseText;
						showRepeatDiv(response,checksArr[0][1]);
					}
					count++;
					if(count==checksArr.length){//判断循环结束
						check();
					}
				}
			});
		}
		
	}
}

/*
	文本框输入触发
	obj:判断重复值的输入框(一个或多个)对象
	url:重复判断方法
	oldValue:编辑页面中字段原值(录入页面无此参数)
	errDivIndex:错误提示层序号(有多个错误提示层用)
*/
function checkIsRepeat(obj,url,oldValue,errDivIndex) {
	if(oldValue!=undefined&&oldValue!=null&&isEqualOld(obj,oldValue,errDivIndex)){
		return ;//编辑时未改变原值
	}
	else{
		if(Object.isString(obj)){
			obj=$(obj);
		}
		if(!isArray(obj)){
			url+=encodeURIComponent(obj.value);
		}
		if(errDivIndex!=undefined){
			url+="&divCount=" + errDivIndex;
		}
		url+=("&ran="+Math.random());//加随机数
		new Ajax.Request(url,{
			method: 'get',
			onSuccess: function(transport) {
				showRepeatDiv(transport.responseText,obj);
			}
		}); 
	}
}

//判断是否与原值相等
function isEqualOld(obj,oldValue,errDivIndex){
	if(obj!=null&&Object.isString(obj)){
		obj=$(obj);
	}
	if(!isArray(obj)){
		if(obj!=null){
			if(obj.value==oldValue){
				showRepeatInput(0,obj,errDivIndex);
				return true;
			}
		}
	}
	else{//对象数组
	
		var count=0;
		for(var i =0; i<obj.length;i++){
			if(obj[i].value==oldValue[i]){
				count++;
			}
		}
		
		if(count==obj.length){
			showRepeatInput(0,obj,errDivIndex);
			return true;
		}
		
	}
	return false;
}

/*
	显示重复层
	transport:ajax响应
	obj:触发重复判断事件的对象数组或对象id
*/
function showRepeatDiv(response,obj){
	var isRepeat;
	var divCount=null;
	var resp="";
	if(response.indexOf(',')!=-1){
		resp=response.split(',');
		isRepeat=resp[0];
		divCount=resp[1];
	}
	else{
		isRepeat = response;
	}
	showRepeatInput(isRepeat,obj,divCount);
}

//显示/隐藏重复提示层
function showRepeatInput(isRepeat,obj,divCount){
	var divId="isRepeat";
	var divId2="errDiv";
	if(divCount!=undefined&&divCount!=''){
		divId+=divCount;
		divId2+=divCount;
	}
	$(divId).value = isRepeat;
	if(isRepeat==1){
		//$(divId2).show();
		setErrStyle(obj,true,divId2);
	}
	else{
		//$(divId2).hide();
		setErrStyle(obj,false,divId2);
	}
	
}
//设置出错元素样式
function setErrStyle(obj,isErr,divId){
	if(isErr){
		color = "#FF0000";
		bgColor = "#fee5dd";
	}
	else{
		color = "#999";
		bgColor = "#fff";
	}
	
	if(typeof(obj) == 'string'){
		obj=$(obj);
	}
	if(obj.length==undefined){
		obj.style.borderColor=color;
		obj.style.backgroundColor=bgColor;
		if(isErr){
			$(divId).className = "floatTipsDiv";
			obj.onmousemove=function(){floatTipsLayer(divId,2,0);};
			obj.onmouseout=function(){floatTipsLayer(divId);};
		}
		else{
			$(divId).style.display = "none"; 
			obj.onmousemove=function(){};
			obj.onmouseout=function(){};
		}
	}
	else{//obj为对象数组
		for(var i =0; i<obj.length;i++){
			obj[i].style.borderColor=color;
			obj[i].style.backgroundColor=bgColor;
			if(isErr){
				$(divId).className = "floatTipsDiv";
				obj[i].onmousemove=function(){floatTipsLayer(divId,2,0);};
				obj[i].onmouseout=function(){floatTipsLayer(divId);};
			}
			else{
				$(divId).style.display = "none"; 
				obj[i].onmousemove=function(){};
				obj[i].onmouseout=function(){};
			}
		}
	}
}
/* ----------------------- 重复名称判断 end -------------------------- */

/*
	检查字符字数
	maxLength：字符长度（中文占两个字符）
*/
function checkLength(id,maxLength) {
	var str=$(id).value;
	if(str.length>maxLength){
		return true;
	}
	else{
		return false;
	}
}

function checkCnLength(id,maxLength) {
	var str=$(id).value;
	strLength=str.replace(/[^\x00-\xff]/g, "**").length;
	if(strLength>maxLength){
		return true;
	}
	else{
		return false;
	}
}

/*
	自动截断字符串
	maxLength：字数
*/
function autoShort(obj,maxLength){
	if(obj.value.length>maxLength){
		alert("输入文本过长,此次输入将被截断至"+maxLength+"个字符...");
		obj.value=obj.value.substring(0,maxLength);
	}
}


//自动清空不是数字的输入框
function checkIsNum(obj){
	var n = obj.value;
	if(n==""||n==null){
		return false;
	}
	else{
		if(isNaN(n)){
			n = n.replace(/^\D*(\d*\.\d+|\d+).*$/,'$1');
			if(/^\D+$/){
				n = n.replace(/^\D+$/,'');
			}
			if(parseMax(n)){
				obj.value=parseMax(n);
			}
			else{
				obj.value=n;
			}
			return false;
		} 
		else{
			if(parseMax(n)){
				obj.value=parseMax(n);
			}
			return true;
		}
	}
}

//自动清空不是整数的输入框
function checkIsInt(obj){
	var n = obj.value;
	if(n==""||n==null){
		return false;
	}
	else{
		if(!isInt(obj.id)){
			n = n.replace(/\D+/,'');
			if(parseMax(n)){
				obj.value=parseMax(n);
			}
			else{
				obj.value=n;
			}
			return false;
		} 
		else{
			if(parseMax(n)){
				obj.value=parseMax(n);
			}
			return true;
		}
	}
}

/*
	检查是否为空
*/
function isEmpty(id){
	var  isEmp=/^\s*$/;//匹配空行
	if($(id)==null||$(id).value==""||isEmp.test($(id).value)){
		return true;
	}
	else{
		return false;
	}
}

function isEmptyByName(name){
	var array=document.getElementsByName(name);
	for(var i=0;i<array.length;i++){
		if(array[i].checked==true){
			return false;
		}
	}
	return true;
}

/*
	检查是否整数
*/
function isInt(id){
	var  isInt=/^[-\+]?\d+$/;//匹配整数
	if(document.getElementById(id)==null||document.getElementById(id).value==""||isInt.test(document.getElementById(id).value)){
		return true;
	}
	else{
		return false;
	}
}
/* 自身的onpropertychange触发的方法中得到自己的对象，不能使用$()，会引起循环应用 */