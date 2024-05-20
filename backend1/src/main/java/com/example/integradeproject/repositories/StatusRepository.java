package com.example.integradeproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.integradeproject.entities.Status;

public interface StatusRepository extends JpaRepository  <Status , Integer>{
    Optional<Status> findByStatusName(String statusName);

}
