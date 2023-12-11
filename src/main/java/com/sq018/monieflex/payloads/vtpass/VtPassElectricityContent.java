package com.sq018.monieflex.payloads.vtpass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VtPassElectricityContent {
    private VtPassElectricityTransaction transactions;
}
