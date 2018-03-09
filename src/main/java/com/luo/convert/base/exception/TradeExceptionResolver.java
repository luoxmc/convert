package com.luo.convert.base.exception;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.luo.convert.base.response.JsonResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TradeExceptionResolver implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o,
			Exception e) {
		ModelAndView mv = null;
        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
            		mv = new ModelAndView(new MappingJackson2JsonView()).addAllObjects(JsonResponse.failure(-999,e.getMessage()));
                break;
            case POST:
            		mv = new ModelAndView(new MappingJackson2JsonView()).addAllObjects(JsonResponse.failure(-999,e.getMessage()));
                break;
            default:
                break;
        }
        return mv;
	}
    
}
