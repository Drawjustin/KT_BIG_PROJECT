package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.util.List;


@Getter
@Entity
@Builder
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE complaint SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@AllArgsConstructor
@FilterDef(name = "deletedFilter")
@Filter(name = "deletedFilter", condition = "is_deleted = false")
@Table(name = "complaint")
public class Complaint extends baseEntity {

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

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    private List<ComplaintComment> complaintComment;

    @Column(name = "complaint_title", length = 256)
    private String complaintTitle;

    @Column(name = "complaint_count")
    private Byte complaintCount;

    @Column(name = "complaint_content", columnDefinition = "TEXT")
    private String complaintContent;

    @Column(name = "complaint_summary")
    private String complaintSummary;

    @Column(name = "complaint_combined")
    private String complaintCombined;

    @Column(name = "complaint_file_path", length = 256)
    private String complaintFilePath;

    @Column(name="is_answered")
    private Boolean isAnswered;

    @Column(name="is_bad")
    private Boolean isBad;

    @Builder
    public Complaint(Member member, Team team, String complaintTitle, String complaintContent, String complaintFilePath,Boolean isAnswered,Boolean isBad) {
        this.member = member;
        this.team = team;
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
        this.isAnswered=isAnswered;
        this.isBad=isBad;
    }

    public void updateComplaintFile(String title, String content, String newFilePath) {
        complaintTitle = title;
        complaintContent = content;
        complaintFilePath = newFilePath;
    }
    public void updateComplaint(String title, String content) {
        complaintTitle = title;
        complaintContent = content;
    }

}