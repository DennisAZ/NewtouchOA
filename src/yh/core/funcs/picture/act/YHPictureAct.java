package yh.core.funcs.picture.act;

import java.awt.Image;
import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sanselan.Sanselan;

import yh.core.data.YHMapComparator;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.data.YHPicture;
import yh.core.funcs.picture.logic.YHPictureLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;

public class YHPictureAct {

	/**
	 * 获取图片目录信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPicFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHPictureLogic logic = new YHPictureLogic();
		StringBuffer sb = new StringBuffer();

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		// boolean userFlag = false;
		// boolean roleFlag = false;
		// boolean deptFlag = false;

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean isHave = false;


			List<YHPicture> list = logic.getPicFolderInfo(dbConn);
			if (list.size() > 0) {
				sb.append("[");
				for (YHPicture picture : list) {
					Map map = new HashMap();
					map.put("SEQ_ID", picture.getSeqId());

					String toDdeptIdStr = "";
					String toPrivIdStr = "";
					String toUserIdStr = "";
					if (!"".equals(picture.getToDeptId()) && picture.getToDeptId() != null) {
						toDdeptIdStr = picture.getToDeptId();
					}
					if (!"".equals(picture.getToPrivId()) && picture.getToPrivId() != null) {
						toPrivIdStr = picture.getToPrivId();
					}
					if (!"".equals(picture.getToUserId()) && picture.getToUserId() != null) {
						toUserIdStr = picture.getToUserId();
					}

					boolean toDeptIdFlag = logic.getDeptIdPriv(loginUserDeptId, toDdeptIdStr);
					boolean toPrivIdFlag = logic.getPrivate(Integer.parseInt(loginUserRoleId), toPrivIdStr);
					boolean toUserIdFlag = logic.getPrivate(loginUserSeqId, toUserIdStr);

					if (toDeptIdFlag || toPrivIdFlag || toUserIdFlag) {
						sb.append("{");
						sb.append("seqId:\"" + picture.getSeqId() + "\"");
						sb.append(",picName:\"" + YHUtility.encodeSpecial(picture.getPicName()) + "\"");
						sb.append(",picPath:\"" + picture.getPicPath() + "\"");
						sb.append(",toDeptId:\"" + picture.getToDeptId() + "\"");
						sb.append(",toPrivId:\"" + picture.getToPrivId() + "\"");
						sb.append(",toUserId:\"" + picture.getToUserId() + "\"");
						sb.append(",privStr:\"" + picture.getPrivStr() + "\"");
						sb.append(",delPrivStr:\"" + picture.getDelPrivStr() + "\"");
						sb.append("},");
						isHave = true;
					}
				}
				if (isHave) {
					sb.deleteCharAt(sb.length() - 1);
				}

				sb.append("]");

			} else {
				sb.append("[]");
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 通过id获取目录下的所有文件信息
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPicInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String currNoStr = request.getParameter("currNo"); // 当前的页码
		String picIdStr = request.getParameter("seqId");
		String subDir = request.getParameter("subDir"); // 子文件夹路径 cc/aa/aa

		String orderBy = request.getParameter("field");
		String ascDesc = request.getParameter("ascDescFlag");

		if (subDir == null) {
			subDir = "";
		}
		if (subDir.trim().startsWith("/")) {
			subDir = subDir.trim().substring(subDir.indexOf("/") + 1, subDir.length());
		}
		if (YHUtility.isNullorEmpty(orderBy)) {
			orderBy = "NAME";
		}
		if (YHUtility.isNullorEmpty(ascDesc)) {
		  if (orderBy.equalsIgnoreCase("TIME")) {
		    ascDesc = "DESC";
		  }else {
		    ascDesc = "ASC";
		  }
		}

		int picId = 0;
		if (picIdStr != null && !"".equals(picIdStr.trim())) {
			picId = Integer.parseInt(picIdStr);
		}

		int currNo = 1;
		if (YHStringUtil.isEmpty(currNoStr)) {
			currNo = 1;
		} else {
			currNo = Integer.parseInt(currNoStr);
		}

		YHPictureLogic logic = new YHPictureLogic();
		Map map = new HashMap();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		YHPicture picture = new YHPicture();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			map.put("SEQ_ID", picId);
			picture = logic.getPicFolderInfoById(dbConn, map);

			String showDir = ""; // 显示当前目录路径
			String dirPath = ""; // 文件夹目录路径
			if (picture != null) {
				dirPath = picture.getPicPath() + "/" + subDir;
				showDir = picture.getPicName() + "/" + subDir;

				if (showDir.endsWith("/")) {
					showDir = showDir.substring(0, showDir.lastIndexOf("/"));
				}
			}

			String noFolderFlag = "";

			String isPath = "";
			String isImage = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";

			if (dirPath != null && !"".equals(dirPath.trim())) {
				File file = new File(dirPath);
				if (file != null && file.exists()) {
					File[] files = file.listFiles();
					if (files != null && files.length > 0) {
						for (File f : files) {
							if (f.isDirectory()) {
								isPath = "isDir";
							} else {
								String fileNameStr = f.getName();
								String lastName = "";
								if (fileNameStr.lastIndexOf(".") != -1) {
									lastName = fileNameStr.substring(fileNameStr.lastIndexOf("."));
								}
								if (lastName.matches(isImage)) {
									isPath = "isImage";
								} else {
									isPath = "isFile";
								}
							}

							String fileType = YHFileUtility.getFileExtName(f.getAbsolutePath());

							Map m = new HashMap();
							long fileSize = 0;
							if (!"isDir".equals(isPath)) {
								fileSize = f.length();
							}

							if (!"tdoa_cache".equals(f.getName())) {
								m.put("picName", f.getName());
								m.put("seqId", String.valueOf(picId));
								m.put("picPath", file.getPath());
								m.put("isPath", isPath);
								m.put("lastModify", YHUtility.getDateTimeStr(new Date(f.lastModified())));
								m.put("length", String.valueOf(fileSize));
								m.put("fileType", fileType);
								list.add(m);
							}

						}

					}

				} else {
					noFolderFlag = "noFolder";
				}

			} else {
				noFolderFlag = "noFolder";
			}

			if (list != null && list.size() > 0) {

				if ("NAME".equals(orderBy.trim())) {

					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "picName", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "picName", YHMapComparator.TYPE_STR);
					}

				} else if ("SIZE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "length", YHMapComparator.TYPE_LONG);
					} else {
						YHUtility.sortAsc(list, "length", YHMapComparator.TYPE_LONG);
					}
				} else if ("TYPE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileType", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileType", YHMapComparator.TYPE_STR);
					}
				} else if ("TIME".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "lastModify", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "lastModify", YHMapComparator.TYPE_STR);
					}
				}

			}
			
			long count = list.size();
			int pageSize = 35;// 一个页面显示的数目
			YHPage page = new YHPage(pageSize, count, currNo);
			long first = page.getFirstResult();
			long last = page.getLastResult();
			List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
			for (int i = (int) first; i < last; i++) {
				Map<String, String> map2 = new HashMap<String, String>();
				map2 = list.get(i);
				String nameString = map2.get("picName");
				String picPathStr = map2.get("picPath");

				String fileSize = "";

				String fileLeng = (String) map2.get("length");
				fileSize = logic.transformSize(Long.parseLong(fileLeng));

				map2.put("length", fileSize);

				if (nameString.matches(isImage)) {
					// String path = picPathStr  + File.separator + nameString;
					boolean flagStr = logic.createCache(picPathStr, nameString);
				}
				list2.add(map2);
			}
			request.setAttribute("picList", list2);
			request.setAttribute("page", page);
			request.setAttribute("showDir", showDir);
			request.setAttribute("subDir", subDir);
			request.setAttribute("noFolderFlag", noFolderFlag);
			request.setAttribute("picFilePath", dirPath);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/funcs/picture/picture.jsp?seqId=" + picId + "&ascDescFlag=" + ascDesc + "&field=" + orderBy;
	}
	
/**
 * 取得图片的大小
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getPicDimension(HttpServletRequest request, HttpServletResponse response) throws Exception {
  String fileNameSrver = request.getParameter(YHActionKeys.UPLOAD_FILE_NAME_SERVER);
  String filePath = fileNameSrver.replace("/", "\\");
  if (filePath.indexOf(":") != 1) {
    filePath = YHSysProps.getWebPath() + fileNameSrver.replace("/", "\\");
  }
  try {
    File newFile = new File(filePath);
    Image imgSrc = null; // 读入文件,构造Image对象
    try {
      imgSrc = ImageIO.read(newFile);
    } catch (Exception ex) {
      imgSrc = Sanselan.getBufferedImage(newFile);
    }
    int width = imgSrc.getWidth(null); // 得到源图宽
    int height = imgSrc.getHeight(null);
	  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    request.setAttribute(YHActionKeys.RET_DATA, "{width:" + String.valueOf(width) + ", height:" + String.valueOf(height) + "}");
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }

  return "/core/inc/rtjson.jsp";
}

	/**
	 * 获取显示单张图片信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String showPicInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHPictureLogic logic = new YHPictureLogic();
		String subDir = request.getParameter("fileDir");
		String orderBy = request.getParameter("viewType");
		String ascDesc = request.getParameter("ascDesc");
		String seqIdStr = request.getParameter("seqId");

		if (subDir == null) {
			subDir = "";
		}

		if (orderBy == null) {
			orderBy = "";
		}
		if (ascDesc == null) {
			ascDesc = "";
		}
		
		int seqId=0;
		if (seqIdStr!=null) {
			seqId=Integer.parseInt(seqIdStr);
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			String picPath = "";
			if (picture!=null) {
				picPath = picture.getPicPath() + "/" + subDir;
			}

			StringBuffer sb = new StringBuffer("[");
			String isImage = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";
			// String
			// isImage="^.*?(\\.(gif|jpg|png|swf|swc|tiff|bmp|iff|jp2|jpx|jb2|jpc|xbm|wbmp))$";
			String isPath = "";
			boolean flag = false;
			if (picPath != null && picPath.trim().length() > 0) {
				File file = new File(picPath.trim());
				if (file.exists()) {
					File[] files = file.listFiles();

					for (File f : files) {
						if (f.isDirectory()) {
							isPath = "isDir";
						} else {
							isPath = "isFile";
						}
						Map m = new HashMap();
						if (!"tdoa_cache".equals(f.getName())) {
							if (f.getName().matches(isImage)) {
//								String filePath = f.getPath();
//								File newFile = new File(filePath);
//								Image imgSrc = null; // 读入文件,构造Image对象
//								try {
//									imgSrc = ImageIO.read(newFile);
//								} catch (Exception ex) {
//									imgSrc = Sanselan.getBufferedImage(newFile);
//								}
//								int width = imgSrc.getWidth(null); // 得到源图宽
//								int height = imgSrc.getHeight(null);
//
								long fileSize = 0;
								if (!"isDir".equals(isPath)) {
									fileSize = f.length();
								}

								String fileType = YHFileUtility.getFileExtName(f.getAbsolutePath());

								m.put("picName", f.getName());
								m.put("fileSize", String.valueOf(fileSize));
								m.put("fileTime", YHUtility.getDateTimeStr(new Date(f.lastModified())));
								m.put("imgWidth", String.valueOf(0));
								m.put("imgHeight", String.valueOf(0));
								m.put("fileType", fileType);
								list.add(m);
							}
						}
					}
				}
			}

			if (list != null && list.size() > 0) {

				if ("NAME".equals(orderBy.trim())) {

					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "picName", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "picName", YHMapComparator.TYPE_STR);
					}

				} else if ("SIZE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileSize", YHMapComparator.TYPE_LONG);
					} else {
						YHUtility.sortAsc(list, "fileSize", YHMapComparator.TYPE_LONG);
					}
				} else if ("TYPE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileType", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileType", YHMapComparator.TYPE_STR);
					}
				} else if ("TIME".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileTime", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileTime", YHMapComparator.TYPE_STR);
					}
				}

				for (int i = 0; i < list.size(); i++) {
					Map<String, String> map2 = new HashMap<String, String>();
					map2 = list.get(i);
					String nameString = map2.get("picName");

					long fileSizeStr = Long.parseLong(map2.get("fileSize"));
					String fileTimeStr = map2.get("fileTime");
					String imgWidthStr = map2.get("imgWidth");
					String imgHeightStr = map2.get("imgHeight");

					sb.append("{");
					sb.append("picName:\"" + YHUtility.encodeSpecial(nameString) + "\"");
					sb.append(",fileSize:\"" + fileSizeStr + "\"");
					sb.append(",fileTime:\"" + fileTimeStr + "\"");
					sb.append(",imgWidth:\"" + imgWidthStr + "\"");
					sb.append(",imgHeight:\"" + imgHeightStr + "\"");
					sb.append("},");
					flag = true;
				}
				if (flag) {
					sb.deleteCharAt(sb.length() - 1);
				}
			}
			sb.append("]");

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 上传批量文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir");
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr.trim())) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (subDir == null) {
			subDir = "";
		}

		YHPictureLogic logic = new YHPictureLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			String picPath = "";
			if (picture != null) {
				picPath = YHUtility.null2Empty(picture.getPicPath()) + "/" + subDir;
			}

			YHFileUploadForm fileForm = new YHFileUploadForm();
			fileForm.parseUploadRequest(request);
			String fileExists = fileForm.getExists(picPath);

			if (fileExists != null) {
				response.setCharacterEncoding(YHConst.DEFAULT_CODE);
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.println("-ERR 文件\"" + fileExists + "\"已经存在！");
				pw.flush();
				return null;
			}

			Iterator<String> iKeys = fileForm.iterateFileFields();
			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				File file = new File(picPath+"/" + fileName);
				if (file!=null && !file.exists()) {
					fileForm.saveFile(fieldName, picPath + File.separator + fileName);
				}else {
					StringBuffer buffer = new StringBuffer();
					String fileType = "";
					String nameTitle = "";
					if (fileName.lastIndexOf(".") != -1) {
						fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
						nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
					}
					logic.uploadEexistsFile(buffer,picPath, fileName, nameTitle, fileType);
					String newFileName = buffer.toString().trim();
					if (!YHUtility.isNullorEmpty(newFileName)) {
						fileForm.saveFile(fieldName, picPath  + File.separator +newFileName);
					}
				}
//				fileForm.saveFile(fieldName, picPath  + File.separator + fileName);
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 上传单个文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String uploadSingleFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);

		String subDir = fileForm.getParameter("fileSubDir");
		String seqIdStr = request.getParameter("seqId");

		String ascDescFlag = fileForm.getParameter("ascDescFlag");
		String field = fileForm.getParameter("field");

		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHPictureLogic logic = new YHPictureLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			String picPath = "";
			if (picture != null) {
				picPath = YHUtility.null2Empty(picture.getPicPath()) + "/" + subDir;
			}

			if (picPath != null) {
				Iterator<String> iKeys = fileForm.iterateFileFields();
				while (iKeys.hasNext()) {
					String fieldName = iKeys.next();
					String fileName = fileForm.getFileName(fieldName);
					if (YHUtility.isNullorEmpty(fileName)) {
						continue;
					}
					File file = new File(picPath+"/" + fileName);
					if (file!=null && !file.exists()) {
						fileForm.saveFile(fieldName, picPath  + File.separator +fileName);
					}else {
						StringBuffer buffer = new StringBuffer();
						String fileType = "";
						String nameTitle = "";
						if (fileName.lastIndexOf(".") != -1) {
							fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
							nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
						}
						logic.uploadEexistsFile(buffer,picPath, fileName, nameTitle, fileType);
						String newFileName = buffer.toString();
						if (!YHUtility.isNullorEmpty(newFileName)) {
							fileForm.saveFile(fieldName, picPath + File.separator + newFileName.trim());
						}
					}
				}
			}

			request.setAttribute("seqId", seqId);
			request.setAttribute("subDir", subDir);
			request.setAttribute("ascDescFlag", ascDescFlag);
			request.setAttribute("field", field);

			// request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			// request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		// return "/core/funcs/picture/loadPicture.jsp?seqId=" + seqId + "&subDir="
		// + subDir + "&ascDescFlag=" + ascDescFlag
		// + "&field=" + field;

		// return
		// "/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=" +
		// seqId + "&subDir=" + subDir + "&ascDescFlag=" + ascDescFlag
		// + "&field=" + field;

		return "/core/funcs/picture/picUploadResult.jsp";
	}

	/**
	 * 新建子文件夹
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String newFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir"); // 路径
		String folderName = request.getParameter("folderName"); // 修改后的文件夹名
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr.trim())) {
			seqId = Integer.parseInt(seqIdStr);
		}

		String data = "";
		boolean flag = false;
		String sucuss = "";
		String isExist = "";

		YHPictureLogic logic = new YHPictureLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			if (picture != null) {
				subDir = picture.getPicPath() + "/" + subDir + "/" + folderName;
			}

			if (subDir != null && subDir.trim().length() > 0) {
				File file = new File(subDir);
				if (!file.exists()) {
					if (file.mkdir()) {
						request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
						request.setAttribute(YHActionKeys.RET_MSRG, "创建成功！");
						sucuss = "创建成功";
						flag = true;
					} else {
						sucuss = "创建不成功";
					}
				} else {
					isExist = "文件夹已存在";
				}

			}

			data = "{sucuss:\"" + sucuss + "\",isExist:\"" + isExist + "\",flag:\"" + flag + "\"}";
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 文件夹重命名
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String folderRename(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir");
		String folderName = request.getParameter("folderName");
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr.trim())) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (subDir == null) {
			subDir = "";
		}
		if (folderName == null) {
			folderName = "";
		}
		String newSubDir = "";
		if (subDir.lastIndexOf("/") != -1) {
			newSubDir = subDir.substring(0, subDir.lastIndexOf("/")) + "/" + folderName;
		} else {
			newSubDir = folderName;
		}

		String data = "";
		boolean flag = false;
		String isExist = "";

		YHPictureLogic logic = new YHPictureLogic();
		String newFolderPath = "";
		String curDirPath = "";
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			if (picture != null) {
				newFolderPath = picture.getPicPath() + "/" + newSubDir; // 新的路径
				curDirPath = picture.getPicPath() + "/" + subDir; // 原来的路径			}
			String returnSubDir = "";
			if (folderName != null && curDirPath != null && curDirPath.trim().length() > 0) {
				File newFile = new File(newFolderPath);
				File file = new File(curDirPath);
				if (newFile!=null && file!=null) {
					if (!newFile.exists()) {
						file.renameTo(newFile);
						returnSubDir = newSubDir;
						request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
						request.setAttribute(YHActionKeys.RET_MSRG, "成功重命名文件夹");
						flag = true;
					}
				}
			}
			data = "{subDir:\"" + YHUtility.encodeSpecial(returnSubDir) + "\",isExist:\"" + isExist + "\",flag:\"" + flag + "\"}";
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除文件夹级其下的文件
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir"); // cc/dd2
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr.trim())) {
			seqId = Integer.parseInt(seqIdStr);
		}

		String newSubDir = "";
		if (subDir.lastIndexOf("/") != -1) {
			newSubDir = subDir.substring(0, subDir.lastIndexOf("/"));
		}

		boolean flag = false;
		YHPictureLogic logic = new YHPictureLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			if (picture != null) {
				subDir = picture.getPicPath() + "/" + subDir;
			}

			if (subDir != null && subDir.trim().length() > 0) {
				YHFileUtility.deleteAll(subDir);
				flag = true;
			}

			String data = "{subDir:\"" + newSubDir + "\",flag:\"" + flag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除文件夹");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 返回上一级目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String comeBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir"); // c/dd2
		String seqIdStr = request.getParameter("seqId");

		if (subDir == null) {
			subDir = "";
		}

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHPictureLogic logic = new YHPictureLogic();

		boolean flag = false;
		if (!"".equals(subDir.trim())) {
			flag = true;
		}

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			if (subDir.lastIndexOf('/') != -1) {
				subDir = subDir.substring(0, subDir.lastIndexOf('/'));
			} else {
				subDir = "";
			}
			String data = "{subDir:\"" + subDir + "\",flag:\"" + flag + "\",seqId:\"" + seqId + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subDir = request.getParameter("subDir"); // d:/cc/dd2
		String delNameStr = request.getParameter("fileStr"); // 文件名，以*号区分
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr.trim())) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHPictureLogic logic = new YHPictureLogic();

		String fileList = URLDecoder.decode(delNameStr);
		String[] names = null;
		String isImage = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			String picPath = "";
			if (picture != null) {
				picPath = picture.getPicPath() + "/" + subDir;
			}

			if (picPath != null && picPath.trim().length() > 0) {
				if (fileList != null && !"".equals(fileList)) {
					names = fileList.substring(0, fileList.length() - 1).split("\\*");
				}
				if (names.length != 0) {
					for (int i = 0; i < names.length; i++) {
						YHFileUtility.deleteAll(picPath + "/" + names[i]);
						if (names[i].matches(isImage)) {
							YHFileUtility.deleteAll(picPath + "/tdoa_cache/" + names[i]);
						}
					}
				}
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "成功返回");
			}

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取图片上传、管理权限信息
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		String subDir = request.getParameter("subDir");

		// String fileDirPath = "";

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPictureLogic logic = new YHPictureLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			// YHPictureLogic logic = new YHPictureLogic();

			Map map = new HashMap();
			map.put("SEQ_ID", Integer.parseInt(seqId));
			String data = logic.getPrivate(dbConn, map, loginUserSeqId, loginUserDeptId, loginUserRoleId, subDir);

			// System.out.println("data:" + data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 根据seqId得到路径
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPicFolderPathBySeqId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHPictureLogic logic = new YHPictureLogic();

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPicture picture = logic.getPicInfoById(dbConn, seqId);
			String picPath = picture.getPicPath() == null ? "" : picture.getPicPath();

			String data = "{folderPath:\"" + picPath + "\" }";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	public static void main(String[] args) {
		String folderPath = "E:/ee";

		File file = new File(folderPath);
		// System.out.println(file.getName());
		// System.out.println(file.getPath());

		File[] files = file.listFiles();
		String subDir = "";
		String fileName = "";
		for (File f : files) {
			// System.out.println(f.getPath());
			// System.out.println("fileName========="+f.getName()+"===");
			if (f.isDirectory()) {
				// subDir=f.getName();
				// System.out.println("directory==="+subDir);
				// System.out.println("directory==="+f.getPath());
			} else {
				// fileName=file.getName()+"  ";
				// System.out.println("fileName==="+fileName);
			}
			// System.out.println("subDir:"+subDir+"===fileName==="+fileName);
		}

	}

}
