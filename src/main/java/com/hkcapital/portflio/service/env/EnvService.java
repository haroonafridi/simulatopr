package com.hkcapital.portflio.service.env;

import com.hkcapital.portflio.service.registry.Service;
import org.springframework.core.env.Environment;

import java.util.Arrays;


public interface EnvService extends Service
{
    String getActiveProfile();
}