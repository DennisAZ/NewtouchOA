<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic"%>
<%
int receiveCount = YHDocSendLogic.getReceiveCount(request);
int sendCount = YHDocSendLogic.getWaitSendCount(request);
out.print("(function($){\n"+
  "$(document).ready(function($){\n"+
	  "var count_str = {\"JH130\":\""+sendCount+"\",\"JH131\":\""+receiveCount+"\",\"JH132\":\""+0+"\"};\n"+
	  "var array = count_str;\n"+
	  "if(typeof(array) == \"object\"){\n"+
	    "var counts = 0;\n"+
	    "var countid = 0;\n"+
	    "for(var id in array){\n"+
	      "countid = Math.min(1000, eval('array.' + id));\n"+
        "var count = Math.min(10, eval('array.' + id));\n"+
	      "var className = count > 0 ? ('count count' + count) : 'count';\n"+
        "if(countid > 0 && countid < 10){\n"+
          "className += ' countNEW1';\n"+
        "}else if(countid < 100 && countid > 0){\n"+
          "className += ' countNEW10';\n"+
        "}else if(countid >= 100){\n"+
          "className = ' countNEW100';\n"+
        "}\n"+
	      "switch(id){\n"+
	         "case 'JH130' : $('#count_JH130').attr('class', className);if(countid>0){$('#count_JH130 .countword').html(countid);}break;\n"+
	         "case 'JH131' : $('#count_JH131').attr('class', className);if(countid>0){$('#count_JH131 .countword').html(countid);}break;\n"+
	         "case 'JH132' : $('#count_JH132').attr('class', className);if(countid>0){$('#count_JH132 .countword').html(countid);}break;\n"+
	      "}\n"+
	      "counts += count;\n"+
	    "}\n"+
	    "if(counts > 0){\n"+
	      "parent.BlinkTabs('p0');\n"+
	    "}\n"+
    "}\n"+
  "});\n"+
"})(jQuery);"); %>