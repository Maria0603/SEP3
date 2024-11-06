package repository;

import com.example.shared.dao.OfferDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends MongoRepository<OfferDao, String>
{

}
