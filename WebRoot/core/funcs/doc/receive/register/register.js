function doInit(){
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  parseObj(secrtGrade, "secretsLevel");
  parseObj(docType, "recType");
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  getFlowType();
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRegList.act?type=" +type ;
  cfgs = {
      dataAction: url,
      container: "container",
      moduleName:"doc",
      paramFunc: getParam,
      colums:[
              {type:"data", name:"title", text:"标题", width:"20%" ,render: titleRender },
              {type:"data", name:"sendDocNo", text:"来文文号", width:"10%"},
              {type:"data", name:"attach", text:"来文正文", width: "15%", dataType:"attach"},
              {type:"hidden",name:"attachId"},
              {type:"data", name:"fromDeptName", text:"来文单位", width:"5%"},
              {type:"data", name:"secretsLevel", text:"密级", width:"5%"},
              {type:"data", name:"recType", text:"类型", width:"5%"},  
              {type:"data", name:"registerTime", text:"登记时间", width:"10%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"status", text:"步骤", width:"8%" , render:prcsRender},
              {type:"hidden", name:"seqId"},
              {type:"hidden", name:"runId"},
              {type:"hidden", name:"runEnd"},
              {type:"hidden", name:"recNo"},
              {type:"hidden", name:"flowId"},
              {type:"selfdef",text:"操作", width:"15%",render:opRender}
              ]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
function getParam(){
  queryParam = $("queryForm").serialize();
  return queryParam;
}
function titleRender (cellData, recordIndex, columIndex) {
  var sub = cellData;
  if (sub.length > 20) {
    sub = sub.substring(0 , 20) + "...";
  }
  cellData = "<span title='" + cellData +"'>" + sub + "</span>";
  return cellData;
}
function parseObj(docType, id , value){
  if(docType){
    var tt = docType.split(",");
    var sel = document.getElementById(id);
    if(tt && sel){
      for(var i=0; i<tt.length; i++ ){  
        var op = new Option(tt[i], tt[i]);
        if (tt[i] == value) {
          op.selected = true;
        }       
        sel.options.add(op);
      }
    }
  }
}
function prcsRender (cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var status = this.getCellData(recordIndex,"status");
  if (runId != '0') {
    return "<a href=\"javascript:flowView("+ runId +"," + flowId +",'','" + sortId +"','" + skin +"' , 1);\">" + status + "</a>";
  } else {
    return status;
  }
}
function opRender(cellData, recordIndex, columIndex){
  var runEnd = this.getCellData(recordIndex,"runEnd");
  var seqId = this.getCellData(recordIndex,"seqId");
  var runId = this.getCellData(recordIndex,"runId");
  
  var fromDeptName = this.getCellData(recordIndex,"fromDeptName");
  var secretsLevel = this.getCellData(recordIndex,"secretsLevel");
  var sendDocNo = this.getCellData(recordIndex,"sendDocNo");
  var title = this.getCellData(recordIndex,"title");
  var recType = this.getCellData(recordIndex,"recType");
  var registerTime = this.getCellData(recordIndex,"registerTime");
  var recNo = this.getCellData(recordIndex,"recNo");
  
  var result = "";
  if (runId == '0' &&  runEnd != '1') {
    var param = '密级='+ encodeURIComponent(secretsLevel) + "&类型=" + encodeURIComponent(recType) +'&收文日期='+ registerTime +
      '&来文单位='+encodeURIComponent(fromDeptName)+'&原文编号='+encodeURIComponent(sendDocNo)+'&标题='+encodeURIComponent(title)+
      '&收文ID='+seqId +'&文号='+ recNo+ '&联系人='+encodeURIComponent(userName);
      
    //result += "<select id='select-" + seqId + "'><option value='0'>请选择流程</option>"
    //+ flowTypeStr
   // +"</select>";
    result += "&nbsp;<input id='param-"+seqId+"' type='hidden' value=\"" + param + "\"/>"
    result += "&nbsp;<a href='javascript:void(0)'  onclick='handler("+seqId+")'>办理</a>";
  } else {
    result += "&nbsp;<a  href='javascript:void(0)' onclick='formViewByRunId("+runId+")' />办理详情</a>";
  } 
  result += "&nbsp;<a  href='javascript:void(0)' onclick='edit("+seqId+")'>修改</a>";
  if (type == '2') {
    result += "&nbsp;<a  href='javascript:void(0)' onclick='endWorkFlow("+runId+")'>结束</a>";
  } 
  if (type == '3') {
    result += "&nbsp;<a  href='javascript:void(0)' onclick='restore("+runId+")'>恢复执行</a>";
  } 
  return result;
}
function endWorkFlow(runId) {
  if(!confirm("确认结束工作！")) {
    return ;
  }
 var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/endWorkFlow.act";
 var json = getJsonRs(url, "runIdStr=" + runId) ;
 if (json.rtState == '0') {
   alert(json.rtMsrg); 
   pageMgr.search();
 }
}
function restore(runId) {
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/restore.act?runId=" + runId ;
  var json = getJsonRs(url , par);
  if (json.rtState == '0') {
    alert(json.rtMsrg);
    pageMgr.search();
  }
}
function createNewWork(flowId , par , isNotOpenWindow , seqId ,flag , sortId){
  var url = contextPath +   moduleSrcPath + "/receive/act/YHDocReceiveHandlerAct/createWorkNew.act?seqId=" + seqId;
  if (par) {
    par = "flowId=" + flowId + "&" + par;
  } else {
    par = 'flowId=' + flowId;
  }
  if (flag) {
    url += "&attid=" + flag; 
  }
  var json = getJsonRs(url ,  par);
  if(json.rtState == "0"){
    var runId = json.rtData.runId;
    var url2 =   contextPath +   moduleContextPath +"/flowrunRec/list/inputform/index.jsp?skin="+ skin +"&sortId="+ sortId +"&runId=" + runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
    if (isNotOpenWindow) {
      parent.location.href = url2;
    } else {
      window.open(url2);
    }
  }else{
    alert(json.rtMsrg);
  }
}
function handler(seqId) {
  var param = $('param-' + seqId).value;
  var url = contextPath + "/core/funcs/doc/receive/register/handler.jsp?seqId=" + seqId ;
  var resultValue = window.showModalDialog(url,null,'dialogWidth=500px;dialogHeight=500px;help:no;status:no; ');  
  if (resultValue != undefined) {
    var attid = resultValue.attid;
    var flowId = resultValue.flowId;
    var sortId = resultValue.sort;
    createNewWork(flowId , param , true , seqId , attid , sortId);
  }
  return false;
}
//清空时间组件
function empty_date(){
  $("startTime").value="";
  $("endTime").value="";
}
function query() {
  pageMgr.search();
}