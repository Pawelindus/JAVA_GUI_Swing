import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();
        JScrollPane scrollPane = new JScrollPane(panelMain);
        //ScrollPane
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //Table Column Names
        String[] columnNames = {"Forename", "Surname", "Job position", "Salary"};

        //Frame Section
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setSize(500, 500);

        //MenuBar Section
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

                //Data for JTable
                Object[][] data = new Object[lines][4];
                for (int i = 0; i < lines; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (j == 0) {
                            data[i][j] = employees[i].getForename();
                        } else if (j == 1) {
                            data[i][j] = employees[i].getSurname();
                        } else if (j == 2) {
                            data[i][j] = employees[i].getJobsEnum();
                        } else {
                            data[i][j] = employees[i].getSalary();
                        }
                    }
                }

                //JTable creation
                JTable table = new JTable(data, columnNames);
                table.getTableHeader().setReorderingAllowed(false);

                //Adding JTable to panelMain
                panelMain.add(table.getTableHeader(), BorderLayout.PAGE_START);
                panelMain.add(table, BorderLayout.CENTER);
                panelMain.revalidate();
            }
        });


    }
}
