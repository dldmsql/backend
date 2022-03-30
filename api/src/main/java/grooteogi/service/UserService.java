package grooteogi.service;

import grooteogi.domain.LogoutAccessTokenRedisRepository;
import grooteogi.domain.RefreshToken;
import grooteogi.domain.RefreshTokenRedisRepository;
import grooteogi.domain.User;
import grooteogi.domain.dto.LoginDto;
import grooteogi.domain.dto.TokenDto;
import grooteogi.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;

import grooteogi.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static grooteogi.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,
                     RefreshTokenRedisRepository refreshTokenRedisRepository, LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository, JwtTokenUtil jwtTokenUtil ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    this.logoutAccessTokenRedisRepository = logoutAccessTokenRedisRepository;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  public void testAdmin(LoginDto loginDto) {
    loginDto.setPassword(passwordEncoder.encode(loginDto.getPassword()));
    userRepository.save(User.ofAdmin(loginDto));
  }

  public TokenDto login(LoginDto loginDto) {
    User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
    checkPassword(loginDto.getPassword(), user.getPasssword());

    String username = user.getNickname();
    String accessToken = jwtTokenUtil.generateAccessToken(username);
    RefreshToken refreshToken = saveRefreshToken(username);
    return TokenDto.of(accessToken, refreshToken.getRefreshToken());
  }
  private void checkPassword(String rawPassword, String findMemberPassword) {
    if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
      throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }
  }
  private RefreshToken saveRefreshToken(String email) {
    return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(email,
            jwtTokenUtil.generateRefreshToken(email), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
  }

}
