package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "complaint")
public class Complaint extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentSeq")
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberSeq")
    private Member member;
    @JoinColumn(name = "userSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String complaintTitle;
    private String complaintContent;
    private String complaintFilePath;

    @Builder
    public Complaint(Department department, Member member, User user, String complaintTitle, String complaintContent, String complaintFilePath) {
        this.department = department;
        this.member = member;
        this.user = user;
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
    }
}
