public class Employee {
    private String forename;
    private String surname;
    private Enum<Jobs> jobsEnum;
    private int yearsOfWork;
    private int salary;


    public Employee() {
    }

    public Employee(String forename, String surname, Enum<Jobs> jobsEnum, int yearsOfWork, int salary) {
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

    public int getYearsOfWork() {
        return yearsOfWork;
    }

    public void setYearsOfWork(int yearsOfWork) {
        this.yearsOfWork = yearsOfWork;
    }

    public int getSalary() {
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


