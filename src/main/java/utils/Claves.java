package utils;

import io.vavr.control.Either;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.concurrent.atomic.AtomicReference;

public class Claves {
    @SneakyThrows
    public Either<String,KeyPair> generarClaveRSA() {
        AtomicReference<Either<String,KeyPair>> result = new AtomicReference<>();

        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); // Hace uso del provider BC
        keyGen.initialize(2048);  // tamano clave 512 bits
        KeyPair clavesRSA = keyGen.generateKeyPair();
        result.set(Either.right(clavesRSA));
        return result.get();
    }

    @SneakyThrows
    public Either<String,PrivateKey> generarClavePrivada() {
        AtomicReference<Either<String,PrivateKey>> result = new AtomicReference<>();
        PrivateKey clavePrivada = generarClaveRSA().get().getPrivate();
        result.set(Either.right(clavePrivada));
        return result.get();
    }
    @SneakyThrows
    public Either<String, PublicKey> generarClavePublica() {
        AtomicReference<Either<String,PublicKey>> result = new AtomicReference<>();
        PublicKey clavePublica = generarClaveRSA().get().getPublic();
        result.set(Either.right(clavePublica));
        return result.get();
    }
}
