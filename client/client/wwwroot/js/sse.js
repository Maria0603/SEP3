export function connectSSE(url, dotnetHelper) {
    const eventSource = new EventSource(url);

    eventSource.onopen = (event) => {
        console.log("SSE connection opened", event);
    };

    eventSource.onmessage = (event) => {
        console.log("SSE message received:", event.data); // Add debugging
        dotnetHelper.invokeMethodAsync("ReceiveNotification", event.data);
    };

    eventSource.onerror = (event) => {
        console.error("SSE error", event);
        console.error("EventSource readyState:", eventSource.readyState);
        eventSource.close();
    };
}
