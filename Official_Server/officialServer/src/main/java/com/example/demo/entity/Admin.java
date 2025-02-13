package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
//@SQLDelete(sql = "UPDATE admin SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name = "admin")
public class Admin extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_seq")
    private Long adminSeq;

    @Column(name = "admin_id", nullable = false, length = 20)
    private String adminId;

    @Column(name = "admin_password", nullable = false, length = 512)
    private String adminPassword;

    @Builder
    public Admin(String adminId, String adminPassword) {
        this.adminId = adminId;
        this.adminPassword = adminPassword;
    }
}