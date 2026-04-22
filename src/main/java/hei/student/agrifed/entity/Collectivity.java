package hei.student.agrifed.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data

public class Collectivity {
    private String id;
    private Integer federationNumber;
    private String name;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;

    public Collectivity() {}

    public Collectivity(String id, String location, CollectivityStructure structure, List<Member> members) {
        this.id = id;
        this.location = location;
        this.structure = structure;
        this.members = members;
    }
}
