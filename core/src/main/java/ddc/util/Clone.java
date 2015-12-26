package ddc.util;

import org.apache.commons.beanutils.BeanUtils;

public class Clone {

    public static Object clone(Object source) throws CloneNotSupportedException {
        try {
            return BeanUtils.cloneBean(source);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }
}
