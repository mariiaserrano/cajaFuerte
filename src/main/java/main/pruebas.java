package main;

import dao.DaoUsuarios;
import model.Usuario;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import utils.PasswordHash;
import utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class pruebas {
    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, KeyStoreException, IOException, CertificateException {

//        DaoUsuarios dao = new DaoUsuarios();
//        Usuario usuario = new Usuario(1,"Laura","mipass","k");
//
//        dao.addUsuario(usuario);
//
  }


}
