package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Usuario;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Alert alert;

    @FXML
    private TextField fxUsuario;
    @FXML
    private TextField fxContrasena;

    private ServiciosUsuarios su = new ServiciosUsuarios();


    private PrincipalController principalController;
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }
    public void iniciarSesion(){
        if (!fxUsuario.getText().isEmpty() && !fxContrasena.getText().isEmpty()) {
            Usuario login = new Usuario(/*1,*/fxUsuario.getText(), fxContrasena.getText(),"");
            su.getUsuarioLogin(login)
                    .peek(usuario -> {
                        principalController.cargarMensajes();
                    })
                    .peekLeft(s -> {
                        alert.setContentText(s);
                        alert.showAndWait();
                    });
        } else {
            alert.setContentText("rellenar todos los campos");
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
