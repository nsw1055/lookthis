package com.kirby.lookthis.main.security;

import com.google.gson.Gson;
import com.kirby.lookthis.main.uil.JwtUtil;
import com.kirby.lookthis.user.entity.LoginHistory;
import com.kirby.lookthis.user.entity.User;
import com.kirby.lookthis.user.repository.LoginHistoryRepository;
import com.kirby.lookthis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = userRepository.findByUserId(request.getParameter("userId"));

        String id = user.getUserId();
        String name = user.getName();

        Map<String, Object> jwtInfo = new HashMap<>();
        jwtInfo.put("userId", id);
        jwtInfo.put("name", name);
        String jwt = jwtUtil.generateToken(jwtInfo, id);
        String url = makeRedirectUrl(request.getHeader("Referer"), jwt);

        LoginHistory loginHistory = LoginHistory
                .builder()
                .ipAddress(request.getRemoteAddr())
                .status("Login")
                .platform("lookthis")
                .user_id(id)
                .service("http://lookthis.co.kr")
                .build();
        loginHistoryRepository.save(loginHistory);

        if (response.isCommitted()) {
            log.debug("????????? ?????? ????????? ???????????????. " + url + "??? ???????????????????????? ?????? ??? ????????????.");
            return;
        }
        String jsonObj = new Gson().toJson(url);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObj);
        out.flush();
    }

    private String makeRedirectUrl(String url, String token) {
        return UriComponentsBuilder.fromUriString("/oauth2/redirect/"+token)
                .build().toUriString();
    }

}
