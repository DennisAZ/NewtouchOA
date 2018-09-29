var flowrun_new_left = {spanText:[['span1','请选择公文流程']]};
var flowrun_new_flowtop = {spanText:[['span1','常用公文流程'],['span2','上次建立的公文'],['spanMsg','没有公文']]};
var flowrun_new_edit = {spanText:[['span1','填写该公文的名称或文号']]};
var flowrun_list_index = {tabTitle:['未办理公文','办理中的公文','我已办理的公文','全部公文']};
var flowrun_list_index1 = {
    spanText:[['span1','公文名称/文号'],['span2','暂无待办公文'],['span3','所有公文类型']],
    tooltipMsg:[['tooltipMsg1','所选流程暂无待办公文'],['tooltipMsg2','无待办公文'],['tooltipMsg3','下一步骤尚未接收时可收回至本步骤重新办理，确认要收回吗？'],['tooltipMsg4','确认要结束该公文流程吗？'],['tooltipMsg5','确认要删除该公文吗？']]
    ,loadScriptUrl:contextPath + "/subsys/inforesource/docmgr/js/listIndex1.js"
}
var flowrun_list_inputform_index = {title:'公文办理'}
var flowrun_list_inputform_operate = {
    spanText:[['span1','关注此公文']],
    tooltipMsg:[['tooltipMsg1','确认要关注此公文吗？']],
    hiddenSpan:['span2','span5']
}
var docStr = "<table id='docTable' width=\"90%\" cellspacing=\"0\" cellpadding=\"3\" bordercolor=\"#b8d1e2\" border=\"1\" align=\"center\" class=\"TableList\" style=\"border-collapse: collapse;\"><tr class=\"TableHeader\"><td><div style=\"float: left;\"><img align=\"absmiddle\" src=\""+imgPath+"/green_arrow.gif\"/> 正文区</div></td></tr>";
docStr += "<tbody id=\"docList\"></tbody>";
docStr += "<tr id='tr1' style='display:none'><td align=\"left\">正文：<textarea  rows=\"40\" cols=\"100\" id=\"docContent\" name=\"id=\"docContent\"\"></textarea></td></tr>";
docStr += "<tr id='tr2' style='display:none'><td align=\"left\"><input type=\"button\" class=\"SmallButtonW\" value=\"保存\" onclick=\"saveDoc()\"/>&nbsp;<span id=\"createDocDiv\" style='display:none'><input onclick=\"createDoc()\" type=\"button\" class=\"SmallButtonW\" value=\"生成正文\"/></span></td></tr>";
docStr += "<tr id='tr3' style='display:none'><td align=\"left\" id='showTd'></td></tr>";
docStr += "</table>";
var _docId = "";
var _docName  = "";
var isPigeonhole = false;
var hasPigeonhole =  false;
var docShowPriv = true;
var docCreatePriv = true;
function getDoc() {
  $("attachDiv").show();
  getDoc1();
  if (docShowPriv) {
    $("div-extend").show();
    if (!_docId) {
      getDocList(true);
      if (docCreatePriv) {
        $('createDocDiv').show();
      }
      $('tr1').show();
      $('tr2').show();
    } else {
      var ext = _docName.split(".");
      var imgStr = getAttachImage(ext[ext.length - 1]);
      var str = "<a href='javascript:void(0)' onclick='return false;' onmouseover='createDiv(event  ,this , \""+ _docId +"\" , \""+ _docName +"\" , \""+ ext[ext.length - 1] +"\")'><img src='" + imgStr + "'/>&nbsp;" + _docName + "</a>";
      $('showTd').innerHTML = str; 
      $('tr3').show();
    }
  }
}
function getDocList(flag) {
  $('docList').update("");
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDoc2Act/getDocHistory.act";
  var json = getJsonRs(url , "runId=" + runId);
  if (json.rtState == "0") {
    var list = json.rtData;
    for(var i = 0 ;i < list.length ;i++){
      addDoc(list[i]);
      if (flag && i == 0) {
        $('docContent').update(list[i].content);
      }
    }
  }
}
function delDocHistory(seqId) {
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDoc2Act/delDocHistory.act";
  var json = getJsonRs(url , "seqId=" + seqId);
  if (json.rtState == "0") {
    var tr = $('docTr-' + seqId);
    tr.parentNode.removeChild(tr);
  }
}
function openDocHistory(seqId) {
  var url = contextPath + "/subsys/inforesource/docmgr/sendManage/openDoc.jsp?seqId=" + seqId;
  window.open(url);
}
function addDoc(feedback){
  var tr = new Element('tr' , {'class' : 'TableData'});
  tr.id = "docTr-" + feedback.seqId;
  $('docList').appendChild(tr);
  var td = new Element('td',{'align' : 'left','width' : '100%'});
  tr.appendChild(td);
  var title = "第" + feedback.prcsId + "步&nbsp;&nbsp;"+ feedback.prcsName 
  + "&nbsp;&nbsp;"
  + feedback.userName + "&nbsp;&nbsp;"  
  + feedback.editTime + "&nbsp;&nbsp;";
  if(feedback.del){
    title += "<img  style='cursor:pointer' src='"+imgPath+"/remove.png' onclick='delDocHistory("+feedback.seqId+")'/>";
  }
  title += "&nbsp;&nbsp;<img  style='cursor:pointer' title='查看正文' src='"+imgPath+"/focus.gif' onclick='openDocHistory("+feedback.seqId+")'/>";
  title += "<input type=hidden id='docContent-"+ feedback.seqId +"' value=\"" + feedback.content + "\"/>";
  var contentStr = feedback.contentStr;
  var attachment = "";
  var div = new Element('div');
  td.appendChild(div);
  div.innerHTML = title;
  
  div = new Element('div');
  td.appendChild(div);
  div.id = 'doc-' + feedback.seqId;
  div.style.lineHeight = "normal";
  div.innerHTML = contentStr;
}
function saveDoc(){
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDoc2Act/saveDoc.act";
  var docContent = $('docContent').innerHTML;
  var json = getJsonRs(url , "docContent=" + docContent + "&runId=" + runId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs);
  if (json.rtState == "0") {
    getDocList(false);
  }
}
function createDoc() {
  newDOC();
  officeDoc();
}
/**
* 加载公文相关项
* @return
*/
function getDoc1(){
 var param = 'runId=' + runId + '&flowPrcs=' + flowPrcs + "&flowId=" + flowId;
 var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDoc2Act/getDoc.act";
 var json = getJsonRs(url , param);
 if(json.rtState == '0'){
   if (json.rtData) {
     _docId = json.rtData.docId ;
     _docName = json.rtData.docName ;
   }
   if (json.isPigeonhole) {
     isPigeonhole = true;
   }
   if (json.hasPigeonhole) {
     hasPigeonhole = true;
   }
   if (!json.docShowPriv) {
     docShowPriv = false;
   }
   if (!json.docCreatePriv) {
     docCreatePriv = false;
   }
 }
}
/**
* 编辑公文
* @return
*/
function newDOC() {
 saveForm(4);
 if (_docId) {
   return ;
 } 
 var docName = "";
 var inputs = document.getElementsByTagName("input");
 for (var i = 0 ;i < inputs.length ;i ++) {
   var input = inputs[i];
   if (input && input.title == '标题') {
     docName = input.value;
   }
 }
 if (!docName) {
   docName = "新建文档";
 }
 docName += ".doc";
 var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDoc2Act/createDoc.act";
 var json = getJsonRs(url , "runId=" + runId + "&docName=" + encodeURIComponent(docName));
 if (json.rtState == '0') {
   _docId = json.rtData;
   _docName = docName;
 }
}
function officeDoc(){
  var url = getDocParam();
  openWindow(url,'正文',900,600);
}
function getDocParam() {
  var param = "attachmentName=" + _docName 
  + "&attachmentId=" + _docId 
  + "&moudle=workflow" 
  + "&op=7" 
  + "&signKey="
  + "&print="
  + "&runId=" + runId; 
  var url = contextPath + "/subsys/inforesource/docmgr/ntko2/indexNtko.jsp?" + param;
  url = encodeURI(url);
  return url;
}

var flowrun_list_inputform_main = {
    spanText:[['div-extend',docStr ],['span1','签发意见区'],['span2','启用签发手写签章功能'],['span3','此步骤禁止签发!']],
    tooltipMsg:[['tooltipMsg1','确认该公文已经办理完毕么？']
                 ,['tooltipMsg2','确认不保存此公文吗？']
                   ,['tooltipMsg3','此公文已被以下人员关注']
                     ,['tooltipMsg4','此公文没有人员关注']
                       ,['tooltipMsg5','确认要结束该公文流程吗？']
                         ,['tableTitle','登记信息']
                           ,['feedbackTitle','审批意见']
                             ,['dispAipTitle','处理单']
                             ,['isNoTab',1]
                   ]
     ,onloadFun:getDoc
}
/*****/
/*****/
var flowrun_list_turn_turnnext = {
    title:'公文 办结'
    ,tooltipMsg:[['tooltipMsg1','公文流转结束提醒：'],['tooltipMsg2','公文流转提醒：']]
    ,loadScriptUrl:contextPath + "/subsys/inforesource/docmgr/js/turnnext.js"
}
/*****/
var flowrun_query_index = {
    title:'公文查询' ,
    spanText:[['span1','所有公文类型']]
              ,tooltipMsg:[['tooltipMsg1','公文名称/文号']
                            ,['tooltipMsg2','删除公文，请至少选择其中一项！']
                              ,['tooltipMsg3','确认要删除所选公文吗？']
                                ,['tooltipMsg4','结束公文流转，请至少选择其中一项！']
                                  ,['tooltipMsg5',"确认要结束所选工作吗？"]
                                    ,['tooltipMsg6',"确认要关注此公文吗？"]
                                      ,['tooltipMsg7',"确认要取消关注此公文吗？"]
                                        ,['tooltipMsg8',"要导出公文，请至少选择其中一项。"]
                              ]
}
var flowrun_manage_index = {
    title:'公文监控'
    ,spanText:[['span1','所有公文类型']]
    ,tooltipMsg:[['tooltipMsg1','公文名称/文号']
                  ,['tooltipMsg2',"确认要删除当前公文吗？"]
                    ,['tooltipMsg3',"确认要结束当前公文流转吗？"]
                    ]
}
var flowrun_overtime_index = {
    title:'超时统计查询',
    tabTitle:['超时公文查询','超时公文统计']
}
var flowrun_overtime_overquery = {
    spanText:[['span1','所有公文类型'],['span2','公文接收时间: 从']]
}
var flowrun_overtime_overtotal = {
    spanText:[['span1','公文接收时间:']]
}
var flowrun_overtime_viewDetail = {
    title:'超时公文列表',
    tooltipMsg:[['tooltipMsg1','公文名称/文号']]
}
var flowrun_rule_index = {
    title:'公文委托'
}
var flowrun_rule_edit = {
    spanText:[['span1','编辑公文委托规则']]
}
var flowrun_rule_to = {
    spanText:[['span1','被委托公文记录']]
    ,tooltipMsg:[['tooltipMsg1','公文名称/文号']]
}
var flowrun_rule_from = {
    spanText:[['span1','已委托公文记录 ']]
    ,tooltipMsg:[['tooltipMsg1','公文名称/文号']]
}
var flowrun_destroy_index = {
    spanText:[['span1',' 可以查询到系统中所有已经删除的公文（不局限于自己经办的），然后进行彻底销毁或还原操作']
               ,['span2' ,'公文销毁与还原']
                 ,['span3','公文名称(关键字)：']]
  ,tooltipMsg:[['tooltipMsg1','确认要销毁指定范围内的所有公文吗？删除后将不可恢复，确认删除请输入大写字母“OK”']
                 ,['tooltipMsg2',"公文销毁失败删除失败："]
                   ,['tooltipMsg3',"确认要恢复指定范围内的所有公文吗？确认还原请输入大写字母“OK”"]
                     ,['tooltipMsg4', "公文销毁失败删除失败："]
                       ,['tooltipMsg5','请选择一条要销毁的公文！']
                         ,['tooltipMsg6',"确认要永久销毁所选公文吗？"]
                           ,['tooltipMsg7',"销毁公文成功！"]
                             ,['tooltipMsg8', "销毁公文失败："]
                               ,['tooltipMsg9',"确认要永久销毁所选公文吗？"]
                                 ,['tooltipMsg10',"销毁公文失败："]
                                   ,['tooltipMsg11', "要还原公文，请至少选择其中一项。"]
                                     ,['tooltipMsg12',"确认要将所选公文恢复到执行中吗？"]
                                       ,['tooltipMsg13',"确认要将所选公文恢复到执行中吗？"]
                    ]
}
var flowrun_destroy_search = {
    spanText:[['span5','已删除公文查询结果']]
  ,tooltipMsg:[['tooltipMsg1','确认要销毁指定范围内的所有公文吗？删除后将不可恢复，确认删除请输入大写字母“OK”']
                 ,['tooltipMsg2',"公文销毁失败删除失败："]
                   ,['tooltipMsg3',"确认要恢复指定范围内的所有公文吗？确认还原请输入大写字母“OK”"]
                     ,['tooltipMsg4', "公文销毁失败删除失败："]
                       ,['tooltipMsg5','请选择一条要销毁的公文！']
                         ,['tooltipMsg6',"确认要永久销毁所选公文吗？"]
                           ,['tooltipMsg7',"销毁公文成功！"]
                             ,['tooltipMsg8', "销毁公文失败："]
                               ,['tooltipMsg9',"确认要永久销毁所选公文吗？"]
                                 ,['tooltipMsg10',"销毁公文失败："]
                                   ,['tooltipMsg11', "要还原公文，请至少选择其中一项。"]
                                     ,['tooltipMsg12',"确认要将所选公文恢复到执行中吗？"]
                                       ,['tooltipMsg13',"确认要将所选公文恢复到执行中吗？"]
                                         ,['tooltipMsg14','公文名称/文号']
                    ]
}
var flowrun_log_search = {tooltipMsg:[['tooltipMsg1','公文名称/文号']]};
var flowrun_log_index = { spanText:[['span1','公文名称/文号：']]}
var flowrun_query_flowList = {spanText:[['span1','公文高级查询 - 请选择流程']]};
var flowrun_query_query= {
    spanText:[['span1','公文高级查询 - 请指定查询条件'],['span2','公文状态：'],['span3','公文流程基本属性']]
}
var flowrun_query_doQuery={spanText:[['span1','公文查询结果'],['span2','公文名称/文号']]
                        ,tooltipMsg:[['tooltipMsg1',"确认要关注此公文吗？"]
                                     ,['tooltipMsg2',"确认要取消关注此公文吗？"]
                                      ,['tooltipMsg3','结束公文流程，请至少选择其中一项！']
                                        ,['tooltipMsg4',"确认要结束所选公文吗？"]
                                          ,['tooltipMsg5','删除公文，请至少选择其中一项！']
                                            ,['tooltipMsg6',"确认要删除所选公文吗？"]
                                              ,['tooltipMsg7',"要导出公文，请至少选择其中一项。"]]
                 }
var flowrun_destroy_deleteMsrg = {tooltipMsg:[['tooltipMsg1',"操作已成功，共销毁公文"]]};
var flowrun_destroy_recoverMsrg = {tooltipMsg:[['tooltipMsg1', "操作已成功，共还原公文"]]};
var flowrun_list_allList = {
    spanText:[['span1','&nbsp;我的全部公文']]
}