package com.example.demo.utils;

import com.example.demo.entity.*;
import com.example.demo.entity.embeddable.FileDepartmentId;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitSetting {
    private final InitService initService;

    @PostConstruct
    public void init(){initService.init();}

    @Component
    static class InitService{
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){
            InitAdmin();
            InitUser();
            InitChatBox();
            InitChat();
            InitFile();
            InitDepartment();
            InitTelecom();
            InitFileDepartment();
            InitMember();
            InitBlock();
            InitComplaint();
            InitComplaintComment();
        }

        private void InitDepartment() {
            Department department = Department.builder()
                    .departmentName("KT")
                    .build();
            em.persist(department);
        }

        private void InitTelecom() {
            Department department = em.find(Department.class, 1L);
            Telecom teleContent = Telecom.builder()
                    .telecomContent("teleContent")
                    .telecomCount((byte) 2)
                    .telecomFilePath("C:\\HOME\\BACK\\IWANT")
                    .isComplain(false)
                    .department(department)
                    .build();
            em.persist(teleContent);
        }

        private void InitMember() {
            Member member = Member.builder()
                    .memberId("guswhd903")
                    .memberName("현종")
                    .memberPassword("1234")
                    .memberEmail("guswhd903@naver.com")
                    .build();
            em.persist(member);
        }

        private void InitFileDepartment() {
            File file = em.find(File.class, 1L);
            Department department = em.find(Department.class, 1L);

            FileDepartmentId fileDepartmentId = FileDepartmentId.builder()
                    .fileSeq(file.getFileSeq())
                    .departmentSeq(department.getDepartmentSeq())
                    .build();
            FileDepartment fileDepartment = FileDepartment.builder()
                    .fileDepartmentId(fileDepartmentId)
                    .file(file)
                    .department(department)
                    .build();
            em.persist(fileDepartment);
        }

        private void InitFile() {
            Admin admin = em.find(Admin.class, 1L);
            File file = File.builder()
                    .admin(admin)
                    .fileContent("fileContent")
                    .filePath("filePath")
                    .fileTitle("fileTitle")
                    .fileType("fileType")
                    .build();
            em.persist(file);
        }

        private void InitComplaintComment() {
            Complaint complaint = em.find(Complaint.class, 1L);
            ComplaintComment complaintComment = ComplaintComment.builder()
                    .complaint(complaint)
                    .complaintCommentContent("complaintCommentContent")
                    .build();
            em.persist(complaintComment);
        }

        private void InitComplaint() {
            User user = em.find(User.class, 1L);
            Department department = em.find(Department.class, 1L);
            Member member = em.find(Member.class, 1L);
            Complaint complaint = Complaint.builder()
                    .complaintContent("complaintContent")
                    .complaintTitle("complainTitle")
                    .complaintFilePath("complainFilePath")
                    .user(user)
                    .department(department)
                    .member(member)
                    .build();
            em.persist(complaint);
        }

        private void InitChatBox() {
            ChatBox chatBox = ChatBox.builder()
                    .chatBoxTitle("chatBoxTitle")
                    .build();
            em.persist(chatBox);
        }

        private void InitChat() {
            ChatBox chatBox = em.find(ChatBox.class, 1L);
            User user = em.find(User.class, 1L);
            Chat chat = Chat.builder()
                    .chatBox(chatBox)
                    .chatContent("chatContent")
                    .chatFilepath("chatFilePath")
                    .chatQuestion("chatQuestion")
                    .user(user)
                    .build();
            em.persist(chat);
        }

        private void InitBlock() {
            Member member = em.find(Member.class, 1L);
            Department department = em.find(Department.class, 1L);
            Block build = Block.builder()
                    .member(member)
                    .department(department)
                    .build();
            em.persist(build);
        }


        private void InitAdmin() {
            Admin admin1 = Admin.builder()
                    .userId("adminID")
                    .userEmail("guswhd903@naver.com")
                    .userPassword("1234")
                    .userName("현종")
                    .userNumber("010-1234-5678")
                    .build();
            em.persist(admin1);
        }


        public void InitUser(){
            User user = new User("guswhd903", "1234", "현종", "010-1234-5678", "guswhd903@naver.com");
            em.persist(user);
        }


    }
}
