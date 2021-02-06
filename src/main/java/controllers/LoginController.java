package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import servicios.ServiciosUsuarios;
import utils.Constantes;
import utils.Hasheo;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Alert alert;

    @FXML
    private TextField fxUsuario;
    @FXML
    private PasswordField fxContrasena;

    private Hasheo hash = new Hasheo();

    private ServiciosUsuarios su = new ServiciosUsuarios();

    private PrincipalController principalController;
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }
    public void iniciarSesion(){
        if (!fxUsuario.getText().isEmpty() && !fxContrasena.getText().isEmpty()) {
            Usuario login = new Usuario(fxUsuario.getText(), fxContrasena.getText(),"");
            su.getUsuarioLogin(login)
                    .peek(usuario -> {
                        principalController.cargarMensajes();
                        principalController.setUsuario(login);
                    })
                    .peekLeft(s -> {
                        alert.setContentText(s);
                        alert.showAndWait();
                    });
        } else {
            alert.setContentText(Constantes.DEBES_RELLENAR_TODOS_LOS_CAMPOS);
            alert.showAndWait();
        }

    }

    public void abrirRegistro(){
        principalController.cargarRegistro();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
}
