package com.greenbone.samplecompany.util;

public class ComputerUtils {

    //https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    public static final String IPV4_PATTERN =
            "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";


    //https://uibakery.io/regex-library/mac-address-regex-java
    public static final String MAC_PATTERN = "^(?:[0-9A-Fa-f]{2}[:-]){5}(?:[0-9A-Fa-f]{2})$";
}
