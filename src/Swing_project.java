import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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

        //fileChooser Filter
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

        ReadFromSettings rfs = new ReadFromSettings();
        panelMain.setBackground(new Color(rfs.getRedL(), rfs.getGreenL(), rfs.getBlueL()));

        //panelFilter.setPreferredSize(new Dimension(700, 300));
        panelFilter.add(textField, BorderLayout.CENTER);
        panelFilter.add(buttonClrFilter, BorderLayout.LINE_END);
        panelFilter.add(panelRadioBut, BorderLayout.LINE_START);
        panelRadioBut.add(forenameChBox);
        panelRadioBut.add(surnameChBox);
        panelRadioBut.add(jobChBox);
        panelRadioBut.add(yearsChBox);
        panelRadioBut.add(salaryChBox);

        //CheckIfSalaryGood Timer
        Timer timer = new Timer(100, e -> CheckIfSalaryGood(table.get()));
        timer.start();

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
        buttonClrFilter.addActionListener(e ->

        {
            textField.setText("");
            new MyRowFilter(table.get(), textField, checkBoxes);
        });

        ActionListener refreshRowListener = e -> new MyRowFilter(table.get(), textField, checkBoxes);
        for (
                int i = 0;
                i < 5; i++) {
            checkBoxes[i].addActionListener(refreshRowListener);
        }

        //MenuBar Section
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuTable = new JMenu("Table");
        JMenu menuWindow = new JMenu("Window");
        JMenuItem openItem = new JMenuItem("Open File");
        JMenuItem saveItem = new JMenuItem("Save File");
        JMenuItem newTableItem = new JMenuItem("New Table");
        JMenuItem addRowItem = new JMenuItem("Add Row");
        JMenuItem removeRowItem = new JMenuItem("Remove Row");
        JMenuItem destroyTableItem = new JMenuItem("Destroy Table");
        JMenuItem changeColorItem = new JMenuItem("Change Bg Color");
        frame.setJMenuBar(menuBar);
        menuBar.add(menuFile);
        menuBar.add(menuTable);
        menuBar.add(menuWindow);
        menuFile.add(openItem);
        menuFile.add(saveItem);
        menuTable.add(newTableItem);
        menuTable.add(addRowItem);
        menuTable.add(removeRowItem);
        menuTable.add(destroyTableItem);
        menuWindow.add(changeColorItem);


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
                    model.set(new DefaultTableModel(data, columnNames) {
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            if (columnIndex == 0) {
                                return String.class;
                            }
                            if (columnIndex == 1) {
                                return String.class;
                            }
                            if (columnIndex == 2) {
                                return Enum.class;
                            }
                            if (columnIndex == 3) {
                                return Integer.class;
                            }
                            if (columnIndex == 4) {
                                return Integer.class;
                            } else return String.class;
                        }
                    });
                    table.set(new JTable(model.get()));

                    //Adding JTable to panelMain
                    new CreateTable(panelMain, frame, table.get());
                    panelMain.add(panelFilter, BorderLayout.PAGE_END);
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Wybierz właściwy format pliku.", "Zły format pliku", JOptionPane.WARNING_MESSAGE));
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
        addRowItem.addActionListener(e -> model.get().addRow(new Object[]{"", "", Jobs.EMPTY, 0, 0}));

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
            model.set(new DefaultTableModel(new Object[][]{{"", "", Jobs.EMPTY, 0, 0}}, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0) {
                        return String.class;
                    }
                    if (columnIndex == 1) {
                        return String.class;
                    }
                    if (columnIndex == 2) {
                        return Enum.class;
                    }
                    if (columnIndex == 3) {
                        return Integer.class;
                    }
                    if (columnIndex == 4) {
                        return Integer.class;
                    } else return String.class;
                }
            });
            table.set(new JTable(model.get()));
            new CreateTable(panelMain, frame, table.get());
            panelMain.add(panelFilter, BorderLayout.PAGE_END);
        });

        //Change color dialog and listener
        JDialog dialog = new JDialog();
        changeColorItem.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 1));
            JScrollBar scrollBarRed = new JScrollBar();
            JScrollBar scrollBarGreen = new JScrollBar();
            JScrollBar scrollBarBlue = new JScrollBar();

            scrollBarRed.setOrientation(Adjustable.HORIZONTAL);
            scrollBarGreen.setOrientation(Adjustable.HORIZONTAL);
            scrollBarBlue.setOrientation(Adjustable.HORIZONTAL);
            panel.add(scrollBarRed);
            panel.add(scrollBarGreen);
            panel.add(scrollBarBlue);

            dialog.setVisible(true);
            dialog.setContentPane(panel);
            dialog.setSize(new Dimension(300, 200));
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            scrollBarRed.setBackground(Color.red);
            scrollBarGreen.setBackground(Color.green);
            scrollBarBlue.setBackground(Color.blue);

            scrollBarRed.setMinimum(0);
            scrollBarRed.setMaximum(255);
            scrollBarGreen.setMinimum(0);
            scrollBarGreen.setMaximum(255);
            scrollBarBlue.setMinimum(0);
            scrollBarBlue.setMaximum(255);

            int[] red = new int[1];
            int[] green = new int[1];
            int[] blue = new int[1];

            ReadFromSettings rfsNew = new ReadFromSettings();
            int redL = rfsNew.getRedL();
            int greenL = rfsNew.getGreenL();
            int blueL = rfsNew.getBlueL();

            panelMain.setBackground(new Color(redL, greenL, blueL));
            scrollBarRed.setValue(redL);
            scrollBarGreen.setValue(greenL);
            scrollBarBlue.setValue(blueL);

            red[0] = scrollBarRed.getValue();
            green[0] = scrollBarGreen.getValue();
            blue[0] = scrollBarBlue.getValue();


            scrollBarRed.addAdjustmentListener(e1 -> {
                red[0] = scrollBarRed.getValue();
                panelMain.setBackground(new Color(red[0], green[0], blue[0]));
                try {
                    PrintStream printStream = new PrintStream("settings.ini");
                    printStream.print(red[0] + " " + green[0] + " " + blue[0]);
                    printStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });

            scrollBarGreen.addAdjustmentListener(e2 -> {
                green[0] = scrollBarGreen.getValue();
                panelMain.setBackground(new Color(red[0], green[0], blue[0]));
                try {
                    PrintStream printStream = new PrintStream("settings.ini");
                    printStream.print(red[0] + " " + green[0] + " " + blue[0]);
                    printStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            scrollBarBlue.addAdjustmentListener(e3 -> {
                blue[0] = scrollBarBlue.getValue();
                panelMain.setBackground(new Color(red[0], green[0], blue[0]));
                try {
                    PrintStream printStream = new PrintStream("settings.ini");
                    printStream.print(red[0] + " " + green[0] + " " + blue[0]);
                    printStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        });
        destroyTableItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK));
        destroyTableItem.addActionListener(e -> {
            panelMain.removeAll();
            panelMain.repaint();
            panelMain.revalidate();
        });
    }

    private static void KeyboardClicked(JTextField textField, JTable table, JCheckBox[] checkBoxes) {
        if (textField.getText().length() >= 1) {
            if (textField.getText().charAt(0) != '>' && textField.getText().charAt(0) != '<') {
                new MyRowFilter(table, textField, checkBoxes);
            } else if (textField.getText().charAt(0) == '>' || textField.getText().charAt(0) == '<') {
                new MyNumberRowFilter(table, textField);
            }
        } else {
            new MyRowFilter(table, textField, checkBoxes);
        }
    }

    private static void CheckIfSalaryGood(JTable table) {
        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                Jobs jobsEnum = Jobs.EMPTY;
                if (table.getValueAt(i, 2).equals(Jobs.CEO)) {
                    jobsEnum = Jobs.CEO;
                }
                if (table.getValueAt(i, 2).equals(Jobs.MANAGER)) {
                    jobsEnum = Jobs.MANAGER;
                }
                if (table.getValueAt(i, 2).equals(Jobs.ACCOUNTING)) {
                    jobsEnum = Jobs.ACCOUNTING;
                }
                if (table.getValueAt(i, 2).equals(Jobs.MARKETING)) {
                    jobsEnum = Jobs.MARKETING;
                }
                if (table.getValueAt(i, 2).equals(Jobs.QUALITY_CONTROL)) {
                    jobsEnum = Jobs.QUALITY_CONTROL;
                }
                if (table.getValueAt(i, 2).equals(Jobs.RECEPTIONIST)) {
                    jobsEnum = Jobs.RECEPTIONIST;
                }
                if (Integer.parseInt(table.getValueAt(i, 4).toString()) < jobsEnum.getMin()) {
                    table.setValueAt(jobsEnum.getMin(), i, 4);
                    int finalI = i;
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Zbyt mała wartość pensji w rzędzie: " + finalI, "Błąd wartości pensji", JOptionPane.INFORMATION_MESSAGE));
                }
                if (Integer.parseInt(table.getValueAt(i, 4).toString()) > jobsEnum.getMax()) {
                    table.setValueAt(jobsEnum.getMax(), i, 4);
                    int finalI = i;
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Zbyt duża wartość pensji w rzędzie: " + finalI, "Błąd wartość pensji", JOptionPane.INFORMATION_MESSAGE));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nie można sprawdzić, czy wprowadzona pensja jest prawidłowa", "Błąd sprawdzania pensji", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }
}

class Employee {
    private String forename;
    private String surname;
    private Enum<Jobs> jobsEnum;
    private Integer yearsOfWork;
    private Integer salary;


    public Employee() {
    }

    public Employee(String forename, String surname, Enum<Jobs> jobsEnum, Integer yearsOfWork, Integer salary) {
        this.forename = forename;
        this.surname = surname;
        this.jobsEnum = jobsEnum;
        this.yearsOfWork = yearsOfWork;
        this.salary = salary;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Enum<Jobs> getJobsEnum() {
        return jobsEnum;
    }

    public void setJobsEnum(Enum<Jobs> jobsEnum) {
        this.jobsEnum = jobsEnum;
    }

    public Integer getYearsOfWork() {
        return yearsOfWork;
    }

    public void setYearsOfWork(int yearsOfWork) {
        this.yearsOfWork = yearsOfWork;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return forename + " " + surname + " " + jobsEnum + " " + salary;
    }
}

enum Jobs {
    CEO(5400, 12000),
    MANAGER(3500, 7500),
    ACCOUNTING(3000, 6500),
    MARKETING(3500, 7000),
    QUALITY_CONTROL(4200, 8000),
    RECEPTIONIST(3400, 6400),
    EMPTY(0, 0);

    private int min;
    private int max;

    Jobs(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

class GetFromTXT {
    public Employee[] getFromFile(String file, int lines) {
        Employee[] employees = new Employee[lines];
        int i;
        int j = 0;
        int spaceN = 0;
        String text = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((i = bufferedReader.read()) != -1) {
                if (spaceN == 0) {
                    employees[j] = new Employee();
                }
                if (spaceN == 0 && i != ' ') {
                    text += (char) i;
                }
                if (i == ' ' && spaceN == 0) {
                    employees[j].setForename(text);
                }
                if (spaceN == 1 && i != ' ') {
                    text += (char) i;
                }
                if (i == ' ' && spaceN == 1) {
                    employees[j].setSurname(text);
                }
                if (spaceN == 2 && i != ' ') {
                    text += (char) i;
                }
                if (i == ' ' && spaceN == 2) {
                    if (text.equals(Jobs.CEO.name())) {
                        employees[j].setJobsEnum(Jobs.CEO);
                    }
                    if (text.equals(Jobs.MANAGER.name())) {
                        employees[j].setJobsEnum(Jobs.MANAGER);
                    }
                    if (text.equals(Jobs.ACCOUNTING.name())) {
                        employees[j].setJobsEnum(Jobs.ACCOUNTING);
                    }
                    if (text.equals(Jobs.MARKETING.name())) {
                        employees[j].setJobsEnum(Jobs.MARKETING);
                    }
                    if (text.equals(Jobs.QUALITY_CONTROL.name())) {
                        employees[j].setJobsEnum(Jobs.QUALITY_CONTROL);
                    }
                    if (text.equals(Jobs.RECEPTIONIST.name())) {
                        employees[j].setJobsEnum(Jobs.RECEPTIONIST);
                    }
                    if (text.equals(Jobs.EMPTY.name())) {
                        employees[j].setJobsEnum(Jobs.EMPTY);
                    }
                }
                if (spaceN == 3 && i != ' ') {
                    text += (char) i;
                }
                if (i == ' ' && spaceN == 3) {
                    employees[j].setYearsOfWork(Integer.parseInt(text));
                }
                if (spaceN == 4 && i != '\r') {
                    text += (char) i;
                }
                if ((i == '\r' || i == ' ') && spaceN == 4) {
                    employees[j].setSalary(Integer.parseInt(text));
                }
                if (i == ' ') {
                    spaceN++;
                    text = "";
                }
                if (i == '\n') {
                    j++;
                    spaceN = 0;
                    text = "";
                }

            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }


        return employees;
    }

    public int countFileLines(String file) {
        int lines = 0;
        int i;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((i = bufferedReader.read()) != -1) {
                if (i == '\n') {
                    lines++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
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

class MyRowFilter {
    MyRowFilter(JTable table, JTextField textField, JCheckBox[] checkBoxes) {
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

class MyNumberRowFilter {
    MyNumberRowFilter(JTable table, JTextField textField) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        String value = "";
        if (textField.getText().charAt(0) == '>' && textField.getText().length() > 1) {
            for (int i = 0; i < textField.getText().length(); i++) {
                if (Character.isDigit(textField.getText().charAt(i))) {
                    try {
                        value = value + textField.getText().charAt(i);
                        sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Integer.parseInt(value), 4));
                    } catch (Exception e) {
                        textField.setText("");
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Podana liczba jest zbyt duża (Integer.MAX_VALUE)", "Osiągnięto limit inta.", JOptionPane.WARNING_MESSAGE));
                    }
                }
            }
        }
        if (textField.getText().charAt(0) == '<' && textField.getText().length() > 1) {
            for (int i = 0; i < textField.getText().length(); i++) {
                if (Character.isDigit(textField.getText().charAt(i))) {
                    try {
                        value = value + textField.getText().charAt(i);
                        sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, Integer.parseInt(value), 4));
                    } catch (Exception e) {
                        textField.setText("");
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Podana liczba jest zbyt duża (Integer.MAX_VALUE)", "Osiągnięto limit inta.", JOptionPane.WARNING_MESSAGE));
                    }
                }
            }
        }

    }
}

class ReadFromSettings {
    private int redL = 0;
    private int greenL = 0;
    private int blueL = 0;

    ReadFromSettings() {
        if (new File("settings.ini").exists()) {
            int i;
            String text = "";
            int spaceN = 0;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("settings.ini"));
                while ((i = bufferedReader.read()) != -1) {
                    if (i != ' ' && spaceN == 0) {
                        text += (char) i;
                        redL = Integer.parseInt(text);
                    }
                    if (i != ' ' && spaceN == 1) {
                        text += (char) i;
                        greenL = Integer.parseInt(text);
                    }
                    if (i != ' ' && spaceN == 2) {
                        text += (char) i;
                        blueL = Integer.parseInt(text);
                    }
                    if (i == ' ') {
                        spaceN++;
                        text = "";
                    }
                }
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Brak pliku 'Settings.ini', utworzono nowy plik ustawień.", "Brak pliku ustawień.", JOptionPane.WARNING_MESSAGE));
            try {
                new PrintStream("settings.ini").print("255 255 255");
                redL = 255;
                greenL = 255;
                blueL = 255;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public int getRedL() {
        return redL;
    }

    public int getGreenL() {
        return greenL;
    }

    public int getBlueL() {
        return blueL;
    }
}


