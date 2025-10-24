package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {

    private JTextField nomeField;
    private JTextField emailField;
    private JTextField idadeField;
    private JButton entrarButton;

    public LoginScreen() {
        setTitle("Login - Tetris");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nomeLabel = new JLabel("Nome:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel idadeLabel = new JLabel("Idade:");

        nomeField = new JTextField();
        emailField = new JTextField();
        idadeField = new JTextField();

        entrarButton = new JButton("Entrar");

        add(nomeLabel); add(nomeField);
        add(emailLabel); add(emailField);
        add(idadeLabel); add(idadeField);
        add(new JLabel()); add(entrarButton);

        entrarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String email = emailField.getText();
            String idade = idadeField.getText();

            if (nome.isEmpty() || email.isEmpty() || idade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            dispose(); // fecha tela de login
            new PrincipalMenu(nome); // abre o menu principal e passa o nome do jogador
        });

        setVisible(true);
    }
}
