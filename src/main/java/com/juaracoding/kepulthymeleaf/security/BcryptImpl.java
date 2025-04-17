package com.juaracoding.kepulthymeleaf.security;

import java.util.function.Function;

public class BcryptImpl {

    private static final BcryptCustom bcrypt = new BcryptCustom(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password,
                                              String hash,
                                              Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }

    public static boolean verifyHash(String password , String hash)
    {
        return bcrypt.verifyHash(password,hash);
    }

    public static void main(String[] args) {
//        String strUserName = "paul123Bagas@123";
        System.out.println(hash(""));
        System.out.println(hash(""));
        System.out.println(hash("").length());
        System.out.println(verifyHash("",""));
        System.out.println(verifyHash("",""));
    }
}