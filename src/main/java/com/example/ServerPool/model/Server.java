package com.example.ServerPool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    @Id

    private int id;
    private String name;
    private int memory;
    private int allocatedMemory;
    private String status;

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", serverName=" + name +
                ", memory=" + memory +
                ", allocatedMemory=" + allocatedMemory +
                ", status='" + status + '\'' +
                '}'
                +"\n";
    }
}
