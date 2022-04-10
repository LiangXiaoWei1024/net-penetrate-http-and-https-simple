package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("unused")
public class RequestTextBody extends BaseRequest
{
    private String dataText;
}
