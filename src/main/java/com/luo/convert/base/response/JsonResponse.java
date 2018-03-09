package com.luo.convert.base.response;

import java.util.HashMap;

public class JsonResponse extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	private static final String CODE_KEY = "error_no";//200 means success, other values means fail.
    private static final String MESSAGE_KEY = "error_info";
    public static final String DATA_KEY = "result";

    public JsonResponse(int code, String msg) {
        put(CODE_KEY, code);
        put(MESSAGE_KEY, msg);
    }

    public JsonResponse add(String key, Object value) {
        if (CODE_KEY.equals(key)
                || MESSAGE_KEY.equals(key)) {
            throw new RuntimeException("Predefined key");
        }
        put(key, value);
        return this;
    }

    public static JsonResponse success() {
        return new JsonResponse(0, "");
    }

    public static JsonResponse failure(int code, String message) {
        return new JsonResponse(code, message);
    }

    public static JsonResponse failure(String message) {
        return failure(-10000, message);
    }
}
