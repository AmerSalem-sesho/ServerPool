package com.example.ServerPool.controller;
import com.example.ServerPool.model.Server;
import com.example.ServerPool.service.ResourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String allocateMemory(@PathVariable int memory) throws Exception {
        return resourceManagementService.allocateMemory(memory);
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
