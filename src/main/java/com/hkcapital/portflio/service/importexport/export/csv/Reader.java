package com.hkcapital.portflio.service.importexport.export.csv;

public interface Reader<T,R>
{
    R upload(T t);
}
