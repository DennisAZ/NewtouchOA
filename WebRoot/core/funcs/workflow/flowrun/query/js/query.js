data_hide=1;
function data_show_hide()
{
  data_hide=1-data_hide;
  for (step_i=0; step_i<document.all.length; step_i++)
  {
    if(document.all(step_i).className=="TableLine1"||document.all(step_i).className=="TableLine2")
    {
       if(data_hide)
          document.all(step_i).style.display="none";
       else
          document.all(step_i).style.display="";
    }
  }
}

function hide_item(obj)
{
  var obj1=obj.parentNode.nextSibling.firstChild;
  if(obj1)
  {
    if(obj.value==10)
       obj1.style.display="none";
    else
       obj1.style.display="";
  }    
}
function exchangeHandler(ids){
  $('listFldsStr').value = ids;
}
function empty_date(){
   $("prcsDate1").value="";
   $("prcsDate2").value="";
}
function covert(number) {
  if (number < 10 )
    return "0" + number;
    else return number;
}
function date_change(opt)
{
  var prcs_date1 = $('prcsDate1');
  var prcs_date2 = $('prcsDate2');

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
function createExchangeSelect(selected, disSelected){
  new ExchangeSelectbox({containerId:'exchangeDiv'
    , selectedArray:selected
    , disSelectedArray:disSelected 
    , isSort:false 
    , selectName:'selectedName'
    , selectedChange:exchangeHandler
    , titleText:{selectedTitle:'不显示以下字段 ',disSelectedTitle:'显示以下字段作为表格列'}
  });
}
var options1 = "<option value=\"1\">等于</option>"
    + "<option value=\"2\" >大于</option>"
    + "<option value=\"3\" >小于</option>"
    + "<option value=\"4\" >大于等于</option>"
    + "<option value=\"5\" >小于等于</option>"
    + "<option value=\"6\" >不等于</option>"
    + "<option value=\"7\" >开始为</option>"
    + "<option value=\"8\" selected>包含</option>"
    + "<option value=\"9\" >结束为</option>"
    + "<option value=\"10\" >为空</option>";
var options2 = "<option value=\"1\" selected>等于</option>";
function createRow(item){
  var tr = new Element("tr" ,{'class' : 'TableLine2'});
  tr.style.display = "none";
  $('fromItemList').appendChild(tr);
  var td1 = new Element("td");
  td1.update("[" + item.itemId + "]&nbsp;" + item.title + ":");
  tr.appendChild(td1);
  var td2 = new Element("td");
  tr.appendChild(td2);
  td2.nowrap = true;
  var select = new Element("select" ,{'class' : 'SmallSelect'});
  select.onchange = function() {
    hide_item(this);
  }
  if (item.hasAll) {
    select.update(options1);
  } else {
    select.update(options2);
  }
  td2.appendChild(select);
  tr.appendChild(td2);
  var td3 = new Element("td");
  tr.appendChild(td3);
  td3.update(item.content);
  
  select.id = "relation_" + item.itemId;
  select.setAttribute("name","relation_" + item.itemId);
}
function doInit(){
  skinObjectToSpan(flowrun_query_query);
  var date = new Date();
  $('prcsDate1').value = date.getYear() + "-01-01";
  var beginParameters = {
      inputId:'prcsDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'prcsDate1Img'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'prcsDate2',
      property:{isHaveTime:false}
      ,bindToBtn:'prcsDate2Img'
  };
  new Calendar(endParameters);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHWorkQueryAct/getQueryItem.act";
  var json = getJsonRs(url, "flowId=" + flowId + "&versionNo=" + versionNo) ;
  if (json.rtState == "0") {
    var disSelected = [];
    var queryItem = json.rtData.queryItem;
    var items = json.rtData.items;
    
    var queryItems = queryItem.split(",");
    for (var i = 0 ;i < items.length ;i ++) {
      var item = items[i];
      if (item) {
        if (!queryItem || findIsIn(queryItems, item)) {
          createRow(item);
        }
        if (item.itemId > 0) {
          var option = new Element("option");
          option.value = "DATA_"  + item.itemId;
          option.update(item.title);
          $('groupFld').appendChild(option);
        }
        var tmp = {};
        tmp.value = item.itemId;
        tmp.text = item.title;
        disSelected.push(tmp);
      }
    }
    createExchangeSelect([] , disSelected);
  }
}
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext.title){
      return true;
    }
  }
  return false;
}


