package com.econovation.whichbook_user.domain.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthPolicy {
    boolean authorize(HttpServletRequest httpServletRequest);
}
