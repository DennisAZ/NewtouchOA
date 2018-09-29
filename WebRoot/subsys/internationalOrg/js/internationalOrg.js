function index(type,subjecti){
    var urls =contextPath+"/yh/subsys/internationalOrg/act/YHInternationalOrgAct/getSubjectAct.act?type=" + type;
    if(type!=3){
      var rtJsons = getJsonRs(urls);
    }else{
      var rtJsons = getJsonRs(urls,"subjecti=" + subjecti );
    }
      //alert(rsText);
      var text="";
      var index=0;
      if(rtJsons.rtState == '0'){
         var data=rtJsons.rtData.data;
         for(var i=0;i<data.length;i++){
           var cap="capital";
           if(i%2==0){
             cap="capital1";
           }
           
        text+="    <div class=\""+cap+"\">";
        text+="      <div class=\"Super\" id=\""+data[i].FL+"\">"+data[i].FL+"</div>";
        text+="     <ul>";
        var names=data[i].name;
        for(var j=0;j<names.length;j++){
          
          if(type == "1"){
            var nameStr=names[j].subjecti;
            
            nameStr="<font color=''>"+nameStr+"</font>";
            text+="<li><a href=\"javascript:checkSubjecti('"+data[i].FL+"','"+names[j].subjecti+"','3')\">"+nameStr+"</a> </li>";
          }else{
            var nameStr=names[j].subjectii;
            
            nameStr="<font color=''>"+nameStr+"</font>";
            text+="<li><a href=\"javascript:saveSubjectii('"+names[j].subjectii+"')\">"+nameStr+"</a>&nbsp;&nbsp;&nbsp; </li>";
          }
          
        }
        text+="      </ul>";
        text+="     </div>  ";
              
         }
        $("container").style.display = "";
        $("cd-content").innerHTML=text;
        }else{
         alert(rtJsons.rtMsrg);
      }
}
function checkSubjecti(letter,subjecti,type){
  $("subjecti").value = subjecti;
  getGroup();
  index(type,subjecti);
  
}

function saveSubjectii(subjectii){
  $("subjectii").value = subjectii;
  getGroup();
}

//(function(jQuery){    jQuery.fn.highlight = function(pat, _c) {   function innerHighlight(node, pat, callback) {    var skip = 0;    if (node.nodeType == 3) {     var pos = node.data.toUpperCase().indexOf(pat);     if (pos >= 0) {      var spannode = document.createElement('span');      spannode.className = 'highlight';      var middlebit = node.splitText(pos);      var endbit = middlebit.splitText(pat.length);      var middleclone = middlebit.cloneNode(true);      spannode.appendChild(middleclone);      middlebit.parentNode.replaceChild(spannode, middlebit);      skip = 1;      var ui = {"highlighted": spannode};      jQuery(middlebit.parentNode).trigger("ui-highlighted", ui)      if (callback) {callback(ui)};     }    }    else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {     for (var i = 0; i < node.childNodes.length; ++i) {      i += innerHighlight(node.childNodes[i], pat, callback);     }    }    return skip;   }   return this.each(function() {    innerHighlight(this, pat.toUpperCase(), _c);   });  };  jQuery.fn.removeHighlight = function() {   return this.find("span.highlight").each(function() {    this.parentNode.firstChild.nodeName;    with (this.parentNode) {     replaceChild(this.firstChild, this);     normalize();    }   }).end();  };})(jQuery);