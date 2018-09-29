/* --------------------- */
/* ------- 选择完成方法 -------- */
/* -------------------- */
/* 
	选择客户完成传值 
	cusCode:客户编号
	cusName:客户名称
*/
function chooseCus(cusId,cusName){
	cusName = unescape(cusName);
	if(parent.document.getElementById("info")!=null){
		var info=parent.document.getElementById("info").value;
		if(info!=""){
			//查询条件里的选择客户
			if(info=="search"){    
				parent.document.getElementById("cusId").value=cusId;
				parent.document.getElementById("cusName").value=cusName;
			}
		} 
	}
	//弹出层内的选择客户
	else {	
		var iframe=parent.document.getElementById("popInside").contentWindow;
		iframe.document.getElementById("cusId").value=cusId;
		iframe.document.getElementById("cusName").value=cusName;
		/*if(iframe.document.getElementById("newContrat")!=null&&iframe.document.getElementById("newContrat").value=="1"){
			iframe.findCusPro();//选择客户后显示对应的项目下拉列表
		}*/
		if(iframe.document.getElementById("oppId")!=null){
			iframe.findOpp();//来往记录里选择客户后显示对应销售机会
		}
	}
	parent.removeCd("brow_popDiv");
}

/* 
	选择联系人完成传值 
	cusCode:联系人id
	cusName:联系人名称
*/
function chooseContact(conId,conName){
	conName = unescape(conName);
	var iframe=parent.document.getElementById("popInside").contentWindow;
	iframe.document.getElementById("conId").value=conId;
	iframe.document.getElementById("conName").value=conName;
	parent.removeCd("brow_popDiv");
}	

/* 
	选择产品完成传值 
	cusCode:产品编号
	cusName:产品名称
*/
function chooseProduct(wprId,wprName,type){
	wprName = unescape(wprName);
	var iframeObj = parent.document.getElementById("popInside");
	if(iframeObj){
		var iframe =  iframeObj.contentWindow;
		iframe.document.getElementById("wprId").value=wprId;
		iframe.document.getElementById("wprName").value=wprName;
		if(type!="undefined"){
			iframe.document.getElementById("pstName").value=wprName;
		}
		parent.removeCd("brow_popDiv");
	}
	else{
		parent.document.getElementById("wprId").value=wprId;
		parent.document.getElementById("wprName").value=wprName;
		parent.removeCd("brow_popDiv");
	}

}
/* 
	选择供应商完成传值 
	supId:产品编号
	supName:产品名称
*/
function chooseSupplier(supId,supName){
	supName = unescape(supName);
	var iframe=parent.document.getElementById("popInside").contentWindow;
	iframe.document.getElementById("supId").value=supId;
	iframe.document.getElementById("supName").value=supName;
	parent.removeCd("brow_popDiv");
}


/* 
	选择供应商完成传值 
	supId:产品编号
	supName:产品名称
*/
function choosePurOrd(puoId,puoCode){
	puoCode = unescape(puoCode);
	var iframe=parent.document.getElementById("popInside").contentWindow;
	iframe.document.getElementById("puoId").value=puoId;
	iframe.document.getElementById("puoCode").value=puoCode;
	parent.removeCd("brow_popDiv");
}


/* 
	选择库存完成传值 
	pstId:产品编号
	pstName:产品名称
*/
function chooseProdStore(pstId,pstName){
	pstName = unescape(pstName);
	var iframe=parent.document.getElementById("popInside").contentWindow;
	iframe.document.getElementById("pstId").value=pstId;
	iframe.document.getElementById("pstName").value=pstName;
	parent.removeCd("brow_popDiv");
}


/*
  员工选择完成传值
  seNo：工号
  seName：员工姓名
*/
function chooseEmpComplete(seNo,seName,rolName){
	if(parent.document.getElementById("popInside")!=null){
		var iframe=parent.document.getElementById("popInside").contentWindow;
		if(iframe.document.getElementById("soUserName").type!="text"){//传值给非输入框对象
			iframe.document.getElementById("soUserName").innerHTML=seName;
			if(iframe.document.getElementById("userName")!=null)
				iframe.document.getElementById("userName").value=seName;
		}
		else{//传值给输入框
			if(iframe.document.getElementById("soUserName")!=null){
			   iframe.document.getElementById("soUserName").value=seName;
			}
			if(iframe.document.getElementById("seId")!=null){
				iframe.document.getElementById("seId").value=seNo;
			}
			if(iframe.document.getElementById("seNo")!=null){
				iframe.document.getElementById("seNo").value=seNo;
			}
			if(rolName!=undefined){//输入框roleId,rolLev新建账号时使用
				if(iframe.document.getElementById("rolName")!=null){
					iframe.document.getElementById("rolName").innerHTML=rolName;
				}
				iframe.checkIsUseEmp();//检查选择的员工是否已分配账号
			}
		}
	}
	else{
		if(parent.document.getElementById("rightList")!=null){
			var iframe=parent.document.getElementById("rightList").contentWindow;
			if(iframe.document.getElementById("modSoUserName")!=null){
				iframe.document.getElementById("modSoUserName").value=seName;
			}
			if(iframe.document.getElementById("seNo")!=null){
				iframe.document.getElementById("seNo").value=seNo;
			}
		}
		else{
			parent.document.getElementById("modSoUserName").value=seName;
		}
		
	}
	parent.removeCd("brow_popDiv");
}
/*
  选择负责账号完成传值
  userCode：负责账号账号
  seName：负责账号姓名
*/
function chooseUserComplete(userCode,seName)
{	
    var info=parent.document.getElementById("info");
	if(info!=null&&info.value=="search"){
		//查询条件里的选择负责账号 
		parent.document.getElementById("userCode").value=userCode;
		parent.document.getElementById("uName").value=seName;
	}
	else{
	    if(parent.document.getElementById("popInside")!=null){
			var iframe=parent.document.getElementById("popInside").contentWindow;
			iframe.document.getElementById("userCode").value=userCode;
			iframe.document.getElementById("seName").value=seName;
	    }
    }
	parent.removeCd("brow_popDiv");
}

/*
	弹出层中勾选多个树节点
	treeName:树对象名
*/
function chooseManyNodes(treeObj,hasCallBack){
	var selectedNodes = treeObj.getSelectedNodes();
	var parEl;
	if(parent.document.getElementById("popInside")!=null){
		parEl=parent.document.getElementById("popInside").contentWindow;
    }else{
		parEl=parent;
	}
	if(!hasCallBack){//勾选树节点后调用的默认方法
		if(parEl.document.getElementById("nodeIds")!=null){
			parEl.document.getElementById("nodeIds").value = selectedNodes[0];
		}
		if(parEl.document.getElementById("nodeNames")!=null){
			parEl.document.getElementById("nodeNames").innerHTML = selectedNodes[1];
		}
		if(parEl.document.getElementById("nodeNamesInput")!=null){
			parEl.document.getElementById("nodeNamesInput").value = selectedNodes[1];
		}
	}
	else{//自定义的回调方法
		parEl.treeCallBack(selectedNodes);
	}
	
	parent.removeCd("brow_popDiv");
}



/*
  选择项目完成传值
  proId：项目id
  proTitle：项目名称
*/
function chooseProComplete(proId,proTitle)
{
    var info=parent.document.getElementById("info");
	if(info!=null&&info.value=="search"){
		//查询条件里的选择项目 
		parent.document.getElementById("proTitle").value=proTitle;
		parent.document.getElementById("proId").value=proId;
	}
	else if(info!=null&&info.value=="create"){
		if(parent.document.getElementById("popInside")!=null){
			var iframe=parent.document.getElementById("popInside").contentWindow;
			iframe.document.getElementById("proTitle").value=proTitle;
			iframe.document.getElementById("proId").value=proId;
		}
		if(iframe.findSubPro != undefined){
	  	 	iframe.findSubPro();
		}
	}
	parent.removeCd("brow_popDiv");
}
/*
  选择供应商完成传值
  supId：供应商id
  supName：供应商名称
*/
function chooseSupComplete(supId,supName)
{
	var info=parent.document.getElementById("info").value;
	if(info!=""){
		//查询条件里的选择客户
		if(info=="search"){    
			parent.document.getElementById("supId").value=supId;
			parent.document.getElementById("supName").innerText=supName;
		}
		//新建
		else if(info=="create"){	
			var iframe=parent.document.getElementById("popInside").contentWindow;
			iframe.document.getElementById("supId").value=supId;
			iframe.document.getElementById("supName").innerText=supName;
		}
	}
		parent.removeCd("brow_popDiv");
}

/*
	选择订单完成传值
	ordId:订单id
	ordTil:订单主题
	ordOwner:订单负责账号
*/
function chooseOrd(ordId,ordTil,seName,seId,cusName,cusId){
	ordTil = unescape(ordTil);
	seName = unescape(seName);
	cusName = unescape(cusName);
	if(parent.document.getElementById("popInside")!=null){
		var iframe=parent.document.getElementById("popInside").contentWindow;
		iframe.document.getElementById("ordTil").value=ordTil;
		iframe.document.getElementById("ordId").value=ordId;
		if(iframe.document.getElementById("seId")!=null){
		   iframe.document.getElementById("seId").value=seId;
		}
		if(iframe.document.getElementById("soUserName")!=null){
		   iframe.document.getElementById("soUserName").value=seName;
		}
		if(iframe.document.getElementById("cusName")!=null){
		   iframe.document.getElementById("cusName").value=cusName;
		   iframe.document.getElementById("cusId").value=cusId;
		}
		//调用回调函数
		if(iframe.selOrdCallBack != undefined){
			iframe.selOrdCallBack();
		}
	}
	parent.removeCd("brow_popDiv");
}

/*
	选择采购单完成传值
*/
function choosePurComplete(purId,purTil,purOwner,ownerCode,supName){
	if(parent.document.getElementById("popInside")!=null){
		var iframe=parent.document.getElementById("popInside").contentWindow;
		iframe.document.getElementById("purTil").value=purTil;
		iframe.document.getElementById("purId").value=purId;
		if(iframe.document.getElementById("seNo")!=null)
		   iframe.document.getElementById("seNo").value=ownerCode;
		if(iframe.document.getElementById("soUserName")!=null)
		   iframe.document.getElementById("soUserName").value=purOwner;
		if(iframe.document.getElementById("supName")!=null)
		   iframe.document.getElementById("supName").value=supName;
	}
	parent.removeCd("brow_popDiv");
}

/**
	选择负责账号
	name:名字
	id:userCode
*/
function chooseUser(code,name){
	try{
		window.opener.document.getElementById("userCode").value=code;
		window.opener.document.getElementById("name").value=name;
	}catch(error){}
	window.open("",'_self',""); 
	window.close();
	
}