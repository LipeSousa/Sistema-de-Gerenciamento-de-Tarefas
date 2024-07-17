package bancojbdc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BancoJDBC {

    private Connection con;
    private Statement stmt;

    public BancoJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Encontrado");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado!" + e);
            System.out.println("Error: " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/gerenciamentotarefas";
        String user = "root";
        String password = "";

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void cadastrarUsuario(String nome, String senha) {
        try {
            stmt.executeUpdate("INSERT INTO user (nome, senha) VALUES ('" + nome + "','" + senha + "')");
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public int fazerLogin(String nome, String senha) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT idUsuario FROM user WHERE nome = '" + nome + "' AND senha = '" + senha + "'");
            if (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                System.out.println("");
                System.out.println("Login realizado com sucesso!");
                System.out.println("");
                return idUsuario;
            } else {
                System.out.println("Nome de usuário ou senha incorretos.");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return -1;
        }
    }

    public void inserirTarefa(int idUsuario, String nome, String descricao, String data, String estado) {
        try {
            stmt.executeUpdate("INSERT INTO tarefas (idUsuario, nome_tarefa, descricao_tarefa, data_tarefa, estado_atual) VALUES (" + idUsuario + ",'" + nome + "','" + descricao + "','" + data + "','" + estado + "')");
            System.out.println("Tarefa inserida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void alterarTarefa(int idUsuario, int idTarefa, String estado) {
        try {
            stmt.executeUpdate("UPDATE tarefas SET estado_atual = '" + estado + "' WHERE idTarefa=" + idTarefa + " AND idUsuario=" + idUsuario);
            System.out.println("Tarefa alterada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarTarefas(int idUsuario) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tarefas WHERE idUsuario=" + idUsuario);
            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.println("Id da Tarefa | Nome da Tarefa    | Descrição da Tarefa | Data da Tarefa | Estado Atual |");
            System.out.println("---------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int idTarefa = rs.getInt("idTarefa");
                String nome_tarefa = rs.getString("nome_tarefa");
                String descricao_tarefa = rs.getString("descricao_tarefa");
                String data_tarefa = rs.getString("data_tarefa");
                String estado_atual = rs.getString("estado_atual");
                System.out.println(idTarefa + "\t     | " + nome_tarefa + "\t | " + descricao_tarefa + "\t| " + data_tarefa + "\t | " + estado_atual + "\t| ");
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void apagarTarefa(int idUsuario, int idTarefa) {
        try {
            stmt.executeUpdate("DELETE FROM tarefas WHERE idTarefa=" + idTarefa + " AND idUsuario=" + idUsuario);
            System.out.println("");
            System.out.println("");
            System.out.println("Tarefa com o id: " + idTarefa + " excluída com sucesso!");
            System.out.println("");
            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BancoJDBC bancoJDBC = new BancoJDBC();
        Scanner leia = new Scanner(System.in);
        int opcao;
        String nome, senha;

        System.out.println("1 - Cadastrar Usuário");
        System.out.println("2 - Fazer Login");
        opcao = Integer.parseInt(leia.nextLine());

        switch (opcao) {
            case 1:
                System.out.println("Digite o nome do usuário: ");
                nome = leia.nextLine();
                System.out.println("Digite a senha do usuário: ");
                senha = leia.nextLine();
                bancoJDBC.cadastrarUsuario(nome, senha);
                break;
            case 2:
                System.out.println("Digite o nome do usuário: ");
                nome = leia.nextLine();
                System.out.println("Digite a senha do usuário: ");
                senha = leia.nextLine();
                int idUsuario = bancoJDBC.fazerLogin(nome, senha);
                if (idUsuario != -1) {
                    int idTarefaExcluida, iDalterarEstado;
                    String descricao, data, estado, alterarEstado;
                    do {
                        System.out.println("1 - Inserir uma nova Tarefa");
                        System.out.println("2 - Alterar estado da Tarefa");
                        System.out.println("3 - Consultar as Tarefas");
                        System.out.println("4 - Excluir Tarefa");
                        System.out.println("5 - Sair");
                        System.out.println("");
                        System.out.println("Digite a sua opção: ");
                        opcao = Integer.parseInt(leia.nextLine());

                        switch (opcao) {
                            case 1:
                                System.out.println("Digite o nome da Tarefa: ");
                                nome = leia.nextLine();
                                System.out.println("Digite a descrição da Tarefa: ");
                                descricao = leia.nextLine();
                                System.out.println("Digite a data da Tarefa (dd/mm/yyyy): ");
                                data = leia.nextLine();
                                System.out.println("Digite o estado da Tarefa: ");
                                estado = leia.nextLine();
                                bancoJDBC.inserirTarefa(idUsuario, nome, descricao, data, estado);
                                break;
                            case 2:
                                System.out.println("Digite o iD da Tarefa para alterar: ");
                                iDalterarEstado = Integer.parseInt(leia.nextLine());
                                System.out.println("Digite o novo estado da Tarefa: ");
                                alterarEstado = leia.nextLine();
                                bancoJDBC.alterarTarefa(idUsuario, iDalterarEstado, alterarEstado);
                                break;
                            case 3:
                                bancoJDBC.listarTarefas(idUsuario);
                                break;
                            case 4:
                                System.out.println("Digite o id da Tarefa para exclusão: ");
                                idTarefaExcluida = Integer.parseInt(leia.nextLine());
                                bancoJDBC.apagarTarefa(idUsuario, idTarefaExcluida);
                                break;
                            case 5:
                                System.out.println("Encerrando Programa!");
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    } while (opcao != 5);
                }
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
        leia.close();
    }
}
