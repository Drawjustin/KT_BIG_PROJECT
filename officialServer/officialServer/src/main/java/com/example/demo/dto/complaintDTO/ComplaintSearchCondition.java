package com.example.demo.dto.complaintDTO;

import com.example.demo.dto.base.BaseSearchCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
public class ComplaintSearchCondition extends BaseSearchCondition {
    private String author;
    private Boolean isAnswered;
}
