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
  var fso, s, n, e, x;
  fso = new ActiveXObject("Scripting.FileSystemObject");
  e = new Enumerator(fso.Drives);
  s = "";
  for (; !e.atEnd(); e.moveNext()) {
    x = e.item();
    s = s + x.DriveLetter;
    s += ":";
    if (x.DriveType == 3)
      n = x.ShareName;
    else if (x.IsReady)
      n = x.VolumeName;
    else
      n = "[驱动器未就绪]";
    s += n + ",";
  }
  drives = s.split(",");
  var tableDrives = document.getElementById("tables_drives");
  for ( var i = 0; i < drives.length-1; i++ ) {
    var option = document.createElement("OPTION");
    drives[i].split(":");
    option.value = "["+drives[i].split(":")[0]+":]"+drives[i].split(":")[1];
    option.text = "["+drives[i].split(":")[0]+":]"+drives[i].split(":")[1];
    tableDrives.add(option);
  }
  get_drives();
}

/**//*
*tables_drives列表中选中的驱动器上所有文件夹放入table_folder列表中
*/
function get_drives()
{
  var tableDrives = document.getElementById("tables_drives");
  var tableFolders = document.getElementById("table_folder"); 
  for ( var i = 0; i < tableDrives.options.length; i++ ) { 
    if ( tableDrives.options[i].selected == true ) {
    var fso, f, fc, s; 
    var drive = tableDrives.options[i].value.split(":")[0].substring(1,tableDrives.options[i].value.split(":")[0].length);
    document.getElementById("backDir").value = drive + ":\\";
    fso = new ActiveXObject("Scripting.FileSystemObject"); 
    if (fso.DriveExists(drive)) {
      d = fso.GetDrive(drive);
      if ( d.IsReady ) {
        f = fso.GetFolder(d.RootFolder); 
        fc = new Enumerator(f.SubFolders);
        s = "";
        for (;!fc.atEnd(); fc.moveNext())  {
          s += fc.item();
          s += ",";
        }
        var len = tableFolders.options.length;
        while(len >= 0) {
          tableFolders.options.remove(len);
          len--;
        }
        var option = document.createElement("OPTION");
        option.value = drive + ":\\";
        option.text = drive + ":\\";
        tableFolders.add(option);
        var folders = s.split(","); 
        for ( j = 0; j < folders.length -1; j++) {
          option = document.createElement("OPTION");
          option.value = folders[j];
          option.text = folders[j];
          tableFolders.add(option);
        } 
      } else {
        alert("无法改变当前内容！")
      } 
    } else
      return false; 
    } 
  }
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
function get_folder()
{ 
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
      f = fso.GetFolder(folderpath); 
      fc = new Enumerator(f.SubFolders);
      s = "";
      for (;!fc.atEnd(); fc.moveNext()) {
        s += fc.item();
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
     if ( s != "" ) { 
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