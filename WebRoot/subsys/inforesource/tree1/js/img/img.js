/**
 * 查询图片
 * @param currNo
 * @return
 */
function doSeachAllImage(currNo){  
  $(".next-page").html("");  
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHManageImgAct/findAllImages.act",
    type:"post",             
    data:"currNo="+ currNo,   
    success:function(data){   
     var jsonObj = eval( "(" + data + ")" ); 
      onloadData(jsonObj);
      createPageNo( jsonObj.rtData.totalNo, jsonObj.rtData.currNo, jsonObj.rtData.pageSize);      
    }
  });
}
/**
 * 加载数据
 * @param obj
 * @return
 */
function onloadData(obj){
  var imgObj = obj.rtData.data;
  var len = imgObj.length; 
  $("#imgContainer").html(image);
  var temp ="";
  for(var i=0; i<len; i++){
    temp += "<div class=\"up-left-con-img\">";
    temp += "<div class=\"con-box\">";
    temp += "<span class=\"h1\"><a href=\"javascript:void(0);\" onclick=\"openImageWindow("+ imgObj[i].newsId+")\">"+ imgObj[i].subject.replace(new RegExp("</{0,1}[a-z,A-Z]>"),"").replace(new RegExp("</{0,1}[a-z,A-Z]>"),"")+"</a></span>";
    temp +="</div>";
    temp +="<div class=\"con-img-box\">";
    temp +="<p class=\"con-img\">";
    var imgLen = imgObj[i].imgPaths.length;
    if(imgLen > 0){
      for(var j=0; j<imgLen; j++){       
        temp+="<img src=\""+ imgObj[i].imgPaths[j]+"\" />";
      }
    }else{
      temp += "<img src='"+ contextPath + "/subsys/inforesource/tree1/images/tongyong.gif" +"'>";
    }
    temp += "</p>";
    temp += "<p class=\"con-txt-img\"><a href=\"javascript:void(0);\">";
    temp += substr(imgObj[i].description.replace(new RegExp("</{0,1}[a-z,A-Z]>"),"").replace(new RegExp("</{0,1}[a-z,A-Z]>"),"")) ;
    temp += "</a></p>";
    temp += "</div><br>";
    
    //////////////////////////////////一下为,人名，地名，主题词//////////////////////////////
    temp += returnAmate(imgObj[i].newsId);
    temp += "</div>";
  }
  $("#imgContainer").html(temp);
}

/**
 * 打开定义窗口
 * @return
 */
function tomanage(){ 
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act?ftype=2";
  myleft=(screen.availWidth-500)/2;
  window.open(url,"_blank","height=800,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); 
}

function filtag(){//填写 自定义标签
  var url = contextPath + "/subsys/inforesource/tagname.jsp?nodeType=2";   
  window.open(url, "window","height=200,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=220,top=180,resizable=no");
}
function freshContent(){  
 
}
function findTypeMenu(){
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHMateTreeAct/findMymoth.act?typemenu=2",
    type:"post",
    success:function(data){   
      var obj = eval("(" + data +")");
      window.trData = obj;
      onload(obj);
    }
  });
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



function substr(str){ 
  if(str.length > 120){
    str = str.substring(0, 120) + "...";
  }
  return str;
}


function returnAmate(newsId){ 
  var temp = "";
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHManageImgAct/findAmate.act",
    type:"post",
    data:"newSeqId="+newsId,  
    async:false,     
    success:function(data){
      var obj = eval("(" + data +")"); 
      var vale = obj.rtData;  //
      var name = vale.name;//人名数组
      var area = vale.area;
      var orge = vale.orge;  
      var names  = (name);
      var areas  = (area);
      var orges  = (orge);
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
  return temp; 
}
/**
 * 临时使用
 * @return
 */
function doTemp(){
  $("#imgContainer").html(image);
  $(".next-page").html("");
  doSeachAllImage(1);
}