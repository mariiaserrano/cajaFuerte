package Cifrado;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Mensaje;
import model.Usuario;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

@Log4j2
public class CifrarMensaje {

    private Claves cl = new Claves();
    private Randoms rd = new Randoms();

    public Either<String, Mensaje> cifrarMensaje(Usuario usuario, String mensajeACifrar) {
        Either<String, Mensaje> resultado = null;
        String claveSimetrica = cl.generarClaveSimetrica().get();
        byte[] iv = rd.getIv();
        byte[] salt = rd.getSalt();
        int it = 65536;
        String firma = null;
        Mensaje msg = new Mensaje();
        String mensajeCifrado = null;
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        SecretKeyFactory factory = null;
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(claveSimetrica.toCharArray(), salt, it, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            mensajeCifrado = cifra(secretKey, parameterSpec, mensajeACifrar);

            msg.setIv(Base64.getUrlEncoder().encodeToString(iv));
            msg.setSalt(Base64.getUrlEncoder().encodeToString(salt));
            msg.setIteraciones(it);
            msg.setMensaje(mensajeCifrado);

            publicKey = cl.getClavePublica(usuario);
            privateKey = cl.getClavePrivada(usuario);

            claveSimetrica = cifraConSimetrica(claveSimetrica, publicKey);
            msg.setClaveSimetrica(claveSimetrica);

            firma = firma(mensajeCifrado, privateKey);
            msg.setFirma(firma);

            msg.setEmisor(usuario.getNombre());
            msg.setReceptor(usuario.getNombre());
            resultado = Either.right(msg);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado=Either.left(e.getMessage());
        }
        return resultado;

    }

    public String cifra(SecretKey secretKey, GCMParameterSpec parameterSpec, String mensajeAcifrar) {
        String mensaje = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            mensaje = Base64.getUrlEncoder().encodeToString(
                    cipher.doFinal(mensajeAcifrar.getBytes("UTF-8")));

        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        return mensaje;
    }

    public String cifraConSimetrica(String claveSimetrica, PublicKey clavePublica) {

        try {
            Cipher cifrador = Cipher.getInstance("RSA");


            // Cifra con la clave publica
            cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
            claveSimetrica = Base64.getUrlEncoder()
                    .encodeToString(cifrador.doFinal(claveSimetrica.getBytes()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        return claveSimetrica;
    }

//    public String cifra(SecretKey secretKey, GCMParameterSpec parameterSpec, String mensajeAcifrar) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
//        final byte[] bytes = mensajeAcifrar.getBytes("UTF-8");
//        Cipher aes = Cipher.getInstance("AES/GCM/noPadding");
//        aes.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
//        final byte[] cifrado = aes.doFinal(bytes);
//        return Base64.getUrlEncoder().encodeToString(cifrado);
//    }

    public String firma(String mensaje, PrivateKey clavePrivada) {
        String firma = null;
        try {
            //Comprobamos la firma
            Signature sign = Signature.getInstance("SHA256WithRSA");
            sign.initSign(clavePrivada);
            sign.update(mensaje.getBytes());
            firma = Base64.getUrlEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return firma;
    }
}
