package com.example.integradeproject.repositories;

import com.example.integradeproject.entities.Status;
import com.example.integradeproject.entities.Task2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Task2Repository extends JpaRepository<Task2 , Integer> {
    List<Task2> findByStatus(Status string);
    @Query("SELECT t FROM Task2 t WHERE t.status.name IN :statusNames")
    List<Task2> findByStatusNames(@Param("statusNames") List<String> statusNames);


    boolean existsByStatusId(Integer statusId);
}
