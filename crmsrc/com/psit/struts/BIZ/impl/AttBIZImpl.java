package com.psit.struts.BIZ.impl;

import java.util.List;

import com.psit.struts.BIZ.AttBIZ;
import com.psit.struts.DAO.AttachmentDAO;
import com.psit.struts.entity.Attachment;
/**
 * 附件BIZ实现类 <br>
 */
public class AttBIZImpl implements AttBIZ {
	AttachmentDAO attDao=null;
	/**
	 * 保存附件 <br>
	 */
	public void save(Attachment att){
		attDao.save(att);
	}
	/**
	 * 根据id查询附件 <br>
	 */
	public Attachment findById(Long id){
		return attDao.findById(id);
	}
	/**
	 * 删除附件 <br>
	 */
	public void delete(Attachment persistentInstance){
		attDao.delete(persistentInstance);
	}
	
	public void setAttDao(AttachmentDAO attDao) {
		this.attDao = attDao;
	}
}
