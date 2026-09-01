package com.hkcapital.portflio.service.csv;

public interface Reader<T,R>
{
    R upload(T t);
}
