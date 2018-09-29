

function doSeach(){
  var condition = $("#searchTxt").val();
  var type = "";
  if(condition == null || condition == ""){
     alert("搜索条件不能为空!");
     return false;
  }
  var radioSel = document.getElementsByName("selType");
  for(var i=0; i<radioSel.length; i++){
     if(radioSel[i].checked){
       type = radioSel[i].value;
     }
  }
  if(!type){
    alert("请选择类型");
    return false;
  }
  openWindow(condition, type);
}

function openWindow(condtion, type){
  var url = contextPath + "/subsys/inforesource/";
  if(type == '1'){
    url += "index1.jsp?";
  }else if(type=="2"){
    url += "imgIndex.jsp?";
  }else{
    url += "";
  }
  url += "openCondtion=" + encodeURI(condtion);  
  openWin(url);
  /*var widths = document.body.clientWidth;  
  var heights = document.body.clientHeight;  
  myleft=(screen.availWidth-500)/5;
  window.open(url,"read_news","height="+ heights +",width="+ widths +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); */
}

/**
 * 获得热点文章
 * @return
 */
function getHotArticle(keyIds,currNo2){
  $("#bone-left").html(image);
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getArticleListByKeyIDs.act?KeyIDs=" + keyIds +"&nStartPage=" + (currNo2-1) +"&nPageSize=10",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){            
       var toJson = eval("("+data+")");
       if(toJson){
         var len = toJson.rtData.Rows.length;
         var rtdata = toJson.rtData.Rows; 
         var page = new Pager(toJson.rtData.nTotalRec, currNo2-1);
         var totalNo = page.getPageCount();
         var currNo = toJson.rtData.nStartPage +1;
          if(len>0){
            if(len > 8) len = 8;
            var temp = "<ul>";
            for(var j=0; j<len; j++){
              var tName = rtdata[j].FILE_PATH;
              var subtitle = "";
              var moudle = tName.substring(tName.indexOf("\\"+ attachFilePath +"\\")+attachFilePath.length + 2,  tName.length);  
              moudle = moudle.substring(0,moudle.indexOf("\\"));       // email   
              
              var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx  
              var titleName = rtdata[j].TITLE;              
              var size = rtdata[j].FILE_SIZE;          
              var last = fName.substring(fName.indexOf(".")+1, fName.length); 
              var datep = rtdata[j].UPDATE_TIME;
              datep = datep.replaceAll("\/","").replaceAll("Date","").replaceAll("\\(","").replaceAll("\\)","");
              var showdate = new Date(parseInt(datep));
              var datestr = showdate.getFullYear()+"-"+ returnData(showdate.getMonth()+1) +"-" + returnData(showdate.getDate());                          
              var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
              readUrl += "&attachmentId="+ rtdata[j].FILE_ID +"&moudle="+ moudle +"&op=5&signKey=&print=";
              if(titleName.length >30){
                subtitle = titleName.substring(0,30)+" ...";
              }else{
                subtitle = titleName;
              }
              
              temp += "<li>";
               temp += "<a href=\"javascript:void(0)\" onclick=\"openWin('"+ readUrl +"')\" title=\""+ titleName +"\">";
                temp += subtitle;
               temp += "</a>";
               temp += "<span>";
                temp += "&nbsp;&nbsp;&nbsp;"+datestr;
               temp += "</span>";
              temp += "</li>";
            }
            temp += "</ul>";
           $("#bone-left").html(temp);  
          }else {
            $("#bone-left").html("没有相关文档");
          }
        }
      },
      error:function(a, b){
        if(b == "timeout"){
          $("#bone-left").html(LINK_TIME_OUT_MSG);  
        }
      }
    }); 
}
/**
 * 热点图片
 * @return
 */
function getHotPictrues(){
  $.ajax({
    url: contextPath + "/yh/subsys/inforesouce/act/YHManageImgAct/findTenImages.act",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){     
     var obj = eval("(" + data + ")");
     onloadData(obj);
    },
    error:function(a, b){
     if(b == "timeout"){
       $(".row-a").html(LINK_TIME_OUT_MSG); 
     }
    }
  });
}
/**
 * 加载热点图片
 * @param obj
 * @return
 */

function onloadData(obj){
  var imgObj = obj.rtData.data;
  var len = imgObj.length;   
  $(".row-a").html(image);  
  $(".row-b").html("");
  if(len > 5){ //分成两部分<5的部分，和>5的部分
    var tempa= "<ul>";  
    for(var i=0; i<5; i++){
      tempa += "<li class=\"img-li\">";
      tempa += "<span class=\"img-sp\"><a href=\"javascript:void(0)\" onclick=openImageWindow("+ imgObj[i].newsId +")><img src=\""+ getFirstPic(imgObj[i])+"\" border=\"0\" width=\"70px\" height=\"57px\"/></a></span>";
      tempa += "<span class=\"img-tit\"><a href=\"javascript:void(0)\" title=\""+ imgObj[i].subject +"\" onclick=openImageWindow("+ imgObj[i].newsId +")>"+ subTitle(imgObj[i].subject) +"</a></span>";
      tempa += "</li>";
    }
    tempa += "</ul>";  //小于5个的部分    
    $(".row-a").html(tempa);
    var tempb= "<ul>";
    for(var j=5; j<len; j++){
      tempb +="<li class=\"img-li\">";
      tempb += "<span class=\"img-sp\"><a href=\"javascript:void(0)\" onclick=openImageWindow("+ imgObj[j].newsId +")><img src=\""+ getFirstPic(imgObj[j]) +"\" border=\"0\" width=\"70px\" height=\"57px\"/></a></span>";
      tempb += "<span class=\"img-tit\"><a href=\"javascript:void(0)\" title=\""+ imgObj[j].subject +"\" onclick=openImageWindow("+ imgObj[j].newsId +")>"+ subTitle(imgObj[j].subject) +"</a></span>";
      tempb += "</li>";
    }
    tempb += "</ul>";
    $(".row-b").html(tempb);
  }else{
    var tempc= "<ul>";  
    for(var i=0; i<5; i++){
      tempc += "<li class=\"img-li\">";
      tempc += "<span class=\"img-sp\"><a href=\"javascript:void(0)\" onclick=openImageWindow("+ imgObj[i].newsId +")><img src=\""+ getFirstPic(imgObj[i]) +"\" border=\"0\" width=\"70px\" height=\"57px\"/></a></span>";
      tempc += "<span class=\"img-tit\"><a href=\"javascript:void(0)\" title=\""+ imgObj[i].subject +"\" onclick=openImageWindow("+ imgObj[i].newsId +")>"+ subTitle(imgObj[i].subject) +"</a></span>";
      tempc += "</li>";
    }
    tempc += "</ul>";  //小于5个的部分    
    $(".row-a").html(tempc);
  }  
}
/**
 * 获得第一个图片的路径
 * @return
 */
function getFirstPic(obj){
  var imgLen = obj.imgPaths.length;
  if(imgLen > 0){   
    return obj.imgPaths[0];
  }else{
    return contextPath + "/subsys/inforesource/tree1/images/tongyong.gif";
  }
}
/**
 * 处理字符长度的问题，最多5个字符
 * @param str
 * @return
 */
function subTitle(str){
  var substr = str.replace(new RegExp("</{0,1}[a-z,A-Z]>"),"");
      substr=substr.replace(new RegExp("</{0,1}[a-z,A-Z]>"),"");
  if(substr.length > 5){
    substr = substr.substring(0, 5);    
  }
  return  substr;
}
/**
 * 
 * 返回小于10的数字格式
 * @param data
 * @return
 */
function returnData(data){
  if(data < 10){
    return "0" + data;
  }else{
    return data;
  }
}
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
} 
var searchText = "";
function getSearchText() {
  return searchText;
}
function touchGraphSearch(text) {
  getSwf("smalltouchgraph").doSearch(text);
}
/**
 * 打开图片浏览
 * @param id
 * @return
 */
function openImageWindow(id) {
  var widths = document.body.clientWidth;  
  var heights = document.body.clientHeight;  
  window.open(contextPath + "/core/funcs/news/imgNews/imageWindow.jsp?id=" + id  , "imgWindow", "resizable=yes,scrollbars=yes,height="+heights+",width="+widths);
}

function getHotPerson(flag){
  $.ajax({
    url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getHotPersonOfMonth.act",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){     
     var obj = eval("(" + data + ")");
     onloadHotPerson(obj , flag);
    },
    error:function(a, b){
      if(b == "timeout"){
        $(".row-a-left").html(LINK_TIME_OUT_MSG);
      }
    }
  });
}
/**
 * 热点人物
 * @param obj
 * @param flag
 * @return
 */
function onloadHotPerson(obj , flag){
  var temp = "<ul>";
  for(var i=0; i<obj.length; i++){
    if(i==0){
      if (flag == 1) {
        getColumnChart("userName"  , obj[i].KeyID , obj[i].Person);
      } else {
        searchText = obj[i].Person;
        createLineChart(obj[i].KeyID);
      }
    }  
    var tName =  obj[i].FILE_PATH;
    var moudle = obj[i].MODULE;
    var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx   
     temp += "<li>";
     temp += "<span class=\"name-font\">";
     temp += "<a href=\"javascript:void(0)\" onclick=openWindow('"+ obj[i].Person +"',1)>";
     temp += subAstr(obj[i].Person);
     temp += "</a></span>";
     temp += "<span class=\"txt-font\">";
     //getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"')
     temp += "<a href=\"javascript:void(0)\" onclick=\"getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"');return false;\">";
     temp += obj[i].TITLE;
     temp += "</a>";
     temp += "</span>";   
    temp += "</li>";
  }
  temp += "</ul>";
  temp += "<div class=\"clear\"></div>";  
  $(".row-a-left").html(temp);
}

/**
 * 自动提示
 */
$(document).ready(function () {
  var url = contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullText.act";
  try{
     $("#searchTxt").autocomplete(url,
     {   max:10,
         scrollHeight: 300,
         multiple: true,
         multipleSeparator: " ",
         dataType: "json",
         parse: function (data) {
             return $.map(data.rtData, function (row) {
                 return {
                     data: row,
                     value: "" + row.Count,
                     result: "" + row.Term
                 }
             });
         },
         formatItem: function (row) {
            return "<span id='Left'>" + row.Term + "</span><span id='Right'>" + row.Count + "结果</span>";
         }
     }).result(function (e, row) {});
  }catch(e){
      
  }        
});
/**
 * 获得热点组织机构
 * @return
 */
function getHotOrganization(){
  $.ajax({
    url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getHotOrganizationOfMonth.act",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){     
     var obj = eval("(" + data + ")");
     onloadHotOrganization(obj);
    },
    error:function(a, b){
      if(b=="timeout"){
        $(".row-a-left").html(LINK_TIME_OUT_MSG);
      }
    }
  });
}

/**
 * 返回热点地区
 * @return
 */
function getHotAddress(){
  $.ajax({
    url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getHotAddressOfMonth.act",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){     
     var obj = eval("(" + data + ")");
     onloadHotAddress(obj);
    },
    error:function(a, b){
      if(b == "timeout"){
        $(".row-a-left").html(LINK_TIME_OUT_MSG);
      }
    }
  });
}
/**
 * 返回热点关键字
 * @return
 */
function getHotKeyword(){
  $.ajax({
    url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getHotKeywordOfMonth.act",
    type:"post",
    timeout:TIME_OUT,
    success:function(data){     
     var obj = eval("(" + data + ")");
     onloadHotKeyword(obj);
    },
    error:function(a, b){
      if(b == "timeout"){
        $(".row-a-left").html(LINK_TIME_OUT_MSG);
      }
    }
  });
}
/**
 * 加载组织机构
 * @param obj
 * @return
 */
function onloadHotOrganization(obj){
  var temp = "<ul>";
  for(var i=0; i<obj.length; i++){
    if(i==0){    
      getColumnChart("org"  , obj[i].KeyID , obj[i].Organization );
    }  
    var tName =  obj[i].FILE_PATH;
    var moudle = obj[i].MODULE;
    var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx   
     temp += "<li>";
     temp += "<span class=\"name-font\">";
     temp += "<a href=\"javascript:void(0)\" onclick=openWindow('"+ obj[i].Organization +"',1)>";
     temp += subAstr(obj[i].Organization);
     temp += "</a></span>";
     temp += "<span class=\"txt-font\">";
     //getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"')
     temp += "<a href=\"javascript:void(0)\" onclick=\"getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"');return false;\">";
     temp += obj[i].TITLE;
     temp += "</a>";
     temp += "</span>";   
    temp += "</li>";
  }
  temp += "</ul>";
  temp += "<div class=\"clear\"></div>";  
  $(".row-a-left").html(temp);
}

/**
 * 加载地区
 * @param obj
 * @return
 */
function onloadHotAddress(obj){
  var temp = "<ul>";
  for(var i=0; i<obj.length; i++){
    if(i==0){
      getColumnChart("address"  , obj[i].KeyID , obj[i].Address );
    }  
    var tName =  obj[i].FILE_PATH;
    var moudle = obj[i].MODULE;
    var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx   
     temp += "<li>";
     temp += "<span class=\"name-font\">";
     temp += "<a href=\"javascript:void(0)\" onclick=openWindow('"+ obj[i].Address +"',1)>";
     temp += subAstr(obj[i].Address);
     temp += "</a></span>";
     temp += "<span class=\"txt-font\">";
     //getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"')
     temp += "<a href=\"javascript:void(0)\" onclick=\"getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"');return false;\">";
     temp += obj[i].TITLE;
     temp += "</a>";
     temp += "</span>";   
    temp += "</li>";
  }
  temp += "</ul>";
  temp += "<div class=\"clear\"></div>";  
  $(".row-a-left").html(temp);
}
/**
 * 加载关键字
 * @param obj
 * @return
 */
function onloadHotKeyword(obj){
  var temp = "<ul>";
  for(var i=0; i<obj.length; i++){
    if(i==0){
      getColumnChart("subject"  , obj[i].KeyID , obj[i].Keyword );
    }  
    var tName =  obj[i].FILE_PATH;
    var moudle = obj[i].MODULE;
    var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx   
     temp += "<li>";
     temp += "<span class=\"name-font\">";
     temp += "<a href=\"javascript:void(0)\" onclick=openWindow('"+ obj[i].Keyword +"',1)>";
     temp += subAstr(obj[i].Keyword);
     temp += "</a></span>";
     temp += "<span class=\"txt-font\">";
     //getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"')
     temp += "<a href=\"javascript:void(0)\" onclick=\"getUrlWindow('"+ fName +"','"+ obj[i].MODULE +"','"+ obj[i].FILE_ID +"');return false;\">";
     temp += obj[i].TITLE;
     temp += "</a>";
     temp += "</span>";   
    temp += "</li>";
  }
  temp += "</ul>";
  temp += "<div class=\"clear\"></div>";  
  $(".row-a-left").html(temp);
}

function subAstr(str){
  if(str.length > 4){
    return str.substring(0, 4);
  }else{
    return str;
  }
}
