/*
 * Created on 2006-3-28
 */
package oa.spring.base;


import java.io.Serializable;


public interface IVO extends Serializable
{
    public IDO toDO();
}
