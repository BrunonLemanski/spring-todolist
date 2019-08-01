package pl.retrilx.todolist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.retrilx.todolist.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);
    User getById(Long id);

    //Optional<User> findById(Long id); //no pointer exception
}
