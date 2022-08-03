package com.everestengg.code.challenge.service;

public class PackageOrderFactory {

    private PackageOrderFactory(){}
    public enum PackageServiceType{
        SIMPLE;
    }
    public static PackageOrderService getPackageOrderService(PackageServiceType packageServiceType){
        PackageOrderService packageOrderService;
        switch (packageServiceType){
            case SIMPLE:
                packageOrderService = new PackageOrderImpl();
                break;
            default: throw new IllegalArgumentException("invalid type "+packageServiceType);
        }
        return packageOrderService;

    }
}
