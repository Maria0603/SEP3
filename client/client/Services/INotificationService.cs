using client.Dto.Notification;

namespace client.Services;

public interface INotificationService
{
    Task<List<NotificationResponseDto>> GetNotifications();
}