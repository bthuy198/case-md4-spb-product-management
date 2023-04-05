package com.example.api;

import com.example.exception.DataInputException;
import com.example.exception.FieldExistsException;
import com.example.model.dto.RoleDTO;
import com.example.model.dto.UserCreateReqDTO;
import com.example.model.JwtResponse;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.UserLoginReqDTO;
import com.example.service.jwt.JwtService;
import com.example.service.role.IRoleService;
import com.example.service.user.IUserService;
import com.example.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private AppUtils appUtils;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserCreateReqDTO userCreateReqDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (userService.existsByUsername(userCreateReqDTO.getUsername())) {
            throw new FieldExistsException("Username has been exist");
        }

        Optional<Role> roleOptional = roleService.findById(userCreateReqDTO.getRole().getId());

        if (!roleOptional.isPresent()) {
            throw new DataInputException("Invalid role");
        }

        try {
            userService.save(userCreateReqDTO.toUser());

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginReqDTO userLoginReqDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginReqDTO.getUsername(), userLoginReqDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getByUsername(userLoginReqDTO.getUsername());

            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    currentUser.getId(),
                    userDetails.getUsername(),
                    currentUser.getUsername(),
                    userDetails.getAuthorities()
            );

            ResponseCookie springCookie = ResponseCookie.from("JWT", jwt)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 1000)
                    .domain("localhost")
                    .build();

            System.out.println(jwtResponse);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .body(jwtResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole() {
        List<RoleDTO> roleDTOS = roleService.getAllRoleDTO();
        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }
}
