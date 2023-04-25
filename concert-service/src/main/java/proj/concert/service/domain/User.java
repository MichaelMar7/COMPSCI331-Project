package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.dto.UserDTO;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    protected User() {}

    public User(Long id,
                String username,
                String password,
                Long version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User other = (User) o;

        return new EqualsBuilder()
                .append(id, other.id)
                .append(username, other.username)
                .append(password, other.password)
                .append(version, other.version)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(password)
                .append(version)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}