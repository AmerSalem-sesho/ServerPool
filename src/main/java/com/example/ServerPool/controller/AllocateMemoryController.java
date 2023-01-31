package com.example.ServerPool.controller;
import com.example.ServerPool.model.Server;
import com.example.ServerPool.service.ResourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/allocateMemory")
public class AllocateMemoryController {
    @Autowired
    private ResourceManagementService resourceManagementService;

    @GetMapping("{id}")
    public Server getServerById(@PathVariable int id){
        return resourceManagementService.getServerById(id);
    }
    @PostMapping("{memory}")
    public ResponseEntity<String> allocateMemory(@PathVariable int memory)
            throws ResponseStatusException
    {
        try
        {
            String response = resourceManagementService.allocateMemory(memory);
            return ResponseEntity.ok(response);
        }
        catch (ResponseStatusException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("{id}")
    public void deleteServerById(@PathVariable int id){
        resourceManagementService.deleteServerById(id);
    }
    @GetMapping("/getAllServers")
    public List<Server>getAllServers(){
    return resourceManagementService.findAllServer();
    }
}
