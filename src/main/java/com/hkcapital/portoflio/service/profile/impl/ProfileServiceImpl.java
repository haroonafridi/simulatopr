package com.hkcapital.portoflio.service.profile.impl;

import com.hkcapital.portoflio.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService
{
    @Autowired
    private Environment env;
    @Override
    public String getActiveProfile()
    {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return "default"; // fallback if no profile is active
        }
        return profiles[0];
    }
}
