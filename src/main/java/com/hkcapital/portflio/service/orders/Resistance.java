package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resistance
{
    private Region r1;
    private Region r2;
    private Region r3;

    public static Resistance of(Region r1, Region r2, Region r3)
    {
        return new Resistance(r1, r2, r3);
    }
}
