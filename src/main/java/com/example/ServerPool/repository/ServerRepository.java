package com.example.ServerPool.repository;

import com.example.ServerPool.model.Server;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.data.annotation.QueryAnnotation;
import java.util.List;

public interface ServerRepository extends AerospikeRepository<Server,Integer> {
}
