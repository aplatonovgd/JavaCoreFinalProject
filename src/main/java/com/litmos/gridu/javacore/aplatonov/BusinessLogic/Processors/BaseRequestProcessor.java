package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.InvalidJsonException;

public interface BaseRequestProcessor {
    Object ParseJson(String json) throws InvalidJsonException;
}
