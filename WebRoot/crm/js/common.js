/* ------------------------ */
/* ------- 公用脚本 -------- */
/* ------------------------ */
var DOC = document;

function getSalTaxRate(){
	var taxRate = 0.1;//固定税率
	return taxRate;
}

//生成下拉框
function createSelector(id,showValues,DBValues,defaultValue){
	if(showValues!=null){
		for(var i=0; i<showValues.length; i++){
			$(id).add(new Option(showValues[i],(DBValues!=null?DBValues[i]:showValues[i])));
		}
		if(defaultValue!=undefined&&defaultValue!=""){
			$(id).value=defaultValue;
		}
	}
}

//生成单选
function createRadio(id,cbName,showValues,DBValues,defaultValue){	
	if(showValues!=null){
		var inputHTML = [];
		for(var i=0; i<showValues.length; i++){
			inputHTML.push("<input type='radio' id='"+cbName+i+"' name='"+cbName+"' value='"+(DBValues!=null?DBValues[i]:showValues[i])+"' "
						   +(defaultValue?(defaultValue==(DBValues!=null?DBValues[i]:showValues[i])?"checked='checked'":""):(i==0?"checked='checked'":""))
						   +"/><label for='"+cbName+i+"'>"+showValues[i]+"&nbsp;</label>");
		}
		$(id).innerHTML=inputHTML.join("");	
	}
}

/*
	路径栏切换功能模块
	curMenu: 当前菜单项[0]模块类型,[1]当前菜单索引
	isShow:布尔值,显示或隐藏菜单
*/
function popFuncMenu(curMenu,isShow,e,handler){
	if($$("#funcMenu ul").length==0){ creatFuncMenu(curMenu); }
	if(isShow){
		$("changeFuncBt").className = "showFuncBt";
		$("funcMenu").show();
		$("changeFuncBt").hide();
	}
	else{
		$("changeFuncBt").className = "";
		$("funcMenu").hide();
		$("changeFuncBt").show();
	}
}
function creatFuncMenu(curMenu){
	var funcMenus ;
	var menuHTML = [];
	var menuWidth = 82;
	switch(curMenu[0]){
	case "case":
		funcMenus = [
			["案件导入","caseBatAction.do?op=toListBatXls&xlsState=3"],
			["批次管理","caseBatAction.do?op=toListBats&batState=0"],
			["案件管理","caseAction.do?op=toSearch&logstate=0"],
			["批次催收分析","caseAction.do?op=toSearch&logstate=1"],
			["主管协催","caseAction.do?op=toListCaseHurry&state=0&hType=10"] ];
		menuWidth = 110;
		break;
	case "hur":
		funcMenus = [
			["我的案件","caseAction.do?op=toListMyCase&state="],
			["来电查询","caseAction.do?op=toListPhoneSea&pstate=1"] ];
		break;
	case "caseAss":
		funcMenus = [
			["待处理信函","assitanceAction.do?op=toListCaseAss&state=0&dfType=1"],
			["信函记录","assitanceAction.do?op=toListCaseAss&state=1&dfType=1"],
			["待处理协催","assitanceAction.do?op=toListCaseAss&state=0"],
			["已处理协催","assitanceAction.do?op=toListCaseAss&state=1"],
			["待银行对账","cpListAction.do?op=toListCP&pastate=1"],
			["案件还款记录","cpListAction.do?op=toListCP&pastate=2"] ];
		menuWidth = 125;
		break;
	case "vis":
		funcMenus = [
			["待排程外访","visRecordAction.do?op=toUnVisList"],
			["已排程外访","visRecordAction.do?op=toVisList&state=2"],
			["已出发外访","visRecordAction.do?op=toVisList&state=3"],
			["已回程外访","visRecordAction.do?op=toVisList&state=4"] ];
		break;
	case "oa":
		funcMenus = [
			["内部消息","messageAction.do?op=toListAllMess"],
			["新闻公告","messageAction.do?op=toListAllNews&category=0"],
			["我的日程","messageAction.do?op=toShowSchList"],
			["工作安排","salTaskAction.do?op=toListMyTaskSearch"],
			["报告管理","messageAction.do?op=toListHaveSenRep"] ];
		break;
	case "emp":
		funcMenus = [
			["在职员工","empAction.do?op=toListEmps&workstate=0"],
			["离职员工","empAction.do?op=toListEmps&workstate=1"] ];
		break;
	case "sys":
		funcMenus = [
			["帐号设置","userAction.do?op=userManage"],
			["职位设置","userAction.do?op=searchLimRole"],
			["部门设置","empAction.do?op=findForwardOrg"],
			["类别设置","typeAction.do?op=findTypeList&typType=caseBat"],
			["模板管理","templateAction.do?op=toListTemplate&tmpType=assMail"],
			["回收站","empAction.do?op=toListDelSalEmp"] ];
		break;
	}
	for(var i=0; i<funcMenus.length; i++){
		if(i!=curMenu[1]){
			menuHTML.push("<li><a href='"+funcMenus[i][1]+"' style='width:"+menuWidth+"px;'>"+funcMenus[i][0]+"</a></li>");
		}
	}
	var ulObj = document.createElement("ul");
	ulObj.innerHTML = menuHTML.join("");
	$("funcMenu").appendChild(ulObj);
}

//String 转码
function encodeString(str){
	return str.replace(/'/g,"&acute;").replace(/"/g,"&quot;").replace(/\r\n/g,"\\n").replace(/\n/g,"\\n").replace(/\r/g,"\\n");
}

function stringFilter(obj,callBackFunc,callBackArgs){
	if(obj.value!=undefined){
		obj.value = obj.value.stripScripts(); 
	}
	else if(obj.innerText!=undefined){
		obj.innerText = obj.innerText.stripScripts();
	}
	if(callBackFunc!=undefined){
		callBackFunc(callBackArgs);
	}
}
//拼装列表中的隐藏域的值
function getValuesOfInps(inpName,hasParent){
	var inpObjs;//被选中的待删除的主键数组
	if(hasParent==undefined||hasParent){
		inpObjs=parent.document.getElementsByName(inpName);
	}
	else{
		inpObjs=document.getElementsByName(inpName);
	}
	var returnValues = "";
	for (var i = 0; i < inpObjs.length; i++) {
		if (inpObjs[i].checked==true){
			returnValues += inpObjs[i].value+",";
		}
	}
	return returnValues;
}

//得到批量操作ID
function getBacthIds(splitStr,hasParent){
	var priKey;//被选中的待删除的主键数组
	if(hasParent==undefined||hasParent){
		priKey=parent.document.getElementsByName("priKey");
	}
	else{
		priKey=document.getElementsByName("priKey");
	}
	var returnValue;
	if(splitStr!=undefined&&splitStr!=""){
		returnValue = new Array();
		for (var i = 0; i < priKey.length; i++) {
			if (priKey[i].checked==true){
				 var keyArray = priKey[i].value.split(splitStr);
				 for(var j=0;j<keyArray.length;j++){
					if(returnValue[j]==undefined){
						returnValue[j]="";
					}
					returnValue[j] += keyArray[j]+",";
				 }
			}
		}
		
	}
	else{
		returnValue = "";
		for (var i = 0; i < priKey.length; i++) {
			if (priKey[i].checked==true){
				returnValue += priKey[i].value+",";
			}
		}
	}
	return returnValue;
}

/* 
	保存cookie
	name:cookie名称
	value:cookie值
	hours:cookie失效时间
*/
function saveCk(name, value, hours){ 
	var expire = ""; 
	if(hours != null){ 
		expire = new Date((new Date()).getTime() + hours * 3600000); 
		expire = "; expires=" + expire.toGMTString(); 
	} 
	DOC.cookie = name + "=" + escape(value) + expire;
}
	
/*
	读取cookie
	name:cookie名称
*/
function readCk(name){
	var cookieValue = ""; 
	var search = name + "="; 
	if(DOC.cookie.length > 0){
		offset = DOC.cookie.indexOf(search); 
		if (offset != -1) {  
			offset += search.length; 
			end = DOC.cookie.indexOf(";", offset); 
			if (end == -1) end = DOC.cookie.length; 
			cookieValue = unescape(DOC.cookie.substring(offset, end)) 
		} 
	}
	return cookieValue; 
} 

/*
	加载列表标签样式-new
*/
function loadListTab(curValue,valueArray){
	for(var i=0;i<valueArray.length;i++){
		if(curValue==valueArray[i]){
			$("tabType"+(i+1)).className="tabTypeWhite";
			setOtherStyle((i+1),valueArray.length);
		}
	}
}
/*
	加载列表标签样式-old
*/
function loadTabType(range){
	switch(range){
		//我的
		case '0':
			$("tabType1").className="tabTypeWhite";
			setOtherStyle(1,3);
			break;
		//下属
		case '1':
			$("tabType2").className="tabTypeWhite";
			setOtherStyle(2,3);
			break;
		//全部
		case '2':
			$("tabType3").className="tabTypeWhite";
			setOtherStyle(3,3);
			break;
	}
}
//设置标签层宽度
function loadTabTypeWidth(){
	if($("tabType")!=null){
		$("tabType").style.width=(parseInt($("tabType").childElements()[0].offsetWidth-22)*($("tabType").childElements().length)+22)+"px";
	}
}

/* 
	页面权限判断（显示层）
	rigCode:功能点的权限码(多个权限码用|分隔)
	id:显示或隐藏的层的id(或层id数组)
	func:执行完ajax后的回调方法名
*/
function displayLimAllow(rigCode,id,func){
	isLimAllow(rigCode,'display',id,func);
}
function invokeFuncLimAllow(rigCode,func){
	isLimAllow(rigCode,'func',null,func);
}
/* 
	页面权限判断（输出html内容）
	rigCode:功能点的权限码(多个权限码用|分隔)
	html:输出的html内容(或html内容数组)
	appendId:输出html内容的页面元素id(或id数组)
	position:输出内容与页面元素的位置（before:在页面元素前显示 after:在页面元素后显示 top:在页面元素内部上方显示 bottom:在页面元素内部底部显示）
	func:执行完ajax后的回调方法名
*/
function writeLimAllow(rigCode,html,appendId,position,func){
	isLimAllow(rigCode,'write',html,func,appendId,position);
}
function isLimAllow(rigCode,outType,arg,func,appendId,position){
	var url = 'userAction.do';
	var pars = 'op=hasLimByAjax&rig=' + rigCode + "&ran=" + Math.random();
	new Ajax.Request(url,{
		method : 'get',
		parameters : pars,
		onSuccess : function(response){
			var resp = response.responseText.split('|');
			for(var i=0; i<resp.length;i++){
				//有权限
				if(resp[i] == 'true'){
					//输出html
					switch(outType){
					case  'write':
						var html ;
						if(isArray(arg)){
							html = arg[i];
						}
						else{
							html = arg;
						}
						var insertId;
						if(isArray(appendId)){//多个Id
							insertId = appendId[i];
						}
						else{
							insertId = appendId;
						}
						if(position!= undefined){
							switch(position){
								case 'before':
									$(insertId).insert({before:html});
									break;
								case 'after':
									$(insertId).insert({after:html});
									break;
								case 'top':
									$(insertId).insert({top:html});
									break;
								case 'bottom':
									$(insertId).insert({bottom:html});
									break;
							}
						}
						else{
							$(appendId).insert(html);
						}
						break;
					//切换层
					case 'display':
						if(isArray(arg)){
							$(arg[i]).show();
						}
						else{
							$(arg).show();
						}
						break;
					}
				}
				//无权限
				else{
					if(outType == 'display'){
						if(isArray(arg)){
							$(arg[i]).hide();
						}
						else{
							$(arg).hide();
						}
					}
				}
			}
			if(func != undefined && func != null) {
				func(resp);
			}
		},
		onfailure : function(response){
			if (transport.status == 404)
				alert("您访问的url地址不存在！");
			else
				alert("Error: status code is " + transport.status);
		}
	});
}


/*
var eventUtil = {
	addHandler: function(element, type, handler){
		if(element.addEventListener){
			elemet.addEventListener(type, handler, false);
		}
		else if(element.attachEvent){
			element.attachEvent("on"+type, handler);
		}
		else{
			element["on"+type] = handler;
		}
	};
	
	removeHandler: function(element, type, handler){
		if(element.removeEventListener){
			element.removeEventListener(type. handler, false);
		}
		else if(element.removeEvent){
			element.removeEvent("on"+type, handler);
		}
		else{
			element["on"+type] = ;
		}
	}
}
*/

/*浏览器判断*/
function getBrowser(){
	var ua = navigator.userAgent.toLowerCase(); 
	var s; 
	var browserVer;
	(s = ua.match(/msie ([\d.]+)/)) ? browserVer = 'IE:' + s[1] : 
	(s = ua.match(/firefox\/([\d.]+)/)) ? browserVer = 'FF:' + s[1] : 
	(s = ua.match(/chrome\/([\d.]+)/)) ? browserVer = 'CHM' + s[1] : 
	(s = ua.match(/opera.([\d.]+)/)) ? browserVer = 'OPR' + s[1] : 
	(s = ua.match(/version\/([\d.]+).*safari/)) ? browserVer = 'SAf' + s[1] : browserVer = '0'; 
	return browserVer;
}

//检查后缀名 file:文件路径，sufs:后缀名数组
function checkSuf(file,sufs){
	while (file.indexOf("\\") != -1) {
		file = file.slice(file.indexOf("\\") + 1);
	}
	var ext = file.slice(file.lastIndexOf(".")).toLowerCase();
	for (var i = 0; i < sufs.length; i++) {
		if (sufs[i] == ext) {
			return true;
		}
	}
	return false;
}

function reSizePic(id,max_w,max_h){
	if($(id)!=null){
		if($(id).width>max_w){
			$(id).width=max_w;
		}
		if($(id).height>max_h){
			$(id).height=max_h;
		}
	}
}

//下拉框显示选中账户余额
function showCountMon(selId,monId){
	var values=($(selId).options[$(selId).selectedIndex].value).split(",");
  	if(values[1]==null)
		$(monId).innerHTML="";
	else{
		if(values[1]!=""){
			var mon = numFormat(values[1]);
			if(mon.toString().indexOf(".")==-1){
				mon+=".0";
			}
			$(monId).innerHTML=mon;
		}
	   	else {
		 	$(monId).innerHTML="0.0";
		}
	}
	$(monId).className="";
}
		
//代替getElementsByName
function $N(){
	var results = [], element;        
  	for (var i = 0; i < arguments.length; i++) { // 可以传入多个参数      
    	element = arguments[i];         
    	if (typeof element == 'string') // 如果参数的类型是字符串            
      		element = DOC.getElementsByName(element);       
      	results.push(Element.extend(element)); // 将得到的对象加入results数组      
  	}      
  	// 如果只返回一个对象，直接返回该对象，如果返回了多个对象，则返回包含所有这些对象的数组      
  	return results.length < 2 ? results[0] : results; 
}

//导航链接样式
function loadGuideStyle(){
	var guides = $$(".guideLayer");
	
	for(var i = 0; i < guides.length; i++){
		guides[i].onmouseover=function(){
			if(this.className!="guideLayerDis"){ this.style.color="#234995"; }
			this.style.backgroundColor="#f0f5fe";
		};
		guides[i].onmouseout=function(){
			if(this.className!="guideLayerDis"){ this.style.color="#666"; }
			this.style.backgroundColor="#fff";
		};
	}
}


//载入头部筛选按钮样式
/*function loadTop(id){
	if(id==null){
		id="topChoose";
	}
	for(var i=0; i < $(id).childNodes.length; i++) { 
		var node = $(id).childNodes[i]; 
		if((node.nodeType != 3)&&((/\S/).test(node.nodeValue))){
			node.className="gray normal lightGrayBack quickBut";
			node.onmouseover=function(){
				this.className='blue normal lightGrayBack quickBut';
			}
			node.onmouseout=function(){
				this.className='gray normal lightGrayBack quickBut';
			}
		}
	}
}*/
//载入头部选中按钮样式
function setCuritemStyle(a1,a2,id){
	if(arguments.length < 3){
		id="topChoose";
	}
	var j=0;
	for(var i=0;i<$(id).childNodes.length;i++){
		if($(id).childNodes[i].nodeName=="A"){
			if(a1==a2[j]){
				$(id).childNodes[i].className="curitem";
			}
			else{
				$(id).childNodes[i].className="";
			}
			j++;
		}
	}
}
//载入iframe
function loadIfmSrc(id,src){
	$(id).src=src;
}


//清除空白,换行节点 
function cleanWhitespace(node) { 
	var notWhitespace = /\S/; 
	for (var i=0; i < node.childNodes.length; i++) { 
		var childNode = node.childNodes[i]; 
	  	if((childNode.nodeType == 3)&&(!notWhitespace.test(childNode.nodeValue))) { 
			node.removeChild(node.childNodes[i]); 
		  	i--; 
	  	} 
	  	if (childNode.nodeType == 1) { 
		  	cleanWhitespace(childNode); 
	  } 
    } 
} 

/* 弹出详情 */
function descPop(url){
	window.open(url,"","directories=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
}
/* 使用post打开新页面 */
function openPostWindow(url, args, name){
	var tempForm = document.createElement("form");
	tempForm.id="tempForm";
	tempForm.method="post";
	tempForm.action=url;
	tempForm.target=name;
	tempForm.style.display="none";
	for(var i=0; i<args.length; i++){
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name=args[i][0];
		hideInput.value=args[i][1];
		tempForm.appendChild(hideInput);  
	}
	tempForm.attachEvent("onsubmit",function(){window.open("about:blank",name,"directories=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no"); });
	document.body.appendChild(tempForm);
	tempForm.fireEvent("onsubmit");
	tempForm.submit();
	document.body.removeChild(tempForm);
}

/* 表格鼠标滑过高亮 */
function focusTr(id,count,isMouseOver){
	var obj=$(id+count);
	if(isMouseOver==1){
		obj.oldBg = obj.style.backgroundColor;
		obj.style.backgroundColor="#fef97d";
	}
	else {
		obj.style.backgroundColor = obj.oldBg;
	}
}
function focusTr2(id,count,isMouseOver){
	var obj=$(id+count);
	if(isMouseOver==1){
		obj.oldBg = obj.style.backgroundColor;
		obj.style.backgroundColor="#fef97d";
	}
	else {
		obj.style.backgroundColor = obj.oldBg;
	}
}

/* 隔行换色 */
function rowsBg(id,count){
	if(count%2!=0){
		//$(id+count).style.backgroundColor="#eeecec";
		$(id+count).style.backgroundColor="#f4f6f9";
	}
	else{
		$(id+count).style.backgroundColor="#ffffff";
	}
}
function rowsBg2(id,count){
	if(count%2!=0){
		$(id+count).style.backgroundColor="#f5f3f3";
	}
	else{
		$(id+count).style.backgroundColor="#ffffff";
	}
}


/*
	生成编辑，删除按钮
	id1:鼠标滑过层的id
	id2:包含编辑，删除按钮的隐藏层id
*/
function loadEditSpan(id1,id2){
		
	var obj1=$(id1);
	var obj2=$(id2);
	obj1.onmouseover=function(){
		obj2.style.display="";
	}
	obj1.onmouseout=function(){
		obj2.style.display="none";
	}
}


//生成iframe进度条
function createIfmLoad(id){
	DOC.write('<div id="'+id+'" class="frameLoad"><img src="images/gif/uploading.gif"/>&nbsp;数据加载中...</div>');
}

//加载iframe结束
function loadEnd(id){
	setTimeout(
		function(){
			if($(id)!=null){
				$(id).hide();}
		},100);
	;
}

//加载iframe高度
function loadAutoH(obj,id){
	obj.height=obj.contentWindow.document.body.scrollHeight;
	loadEnd(id);
}


/*
	生成进度条
*/
function createProgressBar(){
	if($("proBar")==null){
		DOC.write('<div id="proBar">&nbsp;</div>');
		var proBar1 = $("proBar");
		proBar1.style.display="block";
		proBar1.className="proBarStyle";
		proBar1.innerHTML="数据加载中&nbsp;<img src='crm/images/gif/dot.gif' style='vertical-align:middle'/>";
		proBar1.style.top=DOC.body.scrollTop+10+"px";
	}
	else{
		var proBar2 = $("proBar");
		if(proBar2.style.display == "none"){
			proBar2.style.display="block";
			proBar2.style.top=DOC.body.scrollTop+10+"px";
		}
	}
}

/*
	关闭进度条
*/
function closeProgressBar(){
	setTimeout(
		function(){
			if($("proBar")!=null){
				$("proBar").style.display="none";}
		},100);
}

function changeBg(obj,isChange){
	obj.className=isChange==1?"divFocus":"divBlur";
}

function checkBoxIsEmpty(name){
	var cbs = $N(name);
	var isNotEmpty=false;
	for(var i=0;i<cbs.length;i++){ 
		if(cbs[i].checked==true){ 
			isNotEmpty=true;
			break;
		}
	}
	if(!isNotEmpty){
		alert("请先在列表中勾选数据！");
	}
	return isNotEmpty;
}
/*
	判断批量删除时是否已经勾选要删除的内容
	type:要删除的实体类型
*/
function checkBatchDel(divType,delName,type){
	if(checkBoxIsEmpty("priKey")){
		batchAddDiv(divType,type);
	}
}
/* 批量操作全选多选框  */
function selectAll(){    
	var allCheck=$("allCheck");
	var priKey= $N("priKey");
	allCheck.allClicked=false;
	for (var i = 0;i<priKey.length;i++){
		if(i == priKey.length-1){
			allCheck.allClicked=true;//标识完成全选动作
		}
		if((allCheck.checked == true && priKey[i].checked ==false)||(allCheck.checked == false && priKey[i].checked ==true)){
			priKey[i].click();
		}
   	}
   	
}

/* 载入值 */
function loadValue(id,value){
	$(id).value=value;
}

/*
	设置选项卡其他标签样式
	s:选中标签id最后的数字序号
*/
function setOtherStyle(s,maxCount){
	var count=1;
	for(var i=1;i<=maxCount;i++){
		if(i!=s&&$("tabType"+i)!=null){
			$("tabType"+i).className="tabTypeBlue"+count;
			$("tabType"+i).onmouseover=function(){
				this.className="tabTypeOver"+this.className.substring(11);
			};
			$("tabType"+i).onmouseout=function(){
				this.className="tabTypeBlue"+this.className.substring(11);
			};
			count++;
		}
	}
}
/* 选项卡鼠标滑过变亮 */
function changeTypeBg(obj,isMouseOver,n) {
	obj.className=isMouseOver==1?"tabTypeOver"+n:"tabTypeBlue"+n;
}

/* --------------------------------------- 表单  ------------------------------------------------------------- */

//表单提交
function formSubmit(formId){
	setTimeout(function(){$(formId).submit();},0);//IE6下直接调用formSubmit不提交,需延迟
}

//清空input
function clearInput(obj,id){
	if(obj!=null){
		obj.value="";
	}
	if(id!=undefined){
		if(isArray(id)){
			for(var i = 0; i<id.length; i++){
				if($(id[i])!=null){
					$(id[i]).value="";
				}
			}
		}
		else{
			if($(id)!=null){
				$(id).value="";
			}
		}
	}
}


/*
	创建清空表单按钮 （将此方法放入window.onload中，因为在页面未加载完成前ie6无法得到document.body对象）
	formId:表单id
	offsetX: 提示层水平方向偏移
	offsetY: 提示层垂直方向偏移
	elId: 需要在指定对象旁显示时传入按钮Id（可选参数）
	pos: 清空按钮位于指定对象的相对位置(after,before,top,bottom)
*/
function createCancelButton(loadFunc,formId,offsetX,offsetY,elId,pos){
	//创建浮动层
	var cancelDiv = DOC.createElement("div");
	cancelDiv.id="cancelSearTips";
	cancelDiv.className="floatTipsDiv";
	cancelDiv.style.display="none";
	cancelDiv.innerHTML = "清空查询条件，查看全部数据";
	if($("cancelSearTips")==null){
		DOC.body.appendChild(cancelDiv);
	}
	//创建清空按钮
	var cancelButton = DOC.createElement("a");
	cancelButton.href="javascript:void(0)";
	cancelButton.onclick=function(){
		cancelSearch(formId);
		if(loadFunc!=undefined){
			loadFunc();
		}
		//formSubmit(formId);
	}
	cancelButton.onmouseover=function(){
		floatTipsLayer('cancelSearTips',offsetX,offsetY);
	}
	cancelButton.onmouseout=function(){
		floatTipsLayer('cancelSearTips');
	}
	cancelButton.innerHTML="清空";
	if(elId!=undefined){
		switch(pos){
			case 'after':
				cancelButton.style.marginLeft="5px";
				$(elId).insert({after:cancelButton});
				break;
			case 'top':
				$(elId).insert({top:cancelButton});
				break;
			case 'bottom':
				$(elId).insert({bottom:cancelButton});
				break;
			default:
				cancelButton.style.marginRight="5px";
				$(elId).insert({before:cancelButton});
		}
	}
	else{
		$(formId).appendChild(cancelButton);
	}
}

//清空查询表单
function cancelSearch(formId){
	var inputs = $$('#'+formId+' *');
	for(var i=0;i<inputs.length;i++){
		var inputEl = inputs[i];
		if(!inputEl.disabled){
			if(inputEl.type=="text"||inputEl.type=="textarea"){
				inputEl.value="";
			}
			else if(inputEl.type=="select-one"||inputEl.type=="select-multiple"){
				inputEl.selectedIndex=0;
			}
		}
	}
}

/*
	灰化表单输入项
	ids: 元素Id，如多个元素用,分隔
	oldTitles: 保存输入元素原title的变量
*/
function disableInput(ids){
	if(ids.indexOf(',')==-1){//单个元素灰化
		if(!$(ids).disabled){
			$(ids).setAttribute('oldTitle',$(ids).title);//保存原title
			$(ids).disabled = true;
			$(ids).title = "此项暂时无法编辑";
		}
	}
	else{//多个元素灰化
		var idsArr = ids.split(',');
		for(var i = 0; i<idsArr.length; i++){
			disableInput(idsArr[i]);
		}
	}
}
/*
	还原表单输入项
	ids: 元素Id，如多个元素用,分隔
	oldTitles: 保存输入元素原title的变量
*/
function restoreInput(ids){
	if(ids.indexOf(',')==-1){//单个元素灰化
		if($(ids).disabled){
			$(ids).disabled = false;
			if($(ids).oldTitle != undefined){
				$(ids).title = $(ids).oldTitle;//恢复原title
			}
			else{
				$(ids).title = "";
			}
		}
	}
	else{//多个元素灰化
		var idsArr = ids.split(',');
		for(var i = 0; i<idsArr.length; i++){
			restoreInput(idsArr[i]);
		}
	}
}


//全选按钮
function checkAll(name,id){ 
	var array= $N(name);
	for(var i=0;i<array.length;i++)
		array[i].checked=$(id).checked;
}

//高级查询负责账号初始化
function showCheckBox(limCode,treeName){
	var checkBoxs=$$("input");
	var box=limCode.split(",");
	for(var i=0;i<box.length;i++){
		for (var j=0;j<checkBoxs.length;j++){
			var e = checkBoxs[j];
			if (e.type=="checkbox"&&e.id.indexOf("cb"+treeName)!=-1&&e.value==box[i]){ 
				e.checked=true; 
			} 
		} 
	}
 }
 
/*设置权限页面多选框全选 */
 function cascadeSel(id,id1){
	var chkbs=$$('#'+id+' input');
	var allCheck=$(id1);
	for (var i = 0;i<chkbs.length;i++)
	{
		if (allCheck.checked == true)
		   chkbs[i].checked = true;  
		else
		   chkbs[i].checked = false;
   }
 }
 
 
/* 
	提交表单时屏蔽提交按钮
	id:按钮Id
	str:提交按钮时按钮内的文本（无此参数则不改变原文本）
*/
function waitSubmit(id,str){
	var obj ;
	if(typeof(id)=='object'){
		obj = id;
	}
	else {
		obj = $(id);
	}
	if(obj!=null){
		obj.disabled=true;
		if(str!=undefined){
			if(obj.value!=null){
				obj.value=str;
			}
			else{
				obj.innerHTML=str;
			}
		}
	}
}
/* 
	恢复提交按钮
	id:按钮Id
	str:提交按钮内的原文本
*/
function restoreSubmit(id,str){
	var obj ;
	if(typeof(id)=='object'){
		obj = id;
	}
	else {
		obj = $(id);
	}
	if(obj!=null){
		obj.disabled=false;
		if(str!=undefined){
			if(obj.value!=null){
				obj.value=str;
			}
			else{
				obj.innerHTML=str;
			}
		}
	}
}


/* ----------------------------------------- 层切换 显示 -------------------------------------------------------------------- */

//载入详情关联数据选中标签，触发点击事件
function loadXpTabSel(){
	var tabIndex = 0;
	for(var i=1;i<=$$('.xpTabGray').length;i++){
		if($("xpTab"+i).style.display == "none"){
			continue;
		}
		else {
			tabIndex=i;
			break;
		}
	}
	if(tabIndex != 0){
		if(DOC.all){
			//IE
			$("xpTab"+tabIndex).click();
		}    
		else{
			//FF
			var evt = DOC.createEvent("MouseEvents");
			evt.initEvent("click",true,true); 		
			$("xpTab"+tabIndex).dispatchEvent(evt);
		}
	}
}


//显示大图
function showPic(count){ 
	var obj = $("picLayer"+count);
	cursorDiv(obj,10,-80);
	obj.style.display = "block"; 
} 
			
//隐藏大图
function hiddenPic(count){
	$("picLayer"+count).style.display = "none"; 
} 	

//鼠标旁显示层
function cursorDiv(obj,offsetX,offsetY){
	var x,y; 
	x = event.clientX + DOC.body.scrollLeft; 
	y = event.clientY + DOC.body.scrollTop; 
	obj.style.left = x+offsetX; 
	obj.style.top = y+offsetY;
}
function floatTipsLayer(id,offsetX,offsetY){
	var obj;
	if(typeof(id) == 'string'){
		obj = $(id);
	}
	else{
		obj = id;
	}
	if(arguments.length==3){
		cursorDiv(obj,offsetX,offsetY);
		obj.show();
	}
	else{
		obj.hide();
	}
}

//显示并隐藏层，showId为要显示的层，hideId为要隐藏的层
function showAndHide(showId,hideId){
	if($(showId)!=null&&$(hideId)!=null){
		$(showId).show();
		$(hideId).hide();
	}
}

//隐藏显示层
function showDiv(divId){
	var div=$(divId);
	if(div.style.display=="none")
		div.style.display="block";
	else
		div.style.display="none";
}

/* 展开收缩层(带+,-号图片) */
function showHide(divId,imgId){
	var target=$(divId);
	if (target.style.display==""){
		target.hide();
		if(imgId!=null){
			$(imgId).src="images/content/showmore.gif";
			$(imgId).alt="点击展开";
		}
	} 
	else {
		target.show();
		if(imgId!=null){
			$(imgId).src="images/content/hide.gif";
			$(imgId).alt="点击收起";
		}
	}
}

// 详情选项卡 
function swapTab(i,n,url) {
	url += "&ran=" + Math.random();
	if($("ifrList")==null){
		//临时
		for(var j=1;j<=n;j++){
			if($("xpTab"+j)!=null){
				if(j==i){
					$("xpTab"+j).className="xpTabSelected";
					$("tabContent"+j).show();
					if(url!=null){
						var ifr="ifr"+i;
						$(ifr).src=encodeURI(url);
					}
				}
				else {
					$("xpTab"+j).className="xpTabGray";
					$("tabContent"+j).hide();
				}
			}
		}
	}
	else{
		for(var j=1;j<=n;j++){
			if($("xpTab"+j)!=null){
				if(j==i){
					$("xpTab"+j).className="xpTabSelected";
					$("ifrContent").show();
					if(url!=null){
						$("ifrList").src=encodeURI(url);
					}
				}
				else {
					$("xpTab"+j).className="xpTabGray";
				}
			}
		}
	}
}

/* -------------------------- 表单验证 --------------------------- */

//整数验证
function isint(str)
{
	var result=str.match(/^(-|\+)?\d+$/);
	if(result==null) return false;
	return true;
}

/* -------------------------- 格式转换 --------------------------- */

//传入的参数是否为数组类型
function isArray(arr){
	return Object.prototype.toString.apply(arr) === '[object Array]';
}


//缩略字符串
function shortText(id,maxLength){
	var innerText = shortTextImpl($(id).innerHTML,maxLength);
	if(innerText!=null){
		$(id).innerHTML = innerText;
		return true;
	}
	else{
		return false;
	}
}
function shortTextImpl(str,maxLength){
	if(str.length > maxLength ){
		str = str.substring(0,maxLength) + "...";
		return str; 
	}
	else{
		return null;
	}
}

//缩略层
function shortDiv(id,maxHeight){
	var curHeight = $(id).offsetHeight;
	if(curHeight > maxHeight ){
		$(id).style.height = maxHeight + 'px';
		$(id).style.overflow = 'hidden';
		return true;
	}
	else{
		return false;
	}
}

//保留小数点后两位(金额)
function changeTwoDecimal(s){
	s=numFormat(s);
	//0就直接返回
	if(s=="0"){
		return "0.00";
	}

	//非数字返回0
	else if(/[^0-9\.\-]/.test(s)){
		return "0.00";
	}
    s=s.toString().replace(/^(\-?)(\d*)$/,"$1$2.");//匹配小数点前
    s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");//保留两位小数
    s=s.replace(".",",");
    var re=/(\d)(\d{3},)/;
    while(re.test(s))//三位加，分隔
        s=s.replace(re,"$1,$2");
    s=s.replace(/,(\d\d)$/,".$1");//把最后两位前的，转换
	s=s.replace(/^\./,"0.");//没有整数位的补0
    return s;
}
//保留小数点后n位(默认两位)
function formatFloat(num,n){
	var floatNum=numFormat(num);
	
	if(n==undefined){
		n=2;
	}
	var tempNum = Math.pow(10,n);
   	floatNum = Math.round(floatNum*tempNum)/tempNum;

   	return floatNum;
}


//价格输入格式自动更正
function checkPrice(obj){ 
	var n = obj.value;
	if(n==""||n==null){
		return false;
	}
	else{
		if(!(/^(\d+|\d+\.\d{0,2})$/.test(n))){
			n = n.replace(/^\D*(\d*\.\d{0,2}|\d+).*$/,'$1');
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

//数字最大位判断
function parseMax(str){
	var i = str.indexOf(".");
	var l = str.length;
	//整数
	if((i==-1&&l>9)||i>9){
		str="999999999";
		return str;
	}
	//小数
	else if(l>11){
		str=str.substring(0,9);
		return str;
	}
	else{
		return false;
	}

}

//分解科学计数法
function numFormat(s){
	s=s.toString().toLowerCase();
	if(s.indexOf('e')!=-1){
		var i=s.indexOf('e');
		var n=s.substring(0,i);
		var count=s.substring(i+1,s.length);
		s=parseFloat(n)*Math.pow(10,parseInt(count));
		if(s.toString().indexOf('e')!=-1){
			s=s.toFixed(15);//精确到小数点后15位
		}
	}
	else if(isNaN(s)){
		s=0;
	}
	return s;
}

//将BR转换为换行
function parseBR(id,str){
	if($(id)!=null){$(id).value=str.replace(/<br\/>/g,"\r\n");}
}

/*
	输入框日期的初始化
*/
function dateInit(id,dateValue){
	if($(id)!=null){$(id).value=dateValue.substring(0,10);}
}

/*
	输入框日期的初始化
*/
function dateInitToShow(id,dateValue){
	if($(id)!=null){$(id).innerHTML=dateValue.substring(0,10);}
}

/* 
	批量格式化日期 
	labelPre：标签前缀
	count： 	  标签后缀
	date:	  日期值
*/
function dateFormat(labelPre,date,count){
	var obj=labelPre+count;
	removeTime(obj,date,1);
}
function dateFormat2(labelPre,date,count){
	var obj=labelPre+count;
	removeTime(obj,date,2);
}
function dateFormat3(labelPre,date,count){
	var obj=labelPre+count;
	removeTime(obj,date,3);
}

/*
	type=1:去时分秒   
	type=2:去掉日期后的秒和毫秒   
	type=3:去掉年份
*/
function removeTime(id,date,type){
	if($(id)!=null){
		var today;;
		var tomorrow;
		var afTom;
		var yesterday;
		var bfYesd;
		//从当前页面取值
		if($("curPage")!=null&&$("curPage").value=="1"){
			today=$("today");
			tomorrow=$("tomorrow");
		 	afTom=$("afTom");
		 	yesterday=$("yesterday");
		 	bfYesd=$("bfYesd");
		}
		else{
			today=parent.document.getElementById("today");
			tomorrow=parent.document.getElementById("tomorrow");
		 	afTom=parent.document.getElementById("afTom");
		 	yesterday=parent.document.getElementById("yesterday");
		 	bfYesd=parent.document.getElementById("bfYesd");
		}
		if(today!=null&&today.value==date.substring(0,10)){
			$(id).innerText="今天";
		}
		else if(tomorrow!=null&&tomorrow.value==date.substring(0,10)){
			$(id).innerText="明天";
		}
		else if(afTom!=null&&afTom.value==date.substring(0,10)){
			$(id).innerText="后天";
		}
		else if(yesterday!=null&&yesterday.value==date.substring(0,10)){
			$(id).innerText="昨天";
		}
		else if(bfYesd!=null&&bfYesd.value==date.substring(0,10)){
			$(id).innerText="前天";
		}
		else{
			switch(type){
				case 1:
					$(id).innerText=date.substring(0,10);
					break;
				case 2:
					$(id).innerText=date.substring(0,16);
					break;
				case 3:
					$(id).innerText=date.substring(5,10);
					break;
			}
		}
	}
}


//附件大小格式化
function formatSize(size,count){
	var sizeOfKb=changeTwoDecimal(parseInt(size)/1024)+"KB";
	if($("fileSize"+count)!=null){
		$("fileSize"+count).innerHTML="("+sizeOfKb+")";
	}
	else{
		return sizeOfKb;
	}
	
}

/*--------------------------*
 * @class MGrid 动态表格对象
 * @author GM   	
 *--------------------------*/

/**
 * 构造函数
 * @param tabId				:	表格ID名
 * @param containerLayerId 	:	容器层元素ID
 */
function MGrid(tabId,containerLayerId){
	this.id = tabId; 				//表格ID名
	this.layerId = containerLayerId;//容器层元素ID
	this.colCount = 0;				//列数	
	this.colArr ;					//保存表头数据、格式
	this.gridStr;					//保存表格内容字符串
	this.url;						//查询列表url 
	this.pars;						//查询参数
	this.loadFunc;					//页面查询JS方法名
	this.sortCol;					//排序列的序号（被隐藏的列也有序号） 
	this.isDe;						//是否降序（1为降序，0为升序） 
	this.pageSize;					//每页显示条数
	this.curP;						//当前页码
	// 统计属性 
	this.dataXML;					//统计图表xml
	
	//表格配置内容
	this.config = {
		className: 		'',			//自定义样式
		hasCheckBox: 	false,		//是否显示多选框
		hasPage: 		true,		//是否有分页
		sortable:		true,		//是否可排序
		isResize: 		true,		//是否可拉伸改变列宽
		isShort:		true,		//是否缩略内容
		listType:		'simple',	//列表类型
		emptyTabShow:	true,		//无数据时是否显示表头列
		hasSumRow:		false,		//是否生成合计行
		sumRowCallBack:	null,		//重新生成合计行回调方法
		selCallBackFunc:null		//统计选中数据回调方法
	};
}

/**
 * 初始化表格
 * @param url		:	查询列表url
 * @param pars		:	查询参数
 * @param ths		:	TH数组
 *						[ name:th文本, isSort:是否可排序, width:列宽度（数字）, renderer:格式类型（date、time、money、num、float2、float4）
 *						 align:对齐方式（left、center、right），默认center, hasTil:是否显示title ]
 * @param loadFunc 	:	页面查询JS方法名
 * @param sortCol	:	排序列的序号（被隐藏的列也有序号）
 * @param pageSize	:	每页显示条数
 * @param curP		:	当前页码
 */
MGrid.prototype.init = function(url,pars,ths,loadFunc,sortCol,isDe,pageSize,curP){
	/* 初始化属性 */
	this.colArr = new Array();
	this.gridStr = new Array();
	this.url = url;
	this.pars = pars;
	this.colArr = ths;
	this.loadFunc = loadFunc;
	if(sortCol!=undefined && sortCol!='undefined'){
		this.sortCol = sortCol;
	}
	if(isDe!=undefined && isDe!='undefined'){
		this.isDe = isDe;
	}
	if(pageSize!=undefined && pageSize!='undefined'){
		this.pageSize = pageSize;
	}
	if(curP!=undefined && curP!='undefined'){
		this.curP = curP;
	}
	this.gridStr.push("<table id='"+this.id+"' class='gridTab "+this.config.className+"' style='width:100%' cellpadding='0' cellspacing='0'>");
}

/**
 * ajax加载数据并输出
 * @param dataMapper	:	数据映射器，组装数据后返回
 * @param columnCreator	:	表头列生成器
 * @param ajaxCallBack	:	ajax回调方法
 */
MGrid.prototype.loadData = function(dataMapper,columnCreator,ajaxCallBack){
	createProgressBar();
	var gridObj = this;
	//组装参数
	var pars = gridObj.pars;
	if(gridObj.sortCol!=undefined&&gridObj.sortCol!=""){
		pars.orderCol=gridObj.sortCol;
		pars.isDe=gridObj.isDe;
	}
	if(gridObj.pageSize!=undefined&&gridObj.pageSize!=""){
		pars.pageSize=gridObj.pageSize;
	}
	if(gridObj.curP!=undefined&&gridObj.curP!=""){
		pars.p=gridObj.curP;
	}
	//pars['ran']=Math.random();
	new Ajax.Request(gridObj.url,{
		method : 'post',
		parameters : pars,
		onSuccess : function(response){
			var jsonData = "";
			if(response.responseText!=""){
				jsonData = response.responseText.evalJSON();
			}
			switch(gridObj.config.listType){
				case 'simple':
					gridObj.simpleList(dataMapper,jsonData);
					break;
				case 'simpleStat':
					gridObj.simpleStat(dataMapper,jsonData,columnCreator);
					break;
				case 'staTab':
					gridObj.statTab(dataMapper,jsonData,columnCreator);
					break;
				default:
					gridObj.simpleList(dataMapper,jsonData);
					break;
			}
			if(ajaxCallBack!=undefined){
				ajaxCallBack(jsonData);
			}
			closeProgressBar();
		},
		onfailure : function(response){
			gridObj.render();
			closeProgressBar();
			if (transport.status == 404) { alert("您访问的url地址不存在！"); }
			else { alert("Error: status code is " + transport.status); }
		}
	});
}
MGrid.prototype.simpleList = function(dataMapper,jsonData){
	this.initCol();
	var listObj = jsonData.list;
	if(listObj!=undefined && listObj!=""){
		for(var i=0; i<listObj.length; i++){
			var obj = listObj[i];
			var initArgs = dataMapper(obj);
			if(initArgs!=undefined&&initArgs.length>3){
				this.initData(initArgs[0],initArgs[1],initArgs[2],initArgs[3],i);
			}
		}
		this.render(jsonData.page);
		if(this.config.hasSumRow){
			this.addSumTr();
		}
	}
	else{
		this.render('',true);
	}
}
MGrid.prototype.simpleStat = function(dataMapper,jsonData,columnCreator){
	this.config.emptyTabShow = false;//空数据时不显示表头
	var listObj = jsonData.list;
	if(listObj!=undefined && listObj!=""){
		if(columnCreator!=undefined&&columnCreator!=""){
			columnCreator(jsonData,this.colArr);
		}
		this.initCol();
		for(var i=0; i<listObj.length; i++){
			var obj = listObj[i];
			var initArgs = dataMapper(obj);
			this.initData(initArgs[0],initArgs[1],initArgs[2],initArgs[3],i);
		}
		this.dataXML = jsonData.dataXML;
		this.render(jsonData.page);
		if(this.config.hasSumRow||this.config.sumRowCallBack!=null){
			this.addSumTr();
		}
	}
	else{
		this.dataXML = '';
		this.render('',true);
	}
}
MGrid.prototype.statTab = function(dataMapper,jsonData,columnCreator){
	this.config.emptyTabShow = false;//空数据时不显示表头
	var listObj = jsonData.dataList;
	
	if(listObj!=undefined && listObj!=""){
		if(columnCreator!=undefined&&columnCreator!=""){
			columnCreator(jsonData,this.colArr);
		}
		this.initCol();
		for(var i=0; i<listObj.length; i++){
			var obj = listObj[i];
			var initArgs = dataMapper(obj);
			if(initArgs!=undefined&&initArgs.length>3){
				this.initData(initArgs[0],initArgs[1],initArgs[2],initArgs[3],i);
			}
		}
		this.dataXML = jsonData.dataXML;
		this.render(jsonData.page);
		if(this.config.hasSumRow||this.config.sumRowCallBack!=null){
			this.addSumTr();
		}
	}
	else{
		this.dataXML = '';
		this.render('',true);
	}
}
/**
 * 初始化表头列
 */
MGrid.prototype.initCol = function(){
	/* 生成表头 */
	if(isArray(this.colArr)){
		var cols = this.colArr;
		var ckArr = null;
		/* 读取cookies */
		if(this.config.isResize){ ckArr = this.getCkArr(); }//cookies保存的列序号和列宽
		/* 保存列长度(写入cookies) */
		this.colCount = cols.length;
		var thsHTML = new Array();
		/* 生成TH */
		if(cols[0].name!=undefined){
			thsHTML.push("<tr>");
			if(this.config.hasCheckBox){ thsHTML.push("<th style='text-align:center;'><input name='allCheck' id='allCheck' type='checkbox' onclick='selectAll()'></th>"); }//生成checkBox
			var thWidth,resizeSpan,thStyle,thClass,sortStyle,sortEvent,sortImg;
			var sortCol = 0;
			for(var i=0; i<cols.length; i++){
					
				//if(ckArr == null || ckArr[i]==undefined || ckArr[i][0] != '0'){//跳过被隐藏的列
				if(1==1){
					//清空变量
					thWidth = null ;
					resizeSpan = '';
					thStyle = new Array();
					thClass = '';
					sortStyle = '';
					sortEvent = '';
					sortImg = '';
					var isDe = this.isDe;
					//生成排序样式
					if(this.config.sortable && cols[i] != undefined && (cols[i].isSort==undefined || cols[i].isSort)){
						if(this.sortCol != undefined && this.sortCol != "" && sortCol == this.sortCol){
							thClass="colFocus";
							if(isDe == undefined || isDe == "" || isDe == "0"){
								isDe="1";
								sortImg = "<img src='crm/images/content/sort_asc.gif' alt='升序'/>";
							}
							else{
								isDe="0";
								sortImg = "<img src='crm/images/content/sort_desc.gif' alt='降序'/>";
							}
						}
						sortStyle="cursor:pointer;";
						sortEvent="onclick=reloadJsonList("+this.loadFunc+",'"+sortCol+"','"+isDe+"','"+this.pageSize+"')";
						sortCol++;
					}
					//设置单元格宽度
					if(ckArr != null&& ckArr[i]!=undefined&&ckArr[i][1]!='' && ckArr[i][1]>0 && cols.length==(ckArr.length-1)){
						thWidth=ckArr[i][1]+"px";//读取上次保存宽度
					}
					else{
						thWidth = cols[i].width? cols[i].width : ((this.config.hasCheckBox?($(this.layerId).offsetWidth-25):$(this.layerId).offsetWidth)/cols.length+'px');
					}
					thStyle.push("width:"+thWidth+";");
					//加入拉伸层
					if(this.config.isResize){
						thStyle.push("text-align:right;padding-right:0;");
						resizeSpan = "<span class='resizeDiv' onmousedown=\"colMDown(this,'"+this.id+"')\" onmousemove=\"colMMove(this,'"+this.id+"')\" onmouseup=\"colMUp(this,'"+this.id+"','"+this.colCount+"')\"></span>";
					} 
					if(i==cols.length-1){ thStyle.push("border-right:0;"); }//最后一列右侧无边框
					thsHTML.push("<th class='"+thClass+"' colNum='"+i+"' style='cursor:default;"+thStyle.join("")+"' ><span style='text-align:center;"+sortStyle+"' "+sortEvent+">"+sortImg+cols[i].name+"&nbsp;</span>"+resizeSpan+"</th>");
				}
			}
		}
		else{//统计表格自定义表头
	
			for(var i=0; i<cols.length; i++){
				thsHTML.push(cols[i].html);
			}
		}
		thsHTML.push("</tr>");
		this.gridStr.push(thsHTML.join(""));
	}
	else{  alert("表格初始化失败,列参数错误");  }
}
/**
 * 初始化数据
 * @param datas		:	数据数组
 * @param className	:	tr行样式
 * @param dblFunc 	:	双击执行的方法名
 * @param dataId	:	数据ID（有多选框时传入）
 * @param trIndex	:	tr行号
*/

MGrid.prototype.initData = function(datas,className,dblFunc,dataId,trIndex) {
	var rowHTML = new Array();
	var rowEvent = "";
	var rowStyle = "";
	/* 生成TR样式 */
	if(className == undefined && className != ''){ className = "" ; }
	if(dblFunc!=undefined&&dblFunc!=""){ rowEvent = "ondblclick=\""+dblFunc+"\" "; }//双击事件
	//隔行换色
	if(trIndex%2!=0){ rowStyle="background-color:#f4f6f9"; }
	else{ rowStyle="background-color:#ffffff"; }
	rowHTML.push("<tr id='"+this.id+"tr"+trIndex+"' class='"+className+"' "+rowEvent+"");
	if(className!="sumTr"){//合计行无以下样式
		rowHTML.push("style='"+ rowStyle +"' onmouseover=\"this.oBg=this.style.backgroundColor;this.style.backgroundColor='#fef97d';\" onmouseout=\"if(this.oBg!=undefined)this.style.backgroundColor=this.oBg;\" ");
	}
	rowHTML.push(">");
	/* 生成checkBox */
	if(this.config.hasCheckBox){
		rowHTML.push("<td style='text-align:center'><input type='checkbox' name='priKey' value='"+dataId+"'\" onclick=\"selectTr(this,"+this.config.selCallBackFunc+")\"></td>");
	}
	/* 生成TD数据 */
	var tdData,tdValue,tdTxt,tdAlign,tdBorder,tdClass,tdStyle,tdAppend,showTdTitle;
	for(var i=0; i<datas.length; i++){
		//初始化TD属性
		tdData = '';
		tdValue = null;
		tdTxt = '';
		tdClass = '';
		tdStyle = new Array();
		tdAppend = new Array();
		if(datas[i]==undefined||datas[i]==null){ datas[i] = ''; }
		if(isArray(datas[i])){//统计表格中第二个元素为数据值
			tdData = datas[i][0];
			tdValue = datas[i][1];
		}
		else{
			tdData = datas[i];
		}
		
		var colCfg = this.colArr[i];
		if(colCfg.renderer!=undefined){
			//格式化td
			switch(colCfg.renderer){
				case 'date': tdTxt = tdData.substring(0,10); break;
				case 'time': tdTxt = tdData.substring(0,16); break;
				case 'money': tdTxt = "￥"+changeTwoDecimal(tdData); break;
				case 'moneyOrEmt': tdTxt = tdData?"￥"+changeTwoDecimal(tdData):""; break;
				case 'num': tdTxt = numFormat(tdData); break;
				case 'float2': tdTxt = formatFloat(tdData,2); break;
				case 'float4': tdTxt = formatFloat(tdData,4); break;
				default: tdTxt = tdData;
			}
		}
		else{ tdTxt = tdData; }
		if(tdValue != null){
			tdAppend.push(" tdValue=\""+tdValue+"\" ");
		}
		
		//是否显示title
		if(colCfg.hasTil!=undefined&&colCfg.hasTil!=""){
			tdAppend.push(" showTitle="+colCfg.hasTil+" ");
		}
		if(colCfg.isSelSum!=undefined&&colCfg.isSelSum!=""){
			tdAppend.push(" isSelSum="+colCfg.isSelSum+" ");
		}
		//设置对齐方式
		if(colCfg.align!=undefined&&colCfg.align!=""){
			tdStyle.push("text-align:"+colCfg.align+";");
		}
		if(i==datas.length-1){ tdStyle.push("border-right:0;"); }
		rowHTML.push("<td class='"+tdClass+"' style='"+tdStyle.join('')+"' "+tdAppend.join('')+">"+tdTxt+"&nbsp;</td>");
	}
	rowHTML.push("</tr>");
	this.gridStr.push(rowHTML.join(""));
}
/**
 * 输出表格
 * @param pageObj 		:	page对象
 * @param isEmpty		:	是否空列表
 * @param hasCol		:	是否显示表头
 */
MGrid.prototype.render = function(pageObj,isEmpty){
	this.gridStr.push("</table>");
	var appendHTML = "";
	if(isEmpty){
		appendHTML = "<div class='noDataTd' style='"+ (this.config.emptyTabShow?"border-top:0px":"")+"'>暂无数据</div>";
		$(this.layerId).innerHTML = (this.config.emptyTabShow?this.gridStr.join(""):"")+appendHTML;
	}
	else{
		if(this.config.hasPage && pageObj!=undefined && pageObj != ""){
			appendHTML = this.createPage(pageObj);
		}
		$(this.layerId).innerHTML = this.gridStr.join("")+appendHTML;
	}
	/* 缩略内容 */
	var tabObj = $(this.id) ;
	if(tabObj != null) {
		if(this.config.isShort){ tabObj.className += " tabShort"; }
		var ths=tabObj.getElementsByTagName("th");
		var colWidth;
		var i=0;
		
		//缩略表头
		if(this.config.hasCheckBox){
			i=1;
			ths[0].style.width='25px';
		}
		else{ i=0; }
		for(i;i<ths.length;i++){
			var thsObj = ths[i];
			thsObj.className +=" noBr";
			var colWidth = thsObj.offsetWidth;
			//thsObj.style.width=colWidth;//将td宽度转换为px（非%）,5为thpading和border宽度
			thsObj.title = thsObj.innerText;
			if(this.config.isResize){
				var innerThObj = thsObj.getElementsByTagName("span");
				innerThObj[0].className +=" textOverflow";
				innerThObj[0].style.width=colWidth-10;//拉伸条宽度6+padding2+border2
			}
			/*else{
				var innerThObj = thsObj.getElementsByTagName("span");
				if(innerThObj!=undefined){
					innerThObj[0].className +=" textOverflow";
					innerThObj[0].style.width=colWidth-6;//padding4+border2
				}
			}*/
			
			/*else{
				thsObj.className +=" textOverflow";
				//设定最小宽度
				var minWidth = this.config.minThWidth;
				if(minWidth!=null&&colWidth<minWidth) { 
					thsObj.style.width=minWidth; 
				}
			}*/
		}
		if(this.config.isResize){ writeColCk(this.id,this.colCount,ths); }
		//缩略td数据行
		if(this.config.isShort){
			var tds=tabObj.getElementsByTagName("td");
			for(var j=0;j<tds.length;j++){
				var tdsObj = tds[j];
				tdsObj.className=tdsObj.className+" textOverflow";
				if((tdsObj.showTitle==undefined||tdsObj.showTitle=="true") && tdsObj.innerText!=""){//添加title
					tdsObj.title = tdsObj.innerText;
				}
			}
		}
	}
}

/**
 * cookies操作
 */
/* 读取cookies */
MGrid.prototype.getCkArr = function(){
	var ckCols = readCk("gridStyle"+this.id);
	if(ckCols != ""){
		var colObj = ckCols.split("|");
		var ckArr = new Array(colObj.length);
		for(var i=0; i<colObj.length;i++){
			ckArr[i] = colObj[i].split(",");
		}
		return ckArr;
	}
	else{ return null; }
}
/* 保存cookies */
function writeColCk(id,colCount,ths){
	if(ths==undefined){
		ths = $(id).getElementsByTagName('th');
	}
	var ckValue = '';
	var j=0;
	if(ths.length>colCount){ j=1; }//有多选
	for(var i=0;i<colCount;i++){
		if(ths[j].colNum == i){
			ckValue += '1,'+ths[j].offsetWidth+'|';
			j++;
		}
		else{ ckValue += '0,|'; }
	}
	saveCk("gridStyle"+id,ckValue,24*30);
}

/**
 * 生成底部页码table
 */
MGrid.prototype.createPage = function(pageObj){
	var DOC = document;
	/* page对象映射 */
	var p_count = parseInt(pageObj.rowsCount);//总数据条数
	var p_size = parseInt(pageObj.pageSize);//每页数据条数
	var p_cur = parseInt(pageObj.currentPageNo);//当前页数
	var p_pre = parseInt(pageObj.prePageNo);//前一页
	var p_nxt = parseInt(pageObj.nextPageNo);//后一页
	var p_all = parseInt(pageObj.pageCount);//总页数
	var canJump = true;
	
	var firstHref = this.createLink(1);
	var preHref = this.createLink(p_pre); 
	var nextHref = this.createLink(p_nxt);
	var endHref = this.createLink(p_all);
	var pContainer = DOC.createElement("div");
	var pTable = DOC.createElement("table");
	pTable.className = "normal nopd";
	var pTbody = DOC.createElement("tbody");
	var pTr = DOC.createElement("tr");
	/* 左侧页码数据td */
	var pDataTd = DOC.createElement("td");
	pDataTd.innerHTML = "&nbsp;共<span class='bold'>"+ p_count +"</span>条数据&nbsp;&nbsp;每页<span class='bold'>"+ this.createPSizeSel(p_cur,p_size) +"</span>条";
	/* 右侧页码列表td */
	var pListTd = DOC.createElement("td");
	//首页
	var firstPage = DOC.createElement("span");
	firstPage.className = "pageStyle";
	firstPage.title = "首页";
	firstPage.innerHTML = "<a title='共"+ p_all +"页' style='color:#666; padding:2px 2px 0 2px;'>"+ p_cur +"/"+ p_all +"</a>"+firstHref +"&lt;&lt;</a>";
	//上一页
	var prePage = DOC.createElement("span");
	if(p_cur>1){
		prePage.className = "pageStyle";
		prePage.title = "上一页";
		prePage.innerHTML = preHref +"&lt;</a>";
	}
	else {
		prePage.className = "grayPage";
		prePage.title = "已经是第一页";
		prePage.innerHTML = "&lt;";
	}
	//页码列表
	var pageList = DOC.createElement("span");
	pageList.id = "curPageList";
	pageList.className="ulHor";
	pageList.innerHTML = this.createPageList(p_cur,p_all);
	//下一页
	var nextPage = DOC.createElement("span");
	if(p_cur<p_all){
		nextPage.className = "pageStyle";
		nextPage.title = "下一页";
		nextPage.innerHTML = nextHref +"&gt;</a>";
	}
	else{
		nextPage.className = "grayPage";
		nextPage.title = "已经是最后一页";
		nextPage.innerHTML = "&gt;";
	}
	//尾页
	var endPage = DOC.createElement("span");
	endPage.className = "pageStyle";
	endPage.title = "尾页";
	endPage.innerHTML = endHref +"&gt;&gt;</a>";
	
	pListTd.appendChild(firstPage);
	pListTd.appendChild(prePage);
	pListTd.appendChild(pageList);
	pListTd.appendChild(nextPage);
	pListTd.appendChild(endPage);
	pListTd.style.textAlign = "right";
	
	//页面跳转Td
	var jumpTd = DOC.createElement("td");
	jumpTd.style.textAlign="right";
	if(canJump){
		jumpTd.style.width="75px";
		jumpTd.innerHTML = "&nbsp;"+"到&nbsp;<input type='text' class='pageForward' id='pageForward' />&nbsp;页<a href='javascript:void(0)' onclick=\"reloadJsonList("+this.loadFunc+",'"+this.sortCol+"','"+this.isDe+"','"+this.pageSize+"',$('pageForward').value);return false;\"><img style='border:0px' alt='跳转' class='imgAlign' src='crm/images/content/forwardPage.gif'/></a>";
	}
	else{
		jumpTd.innerHTML = "&nbsp;<span title='共"+ p_all +"页'>"+ p_cur +"/"+ p_all +"</span>&nbsp;&nbsp;";
	}
	
	pTr.appendChild(pDataTd);
	pTr.appendChild(pListTd);
	pTr.appendChild(jumpTd);
	pTbody.appendChild(pTr);
	pTable.appendChild(pTbody);
	pContainer.appendChild(pTable);
	return "<div class='pageList'>"+pContainer.innerHTML+"</div>";
}
/* 生成链接 */
MGrid.prototype.createLink = function(p){
	var linkHTML = "<a href=\"javascript:void(0)\" onclick=\"reloadJsonList("+this.loadFunc+",'"+this.sortCol+"','"+this.isDe+"','"+this.pageSize+"','"+ p +"');return false;\">";
	return linkHTML;
}
/* 生成页码列表 */
MGrid.prototype.createPageList = function(p_cur,p_all){
	var listSize = 4;//一个列表显示的页码个数-1
	var startPage=Math.floor(p_cur-listSize/2);
	var endPage ;
	if(startPage <= 0){ startPage = 1; }
	
	if(p_all <= (listSize + 1)){ endPage=p_all; startPage = 1;}
	else {
		endPage = startPage+listSize;
		if(endPage > p_all){
			endPage = p_all;
			startPage = p_all - listSize;
		}
	}
	var nodeHTML = new Array();
	for(var i=startPage;i<=endPage;i++){
		nodeHTML.push("<span");
		if(i==p_cur){ nodeHTML.push(" class='pageFocus'>&nbsp;"+i+"&nbsp;</span>"); }
		else { nodeHTML.push(" class='pageStyle'>"+this.createLink(i) + "&nbsp;"+i+"&nbsp;</a>"+"</span>"); }
	}
	return nodeHTML.join("");
}
/* 生成每页显示条数下拉列表 */
MGrid.prototype.createPSizeSel = function(curP,pSize){
	var sizes = new Array('10','30','50','100','200');
	var selectHTML = "";
	var optionsHTML = "";
	for(var i = 0 ;i<sizes.length;i++){
		var isSelected = (sizes[i] == pSize.toString())?"selected":"";
		optionsHTML += "<option value='"+sizes[i]+"' "+isSelected+">"+sizes[i]+"</option>";
	}
	selectHTML = "<select class=\"inputBoxAlign\" onchange=\"reloadJsonList("+this.loadFunc+",'"+this.sortCol+"','"+this.isDe+"',this.value,'"+this.curP+"')\">"+optionsHTML+"</select>";
	return selectHTML;
}

/* 增加合计行 */
MGrid.prototype.addSumTr = function(){
	if($(this.id)!=null){
		var colCfg = this.colArr;
		
		var trObjs = $(this.id).rows;
		var sumTrObj = $(this.id).insertRow();
		
		sumTrObj.className="sumTr";
		var sumValues = new Array();
		//-1去除左侧列
		for(var m=0; m<(colCfg.length-1); m++){
			sumValues[m] = 0;
		}
		sumTrObj.insertCell(0).innerHTML = "合计";
		for(var i=1; i<trObjs.length; i++){
			var tdObjs = trObjs[i].cells;
			for(var j=1; j<tdObjs.length; j++){
				if(tdObjs[j].tdValue!=undefined && !isNaN(tdObjs[j].tdValue)){
					sumValues[j-1] += parseFloat(tdObjs[j].tdValue);
				}
				else if(!isNaN(tdObjs[j].innerText)&&parseFloat(tdObjs[j].innerText)){
					sumValues[j-1] += parseFloat(tdObjs[j].innerText);
				}
				else {
					sumValues[j-1] = "&nbsp;";
				}
			}
		}
		//回调方法修改sumvalues
		if(this.config.sumRowCallBack!=undefined){
			this.config.sumRowCallBack(sumValues);
		}
		
		for(var n=0; n<sumValues.length; n++){
			var cellObj = null;
			var sumValue = sumValues[n];
			var tdFmt = colCfg[n+1].renderer;
			var tdAlign = colCfg[n+1].align;
			if(tdFmt!=undefined && tdFmt!="" && !isNaN(sumValue)){
				switch(tdFmt){
					case 'money':
						sumValue = "￥"+changeTwoDecimal(sumValue);
						break;
					case 'num':
						sumValue = numFormat(sumValue);
						break;
					case 'float2':
						sumValue = formatFloat(sumValue,2);
						break;
					case 'float4':
						sumValue = formatFloat(sumValue,4);
						break;
				}
			}
			cellObj = sumTrObj.insertCell(n+1);
			cellObj.innerHTML = sumValue+"&nbsp;";
			cellObj.title = cellObj.innerText;
			if(tdAlign != undefined && tdAlign != ""){
				cellObj.style.textAlign = tdAlign;
			}
		}
	}
}

/** 
 * 刷新列表
 * @param reloadMethod	:	列表JS方法
 */
function reloadJsonList(reloadMethod,sortCol,isDe,pageSize,curP){
	var pageSize;
	if(curP != undefined && curP!='undefined' && curP != ""){
		var isInt = /^[-\+]?\d+$/;
		if(!isInt.test(curP)){
			alert("请输入正确的页码");
			return;
		}
	}
	reloadMethod(sortCol,isDe,pageSize,curP);
}

/**
 * 表格拉伸
 */
 /* 鼠标按下 */
function colMDown(obj,id){
    obj.mouseDownX=event.clientX;                 //当前鼠标X坐标
    obj.pareneTdW=obj.parentElement.offsetWidth;  //父元素的宽度
    obj.pareneTableW=$(id).offsetWidth;          //表格的宽度
    obj.setCapture();                             //捕获鼠标方法
}
/* 鼠标移动 */
function colMMove(obj,id){
	if(!obj.mouseDownX) return false;             //判断是否是否已经按下
    var newWidth=obj.pareneTdW*1+event.clientX*1-obj.mouseDownX;
    if(newWidth>10){
        obj.parentElement.style.width = newWidth;
		var innerObj = obj.parentElement.getElementsByTagName("span");
		innerObj[0].style.width=newWidth-10;
       	$(id).style.width=obj.pareneTableW*1+event.clientX*1-obj.mouseDownX;//重新设计宽度
   	}
}
/* 鼠标抬起 */
function colMUp(obj,id,colCount){
    obj.releaseCapture();                      //释放鼠标的捕获
    obj.mouseDownX=0;                  			//鼠标抬起
	writeColCk(id,colCount);
}


/*
 * 选中行事件
 */
function selectTr(eventObj,selCallBack){
	var trObj = eventObj.parentElement.parentElement;
	if(trObj.clicked != undefined && trObj.clicked=='true'){
		trObj.clicked = 'false';
		trObj.style.backgroundColor = trObj.oBg;
		trObj.onmouseout = trObj.oldMOut;
		trObj.onmouseover = trObj.oldMOver;
	}
	else {
		trObj.clicked = 'true';
		if(trObj.oldMOut == undefined){
			trObj.oldMOut = trObj.onmouseout;
			trObj.oldMOver = trObj.onmouseover;
		}
		if(trObj.oBg == undefined){
			trObj.oBg = trObj.style.backgroundColor;
		}
		trObj.style.backgroundColor = '#cee6f8';
		trObj.onmouseout = function(){};
		trObj.onmouseover = function(){};
	}

	if(selCallBack!=undefined && selCallBack!=null && ($("allCheck").allClicked==undefined||$("allCheck").allClicked)){
		var trObjs = $(trObj.id).parentElement.getElementsByTagName("tr");
		var selTrNum = 0;
		var tdSums = [0];
		var tdObjs,k;
		for(var i=0; i<trObjs.length; i++){
			if(trObjs[i].clicked=='true'){
				selTrNum++;
				tdObjs = trObjs[i].getElementsByTagName("td");
				k = 0;
				for(var j=0; j<tdObjs.length; j++){
					if(tdObjs[j].isSelSum){
						if(tdSums[k]==undefined){
							tdSums[k] = 0;
						}
						tdSums[k] += parseFloat(tdObjs[j].tdValue);
						k++;
						break;//如有多个合计列，需注释此行
					}
				}
			}
		}
		selCallBack(selTrNum,tdSums);
	}
}

/* -------------------------------- Ajax ----------------------------------- */
/* ajax加载用户列表（在调用此方法时要实现getUserListHTML(userList)方法生成列表内容） */
function getUserDiv(url,tagName){
	if($("showUserLayer")==null){
		//初始化
		var userLayerDiv = DOC.createElement("div");
		var userListDiv = DOC.createElement("div");
		var userCloseDiv = DOC.createElement("div");
		userLayerDiv.id="showUserLayer";
		userListDiv.id="taUserList";
		userCloseDiv.id="taUserClose";
		userLayerDiv.style.display="none";
		userLayerDiv.appendChild(userListDiv);
		userLayerDiv.appendChild(userCloseDiv);
		DOC.body.appendChild(userLayerDiv);
		getUserDiv(url,tagName);
	}
	else{
		if($("showUserLayer").style.display!="none"){
			$("showUserLayer").hide();
		}
		cursorDiv($("showUserLayer"),10,0);
		$("showUserLayer").style.display = "";
		$("taUserList").innerHTML="<img src='crm/images/gif/uploading.gif'>加载中...";
		$("taUserClose").innerHTML="[<a href='javascript:void(0)' onclick='closeUserLayer();return false;'>关闭</a>]";
		url += "&ran=" + Math.random();
		new Ajax.Request(url,{
			method:"get",
			onSuccess: function(transport){
				var docXml = transport.responseXML;
				if(docXml!=null||docXml!=""){
					var userList = docXml.getElementsByTagName(tagName);
					var listHtml= getUserListHTML(userList);
					if($("taUserList")!=null){
						$("taUserList").innerHTML=listHtml;
					}
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
}
function closeUserLayer(){
	$("showUserLayer").hide();
}


//生成下载链接
function createFileToDL(count,attId,attPath,attName,attSize,attDate){
	var url = "fileAction.do";
	var pars = "op=hasFile&fileId="+attId+"&path="+attPath+"&count="+count+"&ran=" + Math.random();
	new Ajax.Request(url, { 
		method: 'get',
		parameters: pars,
		onSuccess: function(transport) {
			var response = transport.responseText;
			var fileHTML="";
			if(response=="1"){
				fileHTML = "<img src='crm/images/content/attIconS.gif' alt='文件下载'/>&nbsp;<a href='downloadAction.do?op=getStreamInfo&fileId="+attId+"&path="+encodeURIComponent(attPath)+"'>"+attName+"</a>&nbsp;<span>("+formatSize(attSize)+")</span>&nbsp;<span class='gray' id='update"+count+"'></span>&nbsp;";
			}
			else if(response=="0"){//文件不存在
				fileHTML = "<img src='crm/images/content/attIconS.gif' alt='文件下载'/>&nbsp;<span class='attError' title='无法下载此文件，该文件可能已从服务器删除'>"+attName+"&nbsp;<span>("+formatSize(attSize)+")</span>&nbsp;</span><span id='upDate"+count+"' class='gray'></span>&nbsp;"
			}
			$("fileDLLi"+count).innerHTML=fileHTML;
			dateFormat("upDate",attDate,count);
		},
		
		onFailure: function(transport){
			if (transport.status == 404)
				alert("您访问的url地址不存在！");
			else
				alert("Error: status code is " + transport.status);
		}
	}); 
}
				
//删除附件
function delAttFile(extType,type,id,attId,hasIcon,attPath){
	if(confirm("是否删除该附件?")){
		self.location.href="fileAction.do?op=deleteFile&extType="+extType+"&type="+type+"&id="+id+"&attId="+attId+"&hasIcon="+hasIcon+"&path="+encodeURIComponent(attPath);
	}
}
		
//------------生成国家省市-------------
function getCityInfo(info,prvId,cityId) {
	var couId = $("country").value;
	var proId = $("pro").value;
	var url;
	var pars;
	var obj;
	var isSetValue = true;
	if(arguments.length<3)
		isSetValue = false;
	if(info=="cou"){    
		obj=$("pro");
		obj.options.length=0; 
		obj.add(new Option("请选择",1));
		if($("city")!=null){
			obj=$("city");
			obj.options.length=0; 
			obj.add(new Option("请选择",1));
		}
		url = "customAction.do";
		pars = "op=getProvince&couId="+couId+"&ran=" + Math.random();
		new Ajax.Request(url, {
			method:'get',
			parameters: pars,
			onSuccess:function(transport){
				var response = transport.responseText.split(",");
				var o= $("pro");
				for(var i=0;i<response.length-1;i+=2){     
					o.options.add(new Option(response[i+1],response[i])); //添加到options集合中
				}
				if(isSetValue){
					o.value=prvId;
					getCityInfo("province",prvId,cityId);
				}
				
			},
			onFailure:function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		});
	}
	else if(info=="province"){
		obj=$("city");
		obj.options.length=0; 
		obj.add(new Option("请选择",1));
		url ="customAction.do";
		pars = "op=getCity&proId="+proId+"&ran=" + Math.random();
		new Ajax.Request(url, {
			method:'get',
			parameters: pars,
			onSuccess:function(transport){
				var response = transport.responseText.split(",");	
				var o= $("city");
				for(var i=0;i<response.length-1;i+=2){   
					o.options.add(new Option(response[i+1],response[i])); //添加到options集合中
				}
				if(isSetValue){
					o.value=cityId;
					isSetValue=false;
				}
			},
			onFailure:function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		});
	}
}


//------------生成自己管辖的国家省市-------------
function getManageCityInfo(info,prvId,cityId) {
	var couId = $("country").value;
	var proId = $("pro").value;
	var url;
	var pars;
	var obj;
	var isSetValue = true;
	if(arguments.length<3)
		isSetValue = false;
	if(info=="cou"){    
		obj=$("pro");
		obj.options.length=0; 
		obj.add(new Option("请选择",1));
		if($("city")!=null){
			obj=$("city");
			obj.options.length=0; 
			obj.add(new Option("请选择",1));
		}
		url = "customAction.do";
		pars = "op=getmyManageProvince&couId="+couId+"&ran=" + Math.random();
		new Ajax.Request(url, {
			method:'get',
			parameters: pars,
			onSuccess:function(transport){
				var response = transport.responseText.split(",");
				var o= $("pro");
				for(var i=0;i<response.length-1;i+=2){     
					o.options.add(new Option(response[i+1],response[i])); //添加到options集合中
				}
				if(isSetValue){
					o.value=prvId;
					getManageCityInfo("province",prvId,cityId);
				}
				
			},
			onFailure:function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		});
	}
	else if(info=="province"){
		obj=$("city");
		obj.options.length=0; 
		obj.add(new Option("请选择",1));
		url ="customAction.do";
		pars = "op=getmyManageCity&proId="+proId+"&ran=" + Math.random();
		new Ajax.Request(url, {
			method:'get',
			parameters: pars,
			onSuccess:function(transport){
				var response = transport.responseText.split(",");	
				var o= $("city");
				for(var i=0;i<response.length-1;i+=2){   
					o.options.add(new Option(response[i+1],response[i])); //添加到options集合中
				}
				if(isSetValue){
					o.value=cityId;
					isSetValue=false;
				}
			},
			onFailure:function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		});
	}
}
//------------ 选择客户下的项目 -----------
function findCusPro(){
	 var cusCode=$("cusCode").value;
	 var url ="orderAction.do";
	 var pars = "op=loadCusProject&cusCode="+cusCode+"&ran=" + Math.random();
	 new Ajax.Request(url,{
		method:'get',
		parameters: pars,
		onSuccess:function(transport){
			var docXml = transport.responseXML;
			if(docXml!=null||docXml!=""){    
				var sel=$("proId");//获得select对象
				sel.options.length=0; 
				sel.add(new Option("",""));
				var n=docXml.getElementsByTagName("option").length;
				for(var i=0;i<n;i++){
					var selOption=docXml.getElementsByTagName("option")[i];
					if(selOption!=null){
						var opValue=selOption.getAttribute("value");
						var opTxt=selOption.childNodes[0].nodeValue;      
						sel.options.add(new Option(opTxt,opValue)); //添加到options集合中
					}
				}
			}
		},
		onFailure:function(transport){
			if (transport.status == 404)
				alert("您访问的url地址不存在！");
			else
				alert("Error: status code is " + transport.status);
		}
	 });
} 

/* -------------------------------- 弹出层 ----------------------------------- */

/* 点击弹出层（快速新建） */
function rightWindow(id){
	if($(id).style.display!="none"){
		$(id).hide();
	}
	var x,y; 
	x = event.clientX + DOC.body.scrollLeft; 
	y = event.clientY + DOC.body.scrollTop; 
	
	$(id).style.left = x+10; 
	$(id).style.top = y; 
	$(id).style.display = "";
	$(id).innerHTML="<img src='crm/images/gif/uploading.gif'>加载中...";
}

/*
	附件弹出层(att_)
	arg1=id:附件所属对象id, arg2=folder: 文件保存位置, arg3=extType:类型
	hidden: extType:类型, type:附件所属对象类别
*/
function commonPopDiv(n,arg1,arg2,arg3){
	var width=465;
	var height=475;
	var idPre="att_";
	var titleTxt="";
	var hasScroll = true;
	switch(n){
		case 1:
			titleTxt="附件管理";
			url="fileAction.do?op=attList&extType="+arg3+"&type="+arg2+"&id="+arg1;
			break;
		case 2:
			titleTxt="添加附件";
			url="fileAction.do?op=attList&extType="+arg3+"&type="+arg2+"&id="+arg1+"&hasIcon=0";
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,false,hasScroll);
}

/*
	选择小窗口(brow_)
*/
function addDivBrow(n,arg){
	var width=480;
	var height=350;
	var idPre="brow_";
	var titleTxt="";
	var hasScroll = true;
	switch(n){
		case 1://录入、编辑表单内弹出选择客户
			titleTxt="选择客户";
			height=450;
			url="customAction.do?op=toListCustomersToChoose";//arg标识查询所有客户
			break;
			
		case 2://列表内弹出的选择客户
			titleTxt="选择客户<input type='hidden' id='info' value='search' />";
			height=450;
			url="customAction.do?op=toListCustomersToChoose";
			break;
		case 3://客户对应的联系人
			titleTxt="选择联系人";
			height=450;
			url="customAction.do?op=toListContactsToChoose&cusId="+arg;//arg标识客户id
			break;
		
		case 6:
			titleTxt="选择项目<input type='hidden' id='info' value='create' />";//新建时选择项目弹出的层
			url="projectAction.do?op=getAllProject&forwardType=selPro";
			break;
			
		case 17:
			titleTxt="选择项目<input type='hidden' id='info' value='search' />";//查询时选择项目弹出的层
			url="projectAction.do?op=getAllProject&forwardType=selPro";
			break;
				
		case 7:
		    titleTxt="选择供应商<input type='hidden' id='info' value='create' />";//新建时选择供应商弹出的层
			url="salSupplyAction.do?op=getSelectSup";
			break;	
			
		case 8:
		    titleTxt="选择供应商<input type='hidden' id='info' value='search' />";//查询时选择供应商弹出的层
			url="salSupplyAction.do?op=getSelectSup";
			break;	
		
		case 9:
			titleTxt="选择订单";
			url="orderCusAction.do?op=toListOrdersToChoose";
			if(arg != undefined){
				url += "&cusId="+arg[0];
				url += "&cusName="+arg[1];
			}
			width=480;
			height=350;
			break;
		
		case 10:
			titleTxt="选择采购单";
			url="salPurAction.do?op=showSpo&isok=1&ischoose=1&isAll=2";
			width=480;
			height=350;
			break;
		
		case 12:
			titleTxt="选择员工";
			url="empAction.do?op=salEmpTree";
			if(arg != undefined){
				url += "&type=" + arg;
			}
			width=300;
			height=400;
			break;
			
	    case 13:
			titleTxt="选择负责账号";
			url="userAction.do?op=limUserTree";
			if(arg != undefined){
				url += "&type=" + arg;
			}
			width=300;
			height=400;
			break;
		
		case 19:
			titleTxt="选择负责账号<input type='hidden' id='info' value='search' />";//查询时选择负责账号弹出的层
			url="userAction.do?op=limUserTree";
			if(arg != undefined){
				url += "&type=" + arg;
			}
			width=300;
			height=400;
			break;
			
	    case 14://新建、编辑账号(编辑账号需传入当前账号userNum在树中排除)
			width=300;
			height=350;
			titleTxt="选择上级账号";
			url="userAction.do?op=limUserTree";
			if(arg != undefined && arg != ""){
				url += "&removeUser=" + arg;
			}
			break;
			
	    case 15:
			titleTxt="快速添加员工";
			height=165;
			width=320;
			url="empAction.do?op=toNewSalEmp&toQuickAdd=1";
			hasScroll = false;
		    break;
		case 16:
			height = 450;
			width = 400;
			titleTxt = "选择产品类别";
			url = "prodAction.do?op=selProType&type="+arg;
			hasScroll = true;
			break;    
		case 20://录入、编辑表单内弹出选择产品
			titleTxt="选择产品";
			height=450;
			url="prodAction.do?op=toListProductToChoose&type="+arg;
			break;
		case 21:
		    titleTxt="选择供应商";
		    height=450;
		    url="supAction.do?op=toListSupplierToChoose";//arg标识查询所有供应商
		    break;		
		case 22:
			titleTxt="选择库存";
			height=450;
			url="supAction.do?op=toListProdStoreToChoose";
			break;
		case 23:
			titleTxt="选择采购单";
			height=450;
			url="supAction.do?op=toListPurOrdToChoose";
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,false,hasScroll);
}
/*
	批量删除弹出层
*/
function batchAddDiv(n,type){
	var width=250;
	var height=136;
	var idPre="";
	var titleTxt="";
	switch(n){
		case 1:
			titleTxt="批量删除客户";
			url="customAction.do?op=batchDelConfirm&type="+type+"&delType=1";
			break;
			
		case 2:
			titleTxt="批量删除联系人";
			url="customAction.do?op=batchDelConfirm&delType=2";
			break;
			
		case 3:
			titleTxt="批量删除销售机会";
			url="customAction.do?op=batchDelConfirm&delType=3";
			break;
			
		case 4:
			titleTxt="批量删除来往记录";
			url="customAction.do?op=batchDelConfirm&delType=4";
			break;
		
		case 5:
			titleTxt="批量删除客户服务";
			url="customAction.do?op=batchDelConfirm&delType=5";
			break;
			
		case 6:
			titleTxt="批量删除消息";
			url="customAction.do?op=batchDelConfirm&type="+type+"&delType=6";
			break;
	    case 7:
			titleTxt="批量删除供应商联系人";
			url="customAction.do?op=batchDelConfirm&delType=7";
			break;
		case 8:
			titleTxt="批量删除联系电话";
			url="phoneListAction.do?op=batchDelConfirm&delType=8";
			break;
		//彻底删除
		case 9:
			titleTxt = "批量删除客户";
			url="customAction.do?op=recBatchDelConfirm&type=cus&delType=1";	
			break;
		case 10:
			titleTxt = "批量删除联系人";
			url = "customAction.do?op=recBatchDelConfirm&type=cont&delType=2";	
			break;
		case 11:
			titleTxt = "批量删除销售机会";
			url = "customAction.do?op=recBatchDelConfirm&type=opp&delType=3";
			break;
		case 12:
			titleTxt = "批量删除来往记录";
			url = "customAction.do?op=recBatchDelConfirm&type=pra&delType=4";
			break;
		case 13:
			titleTxt = "批量删除订单";
			url = "orderAction.do?op=recBatchDelConfirm&type=ord&delType=5";
			break;
		case 14:
			titleTxt = "批量删除回款计划";
			url = "paidAction.do?op=recBatchDelConfirm&type=plan&delType=6";
			break;
		case 15:
			titleTxt = "批量删除回款记录";
			url = "paidAction.do?op=recBatchDelConfirm&type=past&delType=7";
			break;
		case 16:
			titleTxt = "批量删除产品";
			url ="prodAction.do?op=recBatchDelConfirm&type=prod&delType=8";
			break;
		case 17:
			titleTxt = "批量删除客户服务";
			url ="customAction.do?op=recBatchDelConfirm&type=serv&delType=9";
			break;
		case 18:
			titleTxt = "批量删除已发报告";
			url="customAction.do?op=recBatchDelConfirm&type=report&delType=10";
			break;
		case 19:
			titleTxt = "批量删除工作安排";
			url = "customAction.do?op=recBatchDelConfirm&type=task&delType=11";
			break;
		case 20:
			titleTxt = "批量删除员工档案";
			url = "customAction.do?op=recBatchDelConfirm&type=emp&delType=12";
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,false);
}

/*
	删除确认层生成方法
	url:自定义url，未定义则使用默认url
	id:实体id
	delType:删除类型（序号）
	isIfrm:删除成功后是否需要刷新iframe
*/
function createConfirmWindow(idPre,titleTxt,url,width,height,id,delType,isIfrm){
	if(width == undefined){
		width=250;
	}
	if(height == undefined) {
		height=136;
	} 
	if(url == undefined || url == ""){
		url = "commonAction.do?op=toDelConfirm&id="+id+"&delType="+delType;
		if(isIfrm != undefined){
			url += "&isIfrm=" + isIfrm;
		}
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,false);
}


/*
	撤销确认层生成方法
	url:自定义url，未定义则使用默认url
	id:实体id
	delType:撤销类型（序号）
	isIfrm:撤销成功后是否需要刷新iframe
*/
function createCancleConfirmWindow(idPre,titleTxt,url,width,height,id,canType,isIfrm){
	if(width == undefined){
		width=250;
	}
	if(height == undefined) {
		height=136;
	} 
	if(url == undefined || url == ""){
		url = "commonAction.do?op=toCancleConfirm&id="+id+"&canType="+canType;
		if(isIfrm != undefined){
			url += "&isIfrm=" + isIfrm;
		}
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,false);
}

/*
	弹出层生成方法
	w_id_pre    : 层前缀（加了层前缀的为二级层）
	w_title     : 标题栏文字
	w_url       : iframe url
	w_width     : 宽度(默认为570)
	w_height    : 高度
	w_hasCover  : 有无遮罩层
	w_hasScroll : 是否有滚动条
	w_hasTitle  : 是否有标题栏
	w_hasButton : 层是否限定最大高度
*/
function createPopWindow(w_id_pre , w_title , w_url, w_width , w_height , w_hasCover , w_hasScroll , w_hasTitle, w_hasButton){
	//初始化
	if(w_width == undefined || w_width == null){
		w_width = 570;
	}
	else if(w_width == 'half'){
		w_width = 570/2;
	}
	if(w_height == undefined || w_height == null){
		w_height = DOC.body.clientHeight-20;//屏幕最大高度
	}
	if(w_hasCover == undefined || w_hasCover == null){
		w_hasCover = true;
	}
	if(w_hasScroll == undefined || w_hasScroll == null){
		w_hasScroll = false;
	}
	else if(w_hasScroll){
		w_width += 18;
	}
	if(w_hasTitle == undefined || w_hasTitle == null){
		w_hasTitle = true;
	}
	//传入前缀参数
	if(w_id_pre!=""){
		w_url += "&wIdPre=" + w_id_pre;
	}
	
	if($(w_id_pre+"popDiv")==null){
		createProgressBar();//生成进度条
		/* --------- 生成遮罩层 --------- */
		if(w_hasCover){
			var coverDiv = DOC.createElement("div");
			coverDiv.id = "coverDiv";
			coverDiv.className="coverDiv";
			
			if(DOC.body.scrollHeight<DOC.body.clientHeight){
				coverDiv.style.height = DOC.body.clientHeight;
			}
			else {
				coverDiv.style.height = DOC.body.scrollHeight;
			}
		}
		/* --------- 生成弹出层 ---------- */
		var popDiv = DOC.createElement("div");//弹出层
		var popContent = DOC.createElement("div");//弹出层内容（不包括标题栏）
		var popIframe = DOC.createElement("iframe");//弹出层内容iframe
		
		popDiv.id = w_id_pre+"popDiv";
		popContent.id = w_id_pre+"popCon";
		popIframe.id = w_id_pre+"popInside";
		
		//弹出层样式,宽高
		popDiv.className="popDiv";
		//if(w_hasHMax && w_height > maxHeight){
		if(w_hasScroll){
			/*if(w_height > maxHeight){//如果要显示滚动条并且高度大于屏幕高度将自动缩短
				w_height = maxHeight;
			}*/
			popIframe.scrolling = "yes";
		}
		else{
			popIframe.scrolling = "no";
		}
		
		popDiv.style.width = w_width+"px";
		popDiv.style.height = w_height+"px";
		//判断层是否偏移出界
		if(DOC.body.clientWidth>w_width){
			popDiv.style.marginLeft = "-"+w_width/2+"px";
		}
		else{
			popDiv.style.left="0px";
		}
		var topOffSet;
		if(DOC.body.clientHeight > w_height){//层高度未超过屏幕高度，则层位置居中
			topOffSet = (DOC.body.clientHeight-w_height)/2;
			if(topOffSet > w_height){//如果离顶部距离大于层高度，则将距离缩短1/3
				topOffSet = topOffSet/3*2;
			}
		}
		else{
			topOffSet = 10;
		}
		topOffSet += DOC.body.scrollTop;
		if(topOffSet<0){
			topOffSet = "0px";
		}
		popDiv.style.top = topOffSet + "px";
		//popDiv.style.top = DOC.body.scrollTop+(DOC.body.clientHeight-w_height-125)/2+"px";//删除层向上偏移
		if(w_hasTitle){
			//标题栏
			popDiv.innerHTML = "<div id='"+ w_id_pre+"popTop' class='popTop'><table class='normal'><tr><td class='popTopMid'>"+w_title+"</td><td class='popTopRight'><img onclick=\"closeDiv('"+ w_id_pre +"')\" src='crm/images/content/popWinC1.gif' onmouseover=\"this.src='crm/images/content/popWinC2.gif'\" onmouseout=\"this.src='crm/images/content/popWinC1.gif'\" alt='关闭'/></td></tr></table></div>";
			popIframe.style.height = (w_height-29-2)+"px";
		}
		else{
			popIframe.style.height = (w_height-10)+"px";
		}
		//内容层（不包括标题栏）样式
		popContent.className = "popContent";
		popContent.style.height = (w_height-29)+"px"; 
		
		//内容层的iframe
		popIframe.setAttribute("frameborder","0",0);
		popIframe.src = w_url;
		
		/* --------- append --------- */
		
		
		if(w_hasCover){
			DOC.body.appendChild(coverDiv);
			hideSelect();//使下拉列表失效
			coverDiv=null;
		}
		popContent.appendChild(popIframe);
		popDiv.appendChild(popContent);
		DOC.body.appendChild(popDiv);
		
		drag(popDiv.id);//拖动层
		popIframe.onreadystatechange = function(){
			if (this.readyState=="complete"){
				closeProgressBar();//移除进度条
			}
		}
		popDiv=null;
		popContent=null;
		popIframe=null;
	}
}

/* 
	弹出窗口拖动 
	o:div对象名或对象
	s[可选]:是否随窗口滚动,默认否
*/

function docMM(a,o,x,y){
	if(!a)a=window.event;  
	o.style.marginLeft="0px";
	if(a.clientX+DOC.body.scrollLeft-x<0){
		o.style.left = 0;  
	}
	/*else if((a.clientX+DOC.body.scrollLeft-x)+o.clientWidth>DOC.body.clientWidth){
		o.style.right = 0;
	}*/
	else{
		o.style.left = a.clientX+DOC.body.scrollLeft-x;
	}
	if(a.clientY+DOC.body.scrollTop-y<0){
		o.style.top = 0;  
	}
	/*else if((a.clientY+DOC.body.scrollTop-y)+o.clientHeight>DOC.body.clientHeight){
		o.style.bottom = 0;
	}*/
	else{
		o.style.top = a.clientY+DOC.body.scrollTop-y;  
	}
	o.orig_x = parseInt(o.style.left) - DOC.body.scrollLeft;  
	o.orig_y = parseInt(o.style.top) - DOC.body.scrollTop;  
	a=null;
	o=null;
}
function docMU(o){
	if(o.releaseCapture)  
		o.releaseCapture();  
	else if(window.captureEvents)  
		window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);  
	DOC.onmousemove = null;  
	DOC.onmouseup = null;  
	DOC.ondragstart = null;  
	DOC.onselectstart = null;  
	DOC.onselect = null;  
	o.style.zIndex = o.orig_index;  
	o=null;
}

function dragObjMD(a,o)  
{  
	//this.style.zIndex = 10000;  
	if(!a)a=window.event;  
	var x = a.clientX+DOC.body.scrollLeft-o.offsetLeft;//鼠标距对象左边距距离  
	var y = a.clientY+DOC.body.scrollTop-o.offsetTop; //鼠标距对象上边距距离   
	DOC.ondragstart = "return false;"  
	DOC.onselectstart = "return false;"  
	DOC.onselect = "DOC.selection.empty();"  
	if(o.setCapture)  
		o.setCapture();  
	else if(window.captureEvents)  
		window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);  

	DOC.onmousemove = function(a)  
	{  
		docMM(a,o,x,y);
	}  

	DOC.onmouseup = function()  
	{  
		docMU(o);
	}  
} 

function drag(id,s)  
{  
    var o = DOC.getElementById(id); 
    o.orig_x = parseInt(o.style.left) - DOC.body.scrollLeft;  
    o.orig_y = parseInt(o.style.top) - DOC.body.scrollTop;  
    o.orig_index = o.style.zIndex;  
    o.onmousedown = function(a){
		dragObjMD(a,this);
	}
      
    /*if (s)  
    {  
        var orig_scroll = window.onscroll?window.onscroll:function (){};  
        window.onscroll = function ()  
        {  
            orig_scroll();  
            o.style.left = o.orig_x + DOC.body.scrollLeft;  
            o.style.top = o.orig_y + DOC.body.scrollTop;  
        }  
    }  */
	
	o=null;
}   

/*
	弹出层隐藏下拉列表a
*/
function hideSelect(){
	var objArray=DOC.getElementsByTagName("select");
	for(var i=0;i<objArray.length;i++){
		/*objArray[i].disabled=true;*/
		objArray[i].style.visibility="hidden";
	}
}

function showSelect(){
	var objArray=DOC.getElementsByTagName("select");
	for(var i=0;i<objArray.length;i++){
		objArray[i].style.visibility="visible";
		/*objArray[i].disabled=false;*/
	}
}

/* 
	关闭新建层 
*/
function closeDiv(idPre){
	this.focus();
	var popId = 'popDiv';
	
	//关闭当前层
	if(idPre == undefined || idPre == ''){
	
		// 关闭二级弹出层
		removeCd("brow_" + popId);
		removeCd("att_" + popId);
		
		removeCd(popId);
	}
	else {
		removeCd(idPre + popId);
	}
	
	if(getPopWindowNum()==0){//关闭了所有弹出层后才可关闭遮罩层
		removeCd("coverDiv");
		//恢复下拉列表
		if($("coverDiv")==null){
			showSelect();
		}
	}
}
//得到页面上弹出层的数量
function getPopWindowNum(){
	return $$(".popDiv").length;
}

function removeCd(id){
	if($(id)!=null)
		DOC.body.removeChild($(id));
}

function cancel(idPre){
	parent.closeDiv(idPre);
}

/*
n:图表的类型
url：获得图表所需XML的url
showDivId：图表显示层id
*/
function renderChar(n,url,showDivId)
{
	 url += "&ran=" + Math.random();
	 new Ajax.Request(url,{ 
			method: 'get',
			onSuccess: function(transport) {
				var response = transport.responseText;
				renderCharByXML(n,response,showDivId);
			},
			onFailure: function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		}); 
}		
function renderCharByXML(n,xml,showDivId){
	var charStyle;
	switch(n){
	    //单系列图
	    case 1:
			charStyle="./salStat/freeChart/charts/Column2D.swf";
			break;
		case 2:
			charStyle="./salStat/freeChart/charts/Column3D.swf";
			break;
		case 3:
			charStyle="./salStat/freeChart/charts/Bar2D.swf";
			break;
		case 4:
			charStyle="./salStat/freeChart/charts/Line.swf";
			break;
		case 5:
			charStyle="./salStat/freeChart/charts/Area2D.swf";
			break;
		case 6:
			charStyle="./salStat/freeChart/charts/Pie2D.swf";
			break;
		case 7:
			charStyle="./salStat/freeChart/charts/Pie3D.swf";
			break;
		case 8:
			charStyle="./salStat/freeChart/charts/Doughnut2D.swf";
			break;
		//多系列图		
		case 9:
			charStyle="./salStat/freeChart/charts/MSColumn2D.swf";
			break;
		case 10:
			charStyle="./salStat/freeChart/charts/MSColumn3D.swf";
			break;
		case 11:
		    charStyle="./salStat/freeChart/charts/MSLine.swf";
			break;
		case 12:
			charStyle="./salStat/freeChart/charts/MSArea.swf";
			break;
		case 13:
			charStyle="./salStat/freeChart/charts/MSBar2D.swf";
			break;
		case 14:
			charStyle="./salStat/freeChart/charts/MSBar3D.swf";
			break;
	    case 15:
			charStyle="./salStat/freeChart/charts/Funnel.swf";
			break;
		//双Y轴
		case 20:
			charStyle="./salStat/freeChart/charts/MSColumn3DLineDY.swf";
			break;
		case 21:
			charStyle="./salStat/freeChart/charts/MSCombiDY2D.swf";
			break;
	}
	var chart1 = new FusionCharts(charStyle, "chart1Id", $(showDivId).clientWidth, $(showDivId).clientHeight);
	chart1.setDataXML(xml);
	chart1.render(showDivId);
}		

/*
	特殊跳转
*/
function mailTo(address){
	self.location.href= "mailto:" + encodeURIComponent(address);      
}

function qqTo(qq){
	parent.location.href= "tencent://message/?uin="+ encodeURIComponent(qq) +"&Site=点击开始交谈&Menu=yes";
}

function msnTo(msn){
	self.location.href= "msnim:chat?contact=" + msn;
}