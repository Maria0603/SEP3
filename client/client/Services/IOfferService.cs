using client.DTO;
using client.DTO.Offer;
using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OfferResponseDto> CreateOfferAsync(CreateOfferRequestDto createOfferRequestDto);
    public Task<List<OfferResponseDto>> GetOffersAsync(FilterRequestDto? filterRequestDto);
    public Task<OfferResponseDto> GetOfferByIdAsync(string id);
    public Task<List<OfferResponseDto>> GetOffersByBusinessIdAsync(string id);
}