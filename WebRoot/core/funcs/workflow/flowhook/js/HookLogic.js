
function LoadDialogWindow(URL, parent, loc_x, loc_y, width, height){ 
	  window.showModalDialog(URL,parent,"edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px",true);
	}
function getMap(cellData, recordIndex, columIndex){
  var map = this.getCellData(recordIndex,"map");
  var module = this.getCellData(recordIndex,"mapName");
  eval("var modObj = " + module);
  
  var mapArray = map.split(",");
  var mapName = "";
  for (var i = 0 ;i < mapArray.length ;i++) {
    if (mapArray[i]) {
      var newArray = mapArray[i].split('=>');
      var name = newArray[0];
      var newName = modObj[name];
      mapName += newName + "=>" + newArray[1] + ",";
    }
  }
  return mapName;
}
function opts(cellData, recordIndex, columIndex){
	
	var seqId = this.getCellData(recordIndex,"seqId");
	var system = this.getCellData(recordIndex,"system");
    var opts1="<a href=\"javascript:editHook('"+seqId+"')\" >编辑 </a>";
    var opts2="<a href=\"javascript:delHook('"+seqId+"')\" > 删除</a>";
	var str=opts1;
	if(system=="0"){
		str+=opts2;
	}
	return "<center>" + str+ "</center>";
}

function delHook(seqId){
	if(window.confirm("确定删除该引擎！")){
    var param="seqId="+seqId;
    var url = contextPath+"/yh/core/funcs/workflow/act/YHFlowHookAct/deleteHookAct.act";
    var rtJson = getJsonRs(url,param);
    if (rtJson.rtState == "0") {
   
     window.location.href="index.jsp";
     
    } else {
      alert(rtJson.rtMsrg); 
    }
	}
}
function editHook(seqId){
	var url = contextPath + "/core/funcs/workflow/flowhook/edit.jsp?hid=" + seqId;
	var mytop=(screen.availHeight-450)/2-30;
  var myleft=(screen.availWidth-650)/2;
  window.open(url,"edit_"+seqId,"status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=650,height=450,left="+myleft+",top="+mytop+"\"");
}

function getFlowName(cellData, recordIndex, columIndex){
	
	var flowId = this.getCellData(recordIndex,"flowName");
    var param="flowId="+flowId;
    var url = contextPath+"/yh/core/funcs/workflow/act/YHFlowHookAct/getFlowNameAct.act";
    var rtJson = getJsonRs(url,param);
    if (rtJson.rtState == "0") {
      var data=rtJson.rtData;
      return "<center>" + data.flowName + "</center>";
    } else {
      alert(rtJson.rtMsrg); 
    }
}


function getStatus(cellData, recordIndex, columIndex){
	var status = this.getCellData(recordIndex,"status");

	var result="";
	if("0"==status){
		result="停用";
	}else if("1"==status){
		result="必选";
	}else if("2"==status){
		result="可用";
	}
	return result;
}


function deleteAll(){
 
}
function checkAll(field) {

	}