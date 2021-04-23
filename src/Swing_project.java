import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicReference;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();
        AtomicReference<JTable> table = new AtomicReference<>(new JTable(new DefaultTableModel()));

        //Table Column Names
        String[] columnNames = {"Forename", "Surname", "Job position", "Years of work", "Salary"};

        //Frame and panelMain Section

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setSize(500, 500);
        panelMain.setPreferredSize(new Dimension(500, 500));
        panelMain.setBackground(Color.getHSBColor(12, 12, 12));

        //MenuBar Section
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuTable = new JMenu("Table");
        JMenuItem openItem = new JMenuItem("Open File");
        JMenuItem saveItem = new JMenuItem("Save File");
        JMenuItem newTableItem = new JMenuItem("New Table");
        JMenuItem addRowItem = new JMenuItem("Add Row");
        JMenuItem removeRowItem = new JMenuItem("Remove Row");
        frame.setJMenuBar(menuBar);
        menuBar.add(menuFile);
        menuBar.add(menuTable);
        menuFile.add(openItem);
        menuFile.add(saveItem);
        menuTable.add(newTableItem);
        menuTable.add(addRowItem);
        menuTable.add(removeRowItem);


        //OpenItem Section
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> {
            fileChooser.showOpenDialog(frame);
            if (fileChooser.getSelectedFile() != null) {
                panelMain.removeAll();
                String option = fileChooser.getSelectedFile().getAbsolutePath();
                GetFromTXT getFromTXT = new GetFromTXT();
                int lines = getFromTXT.countFileLines(option);
                Employee[] employees = getFromTXT.getFromFile(option, lines);

                //Data for JTable
                Object[][] data = new Object[lines][5];
                for (int i = 0; i < lines; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (j == 0) {
                            data[i][j] = employees[i].getForename();
                        } else if (j == 1) {
                            data[i][j] = employees[i].getSurname();
                        } else if (j == 2) {
                            data[i][j] = employees[i].getJobsEnum();
                        } else if (j == 3) {
                            data[i][j] = employees[i].getYearsOfWork();
                        } else {
                            data[i][j] = employees[i].getSalary();
                        }
                    }
                }

                //JTable creation
                table.set(new JTable(new DefaultTableModel(data, columnNames)));

                //Adding JTable to panelMain
                new CreateTable(panelMain, frame, table.get());
            }
        });

        //SaveItem Section
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> {
            fileChooser.showSaveDialog(frame);
            if (fileChooser.getSelectedFile() != null) {
                String option = fileChooser.getSelectedFile().getAbsolutePath();
                File file = new File(option);
                PrintStream fileStream = null;
                try {
                    fileStream = new PrintStream(file);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                for (int i = 0; i < table.get().getColumnCount() - 1; i++) {
                    assert fileStream != null;
                    fileStream.println(table.get().getModel().getValueAt(i, 0) + " " + table.get().getModel().getValueAt(i, 1) + " " + table.get().getModel().getValueAt(i, 2) + " " + table.get().getModel().getValueAt(i, 3));
                }
            }

        });

        addRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        addRowItem.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.get().getModel();
            model.addRow(new Object[]{"", "", "", "", ""});
        });

        removeRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        removeRowItem.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.get().getModel();
            if (table.get().isRowSelected(table.get().getSelectedRow())) {

                model.removeRow(table.get().getSelectedRow());
            }
        });

        newTableItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        newTableItem.addActionListener(e -> {
            panelMain.removeAll();
            table.set(new JTable(new DefaultTableModel(new Object[][]{{"", "", "", "", ""}}, columnNames)));
            new CreateTable(panelMain, frame, table.get());
        });


    }
}

class CreateTable {
    CreateTable(JPanel panel, JFrame frame, JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);

        panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(table, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);

        panel.revalidate();
        frame.revalidate();
    }
}
