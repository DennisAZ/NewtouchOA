package com.psit.struts.BIZ;

import com.psit.struts.entity.Attachment;
/**
 * 附件BIZ <br>
 */
public interface AttBIZ {
	/**
	 * 保存附件 <br>
	 * @param att 附件实体 <br>
	 */
	public void save(Attachment att);
	/**
	 * 根据id查询附件 <br>
	 * @param id 附件id
	 * @return Attachment 返回附件实体 <br>
	 */
	public Attachment findById(Long id);
	/**
	 * 删除附件 <br>
	 * @param persistentInstance 附件实体 <br>
	 */
	public void delete(Attachment persistentInstance);
}
