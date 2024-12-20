﻿//sse.js
export function connectSSE(url, dotnetHelper, token) {
    
    const eventSource = new EventSource(`${url}?token=${encodeURIComponent(token)}`);
    
    eventSource.onmessage = (event) => {
        console.log("SSE message received:", event.data); // Add debugging
        if(event.data.toString()!=="ping")
            dotnetHelper.invokeMethodAsync("ReceiveNotification", event.data);
    };

    eventSource.onerror = (event) => {
        console.error("SSE error", event);
        console.error("EventSource readyState:", eventSource.readyState);
        eventSource.close();
    };
}
