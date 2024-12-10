namespace client.Services.Implementations;

using Microsoft.AspNetCore.SignalR.Client;

public class NotificationService
{
    public event Action<string>? OnNotificationReceived;
    private HubConnection? _hubConnection;

    public async Task Connect(string userToken)
    {
        
        _hubConnection = new HubConnectionBuilder()
            .WithUrl("https://your-spring-server/notifications", options =>
            {
                options.AccessTokenProvider = () => Task.FromResult(userToken);
            })
            .Build();

        _hubConnection.On<string>("ReceiveNotification", (message) =>
        {
            OnNotificationReceived?.Invoke(message);
        });

        await _hubConnection.StartAsync();
    }

    public async Task Disconnect()
    {
        if (_hubConnection != null)
        {
            await _hubConnection.StopAsync();
            await _hubConnection.DisposeAsync();
        }
    }
}
