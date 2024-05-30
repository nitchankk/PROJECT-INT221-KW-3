package com.example.integradeproject.services;

import com.example.integradeproject.dtos.NewTask2DTO;
import com.example.integradeproject.dtos.NewTaskDTO;
import com.example.integradeproject.entities.Status;
import com.example.integradeproject.entities.Task;
import com.example.integradeproject.entities.Task2;
import com.example.integradeproject.repositories.StatusRepository;
import com.example.integradeproject.repositories.Task2Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private Task2Repository task2Repository ;
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Status> findAllStatus() {
        return statusRepository.findAll();
    }

    @Transactional
    public Status findById(Integer id){
        return statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"task " + id + " does not exist !! "));
    }

    public Status createNewStatus(Status status) {
        if (status.getName() == null || status.getName().trim().toLowerCase().isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Name must not be null");
        }

        boolean exists = statusRepository.existsByName(status.getName());
        if (exists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Name must be unique. ");
        }

        StringBuilder error = new StringBuilder();
        if (status.getName().length() > 50) {
            error.append("name size must be between 0 and 50.");
        }

        if (status.getDescription().length() > 200) {
            error.append("description size must be between 0 and 200.");
        }
        if (error.length() > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error.toString());
        }

        return statusRepository.saveAndFlush(status);
    }





    public Status updateStatus(Integer id, Status updatedStatus) {
        if ( updatedStatus.getName() == null || updatedStatus.getName().trim().toLowerCase().isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST ,  "Name must not be null");
        }
        if (id == 1){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST ,  "No Status Cannot be changed");
        }
        if (id == 6){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST ,  "Done Cannot be changed");
        }
        boolean exists = statusRepository.existsByName(updatedStatus.getName());
        if (exists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Name must be unique. ");
        }

        StringBuilder error = new StringBuilder();
        if (updatedStatus.getName().length() > 50) {
            error.append("name size must be between 0 and 50.");
        }

        if (updatedStatus.getDescription().length() > 200) {
            error.append("description size must be between 0 and 200.");
        }
        if (error.length() > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error.toString());
        }

        Optional<Status> optionalStatus = statusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            Status existingStatus = optionalStatus.get();
            existingStatus.setName(updatedStatus.getName());
            existingStatus.setDescription(updatedStatus.getDescription());
            return statusRepository.save(existingStatus);
        } else {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Status not found with id: " + id);
        }
    }
    @Transactional
    public NewTask2DTO deleteById(Integer id) {
        if (id == 1){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST ,  "No Status Cannot be deleted");
        }
        if (id == 6){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST ,  "Done Cannot be deleted");
        }
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID " + id + " DOES NOT EXIST !!!"));

        if (isStatusInUse(id)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Cannot delete Status with ID " + id + " as it is currently in use.");
        }

        statusRepository.deleteById(id);
        NewTask2DTO deletedStatusDTO = mapper.map(status, NewTask2DTO.class);

        return deletedStatusDTO;
    }
    private boolean isStatusInUse(Integer statusId) {
        return task2Repository.existsByStatusId(statusId);
    }
    @Transactional
    public void deleteStatusAndTransferTasks(int id, int newStatusId) {
        if (id == newStatusId){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "destination status for task transfer must be different from current status");
        }

        Status currentStatus = statusRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Status with ID " + id + " does not exist"));
        Status newStatus = statusRepository.findById(newStatusId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "the specified status for task transfer does not exist"));

        List<Task2> tasksWithCurrentStatus = task2Repository.findByStatus(currentStatus);
        tasksWithCurrentStatus.forEach(task -> task.setStatus(newStatus));
        task2Repository.saveAll(tasksWithCurrentStatus);

        statusRepository.delete(currentStatus);
    }


}
