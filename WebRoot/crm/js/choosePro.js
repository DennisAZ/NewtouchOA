/*
	打开选择产品窗口
*/
function forwardToChoose(n,code){
	switch(n){
		//选择产品(所有产品)
		case 1:
			window.open("prodAction.do?op=searchProType&wm=wm", "选择产品",
				"height=500, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
			break;
		//选择人员
		case 2:
			window.open("empAction.do?op=selEmp&ch=ch", "选择人员",
			 "height=500, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
			break;
		//选择账号
		case 3:
			window.open("empAction.do?op=searchUser", "选择账号",
	 "height=500, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
			break;
		//选择某个仓库的产品
		case 4:
			window.open("wwoAction.do?op=stroSearch&wmsCode="+code, "选择产品",
	 "height=500, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
			break;
		//选择订单
		case 5:
			window.open("orderAction.do?op=searchOrderType", "选择订单",
	 "height=500, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
			break;
	}
	
}	

/* 
	选择产品完成传值 
	cusCode:产品编号
	cusName:产品名称名称
*/
function choosePro(n,code,name){
	switch(n){
		//选择产品
		case 1:
			try{
				window.opener.document.getElementById("wprId").value=code;
				window.opener.document.getElementById("wprName").value=name;
			}catch(error){}
			window.close();
			break;
		//选择人员	
		case 2:
			try{
			 	window.opener.document.getElementById("seNo").value=code;
   			 	window.opener.document.getElementById("seName").value=name;
			}catch(error){}
    		window.close();
			break;
		//选择账号
		case 3:
			try{
			 	window.opener.document.getElementById("uc").value=code;
   			 	window.opener.document.getElementById("ul").value=name;
			}catch(error){}
			window.close();
			break;
		//选择订单
		case 4:
			try{
			 	window.opener.document.getElementById("sodCode").value=code;
    			window.opener.document.getElementById("sodTil").value=name;
			}catch(error){}
    		window.close();
			break;
	}
}

/* 
	选择完成传值 
	wprCode:产品编号
	wprName:产品名称名称
	rspProNum:库存数量
*/
function chooseStroPro(wprCode,wprName,wprModel,rspProNum){
	if(wprModel!=''){
		wprName += '/' + wprModel;
	}
	try{
		window.opener.document.getElementById("wprCode").value=wprCode;
		window.opener.document.getElementById("wprName").value=wprName;
		window.opener.document.getElementById("rspProNum").innerText=rspProNum;
		if(window.opener.document.getElementById("num")!=null){
			window.opener.document.getElementById("num").value=rspProNum.replace(/,/g,"");
		}
	}catch(error){}
    window.close();
}
/*
	产品选择增减表格行
	tabId:表格Id
	wprId:产品Id
	wprName:产品名
	wprOtherName:产品别名
	wprCode:编号
	typName:单位
	salPrc:销售价格
	cusPrc:客户价格
*/
function tbladdrow(tabId,wprId,wprName,wprCode,typName,salPrc,cusPrc, hasTax,type) { 
	//alert("in"+tabId+","+wprId+","+wprName+","+wprOtherName+","+wprCode+","+typName+","+salPrc+","+cusPrc);

	var tableEl = null;
	var proCode = null;
	if(parent.document.getElementById(tabId)!=null){
		tableEl = parent.document.getElementById(tabId);
		proCode = parent.document.getElementsByName("wprId");
	}
	else{
		tableEl = $(tabId);
		proCode = document.getElementsByName("wprId");
	}
	var row = tableEl.insertRow(tableEl.rows.length);
	for(var i=0; i<proCode.length;i++){
		if(proCode[i].value==wprId){
			alert("您已经添加过 "+wprName+" 这个产品！")
			return ;
		}
	}
	if(cusPrc==undefined || cusPrc==''){ cusPrc = salPrc; }
	if(hasTax==undefined || hasTax==''){ hasTax = '1'; }
	var totalPrc = cusPrc;
	var col = row.insertCell(0);
	col.innerHTML = wprName; 
	col = row.insertCell(1); 
	col.innerHTML =wprCode+"&nbsp;";
	col = row.insertCell(2); 
	col.innerHTML =salPrc+"&nbsp;";
	col = row.insertCell(3);
	col.innerHTML = "<INPUT type='text' class='inputSize2' style='width:95%;' name='price"+wprId+"' value='"+cusPrc+"' onKeyUp=\"changePrice('"+wprId+"',this,'mon')\">";
	col = row.insertCell(4); 
	col.innerHTML = "<INPUT type='text' class='inputSize2' style='width:95%;' name='num"+wprId+"' value='1.00' onKeyUp=\"changePrice('"+wprId+"',this,'num')\">"; 
	col = row.insertCell(5);
	col.innerHTML = typName+"&nbsp;";
	if(type =="purOrd"){
		col = row.insertCell(6); 
		col.innerHTML = "<span id='totalPrc"+wprId+"'>"+totalPrc+"</span><input type='hidden' id='allPrice"+wprId+"' name='allPrice"+wprId+"' value='"+totalPrc+"' />";
		col = row.insertCell(7); 		 
		col.innerHTML = "<textarea rows='1' name='remark"+wprId+"' style='width:95%;' onblur='autoShort(this,500)'></textarea>"; 
		col = row.insertCell(8); 
		col.innerHTML = "<img src='images/content/del.gif' onClick='delTable(this,\""+tabId+"\")' alt='删除' style='cursor:pointer'/>"+
					"<INPUT style='display:none' type='checkbox' name='wprId' checked='checked' value="+wprId+">";
	}else{
		col = row.insertCell(6); 
		col.innerHTML = "<input type='checkbox' id='hasTax"+wprId+"' name='hasTax"+wprId+"' onClick=\"setTaxRate('"+wprId+"')\" "+(hasTax=='1'?"":"checked")+" /><input type='hidden' id='taxRate"+wprId+"' name='taxRate"+wprId+"' value='"+(hasTax=='1'?"":getSalTaxRate())+"' />";
		col = row.insertCell(7); 
		col.innerHTML = "<span id='totalPrc"+wprId+"'>"+totalPrc+"</span><input type='hidden' id='allPrice"+wprId+"' name='allPrice"+wprId+"' value='"+totalPrc+"' />";
		col = row.insertCell(8); 			 
		col.innerHTML = "<textarea rows='1' name='remark"+wprId+"' style='width:95%;' onblur='autoShort(this,500)'></textarea>"; 
		col = row.insertCell(9); 
		col.innerHTML = "<img src='images/content/del.gif' onClick='delTable(this,\""+tabId+"\")' alt='删除' style='cursor:pointer'/>"+
					"<INPUT style='display:none' type='checkbox' name='wprId' checked='checked' value="+wprId+">";
	}
	col.style.borderRight="0px";
	col.style.textAlign="center";
} 
function delTable(r,idName){
	var tableEl=parent.document.getElementById(idName);
	var i=r.parentNode.parentNode.rowIndex;
	tableEl.deleteRow(i);
	/*if(confirm("确定删除这条数据吗?")){
			
	}*/
}
