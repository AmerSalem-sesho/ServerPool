package com.example.ServerPool.service;


import com.example.ServerPool.model.Server;
import com.example.ServerPool.response.AllocateMemoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceManagementService {
    private final int maxServerCapacity = 100;
    @Autowired
    private AerospikeRepository repository;
    private List<Server> servers = new ArrayList<>();

    public Server getServerById(int id) {
        return (Server) repository.findById(id).orElse(null);
    }

    public void deleteServerById(int id) {
        repository.deleteById(id);
    }

    public synchronized String allocateMemory(int memory) throws Exception {
        if (memory > maxServerCapacity) {
            throw new Exception("Sorry not enough resources, requested memory is bigger than maxServerCapacity");
        }
        AllocateMemoryResponse message = new AllocateMemoryResponse();
        Thread thread = new Thread(() -> {
            Server server = findServerWithEnoughMemory(memory);
            if (server != null) {
                server.setAllocatedMemory(server.getAllocatedMemory() + memory);
                message.setMessage("user allocated " + memory + "GB of memory from server: " + server.getId());
                repository.save(server);
            } else {
                Server lNewServer = new Server((int)repository.count(), "server" + (int)repository.count(), maxServerCapacity, 0, "Creating");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                lNewServer.setStatus("active");
                lNewServer.setAllocatedMemory(memory);
                servers.add(lNewServer);
                message.setMessage("user allocated " + memory + "GB of memory from server: " + lNewServer.getId());
                repository.save(lNewServer);
            }
        });
        thread.start();
        thread.join();
        return message.getMessage();
    }

    public List<Server>findAllServer(){
        return (List<Server>)repository.findAll();
    }
    private Server findServerWithEnoughMemory(int memory) {
        List <Server> servers1 = findAllServer().stream().sorted(Comparator.comparing(Server::getId)).filter(server ->server.getAllocatedMemory() <= maxServerCapacity-memory)
                .collect(Collectors.toList());
        for (Server server : servers1) {
            if ((server.getStatus().equals("active"))){
                return server;
        }
    }
        return null;

    }
}



