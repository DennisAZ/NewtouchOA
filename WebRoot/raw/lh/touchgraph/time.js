var bindToInput = null;
function doInit() {
  $('date1Img').onclick = function() {
    bindToInput = 'date1';
    openCal();
  }
  $('date2Img').onclick = function() {
    bindToInput = 'date2';
    openCal();
  }
}
function openCal() {
  var url = contextPath + "/core/module/touchgraph/calender.jsp";
  openDialog(url , 200, 210);
}
function covert(number) {
  if (number < 10 )
    return "0" + number;
    else return number;
}
function date_change(opt)
{
  var prcs_date1 = $('date1');
  var prcs_date2 = $('date2');

  var date = new Date();
  var month =  date.getMonth() + 1;
  var day = date.getDate();

  switch(opt){
    case '1':
      var now = date.getYear() + "-"+ covert(month)  +"-" + covert(day);
      prcs_date1.value= now;
      prcs_date2.value= now;
      break;
    case '2':
      var now = date.getYear() + "-"+ covert(month)  +"-" + covert(day - 1);
      prcs_date1.value= now;
      prcs_date2.value= now;
      break;
    case '3':
      var weekfirstDay = getWeekFirstDay();
      var weeklastDay = getWeekLastDay();
      prcs_date1.value=  getString(weekfirstDay);
      prcs_date2.value= getString(weeklastDay);
      break;
    case '4':
      var weekfirstDay = getPWeekFirstDay();
      var weeklastDay = getPWeekLastDay();
      prcs_date1.value=  getString(weekfirstDay);
      prcs_date2.value= getString(weeklastDay);
      break;
    case '5':
      var weekfirstDay = getMonthFirstDay();
      var weeklastDay = getMonthLastDay();
      prcs_date1.value=  getString(weekfirstDay);
      prcs_date2.value= getString(weeklastDay);
      break;
    case '6':
      var weekfirstDay = getPMonthFirstDay();
      var weeklastDay = getPMonthLastDay();
      prcs_date1.value=  getString(weekfirstDay);
      prcs_date2.value= getString(weeklastDay);
      break;
  }
  setHSliderValue();
}
function getString(day) {
  var month = day.getMonth() + 1; 
  var now = day.getYear() + "-"+ covert(month)  +"-" + covert(day.getDate());
  return now;
}
function getWeekFirstDay() {
  var Nowdate = new Date();
  var WeekFirstDay = new Date(Nowdate - (Nowdate.getDay() - 1) * 86400000);
  return WeekFirstDay;
}
function getWeekLastDay() {
  var WeekLastDay = new Date((getWeekFirstDay() / 1000 + 6 * 86400) * 1000);
  return WeekLastDay;
}
function getMonthFirstDay() {
  var Nowdate = new Date();
  var MonthFirstDay = new Date(Nowdate.getYear(), Nowdate.getMonth(), 1);
  return MonthFirstDay;
}
function getMonthLastDay() {
  var Nowdate = new Date();
  var MonthNextFirstDay = new Date(Nowdate.getYear(), Nowdate.getMonth() + 1, 1);
  var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
  return MonthLastDay;
}
function getPWeekFirstDay() {
  var Nowdate = new Date();
  var WeekFirstDay = new Date(Nowdate - ((Nowdate.getDay() - 1) + 7) * 86400000);
  return WeekFirstDay;
}
function getPWeekLastDay() {
  var WeekLastDay = new Date((getPWeekFirstDay() / 1000 + 6 * 86400) * 1000);
  return WeekLastDay;
}
function getPMonthFirstDay() {
  var Nowdate = new Date();
  var pMonth = Nowdate.getMonth();
  var year = Nowdate.getYear();
  if (Nowdate.getMonth() == 0) {
    pMonth = 11;
    year = year - 1;
  } else {
    pMonth = pMonth - 1;
  }
  var MonthFirstDay = new Date(year, pMonth, 1);
  return MonthFirstDay;
}
function getPMonthLastDay() {
  var MonthLastDay = new Date(getMonthFirstDay() - 86400000);
  return MonthLastDay;
}
function empty_date(){
  $("date1").value="";
  $("date2").value="";
}
function setDate(value , value2) {
  $('date1').value = value;
  $('date2').value = value2;
}
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}
function setHSliderValue() {
  var val1 = 0 ;
  var val2 = 0 ;
  
  var date1 = $('date1').value;
  if (date1) {
    var ss = date1.split("-");
    var year = parseInt(ss[0]);
    if (ss[1].startsWith("0")) {
      ss[1] = ss[1].substr(1);
    }
    var month = parseInt(ss[1]);
    if (ss[2].startsWith("0")) {
      ss[2] = ss[2].substr(1);
    }
    var day = parseInt(ss[2]);
    val1 = new Date(year , month - 1 , day);
  }
  var date2 = $('date2').value;
  if (date2) {
    var ss = date2.split("-");
    var year = parseInt(ss[0]);
    if (ss[1].startsWith("0")) {
      ss[1] = ss[1].substr(1);
    }
    var month = parseInt(ss[1]);
    if (ss[2].startsWith("0")) {
      ss[2] = ss[2].substr(1);
    }
    var day = parseInt(ss[2]);
    val2 = new Date(year , month - 1 , day);
  }
  getSwf("time").setHSliderValue(val1 , val2);
}

