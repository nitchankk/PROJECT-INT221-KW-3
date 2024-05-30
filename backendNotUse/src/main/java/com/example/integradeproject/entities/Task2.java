package com.example.integradeproject.entities;


import com.example.integradeproject.services.CharConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

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

