package com.example.demo.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long departmentSeq) {
        super("Department not found with ID: " + departmentSeq);
    }
}
