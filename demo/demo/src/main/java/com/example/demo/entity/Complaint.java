package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key, AUTO_INCREMENT
    @Column(name = "complaint_seq")
    private Long complaintSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq",nullable = false)
    private Team team;

    @Column(name = "complaint_title", length = 256)
    private String complaintTitle;

    @Column(name = "complaint_content", columnDefinition = "TEXT")
    private String complaintContent;

    @Column(name = "complaint_file_path", length = 256)
    private String complaintFilePath;

    @Builder
    public Complaint(Member member, Team team, String complaintTitle, String complaintContent, String complaintFilePath) {
        this.member = member;
        this.team = team;
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
    }
}