package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import servicios.ServiciosUsuarios;
import utils.Constantes;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    private Alert alert;
    private PrincipalController principalController;

    private ServiciosUsuarios sv = new ServiciosUsuarios();

    @FXML
    private TextField fxRegistroNombre;
    @FXML
    private PasswordField fxRegistroPass;
    @FXML
    private PasswordField fxRegistroPassDos;

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void vuelveAtras() {
        principalController.cargarLogin();
    }

    public void registrar() {
        if (!fxRegistroNombre.getText().isEmpty() && !fxRegistroPass.getText().isEmpty()
                && !fxRegistroPassDos.getText().isEmpty()) {
            if (fxRegistroPass.getText().equals(fxRegistroPassDos.getText())) {
                Usuario us = new Usuario(fxRegistroNombre.getText(), fxRegistroPass.getText(), "");
                sv.addUsuario(us).peek(usuario ->
                {
                    alert.setContentText(Constantes.USUARIO_AÑADIDO_CORRECTAMENTE);
                    alert.showAndWait();
                    limpiar();
                })
                        .peekLeft(s -> {
                            alert.setContentText(s);
                            alert.showAndWait();
                        });

            } else {
                alert.setContentText(Constantes.LAS_CONTRASEÑAS_DEBEN_SER_IGUALES);
                alert.showAndWait();
            }
        } else {
            alert.setContentText(Constantes.DEBES_RELLENAR_TODOS_LOS_CAMPOS);
            alert.showAndWait();
        }

    }

    public void limpiar() {
        fxRegistroNombre.clear();
        fxRegistroPass.clear();
        fxRegistroPassDos.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
}
