package com.skybory.seoulArt.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skybory.seoulArt.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
//    @Query("SELECT u.replies FROM User u JOIN u.replies r WHERE u.id = :userId AND r.state = 'Question'")
//    List<Reply> findQuestionRepliesByUserId(Long userId);

    List<User> findByDepartment(String department);

	User findByVerifyCode(String verifyCode);
	

//	void save(Long userId);

//	User findByUserId(String userId);

   
}