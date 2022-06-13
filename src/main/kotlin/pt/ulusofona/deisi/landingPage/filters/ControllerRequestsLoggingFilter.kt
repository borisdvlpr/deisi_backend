package org.dropProject.filters

import org.springframework.web.filter.AbstractRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

class ControllerRequestsLoggingFilter : AbstractRequestLoggingFilter() {

    init {
        isIncludeClientInfo = true
        setAfterMessagePrefix("[REQ] ")
    }

    override fun shouldLog(request: HttpServletRequest): Boolean {
        return logger.isInfoEnabled &&
                !request.requestURI.orEmpty().endsWith(".css") &&
                !request.requestURI.orEmpty().endsWith(".js") &&
                !request.requestURI.orEmpty().endsWith(".ico") &&
                !request.requestURI.orEmpty().endsWith(".png");
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        // do nothing
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        logger.info(message)
    }
}
