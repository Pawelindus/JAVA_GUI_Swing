public enum Jobs {
    CEO(5400,12000),
    MANAGER(3500,7500),
    ACCOUNTING(3000, 6500),
    MARKETING(3500, 7000),
    QUALITY_CONTROL(4200, 8000),
    RECEPTIONIST(3400, 6400),
    EMPTY(0,0);

     private int min;
     private int max;
    Jobs(int min, int max){
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
