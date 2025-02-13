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
@Table(name = "district")
public class District extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_seq")
    private Long districtSeq; // 부서 고유번호

    @Column(name = "district_name", nullable = false, length = 50)
    private String districtName; // 부서 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_seq", nullable = false)
    private Organization organization; // Organization 관계 설정

    @Builder
    public District(String districtName, Organization organization) {
        this.districtName = districtName;
        this.organization = organization;
    }
}