package com.psit.struts.entity;

import java.util.Date;


/**
 * 
 * 工作安排执行人附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:20:26 PM<br>
 * @author zjr
 */

public class AttTa extends Attachment  implements java.io.Serializable {

    // Fields    
     private TaLim taLim;

    // Constructors

    /** default constructor */
    public AttTa() {
    }
    
    /** full constructor */
    public AttTa(TaLim taLim) {
        this.taLim = taLim;
    }

	public TaLim getTaLim() {
		return taLim;
	}

	public void setTaLim(TaLim taLim) {
		this.taLim = taLim;
	}


}