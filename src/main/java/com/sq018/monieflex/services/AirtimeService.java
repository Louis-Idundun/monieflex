package com.sq018.monieflex.services;

import com.sq018.monieflex.services.providers.VtPassService;
import org.springframework.stereotype.Service;

@Service
public class AirtimeService {

    private final VtPassService vtPassService;

    public AirtimeService(VtPassService vtPassService) {
        this.vtPassService = vtPassService;
    }

}
