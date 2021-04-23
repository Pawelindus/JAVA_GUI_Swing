import javax.swing.*;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setSize(500,500);
    }
}
