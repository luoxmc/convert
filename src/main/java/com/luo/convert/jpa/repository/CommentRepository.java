package com.luo.convert.jpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.luo.convert.jpa.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
	Comment findById(Long id);
	
	@Query("SELECT count(1) FROM Comment t WHERE t.type = ?1")
	int findCountByType(int type);
	
	//分页查询
	@Query(value="SELECT t.* FROM t_comment t WHERE t.type=1 ORDER BY t.create_date DESC LIMIT ?1,5",nativeQuery=true)
	List<Comment> findByPage(int num);
	
	@Query(value="SELECT t.* FROM t_comment t WHERE t.TYPE=2 AND t.JOIN_ID IN ?1",nativeQuery=true)
	List<Comment> findByJoinIds(Integer[] joinId);
}
