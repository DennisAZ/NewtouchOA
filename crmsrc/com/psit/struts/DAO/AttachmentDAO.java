package com.psit.struts.DAO;

import com.psit.struts.entity.Attachment;
/**
 * 附件DAO <br>
 */
public interface AttachmentDAO{
	/**
	 * 保存附件  <br>
	 */
	public void save(Attachment transientInstance);
	/**
	 * 根据id查询附件 <br>
	 */
	public Attachment findById(Long id);
	/**
	 * 删除附件 <br>
	 */
	public void delete(Attachment persistentInstance);
}