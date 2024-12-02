package main;

import controller.ListarEstacionamentoController;
import dao.EstacionamentoDAOException;
import view.ListarEstacionamentosView;

public class Main {
    public static void main(String[] args) throws EstacionamentoDAOException {
        ListarEstacionamentoController controller = new ListarEstacionamentoController();
        ListarEstacionamentosView listarEstacionamentosView = new ListarEstacionamentosView(controller);

        listarEstacionamentosView.setVisible(true);
    }
}
