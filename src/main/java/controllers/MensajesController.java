package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Usuario;
import servicios.ServiciosUsuarios;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class MensajesController  implements Initializable {
    private Alert alert;

    //Mensajes Mios
    @FXML
    private ListView fxListaMensajesMios;
    @FXML
    private TextArea fxRedactoMensajeMio;

    //Mensajes de Otros
    @FXML
    private ListView fxListaMensajesOtros;
    @FXML
    private TextArea fxEscribeMensajeOtros;
    @FXML
    private ComboBox<Usuario> fxComboUsuarios;

    private ServiciosUsuarios su = new ServiciosUsuarios();

    private PrincipalController principalController;
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }
    public void vuelveAtras(){
        principalController.cargarLogin();
    }

    public void rellenaCombo(){
        fxComboUsuarios.getItems().clear();
      fxComboUsuarios.getItems().addAll(su.getAllUsuarios());
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
}
