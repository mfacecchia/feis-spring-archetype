package ${package}.features.user.repository;

import ${package}.common.repository.BaseRepository;
import ${package}.features.user.data.User;

public interface UserRepository extends BaseRepository<User, Integer> {

    boolean existsByEmail(String email);
}
