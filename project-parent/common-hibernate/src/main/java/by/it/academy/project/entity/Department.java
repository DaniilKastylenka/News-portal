package by.it.academy.project.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DEPARTMENT")
@ToString
@Entity

public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID", unique = true)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    private Set<Employee> employees = new HashSet<>();

    public Department(String name){
        this.name = name;
    }
}
