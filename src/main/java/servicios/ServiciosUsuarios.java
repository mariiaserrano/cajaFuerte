package servicios;

import dao.DBConnection;
import dao.DaoUsuarios;
import io.vavr.control.Either;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import org.checkerframework.checker.units.qual.A;
import utils.Certificados;
import utils.Hasheo;
import utils.PasswordHash;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Log4j2
public class ServiciosUsuarios {


    private DaoUsuarios dao = new DaoUsuarios();
    private Certificados certificados = new Certificados();

    public List<Usuario> getAllUsuarios() {
        return dao.getAllUsuarios();
    }

    public Either<String, Usuario> getUsuarioLogin(Usuario login) {
        Hasheo hash = new Hasheo();

        DaoUsuarios dao = new DaoUsuarios();
        String password = hash.hashPassword(login.getContrasena());
        login.setContrasena(password);
        return dao.getUsuarioLogin(login);
    }

    public Either<String, String> addUsuario(Usuario usuario) {
        AtomicReference<Either<String, String>> resultado = new AtomicReference<>();
        Hasheo hash = new Hasheo();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String error = validator.validate(usuario).stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        if (!error.isEmpty()) {
            resultado.set(Either.left(error));
        } else {
            try {
                usuario.setContrasena(hash.hashPassword(usuario.getContrasena()));
                usuario.setRutaKeyStore(usuario.getNombre() + "keystore.pfx");
                dao.addUsuario(usuario)
                        .peek(u -> {
                            certificados.generarKeyStore(u)
                                    .peek(s -> {
                                        resultado.set(Either.right(s));
                                    }).peekLeft(s -> {
                                resultado.set(Either.left(s));
                            });
                        })
                        .peekLeft(s -> {
                            resultado.set(Either.left(s));
                        });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultado.set(Either.left("Error base de datos"));

            }
        }

        return resultado.get();

    }



}
