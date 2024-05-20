package com.example.integradeproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.integradeproject.entities.Status;
import com.example.integradeproject.entities.Task2;

public interface Task2Repository extends JpaRepository<Task2 , Integer> {
    List<Task2> findByStatusId(Status string);
}
