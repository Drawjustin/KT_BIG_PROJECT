package com.example.demo.dto.complaintDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIComplaintRequestDTO {
    private String text;
    private List<PastComplaint> pastComplaints;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PastComplaint {
        private String text;
    }
}
