function MyCalendar(evt,popCtrl,isHaveTime,splitCharPara){
var gMonths=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
var WeekDay=new Array("日","一","二","三","四","五","六");
var strToday="今天";
var strYear="年";
var strMonth="月";
var strDay="日";
var splitChar="-";
if(splitCharPara != null){
 splitChar=splitCharPara;
}

var startYear=2000;
var endYear=2050;
var dayTdHeight=12;
var dayTdTextSize=12;
var gcNotCurMonth="#E0E0E0";
var gcRestDay="#FF0000";
var gcWorkDay="#444444";
var gcMouseOver="#79D0FF";
var gcMouseOut="#F4F4F4";
var gcToday="#444444";
var gcTodayMouseOver="#6699FF";
var gcTodayMouseOut="#79D0FF";
var gdCtrl=new Object();
var goSelectTag=new Array();
var gdCurDate=new Date();
var giYear=gdCurDate.getFullYear();
var giMonth=gdCurDate.getMonth()+1;
var giDay=gdCurDate.getDate();
var giHour = gdCurDate.getHours();
var giMinutes = gdCurDate.getMinutes();
var giSeconds=gdCurDate.getSeconds();
var dateCtrl = popCtrl;
fPopCalendar(evt,popCtrl,dateCtrl,isHaveTime);
function $C(tag){
	return document.createElement(tag);
}

function createDiv(){
	var div = $C('div');
	
	with(div){
	setAttribute('id','calendardiv');
	style.position = "absolute";
	style.index = '99';
	style.visibility = "hidden";
	style.border = '1px solid #999999';
	// Event.observe("calendardiv", "onclick", stopMaoPao, false);
	div.onclick = function(event){
		if (window.event) {
			window.event.cancelBubble=true;
		} else {
			event.stopPropagation();
		} 
		
	};
	
	if(document.all){
		div.onselectstart = function(){
			return false;
		};
	}else{
		div.style.MozUserSelect = 'none';
    }
	}
    var table = $C('table');
    with(table){
    	border=0;
    	bgcolor='#E0E0E0';
    	cellpadding=1;
    	cellspacing=1;
    }
    var tbody = $C('tbody');
    
    //造第一行数据
    var tr = $C('tr');
    var td = $C('td');
    //向后
    var prevMonthInput = $C('input');
    with(prevMonthInput){
    	type = 'button';
    	id = 'PrevMonth';
    	value = '<';
    	style.height = '20px';
    	style.width = '20px';
    	style.fontWeight = 'bolder';
    	onclick = function(){
    		fPrevMonth();
    	};
    }
    td.appendChild(prevMonthInput);
    tr.appendChild(td);
    //年select
    td = $C('td');
    var select = $C('select');
    with(select){
    	id = 'tbSelYear';
    	style.border = '1px solid';
    	onchange = function(){
    		fUpdateCal($V("tbSelYear"),$V("tbSelMonth"));
    	};
    }
    for(var i=startYear;i<endYear;i++){
	    var option = $C('option');
	    option.value = i;
	    option.appendChild(document.createTextNode(i+strYear));
	    select.appendChild(option);
     }
    td.appendChild(select);
    tr.appendChild(td);
    //月select
    td = $C('td');
    select = $C('select');
    with(select){
    	id = 'tbSelMonth';
    	style.border = '1px solid';
    	onchange = function(){
    		fUpdateCal($V("tbSelYear"),$V("tbSelMonth"));
    	}
    }
    for(var i=0;i<12;i++){
	    var option = $C('option');
	    option.value = parseInt(i)+1;
	    option.appendChild(document.createTextNode(gMonths[i]));
	    select.appendChild(option);
     }
    td.appendChild(select);
    tr.appendChild(td);
    //向后
    td = $C('td');
    var nextMonthInput = $C('input');
    with(nextMonthInput){
    	type = 'button';
    	id = 'PrevMonth';
    	value = '>';
    	style.height = '20px';
    	style.width = '20px';
    	style.fontWeight = 'bolder';
    	onclick = function(){
    		fNextMonth();
    	};
    }
    td.appendChild(nextMonthInput);
    tr.appendChild(td);
    tbody.appendChild(tr);
  //造第二行数据
    
    var tr2 = createCal(giYear,giMonth,dayTdHeight,dayTdTextSize);
    tbody.appendChild(tr2);
    
    table.appendChild(tbody);
    div.appendChild(table);
    
  //造第三行数据
    var div2 = $C('div');
    div.align = 'center';
    var span = $C('span');
    with(span){
    	style.cursor = 'pointer';
    	style.fontWeight = 'bolder';
    	onclick = function(){
    		fSetDate(giYear,giMonth,giDay,giHour,giMinutes,true);
    	};
    	onmouseover = function(){
    		style.color=gcMouseOver;
    	};
    	onmouseout = function(){
    		style.color="#000000";
    	};
    	appendChild(document.createTextNode(strToday+":"+giYear+strYear+giMonth+strMonth+giDay+strDay));
    	 
    }
    
    div2.appendChild(span);

    div.appendChild(div2);
    document.onclick = fHideCalendar;
	document.body.appendChild(div);
}
function timeChange(){
     var iHour = parseInt($("select-hour").value);
	 var iMinutes = parseInt($("select-minutes").value);
	 var iHourNew = new String(iHour);
      var iMinutesNew = new String(iMinutes);

	  if(iHourNew.length<2){
			  iHourNew="0"+iHourNew;
		 }
		  if(iMinutesNew.length<2){
			  iMinutesNew="0"+iMinutesNew;
		 }
	 if(popCtrl.value==null||popCtrl.value==""){
          fSetDate(giYear,giMonth,giDay,iHour,iMinutes);
     }else{
          var inputValueArray = popCtrl.value.split(" ");
		  
		  var inputValue = inputValueArray[0]+" "+iHourNew+":"+iMinutesNew;
          popCtrl.value = inputValue;
	 }
    

}
function addTime(){
	
	var div = $C('div');
	var giHour = gdCurDate.getHours();
	var giMinutes = gdCurDate.getMinutes();
	with(div){
		id = 'div-time';
	    align = 'center';
	}
	var select = $C('select');
	with(select){
		id = 'select-hour';
		onchange=function(){
           timeChange();
		}
	}
	for(var i=0;i<24;i++){
		var option = $C("option");
		with(option){
			value = i;
			if(giHour==i){
				selected = true;
			}
			appendChild(document.createTextNode(i));
		}
		select.appendChild(option);
	}
	div.appendChild(document.createTextNode('时间:'));
	div.appendChild(select);
	div.appendChild(document.createTextNode('点'));
	
	select = $C('select');
	with(select){
		id = 'select-minutes';
		onchange=function(){
           timeChange();
		}
	}
	for(var i=0;i<60;i++){
		var option = $C("option");
		with(option){
			value = i;
			if(giMinutes==i){
				selected = true;
			}
			appendChild(document.createTextNode(i));
		}
		select.appendChild(option);
	}
	div.appendChild(select);
	div.appendChild(document.createTextNode('分'));
	
	$('calendardiv').appendChild(div);
}
function createCal(giYear,giMonth,iCellHeight,iDateTextSize){
	
	var tr = $C('tr');
	var td = $C('td');
	with(td){
		align = 'center';
		colSpan = '4';
	}
	var div = $C('div');
	with(div){
		style.backgroundColor = '#cccccc';
	}
	var table = $C('table');
	with(table){
		width='100%';
		border='0';
		cellpadding='3';
		cellspacing='1';
	}
	var tbody = $C('tbody');
	var tr1 = $C('tr');
	for(var i=0;i<7;i++){
	var td1 = $C('td');
	with(td1){
		bgColor = gcMouseOut;
		borderColor = gcMouseOut;
		valign = 'middle';
		align = 'center';
		style.height = iCellHeight+'px';
		style.fontWeight = 'bolder';
		style.fontSize = iDateTextSize+'px';
		style.color = '#990099';
		appendChild(document.createTextNode(WeekDay[i]));
	  }
	  tr1.appendChild(td1);
	}
	tbody.appendChild(tr1);
	for(var w=1;w<7;w++){
		tr1 = $C('tr');
		for(var d=0;d<7;d++){
			var tmpid=w+""+d;
			var dayTd = $C('td');
			with(dayTd){
				id = 'dayTd-'+tmpid;
				valign = 'middle';
				align = 'center';
				style.height = iCellHeight+'px';
				style.fontWeight = 'bolder';
				style.fontSize = iDateTextSize+'px';
				style.cursor = 'pointer';
				onclick = function(){
					var id = this.id.substr(6);
					fSetSelected(id);
				}
			}
			var span = $C('span');
			span.id='cellText'+tmpid;
			dayTd.appendChild(span);
			tr1.appendChild(dayTd);
		}
		tbody.appendChild(tr1);
	}
	
	table.appendChild(tbody);
	div.appendChild(table);
	td.appendChild(div);
	tr.appendChild(td);
	return tr;
}

function fUpdateCal(iYear,iMonth){
	  var myMonth=fBuildCal(iYear,iMonth);
	  var i=0;
	  for(var w=1;w<7;w++){
	    for(var d=0;d<7;d++){
	      with($("cellText"+w+""+d)){
	        parentNode.bgColor=gcMouseOut;
	        parentNode.borderColor=gcMouseOut;
	        parentNode.onmouseover=function(){
	          this.bgColor=gcMouseOver;
	        };
	        parentNode.onmouseout=function(){
	          this.bgColor=gcMouseOut;
	        };
	      if(myMonth[w][d]<0){
	      style.color=gcNotCurMonth;innerHTML=Math.abs(myMonth[w][d]);
	      }else{
	        style.color=((d==0)||(d==6))?gcRestDay:gcWorkDay;
	        innerHTML=myMonth[w][d];
	        if(iYear==giYear && iMonth==giMonth && myMonth[w][d]==giDay){
	          style.color=gcToday;
	          parentNode.bgColor=gcTodayMouseOut;
	          parentNode.onmouseover=function(){
	            this.bgColor=gcTodayMouseOver;
	          };
	          parentNode.onmouseout=function(){
	           this.bgColor=gcTodayMouseOut;
	          };
	        }
	      }
	    }
	   }
	 }
	}
	function fSetYearMon(iYear,iMon){
	$("tbSelMonth").options[iMon-1].selected=true;
	   for(var i=0;i<$("tbSelYear").length;i++){
	      if($("tbSelYear").options[i].value==iYear){
	        $("tbSelYear").options[i].selected=true;
	      }
	   }
	  fUpdateCal(iYear,iMon);
	}
	function fPrevMonth(){
	  var iMon=$("tbSelMonth").value;
	  var iYear=$("tbSelYear").value;
	  if(--iMon<1){
		  iMon=12;
		  iYear--;
	  }
	  fSetYearMon(iYear,iMon);
	}
	function fNextMonth(){
	  var iMon=$("tbSelMonth").value;
	  var iYear=$("tbSelYear").value;
	  if(++iMon>12){
	     iMon=1;
	     iYear++;
	   }
	  fSetYearMon(iYear,iMon);
	}
	function fGetXY(aTag){
	  var oTmp=aTag;
	  var pt=new Point(0,0);
	  do{
	    pt.x+=oTmp.offsetLeft;
	    pt.y+=oTmp.offsetTop;
	    oTmp=oTmp.offsetParent;
	  }while(oTmp.tagName.toUpperCase()!="BODY");
	  return pt;
	}

	function checkColor(){
		  var color_tmp=(arguments[0]+"").replace(/\s/g,"").toUpperCase();
		  var model_tmp1=arguments[1].toUpperCase();
		  var model_tmp2="rgb("+arguments[1].substring(1,3).HexToDec()+","+arguments[1].substring(1,3).HexToDec()+","+arguments[1].substring(5).HexToDec()+")";
		  model_tmp2=model_tmp2.toUpperCase();
		  if(color_tmp==model_tmp1 ||color_tmp==model_tmp2){
		  return true;
		}
		return false;
		}
		function $V(){
		  return $(arguments[0]).value;
		}

		function fPopCalendar(evt,popCtrl,dateCtrl,isHaveTime){
		  
		 if($('calendardiv')!=null){
			 document.body.removeChild($('calendardiv'));
		 }
		 createDiv();
		 if($('div-time')!=null){
			 $('calendardiv').removeChild($('div-time'));
		 }
		  if(isHaveTime){
			 addTime();
		  }
		  evt.cancelBubble=true;// 阻止事件冒泡
		  gdCtrl=dateCtrl;
		// 参数为当前的年月
		  fSetYearMon(giYear,giMonth);
		  var point=fGetXY(popCtrl);
		  with($("calendardiv").style){
			left=point.x+"px";
			top=(point.y+popCtrl.offsetHeight+1)+"px";

			visibility='visible';

			zindex='99';
			position='absolute';
		  }
		    $("calendardiv").focus();
		}
		function fSetDate(iYear,iMonth,iDay,iHour,iMinutes,isHide){
		  var iMonthNew=new String(iMonth);
		  var iDayNew=new String(iDay);
		  var iHourNew = new String(iHour);
		  var iMinutesNew = new String(iMinutes);
		  if(iMonthNew.length<2){
			iMonthNew="0"+iMonthNew;
		  }
		  
		  if(iDayNew.length<2){
		    iDayNew="0"+iDayNew;
		  }
		  if(iHourNew.length<2){
			  iHourNew="0"+iHourNew;
		 }
		  if(iMinutesNew.length<2){
			  iMinutesNew="0"+iMinutesNew;
		 }
		  gdCtrl.value=iYear+splitChar+iMonthNew+splitChar+iDayNew;
		  if(isHaveTime)
		  gdCtrl.value +=" "+iHourNew+":"+iMinutesNew;
		  if(isHide){
		     fHideCalendar();
		  }
		}
		function fHideCalendar(){
			//alert('dd');
		  $("calendardiv").style.visibility="hidden";
		  for(var i=0;i<goSelectTag.length;i++){
		    goSelectTag[i].style.visibility="visible";
		  }
		  goSelectTag.length=0;
		}
		function fSetSelected(){
		var iOffset=0;
		var iYear=parseInt($("tbSelYear").value);
		var iMonth=parseInt($("tbSelMonth").value);
		if(isHaveTime){
		  var iHour = parseInt($("select-hour").value);
		  var iMinutes = parseInt($("select-minutes").value);
		}
		var aCell=$("cellText"+arguments[0]);
		aCell.bgColor=gcMouseOut;with(aCell){
		var iDay=parseInt(innerHTML);
		if(checkColor(style.color,gcNotCurMonth)){
		iOffset=(innerHTML>10)?-1:1;
		}
		iMonth+=iOffset;
		if(iMonth<1){
		iYear--;iMonth=12;
		}else if(iMonth>12){
		iYear++;iMonth=1;
		}
		}
		fSetDate(iYear,iMonth,iDay,iHour,iMinutes,true);
		}
		function Point(iX,iY){
			this.x=iX;this.y=iY;
		}
		function fBuildCal(iYear,iMonth){
		  var aMonth=new Array();
		  for(var i=1;i<7;i++){
		    aMonth[i]=new Array(i);
		  }
		var dCalDate=new Date(iYear,iMonth-1,1);
		var iDayOfFirst=dCalDate.getDay();
		var iDaysInMonth=new Date(iYear,iMonth,0).getDate();
		var iOffsetLast=new Date(iYear,iMonth-1,0).getDate()-iDayOfFirst+1;
		var iDate=1;
		var iNext=1;
		for(var d=0;d<7;d++){
		  aMonth[1][d]=(d<iDayOfFirst)?(iOffsetLast+d)*(-1):
		  iDate++;
		}
		for(var w=2;w<7;w++)
		{
		  for(var d=0;d<7;d++){
		    aMonth[w][d]=(iDate<=iDaysInMonth)?iDate++:(iNext++)*(-1);
		   }
		}
		  return aMonth;
		}
		
		Array.prototype.Push=function(){
			  var startLength=this.length;
			  for(var i=0;i<arguments.length;i++){
			    this[startLength+i]=arguments[i];
			  }
			   return this.length;
			}
			String.prototype.HexToDec=function(){
			  return parseInt(this,16);
			}
			String.prototype.cleanBlank=function(){
			  return this.isEmpty()?"":this.replace(/\s/g,"");
		}
}