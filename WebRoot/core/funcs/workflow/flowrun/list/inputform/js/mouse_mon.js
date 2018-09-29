var userAgent = navigator.userAgent.toLowerCase();
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);

var mouse_is_out = true;

function moue_over()
{
   mouse_is_out=false;
}
function moue_out()
{
   mouse_is_out=true;
}
function attachBody()
{
   if(is_ie)
   {
      document.body.attachEvent("onmouseover", moue_over);
      document.body.attachEvent("onmouseout", moue_out);
   }
   else
   {
      document.body.addEventListener("mouseover", moue_over, false);
      document.body.addEventListener("mouseout", moue_out, false);
   }
}

if(is_ie)
   window.attachEvent("onload", attachBody);
else
   window.addEventListener("load", attachBody, false);
