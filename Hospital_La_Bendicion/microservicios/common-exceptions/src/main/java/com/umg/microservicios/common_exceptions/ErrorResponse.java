package com.umg.microservicios.common_exceptions;

import java.util.Map;

public record ErrorResponse(Map<String, String> error) {

}
