import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
                    if (text.equals(Jobs.CEO.getLabel())) {
                        employees[j].setJobsEnum(Jobs.CEO);
                    }
                    if (text.equals(Jobs.MANAGER.getLabel())) {
                        employees[j].setJobsEnum(Jobs.MANAGER);
                    }
                    if (text.equals(Jobs.ACCOUNTING.getLabel())) {
                        employees[j].setJobsEnum(Jobs.ACCOUNTING);
                    }
                    if (text.equals(Jobs.MARKETING.getLabel())) {
                        employees[j].setJobsEnum(Jobs.MARKETING);
                    }
                    if (text.equals(Jobs.QUALITY_CONTROL.getLabel())) {
                        employees[j].setJobsEnum(Jobs.QUALITY_CONTROL);
                    }
                    if (text.equals(Jobs.RECEPTIONIST.getLabel())) {
                        employees[j].setJobsEnum(Jobs.RECEPTIONIST);
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