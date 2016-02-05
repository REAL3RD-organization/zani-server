package real.zani.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="user")
@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "loginId")
	private String loginId;
	
	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "gcm_reg_id")
	private String gcmRegId;
	
	@JoinTable(name = "friends", joinColumns = {
			 @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
			 @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)})
			 @ManyToMany(cascade=CascadeType.ALL)
	private Collection<UserEntity> friends;
	
	@Column(name = "created_at")
	private Date createdAt;
}
