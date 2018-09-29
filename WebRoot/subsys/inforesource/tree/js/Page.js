
/**
 * 分页类
 * @param total 总的记录数
 * @param currNo 当前页数
 * @return
 */
function Pager(total, currNo){
  this.total = total;
  this.currNo = currNo;
  this.pageSize = 8; //每个页面显示10条数据
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
  $("#page-a").show();
  $("#pageNumber").empty();
  var totalNo = pu.getPageCount();
  var pageNo="";
  if(currNo > 1){
    //pageNo += "<a>首页</a>&nbsp;&nbsp;";<li><a href="#">1</a></li>
    //pageNo += "<a>上一页</a>";
    pageNo += "<li><a href=\"javascript:void(0);\" onclick=\"pagerNo("+ (currNo-1) +")\" onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image2','','images/page-a-click.gif',1)\"><img src=\"images/page-a.gif\" name=\"Image2\" width=\"18\" height=\"18\" border=\"0\" id=\"Image2\" /></a></li>";
  }
  if(currNo - 4 >0){
    for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数    
     pageNo += "<li><a href='javascript:void(0)' onclick='pagerNo("+ no +")'>"+ no + "</a></li>";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<li><a href='javascript:void(0)' onclick='pagerNo("+ no +")'>"+ no + "</a></li>";
    }
  }  
  if(totalNo >1){
   pageNo += "<li><a href='javascript:void(0)'>["+ currNo + "]</a></li>";
  }
  if(currNo+5 < totalNo){
    for(var no2 = currNo +1; no2< currNo+5; no2++){
     pageNo += "<li><a href='javascript:void(0)'  onclick='pagerNo("+ no2 +")'>"+ no2 + "</a></li>";
    }
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<li><a href='javascript:void(0)' onclick='pagerNo("+ no2 +")' >"+ no2 + "</a></li>";
    }
  }
  if(currNo < totalNo){
    pageNo += " <li><a href=\"javascript:void(0)\" onclick=\"pagerNo("+ (currNo +1)+")\" onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image3','','images/page-b-click.gif',1)\"><img src=\"images/page-b.gif\" name=\"Image3\" width=\"18\" height=\"18\" border=\"0\" id=\"Image3\" /></a></li> ";
   // pageNo += "<li><a href='javascript:void(0)'>末页</a></li>";
  } 
  $("#pageNumber").append(pageNo);
}

/**
 * 
 * 点击页码时候执行的方法
 * @param currNo 当前号码
 * @param pu     分页类
 * @return
 */
function pagerNo(currNo){ 
  //var readUrl = http://192.168.0.159/yh/core/funcs/office/ntko/indexNtko.jsp?attachmentName=aa.docx&attachmentId=1007_1158741a7d9be5094a405dd973cb3ce1&moudle=notify&op=5&signKey=&print=
  var data = this.getData();
  var pu = this.getPage(); 
  if(data){  
    $(".hiddiv").attr("id","tab-a-con");
    $("#dataa").empty();
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
    if(pu.getPageCount() == 1){ //如果只有一页，直接把第一页显示出来
      for(var i=0; i<len; i++){ 
        fName = rtdata[i].fileName;       
        //fName = fName.substring(fName.indexOf("_")+1, fName.length);    
        //last  = fName.substring(fName.indexOf(".")+1, fName.length); 
        uptime = rtdata[i].updateTime;
        uptime = uptime.substring(0, uptime.indexOf(" "));
        title = rtdata[i].title;
        if(title.length >12){
          subtitle = title.substring(0,12)+" ...";
        }else{
          subtitle = title;
        }
        
        var tName = rtdata[j].filePath;
        var ffName = tName.substring(tName.indexOf(rtdata[i].moduleNo)+39+rtdata[i].moduleNo.length, tName.length);  
        fName = ffName.substring(ffName.indexOf("_")+1, ffName.length); 
        last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
        readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+ encodeURI(fName);
        readUrl += "&attachmentId="+ rtdata[i].fileId +"&moudle="+ rtdata[i].moduleNo +"&op=5&signKey=&print=";       
        temp += "<li>";
        //temp += "<input type=\"checkbox\" class=\"con-input\" value=\""+ rtdata[i].seqId +"\" />";
        temp += "<span class='fileTitle'><a title=\""+ title+"\" href=\"javascript:void(0);\" onclick=\"clickFileName('"+ rtdata[i].fileId +"')\">"+ subtitle+"</a></span>";
        temp += "<h1>"+ Math.ceil(rtdata[i].file_Sizes/1024) +"kb</h1>";
        temp += "<h2>"+ last +"</h2>";
        temp += "<h3>"+ uptime +"</h3>";
        temp += "<h4>系统部</h4>";
        temp += "<h5 onclick=openWin(\'"+ readUrl +"\') style='cursor:pointer;'>阅读</h5>";
        temp += "</li>";       
      } 
    }else{
      if(currNo < pu.getPageCount()){    //如果当前的页号小于最大的页号，直接把内容输出
        for(var j=(currNo-1)*pu.getPageSize(); j< currNo*pu.getPageSize(); j++){
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
          var tName = rtdata[j].filePath;          
          var ffName = tName.substring(tName.indexOf(rtdata[j].moduleNo)+33+ (rtdata[j].moduleNo).length, tName.length);  
          fName = ffName.substring(ffName.indexOf("_")+1, ffName.length);   
          last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
          readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
          readUrl += "&attachmentId="+ rtdata[j].fileId +"&moudle="+ rtdata[j].moduleNo +"&op=5&signKey=&print=";
          temp += "<li>";
          //temp += "<input type=\"checkbox\" class=\"con-input\" value=\""+ rtdata[j].seqId +"\" />";
          temp += "<span class='fileTitle'><a title=\""+ title+"\"href=\"javascript:void(0);\" onclick=\"clickFileName('"+ rtdata[j].fileId +"')\">"+ subtitle+"</a></span>";
          temp += "<h1>"+ Math.ceil(rtdata[j].file_Sizes/1024) +"kb</h1>";
          temp += "<h2>"+ last +"</h2>";
          temp += "<h3>"+ uptime +"</h3>";
          temp += "<h4>系统部</h4>";
          temp += "<h5 onclick=openWin(\'"+ readUrl +"\') style='cursor:pointer;'>阅读</h5>";
          temp += "</li>";  
        }
      }else if (currNo == pu.getPageCount()){           
        for(var k=(currNo-1)*pu.getPageSize(); k<pu.getTotal(); k++){//如果当前的页号等于最大的页号，也就是最后一页，需要把这个页面的开始，到最后的记录输出
          fName = rtdata[k].fileName;
          //fName = fName.substring(fName.indexOf("_")+1, fName.length);    
         // last = fName.substring(fName.indexOf(".")+1, fName.length);   
          uptime = rtdata[k].updateTime;
          uptime = uptime.substring(0, uptime.indexOf(" "));
          title = rtdata[k].title;
          if(title.length >12){
            subtitle = title.substring(0,12)+" ...";
          }else{
            subtitle = title;
          }
          var tName = rtdata[k].filePath;
          var ffName = tName.substring(tName.indexOf(rtdata[k].moduleNo)+39+rtdata[k].moduleNo.length, tName.length);  
          fName = ffName.substring(ffName.indexOf("_")+1, ffName.length); 
          last = ffName.substring(ffName.indexOf(".")+1, ffName.length); 
          readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
          readUrl += "&attachmentId="+ rtdata[k].fileId +"&moudle="+ rtdata[k].moduleNo +"&op=5&signKey=&print=";
          temp += "<li>";
          //temp += "<input type=\"checkbox\" class=\"con-input\" value=\""+ rtdata[k].seqId +"\" />";
          temp += "<span class='fileTitle'><a title=\""+ title+"\" href=\"javascript:void(0);\" onclick=\"clickFileName('"+ rtdata[k].fileId +"')\">"+ subtitle+"</a></span>";
          temp += "<h1>"+ Math.ceil(rtdata[k].file_Sizes/1024) +"kb</h1>";
          temp += "<h2>"+ last +"</h2>";
          temp += "<h3>"+ uptime +"</h3>";
          temp += "<h4>系统部</h4>";
          temp += "<h5 onclick=openWin(\'"+ readUrl +"\') style='cursor:pointer;'>阅读</h5>";
          temp += "</li>";  
        }
      }
    }     
    if(pu.getPageCount()==0){
      $("#dataa").append("没有相关文件!"); 
    }else{ 
     $("#dataa").append(temp); 
    }
    createPage(currNo, pu);
  }
}


function openWin(url){ 
  $("#tab-a-con h5").bind("click",function(){ //绑定div id=tab-a-con 下的h5
    $("#tab-a-con a").each(function(){ //循环本次点击 div id=tab-a-con 下的a标签
    var c = $(this).css("color"); //定义当前颜色
    if(c == "red"){ 
      $(this).css("color","");
    }
  });   
    $(this).parent().find("a").css("color","red");  //a标签显示红色
 });  
  window.open(url, "window","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=10,top=10,resizable=yes");
}



