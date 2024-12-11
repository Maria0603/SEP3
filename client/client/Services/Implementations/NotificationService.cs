using System.Text.Json;
using client.DTO.Notification;

namespace client.Services.Implementations;

using System;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

public class NotificationService
{
    //private ClientWebSocket _webSocket;

    public event Action<string> OnMessageReceived;
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
/*
    public async Task ConnectAsync(string uri)
    {
        _webSocket = new ClientWebSocket();
        await _webSocket.ConnectAsync(new Uri(uri), CancellationToken.None);

        // Start receiving messages
        _ = Task.Run(ReceiveMessagesAsync);
    }

    private async Task ReceiveMessagesAsync()
    {
        var buffer = new byte[1024 * 4];
        while (_webSocket.State == WebSocketState.Open)
        {
            var result = await _webSocket.ReceiveAsync(
                new ArraySegment<byte>(buffer), CancellationToken.None);
            if (result.MessageType == WebSocketMessageType.Text)
            {
                var message = Encoding.UTF8.GetString(buffer, 0, result.Count);
                OnMessageReceived?.Invoke(message);
            }
            else if (result.MessageType == WebSocketMessageType.Close)
            {
                await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure,
                    string.Empty, CancellationToken.None);
            }
        }
    }

    public async Task DisconnectAsync()
    {
        if (_webSocket != null && _webSocket.State == WebSocketState.Open)
        {
            await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure,
                string.Empty, CancellationToken.None);
        }
    }

    public bool IsConnected => _webSocket?.State == WebSocketState.Open;*/
}