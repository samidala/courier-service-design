package com.everestengg.code.challenge.service.offer;

import com.everestengg.code.challenge.vo.courier.CourierRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


/**
 * context holder for package and any other information
 */
@AllArgsConstructor
@Getter
@Builder
public class PackageRequestContext {

    private CourierRequest courierRequest;

}
