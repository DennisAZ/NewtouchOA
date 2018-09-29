/*
 * Created on 2007-3-28
 */
package oa.spring.base;


public class SystemException extends Exception
{

    /**
     * <code>serialVersionUID(long)</code>
     */
    private static final long serialVersionUID = 7204108521937467010L;


    /**
     * SystemException
     *
     */
    public SystemException()
    {
        super();

    }


    /**
     * SystemException
     *
     * @param p_message
     */
    public SystemException( String p_message )
    {
        super( p_message );

    }


    /**
     * SystemException
     *
     * @param p_message
     * @param p_cause
     */
    public SystemException( String p_message, Throwable p_cause )
    {
        super( p_message, p_cause );

    }


    /**
     * SystemException
     *
     * @param p_cause
     */
    public SystemException( Throwable p_cause )
    {
        super( p_cause );

    }

}
