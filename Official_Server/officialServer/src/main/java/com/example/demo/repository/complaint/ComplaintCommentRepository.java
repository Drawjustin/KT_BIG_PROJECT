package com.example.demo.repository.complaint;

import com.example.demo.entity.ComplaintComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment,Long> , ComplaintCommentRepositoryCustom{
}
