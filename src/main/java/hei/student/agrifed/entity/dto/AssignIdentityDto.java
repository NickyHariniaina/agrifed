package hei.student.agrifed.entity.dto;

public class AssignIdentityDto {
    private Integer federationNumber;
    private String name;

    public AssignIdentityDto() {}

    public Integer getFederationNumber() { return federationNumber; }
    public void setFederationNumber(Integer federationNumber) { this.federationNumber = federationNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
