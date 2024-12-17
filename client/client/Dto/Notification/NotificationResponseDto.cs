namespace client.Dto.Notification;

public class NotificationResponseDto
{
    public string UserId { get; set; }
    public string UserRole { get; set; }
    public string Content { get; set; }
    public DateTime Timestamp { get; set; }
    public string SubjectId { get; set; }
    public string Type { get; set; } 
}