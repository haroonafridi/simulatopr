package com.hkcapital.portflio.service.importexport;

import com.hkcapital.portflio.service.registry.Service;

/**
 * This interface can be used as a utility and can be extended to generate differ type of files.
 */
public interface FileGenerator<T> extends Service
{
    /**
     * @param data      type of data to be used in order to generate file e.g string etc.
     * @param fileName  name of the file
     * @param fileTypes {@link  FileTypes}
     */
    void fileOf(T data, String fileName, FileTypes fileTypes);
}
