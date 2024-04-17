package com.skybory.seoulArt.domain.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skybory.seoulArt.domain.reply.entity.Reply;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.global.Dept;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
//    @Query("SELECT u.replies FROM User u JOIN u.replies r WHERE u.id = :userId AND r.state = 'Question'")
//    List<Reply> findQuestionRepliesByUserId(Long userId);

    List<User> findByDepartment(Dept department);

}