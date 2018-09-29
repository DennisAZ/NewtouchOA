/**
 * 开始建立页码
 * @param totalNo 总的页数
 * @param currNo  当前的页码
 * @return
 */
function createPageNo(totalNo, currNo, defaultPageSize){   
  var pageNo=""; 
  $(".next-page").html("");  
  pageNo +="<table width=\"\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";  
  pageNo += "<tr>";
  if(currNo > 1){
     pageNo += "<td  width=\"60\" ><a  href=javascript:gotoPage('"+ defaultPageSize +"',"+(currNo-1)+")>《&nbsp;上一页</a></td>";
  }
  if(currNo - 4 >0){
    for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('"+ defaultPageSize +"',"+no+");>"+ no + "</a>"+"</td>";
    }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
     pageNo += "<td  width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('"+ defaultPageSize +"',"+no+");>"+ no + "</a>"+"</td>";
    }
  }
 
  if(totalNo >1){
   pageNo += "<td width=\"35\" class=\"page-td\"><a>"+"["+ currNo +"]"+ "</a></td>";
  }
  if(currNo+5 < totalNo){
    for(var no2 = currNo +1; no2< currNo+5; no2++){
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=\"javascript:void(0)\" onclick = gotoPage('"+ defaultPageSize +"',"+no2+");>"+ no2 +"</a></td>";
    } 
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
     pageNo += "<td width=\"35\" class=\"page-td\"><a href=javascript:gotoPage('"+ defaultPageSize +"',"+no2+");>"+ no2 + "</a></td>";
    }
  } 
  if(currNo < totalNo){
    pageNo += "<td width=\"60\" ><a href='javascript:void(0);' onclick=gotoPage('"+ defaultPageSize +"',"+(currNo+1)+")>下一页》</a></td>";
  }
  pageNo +="</tr>";
  pageNo +="</table>"; 
  $(".next-page").html(pageNo);
}

/**
 * 点击页码
 * @param defaultPageSize  每页显示多少条
 * @param currNo           传入的页码
 * @return
 */
function gotoPage(pageSize, currNo){  
  $("#imgContainer").html(image);
  doSeachAllImage(currNo);
}

