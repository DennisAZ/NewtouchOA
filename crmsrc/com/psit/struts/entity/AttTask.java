package com.psit.struts.entity;

import java.util.Date;


/**
 * 
 * 工作安排附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:20:47 PM<br>
 * @author zjr
 */

public class AttTask extends Attachment  implements java.io.Serializable {

    // Fields    
     private SalTask salTask;

    // Constructors

    /** default constructor */
    public AttTask() {
    }
    
    /** full constructor */
    public AttTask(SalTask salTask) {
        this.salTask = salTask;
    }

	public SalTask getSalTask() {
		return salTask;
	}

	public void setSalTask(SalTask salTask) {
		this.salTask = salTask;
	}
}