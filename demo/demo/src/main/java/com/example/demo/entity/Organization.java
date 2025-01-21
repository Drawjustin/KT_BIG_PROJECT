package com.example.demo.entity;
import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder // Lombok Builder 추가
@Table(name = "organization")
public class Organization extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_seq")
    private Long organizationSeq; // 기관 고유번호

    private String organizationName;
}