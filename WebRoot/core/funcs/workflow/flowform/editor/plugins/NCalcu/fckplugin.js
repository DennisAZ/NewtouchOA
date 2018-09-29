//得到控件对象
var FCKObject = plugins({comName:"NCalcu"
  ,item:"NCalcu"
  ,page:"ncalcu.jsp"
  ,inco:"calc.gif"
  ,width:450
  ,height:350
  ,dlgTitle:"计算控件属性"
  ,btnTitle:"计算控件"
});
//为控件对象注测行为

//注册add 方法
function addCalcu(code){
  FCK.InsertHtml(code);
 // alert(code);
}
FCKObject.RegisterAction('Add',addCalcu);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function( contextMenu, tag, tagName ){
  if(tagName == 'INPUT' && tag.className == "CALC"){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NCalcu','计算控件属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener( DSMenuListener ) ;