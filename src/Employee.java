public class Employee {
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


