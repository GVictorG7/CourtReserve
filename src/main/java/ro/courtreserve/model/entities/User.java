package ro.courtreserve.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.courtreserve.model.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 20, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private UserRole role;

    @Column(name = "mail", length = 30, nullable = false, unique = true)
    private String mail;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Subscription> subscriptions = new HashSet<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.EAGER)
    private Set<Invitation> sendInvitations = new HashSet<>();

    @OneToMany(mappedBy = "to", fetch = FetchType.EAGER)
    private Set<Invitation> receivedInvitations = new HashSet<>();

    public User(Long id, String username, String password, UserRole role, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.mail = mail;
    }
}
