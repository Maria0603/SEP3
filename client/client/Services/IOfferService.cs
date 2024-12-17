using client.Dto;
using client.Dto.Offer;
using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OfferResponseDto> CreateOfferAsync(CreateOfferRequestDto createOfferRequestDto);
    public Task<List<OfferResponseDto>> GetOffersAsync(FilterRequestDto? filterRequestDto);
    public Task<OfferResponseDto> GetOfferByIdAsync(string id);
    public Task<List<CategoryDto>> GetCategoriesAsync();
    public Task<List<OfferResponseDto>> GetOffersByBusinessIdAsync(string id);
}