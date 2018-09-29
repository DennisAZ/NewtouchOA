var pageMgr;
function doInit(){
  
  var requestURL = contextPath + "/yh/subsys/infomgr/bilingual/act/YHBilingualAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID", width:100},
       {type:"text", name:"type", text:"类别", width:50,render:typeRender,align:'center'},
       {type:"text", name:"cnName", text:"中文名称", width:150,align:'center'},
       {type:"text", name:"enName", text:"英文名称", width:150,align:'center'},
       {type:"text", name:"soundFile", text:"英文语音文件", width:150,align:'center',render:soundRender},
       {type:"text", name:"entryUser", text:"录入人", width:80,align:'center'},
       {type:"text", name:"entryDate", text:"录入日期", width:80,align:'center',dataType:'date',format:'YY-MON-DD'},
       {type:"text", name:"enable", text:"是否启用", width:80,align:'center',render:enableRender},
       {type:"opts", width: 80, align:'center',opts:[
         {clickFunc:updateRecord, text:"修改"},
         {clickFunc:deleteRecord, text:"删除 "}
         ]
       }]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function updateRecord(recordIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = contextPath + "/subsys/infomgr/bilingual/edit.jsp?seqId=" + seqId;
  showWindow(url,'修改',450,300,false,true,function(){
    pageMgr.refreshAll();
  });
}

function soundRender(cellData, recordIndex, columInde){
  if (cellData){
    var renderData = '<img src="images/sound.png" style="cursor:pointer;" onclick="playSound(' + recordIndex + ')"></img>';
    return renderData;
  }
  else{
    return '';
  }
}

function deleteRecord(recordIndex){

  if (!confirm("确定要删除本条记录吗?"))
    return;

  var seqId = this.getCellData(recordIndex,"seqId");
  var delJson = getJsonRs(contextPath + "/yh/subsys/infomgr/bilingual/act/YHBilingualAct/deleteRecord.act?seqId=" + seqId);

  if (delJson.rtState == "0") {
    this.refreshAll();
  }else {
    alert(delJson.rtMsrg);
  }
}

function typeRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "职务职称";
  case '1':return "菜谱";
  case '2':return "标识标准";
  default:return "";
  };
}

function enableRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "<font color='red'>不启用</font>";
  case '1':return "启用";
  default:return "";
  };
}

function add(){
  showWindow(contextPath + '/subsys/infomgr/bilingual/add.jsp','新增双语标示',450,300,false,true,function(){
    pageMgr.refreshAll();
  });
}

function batchAdd(){
  showWindow(contextPath + '/subsys/infomgr/bilingual/batchAdd.jsp','批量导入',450,300,false,true,function(){
    pageMgr.refreshAll();
  });
}

function search(){
  para = "?cnName=" + encodeURI(encodeURI($('cnName').value)) + "&enName=" + $('enName').value + '&type=' + $('type').value;
  pageMgr.dataAction = contextPath + "/yh/subsys/infomgr/bilingual/act/YHBilingualAct/searchPage.act" + para;
  pageMgr.refreshAll();
}