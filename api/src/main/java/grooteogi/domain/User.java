package grooteogi.domain;

import javax.persistence.*;

import grooteogi.domain.dto.LoginDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 40, nullable = false)
  private String nickname;

  @Column(length = 40, nullable = false)
  private String email;

  @Column(length = 255, nullable = false)
  private String passsword;

  @Column(length = 2, nullable = false)
  private int type;

  @CreationTimestamp
  private Timestamp modified;

  @CreationTimestamp
  private Timestamp registered;

  @OneToOne
  @JoinColumn(name = "id")
  private UserInfo userInfo;

  @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Authority> authorities = new HashSet<>();

  public static User ofUser(LoginDto loginDto) {
    User user = User.builder()
            .nickname(UUID.randomUUID().toString())
            .email(loginDto.getEmail())
            .passsword(loginDto.getPassword())
            .build();
    user.addAuthority(Authority.ofUser(user));
    return user;
  }

  public static User ofAdmin(LoginDto loginDto) {
    User user = User.builder()
            .nickname(UUID.randomUUID().toString())
            .email(loginDto.getEmail())
            .passsword(loginDto.getPassword())
            .build();
    user.addAuthority(Authority.ofAdmin(user));
    return user;
  }

  private void addAuthority(Authority authority) {
    authorities.add(authority);
  }

  public List<String> getRoles() {
    return authorities.stream()
            .map(Authority::getRole)
            .collect(toList());
  }
}


