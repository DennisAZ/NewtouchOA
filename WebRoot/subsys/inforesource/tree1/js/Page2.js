


/**
 * 开始建立页码（备用）
 * @param totalNo 总的页数
 * @param currNo  当前的页码
 * @return
 */
function createPageNo(totalNo, currNo, divInner, flag){  
  var pageNo="";
  $("."+divInner).empty();
  var constNo = 6;
  pageNo +="<table width=\"\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
  pageNo += "<tr>";
  if(currNo > 1){
     pageNo += "<td  width=\"60\"><a  href=javascript:gotoPage('9',"+(currNo-1)+",'"+ flag +"')>《&nbsp;上一页</a></td>";
  }
  if(currNo - constNo >0){
    for(var no = currNo - constNo; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('9',"+no+",'"+ flag +"');>"+ no + "</a>"+"</td>";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('9',"+no+",'"+ flag +"');>"+ no + "</a>"+"</td>";
    }
  }
  if(totalNo >1){
   pageNo += "<td width=\"35\" class=\"page-td\"><a>"+"["+ currNo +"]"+ "</a></td>";
  }
  if(currNo+constNo < totalNo){
    for(var no2 = currNo +1; no2< currNo+constNo; no2++){
     //pageNo += "<td class=\"page-td\"><a onclick = gotoPage('8',"+no2+",'"+ flag +"');>"+ no2 + "</a></td>&nbsp;";
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=\"javascript:void(0)\" onclick = gotoPage('9',"+no2+",'"+ flag +"');>"+ no2 +"</a></td>";
    } 
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('9',"+no2+",'"+ flag +"');>"+ no2 + "</a></td>";
    }
  }
  if(currNo < totalNo){
    pageNo += "<td width=\"60\"><a href='javascript:void(0);' onclick=gotoPage('9',"+(currNo+1)+",'"+ flag +"')>下一页》</a></td>";
   // pageNo += "<a href=javascript:gotoPage('',"+ totalNo + ");>末页</a>";
  }
  pageNo +="</tr>";
  pageNo +="</table>";
  $("."+divInner).html(pageNo);
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
  $(divInner).html("");
  
  pageNo +="<table width=\"\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";  
  pageNo += "<tr>";
  if(currNo > 1){
     pageNo += "<td  width=\"60\" ><a  href=javascript:gotoPage2('"+ defaultPageSize +"',"+(currNo-1)+")>《&nbsp;上一页</a></td>";
  }
  if(currNo - 4 >0){
    for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage2('"+ defaultPageSize +"',"+no+");>"+ no + "</a>"+"</td>";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage2('"+ defaultPageSize +"',"+no+");>"+ no + "</a>"+"</td>";
    }
  }
 
  if(totalNo >1){
   pageNo += "<td width=\"35\" class=\"page-td\"><a>"+"["+ currNo +"]"+ "</a></td>";
  }
  if(currNo+5 < totalNo){
    for(var no2 = currNo +1; no2< currNo+5; no2++){
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=\"javascript:void(0)\" onclick = gotoPage2('"+ defaultPageSize +"',"+no2+");>"+ no2 +"</a></td>";
    } 
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=javascript:gotoPage2('"+ defaultPageSize +"',"+no2+");>"+ no2 + "</a></td>";
    }
  } 
  if(currNo < totalNo){
    pageNo += "<td width=\"60\" ><a href='javascript:void(0);' onclick=gotoPage2('"+ defaultPageSize +"',"+(currNo+1)+")>下一页》</a></td>";
  }
  pageNo +="</tr>";
  pageNo +="</table>"; 
  $(divInner).html(pageNo);
}

function gotoPage2(pageSize, currNo){  
  Search(currNo, pageSize); 
}
