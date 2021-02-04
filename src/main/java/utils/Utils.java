package utils;

import lombok.SneakyThrows;
import model.Usuario;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Utils {

    @SneakyThrows
    public KeyPair generarClaveRSA() {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); // Hace uso del provider BC
        keyGen.initialize(2048);  // tamano clave 512 bits
        KeyPair clavesRSA = keyGen.generateKeyPair();
        return clavesRSA;
    }
    @SneakyThrows
    public PrivateKey generarClavePrivada() {
        PrivateKey clavePrivada = generarClaveRSA().getPrivate();
        return clavePrivada;
    }
    @SneakyThrows
    public PublicKey generarClavePublica(){

        PublicKey clavePublica = generarClaveRSA().getPublic();
        return clavePublica;
    }

    @SneakyThrows
    public X509Certificate generarCertificado(PrivateKey clavePrivada, PublicKey clavePublica, Usuario usuario){
        X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
        cert1.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number
        cert1.setSubjectDN(new X509Principal("CN="+usuario.getNombre()));  //see examples to add O,OU etc
        cert1.setIssuerDN(new X509Principal("CN="+usuario.getNombre())); //same since it is self-signed
        cert1.setPublicKey(clavePublica);
        cert1.setNotBefore(
                Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
        cert1.setNotAfter(new Date());
        cert1.setSignatureAlgorithm("SHA1WithRSAEncryption");
        PrivateKey signingKey = clavePrivada;


        X509Certificate cert =  cert1.generateX509Certificate(signingKey);
        return cert;
    }

    @SneakyThrows
    public boolean generarKs(X509Certificate cert,PrivateKey clavePrivada, Usuario usuario){
        boolean generado = false;
        if(cert !=null) {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] password = PasswordHash.createHash("abc").toCharArray();
            ks.load(null, null);
            ks.setCertificateEntry(usuario.getNombre()+"publica", cert);
            ks.setKeyEntry(usuario.getNombre() +"privada", clavePrivada, password, new Certificate[]{cert});
            FileOutputStream fos = new FileOutputStream("keystore"+usuario.getNombre()+".pfx");
            ks.store(fos, password);
            fos.close();
            generado = true;
        }
        else {
            generado=false;
        }
      return generado;
    }

}
