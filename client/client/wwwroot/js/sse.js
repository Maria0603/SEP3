export function connectSSE(url, dotnetHelper, token) {
    
    console.log("Token to send: ", token); 
    //const eventSource = new EventSource(url, {headers});

    const eventSource = new EventSource(`${url}?token=${encodeURIComponent(token)}`);


    /* eventSource.onmessage = (event) => {
         dotnetHelper.invokeMethodAsync("ReceiveNotification", event.data);
     };*/
    
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
