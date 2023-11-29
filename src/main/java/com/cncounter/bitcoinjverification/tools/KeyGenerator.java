package com.cncounter.bitcoinjverification.tools;


import org.bitcoinj.core.ECKey;

public class KeyGenerator {

    public static void main(String[] args) {
        randomPrivateKey();
    }
    public static String randomPrivateKey(){
        ECKey key = new ECKey();
        String prv = key.getPrivateKeyAsHex();
        System.out.format("private => %s\n",prv);
        return prv;
    }
}
