package Cifrado;

import io.vavr.control.Either;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import utils.Constantes;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class Certificados {

    private Claves claves = new Claves();

    @SneakyThrows
    public Either<String, X509Certificate> generarCertificado(Usuario usuario) {
        AtomicReference<Either<String, X509Certificate>> resultado = new AtomicReference<>();

        try {
            claves.generarClavePublica()
                    .peek(publicKey -> {
                        try {
                            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                            keyGen.initialize(2048);
                            KeyPair claveRSA = keyGen.generateKeyPair();
                            //genera certificado
                            X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
                            cert1.setSerialNumber(BigInteger.valueOf(1));
                            //usuario
                            cert1.setSubjectDN(new X509Principal("CN=" + usuario.getNombre()));
                            cert1.setIssuerDN(new X509Principal("CN=" + usuario.getNombre()));
                            cert1.setPublicKey(publicKey);
                            cert1.setNotBefore(
                                    Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
                            cert1.setNotAfter(new Date());
                            cert1.setSignatureAlgorithm("SHA1WithRSAEncryption");
                            //firmamos
                            PrivateKey signingKey = claveRSA.getPrivate();
                            X509Certificate cert = cert1.generateX509Certificate(signingKey);
                            resultado.set(Either.right(cert));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
                        }
                    })
                    .peekLeft(s -> {
                        resultado.set(Either.left(s));
                    });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
        }
        return resultado.get();

    }

    @SneakyThrows
    public Either<String, String> generarKeyStore(Usuario usuario) {
        AtomicReference<Either<String, String>> resultado = new AtomicReference<>();
        try {
            generarCertificado(usuario)
                    .peek(X509Certificate -> {
                        claves.generarClavePrivada()
                                .peek(privateKey ->
                                {
                                    try {
                                        KeyStore ks = KeyStore.getInstance("PKCS12");
                                        ks.load(null, null);
                                        ks.setCertificateEntry(usuario.getNombre() + Constantes.PUBLICA, X509Certificate);
                                        ks.setKeyEntry(usuario.getNombre() + Constantes.PRIVADA, privateKey
                                                , usuario.getContrasena().toCharArray(), new Certificate[]{X509Certificate});
                                        FileOutputStream fos = new FileOutputStream(usuario.getRutaKeyStore());
                                        ks.store(fos, "".toCharArray());
                                        fos.close();
                                        resultado.set(Either.right(Constantes.U_SUARIOADD));
                                    } catch (Exception e) {
                                        log.error(e.getMessage(), e);
                                        resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
                                    }
                                })
                                .peekLeft(s -> {
                                    resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
                                });
                    })
                    .peekLeft(s -> {
                        resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
                    });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado.set(Either.left(Constantes.USUARIO_NO_VALIDO));
        }


        return resultado.get();

    }
}
