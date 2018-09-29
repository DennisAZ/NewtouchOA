
/**
 * 开始建立页码（备用）
 * @param totalNo 总的页数
 * @param currNo  当前的页码
 * @return
 */
function createPageNo(totalNo, currNo, divInner, flag){  
  var pageNo="";
  $("#"+divInner).empty();
  if(currNo > 1){
   // pageNo += "<a href=javascript:gotoPage('10',1,"+ flag +");>首页</a>&nbsp;&nbsp;";    
    pageNo += "<li><a href=javascript:gotoPage('8',"+(currNo-1)+",'"+ flag +"') onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image2','','images/page-a-click.gif',1)\"><img src=\"images/page-a.gif\" name=\"Image2\" width=\"18\" height=\"18\" border=\"0\" id=\"Image2\" /></a></li>";
  }
  if(currNo - 4 >0){
    for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
     pageNo += "<li><a href=javascript:gotoPage('8',"+no+",'"+ flag +"');>"+ no + "</a>"+"</li>&nbsp;";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<li><a href=javascript:gotoPage('8',"+no+",'"+ flag +"');>"+ no + "</a>"+"</li>&nbsp;";
    }
  }
  if(totalNo >1){
   pageNo += "<li><a>"+"["+ currNo +"]"+ "</a></li>&nbsp;";
  }
  if(currNo+5 < totalNo){
    for(var no2 = currNo +1; no2< currNo+5; no2++){
     pageNo += "<li><a href=javascript:gotoPage('8',"+no2+",'"+ flag +"');>"+ no2 + "</a></li>&nbsp;";
    }
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<li><a href=javascript:gotoPage('8',"+no2+",'"+ flag +"');>"+ no2 + "</a></li>&nbsp;";
    }
  }
  if(currNo < totalNo){
    pageNo += "<li><a href=javascript:gotoPage('8',"+(currNo+1)+",'"+ flag +"'); onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image3','','images/page-b-click.gif',1)\"><img src=\"images/page-b.gif\" name=\"Image3\" width=\"18\" height=\"18\" border=\"0\" id=\"Image3\" /></a></li>";
   // pageNo += "<a href=javascript:gotoPage('',"+ totalNo + ");>末页</a>";
  }
  $("#"+divInner).append(pageNo);
}

/**
 * 显示分页的文件列表
 * @param pageSize 页面大小  
 * @param currNo 当前页号
 * @return
 */
function gotoPage(pageSize, currNo, flag){  
  if(flag == 'tag'){               //如果是通过tag图查找标签则调用
    var keyIds = window.parent.getKeyIds();
    articleListByKeyIDs(keyIds,currNo);//todo需要把currNo传进去，但是接口没有提供
  }else{
    var fileId = this.getFileId();
    getRelationArticleTags(fileId);//todo需要把currNo传进去，但是接口没有提供
  }
}

/**
 * 开始建立页码（备用）
 * @param totalNo 总的页数
 * @param currNo  当前的页码
 * @return
 */
function createPageNo2(totalNo, currNo, divInner){  
  var pageNo="";
  $("#"+divInner).empty();
  if(currNo > 1){//当前页大于1 就显示当前页-1 的 6行数据
   // pageNo += "<a href=javascript:gotoPage('10',1,"+ flag +");>首页</a>&nbsp;&nbsp;";    
    pageNo += "<li><a href=javascript:gotoPage2('6',"+(currNo-1)+") onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image2','','images/page-a-click.gif',1)\"><img src=\"images/page-a.gif\" name=\"Image2\" width=\"18\" height=\"18\" border=\"0\" id=\"Image2\" /></a></li>";
  }
  if(currNo - 4 >0){// 显示4个页数
    for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
     pageNo += "<li><a href=javascript:gotoPage2('6',"+no+");>"+ no + "</a>"+"</li>&nbsp;";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<li><a href=javascript:gotoPage2('6',"+no+");>"+ no + "</a>"+"</li>&nbsp;";
    }
  }///////以上是当前页 以上的4个页///////////////
  if(totalNo >1){//总页数大于1，就把当前页用【】括起来
   pageNo += "<li><a>"+"["+ currNo +"]"+ "</a></li>&nbsp;";
  }
  //////////以下是当前页 以下的4个页//////
  if(currNo+5 < totalNo){// 显示4个页面
    for(var no2 = currNo +1; no2< currNo+5; no2++){
     pageNo += "<li><a href=javascript:gotoPage2('6',"+no2+");>"+ no2 + "</a></li>&nbsp;";
    }
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<li><a href=javascript:gotoPage2('6',"+no2+");>"+ no2 + "</a></li>&nbsp;";
    }
  }
  if(currNo < totalNo){// 只要当前页小于 总页 就可以显示按钮
    pageNo += "<li><a href=javascript:gotoPage2('6',"+(currNo+1)+"); onmouseout=\"MM_swapImgRestore()\" onmouseover=\"MM_swapImage('Image3','','images/page-b-click.gif',1)\"><img src=\"images/page-b.gif\" name=\"Image3\" width=\"18\" height=\"18\" border=\"0\" id=\"Image3\" /></a></li>";
   // pageNo += "<a href=javascript:gotoPage('',"+ totalNo + ");>末页</a>";
  }  
  $("#"+divInner).append(pageNo);
}

function gotoPage2(pageSize, currNo){
  Search(currNo, pageSize); 
}
