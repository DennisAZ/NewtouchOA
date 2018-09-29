//加载页面文字
function initStatPage(){
	var typTxt=$("typTxt").value;
	$("typTxt_1").innerHTML=typTxt;
	if($("typTxt_2")!=null){
		$("typTxt_2").innerHTML=typTxt;
	}
}

//头部标签页跳转
function topForward(type){
	switch(type){
	  	case 1:
		   self.location.href="statAction.do?op=toCusStat&statType=cusType";
		   break;
		case 2:
		   self.location.href="statAction.do?op=toSalStat&statType=salM";
		   break;
/*	   case 3:
		   self.location.href="statAction.do?op=statContact&statType=conLev";
		   break;
	   case 4:
		   self.location.href="cusServAction.do?op=statSalOpp&statType=oppStage";
		   break;
	   case 5:
		   self.location.href="cusServAction.do?op=statSalPra&statType=praType";
		   break;*/
	   
	}
}

/*
	生成表格
	- url      : a跳转连接
	- value    : 表格文本内容
	- key      : 表格map的key（列数）
	- colCount : 总列数
	- rowCount : 总行数
	- curRow   : 当前行
	- isMonType: 是否格式化成货币格式
*/
function createCell(url,value,key,colCount,rowCount,curRow,isMonType){
	if(isMonType&&!isNaN(parseFloat(value))){
		value = "￥"+changeTwoDecimal(value);
	}
	if(rowCount == (curRow+1) ){
		document.write("<td class='lightBlueBg bold'>&nbsp;"+value+"</td>");
	}
	else if(key==2||key==colCount){
		document.write("<td>&nbsp;"+value+"</td>");
	}
	else if(parseFloat(value)==0){
		document.write("<td class='gray'>&nbsp;0</td>");
	}
	else{
		document.write("<td>&nbsp;<a href='javascript:void(0)' onClick=\"window.open('"+ url +"');return false;\" target='_blank' title='"+value+"'>"+value+"</a></td>");
	}
}

/*
	得到与表格头部对应值
*/
function getTabTop(tabId,key){
	var topRowArray = $(tabId).rows[0].innerHTML.toLowerCase().split('</th>');
	return topRowArray[parseInt(key)-2].split('&nbsp;')[1];//顶部对应元素
}
/*
	得到与表格左侧对应值(隐藏域值)
*/
function getTabLeft(curRow){
	if($('left'+curRow.toString())!=null){
		return $('left'+curRow.toString()).value;
	}
	else{
		return null;
	}

}
//求百分比
function getPercent(curValue,total)
{
	var perCent=((parseInt(curValue)*100)/parseInt(total)).toFixed(1);//截取小数点后一位
	document.write("<span class='textOverflow3' title='"+perCent+"'>"+perCent+"%&nbsp;</span>");
}