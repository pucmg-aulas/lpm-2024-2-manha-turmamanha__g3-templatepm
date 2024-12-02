package view;

import controller.ListarEstacionamentoController;
import dao.ClienteDAOException;
import dao.EstacionamentoDAOException;
import model.EstacionamentoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ListarEstacionamentosView extends JFrame {

    private JPanel panel;
    private JTable tabelaEstacionamentos;
    private DefaultTableModel tabelaModelo;

    private JButton gerenciarEstacionamentoButton;
    private JButton fecharButton;
    private JButton adicionarButton;
    private JButton removerButton;
    private JButton rankingFaturamentoButton;
    private JButton rankingUtilizacaoButton;
    private JButton menuClienteButton; // Adicione esta linha junto com os outros botões

    private ListarEstacionamentoController controller;

    public ListarEstacionamentosView(ListarEstacionamentoController controller) throws EstacionamentoDAOException {
        this.controller = controller;

        setTitle("Lista de Estacionamentos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(890, 500);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarEstacionamentosNaTabela();
        adicionarEventos();
    }

    private void inicializarComponentes() {
        panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        tabelaModelo = new DefaultTableModel(new Object[] { "ID", "Nome", "Endereço", "Telefone" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEstacionamentos = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaEstacionamentos);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        gerenciarEstacionamentoButton = new JButton("Gerenciar Estacionamento");
        fecharButton = new JButton("Fechar");
        adicionarButton = new JButton("Adicionar");
        removerButton = new JButton("Remover");
        rankingFaturamentoButton = new JButton("Ranking Faturamento");
        rankingUtilizacaoButton = new JButton("Ranking Utilizacao");
        menuClienteButton = new JButton("Menu Cliente");

        botoesPanel.add(menuClienteButton);
        botoesPanel.add(gerenciarEstacionamentoButton);
        botoesPanel.add(adicionarButton);
        botoesPanel.add(removerButton);
        botoesPanel.add(rankingFaturamentoButton);
        botoesPanel.add(rankingUtilizacaoButton);
        botoesPanel.add(fecharButton);

        panel.add(botoesPanel, BorderLayout.SOUTH);
    }

    private void adicionarEventos() {
        gerenciarEstacionamentoButton.addActionListener(e -> {
            try {
                gerenciarEstacionamentoSelecionado();
            } catch (EstacionamentoDAOException e1) {
                e1.printStackTrace();
            } catch (ClienteDAOException e1) {
                e1.printStackTrace();
            }
        });
        adicionarButton.addActionListener(e -> {
            try {
                adicionarEstacionamento();
            } catch (EstacionamentoDAOException e1) {
                e1.printStackTrace();
            }
        });
        removerButton.addActionListener(e -> {
            try {
                removerEstacionamento();
            } catch (EstacionamentoDAOException e1) {
                e1.printStackTrace();
            }
        });
        fecharButton.addActionListener(e -> dispose());
        rankingFaturamentoButton.addActionListener(e -> exibirRankingFaturamento());
        rankingUtilizacaoButton.addActionListener(e -> exibirRankingUtilizacao());
        menuClienteButton.addActionListener(e -> {
            try {
                abrirMenuCliente();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    private void carregarEstacionamentosNaTabela() throws EstacionamentoDAOException {
        tabelaModelo.setRowCount(0);
        List<EstacionamentoModel> estacionamentos = controller.listarEstacionamentos();

        for (EstacionamentoModel estacionamento : estacionamentos) {
            tabelaModelo.addRow(new Object[] {
                    estacionamento.getId(),
                    estacionamento.getNome(),
                    estacionamento.getEndereco(),
                    estacionamento.getTelefone()
            });
        }
    }

    private void adicionarEstacionamento() throws EstacionamentoDAOException {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do estacionamento:");
        String endereco = JOptionPane.showInputDialog(this, "Digite o endereço:");
        String telefone = JOptionPane.showInputDialog(this, "Digite o telefone:");
        String colunasStr = JOptionPane.showInputDialog(this, "Digite o número de colunas:");
        String vagasPorColunaStr = JOptionPane.showInputDialog(this, "Digite o número de vagas por coluna:");

        try {
            int colunas = Integer.parseInt(colunasStr);
            int vagasPorColuna = Integer.parseInt(vagasPorColunaStr);

            boolean sucesso = controller.adicionarEstacionamento(nome, endereco, telefone, colunas, vagasPorColuna);
            if (sucesso) {
                carregarEstacionamentosNaTabela();
                JOptionPane.showMessageDialog(this, "Estacionamento adicionado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar estacionamento.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerEstacionamento() throws EstacionamentoDAOException {
        int linhaSelecionada = tabelaEstacionamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estacionamento.");
            return;
        }

        int idEstacionamento = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
        boolean sucesso = controller.removerEstacionamento(idEstacionamento);

        if (sucesso) {
            carregarEstacionamentosNaTabela();
            JOptionPane.showMessageDialog(this, "Estacionamento removido com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover estacionamento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gerenciarEstacionamentoSelecionado() throws EstacionamentoDAOException, ClienteDAOException {
        int linhaSelecionada = tabelaEstacionamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estacionamento.");
            return;
        }

        int idEstacionamento = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
        EstacionamentoModel estacionamento = controller.buscarEstacionamentoPorId(idEstacionamento);

        if (estacionamento != null) {
            GerenciarView gerenciarView = new GerenciarView(estacionamento);
            gerenciarView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Estacionamento não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirRankingFaturamento() {
        // Cria uma nova janela para exibir o ranking
        JFrame rankingFrame = new JFrame("Ranking de Estacionamentos");
        rankingFrame.setSize(600, 400);
        rankingFrame.setLocationRelativeTo(this);

        // Configuração do layout e painel
        JPanel panel = new JPanel(new BorderLayout());
        rankingFrame.add(panel);

        // Tabela para exibir o ranking
        String[] colunas = { "Posição", "Nome", "Total Faturado" };
        DefaultTableModel rankingModel = new DefaultTableModel(colunas, 0);
        JTable tabelaRanking = new JTable(rankingModel);
        panel.add(new JScrollPane(tabelaRanking), BorderLayout.CENTER);

        // Chama o método no controller para buscar o ranking
        List<Object[]> ranking = controller.obterRankingEstacionamentos();

        // Adiciona os dados do ranking na tabela
        for (int i = 0; i < ranking.size(); i++) {
            Object[] linha = ranking.get(i);
            rankingModel.addRow(new Object[] { i + 1, linha[0], String.format("R$ %.2f", linha[1]) });
        }

        // Botão para fechar a janela
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener(e -> rankingFrame.dispose());
        panel.add(fecharButton, BorderLayout.SOUTH);

        rankingFrame.setVisible(true);
    }

    private void exibirRankingUtilizacao() {
        List<Map<String, Object>> ranking = controller.getRankingUtilizacao();

        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma utilização registrada.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Criar dados para exibição na tabela
        String[] colunas = { "ID", "Estacionamento", "Utilizações" };
        String[][] dados = new String[ranking.size()][3];

        for (int i = 0; i < ranking.size(); i++) {
            Map<String, Object> row = ranking.get(i);
            dados[i][0] = String.valueOf(row.get("idEstacionamento"));
            dados[i][1] = String.valueOf(row.get("nomeEstacionamento"));
            dados[i][2] = String.valueOf(row.get("totalUtilizacoes"));
        }

        // Exibir tabela em nova janela
        JTable tabelaRanking = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabelaRanking);

        JFrame rankingFrame = new JFrame("Ranking de Utilização");
        rankingFrame.setSize(400, 300);
        rankingFrame.setLocationRelativeTo(this);
        rankingFrame.add(scrollPane);
        rankingFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ListarEstacionamentoController controller = new ListarEstacionamentoController();
            try {
                ListarEstacionamentosView view = new ListarEstacionamentosView(controller);
                view.setVisible(true);
            } catch (EstacionamentoDAOException e) {
                e.printStackTrace();
            }
        });
    }

    private void abrirMenuCliente() throws ClienteDAOException {
        MenuCliente menuCliente = new MenuCliente();
        menuCliente.setVisible(true);
    }

}
