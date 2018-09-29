window.onload = new function init()
{
  var fso, s, n, e, x;
  fso = new ActiveXObject("Scripting.FileSystemObject");
  e = new Enumerator(fso.Drives);
  s = "";
  for (; !e.atEnd(); e.moveNext()){
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
  var drives = s.split(","); 
  var tableDrives = document.getElementById("tables_drives");
  for ( var i = 0; i < drives.length-1; i++ ) {
    var option = document.createElement("OPTION");
    drives[i].split(":");
    option.value = "["+drives[i].split(":")[0]+":]"+drives[i].split(":")[1];
    option.text = "["+drives[i].split(":")[0]+":]"+drives[i].split(":")[1];
    tableDrives.add(option);
  }
}