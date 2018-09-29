package com.psit.struts.action;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import yh.core.global.YHSysProps;

import com.psit.struts.BIZ.AttBIZ;
import com.psit.struts.BIZ.CusServBIZ;
import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.MessageBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.ProductBIZ;
import com.psit.struts.BIZ.SalTaskBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.BIZ.WmsManageBIZ;
import com.psit.struts.entity.AttAllCus;
import com.psit.struts.entity.AttAllProd;
import com.psit.struts.entity.AttCusProd;
import com.psit.struts.entity.AttEmp;
import com.psit.struts.entity.AttMes;
import com.psit.struts.entity.AttNews;
import com.psit.struts.entity.AttOrd;
import com.psit.struts.entity.AttPra;
import com.psit.struts.entity.AttQuo;
import com.psit.struts.entity.AttRep;
import com.psit.struts.entity.AttServ;
import com.psit.struts.entity.AttTa;
import com.psit.struts.entity.AttTask;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.Message;
import com.psit.struts.entity.News;
import com.psit.struts.entity.Quote;
import com.psit.struts.entity.Report;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.entity.SalPra;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.TaLim;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.format.TransStr;

//添加附件步骤：
//1：添加一个附件entity,附件实体xml加一个subclass,外键对应实体和xml加附件set
//2：在attList方法里加 else if
//3：在upload方法里保存数据库时，添加else if
//4: 在彻底删除实体时物理删除相关附件
/**
 * 上传文件 <br>
 */
public class FileAction extends BaseDispatchAction {
	PrintWriter out;
	AttBIZ attBiz = null;
	CusServBIZ servBiz = null;
	OrderBIZ orderBiz = null;
	EmpBIZ empBiz = null;
	WmsManageBIZ wmsManageBiz = null;
	MessageBIZ messageBiz = null;
	SalTaskBIZ salTaskBiz = null;
	CustomBIZ customBiz = null;
	UserBIZ userBiz = null;
	ProductBIZ productBiz = null;
	
	/*
	public ActionForward backUp(ActionMapping mappng, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");

		// 备份文件的目录
		String backupPath = rootPath + "backup\\";

		if (!new File(backupPath).isDirectory()) {
			new File(backupPath).mkdir();
		}
		System.out.println(backupPath);
		new JdbcCon().updateSql("backup database beone to disk = '"+ backupPath + "dbback.bak" +"'");
		return null;
	}
	
	public ActionForward restore(ActionMapping mappng, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");

		// 备份文件的目录
		String backupPath = rootPath + "backup\\";

		if (!new File(backupPath).isDirectory()) {
			new File(backupPath).mkdir();
		}
		System.out.println(backupPath);
		new JdbcCon().updateSql("use master restore database beone from disk = '"+ backupPath + "dbback.bak" +"'");
		return null;
	}*/
	
	/**
	 * 跳转到客户导入页面<br>
	 * @return ActionForward 跳转到importCus
	 */
	public ActionForward toImportCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("importCus");
	}
	/**
	 * 从excel导入客户 <br>
	 * @return ActionForward 如果导入成功跳转到成功页面，否则跳转到错误页面<br>
	 * 		attribute > msg:导入成功提示文字  errorMsg:导入失败提示文字
	 */
	public ActionForward importCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String errMsg = customBiz.importCus(request);
		if(errMsg.equals("")){
			request.setAttribute("msg", "导入客户");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", errMsg);
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 跳转到上传图片页面 <br>
	 * @param request
	 *         parameter > id:各种上传图片类型所对应实体的主键 type:上传图片类型
	 * @param response
	 * @return ActionForward type值为emp或pro跳转到picUploadImg页面
	 *         type值为logo跳转到simplePicUpload页面<br>
	 *         attribute > type值为emp id:员工实体主键 path:图片路径 type:上传图片类型<br>
	 *         attribute > type值为pro id:商品实体的主键 path:图片路径 type:上传图片类型<br>
	 *         attribute > type值为logo id:部门实体主键 path:图片路径 <br>
	 */
	public ActionForward showImg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		// 员工上传照片
		if (type.equals("empPic")) {
			SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(id));
			request.setAttribute("id", salEmp.getSeNo());
			request.setAttribute("path", salEmp.getSePic());
		}
		// 商品图片
		else if (type.equals("pro")) {
			WmsProduct product = wmsManageBiz.wmsProDesc(Long.parseLong(id));
			request.setAttribute("id", product.getWprId());
			request.setAttribute("path", product.getWprPic());
		}
		// logo图片
		else if (type.equals("logo")) {
			SalOrg org = empBiz.salOrgDesc(id);
			request.setAttribute("id", org.getSoCode());
			request.setAttribute("path", org.getSoRemark());
			return mapping.findForward("simplePicUpload");
		}
		request.setAttribute("type", type);
		return mapping.findForward("picUploadImg");
	}

	/**
	 * 上传文件 <br>
	 * @param request
	 *         parameter > id:各种上传类型对应实体的主键 picPath:旧图片路径 type:上传类型(上传文件夹名)
	 * @param response
	 * @return ActionForward type值为logo或pro或emp执行showImg方法
	 *         不符合上述条件的跳转到uploadSuc页面<br>
	 *         attribute > type值为logo且上传的图片超过规定的图片大小 errorMsg:不符上传条件提示信息
	 *         			   msg:标识上传失败<br>
	 *         attribute > type值为logo或pro或emp msg:标识上传成功 picPath:上传图片的路径
	 *         			   id:各种上传类型对应实体的主键<br>
	 *         attribute > type值不等于logo，pro，emp id:各种上传类型对应实体的主键
	 */
	public ActionForward upload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		String picPath = request.getParameter("picPath");// 旧图片路径(删除用)
		String type = "";
		String savePath = "";

		// 得到上传类型(上传文件夹名)
		if (request.getParameter("type") != null) {
			type = request.getParameter("type");
		}

		int maxSize = 100 * 1024 * 1024;// 文件最大100M

		if (type.equals("empPic") || type.equals("pro") || type.equals("logo")) {
			maxSize = 5 * 1024 * 1024;// 图片最大5M
		}

		String userCode = (String) request.getSession()
				.getAttribute("userCode");

		// 以毫秒命名
		// if (code.equals("time")) {
		String code = userCode
				+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		// }
		FileOperator.createFolder2(YHSysProps.getAttachPath(), null);
		String rootPath =YHSysProps.getAttachPath()+ "/crm/";
		FileOperator.createFolder2(rootPath, null);
		rootPath+= "uploadFiles\\";
		// 创建文件夹
		FileOperator.createFolder2(rootPath, null);
		// 上传文件的目录
		String uploadPath = rootPath + type + "\\";
		FileOperator.createFolder2(uploadPath, null);

		// 临时文件目录
		String tempPath =YHSysProps.getAttachPath()+ "/crm/"
				+ "uploadFiles\\temp\\";
		
		FileOperator.createFolder2(tempPath, null);

		try {
			DiskFileUpload fu = new DiskFileUpload();
			// 转码
			fu.setHeaderEncoding("utf-8");
			// 设置最大文件尺寸
			fu.setSizeMax(maxSize);
			// 设置缓冲区大小，这里是500kb
			fu.setSizeThreshold(500 * 1024);
			// 设置临时目录：
			fu.setRepositoryPath(tempPath);

			// 得到所有的文件：
			List fileList = fu.parseRequest(request);
			Iterator i = fileList.iterator();
			int count = 0;

		

			// 依次处理每一个文件：
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				String fileName = fi.getName();
				// 获得文件后缀名
				int pathIndex = fi.getName().lastIndexOf("\\");
				if (pathIndex > 0)
					fileName = fi.getName().substring(pathIndex + 1);
				int lastIndex = fileName.lastIndexOf(".");
				// 重命名文件
				String saveName = code + fileName.substring(lastIndex);
				// 写入文件
				fi.write(new File(uploadPath + saveName));

				/*--------------- 保存数据库 ----------------------*/
				// 保存数据库的文件目录
				savePath = "uploadFiles/" + type + "/" + saveName;

				if (type.equals("logo")) {
					// -----------------------判断图片尺寸-------------------------
					File file = new File(uploadPath + saveName); // 读入刚才上传的文件
					Image src = ImageIO.read(file); // 构造Image对象
					int imgW = src.getWidth(null); // 得到源图宽
					int imgH = src.getHeight(null);// 得到源图高
					int maxW = 600;
					int maxH = 50;
					if (imgW > maxW || imgH > maxH) {
						FileOperator.delFile(savePath, request);
						request.setAttribute("errorMsg", "上传的图片宽不能超过" + maxW
								+ "px(像素)，高不能超过" + maxH + "px(像素)");
						request.setAttribute("msg", "fail");
						return showImg(mapping, form, request, response);
					}
				}

				// 订单附件
				if (type.equals("ord")) {
					AttOrd ord = new AttOrd();
					ord.setAttName(fileName);
					ord.setAttSize(fi.getSize());
					ord.setAttPath(savePath);
					// 获得上传时间
					ord.setAttDate(new Date(new Date().getTime()));
					ord.setSalOrdCon(new SalOrdCon(id));
					ord.setAttIsJunk("1");
					attBiz.save(ord);
				}
				// 报告附件
				else if (type.equals("rep")) {
					AttRep rep = new AttRep();
					rep.setAttName(fileName);
					rep.setAttSize(fi.getSize());
					rep.setAttPath(savePath);
					// 获得上传时间
					rep.setAttDate(new Date(new Date().getTime()));
					rep.setReport(new Report(Long.valueOf(id)));
					rep.setAttIsJunk("1");
					attBiz.save(rep);
				}
				// 客户服务附件
				else if (type.equals("cusServ")) {
					AttServ serv = new AttServ();
					serv.setAttName(fileName);
					serv.setAttSize(fi.getSize());
					serv.setAttPath(savePath);
					// 获得上传时间
					serv.setAttDate(new Date(new Date().getTime()));
					serv.setCusServ(new CusServ(Long.valueOf(id)));
					serv.setAttIsJunk("1");
					attBiz.save(serv);
				}
				// 消息附件
				else if (type.equals("mes")) {
					AttMes mes = new AttMes();
					mes.setAttName(fileName);
					mes.setAttSize(fi.getSize());
					mes.setAttPath(savePath);
					// 获得上传时间
					mes.setAttDate(new Date(new Date().getTime()));
					mes.setMessage(new Message(Long.valueOf(id)));
					mes.setAttIsJunk("1");
					attBiz.save(mes);
				}
				// 客户资料
				else if (type.equals("allCus")) {
					AttAllCus allCus = new AttAllCus();
					allCus.setAttName(fileName);
					allCus.setAttSize(fi.getSize());
					allCus.setAttPath(savePath);
					// 获得上传时间
					allCus.setAttDate(new Date(new Date().getTime()));
					allCus.setCusCorCus(new CusCorCus(Long.valueOf(id)));
					allCus.setAttIsJunk("1");
					attBiz.save(allCus);
				}
				// 来往记录附件
				else if (type.equals("cusPra")) {
					AttPra pra = new AttPra();
					pra.setAttName(fileName);
					pra.setAttSize(fi.getSize());
					pra.setAttPath(savePath);
					// 获得上传时间
					pra.setAttDate(new Date(new Date().getTime()));
					pra.setSalPra(new SalPra(Long.valueOf(id)));
					pra.setAttIsJunk("1");
					attBiz.save(pra);
				}
				else if (type.equals("cusProd")) {
					AttCusProd pra = new AttCusProd();
					pra.setAttName(fileName);
					pra.setAttSize(fi.getSize());
					pra.setAttPath(savePath);
					// 获得上传时间
					pra.setAttDate(new Date(new Date().getTime()));
					pra.setCusProd(new CusProd(Long.valueOf(id)));
					pra.setAttIsJunk("1");
					attBiz.save(pra);
				}
				//产品
				else if(type.equals("allProd")){
					AttAllProd prod = new AttAllProd();
					prod.setAttName(fileName);
					prod.setAttSize(fi.getSize());
					prod.setAttPath(savePath);
					//获得上传时间
					prod.setAttDate(new Date(new Date().getTime()));
					prod.setWmsProduct(new WmsProduct(id));
					prod.setAttIsJunk("1");
					attBiz.save(prod);
				}
				// 报价记录
				else if (type.equals("quo")) {
					AttQuo quo = new AttQuo();
					quo.setAttName(fileName);
					quo.setAttSize(fi.getSize());
					quo.setAttPath(savePath);
					// 获得上传时间
					quo.setAttDate(new Date(new Date().getTime()));
					quo.setQuote(new Quote(Long.valueOf(id)));
					quo.setAttIsJunk("1");
					attBiz.save(quo);
				}
				// 工作执行人
				else if (type.equals("ta")) {
					AttTa ta = new AttTa();
					ta.setAttName(fileName);
					ta.setAttSize(fi.getSize());
					ta.setAttPath(savePath);
					// 获得上传时间
					ta.setAttDate(new Date(new Date().getTime()));
					ta.setTaLim(new TaLim(Long.valueOf(id)));
					ta.setAttIsJunk("1");
					attBiz.save(ta);
				}
				// 工作
				else if (type.equals("task")) {
					AttTask task = new AttTask();
					task.setAttName(fileName);
					task.setAttSize(fi.getSize());
					task.setAttPath(savePath);
					// 获得上传时间
					task.setAttDate(new Date(new Date().getTime()));
					task.setSalTask(new SalTask(Long.valueOf(id)));
					task.setAttIsJunk("1");
					attBiz.save(task);
				}
				// 销售人员的照片
				else if (type.equals("empPic")) {
					SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(id));
					salEmp.setSePic(savePath);
					// request.setAttribute("salEmp", salEmp);
					if (picPath != null && !picPath.equals("")) {
						FileOperator.delFile(picPath, request);
					}
					empBiz.updateSalEmp(salEmp);
				}
				// 商品图片
				else if (type.equals("pro")) {
					WmsProduct product = wmsManageBiz.wmsProDesc(Long
							.parseLong(id));
					product.setWprPic(savePath);
					// request.setAttribute("salEmp", salEmp);
					if (picPath != null && !picPath.equals("")) {
						FileOperator.delFile(picPath, request);
					}
					wmsManageBiz.wmsProUpdate(product);
					// request.setAttribute("empPicPath", savePath);
				}
				// logo图片
				else if (type.equals("logo")) {
					SalOrg org = empBiz.salOrgDesc(id);
					org.setSoRemark(savePath);
					if (picPath != null && !picPath.equals("")) {
						FileOperator.delFile(picPath, request);
					}
					empBiz.updateSalOrg(org);
				}
				// 新闻公告
				else if (type.equals("news")) {
					AttNews news = new AttNews();
					news.setAttName(fileName);
					news.setAttSize(fi.getSize());
					news.setAttPath(savePath);
					// 获得上传时间
					news.setAttDate(new Date(new Date().getTime()));
					news.setNews(new News(Long.valueOf(id)));
					news.setAttIsJunk("1");
					attBiz.save(news);
				}
				else if (type.equals("emp")) {
					AttEmp attSalEmp = new AttEmp();
					attSalEmp.setAttName(fileName);
					attSalEmp.setAttSize(fi.getSize());
					attSalEmp.setAttPath(savePath);
					// 获得上传时间
					attSalEmp.setAttDate(new Date(new Date().getTime()));
					attSalEmp.setSalEmp(new SalEmp(Long.valueOf(id)));
					attSalEmp.setAttIsJunk("1");
					attBiz.save(attSalEmp);
				}
				// if (!type.equals("")) {
				// request.setAttribute("file", "uploadFiles/" + type + "/"
				// + saveName);
				// } else {
				// request.setAttribute("file", "uploadFiles/" + saveName);
				// }

				count++;
			}
			request.setAttribute("id", id);
			if (type.equals("empPic") || type.equals("pro") || type.equals("logo")) {
				request.setAttribute("msg", "suc");
				request.setAttribute("picPath", savePath);
				return showImg(mapping, form, request, response);
			}
			else{
				return mapping.findForward("uploadSuc");
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			// 请求数据的size超出了规定的大小.
			e.printStackTrace();
			request.setAttribute("errorMsg", "上传文件大小超过最大值" + maxSize / 1024
					/ 1024 + "M");
			if (type.equals("empPic") || type.equals("pro") || type.equals("logo")) {
				request.setAttribute("errorMsg", "上传的图片不能超过" + maxSize / 1024
						/ 1024 + "M");
				request.setAttribute("msg", "fail");
				return showImg(mapping, form, request, response);
			}
			return mapping.findForward("uploadFail");
		} catch (FileUploadBase.InvalidContentTypeException e) {
			// 无效的请求类型,即请求类型enctype != "multipart/form-data"
			e.printStackTrace();
			request.setAttribute("errorMsg", "无效的请求类型");
			if (type.equals("empPic") || type.equals("pro") || type.equals("logo")) {
				request.setAttribute("msg", "fail");
				return showImg(mapping, form, request, response);
			}
			return mapping.findForward("uploadFail");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", e.toString());
			if (type.equals("empPic") || type.equals("pro") || type.equals("logo")) {
				request.setAttribute("msg", "fail");
				return showImg(mapping, form, request, response);
			}
			return mapping.findForward("uploadFail");
		}

	}

	/**
	 * 显示附件列表 <br>
	 * @param request
	 *         parameter > extType:上传类型值reload表示是在详情页面上传上传成功后刷新整个页面
	 *            		   值为doc表示上传附件 type:标识不同类型 id:不同类型实体主键
	 * @param response
	 * @return ActionForward 跳转到attList页面<br>
	 *         attribute > extType:上传类型 type:标识不同类型 id:不同类型实体主键 obj:不同类型的附件列表
	 */
	public ActionForward attList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 根据登录用户判断是否在列表中屏蔽上传按钮
//		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		Long seNo = this.getPersonId(request);
//		String userNum = limUser.getUserNum();
//		Long seNo = limUser.getSalEmp().getSeNo();
		String extType = request.getParameter("extType");
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String hasIcon = request.getParameter("hasIcon");
		request.setAttribute("canDel", "1");
		// 客户服务
		if (type.equals("cusServ")) {
			CusServ serv = servBiz.showCusServ(Long.valueOf(id));
//			if (limUser.getUserIsenabled().equals("3")||(serv.getLimUser().getUserNum() != null
//					&& serv.getLimUser().getUserNum().equals(userNum))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", serv);
		}
		// 来往记录
		else if (type.equals("cusPra")) {
			SalPra salPra = servBiz.showSalPra(Long.parseLong(id));
//			if (limUser.getUserIsenabled().equals("3")||(salPra.getLimUser().getUserNum() != null
//					&& salPra.getLimUser().getUserNum().equals(userNum))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", salPra);
		}
		else if (type.equals("cusProd")) {
			CusProd cusProd = customBiz.showCusProd(Long.parseLong(id));
//			if (limUser.getUserIsenabled().equals("3")||(salPra.getLimUser().getUserNum() != null
//					&& salPra.getLimUser().getUserNum().equals(userNum))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", cusProd);
		}
		// 客户资料
		else if (type.equals("allCus")) {
			CusCorCus cusCorCus = customBiz
					.getCusCorCusInfo(id);
//			if (limUser.getUserIsenabled().equals("3")
//					||(cusCorCus.getPerson() != null
//					&& String.valueOf(cusCorCus.getPerson().getSeqId()).equals(seNo))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", cusCorCus);
		}
		// 订单
		else if (type.equals("ord")) {
			SalOrdCon ord = orderBiz.getOrdCon(id);
			if(ord!=null){
//				if (limUser.getUserIsenabled().equals("3")||(ord.getSalEmp() != null
//						&& ord.getSalEmp().getSeNo().equals(seNo))) {
//					request.setAttribute("canDel", "1");
//				}
				request.setAttribute("obj", ord);
			}
			else{
				//错误页面!!!!!!!
			}
		}
		// 报告
		else if (type.equals("rep")) {
			Report rep = messageBiz.showReportInfo(Long.valueOf(id));
//			if (limUser.getUserIsenabled().equals("3")||(rep.getSalEmp() != null
//					&& rep.getSalEmp().getSeNo().equals(seNo))) {
//				request.setAttribute("canDel", "1");
//			}
			if (hasIcon != null && hasIcon.equals("0")) {
				request.setAttribute("hasIcon", hasIcon);
			}
			request.setAttribute("obj", rep);
		}
		// 消息
		else if (type.equals("mes")) {
			Message mes = messageBiz.showMessInfo(Long.valueOf(id));
//			if (limUser.getUserIsenabled().equals("3")||(mes.getSalEmp()!= null
//					&& mes.getSalEmp().getSeNo().equals(seNo))) {
//				request.setAttribute("canDel", "1");
//			}
			if (hasIcon != null && hasIcon.equals("0")) {
				request.setAttribute("hasIcon", hasIcon);
			}
			// else{
			// //request.setAttribute("toUp", "0");
			// }
			request.setAttribute("obj", mes);
		}
		// 报价记录
		else if (type.equals("quo")) {
			Quote quo = servBiz.showQuote(Long.parseLong(id));
//			if (limUser.getUserIsenabled().equals("3")) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", quo);
		}
		// 工作执行人
		else if (type.equals("ta")) {
			TaLim ta = salTaskBiz.showTaLim(Long.parseLong(id));
//			if (limUser.getUserIsenabled().equals("3")||(ta.getSalEmp() != null
//					&& ta.getSalEmp().getSeNo().equals(seNo))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", ta);
		}
		// 工作
		else if (type.equals("task")) {
			SalTask task = salTaskBiz.salTaskDesc(Long.parseLong(id));

			if (task.getStStu().equals("1")) {
				request.setAttribute("noUp", "1");
			} else {
//				if (limUser.getUserIsenabled().equals("3")||(task.getSalEmp() != null
//						&& task.getSalEmp().getSeNo().equals(seNo))) {
//					request.setAttribute("canDel", "1");
//				}
			}
			request.setAttribute("obj", task);
		}
		// 新闻公告
		else if (type.equals("news")) {
			News news = messageBiz.showNewsInfo(Long.parseLong(id));
//			if (limUser.getUserIsenabled().equals("3")||(news.getSalEmp() != null
//					&& news.getSalEmp().getSeNo().equals(seNo))) {
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", news);
		}
		//产品
		else if(type.equals("allProd")){
			WmsProduct wmsProduct = productBiz.wmsProDesc(Long.parseLong(id));
//			if(limUser.getUserIsenabled().equals("3")){
//				request.setAttribute("canDel", "1");
//			}
			request.setAttribute("obj", wmsProduct);
		}
		//员工
		else if(type.equals("emp")){
			SalEmp salEmp=empBiz.findById(Long.parseLong(id));
			request.setAttribute("canDel", "1");
			request.setAttribute("obj", salEmp);
		}

		request.setAttribute("id", id);
		request.setAttribute("type", type);
		request.setAttribute("extType", extType);
		return mapping.findForward("attList");
	}

	/**
	 * 删除附件 <br>
	 * @param request
	 *         parameter > path:要删除的附件路径 attId:附件ID
	 * @return ActionForward 执行attList方法<br>
	 *         attribute > fromDel:删除成功后刷新页面
	 */
	public ActionForward deleteFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String relPath = request.getParameter("path");
		 relPath =YHSysProps.getAttachPath()+ "/crm/"+relPath;
		String attId = request.getParameter("attId");

		// 删除物理文件
		FileOperator.delFile2(relPath, request);
		// 清空数据库
		attBiz.delete(attBiz.findById(Long.parseLong(attId)));
		request.setAttribute("fromDel", "1");
		return attList(mapping, form, request, response);
	}

	

	/**
	 * 判断服务器文件是否被删 <br>
	 * @param request
	 *         parameter > path:文件路径
	 * @param response
	 * @return ActionForward null 如果服务器文件还存在输出1不存在输出0<br>
	 */
	public ActionForward hasFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			// 转码
			String path = TransStr.transStr(request.getParameter("path")
					.replace("/", "\\"));
			File file = new File(YHSysProps.getAttachPath()+"\\crm\\"
					+ path);
			if (file.exists()) {
				out.print("1");
			} else {
				out.print("0");
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	public void setAttBiz(AttBIZ attBiz) {
		this.attBiz = attBiz;
	}
	public void setServBiz(CusServBIZ servBiz) {
		this.servBiz = servBiz;
	}
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	public void setWmsManageBiz(WmsManageBIZ wmsManageBiz) {
		this.wmsManageBiz = wmsManageBiz;
	}
	public void setMessageBiz(MessageBIZ mesBiz) {
		this.messageBiz = mesBiz;
	}
	public void setSalTaskBiz(SalTaskBIZ salTaskBiz) {
		this.salTaskBiz = salTaskBiz;
	}
	public void setCustomBiz(CustomBIZ customBiz) {
		this.customBiz = customBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
	public void setProductBiz(ProductBIZ productBiz) {
		this.productBiz = productBiz;
	}
}
