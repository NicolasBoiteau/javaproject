import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationLauncher extends JFrame {
    

    public ApplicationLauncher() {
        super("Launcher");

        JButton button1 = new JButton("Lancer Add to Menu");
        JButton button2 = new JButton("Lancer Command(client Version)");
        JButton button3 = new JButton("Lancer Add to stock");
        JButton button4 = new JButton("Lancer DashBoard");
        JButton button5 = new JButton("Lancer Wait time calculator");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BurgerMenuSwing.main(new String[]{});
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestaurantDashboard.main(new String[]{});
            }
        });
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WaitTimeCalculatorGUI.main(new String[]{});
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BurgerOrderApp.main(new String[]{});
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockAdditionGUI.main(new String[]{});
            }
        });

        JPanel panel = new JPanel();
        panel.add(button1);
        panel.add(button2);
        
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);

        add(panel);

        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ajoutez un WindowListener pour personnaliser le comportement de fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Fermez uniquement cette fenêtre, pas toutes les fenêtres
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ApplicationLauncher();
    }
   
}
