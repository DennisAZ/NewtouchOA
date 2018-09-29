var Calendar = Class.create();
Calendar.prototype = {
   /*{bindToBtn,inputId,property:{week,yearRange,month,format,ishaveTime}}
    * 
    * 
    * 
    */
    
  initialize: function(parameters) {
    
    this.bindInput = $(parameters.inputId);
    if(parameters.bindToBtn){
      this.bindToBtn = $(parameters.bindToBtn);
    }else{
      this.bindToBtn = this.bindInput;
    }
    if(parameters.property){
      this.isHaveTime = parameters.property.isHaveTime;
    }else{
      this.isHaveTime = false; 
    }
    this.divEncode = "dateDiv-";
    this.divId =  this.divEncode + parameters.inputId;//日历控件所在DIV的ID
    
    var now =new Date();
    this.daysNum=[31,28,31,30,31,30,31,31,30,31,30,31];
    //用来保存当前所选的时间
    this.date={
      year:now.getFullYear(),
      month:now.getMonth() + 1,
      day:now.getDate(),
      week:now.getDay(),
      hour:now.getHours(),
      minute:now.getMinutes(),
      second:now.getSeconds()
    };
   
    //设定WEEK
    if(parameters.property&&parameters.property.week){
      this.week = parameters.property.week;
    }else{
      this.week = ['Su','Mo','Tu','We','Th','Fr','Sa'];//从左至右，周日，周一。。。。。。周六
    }
    if(parameters.property&&parameters.property.yearRange){
      this.yearRange = parameters.property.yearRange;
    }else{
      this.yearRange = [1970,2030];//数组第一个为开始年份，第二个为结束年份
    }
    if(parameters.property&&parameters.property.month){
      this.month = parameters.property.month;
    }else{
      this.month=['January','February','March','April','May','June','July','August','September','October','November','December'];//数组顺序，从一月至十二月
    }
    if(parameters.property&&parameters.property.format){
      this.format = parameters.property.format;
    }else{
      if(this.isHaveTime){
        this.format = "yyyy-MM-dd hh:mm";
      }else{
        this.format = "yyyy-MM-dd";
      }
    }
    //注册input事件
    this.showCalEvent = this.showCalHandler.bindAsEventListener(this);
    this.closeCalEvent = this.closeCalHandler.bindAsEventListener(this);
    this.bindToBtn.observe('click', this.showCalEvent);   
  },
  showCalHandler:function(e){
    Event.stop(e);
    if(!this.div){
      this.createCalDiv(e);
      this.initDatesByYM(this.date.year,this.date.month);
    }else{
      this.div.style.visibility = "visible";
    }
    //this.bindInput.observe('blur', this.inputBlurEvent); 
    document.observe( 'click', this.closeCalEvent);
  },
  closeCalHandler:function(e){
   // Event.stopObserving(this.bindInput , 'blur', this.inputBlurEvent);
    this.div.style.visibility = "hidden";
  },
  createCalDiv:function(e){
    this.div = document.createElement("div");
    with(this.div){
      id = this.divId;
      className = "Calendar";
      style.display = "";
    }
    document.body.appendChild(this.div);
    this.div.onclick = function(event){
      if (window.event) {
        window.event.cancelBubble=true;
      } else {
        event.stopPropagation();
      } 
    }
    this.div.focus();
    var locate = this.getElCoordinate(this.bindToBtn);
    if(this.bindToBtn == this.bindInput){
      locate.top += this.bindInput.offsetHeight;
    }
    this.div.setStyle({top:locate.top+"px",left:locate.left+"px",display:"block"});

    var temp="<div id='" + this.divId +"Top' class='top'><b class='arrow'><div class='left'></div></b><b class='arrow' style='float:right;'><div class='right'></div></b></div>"
      + "<div id='" + this.divId + "weeks' class='week'></div><div id='" + this.divId + "Days' class='day'></div>"
      + "<div class='bottom'><div class='btn'>today</div><div id='" + this.divId + "Time' class='time'></div><div class='btn' style='float:right'>cancel</div></div>";
    $(this.div).update(temp);
    this.days = $(this.divId + 'Days');
    this.selectMonth = new Element('b', { id: this.divId + 'Month' }).update("<span>aa</span><select style='display:none'></select>");
    this.selectMonth.setStyle({width:'92px'});
    
    this.selectYear = new Element('b', { id: this.divId + 'Year' }).update("<span>bb</span><select style='display:none'></select>");
    this.selectYear.setStyle({width:'36px'});
    
    this.top = this.div.firstChild;
    var left = this.top.firstChild;
    $(this.top).insert(this.selectMonth , {after:left}); 
    $(this.top).insert(this.selectYear , {after:this.selectMonth}); 
    
    this.selectDay = null;
    this.selectTime = null;
    
    //设定所有星期 
    temp="";
    for(i=0;i<this.week.length;++i){
      temp+="<div>"+this.week[i]+"</div>";
    }
    $(this.divId + "weeks").update(temp);
    
    //设定所有年
    temp="";
    for(i=this.yearRange[0];i<=this.yearRange[1];++i){
      temp+="<option value='"+i+"'>"+i+"</option>";
    }
    var selectYearS = this.selectYear.lastChild;
    var selectYearSpan = this.selectYear.firstChild;
    $(selectYearS).update(temp);
    selectYearS.value = this.date.year;
    selectYearSpan.innerHTML = this.date.year;
    
    //设定所有月
    temp="";
    for(i=0;i<this.month.length;++i){
      temp+="<option value='"+(i+1)+"'>"+this.month[i]+"</option>";
    }
    var selectMonthS = this.selectMonth.lastChild;
    var selectMonthSpan = this.selectMonth.firstChild;
    $(selectMonthS).update(temp);
    selectMonthS.value = this.date.month;
    selectMonthSpan.innerHTML = this.month[this.date.month-1];
    if(this.isHaveTime){
      temp = "";
      for(i = 0 ;i < 24; i++){
        var j = i ;
        if(i < 10){
          j = '0' + i;
        }
        temp += "<option value='"+i+"'>" + j + "</option>";
      }
      this.selectHour = new Element('select' , {id: this.divId + 'Hour'}).update(temp);
      this.selectHour.value = this.date.hour;
      temp = "";
      for(i = 0 ;i <60 ; i++){
        var j = i ;
        if(i < 10){
          j = '0' + i;
        }
        temp += "<option value='"+i+"'>" + j + "</option>";
      }
      this.selectMM = new Element('select' , {id : this.divId + 'Minute'}).update(temp);
      this.selectMM.value = this.date.minute;
      var time = $(this.divId + "Time");
      time.appendChild(this.selectHour);
      time.appendChild(document.createTextNode(" : "));
      time.appendChild(this.selectMM);
    }
    
    this.registerListener();
  },
  //根据传入的年，月，设定这个月内的所有日期
  initDatesByYM:function(year,month){
    this.days.update("");
    first=new Date(year,month-1,1).getDay();
    if(first>0)
      var temp="<div style='width:"+(27*first)+"px;height:20px;float:left;'></div>";//占位而已
    else
      var temp="";
    var i;
    now = new Date();
    nowYear = now.getFullYear();nowMonth=now.getMonth();nowDate=now.getDate();
    for(i=1;i<=this.daysNum[month-1];++i){
      temp += "<a href='#' class='";
      if(year == nowYear&&month==nowMonth+1&&i==nowDate)
        temp += "today";
      if(year ==  this.date.year&&month==this.date.month&&i==this.date.day)
        temp += " select";
      temp+="'>" + i + "</a>";
    }
    if(year%4==0&&month==2){//如果是闰年
      temp+="<a href='#' class='";
      if(year==nowYear&&month==nowMonth+1&&i==nowDate)
        temp+="today";
      if(year==this.date.year&&month==this.date.month&&i==this.date.day)
        temp+=" select";
      temp+="'>"+i+"</a>";
    }
    this.days.update(temp);
    
    this.selectDay = this.days.select('.select');
  },  
  //跳转至上一个月
  preMonth:function(e){
    Event.stop(e);
    var span = this.selectYear.firstChild;
    year = span.innerHTML;
    month = this.selectMonth.lastChild.value;
    if(month>1) month--; 
    else{
      month=12;
      year--; 
      span.update(year);
    }
    this.selectMonth.firstChild.update(this.month[month-1]);
    //this.$tempDate.month=month;
    this.selectMonth.lastChild.value = month;
    
    this.initDatesByYM(year,month);
  }, 
  //跳转至下一个月
  nextMonth:function(e){
    Event.stop(e);
    var span = this.selectYear.firstChild;
    year = span.innerHTML;
    month = this.selectMonth.lastChild.value;
    if(month<12) month++;
    else{
      month=1;
      year++; 
      span.update(year);
    }
    this.selectMonth.firstChild.update(this.month[month-1]);
    this.selectMonth.lastChild.value = month;
    this.initDatesByYM(year,month);
  },
  //点击日期事件
  clickDate:function(e){
    var el = Event.findElement(e, 'A'); 
    Event.stop(e);
    if(el){
      this.selectDay.className = "";
      el.className = "select";
      this.selectDay = el;
      this.date.year = this.selectYear.lastChild.value;
      this.date.month = this.selectMonth.lastChild.value;
      this.date.day = el.innerHTML;
      
      
      var lastDate=new Date(this.date.year,this.date.month-1,this.date.day);
      this.date.week = lastDate.getDay();
      if(this.isHaveTime){
        this.date.hour = this.selectHour.value;
        this.date.minute = this.selectMM.value;
        lastDate.setHours(this.date.hour,this.date.minute);
      }
      this.bindInput.value = lastDate.format(this.format);
    }
    this.closeCalHandler();
    return false;
  },
  //选择月span单击 事件
  selectMonthMouHandler:function(e){
    tmpThis = this.selectMonth.firstChild;
    if(tmpThis.style.display != 'none'){
      tmpThis.hide();
      this.selectMonth.lastChild.show();
      this.selectMonth.lastChild.focus();
      this.selectYear.lastChild.blur();
    }
  },
  //选择年span单击 事件
  selectYearMouHandler:function(e){
    tmpThis = this.selectYear.firstChild;
    if(tmpThis.style.display != 'none'){
      tmpThis.hide();
      this.selectYear.lastChild.show();
      this.selectYear.lastChild.focus();
      this.selectMonth.lastChild.blur();
    }
  },
  //选择月选择框失去焦点事件
  selectMonthBlurHandler:function(e){
    var tmpThis = this.selectMonth.lastChild;
    if(this.selectMonth.firstChild.innerHTML 
                != this.month[parseInt(tmpThis.value) - 1]){
      this.selectMonth.firstChild.update(this.month[parseInt(tmpThis.value) - 1]);
      this.initDatesByYM(this.selectYear.lastChild.value , tmpThis.value);
    }
    tmpThis.hide();
    this.selectMonth.firstChild.show();
  },
  //选择年选择框失去焦点事件
  selectYearBlurHandler:function(e){
    var tmpThis = this.selectYear.lastChild;
    if(this.selectYear.firstChild.innerHTML 
                != tmpThis.value){
      this.selectYear.firstChild.update(tmpThis.value);
      this.initDatesByYM( tmpThis.value , this.selectMonth.lastChild.value);
    }
    tmpThis.hide();
    this.selectYear.firstChild.show();
  },
  //转到当前月
  gotoToday:function(e){
    now = new Date();
    year=now.getFullYear();
    month=now.getMonth()+1;
    this.selectMonth.firstChild.update(this.month[month-1]);
    this.selectMonth.lastChild.value = month;
    this.selectYear.firstChild.update(year);
    this.selectYear.lastChild.value = year;
    if(this.isHaveTime){
      this.selectHour.value = now.getHours();
      this.selectMM.value = now.getMinutes();
    }
    this.initDatesByYM(year,month);
  },
  //取消按扭事件
  cancel:function(e){
    this.selectMonth.firstChild.update(this.month[this.date.month-1]);
    this.selectMonth.lastChild.value = this.date.month;
    this.selectYear.firstChild.update(this.date.year);
    this.selectYear.lastChild.value = this.date.year;
    if(this.isHaveTime){
      this.selectHour.value = this.date.hour;
      this.selectMM.value = this.date.minute;
    }
    this.initDatesByYM(this.date.year,this.date.month);
    this.closeCalHandler();
  },
  hourChangeHandler:function(){
    this.date.hour = this.selectHour.value;
  },
  minuteChangeHandler:function(){
    this.date.minute = this.selectMM.value;
  },
  //注册事件
  registerListener:function(){
    this.selectYear.observe('mousedown',this.selectYearMouHandler.bindAsEventListener(this));
    this.selectMonth.observe('mousedown',this.selectMonthMouHandler.bindAsEventListener(this));
    this.selectMonth.lastChild.observe('blur',this.selectMonthBlurHandler.bindAsEventListener(this));
    this.selectYear.lastChild.observe('blur',this.selectYearBlurHandler.bindAsEventListener(this));
    this.selectMonth.lastChild.onchange = function(){
      this.blur();
    }
    this.selectYear.lastChild.onchange = function(){
      this.blur();
    }
    this.div.lastChild.firstChild.observe('click',this.gotoToday.bindAsEventListener(this));
    this.div.lastChild.lastChild.observe('click',this.cancel.bindAsEventListener(this));
    if(this.isHaveTime){
      this.selectHour.observe('change',this.hourChangeHandler.bindAsEventListener(this));
      this.selectMM.observe('change',this.minuteChangeHandler.bindAsEventListener(this));
    }
    this.days.observe('click', this.clickDate.bindAsEventListener(this)); 
    var left = this.div.firstChild.firstChild;
    left.observe('click',this.nextMonth.bindAsEventListener(this));
    left.nextSibling.observe('click',this.preMonth.bindAsEventListener(this));
  },
 
  ////获取一个DIV的绝对坐标的功能函数,即使是非绝对定位,一样能获取到
  getElCoordinate:function(dom) {
    var t = dom.offsetTop;
    var l = dom.offsetLeft;
    dom=dom.offsetParent;
    while (dom) {
      t += dom.offsetTop;
      l += dom.offsetLeft;
    dom=dom.offsetParent;
    }; return {
      top: t,
      left: l
    };
  },
  //兼容各种浏览器的,获取鼠标真实位置
  mousePosition:function(ev){
    if(!ev) ev=window.event;
      if(ev.pageX || ev.pageY){
        return {x:ev.pageX, y:ev.pageY};
    }
    return {
      x:ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft,
      y:ev.clientY + document.documentElement.scrollTop  - document.body.clientTop
    };
  }
}
//给DATE类添加一个格式化输出字串的方法
Date.prototype.format = function(format)   
{   
   var o = {   
      "M+" : this.getMonth()+1, //month  
      "d+" : this.getDate(),    //day  
      "h+" : this.getHours(),   //hour  
      "m+" : this.getMinutes(), //minute  
      "s+" : this.getSeconds(), //second  ‘
    //quarter  
      "q+" : Math.floor((this.getMonth()+3)/3), 
      "S" : this.getMilliseconds() //millisecond  
   }   
   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));   
    for(var k in o)if(new RegExp("("+ k +")").test(format))   
      format = format.replace(RegExp.$1,   
        RegExp.$1.length==1 ? o[k] :    
          ("00"+ o[k]).substr((""+ o[k]).length));   
    return format;   
 } 

var timeInput = null;
function openTime(timeInputPar) {
  timeInput = timeInputPar;
  openDialog(contextPath + "/core/funcs/orgselect/MultiUserSelect.jsp",  470, 400);
}