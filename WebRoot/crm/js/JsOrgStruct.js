
var JsOrgStruct={IA1:null,IM:new Array(),iHV:50,iIP:10,iIW:40,iIH:120,ab2:"<div class=\"dvItemInOrgStruct\" style=\"",fgh2:"<div class=\"dvHLineInOrgStruct\" style=\"width:",vdvInTop:"<div class=\"dvVLineInTopInOrgStruct\" style=\"",vdvInBottom:"<div class=\"dvVLineInBottomInOrgStruct\" style=\"height:",dvLinkInItemDiv:"<div style=\"display:none;height:18px;border-style:none;border-width:0;overflow-x:display;overflow-y:hidden;width:",ppzx4:0,DisplayLevel:2,ujn8:"-1",CurrentRootID:"",SH:"",ppl:"dvOrgStructList",mrpo1:0,ItemIDCurrentSelected:null,CurrentImagePath:"/Image/",$:function(id)
{return document.getElementById(id);},setTimeout:function(fRef,mDelay)
{if(typeof fRef=='function')
{var argu=Array.prototype.slice.call(arguments,2);var f=(function(){fRef.apply(null,argu);});return window.setTimeout(f,mDelay);}
return window.setTimeout(fRef,mDelay);},CreatOrgStruct:function(_ppl,_dItem,ooID1,_SH,dthy7,_blIsShowAll)
{if(typeof(document.readyState)!='undefind'&&(document.readyState=='uninitialized'||document.readyState=='loading'||document.readyState=='loaded'||document.readyState=='interactive'))
{JsOrgStruct.setTimeout(JsOrgStruct.CreatOrgStruct,100,_ppl,_dItem,ooID1,_SH,dthy7,_blIsShowAll);return false;}
else
{var op0=JsOrgStruct.IND(_ppl,_dItem,ooID1,_SH,dthy7,_blIsShowAll);if(op0!=null)
{JsOrgStruct.rym(op0);JsOrgStruct.SLP();JsOrgStruct.sicsdd(JsOrgStruct.ItemIDCurrentSelected);}}},IND:function(_ppl,_dItem,ooID1,_SH,dthy7,_blIsShowAll)
{if(JsOrgStruct.stve(_ppl,_dItem,ooID1,_SH,dthy7))
{JsOrgStruct.aoo(_dItem);var MinItem=JsOrgStruct.GMTCR(_dItem,JsOrgStruct.ItemIDCurrentSelected);if(typeof(MinItem)=="undefined")
{return null;}
var iAry=MinItem.split("|");while(JsOrgStruct.IM.length>0)
{JsOrgStruct.IM.pop();}
JsOrgStruct.TD(_dItem,iAry[1]);JsOrgStruct.sln(iAry[1],0);if(!_blIsShowAll)
{var itemMinAry=JsOrgStruct.gi(JsOrgStruct.IM,iAry[1]).split("|");var itemCurrentRootAry=JsOrgStruct.gi(JsOrgStruct.IM,JsOrgStruct.CurrentRootID).split("|");if(typeof(JsOrgStruct.DisplayLevel)!="number")
{JsOrgStruct.DisplayLevel=parseInt(JsOrgStruct.DisplayLevel);}
if(isNaN(JsOrgStruct.DisplayLevel)||JsOrgStruct.DisplayLevel<1)
{JsOrgStruct.DisplayLevel=JsOrgStruct.ppzx4+1;}
var evem3=iAry[1]==JsOrgStruct.CurrentRootID?JsOrgStruct.DisplayLevel:JsOrgStruct.DisplayLevel+Math.abs(parseInt(itemMinAry[4])-parseInt(itemCurrentRootAry[4]));if(JsOrgStruct.gcia(JsOrgStruct.IM,JsOrgStruct.ItemIDCurrentSelected).length==0)
{var itemSelected=JsOrgStruct.gi(JsOrgStruct.IM,JsOrgStruct.ItemIDCurrentSelected);if(typeof(itemSelected)!="undefined")
{var iDisplayLevel=parseInt(itemSelected.split("|")[4])+1;if(iDisplayLevel>=JsOrgStruct.DisplayLevel)
{evem3=iDisplayLevel;}}}
JsOrgStruct.sdlle(evem3);}
JsOrgStruct.scn();JsOrgStruct.sbwhh();JsOrgStruct.srwmyyou();return iAry[1];}
return null;},stve:function(_ppl,_dItem,ooID1,_SH,dthy7)
{if(typeof(_ppl)=="string"&&_ppl.length>0)
{JsOrgStruct.ppl=_ppl;}
if(JsOrgStruct.$(JsOrgStruct.ppl)==null)
{return false;}
if(typeof(ooID1)=="string")
{JsOrgStruct.CurrentRootID=ooID1;}
if(dthy7!=null)
{if(typeof(dthy7)!="number")
{dthy7=parseInt(dthy7);}
if(isNaN(dthy7))
{}
else if(dthy7<20)
{JsOrgStruct.iIW=20;}
else if(dthy7>50)
{JsOrgStruct.iIW=50;}
else
{JsOrgStruct.iIW=dthy7;}}
JsOrgStruct.SH=_SH;JsOrgStruct.IA1=_dItem;return true;},aoo:function(_dItem)
{for(var i=0;i<_dItem.length;i++)
{iAry=_dItem[i].split("|");if(typeof(JsOrgStruct.gi(_dItem,iAry[3]))=="undefined")
{JsOrgStruct.ujn8=iAry[1];break;}}},GMTCR:function(_dItem,_ItemIDCurrentSelected)
{var itemSelected=JsOrgStruct.gi(_dItem,_ItemIDCurrentSelected);var itemCurrentRoot=JsOrgStruct.gi(_dItem,JsOrgStruct.CurrentRootID);if(_ItemIDCurrentSelected==JsOrgStruct.CurrentRootID||typeof(itemSelected)=="undefined")
{return itemCurrentRoot;}
var iAry=JsOrgStruct.gpia(_ItemIDCurrentSelected,new Array(),false);iAry[iAry.length]=itemSelected;var jAry=JsOrgStruct.gpia(JsOrgStruct.CurrentRootID,new Array(),false);jAry[jAry.length]=itemCurrentRoot;if(iAry.length==0||jAry.length==0)
{return JsOrgStruct.gi(_dItem,JsOrgStruct.ujn8);}
var minCount=iAry.length>jAry.length?jAry.length:iAry.length;for(var i=0;i<minCount;i++)
{if(iAry[i].split("|")[1]!=jAry[i].split("|")[1])
{return iAry[i-1];}}
return iAry[minCount-1];},TD:function(_dItem,id)
{var item=JsOrgStruct.gi(_dItem,id);if(item!="undefined"&&item!=null)
{JsOrgStruct.IM[JsOrgStruct.IM.length]=item;}
var iAry=new Array();for(var i=0;i<_dItem.length;i++)
{iAry=_dItem[i].split("|");if(iAry[3]==id)
{JsOrgStruct.TD(_dItem,iAry[1]);}}},sln:function(pid,ni)
{var n=ni+1;var iAry=new Array();for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(iAry[1]==pid)
{JsOrgStruct.IM[i]=JsOrgStruct.IM[i]+ni;if(JsOrgStruct.ppzx4<ni)
{JsOrgStruct.ppzx4=ni;}
var childAry=JsOrgStruct.gcia(JsOrgStruct.IM,pid);for(var j=0;j<childAry.length;j++)
{JsOrgStruct.sln(childAry[j].split("|")[1],n);}}}},sdlle:function(evem3)
{var iCount=0;var iAry=new Array();for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(iAry.length<5||isNaN(parseInt(iAry[4]))||parseInt(iAry[4])>evem3-1)
{iCount++;}}
while(iCount>0)
{for(var j=0;j<JsOrgStruct.IM.length;j++)
{iAry=JsOrgStruct.IM[j].split("|");if(iAry.length<5||isNaN(parseInt(iAry[4]))||parseInt(iAry[4])>evem3-1)
{JsOrgStruct.IM.splice(j,1);JsOrgStruct.sdlle();break;}}
iCount--;}},scn:function()
{var iAry=new Array();for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");JsOrgStruct.IM[i]=JsOrgStruct.IM[i]+"|"+JsOrgStruct.GCN(iAry[1]);}},sbwhh:function()
{var iAry=new Array();for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(parseInt(iAry[4])==JsOrgStruct.ppzx4||JsOrgStruct.GCN(iAry[1])==0)
{JsOrgStruct.IM[i]=JsOrgStruct.IM[i]+"|1";}
else
{JsOrgStruct.IM[i]=JsOrgStruct.IM[i]+"|0";}}},GCN:function(pid)
{var i;var rnum=0;var iAry=new Array();for(i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(iAry[3]==pid)
{rnum=rnum+1;}}
return rnum;},srwmyyou:function()
{var iAry=new Array();var jAry=new Array();var childCount;for(var depth=JsOrgStruct.ppzx4;depth>=0;depth--)
{for(var i=0;i<JsOrgStruct.IM.length;i++)
{childCount=0;iAry=JsOrgStruct.IM[i].split("|");if(parseInt(iAry[4])!=depth)
{continue;}
var childItemArray=JsOrgStruct.gcia(JsOrgStruct.IM,iAry[1]);for(var j=0;j<childItemArray.length;j++)
{jAry=childItemArray[j].split("|");childCount+=parseInt(jAry[6]);}
if(childItemArray.length>0)
{JsOrgStruct.IM[i]=iAry[0]+"|"+iAry[1]+"|"+iAry[2]+"|"+iAry[3]+"|"+iAry[4]+"|"+iAry[5]+"|"+childCount;}}}},sicsdd:function(ItemID)
{if(typeof(ItemID)!="string")
{return;}
var opl5=JsOrgStruct.$(JsOrgStruct.ppl);if(opl5==null||ItemID==JsOrgStruct.ujn8)
{return;}
var item=JsOrgStruct.gi(JsOrgStruct.IM,ItemID);if(typeof(item)=="undefined")
{return;}
JsOrgStruct.ccdd(opl5,"PathVLineInOrgStruct"+"|"+"PathHLineInOrgStruct");JsOrgStruct.ItemIDCurrentSelected=ItemID;var pAry=JsOrgStruct.gpia(ItemID,new Array(),true);var iAry=new Array();for(var i=0;i<pAry.length;i++)
{var d11pop=null;var d12pom=null;iAry=pAry[i].split("|");var jra6l=iAry[1];var objDvLineOnBottom=JsOrgStruct.$("itemVLineInBottom_"+iAry[1]);if(objDvLineOnBottom!=null)
{d12pom=document.createElement("DIV");opl5.appendChild(d12pom);d12pom.id="dvVPathLineOnBottomInOrgStruct_"+iAry[1];d12pom.className="PathVLineInOrgStruct";d12pom.style.left=objDvLineOnBottom.style.left;d12pom.style.top=objDvLineOnBottom.style.top;d12pom.style.height=objDvLineOnBottom.offsetHeight;d12pom.style.width=parseInt(objDvLineOnBottom.offsetWidth)*2+"px";}
if(i==pAry.length-1)
{iAry=item.split("|");}
else
{iAry=pAry[i+1].split("|");}
var objDvLineInTop=JsOrgStruct.$("itemVLineInTop_"+iAry[1]);if(objDvLineInTop!=null)
{d11pop=document.createElement("DIV");opl5.appendChild(d11pop);d11pop.id="dvVPathLineInTopInOrgStruct_"+iAry[1];d11pop.className="PathVLineInOrgStruct";d11pop.style.left=objDvLineInTop.style.left;d11pop.style.top=objDvLineInTop.style.top;d11pop.style.height=objDvLineInTop.offsetHeight;d11pop.style.width=parseInt(objDvLineInTop.offsetWidth)*2+"px";}
if(d11pop!=null&&d12pom!=null)
{var j56=document.createElement("DIV");opl5.appendChild(j56);j56.id="dvHPathLineInTopInOrgStruct_"+jra6l;j56.className="PathHLineInOrgStruct";j56.style.left=parseInt(d11pop.style.left)>parseInt(d12pom.style.left)?d12pom.style.left:d11pop.style.left;j56.style.top=parseInt(d11pop.style.top)-1+"px";var hlHeight=Math.abs(parseFloat(d11pop.style.top)-parseFloat(d12pom.style.top)-parseFloat(d12pom.offsetHeight));if(hlHeight<1)
{hlHeight=1;}
j56.style.borderTopWidth=parseInt(hlHeight)*3+"px";j56.style.width=Math.abs(parseFloat(d11pop.style.left)-parseFloat(d12pom.style.left))+parseInt(objDvLineOnBottom.offsetWidth)*2+"px";}}
var objDvItem=JsOrgStruct.$("itemDiv_"+iAry[1]);if(objDvItem!=null)
{objDvItem.style.borderColor="red";objDvItem.style.borderWidth="2px";}},ccdd:function(opl5,className)
{var cnAry=className.split("|");var dvAry=opl5.getElementsByTagName("DIV");var dvCount=0;for(var i=0;i<dvAry.length;i++)
{for(var j=0;j<cnAry.length;j++)
{if(dvAry[i].className==cnAry[j])
{dvCount++;}}}
for(;dvCount>0;dvCount--)
{dvAry=opl5.getElementsByTagName("DIV");for(var h=0;h<dvAry.length;h++)
{for(var k=0;k<cnAry.length;k++)
{if(typeof(dvAry[h])!="undefined"&&dvAry[h].className==cnAry[k])
{dvAry[h].parentNode.removeChild(dvAry[h]);}}}}},gp:function(Gobj)
{var objLeft=Gobj.offsetLeft;var objTop=Gobj.offsetTop;var objParent=Gobj.offsetParent;while(objParent!=null)
{objLeft+=objParent.offsetLeft;objTop+=objParent.offsetTop;objParent=objParent.offsetParent;}
return([objLeft,objTop]);},rym:function(op0)
{var opl5=JsOrgStruct.$(JsOrgStruct.ppl);if(opl5==null)
{return;}
opl5.innerHTML="";opl5.style.textAlign="left";opl5.style.position="static";opl5.style.padding="10px";opl5.style.display="block";var opl7=JsOrgStruct.$("PanelDiv_ModelInTop");if(opl7==null)
{opl7=document.createElement("DIV");document.body.appendChild(opl7);opl7.id="PanelDiv_ModelInTop"}
else
{opl7.innerHTML="";}
opl7.style.display="block";opl7.innerHTML=JsOrgStruct.ab2+"width:"+JsOrgStruct.iIW+"px;top:0px;\"id=\"itemDiv_ModelInTop"+"\"></div>";var ItemVLineInTop_ForPosition="itemVLineInTop_"+op0;temdvHtml=JsOrgStruct.vdvInTop+"position:relative;height:1px;top:0px;display:block;visibility:hidden;\" id=\""+ItemVLineInTop_ForPosition+"\"></div>";opl5.innerHTML+=temdvHtml;var iAry=new Array();var mlineo=10000;var maxRightPosition=0;for(var iLevel_Num=0;iLevel_Num<=JsOrgStruct.ppzx4;iLevel_Num++)
{for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(parseInt(iAry[4])!=iLevel_Num)
{continue;}
var objItemDv=JsOrgStruct.$("itemDiv_"+op0);if(objItemDv==null)
{if(iAry[1]!=op0)
{continue;}}
var oosiv=JsOrgStruct.$("itemVLineInTop_"+iAry[1]);if(oosiv==null)
{continue;}
var ona=JsOrgStruct.gp(oosiv);ona[1]=ona[1]+oosiv.offsetHeight;JsOrgStruct.cialdb(opl5,op0,iAry[1],ona,mlineo-25);var dvVLineInBottom=JsOrgStruct.$("itemVLineInBottom_"+iAry[1]);if(dvVLineInBottom!=null)
{var treni=JsOrgStruct.gse(dvVLineInBottom,"height");if(treni!="auto")
{var mlineoTemp=parseInt(treni);if(mlineo>mlineoTemp)
{mlineo=mlineoTemp;}}}
var dvItem=JsOrgStruct.$("itemDiv_"+iAry[1]);if(dvItem!=null)
{var trefta=JsOrgStruct.gse(dvItem,"left");if(trefta!="auto")
{var maxRightPositionTemp=parseInt(trefta)+dvItem.offsetWidth;if(maxRightPosition<maxRightPositionTemp)
{maxRightPosition=maxRightPositionTemp;}}}}
for(var i=0;i<JsOrgStruct.IM.length;i++)
{iAry=JsOrgStruct.IM[i].split("|");if(parseInt(iAry[4])!=iLevel_Num)
{continue;}
var dvVLineInBottom=JsOrgStruct.$("itemVLineInBottom_"+iAry[1]);if(dvVLineInBottom!=null)
{var treni=JsOrgStruct.gse(dvVLineInBottom,"height");if(treni=="auto")
{continue;}
var mlineoTemp=parseInt(treni)-mlineo+25;dvVLineInBottom.style.height=mlineoTemp+"px";}}}
JsOrgStruct.mrpo1=maxRightPosition;opl7.style.display="none";},cialdb:function(opl5,op0,ItemID,ona,heightDeviant)
{var objItemDiv_ModelInTop=JsOrgStruct.$("itemDiv_ModelInTop");var ItemDvWidth=JsOrgStruct.iIW;if(objItemDiv_ModelInTop.offsetWidth>=ItemDvWidth)
{ItemDvWidth-=objItemDiv_ModelInTop.offsetWidth-ItemDvWidth;}
var currentItem=JsOrgStruct.gi(JsOrgStruct.IM,ItemID);var iAry=currentItem.split("|");var txtInDvItem="<a href=\"#\" onclick=\"";if(JsOrgStruct.SH!=null&&JsOrgStruct.SH.length>0)
{txtInDvItem+=JsOrgStruct.SH+"('"+iAry[0]+"|"+iAry[1]+"|"+iAry[2]+"|"+iAry[3]+"',";txtInDvItem+="JsOrgStruct.gpia('";txtInDvItem+=iAry[1];txtInDvItem+="',new Array(),false)"
txtInDvItem+=");";}
txtInDvItem+="return false;\" >"+iAry[2]+"</a>";txtInDvItem+=JsOrgStruct.ghliidfa(currentItem);var lItem=ona[0]-ItemDvWidth/2;var tItem=ona[1];var jsTxt="onmouseover=\"var obj = JsOrgStruct.$('DvLinkInItemDiv_"+iAry[1]+"');if ( obj!=null){obj.style.display='block';} \" onmouseout=\"var obj = JsOrgStruct.$('DvLinkInItemDiv_"+iAry[1]+"');if ( obj!=null){obj.style.display='none';} \"";var temdvHtml=JsOrgStruct.ab2+"width:"+ItemDvWidth+"px;left:"+lItem+"px;top:"+tItem+"px;"+"\"id=\"itemDiv_"+iAry[1]+"\" "+jsTxt+">"+txtInDvItem+"</div>";opl5.innerHTML+=temdvHtml;var objItemDv=JsOrgStruct.$("itemDiv_"+iAry[1]);ona=JsOrgStruct.gp(objItemDv);var intOffsetHeightAndItemHeight=objItemDv.offsetHeight-JsOrgStruct.iIH;var fftta=JsOrgStruct.iHV-intOffsetHeightAndItemHeight;var dvLineHtml="";var cldm=parseInt(iAry[5]);if(cldm>0)
{var childItemArray=JsOrgStruct.gcia(JsOrgStruct.IM,iAry[1]);var iHLineWidth=parseInt(iAry[6])-parseInt(childItemArray[0].split("|")[6])/2-parseInt(childItemArray[childItemArray.length-1].split("|")[6])/2;var hline_width=iHLineWidth*(JsOrgStruct.iIW+JsOrgStruct.iIP);var l1=ona[0]+ItemDvWidth/2;var t1=ona[1]+JsOrgStruct.iIH+intOffsetHeightAndItemHeight;opl5.innerHTML+=JsOrgStruct.vdvInBottom+fftta+"px;left:"+l1+"px;top:"+t1+"\" id=\"itemVLineInBottom_"+iAry[1]+"\"></div>";var objDvVLineInBottom=JsOrgStruct.$("itemVLineInBottom_"+iAry[1]);var l2=l1-(parseInt(iAry[6])/2-parseInt(childItemArray[0].split("|")[6])/2)*(JsOrgStruct.iIW+JsOrgStruct.iIP);var t2=t1+fftta;if(ItemID==op0)
{t2-=fftta;t2+=25;}
else
{t2-=heightDeviant;}
if(cldm>1)
{dvLineHtml+=JsOrgStruct.fgh2+hline_width+"px;left:"+l2+"px;top:"+t2+"\" id=\"itemHLineInBottom_"+iAry[1]+"\"></div>";}
var iAryChild=new Array();var iumd=0;var iumdent=parseInt(iAry[6]);for(var j=0;j<cldm;j++)
{iAryChild=childItemArray[j].split("|");var t3;var l3;t3=t2;if(j==0||j==cldm-1)
{iumd+=parseInt(iAryChild[6])/2;}
else
{iumd+=parseInt(iAryChild[6]);}
if(j==cldm-1)
{l3=l2+hline_width;}
else
{var cwn=iumd-parseInt(iAryChild[6])/2;l3=l2+cwn*(JsOrgStruct.iIW+JsOrgStruct.iIP);}
var tmpline=JsOrgStruct.vdvInTop+"left:"+l3+"px;top:"+t3+"\" id=\"itemVLineInTop_"+iAryChild[1]+"\"></div>";dvLineHtml+=tmpline;}
opl5.innerHTML+=dvLineHtml;}},ghliidfa:function(item)
{var strLink="";var iAry=item.split("|");var blTop=false;var blPre=false;var blNext=false;var blAll=false;if(iAry[1]==JsOrgStruct.CurrentRootID&&JsOrgStruct.ujn8!=JsOrgStruct.CurrentRootID)
{blTop=true;blPre=true;}
if(JsOrgStruct.gcia(JsOrgStruct.IA1,iAry[1]).length>0)
{if(JsOrgStruct.icidl(item))
{blAll=true;}
if(iAry[1]!=JsOrgStruct.CurrentRootID)
{if(parseInt(iAry[4])==JsOrgStruct.DisplayLevel-1)
{blNext=true;}
else
{blAll=true;}}}
if(iAry[1]==JsOrgStruct.CurrentRootID)
{if(JsOrgStruct.ixcind(item))
{blAll=true;}
else
{blAll=false;}}
var iWidthDvLink=3;if(blTop)
{strLink+="<a href=\"#\" onclick=\"";strLink+="JsOrgStruct.CreatOrgStruct(JsOrgStruct.ppl,JsOrgStruct.IA1,JsOrgStruct.ujn8,JsOrgStruct.SH,JsOrgStruct.iIW,false);";strLink+="return false;\" title=\"返回顶部\"><img class=\"imgInOrgStruct\" src=\"";strLink+=JsOrgStruct.CurrentImagePath;strLink+="first_16x16.gif\" /></a>";iWidthDvLink+=16;}
if(blPre)
{strLink+="<a href=\"#\" onclick=\"";strLink+="JsOrgStruct.CreatOrgStruct(JsOrgStruct.ppl,JsOrgStruct.IA1,'";strLink+=iAry[3];strLink+="',JsOrgStruct.SH,JsOrgStruct.iIW,false);";strLink+="return false;\" title=\"上一级\"><img class=\"imgInOrgStruct\" src=\"";strLink+=JsOrgStruct.CurrentImagePath;strLink+="level_up_16x16.gif\" /></a>";iWidthDvLink+=16;}
if(blNext)
{strLink+="<a href=\"#\" onclick=\"";strLink+="JsOrgStruct.CreatOrgStruct(JsOrgStruct.ppl,JsOrgStruct.IA1,'";strLink+=iAry[1];strLink+="',JsOrgStruct.SH,JsOrgStruct.iIW,false);";strLink+="return false;\" title=\"下一级\"><img class=\"imgInOrgStruct\" src=\"";strLink+=JsOrgStruct.CurrentImagePath;strLink+="level_down_16x16.gif\" /></a>";iWidthDvLink+=16;}
if(blAll)
{strLink+="<a href=\"#\" onclick=\"";strLink+="JsOrgStruct.CreatOrgStruct(JsOrgStruct.ppl,JsOrgStruct.IA1,'";strLink+=iAry[1];strLink+="',JsOrgStruct.SH,JsOrgStruct.iIW,true);";strLink+="return false;\" title=\"查看所有下级\"><img class=\"imgInOrgStruct\" src=\"";strLink+=JsOrgStruct.CurrentImagePath;strLink+="zoom_in_16x16.gif\" /></a>";iWidthDvLink+=16;}
if(blTop||blPre||blNext||blAll)
{strLink=JsOrgStruct.dvLinkInItemDiv+iWidthDvLink+"px;\" id=\"DvLinkInItemDiv_"+iAry[1]+"\">"+strLink;strLink+="</div>";}
return strLink;},icidl:function(_item)
{var blReturn=false;var iAry=_item.split("|");var childAry=null;if(parseInt(iAry[4])<JsOrgStruct.DisplayLevel-1)
{childAry=JsOrgStruct.gcia(JsOrgStruct.IM,iAry[1]);for(var i=0;i<childAry.length;i++)
{blReturn=JsOrgStruct.icidl(childAry[i]);if(blReturn)
{break;}}}
else
{childAry=JsOrgStruct.gcia(JsOrgStruct.IA1,iAry[1]);if(childAry.length>0)
{blReturn=true;}}
return blReturn;},ixcind:function(_item)
{var blReturn=false;var iAry=_item.split("|");var childAry=JsOrgStruct.gcia(JsOrgStruct.IM,iAry[1]);for(var i=0;i<childAry.length;i++)
{blReturn=JsOrgStruct.ixcind(childAry[i]);if(blReturn)
{return blReturn;}}
if(childAry.length<JsOrgStruct.gcia(JsOrgStruct.IA1,iAry[1]).length)
{blReturn=true;}
return blReturn;},gi:function(_dItem,id)
{var i;var item;var iAry=new Array();for(i=0;i<_dItem.length;i++)
{iAry=_dItem[i].split("|");if(iAry[1]==id)
{item=_dItem[i];break;}}
return item;},gpia:function(id,parentAry,blIsCurrentRoot)
{if(id==JsOrgStruct.ujn8)
{return parentAry;}
var item=JsOrgStruct.gi(JsOrgStruct.IA1,id);if(typeof(item)=="undefined")
{return parentAry;}
var i;var iAry=new Array();for(i=0;i<JsOrgStruct.IA1.length;i++)
{iAry=JsOrgStruct.IA1[i].split("|");if(iAry[1]==item.split("|")[3])
{if(blIsCurrentRoot&&iAry[1]==JsOrgStruct.CurrentRootID)
{parentAry.push(JsOrgStruct.IA1[i]);}
else
{parentAry=JsOrgStruct.gpia(iAry[1],parentAry,blIsCurrentRoot);parentAry.push(JsOrgStruct.IA1[i]);}
break;}}
return parentAry;},gcia:function(_dItem,pid)
{var i;var rnum=0;var iAry=new Array();for(i=0;i<_dItem.length;i++)
{if(_dItem[i].split("|")[3]==pid)
{iAry[rnum]=_dItem[i];rnum=rnum+1;}}
return iAry;},gse:function(oElm,strCssRule)
{var strValue="";if(document.defaultView&&document.defaultView.getComputedStyle)
{strValue=document.defaultView.getComputedStyle(oElm,"").getPropertyValue(strCssRule);}
else if(oElm.currentStyle)
{strCssRule=strCssRule.replace(/\-(\w)/g,function(strMatch,p1){return p1.toUpperCase();});strValue=oElm.currentStyle[strCssRule];}
return strValue;},SLP:function()
{var minLeft=0;var opl5=JsOrgStruct.$(JsOrgStruct.ppl);var dvAry=opl5.getElementsByTagName("DIV");for(var i=0;i<dvAry.length;i++)
{var strLeft=JsOrgStruct.gse(dvAry[i],"left");if(strLeft=="auto")
{continue;}
var iLeft=parseInt(strLeft);if(minLeft>iLeft)
{minLeft=iLeft;}}
var iWidthOrgStruct=JsOrgStruct.mrpo1-minLeft;var RightDeviant=JsOrgStruct.getPageSize()[0]-iWidthOrgStruct;if(RightDeviant>0)
{RightDeviant=RightDeviant/2;}
else
{RightDeviant=0;}
for(var i=0;i<dvAry.length;i++)
{var strLeft=JsOrgStruct.gse(dvAry[i],"left");if(strLeft=="auto")
{continue;}
var l=parseInt(strLeft)-minLeft+RightDeviant;dvAry[i].style.left=l+"px";}},jabry:function()
{if(navigator.userAgent.search("Opera")>-1)
{return"Opera";}
if(navigator.userAgent.indexOf("Mozilla/5.")>-1)
{return"Firefox";}
if(navigator.userAgent.search("MSIE")>0)
{return"IE";}},getPageSize:function()
{var xScroll,yScroll;if(window.innerHeight&&window.scrollMaxY){xScroll=document.body.scrollWidth;yScroll=window.innerHeight+window.scrollMaxY;}else if(document.body.scrollHeight>document.body.offsetHeight){xScroll=document.body.scrollWidth;yScroll=document.body.scrollHeight;}else{xScroll=document.body.offsetWidth;yScroll=document.body.offsetHeight;}
var windowWidth,windowHeight;if(self.innerHeight){windowWidth=self.innerWidth;windowHeight=self.innerHeight;}else if(document.documentElement&&document.documentElement.clientHeight){windowWidth=document.documentElement.clientWidth;windowHeight=document.documentElement.clientHeight;}else if(document.body){windowWidth=document.body.clientWidth;windowHeight=document.body.clientHeight;}
if(yScroll<windowHeight){pageHeight=windowHeight;}else{pageHeight=yScroll;}
if(xScroll<windowWidth){pageWidth=windowWidth;}else{pageWidth=xScroll;}
arrayPageSize=new Array(pageWidth,pageHeight,windowWidth,windowHeight)
return arrayPageSize;}};