package com.intellica.evam.report.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: eeroglu
 * Date: 24 Şub 2014 10:16:14
 * Package: com.intellica.evam.report.util
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2794929786054654449L;

	public ResourceNotFoundException(String resourceName) {
        super("Cannot find resource: " + resourceName);
    }
}
