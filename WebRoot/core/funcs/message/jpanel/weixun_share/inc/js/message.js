function get_more(req)
{
   if(isUndefined(req))
   {
      if(isUndefined(data_type) || isUndefined(page))
         return;
      page++;
      
      if(typeof(ispirit)=="undefined")
         ispirit = "";
      
      if(typeof(searchkey)!="undefined")
         _get("get_more.php", "ISPIRIT= " + ispirit + "&DATA_TYPE=" + data_type + "&PAGE=" + page + "&SEARCHKEY=" + searchkey, get_more);
      else
         _get("get_more.php", "ISPIRIT= " + ispirit + "&DATA_TYPE=" + data_type + "&PAGE=" + page, get_more); 
         
   }
   else if(req.status==200)
   {
      var data = req.responseText;
      if(data != ''){
         $('content_box').innerHTML += data;
      }else{
         Message_nofocus(td_lang.inc.msg_54);//"暂无更多数据"
      }
   }
   else
   {
      alert("错误：" + req.status);
   }
}

function Message(str){
   if($('msg').style.display == "" || $('msg').style.display == "none" ){
      $('msg').innerHTML = str;
      $('msg').style.display = 'block';
      $('xoverlay').style.display = 'block';
      setTimeout(function(){
         $('msg').style.display = 'none';
         $('xoverlay').style.display = 'none';
         if($('get_more_btn')){$('get_more_btn').style.display = "none";}
         if($("wx_content")){$("wx_content").click();$("wx_content").focus();}
      },2000);
   }   
}

function Message_nofocus(str){
   if($('msg').style.display == "" || $('msg').style.display == "none" ){
      $('msg').innerHTML = str;
      $('msg').style.display = 'block';
      $('xoverlay').style.display = 'block';
      setTimeout(function(){
         $('msg').style.display = 'none';
         $('xoverlay').style.display = 'none';
         if($('get_more_btn')){$('get_more_btn').style.display = "none";}
      },2000);
   }   
}

