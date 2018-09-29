<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
out.print("JHapparrayinfo[\"JH130\"] = [\"密码网发文\",'/yh/subsys/jtgwjh/sendDoc/indexSend.jsp',\"fa1\"];\n");
out.print("JHapparrayinfo[\"JH131\"] = [\"密码网收文\",'/yh/subsys/jtgwjh/receiveDoc/index.jsp',\"shou1\"];\n");
out.print("JHapparrayinfo[\"JH132\"] = [\"密码网查询\",'/yh/subsys/jtgwjh/search/indexSearch.jsp',\"cha1\"];\n");
out.print("(function($){\n"+
  "jQuery(document).ready(function($){\n"+
		"var modules_out = [{\"func_id\":\"JH130\" ,\"id\": \"score\",\"name\":\"密码网发文\"},{\"func_id\":\"JH131\" ,\"id\": \"sale_manage\",\"name\":\"密码网收文\"},{\"func_id\":\"JH132\" ,\"id\": \"attendance_manage\",\"name\":\"密码网查询\"}];\n"+
		"$.each(modules_out || [] ,function(i,e){\n"+
		   "addApp2(e,slideBox.getCursor());\n"+
		"});\n"+
	  "function addApp2(e, index) { \n"+
	    "var s = slideBox.getScreen(index); \n"+
	    "if(s) { \n"+
	      "var ul = s.find(\"ul\"); \n"+
	      "if(!ul.length) { \n"+
	        "ul = jQuery(\"<ul></ul>\");\n"+
	        "s.append(ul); \n"+
	        "ul.sortable({\n"+
	          "revert: true,\n"+
	          "tolerance: 'pointer',\n"+
	          "connectWith: \".screen ul\",\n"+
	          "scroll: false,\n"+
	          "stop: function(e, ui) {\n"+
	            "setTimeout(function() {\n"+
	              "jQuery(\".block.remove\").remove();\n"+
	              "jQuery(\"#trash\").hide();\n"+
	              "ui.item.click(openUrl);\n"+
	            "}, 0);\n"+
	          "},\n"+
	          "start: function(e, ui) {\n"+
	            "jQuery(\"#trash\").show();\n"+
	            "ui.item.unbind(\"click\");\n"+
	          "}\n"+
	        "});\n"+
	      "} \n"+
	      "addModule(e, s.find(\"ul\")); \n"+
	    "} \n"+
	  "}\n"+
	"});\n"+
"})(jQuery);\n"+
"function openUrlJH(OBJ){\n"+
  "var id = OBJ.id.substr(8);\n"+
  "var func_id = jQuery(OBJ).attr('index');\n"+
  "if(typeof(JHapparrayinfo) && typeof(JHapparrayinfo[func_id])) {\n"+
    "pWindow.createTab(func_id, JHapparrayinfo[func_id][0], \"http://"+request.getServerName()+":"+request.getServerPort()+"\"+JHapparrayinfo[func_id][1], JHapparrayinfo[func_id][2]);\n"+
  "}\n"+
  "return;\n"+
"}\n"+
"(function($){\n"+
  "$(document).ready(function($){\n"+
	  "var count_str = {\"workflow\":\"2\"};\n"+
	  "var array = count_str;\n"+
	  "if(typeof(array) == \"object\"){\n"+
	    "var counts = 0;\n"+
	    "for(var id in array){\n"+
	      "var count = Math.min(10, eval('array.' + id));\n"+
	      "var className = count > 0 ? ('count count' + count) : 'count';\n"+
	      "if(id){\n"+
	        "$('#count_JH130').attr('class', className);\n"+
	      "}\n"+
	      "counts += count;\n"+
	    "}\n"+
	    "if(counts > 0){\n"+
	      "parent.BlinkTabs('p0');\n"+
	    "}\n"+
    "}\n"+
  "});\n"+
"})(jQuery);"); %>