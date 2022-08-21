package com.techdisqus.courier.service.offer;

import com.techdisqus.courier.vo.courier.CourierRequest;
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
