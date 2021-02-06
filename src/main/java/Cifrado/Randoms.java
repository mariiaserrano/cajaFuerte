package Cifrado;

import io.vavr.control.Either;

import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Randoms {

    public byte[] getIv() {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[12];
        sr.nextBytes(iv);
        return iv;
    }

    public byte[] getSalt(){
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
