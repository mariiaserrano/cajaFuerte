package Cifrado;

import io.vavr.control.Either;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import utils.Constantes;
import utils.Hasheo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class Claves {


    public Either<String, PrivateKey> generarClavePrivada() {
        Either<String, PrivateKey> resultado = null;
        try {
            AtomicReference<Either<String, PrivateKey>> result = new AtomicReference<>();
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair clavesRSA = keyGen.generateKeyPair();
            PrivateKey clavePrivada = clavesRSA.getPrivate();
            resultado = Either.right(clavePrivada);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(e.getMessage());
        }
        return resultado;
    }

    public Either<String, PublicKey> generarClavePublica() {
        Either<String, PublicKey> resultado = null;
        try {
            AtomicReference<Either<String, PublicKey>> result = new AtomicReference<>();
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair clavesRSA = keyGen.generateKeyPair();
            PublicKey clavePublica = clavesRSA.getPublic();
            resultado = Either.right(clavePublica);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(e.getMessage());
        }
        return resultado;
    }


    public PrivateKey getClavePrivada(Usuario usuario) {
        PrivateKey clavePrivadaRecuperada = null;
        Hasheo has = new Hasheo();
        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(new FileInputStream(Constantes.rutaArchivos + usuario.getNombre() + Constantes.KEYSTORE_PFX), "".toCharArray());
            KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection(usuario.getContrasena().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ksLoad.getEntry(usuario.getNombre() + Constantes.PRIVADA, pt);

            clavePrivadaRecuperada =privateKeyEntry.getPrivateKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return clavePrivadaRecuperada;

    }

    public PublicKey getClavePublica(Usuario usuario) {
        Either<String, PublicKey> resultado = null;
        PublicKey clavePublicaRecuperada = null;
        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(new FileInputStream(Constantes.rutaArchivos + usuario.getNombre() + Constantes.KEYSTORE_PFX), "".toCharArray());
            X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate(usuario.getNombre() + Constantes.PUBLICA);
            clavePublicaRecuperada = certLoad.getPublicKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return clavePublicaRecuperada;

    }

    public Either<String, String> generarClaveSimetrica() {
        Either<String, String> resultado = null;
        try {
            SecureRandom sr = new SecureRandom();
            byte[] clave = new byte[16];
            sr.nextBytes(clave);
            String claveSimetrica = Base64.getUrlEncoder().encodeToString(clave);
            resultado = Either.right(claveSimetrica);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(e.getMessage());
        }
        return resultado;
    }

}
