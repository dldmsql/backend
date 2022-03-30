package grooteogi.domain;

import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
