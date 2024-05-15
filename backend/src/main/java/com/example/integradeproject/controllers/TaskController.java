package com.example.integradeproject.controllers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.integradeproject.dtos.NewTaskDTO;
import com.example.integradeproject.dtos.TaskDTO;
import com.example.integradeproject.dtos.TaskIdDTO;
import com.example.integradeproject.entities.Task;
import com.example.integradeproject.services.ListMapper;
import com.example.integradeproject.services.TaskService;


@RestController
@CrossOrigin(origins = {"http://ip23kw3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
@RequestMapping("/v1/tasks")

public class TaskController {
    @Autowired
    private TaskService service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    //    @GetMapping("")
//    public List<Task> getAllTasks(){
//        return service.getTask();
//    }
    @GetMapping("")
    public ResponseEntity<Object> getTasks(){
        return ResponseEntity.ok(listMapper.mapList(service.getTask(), TaskDTO.class,modelMapper));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer id){
        Task task = service.findById(id);
        TaskIdDTO  taskIdDTO = modelMapper.map(task ,TaskIdDTO.class);
        return ResponseEntity.ok(taskIdDTO);
    }
    @PostMapping("")
    public ResponseEntity<NewTaskDTO> createTask(@RequestBody NewTaskDTO newTask) {
        NewTaskDTO createdTask = service.createTask(newTask);

        return new ResponseEntity<>(createdTask , HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<NewTaskDTO> removeTask (@PathVariable Integer id ){
        NewTaskDTO newTaskDTO = service.deleteById(id);
        return  ResponseEntity.ok(newTaskDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<NewTaskDTO> updateTask(@RequestBody NewTaskDTO newTaskDTO, @PathVariable Integer id) {
        try {
            NewTaskDTO updatedTaskDTO = service.updateTask(newTaskDTO, id);

            return ResponseEntity.ok(updatedTaskDTO);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
}
