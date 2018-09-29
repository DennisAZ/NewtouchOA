
/**
 * 分页类
 * @param total 总的记录数
 * @param currNo 当前页数
 * @return
 */
function Pager(total, currNo){
  this.total = total;
  this.currNo = currNo;
  this.pageSize = defaultPageSize; //每个页面显示10条数据
  this.pageCount = 1; //总的页面数，默认为1
}

/**
 * 获总的记录数
 */
Pager.prototype.getTotal = function(){
  return this.total;
}

/**
 * 获得当前页
 */
Pager.prototype.getCurrNo = function(){
  return this.currNo;
}

/**
 * 获得每个页面显示多少条数据
 */
Pager.prototype.getPageSize = function(){
  return this.pageSize;
}

/**
 * 获得总的页面数
 */
Pager.prototype.getPageCount = function(){
  this.pageCount = ((this.getTotal() % this.getPageSize()) == 0) ? (this.getTotal() / this.getPageSize()): ( Math.floor(this.getTotal() / this.getPageSize()) + 1);
  return this.pageCount;
}




/**
 * 创建分页
 * @param currNo 当前页
 * @param pu     分页类
 * @return page.getPageCount(),
 */
function createPage(currNo, pu){   
  $(".next-page").show();
  $(".next-page").html("");
  var constNo = 6;
  var totalNo = pu.getPageCount(); 
  
  var pageNo="<table width=\"\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
      pageNo += "<tr>";
      
  if(currNo > 1){
    pageNo += "<td width=\"63\"><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ (currNo-1) +")\">《&nbsp;上一页</a></td>";
  }
  if(currNo - constNo >0){
    for(var no = currNo - constNo; no< currNo; no++){    //当前页前面的页数    
      pageNo += "<td width=\"68\" class=\"page-td\"><a href=\"javascript:void(0)\" onclick=\"pagerNo("+ no +")\">"+ no +"</a></td>";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
      pageNo += "<td width=\"68\" class=\"page-td\"><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ no +")\">"+ no +"</a></td>";
    }
  }  
  
  
  if(totalNo >1){
    pageNo += "<td width=\"68\" class=\"page-td\">["+ currNo +"]</a></td>";     //当前页
  }
  
  
  if(currNo+constNo < totalNo){     //当前页以后的页面    
    for(var no2 = currNo +1; no2< currNo+constNo; no2++){     
      pageNo += "<td width=\"68\" class=\"page-td\"><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ no2 +")\">"+ no2 +"</a></td>";
    }
    
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
      pageNo += "<td width=\"68\" class=\"page-td\"><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ no2 +")\">"+ no2 +"</a></td>";
    }
  }
  if(currNo < totalNo){
   pageNo += "<td width=\"131\"><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ (currNo +1) +")\">&nbsp;&nbsp;&nbsp;&nbsp;下一页&nbsp;》</a></td>";
  } 
  pageNo += "</tr>";
  pageNo += "</table>";  
  $(".next-page").html(pageNo);
}

/**
 * 
 * 点击页码时候执行的方法
 * @param currNo 当前号码
 * @param pu     分页类
 * @return
 */
function pagerNo(currNo){ 
  var data = this.getData();
  var pu = this.getPage();     
  if(data){
    $("#contents").empty();
    $("#contents").html(image);
    
    $(".next-page").hide(); 
    var json =eval("("+data+")");
    var rtdata = json.rtData; 
    var len = rtdata.length;
    var fName ="";
    var last = "";
    var temp = "";
    var uptime = "";
    var readUrl="";
    var title = "";    
    var subtitle = "";
    var refNo=0;
    if(pu.getPageCount() == 1){ //如果只有一页，直接把第一页显示出来
      for(var i=0; i<len; i++){ 
        refNo =0;
        fName = rtdata[i].fileName; 
        uptime = rtdata[i].updateTime;
        uptime = uptime.substring(0, uptime.indexOf(" "));
        title = rtdata[i].title;
        if(title.length >12){
          subtitle = title.substring(0,12)+" ...";
        }else{
          subtitle = title;
        }
        var abs = rtdata[i]["abstract"];
        if(abs.length > 75){
          abs = abs.substring(0,75);
        }
        var tName = rtdata[i].filePath;
        var ffName = tName.substring(tName.indexOf(rtdata[i].moduleNo)+39+rtdata[i].moduleNo.length, tName.length);  
        fName = ffName.substring(ffName.indexOf("_")+1, ffName.length); 
        last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
        readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+ encodeURI(fName);
        readUrl += "&attachmentId="+ rtdata[i].fileId +"&moudle="+ rtdata[i].moduleNo +"&op=5&signKey=&print=";       
        temp += "<div class=\"up-left-con-a\">";     
        temp += " <span class=\"h1\"><a href=\"javascript:void(0);\" title=\"点击查看相关文档\" onclick=\"clickFileName('"+ rtdata[i].fileId +"')\">"+ subtitle +"</a></span>";
        temp += "<span ><a href=\"javascript:void(0);\" onclick=\"openWin('"+ readUrl +"')\" style='cursor:pointer;float:right;color:blue;'  align='left'><font size=2>阅读</font></a></span>";
        temp += "<p class=\"con-txt\"><a href=\"javascript:void(0);\">"+ abs +"......</p>";
        temp += getAFileMate(rtdata[i].fileId);        
        temp += "</div>";
      } 
    }else{
      if(currNo < pu.getPageCount()){    //如果当前的页号小于最大的页号，直接把内容输出
        for(var j=(currNo-1)*pu.getPageSize(); j< currNo*pu.getPageSize(); j++){
          refNo = (currNo-1)*pu.getPageSize();
          fName = rtdata[j].fileName;
          //fName = fName.substring(fName.indexOf("_")+1, fName.length);   
          //last = fName.substring(fName.indexOf(".")+1, fName.length);           
          uptime = rtdata[j].updateTime;
          uptime = uptime.substring(0, uptime.indexOf(" "));
          title = rtdata[j].title;
          if(title.length >12){
            subtitle = title.substring(0,12)+" ...";
          }else{
            subtitle = title;
          }
          var abs = rtdata[j]["abstract"];
          if(abs.length > 75){
            abs = abs.substring(0,75);
          }
          var tName = rtdata[j].filePath;          
          var ffName = tName.substring(tName.indexOf(rtdata[j].moduleNo)+33+ (rtdata[j].moduleNo).length, tName.length);  
          fName = ffName.substring(ffName.indexOf("_")+1, ffName.length);   
          last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
          readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
          readUrl += "&attachmentId="+ rtdata[j].fileId +"&moudle="+ rtdata[j].moduleNo +"&op=5&signKey=&print=";
          temp += "<div class=\"up-left-con-a\">";     
          temp += " <span class=\"h1\"><a href=\"javascript:void(0);\" title=\"点击查看相关文档\" onclick=\"clickFileName('"+ rtdata[j].fileId +"')\">"+ subtitle +"</a></span>";
          temp += "<span ><a href=\"javascript:void(0);\" onclick=\"openWin('"+ readUrl +"')\" style='cursor:pointer;float:right;color:blue;'  align='left'><font size=2>阅读</font></a></span>";
          temp += "<p class=\"con-txt\"><a href=\"javascript:void(0);\">"+ abs +"......</p>";         
          temp += getAFileMate(rtdata[j].fileId);        
          temp += "</div>";
          
        }
      }else if (currNo == pu.getPageCount()){           
        for(var k=(currNo-1)*pu.getPageSize(); k<pu.getTotal(); k++){//如果当前的页号等于最大的页号，也就是最后一页，需要把这个页面的开始，到最后的记录输出
          refNo = (currNo-1)*pu.getPageSize();
          fName = rtdata[k].fileName;         
          uptime = rtdata[k].updateTime;
          uptime = uptime.substring(0, uptime.indexOf(" "));
          title = rtdata[k].title;
          if(title.length >12){
            subtitle = title.substring(0,12)+" ...";
          }else{
            subtitle = title;
          }
          var abs = rtdata[k]["abstract"];
          if(abs.length > 75){
            abs = abs.substring(0,75);
          }
          var tName = rtdata[k].filePath;
          var ffName = tName.substring(tName.indexOf(rtdata[k].moduleNo)+39+rtdata[k].moduleNo.length, tName.length);  
          fName = ffName.substring(ffName.indexOf("_")+1, ffName.length); 
          last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
          readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
          readUrl += "&attachmentId="+ rtdata[k].fileId +"&moudle="+ rtdata[k].moduleNo +"&op=5&signKey=&print=";       
          temp += "<div class=\"up-left-con-a\">";     
          temp += " <span class=\"h1\"><a href=\"javascript:void(0);\" title=\"点击查看相关文档\" onclick=\"clickFileName('"+ rtdata[k].fileId +"')\">"+ subtitle +"</a></span>";
          temp += "<span ><a href=\"javascript:void(0);\" onclick=\"openWin('"+ readUrl +"')\" style='cursor:pointer;float:right;color:blue;'  align='left'><font size=2>阅读</font></a></span>";
          temp += "<p class=\"con-txt\"><a href=\"javascript:void(0);\">"+ abs +"......</p>";
          temp += getAFileMate(rtdata[k].fileId); 
          temp += "</div>";
        }
      }
    }     
    if(pu.getPageCount()==0){ 
      $("#contents").html("没有相关文件!"); 
    }else{ 
      clickFileName(rtdata[refNo].fileId);
      $("#contents").html(temp); 
    }
    createPage(currNo, pu);
  }
}




function getAFileMate1(fileId){ 
  var temp = "";
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHAFileMateAct/findAmate.act",
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


function getAFileMate(fileId){ 
  /*var temp = "";
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/titleSignFile.act",
    type:"post",
    data:"attachmentId="+fileId,  
    async:false,     
    success:function(data){
      var obj = eval("(" + data +")"); 
      var vale = obj.rtData;  //
      var name = vale.PersonNames;//人名数组
      var area = vale.AddrNames;
      var orge = vale.OrgNames;  
      var names  ="";
      var areas = "";
      var orges ="";
      if(name){
        names  = array2String(name)
      }
      if(areas){
        areas  = array2String(area);
      }
      if(orge){
        orges  = array2String(orge);
      }
      
      if(names){
        temp += "<p class=\"con-txt3\"><font size=2px; style='text-decoration:none;color:#000099;font-weight: bold'>人名:</font></p>";    
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+names+"</font>&nbsp;</p>";
      }
      if(areas){
        temp += "<p class=\"con-txt3\"><font size=2px; style='text-decoration:none;color:#000099;font-weight: bold'>地名:</font></p>";
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+areas+"</font>&nbsp;</p>";
      }
      if(orges){
        temp += "<p class=\"con-txt3\" ><font size =2px; style='text-decoration:none;color:#000099;font-weight: bold'>组织机构名:</font></p>";
        temp += "<p class=\"con-txt3\"><font size=2px; style='color:#0033FF'>"+orges+"</font>&nbsp;</p>";
      } 
    }    
  });   
  return temp; */
  //return getAFileMate1(fileId);
  return "";
}


function array2String(arr){
   if(arr.length > 0){
      var temp = '';
      var len = arr.length > 2? 2: arr.length;
      for(var i=0; i<len; i++){
        temp += arr[i]+" ";
      }
      return temp;
   }
   return "";
}

