function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}


function openWin(url){  
  var widths = document.body.clientWidth;  
  var heights = document.body.clientHeight; 
  if(heights == 0 || !heights){
    heights = 700;
  }
  myleft=(screen.availWidth-500)/5;
  window.open(url, "window","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=10,top=10,resizable=yes,height="+ heights +", width="+ widths +"");
}

function getUrl(fName, file_id, moudle){
  var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
  readUrl += "&attachmentId="+ file_id +"&moudle="+ moudle +"&op=5&signKey=&print=";  
  return readUrl;
}


/**
  *通过keyIds查找相关的文档
   words 没有用
  */
  function getRelationArticleTags (fileId){
    $.ajax({
      url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getRelationArticleList.act?fileId="+ fileId,
      type:"post",
      timeout:TIME_OUT,
      success:function(data){
         $("#right-contents").empty();        
         var toJson = eval("("+data+")");
         if(toJson){
           var len = toJson.rtData.Rows.length;
           var rtdata = toJson.rtData.Rows;  
            if(len>0){
             len = len > defaultPageSize ? (defaultPageSize+1) : len;
             setLen(len);
              var temp = "";
              
              for(var j=0; j<len; j++){
                var tName = rtdata[j].FILE_PATH;
                var subtitle = "";              
                var moudle = tName.substring(tName.indexOf("\\"+ attachFilePath +"\\")+ attachFilePath.length + 2,  tName.length);               
                moudle = moudle.substring(0,moudle.indexOf("\\"));       // email                  
                var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx  
                var titleName = rtdata[j].TITLE;              
                var size = rtdata[j].FILE_SIZE;          
          
                var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
                readUrl += "&attachmentId="+ rtdata[j].FILE_ID +"&moudle="+ moudle +"&op=5&signKey=&print=";
                if(titleName.length >12){
                  subtitle = titleName.substring(0,10)+" ...";
                }else{
                  subtitle = titleName;
                }
                temp += "<div class=\"up-right-con-a\">";
                temp += "<span class=\"h3\"><a href=\"javascript:void(0)\" title=\""+ titleName +"\" onclick=\"openWin('"+ readUrl +"')\">"+ subtitle +"</a></span>";
                temp += "<span class=\"block\">";
                temp += "<a href=\"#\">"+ zhaiYao(rtdata[j].FILE_ID, 40)+"</a>";
                temp += "</span>";
                temp += getArticleTags(rtdata[j].FILE_ID);
                temp += "</div>";
              }
              
             $("#right-contents").append(temp);  
            }else {             
              $("#right-contents").html("没有相关文档");
            }
          }
        },
        error:function(a, b){
          if(b == "timeout"){
            $("#right-contents").html(LINK_TIME_OUT_MSG);
          }
        }
      });   
   }
  
  var searchTime=null;
  var searchPerson=null;
  var searchAdress=null;
  var searchOrg=null;
  function Search(currNo2,pageSize){      
    var keyword = $("#searchText").val(); 
        keyword = encodeURIComponent(keyword);
        if(!keyword){
          alert("搜索内容不能为空！");
          $("#searchText").focus();
          return ;
        } 
        $("#contents").html(image);
        $(".next-page").html("");
      $.ajax({
              async: false,
              cache: false,
              type: 'post',
              dataType: 'json',
              url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullTextDocList.act?nStartPage="+(currNo2-1)+"&nPageSize="+defaultPageSize,
              data: { q: keyword},
              timeout:TIME_OUT,
              success: function (data) {
                if(data){
                   var toJson =  data;
                   var currNo = toJson.CurPage +1;
                   var totalNo = toJson.TotalPage;
                   var len  = toJson.items.length;
                   var arrLen = toJson.groups.length;
                   var arrObj = toJson.groups;  
                   if(len > 0){ 
                     for(var j=0; j<arrLen; j++){
                       if(arrObj[j].GroupName == "时间"){
                         searchTime = arrObj[j].items;
                       }else if(arrObj[j].GroupName == "相关人"){
                         searchPerson = arrObj[j].items;
                       }else if(arrObj[j].GroupName == "相关地点"){
                         searchAdress = arrObj[j].items;
                       }else if(arrObj[j].GroupName == "相关单位"){
                         searchOrg = arrObj[j].items;
                       }
                     }                    
                     var temp = "";                     
                     for(var i=0; i<len; i++){
                       var subtitle = toJson.items[i].Title;
                       if(subtitle.length > 15){                        
                          subtitle = subtitle.substring(0, 15)+"...";
                       }
                       temp += "<div class=\"up-left-con-a\">";     
                       temp += " <span class=\"h1\"><a href=\"javascript:void(0);\" title=\"点击查看相关文档\" onclick=\"clickFileName('"+ toJson.items[i].FILE_ID +"')\">"+ subtitle +"</a></span>";
                       temp += "<span><a href=\"javascript:void(0);\" onclick=\"findNameAndMoudle('"+ toJson.items[i].FILE_ID +"')\" style='cursor:pointer;float:right;color:blue;'  align='left'><font size=2>阅读</font></a></span>";
                       temp += "<p class=\"con-txt\"><a href=\"javascript:void(0);\">"+ toJson.items[i].Description +"......</p>";         
                       temp += getAFileMate(toJson.items[i].FILE_ID);        
                       temp += "</div>";
                     } 
                     $("#contents").html(temp); 
                     createPageNo2(totalNo, currNo, ".next-page");   
                     clickFileName(toJson.items[0].FILE_ID);               
                   }else {
                      searchTime=[];
                      searchPerson=[];
                      searchAdress=[];
                      searchOrg=[];
                    $(".next-page").html("");
                    $("#contents").html("<font size=2>没有搜索结果！</font>");
                  }
                  changePal(typeConst);
                }
              },
              error:function(a, b){
                if(b=="timeout"){
                  $("#contents").html(LINK_TIME_OUT_MSG);
                }
              }
        });
  }  

  
  /**
   * 点击文件名的时候，显示文件主题词
   */
    function getArticleTags(fileId){   
      var keyword = "";
          $.ajax({
            url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getArticleTags.act?fileId=" + fileId ,
            type:"post",  
            async:false,  
            timeout:TIME_OUT,
            success:function(data){
              var temp = eval("("+data+")");  
              if(temp){       
                if(temp.rtData.length > 0){
                  var len = temp.rtData.length;
                  keyword += "<span class=\"block\">";
                  keyword += "<p class=\"con-txt2\"><font size=2>主题词:</font></p>";
                  if(len > 2){
                     len =2;
                  }
                  for(var i=0; i<len; i++){
                    keyword += "<p class=\"con-txt2\"><font size=2 style=color:blue>"+ subStr(temp.rtData[i].Keyword) +"&nbsp;&nbsp;</font></p>";
                  }
                  keyword += "</span>";
                }
              }
            },
            error:function(a, b){
              if(b == "timeout"){
                keyword = LINK_TIME_OUT_MSG;
              }
            }
          });   
          return keyword;
    }
    
    function subStr(str){
      if(str.length > 5){
        str = str.substring(0,5);
      }
      return str;
    }
    
    //点击树节点后要显示的文件列表-------------------------------Begin-------------qwx----------------------//  
     
     var data = ""
       function setData(data){
         this.data = data;
       }
       function getData(){
         return this.data;
       }

       var page = null;
       function setPage(page){
         this.page = page;
       }

       function getPage(){
         return this.page;
       }

       var fileId = "";
       function setFileId(fileId){
         this.fileId = fileId;
       }
       function getFileId(){
         return this.fileId;
       }
       
     function modAjax(modules, treeNode){
       $("#contents").html(image);    
       $(".next-page").empty(); 
       $("#right-contents").html("点击文件名查看相关文件");
       $.ajax({
         url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findFileList.act" ,
         type:"get",             
         data:"nodes="+ encodeURI(treeNode) +"&modules="+modules,   
         success:function(data){            
          $("#loading").hide();
           setData(data); 
           var data = getData();
           var toJson = eval("("+data+")"); 
           var len  = toJson.rtData.length;
           //setLen(len);
           var page = new Pager(len, 1);     
           setPage(page);  
           pagerNo(1);    
           createPage(1, page);       
         }
       });
     }
     /**
       *  点击文件名调用的方法（1,文件名变色， 展示相关文档，）
       */
       function clickFileName(fileId){
         setFileId(fileId);
         //getArticleTags(fileId); 显示这篇文档的主题词
         getRelationArticleTags(fileId); //相关文档
       }

       
      // 点击树节点后要显示的文件列表   -------------------------------end--------------qwx---------------------//
       
       /**
        * 点击搜索按钮触发1.文件列表显示， 小tachGraph显示， 相关搜索词列出
        */
       function doSearch(){
        
         //1.
         Search(1, 10);
         //2.
         var word = $("#searchText").val();  
         relationWordAjax(word); 
         if(word){           
          doClick(word);
         }
       //3.         
               
       }
       
       $(document).ready(function () {
         var url = contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullText.act";
         try{
            $("#searchText").autocomplete(url,
            {   max:3,
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
            }).result(function (e, row) {doSearch()});
         }catch(e){
             
         }        
      });
       
       /**
        * 点击相关搜索词，搜索框输入搜索词, 文件列表显示, 查出这个词的相关搜索词
        * @return
        */
      function clickRelationWords(word){
        $("#searchText").val(word);             //搜索框输入搜索词
        doSearch();                             // 文件列表显示, 查出这个词的相关搜索词     
      }
      
      /**
       * 调用ajax 查出相关搜索词
       * @param word
       * @return
       */
      function relationWordAjax(word){  
        word = encodeURIComponent(word.trim());
        $.ajax({
          url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullText.act",
          type:"get",  
          data:"q="+ word +"&limit=10",  
          timeout:TIME_OUT,
          success:function(data){
           var jsonObj = eval( "(" + data + ")" ); 
           fillRelationWord(jsonObj);
          },
          error:function(a, b){
            if(b == "timeout"){
              $(".down-box-cen").html(LINK_TIME_OUT_MSG);
            }
          }
        });
      }
      /**
       * 填充相关搜索词
       * @param obj
       * @return
       */
      function fillRelationWord(obj){
        if(obj){
          var len = obj.rtData.length;
          var temp = "";
          if(len > 0){              
            temp += "<div class=\"db-up\">";
            temp += "<ul>";
            for(var i=0; i<len; i++){
              var name = obj.rtData[i].Term;
              temp += "<li><a href=\"javascript:void(0);\" onclick=\"clickRelationWords('"+ name +"')\">"+ name +"</a></li>";
            }
            temp += "</ul>";
            temp += "</div>";
          }else{
            temp += "<div class=\"db-up\"><ul><li><a>没有相关搜索词！</a></li><ul></div>";
          }
        }
       $(".down-box-cen").html(temp);
      }
   
      /**
       * 搜索框事件
       */
   $(document).ready(function(){     
     $("#searchText").keydown(function(event){
       if(event.keyCode == 13){
         doSearch();
         return false;
       }
     }); 
   });   
   
   function findNameAndMoudle(fileId){
     $.ajax({
       url:contextPath + "/yh/subsys/inforesouce/act/YHAFileMateAct/findFileNameAndMoudle.act",
       type:"get",             
       data:"attachmentId="+ fileId,   
       success:function(data){
        var jsonObj = eval( "(" + data + ")" ); 
        if(jsonObj.rtState == '0'){
          var fName = jsonObj.rtData.fileName;
          var moudle = jsonObj.rtData.moudle;
          var url =  getUrl(fName, fileId, moudle);
          openWin(url);
        }
       }
     });
   }
   /**
    * 点击tag云图时，小tachGraph也变
    * @param text
    * @return
    */
   function doClickTag(text) {
     getSwf("smalltouchgraph").doSearch(text);
   }
   
   
   function zhaiYao(fileId, len){//通过sign_files表 中的 file_id查询摘要内容(ABSTRACT)
     var zhaiyaoFile ="";
     var vale= "";
     $.ajax({
      url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/getzhaiYao.act",
      type:"post",
      data:"fileId="+fileId,  
      async:false, 
      timeout:TIME_OUT,
      success:function(data){
        var obj = eval("(" + data +")");
        vale= obj.rtData; 
      },
      error:function(a, b){
        alert(b);
        if(b == "timeout"){
          vale = LINK_TIME_OUT_MSG;
        }
      }
    });  
     if(vale.length > len){
       vale = vale.substring(0, len) + "....";
     }
     if(vale == 'null' || vale == null){
       vale = "";
     }
     return vale;
  }
   
 //选中文本类型||图片类型||视频类型
   function findTypeMenu(typemenu){   
     if(!typemenu){
       typemenu = 1;
     }     
     $.ajax({
       url:contextPath + "/yh/subsys/inforesouce/act/YHMateTreeAct/findMymoth.act?typemenu="+typemenu,
       type:"post",
       success:function(data){   
         var obj = eval("(" + data +")");
         window.trData = obj;
         onload(obj);
       }
     });
   }   
       