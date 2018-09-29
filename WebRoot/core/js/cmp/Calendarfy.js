var Calendar = Class.create();
Calendar.prototype = {
  initialize : function(parameters) {
    var isIE = !!window.ActiveXObject;
    this.isIE6 = isIE && !window.XMLHttpRequest;
    this.bindInput = $(parameters.inputId);
    if (!this.bindInput) {
      this.bindInput = document.getElementsByName(parameters.inputId)[0]
    }
    if (parameters.bindToBtn) {
      this.bindToBtn = $(parameters.bindToBtn);
      this.isBindToBtn = true
    } else {
      this.bindToBtn = this.bindInput;
      this.isBindToBtn = false
    }
    if (parameters.property) {
      this.isHaveTime = parameters.property.isHaveTime
    } else {
      this.isHaveTime = false
    }
    this.callbackFun = parameters.callbackFun;
    this.divEncode = "dateDiv-";
    this.divId = this.divEncode + parameters.inputId;
    var now = new Date();
    this.daysNum = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
    this.date = {
      year : now.getFullYear(),
      month : now.getMonth() + 1,
      day : now.getDate(),
      week : now.getDay(),
      hour : now.getHours(),
      minute : now.getMinutes(),
      second : now.getSeconds()
    };
    if (parameters.property && parameters.property.week) {
      this.week = parameters.property.week
    } else {
      this.week = [ "日", "一", "二", "三", "四", "五", "六" ]
    }
    if (parameters.property && parameters.property.yearRange) {
      this.yearRange = parameters.property.yearRange
    } else {
      this.yearRange = [ 1920, 2050 ]
    }
    if (parameters.property && parameters.property.month) {
      this.month = parameters.property.month
    } else {
      this.month = [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月",
          "十月", "十一月", "十二月" ]
    }
    if (parameters.property && parameters.property.format) {
      this.format = parameters.property.format
    } else {
      if (this.isHaveTime) {
        this.format = "yyyy-MM-dd hh:mm:ss"
      } else {
        this.format = "yyyy-MM-dd"
      }
    }
    this.showCalEvent = this.showCalHandler.bindAsEventListener(this);
    this.closeCalEvent = this.closeCalHandler.bindAsEventListener(this);
    if (this.bindToBtn) {
      this.bindToBtn.observe("click", this.showCalEvent)
    }
  },
  showCalHandler : function(e) {
    Event.stop(e);
    if (this.format == "yyyy-MM-dd" || this.format == "yyyy-MM-dd hh:mm:ss") {
      var reg1 = /[\d]/g;
      var setTime = this.bindInput.value.strip();
      var aa = setTime.split(" ");
      if (aa.length > 0) {
        if (isValidDateStr(aa[0])) {
          var ba = aa[0].split("-");
          if (ba.length > 0 && ba[0].match(reg1)) {
            var year = parseInt(ba[0]);
            if (year <= this.yearRange[1] && year >= this.yearRange[0]) {
              this.date.year = year
            }
          }
          if (ba.length > 1 && ba[1].match(reg1)) {
            var month = parseInt(ba[1]);
            if (month <= 12 && month >= 1) {
              this.date.month = month
            }
          }
          if (ba.length > 2 && ba[2].match(reg1)) {
            var day = parseInt(ba[2]);
            if (day <= this.daysNum[this.date.month - 1] && day >= 1) {
              this.date.day = day
            }
          }
        }
      }
      if (aa.length > 1) {
        var time = aa[1].split(":");
        if (time.length > 0 && time[0].match(reg1)) {
          var hour = parseInt(time[0]);
          if (hour >= 0 && hour <= 23) {
            this.date.hour = hour
          }
        }
        if (time.length > 1 && time[1].match(reg1)) {
          var m = parseInt(time[1]);
          if (m >= 0 && m <= 59) {
            this.date.minute = m
          }
        }
        if (time.length > 2 && time[2].match(reg1)) {
          var s = parseInt(time[2]);
          if (s >= 0 && s <= 59) {
            this.date.second = s
          }
        }
      }
    }
    this.createCalDiv(e);
    document.observe("click", this.closeCalEvent)
  },
  closeCalHandler : function(e) {
    var node = $("calendarDiv");
    if (node) {
      document.body.removeChild(node)
    }
    if (this.isIE6) {
      var iframe = $("menuIframe");
      if (iframe) {
        document.body.removeChild(iframe)
      }
    }
  },
  createCalDiv : function(e) {
    this.closeCalHandler();
    this.div = new Element("div");
    with (this.div) {
      id = "calendarDiv";
      className = "Calendar";
      style.display = "";
      style.zIndex = 99
    }
    document.body.appendChild(this.div);
    this.div.onclick = function(event) {
      if (window.event) {
        window.event.cancelBubble = true
      } else {
        event.stopPropagation()
      }
    };
    var locate = this.getElCoordinate(this.bindToBtn);
    if (this.bindToBtn == this.bindInput) {
      locate.top += this.bindInput.offsetHeight
    }
    var temp = "<div id='"
        + this.divId
        + "Top' class='top'><b class='arrow'><div class='left'></div></b><b class='arrow' style='float:right;'><div class='right'></div></b></div><div id='"
        + this.divId
        + "weeks' class='week'></div><div id='"
        + this.divId
        + "Days' class='day'></div><div class='bottom'><div class='btn'>今日</div><div id='"
        + this.divId
        + "Time' class='time'></div><div class='btn' style='float:right'>取消</div></div>";
    $(this.div).update(temp);
    this.days = $(this.divId + "Days");
    this.selectMonth = new Element("b", {
      id : this.divId + "Month"
    }).update("<span>aa</span><select style='display:none'></select>");
    try {
      this.selectMonth.setStyle( {
        width : "92px"
      })
    } catch (e) {
      this.selectMonth.style.width = "92px"
    }
    this.selectYear = new Element("b", {
      id : this.divId + "Year"
    }).update("<span>bb</span><select style='display:none'></select>");
    try {
      this.selectYear.setStyle( {
        width : "36px"
      })
    } catch (e) {
      this.selectYear.style.width = "36px"
    }
    this.top = this.div.firstChild;
    var left = this.top.firstChild;
    $(this.top).insert(this.selectMonth, {
      after : left
    });
    $(this.top).insert(this.selectYear, {
      after : this.selectMonth
    });
    this.selectDay = null;
    this.selectTime = null;
    temp = "";
    for (i = 0; i < this.week.length; ++i) {
      temp += "<div>" + this.week[i] + "</div>"
    }
    $(this.divId + "weeks").update(temp);
    temp = "";
    for (i = this.yearRange[0]; i <= this.yearRange[1]; ++i) {
      temp += "<option value='" + i + "'>" + i + "</option>"
    }
    var selectYearS = this.selectYear.lastChild;
    var selectYearSpan = this.selectYear.firstChild;
    $(selectYearS).update(temp);
    selectYearS.value = this.date.year;
    selectYearSpan.innerHTML = this.date.year;
    temp = "";
    for (i = 0; i < this.month.length; ++i) {
      temp += "<option value='" + (i + 1) + "'>" + this.month[i] + "</option>"
    }
    var selectMonthS = this.selectMonth.lastChild;
    var selectMonthSpan = this.selectMonth.firstChild;
    $(selectMonthS).update(temp);
    selectMonthS.value = this.date.month;
    selectMonthSpan.innerHTML = this.month[this.date.month - 1];
    if (this.isHaveTime) {
      temp = "";
      for (i = 0; i < 24; i++) {
        var j = i;
        if (i < 10) {
          j = "0" + i
        }
        temp += "<option value='" + i + "'>" + j + "</option>"
      }
      this.selectHour = new Element("select", {
        id : this.divId + "Hour"
      }).update(temp);
      this.selectHour.value = this.date.hour;
      this.selectHour.hide();
      this.inputHour = new Element("input", {
        id : this.divId + "HourInput"
      });
      temp = "";
      for (i = 0; i < 60; i++) {
        var j = i;
        if (i < 10) {
          j = "0" + i
        }
        temp += "<option value='" + i + "'>" + j + "</option>"
      }
      this.selectMM = new Element("select", {
        id : this.divId + "Minute"
      }).update(temp);
      this.selectMM.value = this.date.minute;
      this.selectMM.hide();
      this.inputMM = new Element("input", {
        id : this.divId + "MMInput"
      });
      this.selectSecond = new Element("select", {
        id : this.divId + "Second"
      }).update(temp);
      this.selectSecond.value = this.date.second;
      this.selectSecond.hide();
      this.inputSecond = new Element("input", {
        id : this.divId + "SecondInput"
      });
      var time = $(this.divId + "Time");
      time.appendChild(this.selectHour);
      time.appendChild(this.inputHour);
      time.appendChild(document.createTextNode(":"));
      time.appendChild(this.selectMM);
      time.appendChild(this.inputMM);
      time.appendChild(document.createTextNode(":"));
      time.appendChild(this.selectSecond);
      time.appendChild(this.inputSecond);
      this.setTime(this.selectHour.value, this.selectMM.value,
          this.selectSecond.value)
    }
    this.registerListener();
    this.initDatesByYM(this.date.year, this.date.month);
    this.documentHeight = document.viewport.getHeight();
    this.divHeight = this.div.clientHeight;
    if (locate.top + this.divHeight > this.documentHeight) {
      locate.top = locate.top - this.divHeight;
      if (this.isBindToBtn) {
        locate.top += 20
      } else {
        locate.top -= 20
      }
    }
    this.documentWidth = document.viewport.getWidth();
    this.divWidth = this.div.clientWidth;
    if (locate.left + this.divWidth > this.documentWidth) {
      locate.left = locate.left - this.divWidth;
      if (this.isBindToBtn) {
        locate.left += 20
      } else {
        locate.left -= 20
      }
    }
    try {
      this.div.setStyle( {
        top : locate.top + "px",
        left : locate.left + "px",
        display : "block"
      })
    } catch (e) {
      this.div.style.top = locate.top + "px";
      this.div.style.left = locate.left + "px";
      this.div.style.display = "block"
    }
    if (this.isIE6) {
      this.createShim(locate)
    }
  },
  setTime : function(hh, mm, ss) {
    if (hh.length == 1) {
      hh = "0" + hh
    }
    if (mm.length == 1) {
      mm = "0" + mm
    }
    if (ss.length == 1) {
      ss = "0" + ss
    }
    this.inputHour.value = hh;
    this.inputMM.value = mm;
    this.inputSecond.value = ss
  },
  initDatesByYM : function(year, month) {
    $(this.days).update("");
    first = new Date(year, month - 1, 1).getDay();
    if (first > 0) {
      var temp = "<div  style='width:" + (27 * first)
          + "px;height:18px;float:left;'></div>"
    } else {
      var temp = ""
    }
    var i;
    now = new Date();
    nowYear = now.getFullYear();
    nowMonth = now.getMonth();
    nowDate = now.getDate();
    for (i = 1; i <= this.daysNum[month - 1]; ++i) {
      temp += "<a href='javascript:void(0)' class='";
      var idTmp = "";
      if (year == nowYear && month == nowMonth + 1 && i == nowDate) {
        temp += "today";
        idTmp = " id='todayA'"
      }
      if (year == this.date.year && month == this.date.month
          && i == this.date.day) {
        temp += " select"
      }
      temp += "' " + idTmp + ">" + i + "</a>"
    }
    if (year % 4 == 0 && month == 2) {
      temp += "<a href='javascript:void(0)' class='";
      var idTmp = "";
      if (year == nowYear && month == nowMonth + 1 && i == nowDate) {
        temp += "today";
        idTmp = " id='todayA'"
      }
      if (year == this.date.year && month == this.date.month
          && i == this.date.day) {
        temp += " select"
      }
      temp += "' " + idTmp + ">" + i + "</a>"
    }
    $(this.days).update(temp);
    this.selectDay = this.days.select(".select")
  },
  preMonth : function(e) {
    Event.stop(e);
    var span = this.selectYear.firstChild;
    year = span.innerHTML;
    month = this.selectMonth.lastChild.value;
    if (month > 1) {
      month--
    } else {
      month = 12;
      year--;
      $(span).update(year)
    }
    $(this.selectMonth.firstChild).update(this.month[month - 1]);
    this.selectMonth.lastChild.value = month;
    this.selectYear.lastChild.value = year;
    this.initDatesByYM(year, month)
  },
  nextMonth : function(e) {
    Event.stop(e);
    var span = this.selectYear.firstChild;
    year = span.innerHTML;
    month = this.selectMonth.lastChild.value;
    if (month < 12) {
      month++
    } else {
      month = 1;
      year++;
      $(span).update(year)
    }
    $(this.selectMonth.firstChild).update(this.month[month - 1]);
    this.selectMonth.lastChild.value = month;
    this.selectYear.lastChild.value = year;
    this.initDatesByYM(year, month)
  },
  clickDate : function(e) {
    var el = Event.findElement(e, "A");
    Event.stop(e);
    if (el) {
      this.setTimeToInput(el)
    }
    this.closeCalHandler();
    return false
  },
  setTimeToInput : function(el) {
    this.selectDay.className = "";
    el.className = "select";
    this.selectDay = el;
    this.date.year = this.selectYear.lastChild.value;
    this.date.month = this.selectMonth.lastChild.value;
    this.date.day = el.innerHTML;
    var lastDate = new Date(this.date.year, this.date.month - 1, this.date.day);
    this.date.week = lastDate.getDay();
    if (this.isHaveTime) {
      this.date.hour = this.selectHour.value;
      this.date.minute = this.selectMM.value;
      this.date.second = this.selectSecond.value;
      lastDate.setHours(this.date.hour, this.date.minute, this.date.second)
    }
    var str = lastDate.format(this.format);
    this.bindInput.value = str;
    if (this.callbackFun) {
      this.callbackFun(str)
    }
  },
  selectMonthMouHandler : function(e) {
    tmpThis = this.selectMonth.firstChild;
    if (tmpThis.style.display != "none") {
      $(tmpThis).hide();
      $(this.selectMonth.lastChild).show();
      $(this.selectMonth.lastChild).focus();
      $(this.selectYear.lastChild).blur()
    }
  },
  selectYearMouHandler : function(e) {
    tmpThis = this.selectYear.firstChild;
    if (tmpThis.style.display != "none") {
      $(tmpThis).hide();
      $(this.selectYear.lastChild).show();
      $(this.selectYear.lastChild).focus();
      $(this.selectMonth.lastChild).blur()
    }
  },
  selectMonthBlurHandler : function(e) {
    var tmpThis = this.selectMonth.lastChild;
    if (this.selectMonth.firstChild.innerHTML != this.month[parseInt(tmpThis.value) - 1]) {
      $(this.selectMonth.firstChild).update(
          this.month[parseInt(tmpThis.value) - 1]);
      this.initDatesByYM(this.selectYear.lastChild.value, tmpThis.value)
    }
    tmpThis.hide();
    this.selectMonth.firstChild.show()
  },
  selectYearBlurHandler : function(e) {
    var tmpThis = this.selectYear.lastChild;
    if (this.selectYear.firstChild.innerHTML != tmpThis.value) {
      $(this.selectYear.firstChild).update(tmpThis.value);
      this.initDatesByYM(tmpThis.value, this.selectMonth.lastChild.value)
    }
    tmpThis.hide();
    this.selectYear.firstChild.show()
  },
  gotoToday : function(e) {
    now = new Date();
    year = now.getFullYear();
    month = now.getMonth() + 1;
    $(this.selectMonth.firstChild).update(this.month[month - 1]);
    this.selectMonth.lastChild.value = month;
    $(this.selectYear.firstChild).update(year);
    this.selectYear.lastChild.value = year;
    if (this.isHaveTime) {
      this.selectHour.value = now.getHours();
      this.selectMM.value = now.getMinutes();
      this.selectSecond.value = now.getSeconds();
      this.setTime(this.selectHour.value, this.selectMM.value,
          this.selectSecond.value)
    }
    this.initDatesByYM(year, month);
    var el = $("todayA");
    if (el) {
      this.setTimeToInput(el)
    }
    this.closeCalHandler()
  },
  cancel : function(e) {
    $(this.selectMonth.firstChild).update(this.month[this.date.month - 1]);
    $(this.selectMonth.lastChild).value = this.date.month;
    $(this.selectYear.firstChild).update(this.date.year);
    $(this.selectYear.lastChild).value = this.date.year;
    if (this.isHaveTime) {
      this.selectHour.value = this.date.hour;
      this.selectMM.value = this.date.minute;
      this.selectSecond.value = this.date.second
    }
    this.initDatesByYM(this.date.year, this.date.month);
    this.closeCalHandler()
  },
  hourChangeHandler : function() {
    this.date.hour = this.selectHour.value;
    var hh = this.date.hour;
    if (hh.length == 1) {
      hh = "0" + hh
    }
    this.inputHour.value = hh;
    this.selectHour.hide();
    this.inputHour.show()
  },
  secondChangeHandler : function() {
    this.date.second = this.selectSecond.value;
    var second = this.date.second;
    if (second.length == 1) {
      second = "0" + second
    }
    this.inputSecond.value = second;
    this.selectSecond.hide();
    this.inputSecond.show()
  },
  minuteChangeHandler : function() {
    this.date.minute = this.selectMM.value;
    var minute = this.date.minute;
    if (minute.length == 1) {
      minute = "0" + minute
    }
    this.inputMM.value = minute;
    this.selectMM.hide();
    this.inputMM.show()
  },
  inputHourFouceHandler : function() {
    if (this.selectHour.style.display == "none") {
      this.inputHour.blur();
      this.inputHour.hide();
      this.selectHour.show();
      this.selectHour.focus()
    }
  },
  inputMMFouceHandler : function() {
    if (this.selectMM.style.display == "none") {
      this.inputMM.blur();
      this.inputMM.hide();
      this.selectMM.show();
      this.selectMM.focus()
    }
  },
  inputSecondFouceHandler : function() {
    if (this.selectSecond.style.display == "none") {
      this.inputSecond.blur();
      this.inputSecond.hide();
      this.selectSecond.show();
      this.selectSecond.focus()
    }
  },
  registerListener : function() {
    this.selectYear.observe("mousedown", this.selectYearMouHandler
        .bindAsEventListener(this));
    this.selectMonth.observe("mousedown", this.selectMonthMouHandler
        .bindAsEventListener(this));
    $(this.selectMonth.lastChild).observe("blur",
        this.selectMonthBlurHandler.bindAsEventListener(this));
    $(this.selectYear.lastChild).observe("blur",
        this.selectYearBlurHandler.bindAsEventListener(this));
    if (this.isHaveTime) {
      this.inputHour.observe("mousedown", this.inputHourFouceHandler
          .bindAsEventListener(this));
      this.selectHour.observe("blur", this.hourChangeHandler
          .bindAsEventListener(this));
      this.inputMM.observe("mousedown", this.inputMMFouceHandler
          .bindAsEventListener(this));
      this.selectMM.observe("blur", this.minuteChangeHandler
          .bindAsEventListener(this));
      this.inputSecond.observe("mousedown", this.inputSecondFouceHandler
          .bindAsEventListener(this));
      this.selectSecond.observe("blur", this.secondChangeHandler
          .bindAsEventListener(this));
      this.selectHour.onchange = function() {
        this.blur()
      };
      this.selectMM.onchange = function() {
        this.blur()
      };
      this.selectSecond.onchange = function() {
        this.blur()
      }
    }
    this.selectMonth.lastChild.onchange = function() {
      this.blur()
    };
    this.selectYear.lastChild.onchange = function() {
      this.blur()
    };
    var divlf = this.div.lastChild.firstChild;
    try {
      divlf.observe("click", this.gotoToday.bindAsEventListener(this))
    } catch (e) {
      Event.observe(divlf, "click", this.gotoToday.bind(this))
    }
    var divll = this.div.lastChild.lastChild;
    try {
      divll.observe("click", this.cancel.bindAsEventListener(this))
    } catch (e) {
      Event.observe(divll, "click", this.cancel.bind(this))
    }
    var divllday = this.days;
    try {
      this.days.observe("click", this.clickDate.bindAsEventListener(this))
    } catch (e) {
      Event.observe(divllday, "click", this.clickDate.bind(this))
    }
    var left = this.div.firstChild.firstChild;
    try {
      left.observe("click", this.preMonth.bindAsEventListener(this))
    } catch (e) {
      Event.observe(left, "click", this.preMonth.bind(this))
    }
    var nextSibling = left.nextSibling;
    try {
      nextSibling.observe("click", this.nextMonth.bindAsEventListener(this))
    } catch (e) {
      Event.observe(nextSibling, "click", this.nextMonth.bind(this))
    }
  },
  getElCoordinate : function(dom) {
    var t = dom.offsetTop;
    var l = dom.offsetLeft;
    dom = dom.offsetParent;
    while (dom) {
      t += dom.offsetTop;
      l += dom.offsetLeft;
      dom = dom.offsetParent
    }
    return {
      top : t,
      left : l
    }
  },
  mousePosition : function(ev) {
    if (!ev) {
      ev = window.event
    }
    if (ev.pageX || ev.pageY) {
      return {
        x : ev.pageX,
        y : ev.pageY
      }
    }
    return {
      x : ev.clientX + document.documentElement.scrollLeft
          - document.body.clientLeft,
      y : ev.clientY + document.documentElement.scrollTop
          - document.body.clientTop
    }
  },
  createShim : function(locate) {
    this.shim = new Element("iframe");
    this.shim.id = "menuIframe";
    this.shim.scrolling = "no";
    this.shim.frameborder = "0";
    this.shim.src = contextPath + "/core/inc/emptyshim.html";
    this.shim.style.position = "absolute";
    this.shim.style.filter = "alpha(opacity=40)";
    this.shim.style.opacity = 0.4;
    this.shim.style.position = "absolute";
    this.shim.style.display = "block";
    this.shim.style.zIndex = 10;
    this.shim.style.top = (locate.top - 1) + "px";
    this.shim.style.left = (locate.left - 1) + "px";
    this.shim.style.width = this.div.clientWidth;
    this.shim.style.height = this.div.clientHeight;
    document.body.appendChild(this.shim)
  }
};
Date.prototype.format = function(format) {
  var o = {
    "M+" : this.getMonth() + 1,
    "d+" : this.getDate(),
    "h+" : this.getHours(),
    "m+" : this.getMinutes(),
    "s+" : this.getSeconds(),
    "q+" : Math.floor((this.getMonth() + 3) / 3),
    S : this.getMilliseconds()
  };
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (this.getFullYear() + "")
        .substr(4 - RegExp.$1.length))
  }
  for ( var k in o) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
          : ("00" + o[k]).substr(("" + o[k]).length))
    }
  }
  return format
};
var timeField = null;
var callbackFun = null;
function showTime(time, isHaveDate, callbackFunPar) {
  if (!isHaveDate) {
    $(time).value = ""
  }
  callbackFun = null;
  if (callbackFunPar) {
    callbackFun = callbackFunPar
  }
  timeField = time;
  var url = contextPath + "/core/inc/clock.jsp";
  openDialog(url, 280, 120)
}
function showTime2(time, isHaveDate, callbackFunPar) {
  if (!isHaveDate) {
    $(time).value = ""
  }
  callbackFun = null;
  if (callbackFunPar) {
    callbackFun = callbackFunPar
  }
  timeField = time;
  var url = contextPath + "/core/funcs/attendance/personal/clock.jsp";
  openDialog(url, 280, 120)
};