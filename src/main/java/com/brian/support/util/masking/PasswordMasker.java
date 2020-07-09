package com.brian.support.util.masking;

/**
 * Password 타입 마스킹 처리
 */
public class PasswordMasker implements Masker {
    @Override
    public String doMask(String source) {
        return "password masked";
    }
}
