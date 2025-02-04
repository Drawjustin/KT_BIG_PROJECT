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
//@SQLDelete(sql = "UPDATE organization SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Builder // Lombok Builder 추가
@Table(name = "organization")
public class Organization extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_seq")
    private Long organizationSeq; // 기관 고유번호

    @Column(name = "organization_name")
    private String organizationName;
}