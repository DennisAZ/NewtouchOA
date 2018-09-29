function getDeptFunc(deptDiv) {
  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/selectDeptToAttendance.act";
  var rtJson=getJsonRs(url);
  if (rtJson.rtState == "1") {
    alert(rtJson.rtMsrg);
    return;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById(deptDiv);
  for ( var i = 0; i < prcs.length; i++) {
    var prc = prcs[i];
    var option = document.createElement("option");
    option.value = prc.value;
    option.innerHTML = prc.text;
    selects.appendChild(option);
  }
  return userId;
}
function deptSelect(selectId) {
  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/getUserDeptPrivAct.act";
  var rtJson = getJsonRs(url);
  
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    var objSelect = document.getElementById(selectId);
    var isExit = false;
    for ( var i = 0; i < objSelect.options.length; i++) {
      if (objSelect.options[i].value == data.deptId) {
        objSelect.options[i].setAttribute("selected" , true);
        isExit = true;
        break;
      }
    }
    if (!isExit && objSelect.options.length > 1) {
      objSelect.options[1].setAttribute("selected" , true);
    }
  }

}

function isStaticPerson(id) {
  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/getIsStaticAct.act";
  var rtJson = getJsonRs(url);
  
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;

    if (data.deptPriv == '3') {
      var objSelect = document.getElementById(id);
     
      objSelect.style.display = "none";
    }

  }

}

  
function loadData() {
  var deptId = $("deptId").value;
  var startDate = $("startDate").value;
  var endDate = $("endDate").value;
  var deptMore = $("requDept").value;

  var param = "deptId=" + deptId + "&startDate=" + startDate + "&endDate="
      + endDate + "&deptMore=" + deptMore+"&minNum="+minNum+"&maxNum="+loadNum;

  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataAct.act";
  var rtJson = getJsonRs(url, param);

  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    // 无查看权限

    if (data.userData[0] == 1) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "block";
      return;

    }

    // 无可管理用户
    if (data.userData.length == 0) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "block";
      return;
    }

    var table = new Element('table', {
      "class" : "TableBlock",
      "id" : "beSortTable",
      "width" : "100%"
    }).update("<tbody id = 'tboday'><tr class='TableHeader'><td >部门</td>"
        + "<td >姓名</td>" + "<td >内部邮件(收)</td>" + "<td >内部邮件(发)</td>"
        + "<td >日程安排(完成)</td>" + "<td >日程安排(所有)</td>"
        + "<td >工作日志</td>" + "<td >工作流主办(完成)</td>"
        + "<td >工作流主办(所有)</td>" + "<td >工作流会签(完成)</td>"
        + "<td >工作流会签(所有)</td>" + "<td >公告通知(发)</td>"
        + "<td >新闻(发)</td>");

    $('showTableData').innerHTML = "";
    $('showTableData').appendChild(table);

    for ( var i = 0; i < data.userData.length; i++) {
      // ///////转换data成url

      var emailIn = (data.userData[i].emailIn == '0' ? ""
          : data.userData[i].emailIn);
      var emailOut = (data.userData[i].emailOut == '0' ? ""
          : data.userData[i].emailOut);
      var notify = (data.userData[i].notify == '0' ? ""
          : data.userData[i].notify);
      var news = (data.userData[i].news == '0' ? ""
          : data.userData[i].news);
      var calendarFinish = (data.userData[i].calendarFinish == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar_detail.jsp')>"
              + data.userData[i].calendarFinish + "</a>");
      var calendarAll = (data.userData[i].calendarAll == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar1_detail.jsp')>"
              + data.userData[i].calendarAll + "</a>");
      var diary = (data.userData[i].diary == '0' ? ""
          : "<a href=javascript:windowOpen(1,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','diary.jsp')>"
              + data.userData[i].diary + "</a>");
      var workFlowDeelAll = (data.userData[i].workFlowDeelAll == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op1.jsp')>"
              + data.userData[i].workFlowDeelAll + "</a>");
      var workFlowDeelFinish = (data.userData[i].workFlowDeelFinish == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op.jsp')>"
              + data.userData[i].workFlowDeelFinish + "</a>");
      var workFlowSignFinish = (data.userData[i].workFlowSignFinish == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign.jsp')>"
              + data.userData[i].workFlowSignFinish + "</a>");
      var workFlowSignAll = (data.userData[i].workFlowSignAll == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign1.jsp')>"
              + data.userData[i].workFlowSignAll + "</a>");

      // ///
      var tr = new Element('tr', {
        "class" : "TableData"
      });
      $('tboday').appendChild(tr);
      tr.update("<td align='center'>" + data.userData[i].deptName
          + "</td>" + "<td align='center'>"
          + data.userData[i].userName + "</td>"
          + "<td align='center'>" + emailIn + "</td>"
          + "<td align='center'>" + emailOut + "</td>"
          + "<td align='center'>" + calendarFinish + "</td>"
          + "<td align='center'>" + calendarAll + "</td>"
          + "<td align='center'>" + diary + "</td>"
          + "<td align='center'>" + workFlowDeelFinish + "</td>"
          + "<td align='center'>" + workFlowDeelAll + "</td>"
          + "<td align='center'>" + workFlowSignFinish + "</td>"
          + "<td align='center'>" + workFlowSignAll + "</td>"
          + "<td align='center'>" + notify + "</td>"
          + "<td align='center'>" + news + "</td>");

    }

    var opanel = document.getElementById("beSortTable");
    var pchildren = opanel.childNodes;
    if (pchildren.length > 0) {
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "block";
    }
  }

}

function moreLoadData(){loadType=1;
          var objSelect = document.getElementById("deptId");      
          if(objSelect.style.display!="none"){ 
        HideDialog1("optionBlock");
      
        loadData();
     
          }
            HideDialog1("optionBlock");
  }
  
  
  

function deptLoadData() {
  loadType=2;

  var deptId = $("deptId").value;
  var startDate = $("startDate").value;
  var endDate = $("endDate").value;
  var deptMore = "";
  var param = "deptId=" + deptId + "&startDate=" + startDate + "&endDate="
      + endDate + "&deptMore=" + deptMore+"&minNum="+minNum+"&maxNum="+loadNum;

  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataAct.act";
  var rtJson = getJsonRs(url, param);
  
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    // 无查看权限

    if (data.userData[0] == 1) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "block";
      return;

    }

    // 无可管理用户
    if (data.userData.length == 0) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "block";
      return;
    }

    var table = new Element('table', {
      "class" : "TableBlock",
      "id" : "beSortTable",
      "width" : "100%"
    }).update("<tbody id = 'tboday'><tr class='TableHeader'><td >部门</td>"
        + "<td >姓名</td>" + "<td >内部邮件(收)</td>" + "<td >内部邮件(发)</td>"
        + "<td >日程安排(完成)</td>" + "<td >日程安排(所有)</td>"
        + "<td >工作日志</td>" + "<td >工作流主办(完成)</td>"
        + "<td >工作流主办(所有)</td>" + "<td >工作流会签(完成)</td>"
        + "<td >工作流会签(所有)</td>" + "<td >公告通知(发)</td>"
        + "<td >新闻(发)</td>");

    $('showTableData').innerHTML = "";
    $('showTableData').appendChild(table);

    for ( var i = 0; i < data.userData.length; i++) {
      // ///////转换data成url

      var emailIn = (data.userData[i].emailIn == '0' ? ""
          : data.userData[i].emailIn);
      var emailOut = (data.userData[i].emailOut == '0' ? ""
          : data.userData[i].emailOut);
      var notify = (data.userData[i].notify == '0' ? ""
          : data.userData[i].notify);
      var news = (data.userData[i].news == '0' ? ""
          : data.userData[i].news);
      var calendarFinish = (data.userData[i].calendarFinish == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar_detail.jsp')>"
              + data.userData[i].calendarFinish + "</a>");
      var calendarAll = (data.userData[i].calendarAll == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar1_detail.jsp')>"
              + data.userData[i].calendarAll + "</a>");
      var diary = (data.userData[i].diary == '0' ? ""
          : "<a href=javascript:windowOpen(1,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','diary.jsp')>"
              + data.userData[i].diary + "</a>");
      var workFlowDeelAll = (data.userData[i].workFlowDeelAll == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op1.jsp')>"
              + data.userData[i].workFlowDeelAll + "</a>");
      var workFlowDeelFinish = (data.userData[i].workFlowDeelFinish == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op.jsp')>"
              + data.userData[i].workFlowDeelFinish + "</a>");
      var workFlowSignFinish = (data.userData[i].workFlowSignFinish == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign.jsp')>"
              + data.userData[i].workFlowSignFinish + "</a>");
      var workFlowSignAll = (data.userData[i].workFlowSignAll == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign1.jsp')>"
              + data.userData[i].workFlowSignAll + "</a>");

      // ///
      var tr = new Element('tr', {
        "class" : "TableData"
      });
      $('tboday').appendChild(tr);
      tr.update("<td align='center'>" + data.userData[i].deptName
          + "</td>" + "<td align='center'>"
          + data.userData[i].userName + "</td>"
          + "<td align='center'>" + emailIn + "</td>"
          + "<td align='center'>" + emailOut + "</td>"
          + "<td align='center'>" + calendarFinish + "</td>"
          + "<td align='center'>" + calendarAll + "</td>"
          + "<td align='center'>" + diary + "</td>"
          + "<td align='center'>" + workFlowDeelFinish + "</td>"
          + "<td align='center'>" + workFlowDeelAll + "</td>"
          + "<td align='center'>" + workFlowSignFinish + "</td>"
          + "<td align='center'>" + workFlowSignAll + "</td>"
          + "<td align='center'>" + notify + "</td>"
          + "<td align='center'>" + news + "</td>");

    }
  
    var opanel = document.getElementById("beSortTable");
    var pchildren = opanel.childNodes;
    if (pchildren.length > 0) {
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "block";
    }
  }
  
}
  
  
  function dateLoadData(start, end) {
    
  $("startDate").value = start;
  $("endDate").value = end;

  var deptId = $("deptId").value;
  var startDate = $("startDate").value;
  var endDate = $("endDate").value;
  var deptMore = "";

  var param = "deptId=" + deptId + "&startDate=" + startDate + "&endDate="
      + endDate + "&deptMore=" + deptMore+"&minNum="+minNum+"&maxNum="+loadNum;

  var url = contextPath
      + "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataAct.act";
  var rtJson = getJsonRs(url, param);

  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    // 无查看权限

    if (data.userData[0] == 1) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "block";
      return;

    }

    // 无可管理用户
    if (data.userData.length == 0) {
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "block";
      return;
    }

    var table = new Element('table', {
      "class" : "TableBlock",
      "id" : "beSortTable",
      "width" : "100%"
    }).update("<tbody id = 'tboday'><tr class='TableHeader'><td >部门</td>"
        + "<td >姓名</td>" + "<td >内部邮件(收)</td>" + "<td >内部邮件(发)</td>"
        + "<td >日程安排(完成)</td>" + "<td >日程安排(所有)</td>"
        + "<td >工作日志</td>" + "<td >工作流主办(完成)</td>"
        + "<td >工作流主办(所有)</td>" + "<td >工作流会签(完成)</td>"
        + "<td >工作流会签(所有)</td>" + "<td >公告通知(发)</td>"
        + "<td >新闻(发)</td>");

    $('showTableData').innerHTML = "";
    $('showTableData').appendChild(table);

    for ( var i = 0; i < data.userData.length; i++) {
      // ///////转换data成url

      var emailIn = (data.userData[i].emailIn == '0' ? ""
          : data.userData[i].emailIn);
      var emailOut = (data.userData[i].emailOut == '0' ? ""
          : data.userData[i].emailOut);
      var notify = (data.userData[i].notify == '0' ? ""
          : data.userData[i].notify);
      var news = (data.userData[i].news == '0' ? ""
          : data.userData[i].news);
      var calendarFinish = (data.userData[i].calendarFinish == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar_detail.jsp')>"
              + data.userData[i].calendarFinish + "</a>");
      var calendarAll = (data.userData[i].calendarAll == '0' ? ""
          : "<a href=javascript:windowOpen(0,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate
              + "','calendar1_detail.jsp')>"
              + data.userData[i].calendarAll + "</a>");
      var diary = (data.userData[i].diary == '0' ? ""
          : "<a href=javascript:windowOpen(1,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','diary.jsp')>"
              + data.userData[i].diary + "</a>");
      var workFlowDeelAll = (data.userData[i].workFlowDeelAll == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op1.jsp')>"
              + data.userData[i].workFlowDeelAll + "</a>");
      var workFlowDeelFinish = (data.userData[i].workFlowDeelFinish == '0' ? ""
          : "<a href=javascript:windowOpen(2,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_op.jsp')>"
              + data.userData[i].workFlowDeelFinish + "</a>");
      var workFlowSignFinish = (data.userData[i].workFlowSignFinish == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign.jsp')>"
              + data.userData[i].workFlowSignFinish + "</a>");
      var workFlowSignAll = (data.userData[i].workFlowSignAll == '0' ? ""
          : "<a href=javascript:windowOpen(3,'"
              + data.userData[i].userId + "','" + data.startDate
              + "','" + data.endDate + "','workflow_sign1.jsp')>"
              + data.userData[i].workFlowSignAll + "</a>");

      // ///
      var tr = new Element('tr', {
        "class" : "TableData"
      });
      $('tboday').appendChild(tr);
      tr.update("<td align='center'>" + data.userData[i].deptName
          + "</td>" + "<td align='center'>"
          + data.userData[i].userName + "</td>"
          + "<td align='center'>" + emailIn + "</td>"
          + "<td align='center'>" + emailOut + "</td>"
          + "<td align='center'>" + calendarFinish + "</td>"
          + "<td align='center'>" + calendarAll + "</td>"
          + "<td align='center'>" + diary + "</td>"
          + "<td align='center'>" + workFlowDeelFinish + "</td>"
          + "<td align='center'>" + workFlowDeelAll + "</td>"
          + "<td align='center'>" + workFlowSignFinish + "</td>"
          + "<td align='center'>" + workFlowSignAll + "</td>"
          + "<td align='center'>" + notify + "</td>"
          + "<td align='center'>" + news + "</td>");

    }
    
    var opanel = document.getElementById("beSortTable");
    var pchildren = opanel.childNodes;
    if (pchildren.length > 0) {
      var objSelect = document.getElementById("showNoRight");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showNoData");
      objSelect.style.display = "none";
      var objSelect = document.getElementById("showTableData");
      objSelect.style.display = "block";
    }
  }
  
}
  
  // 导出xls文件
  
function loadExcelData(){
     var deptId=$("deptId").value;
     var startDate=$("startDate").value;
     var endDate=$("endDate").value;
     var deptMore=$("requDept").value;  
     var param="deptId="+deptId+"&startDate="+startDate+"&endDate="+endDate+"&deptMore="+deptMore+"&minNum="+minNum+"&maxNum=-1";
       var url = contextPath + "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataToExeclAct.act?"+param;
       window.location=url;
    }
  
  
function windowOpen(index,userId,start,end,jsp){
  var url=jsp+"?userId="+userId+"&startDate="+start+"&endDate="+end;
  if(index==0){//日程安排
    window.open(url,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=yes");
  }else if(index==1){//工作日志
     window.open(url,"","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=yes");
    }else if(index==2){//工作流主办

     window.open(url,"","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=yes");
  }else if(index==3){//工作流会签

     window.open(url,"","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=yes");
    }
}