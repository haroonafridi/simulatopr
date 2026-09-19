package com.hkcapital.portflio.service.export.file;

import com.hkcapital.portflio.service.registry.Service;

public interface FileGenerator extends Service
{
    void fileOf(String data, String fileName, String type);
}
