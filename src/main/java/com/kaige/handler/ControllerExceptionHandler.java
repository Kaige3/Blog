package com.kaige.handler;

import com.kaige.entity.Result;
import com.kaige.handler.exception.NotFoundException;
import com.kaige.handler.exception.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 对controller 层全局异常处理
 * 捕获异常后，返回 json 数据
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 捕获持久化异常后，返回 json 数据
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(PersistenceException.class)
    public Result persistenceExceptionHandler(HttpServletRequest request, PersistenceException e) {
        logger.error("Request URL ; {},Exception:",request.getRequestURL(),e);
        return Result.create(500,e.getMessage());
    }

    /**
     * 捕获 404 异常后，返回 json 数据
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public Result notFoundExceptionHandler(HttpServletRequest request, NotFoundException e) {
        logger.error("Request URL ; {},Exception:",request.getRequestURL(),e);
        return Result.create(404,e.getMessage());
    }

}
