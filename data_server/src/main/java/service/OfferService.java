package service;

import model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.OfferRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService
{
  private final OfferRepository offerRepository;

  @Autowired
  public OfferService(OfferRepository offerRepository)
  {
    this.offerRepository = offerRepository;
  }

  public List<Offer> getAllOffers() {
    return offerRepository.findAll();
  }

  public Optional<Offer> getOfferById(String id) {
    return offerRepository.findById(id);
  }

  public Offer createOffer(Offer offer) {
    return offerRepository.save(offer);
  }
  public Offer updateOffer(String id, Offer offerDetails) {
    return offerRepository.findById(id).map(offer -> {
      offer.setTitle(offerDetails.getTitle());
      offer.setDescription(offerDetails.getDescription());
      offer.setPrice(offerDetails.getPrice());
      offer.setPickupDate(offerDetails.getPickupDate());
      offer.setPickupTimeStart(offerDetails.getPickupTimeStart());
      offer.setPickupTimeEnd(offerDetails.getPickupTimeEnd());
      offer.setCategories(offerDetails.getCategories());
      offer.setNumberOfFoodBags(offer.getNumberOfFoodBags());

      return offerRepository.save(offer);
    }).orElseThrow(() -> new RuntimeException("Offer not found with id " + id));
  }

  public void deleteOffer(String id) {
    offerRepository.deleteById(id);
  }
}
