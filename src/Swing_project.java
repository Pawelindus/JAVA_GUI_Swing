import javax.swing.*;
import java.awt.event.*;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
        Employee[] employees;
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        String[] columnNames = {"Forename", "Surname", "Job position", "Salary"};

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setSize(500, 500);

        //MenuBarSection
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open File");
        frame.setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(openItem);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(frame);
                String option = fileChooser.getSelectedFile().getAbsolutePath();
                GetFromTXT getFromTXT = new GetFromTXT();
                int lines = getFromTXT.countFileLines(option);
                Employee[] employees = getFromTXT.getFromFile(option, lines);
                JList<Employee> list = new JList<>(employees);
                panelMain.add(list);
                panelMain.revalidate();
            }
        });



    }
}
