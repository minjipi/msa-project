package com.minji.msa.status.springboot.service;

import com.minji.msa.status.domain.repository.IStatusRepository;
import com.minji.msa.status.domain.service.StatusService;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl extends StatusService {

    public StatusServiceImpl(IStatusRepository iStatusRepository) {

        super(iStatusRepository);
    }
}