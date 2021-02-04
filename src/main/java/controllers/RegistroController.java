package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Usuario;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    private Alert alert;
    private PrincipalController principalController;
    private ServiciosUsuarios sv = new ServiciosUsuarios();

    @FXML
    private TextField fxRegistroNombre;
    @FXML
    private TextField fxRegistroPass;
    @FXML
    private TextField fxRegistroPassDos;

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void vuelveAtras(){
        principalController.cargarLogin();
    }

    public void registrar (){
        Usuario us = new Usuario(fxRegistroNombre.getText(),fxRegistroPass.getText(),"");
        sv.addUsuario(us);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
}
