package edu.hei.school.agricultural.api.model;

public class CollectivityLocalStatistics {
    public MemberDescription memberDescription;
    public Double earnedAmount;
    public Double unpaidAmount;
    public Double attendancePercentage;

    @Override
    public String toString() {
        return "CollectivityLocalStatistics{" +
                "memberDescription=" + memberDescription +
                ", earnedAmount=" + earnedAmount +
                ", unpaidAmount=" + unpaidAmount +
                ", attendancePercentage=" + attendancePercentage +
                '}';
    }
}
