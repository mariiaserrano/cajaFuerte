package servicios;

import dao.DaoMensajes;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Mensaje;
import model.Usuario;
import Cifrado.CifrarMensaje;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

@Log4j2
public class ServiciosMensajes {
    private DaoMensajes dao = new DaoMensajes();

    public Either<String, String> addMensaje(Usuario usuario, String mensajeMio) {
        CifrarMensaje cm = new CifrarMensaje();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String error = validator.validate(usuario).stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        Either<String, Mensaje> resultado = cm.cifrarMensaje(usuario, mensajeMio);

        try {
            return dao.addMensaje(resultado.get());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left("Error base de datos");

        }
    }
}




