import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public class Swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_project::CreateNewGUI);
    }

    private static void CreateNewGUI() {
        JFrame frame = new JFrame();
        JPanel panelMain = new JPanel(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();
        JPanel panelFilter = new JPanel(new BorderLayout());
        JPanel panelRadioBut = new JPanel();
        JTextField textField = new JTextField();
        JButton buttonClrFilter = new JButton("Clear");
        JCheckBox forenameChBox = new JCheckBox("Forename");
        JCheckBox surnameChBox = new JCheckBox("Surname");
        JCheckBox jobChBox = new JCheckBox("Job");
        JCheckBox yearsChBox = new JCheckBox("Years");
        JCheckBox salaryChBox = new JCheckBox("Salary");
        AtomicReference<DefaultTableModel> model = new AtomicReference<>(new DefaultTableModel());
        AtomicReference<JTable> table = new AtomicReference<>(new JTable(model.get()));

        JCheckBox[] checkBoxes = {forenameChBox, surnameChBox, jobChBox, yearsChBox, salaryChBox};

        FileFilter employeeFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".empl");
            }

            @Override
            public String getDescription() {
                return ".empl";
            }
        };
        fileChooser.setFileFilter(employeeFilter);


        //Table Column Names
        String[] columnNames = {"Forename", "Surname", "Job position", "Years of work", "Salary"};

        //Frame and panelMain Section
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelMain);
        frame.setSize(700, 500);
        panelMain.setPreferredSize(new Dimension(700, 500));
        panelMain.setBackground(Color.getHSBColor(12, 12, 12));

        //panelFilter.setPreferredSize(new Dimension(700, 300));
        panelFilter.add(textField, BorderLayout.CENTER);
        panelFilter.add(buttonClrFilter, BorderLayout.LINE_END);
        panelFilter.add(panelRadioBut, BorderLayout.LINE_START);
        panelRadioBut.add(forenameChBox);
        panelRadioBut.add(surnameChBox);
        panelRadioBut.add(jobChBox);
        panelRadioBut.add(yearsChBox);
        panelRadioBut.add(salaryChBox);

        //TextField Section
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                KeyboardClicked(textField, table.get(), checkBoxes);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                KeyboardClicked(textField, table.get(), checkBoxes);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeyboardClicked(textField, table.get(), checkBoxes);
            }
        });


        //Button and RadioButton Section
        buttonClrFilter.addActionListener(e -> {
            textField.setText("");
            new myRowFilter(table.get(), textField, checkBoxes);
        });

        ActionListener refreshRowListener = e -> new myRowFilter(table.get(), textField, checkBoxes);
        for (int i = 0; i < 5; i++) {
            checkBoxes[i].addActionListener(refreshRowListener);
        }


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
            int info = fileChooser.showOpenDialog(frame);
            if (fileChooser.getSelectedFile() != null && info == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().getName().endsWith(".empl")) {
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
                    model.set(new DefaultTableModel(data, columnNames));
                    table.set(new JTable(model.get()));

                    //Adding JTable to panelMain
                    new CreateTable(panelMain, frame, table.get());
                    panelMain.add(panelFilter, BorderLayout.PAGE_END);
                } else {
                    JOptionPane.showMessageDialog(frame, "Wybierz właściwy format pliku.");

                }
            }
        });

        //SaveItem Section
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> {
            int info = fileChooser.showSaveDialog(frame);
            if (info == JFileChooser.APPROVE_OPTION) {
                String option = fileChooser.getSelectedFile().getAbsolutePath();
                String filename;
                if (fileChooser.getSelectedFile().getName().endsWith(".empl")) {
                    filename = option;
                } else {
                    filename = (option + ".empl");
                }
                File file = new File(filename);
                PrintStream fileStream = null;
                try {
                    fileStream = new PrintStream(file);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                for (int i = 0; i < table.get().getRowCount(); i++) {
                    for (int j = 0; j < table.get().getColumnCount(); j++) {
                        if (fileStream != null) {
                            if (j == 4) {
                                fileStream.print(table.get().getModel().getValueAt(i, j) + "\r\n");
                            } else {
                                fileStream.print(table.get().getModel().getValueAt(i, j) + " ");
                            }
                        }
                    }
                }
            }

        });

        addRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        addRowItem.addActionListener(e -> {
            model.set((DefaultTableModel) table.get().getModel());
            model.get().addRow(new Object[]{"", "", Jobs.NO_POSITION, 0, 0});
        });
        table.get().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        removeRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        removeRowItem.addActionListener(e -> {
            int row = table.get().getSelectedRow();
            if (row != -1) {
                row = table.get().convertRowIndexToModel(row);
                model.get().removeRow(row);
            }
        });

        newTableItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        newTableItem.addActionListener(e -> {
            panelMain.removeAll();
            model.set(new DefaultTableModel(new Object[][]{{"", "", Jobs.NO_POSITION, 0, 0}}, columnNames));
            table.set(new JTable(model.get()));
            new CreateTable(panelMain, frame, table.get());
            panelMain.add(panelFilter, BorderLayout.PAGE_END);
        });

    }

    private static void KeyboardClicked(JTextField textField, JTable table, JCheckBox[] checkBoxes ){
        if (textField.getText().length() >= 1) {
            if (textField.getText().charAt(0) != '>') {
                new myRowFilter(table, textField, checkBoxes);
            } else  if (textField.getText().charAt(0) == '>') {
                new myNumberRowFilter(table, textField);
            }
        } else {
            new myRowFilter(table, textField, checkBoxes);
        }
    }
}

class CreateTable {
    CreateTable(JPanel panel, JFrame frame, JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredScrollableViewportSize(new Dimension(700, 100));
        table.setFillsViewportHeight(true);

        panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(table, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);

        TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<>(table.getModel());

        class IntComparator implements Comparator<Object> {
            public int compare(Object o1, Object o2) {
                Integer int1 = Integer.parseInt(o1.toString());
                Integer int2 = Integer.parseInt(o2.toString());
                return int1.compareTo(int2);
            }
        }
        class StringComparator implements Comparator<String> {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        }
        class EnumComparator implements Comparator<Jobs> {
            public int compare(Jobs o1, Jobs o2) {
                return o1.compareTo(o2);
            }
        }
        tableRowSorter.setComparator(0, new StringComparator());
        tableRowSorter.setComparator(1, new StringComparator());
        tableRowSorter.setComparator(2, new EnumComparator());
        tableRowSorter.setComparator(3, new IntComparator());
        tableRowSorter.setComparator(4, new IntComparator());
        table.setRowSorter(tableRowSorter);
        JComboBox<Jobs> comboBox = new JComboBox<>();
        comboBox.addItem(Jobs.CEO);
        comboBox.addItem(Jobs.ACCOUNTING);
        comboBox.addItem(Jobs.MANAGER);
        comboBox.addItem(Jobs.MARKETING);
        comboBox.addItem(Jobs.QUALITY_CONTROL);
        comboBox.addItem(Jobs.RECEPTIONIST);
        TableColumn jobColumn = table.getColumnModel().getColumn(2);
        jobColumn.setCellEditor(new DefaultCellEditor(comboBox));

        panel.revalidate();
        frame.revalidate();
    }
}

class myRowFilter {
    myRowFilter(JTable table, JTextField textField, JCheckBox[] checkBoxes) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        RowFilter<TableModel, Object> rf;
        try {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                if (checkBoxes[i].isSelected()) {
                    list.add(i);
                }
            }
            int[] tab = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                tab[i] = list.get(i);
            }
            if (tab.length != 0) {
                rf = RowFilter.regexFilter(textField.getText(), tab);
            } else {
                rf = RowFilter.regexFilter(textField.getText());
            }

        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        if (!textField.getText().equals("") && table.getRowCount() != 0) {

            table.setRowSelectionInterval(0, table.getRowCount() - 1);
        }
        sorter.setRowFilter(rf);
    }
}

// TODO: 08.05.2021  
class myNumberRowFilter {
    myNumberRowFilter(JTable table, JTextField textField) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        String value = "";
        if (textField.getText().charAt(0) == '>' && textField.getText().length() > 1) {
            for (int i = 0; i < textField.getText().length(); i++) {
                if (Character.isDigit(textField.getText().charAt(i))) {
                    try {
                        value = value + textField.getText().charAt(i);
                        sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Integer.parseInt(value), 4));
                    } catch (Exception e){textField.setText("");
                        JOptionPane.showMessageDialog(table, "Podana liczba jest zbyt duża (Integer.MAX_VALUE)");}
                }
            }
        }
    }
}


