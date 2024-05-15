package com.example.integradeproject.controllers;

import java.util.List;

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

import com.example.integradeproject.entities.Status;
import com.example.integradeproject.services.ListMapper;
import com.example.integradeproject.services.StatusService;

@RestController
@CrossOrigin(origins = {"http://ip23kw3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})

@RequestMapping("/v2/statuses")
public class StatusController {
    @Autowired
    private StatusService service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public List<Status> getALLStatus(){
        return service.findAllStatus();
    }
    @PostMapping("")
    public ResponseEntity<Status> createStatus(@RequestBody Status status) {
        Status newStatus = service.createNewStatus(status);
        return new ResponseEntity<>(newStatus, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Status> UpdateStatus (@RequestBody Status status , @PathVariable Integer id ){
        try{
            Status updateStatus  =service.updateByStatusId(status , id);
            return  ResponseEntity.ok(updateStatus);

        }catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeStatus(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.ok("{}");
    }

    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<Object> deleteStatusAndTransferTasks(@PathVariable int id, @PathVariable int newId) {
        service.deleteStatusAndTransferTasks(id, newId);
        return ResponseEntity.ok("{}");
    }


}



