package org.example.model;



import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

// Этот класс реализует интерфейс GrantedAuthority, в котором необходимо переопределить только один метод getAuthority() (возвращает имя роли).
// Имя роли должно соответствовать шаблону: «ROLE_ИМЯ», например, ROLE_USER.
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {


    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " role_id")
    private Long id;

    private String role;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> usersSet;

   public Role() {}
   public Role(Long id, String role) {
       this.id = id;
       this.role = role;
   }

    public void setId() {
       this.id = id;
    }

    public String getRole() {
       return role;
    }

    public void setRole() {
       this.role = role;
    }

    @Override
    public String getAuthority() {
       return getRole();
    }

    public Set<User> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<User> usersSet) {
        this.usersSet = usersSet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
