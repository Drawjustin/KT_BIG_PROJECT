package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "team")
public class Team extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_seq")
    private Long teamSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_seq")
    private Department department;

    @Column(name = "team_name")
    private String teamName;

    @Builder
    public Team(Department department, String teamName) {
        this.department = department;
        this.teamName = teamName;
    }
}
