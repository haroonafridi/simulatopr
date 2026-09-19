package com.hkcapital.portflio.service.export.csv;

public interface Reader<T,R>
{
    R upload(T t);
}
