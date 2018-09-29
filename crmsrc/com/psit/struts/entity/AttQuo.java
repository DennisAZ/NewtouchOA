package com.psit.struts.entity;

import java.util.Date;


/**
 * 
 * 报价记录附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:17:27 PM<br>
 * @author zjr
 */

public class AttQuo extends Attachment  implements java.io.Serializable {

    // Fields    
     private Quote quote;

    // Constructors

    /** default constructor */
    public AttQuo() {
    }
    
    /** full constructor */
    public AttQuo(Quote quote) {
        this.quote = quote;
    }

    // Property accessors

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

}