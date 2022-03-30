package grooteogi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Authority implements GrantedAuthority {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "AUTHORITY_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_ID")
    private User user;

    private String role;

    public static Authority ofUser(User user) {
        return Authority.builder()
                .role("ROLE_USER")
                .user(user)
                .build();
    }

    public static Authority ofAdmin(User user) {
        return Authority.builder()
                .role("ROLE_ADMIN")
                .user(user)
                .build();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
