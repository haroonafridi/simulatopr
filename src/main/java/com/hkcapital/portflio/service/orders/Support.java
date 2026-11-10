package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Support
{
    private Region r1;
    private Region r2;
    private Region r3;

    public static Support of(Region r1,
                             Region r2,
                             Region r3)
    {
        return new Support(r1, r2, r3);
    }
}
