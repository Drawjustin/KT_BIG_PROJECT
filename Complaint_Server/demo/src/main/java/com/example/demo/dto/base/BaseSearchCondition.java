package com.example.demo.dto.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class BaseSearchCondition {
    private String departmentName;
    private String title;
}
