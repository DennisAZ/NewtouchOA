<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>本地文件夹选择----</title>

</head>
<body>
  <input name="backDir" type="hidden" value ="C:\" size="100" width="500">
  <div id="containt" align="center">
    <div align="left" style="width: 200px;height: 200px">
      <select name="tables_drive" id="tables_drives" onchange="get_drives()" ></select><br>
      <select name="table_folder" id="table_folder" size="10" multiple ondblclick="get_folder()"style="width: 200px"></select><br>
    </div>
    <div align="left" style="width: 200px;height: 20px">
      <input type="text" id="folderValue"> <input type="button" value="提交" onclick="onShowPro()">
    </div>
  </div>
</body>
<script type="text/javascript">
var prWindow = window.dialogArguments[0];
var el = window.dialogArguments[1];
function onShowPro() {
  var path = document.getElementById("folderValue").value;
  prWindow.document.getElementById(el).value = path;
  window.close();
}

var drives = null;
window.onload = new function init() {
}
function GetHeader(src) { 
  var ForReading=1; 
  var fso=new ActiveXObject("Scripting.FileSystemObject"); 
  var f=fso.OpenTextFile(src,ForReading); 
  return(f.ReadAll()); 
} 
function dos(path){
  var xml = new ActiveXObject("MSXML2.DOMDocument");
  xml.async = false;
	var xmlRes = GetHeader(path);
	//var xmlObj = eval(xmlRes);
	xml.loadXML(xmlRes);
	var node = xml.getElementsByTagName("projectDescription");//
	var list = xml.selectNodes("//projectDescription/name");
	var elem = node[0];
	var proName =  elem.getElementsByTagName("name")[0].textContent != undefined ? elem.getElementsByTagName("name")[0].textContent : elem.getElementsByTagName("name")[0].text;
  return proName;
}
/**//*
*table_folder双击选项中的一个选项，就将该文件夹下面的文件夹显示在table_folder列表中。
*/
function get_folder(baseUrl){ 
      var fso, f, fc, s; 
      var folderpath = baseUrl;
      fso = new ActiveXObject("Scripting.FileSystemObject"); 
      f = fso.GetFolder(folderpath); 
      fc = new Enumerator(f.SubFolders);
      s = "";
      for (;!fc.atEnd(); fc.moveNext()) {
        s += fc.item();
        s += ",";
      } 
      alert(s);
}

function get_file() {
  var tableFolders = document.getElementById("table_folder"); 
  var tableDrives = document.getElementById("tables_drives");
  for ( var i = 0; i < tableFolders.options.length; i++ ) {
    if ( tableFolders.options[i].selected == true ) {
      var fso, f, fc, s; 
      var folderpath = tableFolders.options[i].value.substring(0,tableFolders.options[i].value.length);
      if ( folderpath.charAt(folderpath.length-1) == "\\" ) {
        document.getElementById("backDir").value = folderpath;
        document.getElementById("folderValue").value = folderpath;
      } else {
        document.getElementById("backDir").value = folderpath + "\\";
        document.getElementById("folderValue").value = folderpath + "\\";
      }
      fso = new ActiveXObject("Scripting.FileSystemObject"); 
      try{
        f = fso.GetFolder(folderpath);
      } catch(e) {
        return;
      } 
      fc = new Enumerator(f.files);
      fc2 = new Enumerator(f.SubFolders);
      s = "";
      for (;!fc.atEnd(); fc.moveNext()) {
        s += fc.item();
        s += ",";
      } 
      for (;!fc2.atEnd(); fc2.moveNext()) {
        s += fc2.item();
        s += ",";
      } 
      var len = tableFolders.options.length;
      while(len >= 0) {
        tableFolders.options.remove(len);
        len--;
      } 
      var opt = ""; 
      var opt1 = "";
      for ( j = 0; j < folderpath.split("\\").length; j++ ) {
        var option = document.createElement("OPTION");
        opt = opt + folderpath.split("\\")[j]+"\\";
        if ( j > 0) {
          opt1 = opt;
          option.value = opt1.substring(0,opt1.length-1);
          option.text = opt1.substring(0,opt1.length-1);
          tableFolders.add(option); 
        } else {
          option.value = opt;
          option.text = opt;
          tableFolders.add(option); 
        }
      }   
      if ( tableFolders.options[0].value == tableFolders.options[1].value ) {
        tableFolders.options.remove(1);
      } 
      if ( s != "" )  { 
        var folders = s.split(","); 
        for ( j = 0; j < folders.length -1; j++) {
          option = document.createElement("OPTION");
          option.value = folders[j];
          option.text = folders[j];
          tableFolders.add(option);
        } 
      } 
    }
  }
}
</script>
</html>