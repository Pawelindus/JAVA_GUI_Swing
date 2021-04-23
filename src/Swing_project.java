import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();
        JScrollPane scrollPane = new JScrollPane(panelMain);
        JTable[] table = new JTable[1];


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
        JMenu menuFile = new JMenu("File");
        JMenu menuTable = new JMenu("Table");
        JMenuItem openItem = new JMenuItem("Open File");
        JMenuItem saveItem = new JMenuItem("Save File");
        JMenuItem addRowItem = new JMenuItem("Add Row");
        JMenuItem removeRowItem = new JMenuItem("Remove Row");
        frame.setJMenuBar(menuBar);
        menuBar.add(menuFile);
        menuBar.add(menuTable);
        menuFile.add(openItem);
        menuFile.add(saveItem);
        menuTable.add(addRowItem);
        menuTable.add(removeRowItem);

        //OpenItem Section
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> {
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
            table[0] = new JTable(new DefaultTableModel(data, columnNames));
            table[0].getTableHeader().setReorderingAllowed(false);
            table[0].setAutoCreateRowSorter(true);

            //Adding JTable to panelMain
            panelMain.add(table[0].getTableHeader(), BorderLayout.PAGE_START);
            panelMain.add(table[0], BorderLayout.CENTER);
            panelMain.revalidate();
        });

        //SaveItem Section
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> {
            fileChooser.showSaveDialog(frame);
            String option = fileChooser.getSelectedFile().getAbsolutePath();
            File file = new File(option);
            PrintStream fileStream = null;
            try {
                fileStream = new PrintStream(file);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            for (int i = 0; i < table[0].getColumnCount() - 1; i++) {
                assert fileStream != null;
                fileStream.println(table[0].getModel().getValueAt(i, 0) + " " + table[0].getModel().getValueAt(i, 1) + " " + table[0].getModel().getValueAt(i, 2) + " " + table[0].getModel().getValueAt(i, 3));
            }


        });

        addRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        addRowItem.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table[0].getModel();
            model.addRow(new Object[]{"", "", "", ""});
        });

        removeRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        removeRowItem.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table[0].getModel();
            if (table[0].isRowSelected(table[0].getSelectedRow())) {

                model.removeRow(table[0].getSelectedRow());
            }
        });


    }
}
