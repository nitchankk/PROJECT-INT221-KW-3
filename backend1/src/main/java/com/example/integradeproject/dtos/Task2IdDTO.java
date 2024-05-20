package com.example.integradeproject.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Task2IdDTO {
        private String title;
        private String description;
        private String assignees;
        private String statusName;
        private Timestamp createdOn;
        private Timestamp updatedOn;
}
