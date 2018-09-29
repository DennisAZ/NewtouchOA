<%@ page language="java" import=" yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //判断是否为管理员
  //判断是否自己是审批人员

  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>已发布的投票</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectVoteToCurrent.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"hidden", name:"fromId", text:"发布人Id", width: "7%",align:"center"},
       {type:"hidden", name:"deptName", text:"部门名字", width: "1%",align:"center"},
       {type:"text", name:"fromName", text:"发布人", width: "7%",align:"center",render:toFromName},
       {type:"text", name:"subject", text:"标题", width: "6%",align:"center",render:toSubject},
       {type:"text", name:"anonymity", text:"匿名", width: "7%",align:"center",render:toAnonymity},
       {type:"text", name:"starTime", text:"生效日期", width: "6%",align:"center",render:toBenginDate},
       {type:"hidden", name:"endTime", text:"终止日期",align:"center", width: "6%"},
       {type:"hidden", name:"type", text:"类型", width: "7%",align:"center"},
       {type:"hidden", name:"viewPriv", text:"查看", width: "7%",align:"center"},
       {type:"hidden", name:"publish", text:"是否发布", width: "6%",align:"center"},
       {type:"hidden", name:"readers", text:"评论人", width: "6%",align:"center"},
       {type:"hidden", name:"top", text:"置顶", width: "6%",align:"center"},
       {type:"selfdef", name:"caozuo",text:"操作", width:"7%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件的信息!</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
}
function findId(readers){
  var userId = '<%=userId%>';
  var arrayReaders = readers.split(",");
  for(var i= 0; i<arrayReaders.length;i++){
    if(arrayReaders[i]==userId){
       return true;
    }
  }
  return false;
}
//操作
function toFromName(cellData, recordIndex, columInde){
  var deptName =  this.getCellData(recordIndex,"deptName");
  var fromName = this.getCellData(recordIndex,"fromName");
  return "<u title='部门：" + deptName + "' style='cursor:pointer'>" + fromName + "</u>";
}

function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var viewPriv = this.getCellData(recordIndex,"viewPriv");
  var readers = this.getCellData(recordIndex,"readers");
  if(viewPriv != '2'){
    if(viewPriv == '0' && !findId(readers)){
      return "<a href=\"javascript:alert('投票后才能查看投票结果');\">查看结果 </a>";
    }
    return "<a href='javascript:showInfo(" + seqId + ");'>查看结果 </a>";
  }
  return "";
}

function toSubject(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var subject = this.getCellData(recordIndex,"subject");
  var viewPriv = this.getCellData(recordIndex,"viewPriv");
  var readers = this.getCellData(recordIndex,"readers");
  var top = this.getCellData(recordIndex,"top");
  if (top == "1") {
    subject = "<span style=\"color: red;\">" + subject + "</span>";
  }
  if(viewPriv!='2' &&findId(readers) ){
      return "<a href=\"javascript:viewResult("+seqId+");\">"+subject+" </a>";
  }else{
    return "<a href='javascript:openVote(" + seqId + ");'>"+subject+" </a>";
  }
}

//返回匿名
function toAnonymity(cellData, recordIndex,columInde){
  var anonymity =  this.getCellData(recordIndex,"anonymity");
  if (anonymity == "0") {
    return "不允许";
  }
  if (anonymity == "1") {
    return "允许";
  }
}
//返回起始时间
function toBenginDate(cellData, recordIndex,columInde){
  var beginDate =  this.getCellData(recordIndex,"starTime");
  return beginDate.substr(0,10);
}
function showInfo(seqId) {
  var URL = "<%=contextPath%>/subsys/oa/vote/show/showVote.jsp?seqId="+seqId;
  myleft=(screen.availWidth-780)/2;
  window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
function openVote(seqId){
 var URL = "<%=contextPath%>/subsys/oa/vote/show/readVote.jsp?seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function viewResult(seqId){
 var URL="<%=contextPath%>/subsys/oa/vote/show/showVote.jsp?seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<div class="big3">&nbsp;<img src="<%=imgPath%>/vote.gif" align="absmiddle">&nbsp;已发布的投票</div>
 <br>
<div id="giftList"></div>
<div id="returnNull">

</div>

</body>
</html>
