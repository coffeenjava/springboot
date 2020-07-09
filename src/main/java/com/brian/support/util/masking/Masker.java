package com.brian.support.util.masking;


import com.brian.support.annotation.Masking;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 마스킹 처리 공통 인터페이스
 */
public interface Masker {
    String doMask(String source);

    static String mask(Masking masking, String value) {
        try {
            if (StringUtils.isBlank(value)) return "";
            Masker masker = masking.type().getConstructor().newInstance();
            return masker.doMask(value);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
