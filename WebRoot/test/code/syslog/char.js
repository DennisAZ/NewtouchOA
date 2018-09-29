
/*************************************************************************
                              chart.js                             
version:1.0
Date:2004/11/5
Author: Gump Lau 
E_mail:lzg_010@163.com

**************************************************************************/


 /**
  条形统计图容器 
  width：统计图宽度（>=100）；
  height：统计图高度（>=450）；
  left：统计图左边距；
  top：统计图上边距；
  title：统计图标题（默认值：统计图）；
  unit：统计图单位（默认值：个）；
  bgcolor：统计图背景颜色（默认值：#CCFFCC）；
  divID：统计图层ID（默认值：div_chart）
 */
function chart(width,height,left,top,title,unit,bgcolor,divID){
 this.width=(width<100||width>2000)?100:width;
 this.height=(height<450||height>2000)?450:height;
 this.left=left?((left<0||left>800)?0:left):0;
 this.top=top?((top<0||top>800)?0:top):0;
 this.title=title?title:"统计图";
 this.unit=unit?unit:"个";
 this.bgcolor=bgcolor?bgcolor:"#CCFFCC";
 this.divID=divID?divID:"div_chart";
 this.columns=new Array();
 this.columnCount=0;
 this.gradLeft=0;
 this.gradTop=this.top+20;
 this.gradHeight=this.height-120;
 this.gradInterval=(this.gradHeight)/11;
 this.maxValue=0;
 this.colWidth=25;
 this.colGap=this.colWidth;
}


 /**
  给条形统计图加一条形，然后计算相应的值
 */
function chart.prototype.addColumn(size,footnote,color){
 var columnObj= new column(size,footnote,color);
if(columnObj.size>this.maxValue){
   this.maxValue=getBetterMaxValue(columnObj.size);
 }
 this.columns[this.columnCount++]=columnObj;
 for(var i=0;i<this.columnCount;i++){
  this.columns[i].height=((this.gradHeight)/11)*10*this.columns[i].size/this.maxValue;
 }
   this.gradLeft=(this.width-(this.colWidth+this.colGap)*this.columnCount)/2;
}


/**
  设置条形统计图中条形的宽度
 */
function chart.prototype.setColWidth(colWidth){
   this.colWidth=colWidth;
}

/**
  设置条形统计图中条形间的间隔
 */
function chart.prototype.setColGap(colGap){
   this.colGap=colGap;
}

/**
  显示条形统计图
 */
function chart.prototype.show(){
 if(document.getElementById(this.divID)) document.getElementById(this.divID).removeNode(true);
 var today=new Date();
 var strToday=today.getFullYear()+"年"+(today.getMonth()+1)+"月"+today.getDate()+"日";
 var strDivHTML="";
  strDivHTML="<DIV id='"+this.divID+"' style='POSITION:absolute;";
  strDivHTML+= "TOP:"+this.top+";LEFT:"+this.left+";width:"+this.width+";height:"+this.height;
  strDivHTML+=";BACKGROUND-COLOR:"+this.bgcolor+";BORDER-TOP:3px RIDGE YELLOW;BORDER-RIGHT:3px RIDGE YELLOW;";
  strDivHTML+="BORDER-BOTTOM:3px RIDGE YELLOW;BORDER-LEFT:3px RIDGE YELLOW;Z-INDEX:0'>";
  strDivHTML+="<table align=center></tr><td><strong>"+this.title+"</strong></td></tr></table>" ;
  strDivHTML+="<table width=100%></tr><td style='FONT-SIZE: 12px;FONT-WEIGHT: bolder'  align=left>单位:"+this.unit+"</td>";
  strDivHTML+="<td style='FONT-SIZE: 12px;'  align=right>统计日期:"+strToday+"</td></tr></table></DIV>" ;
  document.body.insertAdjacentHTML("BeforeEnd",strDivHTML)  ;
 showGrad(this);
 showColumns(this);
}


 /**
  条形统计图中的条形对象 
  size：条形的值（数字型）；
  footnote：条形的脚注（字符串）；
  color：条形的颜色（字符串）
 */
function column(size,footnote,color){
 this.size=size;
 this.footnote=footnote;
 this.color=color;
 this.height=0;
}


 /**
  显示条形统计图的标有刻度的坐标
 */
function showGrad(chartObj){
 var left=chartObj.gradLeft;
 var top=chartObj.gradTop;
 var height=(chartObj.gradHeight)/11;
 var color="red";
 var maxValue=chartObj.maxValue;
 var arrowLeft=left+maxValue.toString().length*6;
 var lineHeight=2;
 var strHTML="<div  style='POSITION:absolute;left:"+(arrowLeft-4)+";top:"+(top-14)+";color:"+color+";z-index:12'> ";//纵坐标箭头
     strHTML+="<span lang=EN-US style='font-size:18pt;font-family:VisualUI'>G</span></div>";
   
    strHTML+="<table   style='POSITION:absolute;left:"+left+";top:"+top+";BORDER-RIGHT: "+color+" 4px solid;FONT-SIZE: 12px;z-index:11'>";
 for(var i=0;i<11;i++){
  strHTML+="<tr><td id='td_grad_"+(i+1).toString()+"'";
  strHTML+="style='HEIGHT:"+height+"; BORDER-BOTTOM: "+color+" 1px solid; TEXT-ALIGN:right;vertical-align:bottom;'>";
  strHTML+=(maxValue/10*(10-i))+"</td></tr>" ;
 }
    strHTML+="</table>";
    var hLineTop=top+height*11+lineHeight*10;
    var hLineLeft=arrowLeft+7;
    var hLineWidth=(chartObj.colWidth+chartObj.colGap)*(chartObj.columnCount+1);
  strHTML+="<table style='POSITION:absolute;left:"+hLineLeft+";top:"+hLineTop+";width:"+hLineWidth+";BORDER-TOP: "+color+" 4px solid;'><tr><td></td></tr></table>";
  strHTML+="<div  style='POSITION:absolute;left:"+(hLineLeft+hLineWidth-10)+";top:"+(hLineTop-10)+";color:"+color+";z-index:12'>";//横坐标箭头
  strHTML+="<span lang=EN-US style='font-size:18pt;font-family:VisualUI'>E</span></div>";
  document.getElementById(chartObj.divID).insertAdjacentHTML("BeforeEnd",strHTML)  ;
}

 

 /**
  显示条形统计图的所有条形
 */
function showColumns(chartObj){
 var arrowLeft=chartObj.gradLeft+chartObj.maxValue.toString().length*6;
 var left=arrowLeft+4+chartObj.colGap;
 var colWidth=chartObj.colWidth;
 var gap=chartObj.colGap;
 var lineHeight=2;//刻度线的高度
 var strHTML="";
 for(var i=0;i<chartObj.columnCount;i++){
  var colObj=chartObj.columns[i];
  var height=colObj.height+parseInt(colObj.size/(chartObj.maxValue/11))*lineHeight;
  var colTop=chartObj.gradTop+chartObj.gradHeight+lineHeight*10-height ;
  height=height>1?height:1;

  strHTML+="<table><tr><td style='position:absolute;top:"+(colTop-15)+";left:"+(left+(colWidth+gap)*i)+";width:"+colWidth+";FONT-SIZE: 12px;word-wrap:break-word;z-index:1000' align='center'>"+colObj.size.toString()+"</td></tr></table>";
         
  strHTML+="<table><tr><td style='position:absolute;top:"+colTop+";left:"+(left+(colWidth+gap)*i)+";height:"+height+";width:"+colWidth+";background-color:"+colObj.color+"'></td></tr></table>";
   
  strHTML+="<table><tr><td style='position:absolute;top:"+(colTop+height+5)+";left:"+(left+(colWidth+gap)*i)+";width:"+colWidth+";FONT-SIZE: 12px;word-wrap:break-word;z-index:1000' align='center'>"+colObj.footnote+"</td></tr></table>";
 }
 
   document.getElementById(chartObj.divID).insertAdjacentHTML("BeforeEnd",strHTML)  ;
 
}

 

 /**
  获得一个能被10整除的较佳最大数
 */
function getBetterMaxValue(pMaxValue){
 for(var i=10;i>1;i--) {
 if(pMaxValue>=Math.pow(10,i))return parseInt(parseFloat(pMaxValue)/Math.pow(10,i)+1)*Math.pow(10,i);
 }
 return parseInt(parseFloat(pMaxValue)/10+1)*10;
}


function document.ondragstart(){
   return false;
}

 function document.onselectstart() {
  return false;
 }

function document.oncontextmenu(){
 return false;
}


