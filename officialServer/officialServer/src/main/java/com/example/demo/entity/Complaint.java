package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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


}
