package com.example.integradeproject.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskIdDTO {
    private String title;
    private String description;
    private String assignees;
    private String status;
    private Timestamp createdOn;
    private Timestamp updatedOn;
}



