package com.example.ServerPool.service;

import com.example.ServerPool.enums.Status;
import com.example.ServerPool.model.Server;
import com.example.ServerPool.response.AllocateMemoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceManagementService
{

    private final int MaxServerCapacity = 100;
    @Autowired
    private AerospikeRepository repository;

    public Server getServerById(int id)
    {
        return (Server) repository.findById(id).orElse(null);
    }

    public void deleteServerById(int id)
    {
        repository.deleteById(id);
    }

    public List<Server> findAllServer()
    {
        return (List<Server>) repository.findAll();
    }
    public synchronized String allocateMemory(int memory) throws ResponseStatusException
    {

        AllocateMemoryResponse message = new AllocateMemoryResponse();

        if (memory > MaxServerCapacity)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Requested memory is too large.");
        }
            Server server = findServerWithEnoughMemory(memory);
            if (server != null)
            {
                server.setAllocatedMemory(server.getAllocatedMemory() + memory);
                message.setMessage("user allocated " + memory +
                        "GB of memory from server: " + server.getId());

                repository.save(server);
            }
            else
            {
                int lServerId = (int)repository.count();

                Server lNewServer = new Server(lServerId,
                        "server" + lServerId, MaxServerCapacity,
                        0, Status.Creating.name());
                try
                {
                    Thread.sleep(2000);
                }

                catch (InterruptedException e)
                {
                    System.out.println(e.getMessage());
                }
                lNewServer.setStatus(Status.Active.name());
                lNewServer.setAllocatedMemory(memory);

            message.setMessage("user allocated " + memory
                        + "GB of memory from server: " + lNewServer.getId());

                repository.save(lNewServer);
            }
        return message.getMessage();
    }

    private Server findServerWithEnoughMemory(int memory)
    {
        List<Server> lServers = findAllServer().stream().sorted(Comparator.
                        comparing(Server::getId)).filter(server ->
                        server.getAllocatedMemory() <= MaxServerCapacity - memory)
                .collect(Collectors.toList());
        for (Server server : lServers)
        {
            if ((server.getStatus().equals(Status.Active.name())))
            {
                return server;
            }
        }
        return null;
    }
}



