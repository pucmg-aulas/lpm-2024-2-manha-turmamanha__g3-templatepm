package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável pela conexão com o banco de dados utilizando Singleton.
 */
public class BancoDados {

    // Configuração do banco na nuvem
    private static final String BANCO = "railway"; // Nome do banco
    private static final String USUARIO = "postgres"; // Usuário
    private static final String SENHA = "rHYZrSgwQjIUvPjvrQOEUEkQFwyhmBwd"; // Senha
    private static final String HOST = "junction.proxy.rlwy.net"; // Host fornecido
    private static final String PORT = "40694"; // Porta fornecida
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + BANCO; // URL final

    // Singleton
    private static BancoDados instancia = null;

    // Conexão
    private Connection conexao;

    // Construtor privado
    private BancoDados() {
        conectar();
    }

    // Método público para obter a instância única
    public static synchronized BancoDados getInstancia() {
        if (instancia == null) {
            instancia = new BancoDados();
        }
        return instancia;
    }

    // Método privado para realizar a conexão
    private void conectar() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o banco PostgreSQL na nuvem foi estabelecida com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados:");
            System.err.println("URL: " + URL);
            System.err.println("Usuário: " + USUARIO);
            e.printStackTrace();
        }
    }

    // Método público para obter a conexão
    public synchronized Connection getConexao() {
        conectar(); // Garante que a conexão está ativa antes de retornar
        return conexao;
    }

    // Método para desconectar
    public void desconectar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão encerrada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao encerrar a conexão com o banco de dados:");
            e.printStackTrace();
        }
    }
}
