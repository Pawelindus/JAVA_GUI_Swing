public class Employee {
    private String forename;
    private String surname;
    private int salary;
    private Enum<Jobs> jobsEnum;

    public Employee() {
    }

    public Employee(String forename, String surname, Enum<Jobs> jobsEnum, int salary) {
        this.forename = forename;
        this.surname = surname;
        this.salary = salary;
        this.jobsEnum = jobsEnum;
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

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Enum<Jobs> getJobsEnum() {
        return jobsEnum;
    }

    public void setJobsEnum(Enum<Jobs> jobsEnum) {
        this.jobsEnum = jobsEnum;
    }

    @Override
    public String toString() {
        return forename + " " + surname + " " + jobsEnum + " " + salary;
    }
}


