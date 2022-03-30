package grooteogi.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 11)
    private String contact;

    @Column(length = 50)
    private String address;

    @Column(length = 45)
    private String college_email;

    @CreationTimestamp
    private Timestamp registered;

    @OneToOne(mappedBy = "userInfo")
    private User user;
}
