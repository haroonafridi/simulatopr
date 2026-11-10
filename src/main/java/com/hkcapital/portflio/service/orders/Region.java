package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Region
{
    private double r1;
    private double r2;
    private Integer density;

    public static Region of(double r1, double r2, Integer density)
    {
        return new Region(r1, r2, density);
    }
}
