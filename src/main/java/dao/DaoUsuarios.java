package dao;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import utils.Certificados;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class DaoUsuarios {

    private static final String QUERY_SELECT_USUARIO = "select * from Usuario";
    private static final String QUERY_SELECT_USUARIO_LOGIN = "select * from Usuario where nombreUsuario=? and contrasenaUsuario=?";
    private static final String QUERY_INSERT_USUARIO = "insert into Usuario (nombreUsuario, contrasenaUsuario, rutaKeyStore) values (?,?,?)";
    private static final String QUERY_UPDATE_USUARIO = "update Usuario set rutaKeyStore=? where nombre=?;";

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_SELECT_USUARIO);

            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Usuario aux;
                aux = new Usuario(/*resultSet.getInt(1),*/ resultSet.getString(2),
                        resultSet.getString(3),resultSet.getNString(4));

                usuarios.add(aux);

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

        return usuarios;
    }

    public Either<String, Usuario> getUsuarioLogin(Usuario login) {
        AtomicReference<Either<String, Usuario>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        Usuario user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean active = false;

        try {
            connection = dbConnection.getConnection();
            preparedStatement = connection.prepareStatement(QUERY_SELECT_USUARIO_LOGIN);
            preparedStatement.setString(1, login.getNombre());
            preparedStatement.setString(2, login.getContrasena());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new Usuario(
                        /*resultSet.getInt("idUsuario"),*/
                        resultSet.getString("nombreUsuario"),
                        resultSet.getString("contrasenaUsuario"),
                        resultSet.getString("rutaKeyStore")
                );
            }

            if (user != null) {
                result.set(Either.right(user));
            }
            else {
                result.set(Either.left("Usuario o contraseña incorrectos"));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            dbConnection.cerrarConexion(connection);
            dbConnection.cerrarResultSet(resultSet);
            dbConnection.cerrarStatement(preparedStatement);
        }
        return result.get();


    }

    public Either<String,Usuario> addUsuario(Usuario usuario){
        AtomicReference<Either<String, Usuario>> result = new AtomicReference<>();
        Certificados cert = new Certificados();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;
        boolean anadido = false;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_INSERT_USUARIO);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getContrasena());
            stmt.setString(3, usuario.getRutaKeyStore());

            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                result.set(Either.right(usuario));

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

    public Either<String, Usuario>  updateKeyStore(Usuario usuario){
        AtomicReference<Either<String, Usuario>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_UPDATE_USUARIO);
            stmt.setString(1, usuario.getRutaKeyStore());
            stmt.setString(2,usuario.getNombre());

            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                result.set(Either.right(usuario));
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


