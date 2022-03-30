package grooteogi.domain.dto;

import lombok.*;
import grooteogi.config.jwt.JwtHeaderUtilEnums;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static TokenDto of(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .grantType(JwtHeaderUtilEnums.GRANT_TYPE.getValue())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
