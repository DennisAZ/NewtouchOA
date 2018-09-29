/*
 * Created on 2006-3-28
 */
package oa.spring.base;

import java.io.Serializable;
import java.util.List;








public interface IDAO
{
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>getReferenceClass</p> <BR>
     * <p>Description : </p>
     * @return
     * <br>--------------------------------------------------------<br>
     */
    public Class getReferenceClass ();
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>insertDO</p> <BR>
     * <p>Description : </p>
     * @param o
     * @return
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public Object insertDO(IDO o) throws SystemException;

    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>updateDO</p> <BR>
     * <p>Description : </p>
     * @param o
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public void updateDO(IDO o) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>insertOrUpdateObject</p> <BR>
     * <p>Description : </p>
     * @param o
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public void insertOrUpdateObject(IDO o) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>deleteDO</p> <BR>
     * <p>Description : </p>
     * @param o
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public void deleteDO(IDO o) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>deleteDO</p> <BR>
     * <p>Description : </p>
     * @param key
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public void deleteDO(Serializable key) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>queryDO</p> <BR>
     * <p>Description : </p>
     * @param key
     * @return
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public IDO queryDO(Serializable key) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>queryDOs</p> <BR>
     * <p>Description : </p>
     * @param queryString
     * @return
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public List queryDOs(String queryString) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>queryDOs</p> <BR>
     * <p>Description : </p>
     * @param queryString
     * @param values
     * @return
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public List queryDOs(String queryString,Object[] values) throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>queryAll</p> <BR>
     * <p>Description : </p>
     * @return
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public List queryAll() throws SystemException;
    /**
     * <br>--------------------------------------------------------<br>
     * <p>Function : </p>
     * <p>refreshObject</p> <BR>
     * <p>Description : </p>
     * @param o
     * @throws SystemException
     * <br>--------------------------------------------------------<br>
     */
    public void refreshObject (IDO o) throws SystemException;
}
