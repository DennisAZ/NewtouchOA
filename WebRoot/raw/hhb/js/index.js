var pageCount = 1 ;
function doInit3(queryParam){
  loadData($F('pageIndex') , $F('pageLen') , $F('flowList') , $F('FLOW_STATUS'), $F('statrTime'), $F('endTime'), $F('RUN_ID'), $F('RUN_NAME'), queryParam);       
}
 
/**
 * pageIndex 第几页
 * showLength 显示每页的长度
 * flowList 刷新显示每页的列表
 */
function loadData(pageIndex , showLength , flowId , FLOW_STATUS, statrTime, endTime, RUN_ID, RUN_NAME, queryParam){
  alert(flowId);
  if(pageCount < pageIndex){ // 如果总8页小于 检索的10页 置为1
  alert('页码超过了总页数!');
  //重置页码为1;
  pageIndex = 1;
  $('pageIndex').value = pageIndex;
  }
  $('freshLoad').className = "pgBtn pgLoad"; 
  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
  
  var par = "";
  par = "pageIndex=" + pageIndex + "&showLength=" + showLength + "&flowId=" + flowId;
  var temp = par + "&"+queryParam;
  var url = contextPath+'/yh/core/funcs/workflow/act/YHMyWorkPageAct/getMyWorkList.act';
  var rtJson = getJsonRs(url,temp);
  if(rtJson.rtState == "0"){
    var rtData = rtJson.rtData;
    var listData = rtData.listData; 
    var pageData= rtData.pageData;
    pageCount = pageData.pageCount;
    
    var recordCount = pageData.recordCount;
    var pgStartRecord = pageData.pgStartRecord;
    var pgEndRecord = pageData.pgEndRecord;
    var pgSearchInfo = "共&nbsp;" + recordCount +"&nbsp;条记录，显示第&nbsp;<span class=\"pgStartRecord\">"+ pgStartRecord +"</span>&nbsp;条&nbsp;-&nbsp;第&nbsp;<span class=\"pgEndRecord\">"+pgEndRecord+"</span>&nbsp;条记录";
    $('pgSearchInfo').innerHTML = pgSearchInfo;
    $('pageCount').innerHTML = pageCount;
  if(pageIndex == pageCount){  //响应事件 当我们点击到第一页，或最后一页， 就可以往下一页或 上一页点击  
    $('pgNext').className = "pgBtn pgNext pgNextDisabled";
    $('pgLast').className = "pgBtn pgLast pgLastDisabled";
    }else{                                          //否侧不可点击
      $('pgNext').className = "pgBtn pgNext pgNext";
      $('pgLast').className = "pgBtn pgLast pgLast";
      }
  if(pageIndex == 1){      //如果等于1 点击事件显示灰色，不可以点击
    $('pgPrev').className = "pgBtn pgPrev pgPrevDisabled";
    $('pgFirst').className = "pgBtn pgFirst pgFirstDisabled";
    }else{                     //否侧可以点击
     $('pgPrev').className = "pgBtn  pgPrev pgPrev";
     $('pgFirst').className = "pgBtn pgFirst pgFirst";
     }
  addEvent( pageIndex , pageCount);
  removeAllChildren($('dataBody'));
  if(listData.length > 0){
    for(var i = 0 ;i < listData.length ;i ++){
    var data = listData[i];
     addRow(data, i);
     }
    }else{
      $('hasData').hide();
      $('noData').show();
      if($F('flowList') != '0'){
        $('msgInfo').update('暂无待办工作');
        }else{
          $('msgInfo').update('没有检索到数据');
          }
      }
  $('freshLoad').className = "pgBtn pgRefresh";
 }  
function removeAllChildren(parentNode){
  parentNode = $(parentNode);
  while(parentNode.firstChild){
    var oldNode = parentNode.removeChild(parentNode.firstChild);
    oldNode = null;
  }
}
// 让页面显示多少条记录
//$F('FLOW_STATUS'), $F('statrTime'), $F('endTime'), $F('RUN_ID'), $F('RUN_NAME'), queryParam); 
//点击事件的效果  function addEvent(index,pageCount){
    var pageLen = $F('pageLen');
    var pageIndex = parseInt(index);
    //alert("pageLen"+pageLen +"null"+"pageIndex"+pageIndex);
    if(pageIndex == pageCount){  //如果索引页 等于 最大页
      $('pgNext').onclick = function(){};  //pgNext 下一页
      $('pgLast').onclick = function(){}; //pgLast 最后一页
      $('pgPrev').onclick = function(){  //pgPrev 上一页  
          $('pageIndex').value = pageIndex - 1; // 如果索引页 等于 最大页   就可以往上翻页          loadData(pageIndex - 1 , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'),queryParam);
      };
      $('pgFirst').onclick = function(){
        $('pageIndex').value = 1; // 一直翻到第一页        loadData(1 , pageLen, $F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'),queryParam);       
      };
    }else if(pageIndex == 1){  // 如果索引页 等于1
      $('pgPrev').onclick = function(){};
      $('pgFirst').onclick = function(){};
      $('pgNext').onclick = function(){
        $('pageIndex').value = pageIndex + 1;  // 如果索引页 等于  1 就可以往下翻页        loadData(pageIndex + 1 , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'), queryParam);
      };
      $('pgLast').onclick = function(){ // 一直翻到最后一页        $('pageIndex').value = pageCount;
        loadData(pageCount , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'), queryParam); 
      };
    }else{  // 索引页在中间页时有 2 种情况   如：上所述 可以往上翻页   也可以往下翻页      $('pgNext').onclick = function(){
      $('pageIndex').value = pageIndex + 1;
        loadData(pageIndex + 1 , pageLen, $F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'), queryParam); 
      }
      $('pgLast').onclick = function(){
        $('pageIndex').value = pageCount;
        loadData(pageCount , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'), queryParam);
      }; 
      $('pgPrev').onclick = function(){
        $('pageIndex').value = pageIndex - 1;
        loadData(pageIndex - 1 , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'),queryParam);
      };
      $('pgFirst').onclick = function(){
        $('pageIndex').value = 1;
        loadData(1 , pageLen,$F('flowList'),$F('FLOW_STATUS'), $F('statrTime'),$F('endTime'),$F('RUN_ID'),$F('RUN_NAME'),queryParam);
      };
    }
  }
  function addRow(tmp,i){
    var td = "<td nowrap align=center><input type=checkbox name=run_select onclick=check_select() value='"+ tmp.run_id +"'></td>"
             + "<td nowrap align=center class=TableContent>" + tmp.run_id +"</td>"
             + "<td class=auto align=center>" + tmp.runName +"</td>"
             + "<td align=center>" + tmp.begin_time +"</td>";
  //判断公众附件是否为空      
  if(tmp.attachmentName != 'null' ){
    td += "<td class=auto align=center>" + tmp.attachmentName +"</td>";
    }else{
      td += "<td class=auto align=center>无  </td>";
      }
  //判断状态是否为 ALL,0,1
  if (tmp.flow_status == "ALL") {
    if (tmp.end_time == "null") {
      td += "<td align=center nowrap><font color=red>执行中</font></td>";
    } else {
      td += "<td align=center nowrap>已结束</td>";
    }
  } else if (tmp.flow_status == "0" ) {
    td += "<td align=center nowrap><font color=red>执行中</font></td>";
  } else {
     td += "<td align=center nowrap>已结束</td>";
  }
  td+= "<td nowrap><a class=op><font color=blue>流程图&nbsp;</a><a class=op><font color=blue>&nbsp;&nbsp"+ tmp.state +"</a><a class=op><font color=blue>&nbsp;&nbsp;&nbsp;更多&nbsp;</a></td>";
  var className = "TableLine2" ;    
  if(i%2 == 0){
    className = "TableLine1" ;
  }
  var tr = new Element("tr" , {"class" : className});
  $('dataBody').appendChild(tr);  
  tr.update(td);
  }
}
