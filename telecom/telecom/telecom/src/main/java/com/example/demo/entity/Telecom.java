package com.example.demo.entity;
import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "telecom")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telecom extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telecom_seq")
    private Long telecomSeq;

    @Column(name = "telecom_content")
    private String telecomContent;

    @Column(name = "telecom_file_path")
    private String telecomFilePath;

    @Column(name = "is_complain")
    private Boolean isComplain;

    @Column(name = "telecom_count")
    private Byte telecomCount;

}

