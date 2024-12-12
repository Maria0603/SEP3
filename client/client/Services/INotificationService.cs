using client.DTO.Notification;

namespace client.Services;

public interface INotificationService
{
    Task<List<NotificationResponseDto>> GetNotifications();
}