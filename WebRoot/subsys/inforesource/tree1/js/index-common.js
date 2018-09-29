
/**
 *点击tag云图 获得文件的相关文件列表 长度 开始
 */
 var len = 0;
 function setLen(len){
   this.len = len;
 }
 function getLen(){
   return this.len;
 }
//////////////点击tag云图 获得文件的相关文件列表 长度  结束///////////////////////
 
 //////////////////点击tag云图 获取相关文件列表   开始/////////////////////////
 function setColKeyWord(keyId,keyword){
   articleListByKeyIDs(getKeyIds(), getKeywords());
 }
 //////////////////点击tag云图 获取相关文件列表   结束/////////////////////////
 
//设计 教育》》科技》》文化 的长度
 var leng = 0;
 function setLeng(leng){
   this.leng = leng;
 }
 function getLeng(){
   return this.leng;
 }
  /**
   * 点击tag云图标签后，content.jsp上会出现层次关系
   */
  var keyId = "";   
  var keyword = "";
  function setLayer(keyId,keyword){
    var num = getKeyIds().split(",");
    var leng = num.length;   
    if(num.length < 7){
       this.keyId = getKeyIds() + keyId+",";//每次点击tag都增加，首次keyId为空 
       this.keyword = getKeywords()+keyword +","; 
       layer(getKeyIds(), getKeywords());//调用子页面（Content.jsp）方法
    }
     setLeng(leng);
  }
  function getKeyIds(){
     return this.keyId;
 }

 function getKeywords(){
    return this.keyword;
 }
 function setKeyIds(keyIds){
   this.keyId = keyIds;
  }

 function setKeyWords(keyword){
   this.keyword = keyword;
 }

 /**
  *显示标签
  */
  function layer(keyIds, words){
    $("#label-up").empty();
    $("#label-up").html("");
    var ids = new Array();
    var id = keyIds.split(",");
    var wds = words.split(",");
    var temp = "";
    for(var i=0; i<id.length; i++){//<li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">词组词</a></li>

      if(wds[i] && id[i]){
         temp += "<li><a href=\"javascript:void(0);\" onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image8','','"+ contextPath +"/subsys/inforesource/tree1/images/x-hover.gif',1)\">";
         temp += "<img src=\""+ contextPath +"/subsys/inforesource/tree1/images/x-a.gif\" name=\"Image8\" width=\"16\" height=\"14\" border=\"0\" id=\"Image8\" /></a>";
         temp += "<a href=\"javascript:void(0);\" onclick=deleteTags("+ id[i] +",'"+ wds[i] +"')>"+ wds[i]  +"</a></li>";
      }
    }
    $("#label-up").append(temp);
  }  
  /**
    * 删除tag路径，1.删除tag标签，2,tag云图更新，3,文件列表更新
    */
    function deleteTags(keyId, keyWord){
      var key =  getKeyIds();
      var word = getKeywords();   
      var keyArr = key.split(",");
      var wordArr = word.split(",");
      var newKey = "";
      var newWord = "";
      if(keyArr && wordArr){
         for(var i=0; i<keyArr.length; i++){
           if(keyArr[i] && keyArr[i]!=keyId){//当点击删除时（id和name相同时newKey="",newWord=""） id和名称 相同的 去掉，不相同的保存，在调用layer方法 重新显示 主题词的关系如：科技>教育>文学>教育  把教育去掉
             newKey += keyArr[i] + ",";
             newWord += wordArr[i] + ","; 
           }
         }
      }
      var newKeyLength = newKey.split(",");
      setKeyIds(newKey);
      setKeyWords(newWord); 
      layer(newKey, newWord);
      articleListByKeyIDs(newKey,1);  //更新文件列表
      //window["sampleframe1"].getTagsByTagIds(newKey);//根据返回的tagIds重新查找tag云图   此方法在tag.js中
      getTagsByTagIds(newKey); //此方法在tag.js中
      setLeng(newKeyLength.length-1);
    }
    
    
  //标签默认显示
  $(document).ready(function(){
    $(".left1").show();
    $(".left2").hide();
    $(".left3").hide();
  });

  function ajaxTag(){//此方法在tag.js中（当点击tag标签触发方法）
    ajax1();
  }
  ///////////////////////点击定义按钮 开始//////////////////////////////////////
  var node="";
  function setNode(node){  
    this.node=node;
  }

  function getNode(){
    return node;
  }
  var mods = "";
  function setModules(mods){
    this.mods = mods;
  }

  function getModules(){
    return this.mods;
  }
  function refreshMe(){ 
    findTypeMenu(getFtype()); 
  }
  function refreshContent(){
    //var a = getIFrameDOM("contentframe");
    //window.location.reload();
    tabAjax();//重新添加自定义标签
  }
  function freshContent(){ 
    var treeNode = getNode(); 
    rightOpt(treeNode);
  }
  var ftype="1";
  function setFtype(ftype){
    this.ftype = ftype;
  }
  function getFtype(){
    return this.ftype;
  }
  function tomanage(){ 
    var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act?ftype="+getFtype();
    myleft=(screen.availWidth-500)/2;
    window.open(url,"_blank","height=800,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); 
  }
  /**
   * 点击左边的树进行的操作   */
    function rightOpt(treeNode){        
      modAjax("", treeNode);
    }   
    
    
    function filtag(){//填写 自定义标签
       var url = contextPath + "/subsys/inforesource/tagname.jsp";   
       window.open(url, "window","height=200,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=220,top=180,resizable=no");
    }
    /**
     * 查找标签
     */
      function tabAjax(){
         var url = contextPath + "/yh/subsys/inforesouce/act/YHMateNodeAct/findTagNameAjax.act";//
         $.getScript(url, function(data){    
           loadData(data);
         });
      }
       function loadData(data){ 
         var dataObj=eval("("+data+")");//把string转化为json对象     
         var temp ="";
         for(var i=0; i<dataObj.length; i++){ 
           temp += "<span class=\"label-span\"><a href=\"javascript:void(0);\" onclick=\"modAjax('','"+ dataObj[i]["nodes"] +"')\">"+dataObj[i]["tagName"]+"</a></span>";
         }
         $("#right-top").empty();
         $("#right-top").append(temp);
       } 

       $(document).ready(function(){
         tabAjax();
       });

   
    /**
    *默认加载相关的文档
     words 没有用
    */
     function articleListByKeyIDs(keyIds,currNo2){   
      // $(".hiddiv").attr("id","tab-a-con");
     // $("#contents").html("正在加载数据，请稍后......");
      $("#contents").html(image);
      $(".next-page").empty(); 
      $.ajax({
        url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getArticleListByKeyIDs.act?KeyIDs=" + keyIds +"&nStartPage=" + (currNo2-1) +"&nPageSize=" + defaultPageSize,
        type:"post", 
        success:function(data){
           $("#contents").empty();         
           var toJson = eval("("+data+")");
           if(toJson){
             var len = toJson.rtData.Rows.length;
             var rtdata = toJson.rtData.Rows; 
             var page = new Pager(toJson.rtData.nTotalRec, currNo2-1);
             var totalNo = page.getPageCount();
             var currNo = toJson.rtData.nStartPage +1;
             
              if(len>0){               
               setLen(len);
                var temp = "";
                for(var j=0; j<len; j++){
                  var tName = rtdata[j].FILE_PATH;  //D:\yh\attach\email\1109\681b8d81a339a1a3b4e22e0661d8a55f_文档 7153.docx
                  var subtitle = "";              
                  var moudle = tName.substring(tName.indexOf("\\"+ attachFilePath +"\\")+ attachFilePath.length + 2,  tName.length);               
                  moudle = moudle.substring(0,moudle.indexOf("\\"));       // email                  
                  var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx  
                  var titleName = rtdata[j].TITLE;              
                  var size = rtdata[j].FILE_SIZE;          
                  var last = fName.substring(fName.indexOf(".")+1, fName.length); 
                  var datep = rtdata[j].UPDATE_TIME;
                  datep = datep.replaceAll("\/","").replaceAll("Date","").replaceAll("\\(","").replaceAll("\\)","");
                  var showdate = new Date(parseInt(datep));
                  var datestr = showdate.getFullYear()+"-"+ (showdate.getMonth()+1) +"-" + showdate.getDate();                          
                  var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
                  readUrl += "&attachmentId="+ rtdata[j].FILE_ID +"&moudle="+ moudle +"&op=5&signKey=&print=";
                  if(titleName.length >12){
                    subtitle = titleName.substring(0,12)+" ...";
                  }else{
                    subtitle = titleName;
                  }
                  temp += "<div class =\"up-left-con-a\">";
                  temp += "<span class =\"h1\"><a href=\"javascript:void(0);\" onclick=getRelationArticleTags(\'"+ rtdata[j].FILE_ID +"\') style='cursor:pointer;' >"+ subtitle +"</a>&nbsp; &nbsp;&nbsp;</span>";
                  temp += "<span ><a href=\"javascript:void(0);\" onclick=openWin(\'"+ getUrl(fName,rtdata[j].FILE_ID, moudle) +"\') style='cursor:pointer;float:right;color:blue;'  align='left'><font size='2'>阅读</font></a></span>";
                  temp += "<p class=\"con-txt\"><a href=\"#\"><font size=2>"+ zhaiYao(rtdata[j].FILE_ID, 100)+"</font></a></p>";
                
                  temp += findAFileMate(rtdata[j].FILE_ID);//此方法在Page.js 显示相关的人名，地名，组织机构名
                  temp += "</div>";
                  
                }
               $("#contents").append(temp);            
               createPageNo(totalNo, currNo, "next-page", "tag");   
               clickFileName(rtdata[0].FILE_ID);//此方法显示本文章的 相关文档        
              }else {
                setLen(0);
                $("#contents").html("没有相关文档");
              }
            }
          }
        }); 
      //$("#Related-bg").empty();
     }
     /**
      *  替换s1为s2
      */
      String.prototype.replaceAll  = function(s1,s2){   
        return this.replace(new RegExp(s1,"gm"),s2);   
      }
      
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}


function doClick(text) {  
  getSwf("smalltouchgraph").doSearch(text); 
}
      ////////////显示小词立方  结束/////////////////////////////    




function findAFileMate(fileId){
  var temp ="";
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHAFileMateAct/getAFileMate.act",
    type:"post",
    data:"attachmentId="+fileId,  
    async:false,     
    success:function(data){
      var obj = eval("(" + data +")"); 
      var vale = obj.rtData; 
      if(vale.name){
        temp += "<p class=\"con-txt3\"><font size=2px; style='text-decoration:none;color:#000099;font-weight: bold'>人名:</font></p>";    
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+vale.name+"</font>&nbsp;</p>";
      }
      if(vale.area){
        temp += "<p class=\"con-txt3\"><font size=2px; style='text-decoration:none;color:#000099;font-weight: bold'>地名:</font></p>";
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+vale.area+"</font>&nbsp;</p>";
      }
      if(vale.orge){
        temp += "<p class=\"con-txt3\" ><font size =2px; style='text-decoration:none;color:#000099;font-weight: bold'>组织机构名:</font></p>";
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+vale.orge+"</font>&nbsp;</p>";
      } 
    }    
  });  
  return temp;
}