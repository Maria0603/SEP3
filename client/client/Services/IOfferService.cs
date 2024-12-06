using client.DTO;
using client.DTO.Offer;
using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OfferResponseDto> SaveOfferAsync(CreateOfferRequestDto createOfferRequestDto);
    public Task<List<OfferResponseDto>> GetOffersAsync();
    public Task<OfferResponseDto> GetOfferByIdAsync(string id);
}