package io.security.basicsecurity.june_23.controller.user;

import io.security.basicsecurity.june_23.domain.Account;
import io.security.basicsecurity.june_23.domain.AccountDto;
import io.security.basicsecurity.june_23.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping(value="/mypage")
    public String myPage() throws Exception {
        ;
        return "user/mypage";
    }

    @GetMapping(value = "/users")
    public String createUser() {
        ;
        return "user/login/register";
    }

    @PostMapping(value = "/users")
    public String createUser(AccountDto accountDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        userService.createUser(account);

        return "redirect:/";
    }

}
