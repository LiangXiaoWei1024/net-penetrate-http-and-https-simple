package com.kele.penetrate.pojo;

import com.kele.penetrate.protocol.RequestFile;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("unused")
public class MultipartBody
{
    private Map<String,String> bodyMap;
    private List<RequestFile> bodyFiles;

}
