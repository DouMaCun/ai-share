package com.aishare.service;

import com.aishare.dto.LoginDTO;
import com.aishare.dto.RegisterDTO;
import com.aishare.vo.LoginVO;

public interface AuthService {
    LoginVO register(RegisterDTO dto);
    LoginVO login(LoginDTO dto);
}
