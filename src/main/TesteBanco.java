package main;

public class TesteBanco {
    public static void main(String[] args) {
        // Inserindo jogadores fictícios
        JogadorDAO.inserirJogador("Letícia", 22, "leticia@email.com", 3, 850);
        JogadorDAO.inserirJogador("João", 25, "joao@email.com", 5, 1200);

        // Mostrando ranking
        Ranking.mostrarRanking();
    }
}
