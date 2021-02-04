package utils;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class Hasheo {

    @SneakyThrows
    public String hashPassword(String args) {

        MessageDigest md = MessageDigest.getInstance("SHA3-512");

        String pass = args;

        byte[] hashBytes = md.digest(pass.getBytes());
        var hash1 = bytesToHex(hashBytes);
        return hash1;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}