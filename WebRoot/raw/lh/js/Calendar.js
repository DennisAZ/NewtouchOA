var Calendar = Class.create();
Calendar.prototype = {
  initialize: function(bindNode,isHaveTime,splitCharPara) {
    
    this.dateCtrl = $(bindNode)||this.getElementsByName_Calendar('input', bindNode)[0];
    if(this.dateCtrl == null||this.dateCtr.type != 'text'){
      alert('找不到指定的输入框！');
    }
    this.gMonths=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
    this.WeekDay=new Array("日","一","二","三","四","五","六");
    this.strToday = "今天";
    this.strYear = "年";
    this.strMonth = "月";
    this.strDay = "日";
    this.splitChar = "-";
    if(splitCharPara != null){
      this.splitChar = splitCharPara;
    }
    
    this.startYear = 2000;
    this.endYear = 2050;
    this.dayTdHeight = 12;
    this.dayTdTextSize = 12;
    this.gcNotCurMonth = "#E0E0E0";
    this.gcRestDay = "#FF0000";
    this.gcWorkDay = "#444444";
    this.gcMouseOver = "#79D0FF";
    this.gcMouseOut = "#F4F4F4";
    this.gcToday = "#444444";
    this.gcTodayMouseOver = "#6699FF";
    this.gcTodayMouseOut = "#79D0FF";
    this.gdCtrl = new Object();
    this.goSelectTag = new Array();
    this.gdCurDate = new Date();
    this.giYear = this.gdCurDate.getFullYear();
    this.giMonth = this.gdCurDate.getMonth() + 1;
    this.giDay = this.gdCurDate.getDate();
    this.giHour = this.gdCurDate.getHours();
    this.giMinutes = this.gdCurDate.getMinutes();
    this.giSeconds = this.gdCurDate.getSeconds();
    
    this.isHaveTime = isHaveTime;
    this.dateCtrl.observe('click',this.fPopCalendar.bindAsEventListener(this))
  },
  getElementsByName_Calendar: function(tag,eltname){
    var elts=document.getElementsByTagName(tag);
    var count=0;
    var elements=[];
    for(var i=0;i<elts.length;i++){
      var node = elts[i];
      Element.extend(node);
      if(node.readAttribute("name")==eltname){
        elements[count++]=node;
      }
    }
    return elements;
  },
  fPopCalendar: function(evt){
    if($('calendardiv')!=null){
      document.body.removeChild($('calendardiv'));
    }
    createDiv();
    if($('div-time')!=null){
      $('calendardiv').removeChild($('div-time'));
    }
    if(this.isHaveTime){
      addTime();
    }
    evt.cancelBubble=true;// 阻止事件冒泡
    this.gdCtrl=this.dateCtrl;
   // 参数为当前的年月
    fSetYearMon(this.giYear,this.giMonth);
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
}
