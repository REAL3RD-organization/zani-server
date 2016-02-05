package real.zani.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import real.zani.entity.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	UserEntity findByLoginId(String loginId);
	
	UserEntity findByLoginIdAndPassword(String loginId, String password);
	
	@Transactional
	@Modifying
	void removeByLoginId(String loginId);
}
