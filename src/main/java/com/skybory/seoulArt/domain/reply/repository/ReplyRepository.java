package com.skybory.seoulArt.domain.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skybory.seoulArt.domain.reply.entity.Reply;


public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
