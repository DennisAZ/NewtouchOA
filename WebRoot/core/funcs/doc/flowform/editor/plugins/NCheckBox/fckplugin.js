//得到控件对象
var FCKObject = plugins({comName:"NCheckBox"
  ,item:"NCheckBox"
  ,page:"ncheckbox.jsp"
  ,inco:"checkbox.gif"
  ,width:340
  ,height:170
  ,dlgTitle:"选择框属性"
  ,btnTitle:"选择框"
});
//为控件对象注测行为

//注册add 方法
function addCheckBox(code){
  FCK.InsertHtml(code);
 // alert(code);
}
FCKObject.RegisterAction('Add',addCheckBox);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("");
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
  if(tagName == 'INPUT' && tag.type == 'checkbox'&& (tag.className != "AUTO"
  )){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NCheckBox','选择框属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener( DSMenuListener ) ;