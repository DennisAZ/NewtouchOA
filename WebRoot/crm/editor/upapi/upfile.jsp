<%@ page contentType="text/html;charset=utf-8" language="java"
	import="java.io.*,java.text.SimpleDateFormat,java.util.Date,java.awt.Image,java.awt.image.*,com.sun.image.codec.jpeg.*,com.jspsmart.upload.*"%>

<%
	String from = request.getHeader("Referer");
	String imgid;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
    Date date=new Date();
	String url = "uploadFiles/edImgs/";
	String url2 = "uploadFiles/edImgs/s/";
    Long time= date.getTime();
	//String geShi = "yyyyMMddhhmmss";
	//String riqi = date.getBygeshi(geShi);
	String riqi = dateFormat1.format(date);
	//int maxId = 9;
	//int radok = rad.getByMaxid(maxId);
	//String newFilename = riqi + radok;
	String newFilename = riqi+time;
	String fileurl;
	//String filemulu = date.getBygeshi("yyyyMMdd");
	String filemulu = dateFormat.format(date);
	String realmulu = request.getRealPath("/") + url;
	String realmulu2 = request.getRealPath("/") + url2;
	java.io.File mulu = new java.io.File(realmulu + filemulu);
	java.io.File mulu2 = new java.io.File(realmulu2 + filemulu);
	mulu.mkdir();
	mulu2.mkdir();

	if (from.indexOf("titlepic") > 0) {
		imgid = "img";
		fileurl = "" + url2 + filemulu + "/" + newFilename + ".jpg";
	} else {
		imgid = "picture";
		fileurl = "" + url + filemulu + "/" + newFilename + ".jpg";
	}

	SmartUpload mySmartUpload = new SmartUpload();
	long file_size_max = 500*1024;//图片大小设置500k

	//初始化上传
	mySmartUpload.initialize(pageContext);

	//只允许上载此类文件
	try {
		mySmartUpload.setAllowedFilesList("jpg,Jpg,JPG,GIF,gif,Gif");
		//上载文件 
		mySmartUpload.upload();
	} catch (Exception e) {

		out
				.println("<script language=javascript>alert('上传格式错误！');   history.back(-1);</script>");

		return;

	}
	try {
		com.jspsmart.upload.File myFile = mySmartUpload.getFiles()
				.getFile(0);
		if (myFile.isMissing()) {

			out
					.println("<script language=javascript>alert('必须选择图片！');   history.back(-1);</script>");

			return;

		} else {
			int file_size = myFile.getSize(); //取得文件的大小  
			if (file_size < 0) {
				out.println("<script language=javascript>alert('上传图片大小太小！');   history.back(-1);</script>");
				return;
			}

			String saveurl = "";
			if (file_size < file_size_max) {
				saveurl = realmulu + filemulu + "/" + newFilename
						+ ".jpg"; //保存路径
				myFile.saveAs(saveurl, mySmartUpload.SAVE_PHYSICAL);

				//-----------------------上传完成，开始生成缩略图-------------------------    
				java.io.File file = new java.io.File(saveurl); //读入刚才上传的文件
				String newurl = realmulu2 + filemulu + "/"
						+ newFilename + ".jpg"; //新的缩略图保存地址
				Image src = javax.imageio.ImageIO.read(file); //构造Image对象
				float tagsize = 300;
				int old_w = src.getWidth(null); //得到源图宽
				int old_h = src.getHeight(null);
				int new_w = 0;
				int new_h = 0; //得到源图长
				//int tempsize;
				float tempdouble;
				if (old_w > old_h) {
					tempdouble = old_w / tagsize;
				} else {
					tempdouble = old_h / tagsize;
				}
				new_w = Math.round(old_w / tempdouble);
				new_h = Math.round(old_h / tempdouble);//计算新图长宽
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(src, 0, 0, new_w, new_h,
						null); //绘制缩小后的图
				FileOutputStream newimage = new FileOutputStream(newurl); //输出到文件流
				JPEGImageEncoder encoder = JPEGCodec
						.createJPEGEncoder(newimage);
				encoder.encode(tag); //近JPEG编码
				newimage.close();

				out.print("<SCRIPT language=''javascript''>");
				out.print("window.parent.document.getElementById('"
						+ imgid + "').value='" + fileurl + "';");
				out.print("window.location.href='upload.jsp';");
				out.print("</script>");

			} else {
				out.print("<SCRIPT language='javascript'>");
				out.print("alert('上传文件大小不能超过"
						+ (file_size_max / 1048576) + "M');");
				out.print("history.back(-1);");
				out.print("</SCRIPT>");
			}
		}
	} catch (Exception e) {
		e.toString();
	}
%>