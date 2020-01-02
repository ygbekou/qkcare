package com.qkcare.controller;

import com.qkcare.config.JwtTokenUtil;
import com.qkcare.domain.AuthToken;
import com.qkcare.domain.LoginUser;
import com.qkcare.domain.MenuVO;
import com.qkcare.domain.PermissionVO;
import com.qkcare.model.User;
import com.qkcare.service.AuthorizationService;
import com.qkcare.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/token")
@CrossOrigin
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	BCryptPasswordEncoder encoder;

	@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
		System.out.println("Authenticating user :"+loginUser);
		try {
			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final User user = userService.getUser(null, loginUser.getUserName(), null);
			final String token = jwtTokenUtil.generateToken(user);

			Pair<List<MenuVO>, List<PermissionVO>> resources = this.authorizationService.getUserResources(user.getId(),
					loginUser.getLang());

			return ResponseEntity.ok(new AuthToken(token, loginUser.getUserName(), loginUser.getPassword(),
					user.getFirstName(), user.getLastName(), user.getUserGroup().getName(), user.getPicture(),
					user.getFirstTimeLogin(), Arrays.asList(new Long[] { user.getUserGroup().getId() }),
					resources.getValue0(), resources.getValue1(),user.getId()));
			
		} catch (Exception b) {
			b.printStackTrace();
			return ResponseEntity.ok(new AuthToken("", loginUser.getUserName(), loginUser.getPassword(),
					null, null, null, null,null, null,null, null,null));
		}

	}

}
