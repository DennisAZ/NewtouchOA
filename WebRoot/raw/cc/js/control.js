var pageCount = 1 ;
function doInit1(queryParam){
//loadData($F('pageIndex') , $F('pageLen') , $F('flowList') , $F('FLOW_STATUS'), $F('statrTime'), $F('endTime'), $F('RUN_ID'), $F('RUN_NAME'), queryParam);       
  loadData1($F('pageIndex'), $F('pageLen'), $F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'), queryParam);  
}
function loadData1(pageIndex, showLength, flowList, userType, user, runId, runName, queryParam){
  //alert(queryParam+"::::::queryParam");
  //alert("pageIndex::"+pageIndex+"pageLen::"+pageLen+"flowList::"+flowList+"userType::"+userType+"user::"+user);
  //alert("runId::"+runId+"runName::"+runName);
  if (pageCount < pageIndex) { // 如果总8页小于 检索的10页 置为1
    //重置页码为1;
    pageIndex = 1;
    $('pageIndex').value = pageIndex;
  }
  $('freshLoad').className = "pgBtn pgLoad"; 
  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
  var par = "";
  par = "pageIndex=" + pageIndex +"&pageLen="+ showLength +"&flowList="+ flowList;
  var temp = par + "&"+queryParam ;
  var url = contextPath+'/yh/core/funcs/workflow/act/YHMyWorkControlAct/getMyWorkList1.act';
  var rtJson = getJsonRs(url, temp);
  //alert(rsText);
  if(rtJson.rtState == "0"){
  //  alert("成功了");
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
    addEvent(pageIndex , pageCount);
    removeAllChildren($('dataBody'));
    
   if(listData.length > 0){
     $('hasData').show();
     $('noData').hide();
    // $('dataBody').update("");
     for(var i = 0 ;i < listData.length ;i ++){
     var data = listData[i];
      addRow1(data, i);
      }
     }else{
       $('hasData').hide();
       $('noData').show();
       $('msgInfo').update('没有检索到数据');
       } 
   $('freshLoad').className = "pgBtn pgRefresh";
  }
}
function removeAllChildren(parentNode){
  parentNode = $(parentNode);
  while(parentNode.firstChild){
    var oldNode = parentNode.removeChild(parentNode.firstChild);
    oldNode = null;
  }
}

//让页面显示多少条记录
//$F('FLOW_STATUS'), $F('statrTime'), $F('endTime'), $F('RUN_ID'), $F('RUN_NAME'), queryParam); 
//点击事件的效果
function addEvent(index,pageCount){
  var pageLen = $F('pageLen');
  var pageIndex = parseInt(index);
  //alert("pageLen"+pageLen +"null"+"pageIndex"+pageIndex);
  if(pageIndex == pageCount){  //如果索引页 等于 最大页
    $('pgNext').onclick = function(){};  //pgNext 下一页

    $('pgLast').onclick = function(){}; //pgLast 最后一页

    $('pgPrev').onclick = function(){  //pgPrev 上一页  
        $('pageIndex').value = pageIndex - 1; // 如果索引页 等于 最大页   就可以往上翻页
        loadData1(pageIndex - 1 , pageLen, $F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));
    };
    $('pgFirst').onclick = function(){
      $('pageIndex').value = 1; // 一直翻到第一页
      loadData1(1 , pageLen, $F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));       
    };
  }else if(pageIndex == 1){  // 如果索引页 等于1
    $('pgPrev').onclick = function(){};
    $('pgFirst').onclick = function(){};
    $('pgNext').onclick = function(){
      $('pageIndex').value = pageIndex + 1;  // 如果索引页 等于  1 就可以往下翻页
      loadData1(pageIndex + 1 , pageLen,$F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));
    };
    $('pgLast').onclick = function(){ // 一直翻到最后一页
      $('pageIndex').value = pageCount;
      loadData1(pageCount , pageLen,$F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName')); 
    };
  }else{  // 索引页在中间页时有 2 种情况   如：上所述 可以往上翻页   也可以往下翻页
    $('pgNext').onclick = function(){
    $('pageIndex').value = pageIndex + 1;
      loadData1(pageIndex + 1 , pageLen, $F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName')); 
    }
    $('pgLast').onclick = function(){
      $('pageIndex').value = pageCount;
      loadData1(pageCount , pageLen,$F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));
    }; 
    $('pgPrev').onclick = function(){
      $('pageIndex').value = pageIndex - 1;
      loadData1(pageIndex - 1 , pageLen,$F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));
    };
    $('pgFirst').onclick = function(){
      $('pageIndex').value = 1;
      loadData1(1 , pageLen,$F('flowList'), $F('userType'),$F('user'), $F('runId'), $F('runName'));
    };
  }
}


function addRow1(tmp, i){
  if(tmp.prcsFlag == "1"){
     var status = "<img src='" + imgPath + "/email_close.gif' alt='未接收'>";
  }
  else if(tmp.prcsFlag == "2"){
     var status = "<img src='" + imgPath + "/email_open.gif' alt='已接收'>"
  }
  else{
    var status = "<img src='" + imgPath + "/flow_next.gif' alt='已办结'>"
  }// $PRCS_NAME="第".$PRCS_ID."步：".$ROW["PRCS_NAME"];
  if(tmp.flowType == "1"){
    var FLOW_TYPE_DESC = "固定流程";
    var prcsId = tmp.prcsId;
    var prcsName = tmp.prcsName;
    var prcsNames = "第"+ prcsId +"步: "+prcsName;
    prcsName1 = prcsNames.substr(0,8)+"...";
  }
  else {
    var FLOW_TYPE_DESC = "自由流程";
    var prcsId = tmp.prcsId;
    var prcsNames = "第"+ prcsId +"步: "
    prcsName1 = prcsNames.substr(0,8)+"...";
  }
  var td = "<td align = center nowrap>" + status +"</td>"
           + "<td align = center nowrap><b>"+ tmp.sunId +"</b></td>"
           + "<td align = center nowrap><a title='流程类型:"+ FLOW_TYPE_DESC +"<br>流程名称:"+ tmp.flowName +"' href = javascript:;>"+ tmp.flowName +"</a></td>" 
           + "<td class = auto colspan=5 nowrap><a href = javascript:; title='"+ tmp.runName +"'>"+ tmp.runName +"</a></td>"
           + "<td class = auto><a href=javascript:; title='" + prcsNames + "'>" + prcsName1 + "</a></td>"  
           + "<td align=center nowrap><a href=javascript:;>" + tmp.userName + "</a></td>"
           if(tmp.prcsTime == "null"){
            if(tmp.prcsFlag == "1"){
               //alert("1111111");
             td += "<td title='"+ tmp.timeStr +"'><img src='" + imgPath + "/email_close.gif' alt='未接收'><br>"  
             }else{
             td += "<td align = center class = auto title='"+ tmp.timeStr +"'>"+ tmp.createTime  +"<br>"
             }
           }else{ //str.substr(str.length-2)
             var prcstime = tmp.prcsTime;
            prcstime =  prcstime.substr(prcstime, prcstime.length-10);
           td += "<td align = center class = auto title='"+ tmp.timeStr +"'>"+ prcstime +"<br>"  
           }
          if(tmp.timeOutFlag != null){
           
          // td += "<font color=red>"+ tmp.timeUsed +"</font><br>"  
            td += "<img src='" + imgPath + "/flow_time1.gif width='"+ tmp.WIDTH +"' height=6>"  
           }
          td += "</td>";
          td += "<td nowrap><a href=# title=转交下一步骤><img src='" + imgPath + "/flow_next.gif border=0> 转交</a>";
          td += "<a href = javascript:; title=将工作委托其他人办理> 委托</a>";
          td += "<a href = # class = dropdown hidefocus=true><span> 更多</span></a>";
          td += "<div class = attach_div align = left><a href = javascript:;>结束</a>";
          td += "<a href = javascript:;>删除</a></div></td>";
   var className = "TableLine2" ;    
  if(i%2 == 0){
  className = "TableLine1" ;
  }
  var tr = new Element("tr" , {"class" : className});
  $('dataBody').appendChild(tr);  
  tr.update(td);
}


