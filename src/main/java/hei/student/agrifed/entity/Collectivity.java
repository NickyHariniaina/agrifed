package hei.student.agrifed.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Collectivity {
    private String id;
    private Integer federationNumber;
    private String name;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;
}
