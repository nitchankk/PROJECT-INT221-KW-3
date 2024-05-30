package com.example.integradeproject.services;

import com.example.integradeproject.dtos.NewTask2DTO;
import com.example.integradeproject.dtos.StatusDTO;
import com.example.integradeproject.dtos.Task2DTO;
import com.example.integradeproject.entities.Status;
import com.example.integradeproject.entities.Task2;
import com.example.integradeproject.repositories.StatusRepository;
import com.example.integradeproject.repositories.Task2Repository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Task2Service {

    @Autowired
    private Task2Repository repository;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private StatusRepository statusRepository;

    public List<Task2DTO> getTask(String sortBy, List<String> filterStatuses) {
        List<Task2> tasks;
        if (filterStatuses == null || filterStatuses.isEmpty()) {
            if (sortBy == null || sortBy.isEmpty()) {
                tasks = repository.findAll();
            } else {
                if (sortBy.equalsIgnoreCase("status.name")) {
                    tasks = repository.findAll(Sort.by(Sort.Direction.ASC, "status.name"));
                } else {
                    throw new IllegalArgumentException("Invalid sorting option");
                }
            }
        } else {
            tasks = repository.findByStatusNames(filterStatuses);
            if (sortBy != null && !sortBy.isEmpty() && sortBy.equalsIgnoreCase("status.name")) {
                tasks = tasks.stream()
                        .sorted(Comparator.comparing(t -> t.getStatus().getName()))
                        .collect(Collectors.toList());
            }
        }

        return tasks.stream()
                .map(task -> {
                    Task2DTO task2DTO = mapper.map(task, Task2DTO.class);
                    StatusDTO statusDTO = mapper.map(task.getStatus(), StatusDTO.class);
                    task2DTO.setStatus(statusDTO);
                    return task2DTO;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public Task2 findById(Integer id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"task " + id + " does not exist !! "));
    }
    @Transactional
    public NewTask2DTO deleteById(Integer id) {
        Task2 task2 = repository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID " + id + " DOES NOT EXIST !!!"));
        repository.deleteById(task2.getId());
        NewTask2DTO deletedTask2DTO = mapper.map(task2, NewTask2DTO.class);

        return deletedTask2DTO;
    }

    public NewTask2DTO createTask(NewTask2DTO newTask2DTO) {
        // Validate the title
        if (newTask2DTO.getTitle() == null || newTask2DTO.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty");
        }

        Task2 task = new Task2();
        task.setTitle(newTask2DTO.getTitle());
        task.setDescription(newTask2DTO.getDescription());

        if (newTask2DTO.getStatus() != null) {
            try {
                int statusId = Integer.parseInt(newTask2DTO.getStatus());
                Status status = statusRepository.findById(statusId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status Id " + statusId + " DOES NOT EXIST !!!"));
                task.setStatus(status);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status ID format");
            }
        }

        task.setAssignees(newTask2DTO.getAssignees());

        Task2 savedTask = repository.save(task);
        NewTask2DTO createdTaskDTO = mapper.map(savedTask, NewTask2DTO.class);
        createdTaskDTO.setStatus(savedTask.getStatus().getName());

        return createdTaskDTO;
    }

    public NewTask2DTO updateTask(Integer taskId, NewTask2DTO newTask2DTO) {
        if (newTask2DTO.getTitle() == null || newTask2DTO.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty");
        }
        Task2 existingTask = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        if (newTask2DTO.getStatus() != null) {
            try {
                int statusId = Integer.parseInt(newTask2DTO.getStatus());
                Status status = statusRepository.findById(statusId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status Id " + statusId + " DOES NOT EXIST !!!"));
                existingTask.setStatus(status);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status ID format");
            }
        }

        existingTask.setTitle(newTask2DTO.getTitle());
        existingTask.setDescription(newTask2DTO.getDescription());
        existingTask.setAssignees(newTask2DTO.getAssignees());

        // Find the Status entity by name
        Optional<Status> status = statusRepository.findByName(newTask2DTO.getStatus());
        status.ifPresent(existingTask::setStatus);

        Task2 updatedTask = repository.save(existingTask);

        NewTask2DTO updatedTaskDTO = mapper.map(updatedTask, NewTask2DTO.class);
        updatedTaskDTO.setStatus(updatedTask.getStatus().getName()); // Set the status name
        return updatedTaskDTO;


    }
}
