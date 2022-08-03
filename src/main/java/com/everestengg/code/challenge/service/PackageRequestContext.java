package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.InputPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Getter
@Builder
public class PackageRequestContext {

    private InputPackage inputPackage;

}
