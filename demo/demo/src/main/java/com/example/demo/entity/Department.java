package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@SQLDelete(sql = "UPDATE department SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Builder
@Table(name = "department")
public class Department extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_seq")
    private Long departmentSeq; // 부서 고유번호

    @Column(name = "department_name", nullable = false, length = 50)
    private String departmentName; // 부서 이름

    @Column(name="department_number",nullable = false, length=30)
    private String departmentNumber; //부서 전화번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_seq", nullable = false)
    private District district;

    @Builder
    public Department(String departmentName, String departmentNumber, District district) {
        this.departmentName = departmentName;
        this.departmentNumber=departmentNumber;
        this.district = district;
    }
}