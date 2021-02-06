package utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public final class BouncyCastleProviderSingleton {


    /**
     * The BouncyCastle provider, lazily instantiated.
     */
    private static BouncyCastleProvider bouncyCastleProvider;


    /**
     * Prevents external instantiation.
     */
    private BouncyCastleProviderSingleton() {
    }


    /**
     * Returns a BouncyCastle JCA provider instance.
     *
     * @return The BouncyCastle JCA provider instance.
     */
    public static BouncyCastleProvider getInstance() {

        if (bouncyCastleProvider != null) {

            return bouncyCastleProvider;

        } else {
            bouncyCastleProvider = new BouncyCastleProvider();
            Security.addProvider(bouncyCastleProvider);
            return bouncyCastleProvider;
        }
    }
}