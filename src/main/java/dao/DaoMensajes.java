package dao;

import io.vavr.control.Either;
import model.Mensaje;
import utils.Constantes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoMensajes {
    private static final String QUERY_INSERT_MENSAJEMIO = "insert into Mensajes (iv, salt, claveSimetrica, mensaje, firma, iteraciones, emisor, receptor) values (?,?,?,?,?,?,?,?)";

    public Either<String, String> addMensaje(Mensaje mensaje){
        AtomicReference<Either<String, String>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;
        boolean anadido = false;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_INSERT_MENSAJEMIO);
            stmt.setString(1, mensaje.getIv());
            stmt.setString(2, mensaje.getSalt());
            stmt.setString(3, mensaje.getClaveSimetrica());
            stmt.setString(4, mensaje.getMensaje());
            stmt.setString(5, mensaje.getFirma());
            stmt.setInt(6, mensaje.getIteraciones());
            stmt.setString(7,mensaje.getEmisor());
            stmt.setString(8,mensaje.getReceptor());

            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                result.set(Either.right(Constantes.MENSAJE_AÑADIDO));

            }
            else {
                result.set(Either.left(Constantes.ERROR_GUARDAR_MENSAJE));
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbConnection.cerrarConexion(con);
            dbConnection.cerrarResultSet(resultSet);
            dbConnection.cerrarStatement(stmt);

        }

        return result.get();
    }
}
