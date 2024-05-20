package com.example.integradeproject.entities;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "tasks")


public class Task2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Integer taskId;
    private String title ;
    private String description ;
    private String assignees ;

    @ManyToOne
    @JoinColumn(referencedColumnName = "statusId" ,name ="statusId")
    private Status  statusId ;

    @Column(name = "createdOn", updatable = false, insertable = false)
    private Date createdOn;
    @Column(name = "updatedOn", updatable = false, insertable = false)
    private Date updatedOn;



}

