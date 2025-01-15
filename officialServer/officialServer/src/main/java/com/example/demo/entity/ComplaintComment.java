package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "complaint_comment")
public class ComplaintComment extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintCommentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaintSeq")
    private Complaint complaint;
    private String complaintCommentContent;


}
