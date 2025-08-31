package com.hkcapital.portoflio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

// -------- Parent Window --------
class MainFrame extends JFrame {
    private JTextField resultField;

    public MainFrame() {
        setTitle("Parent Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new FlowLayout());

        JButton openPopupButton = new JButton("Open Popup");
        resultField = new JTextField(20);

        add(openPopupButton);
        add(new JLabel("Received from Popup:"));
        add(resultField);

        // Open the popup when clicked
        openPopupButton.addActionListener(e -> {
            PopupDialog popup = new PopupDialog(this);
            popup.setVisible(true);  // Blocks until popup is closed
            String popupData = popup.getData(); // Get data from popup
            resultField.setText(popupData);
        });
    }
}

// -------- Popup Window --------
class PopupDialog extends JDialog {
    private JTextField inputField;
    private String data = "";

    public PopupDialog(JFrame parent) {
        super(parent, "Popup Window", true); // true = modal
        setSize(300, 150);
        setLayout(new FlowLayout());
        setLocationRelativeTo(parent);

        inputField = new JTextField(15);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        add(new JLabel("Enter data:"));
        add(inputField);
        add(okButton);
        add(cancelButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data = inputField.getText(); // Save data
                dispose(); // Close popup
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public String getData() {
        return data;
    }
}
