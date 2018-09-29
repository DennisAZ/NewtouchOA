var allRule = "";
var requestUrl = contextPath + "/yh/core/funcs/workflow/act/YHRuleAct";
function clearUser(arr) {
  $(arr[0]).value = "";
  $(arr[1]).value = "";
}
function check_all() {
  if($('checkAll').checked == true){
    $('flowId').disabled = true;
  } else {
    $('flowId').disabled = false;
  }
}
function data_disable(){
  if($('alwaysOn').checked) {
    $('beginDate').disabled = true;
    $('endDate').disabled = true;
  } else {
    $('beginDate').disabled = false;
    $('endDate').disabled = false;
  }
}
//加载流程
function loadFlowType(){
  var url = contextPath+'/yh/core/funcs/workflow/act/YHFlowTypeAct/getFlowTypeJson1.act?sortId=' + sortId;
  var json = getJsonRsAsyn(url , "", doFlowTypeJson);
}
function doFlowTypeJson(json) {
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option");      
    opt.value = rtData[i].seqId ;      
    opt.innerHTML = rtData[i].flowName ;      
    $('flowId').appendChild(opt) ;                        
  }    
}
function doInit(){
  loadFlowType();
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  loadRule();
}
function checkForm() {
   if(!$('checkAll').checked 
       && !$('flowId').value) {
     alert("请选择工作流程!");
     return (false);
   }
   if(!$('toId').value){
     alert("被委托人不能为空!");
     return (false);
   }
   if (!$('alwaysOn').checked) {
     if ($('beginDate').value && !isValidDateStr($('beginDate').value)) {
       alert("生效日期格式不对，应形如 1999-01-02");
       return false;
     }
     if ($('endDate').value && !isValidDateStr($('endDate').value)) {
       alert("终止日期格式不对，应形如 1999-01-02");
       return false;
     }
   }
   var toId = $('toId').value;
   if (isAdminRole){
     var userId = $('userId').value;
     if (toId == userId) {
       alert("委托人和被委托人不能相同!");
       return false;
     }
   } 
   if (!userId && toId == loginUserId ) {
     alert("不能委托给自己！");
     return false;
   }
   
   if($('checkAll').checked) {
     var STR="";
     var select = $('flowId');
     for(var i = 1;i < select.options.length-1;i++)
       STR += select.options(i).value+",";
       $('flowIdStr').value=STR;
   }
   addRule();
}
function addRule() {
  var url = requestUrl + "/addRule.act";
  var json = getJsonRs(url , $('ruleForm').serialize());
  if (json.rtState == "0") {
    alert('添加成功!');
    $('beginDate').disabled = false;
    $('endDate').disabled = false;
    $('ruleForm').reset();
    loadRule();
    //查出自己的记录  }
}
function loadRule() {
  var ruleState = $('ruleState').value;
  var queryUserId  = "";
  var queryUser = document.getElementById('queryUserId'); 
  if (queryUser) {
    queryUserId = queryUser.value;
  }
  var par  = "ruleState=" + ruleState;
  if (queryUserId) {
    par += "&queryUserId=" + queryUserId;
  }
  $('hasData').innerHTML = "";
  var url = requestUrl + "/loadRule.act?sortId=" + sortId;
  var json = getJsonRsAsyn(url , par,myRuleTab ) ;
}
function myRuleTab(json) {
  var ruleState = $('ruleState').value;
  if (json.rtState == "0") {
    var data = json.rtData;
    if (data.length > 0) {
      var table = "";
      if (ruleState == "0") {
        table += "<table  id=\"ruleTable\" width=\"100%\" class=\"TableBlock\">";
        table += "<thead class=\"TableHeader\"><tr><td  align=\"center\" width=\"50\">状态</td><td  align=\"center\">流程名称</td><td  align=\"center\">被委托人</td><td nowrap align=\"center\">有效期</td><td  align=\"center\">操作</td></tr></thead>";
        table += "<tbody id=ruleList></tbody>";
        table += "<tfoot class=\"TableFooter\">"
          + "<tr><td  align=\"center\" colspan=\"9\">"
          + "<input type=\"button\" class=\"SmallButtonW\"  value=\"全部删除\" onclick=\"delAll()\">"
          + "<input type=\"button\" class=\"SmallButtonW\" value=\"全部开启\" onclick=\"closeOrOpenAll(true)\">"
          + "<input type=\"button\" class=\"SmallButtonW\" value=\"全部中止\" onclick=\"closeOrOpenAll(false)\">"
          + "</td></tr></tfoot>" ;  
      } else {
        table += "<table width=\"90%\" class=\"TableBlock\"><thead class=\"TableHeader\"><tr>"
        + "<td  align=center width=50>状态</td>"
        + "<td  align=center>流程名称</td>"
        + "<td  align=center>委托发起人</td>"
        + "<td  align=center>有效期</td></tr></thead><tbody id=ruleList></tbody></table>";
      }
      $('hasData').update(table);
      allRule  = "";
      for (var i = 0 ; i < data.length ; i ++ ){
        var tmp = data[i];
        
        if (ruleState == "0") {
          addRow0(tmp , i);
        } else {
          addRow1(tmp , i);
        }
      }
      $('noData').hide();
      $('hasData').show();
    } else {
      $('hasData').hide();
      var msg = "尚未定义委托规则";
      if (ruleState == 0 ) {
        msg = "尚无被委托记录";
      }
      $('noDataMsg').update(msg);
      $('noData').show();
    }
  } 
}
//var ruleList = [{ruleId : 11, status:1 , flowName:'ddd' , toUserName : 'dd', formUserName:'dddd' , available:''},{ruleId : 11, status:0 , flowName:'ddd',toUserName:'ddd', formUserName:'dddd' , available:''}];
function addRow0(data , i) {
  allRule += data.ruleId + ",";
  var td = "";
  var image = "/rule0.gif";
  if (data.status == 1) {
    image = "/rule1.gif";
  }
  var tmp = "&nbsp;";
  if (data.available) {
    tmp = data.available;
  }
  td += "<td align=center><img src=\""+imgPath + image +"\" width=20 height=20></td>";
  td += "<td align=center>"+ data.flowName +"</td>";
  td += "<td align=center>"+ data.toUserName +"</td>";
  td += "<td align=center>"+ tmp +"</td>";
  td += "<td align=center>";
  if (data.isOpen == 1) {
    td += "<input type=\"button\" class=\"SmallButtonW\" value=\"关闭\" onclick=\"openOrClose('"+data.ruleId+"' , true)\">";
  } else {
    td += "<input type=\"button\" class=\"SmallButtonW\" value=\"开启\" onclick=\"openOrClose('"+data.ruleId+"' , false)\">";
  }
  td += "<input type=\"button\" class=\"SmallButtonW\" value=\"编辑\" onclick=\"openEdit("+data.ruleId+")\">";
  td += "<input type=\"button\" class=\"SmallButtonW\" value=\"删除\" onclick=\"delRule('"+data.ruleId+"')\">";
  td +="</td>";
  
  var className = "TableLine2" ;    
  if(i%2 == 0){
    className = "TableLine1" ;
  }
  var tr = new Element("tr" , {"class" : className});
  $('ruleList').appendChild(tr);  
  tr.update(td);
}
function addRow1(data , i) {
  var td = "";
  var image = "/rule0.gif"
  if (data.status == 1) {
    image = "/rule1.gif"
  }
  td += "<td align=center><img src=\""+imgPath + image +"\" width=20 height=20></td>";
  var tmp = "&nbsp;";
  if (data.available) {
    tmp = data.available;
  }
  td += "<td align=center>"+  data.flowName +"</td>";
  td += "<td align=center>"+ data.formUserName +"</td>";
  td += "<td align=center>"+ tmp +"</td>";
  var className = "TableLine2" ;    
  if(i%2 == 0){
    className = "TableLine1" ;
  }
  var tr = new Element("tr" , {"class" : className});
  $('ruleList').appendChild(tr);  
  tr.update(td);
}
function openEdit(id) {
  location = "edit.jsp?ruleId=" + id + "&sortId=" + sortId + "&skin=" + skin;
}
function openOrClose(ruleId , isOpened ) {
  var url = requestUrl + "/openOrClose.act";
  var json = getJsonRs(url , "isOpened=" + isOpened + "&ruleId=" + ruleId ) ;
  if (json.rtState == "0") {
    alert('操作成功！');
    loadRule();
  }
}
function delRule(ruleId) {
  var url = requestUrl + "/delRule.act";
  var json = getJsonRs(url , "ruleId=" + ruleId) ;
  if (json.rtState == "0") {
    alert('操作成功！');
    loadRule();
  }
}
function closeOrOpenAll(isOpen) {
  var url = requestUrl + "/closeOrOpenAll.act";
  var json = getJsonRs(url , "isOpen=" + isOpen + "&ruleIds=" + allRule) ;
  if (json.rtState == "0") {
    alert('操作成功！');
    loadRule();
  }
}
function delAll() {
  var url = requestUrl + "/delAll.act";
  //if (confirm("确认全部删除?")) {
    var json = getJsonRs(url , "ruleIds=" + allRule) ;
    if (json.rtState == "0") {
      alert('操作成功！');
      loadRule();
    }
  //}
}