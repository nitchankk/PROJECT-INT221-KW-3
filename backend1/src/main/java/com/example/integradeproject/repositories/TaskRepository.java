package com.example.integradeproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.integradeproject.entities.Task;

public interface TaskRepository  extends JpaRepository <Task , Integer> {

}
