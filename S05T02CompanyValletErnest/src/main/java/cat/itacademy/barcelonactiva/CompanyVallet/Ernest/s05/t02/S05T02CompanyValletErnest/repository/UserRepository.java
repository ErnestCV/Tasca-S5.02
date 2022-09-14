package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(collation = "en", value = "{}")
    List<User> findAll(Sort sort);
}
