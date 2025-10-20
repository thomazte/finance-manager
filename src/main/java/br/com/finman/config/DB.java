package br.com.finman.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fornece conexões JDBC para o PostgreSQL usando as configs de src/main/resources/db.properties.
 * Se isso aqui falhar, nada mais vive. Trate-o bem.
 */
public class DB {

    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        try (var is = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado no classpath");
            }
            Properties p = new Properties();
            p.load(is);

            URL  = p.getProperty("db.url");
            USER = p.getProperty("db.user");
            PASS = p.getProperty("db.pass");

            // Carrega o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar configuração de banco", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Opcional: teste rápido de conexão (pode apagar se não quiser)
    public static void main(String[] args) {
        try (Connection c = getConnection()) {
            System.out.println("Conectado ao PostgreSQL com sucesso: " + c.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Deu ruim na conexão. Confira URL/usuário/senha no db.properties.");
        }
    }
}
