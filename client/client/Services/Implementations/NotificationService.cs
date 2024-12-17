using System.Text.Json;
using client.Dto.Notification;

namespace client.Services.Implementations;

using System;
using System.Threading.Tasks;

public class NotificationService : INotificationService
{
    private readonly HttpClient client;

    public NotificationService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<List<NotificationResponseDto>> GetNotifications()
    {
        HttpResponseMessage response =
            await client.GetAsync($"notifications");
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode)
        {
            List<NotificationResponseDto> notifications =
                JsonSerializer.Deserialize<List<NotificationResponseDto>>(
                    responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return notifications;
        }

        throw new Exception(responseContent);
    }
}