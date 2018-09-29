package oa.spring.db.dataSet;


public interface DataSet {


    /**
     * 
     *
     * @param i
     * @return
     */
    public String get(int i);
    public String getName(int i);
    //public String get(int i,String defaultValue);

    /**
     * 
     *
     * @param
     * @return
     */
    public String get(String name);

    public String get(String name, String defaultValue);

    /**
     * 
     *
     * @return
     */
    public boolean next();

    /**
     * 
     * @return
     */
    public boolean previous();

    /**
     * 
     */
    public void first();

    /**
     * 
     */
    public void last();

    /**
     * 
     *
     * @param i
     */
    public void setCurrentRow(int i);

    /**
     * 
     *
     * @return
     */
    public int getCurrentRow();

    /**
     * 
     *
     * @return
     */
    public int getColCount();

    /**
     * 
     *
     * @return
     */
    public int getRowCount();

    /**
     * 
     *
     * @param i
     */
	public void setEndRow(int i);

	/**
     * 
     *
     * @return
     */
	public int getEndRow();
}
