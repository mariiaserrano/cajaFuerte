package main;

import model.Mensaje;
import utils.Constantes;
import utils.Hasheo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.util.Base64;

public class pruebas {
    public static void main(String[] args) throws Exception {
        Hasheo has = new Hasheo();
//        KeyStore ksLoad = KeyStore.getInstance("PKCS12");
//        ksLoad.load(new FileInputStream(Constantes.rutaArchivos + "pedro" + Constantes.KEYSTORE_PFX), "".toCharArray());
//        X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate("pedro" + Constantes.PUBLICA);
//
//        System.out.println(certLoad.getPublicKey());


        System.out.println("bb9f69d5e0c7a1b5eec4e4c1aca224eb5f64a793f8433133a76f6175ad985259110f4bfd85a45fe09db5feb30a1f670cbfe85bce7e207e7da36d5a0b4c6e983f");
        System.out.println("bb9f69d5e0c7a1b5eec4e4c1aca224eb5f64a793f8433133a76f6175ad985259110f4bfd85a45fe09db5feb30a1f670cbfe85bce7e207e7da36d5a0b4c6e983f".toCharArray());
    }

}
