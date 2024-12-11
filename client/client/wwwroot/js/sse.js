export function connectSSE(url, dotnetHelper, token) {
    
    console.log("Token to send: ", token); 
    //const eventSource = new EventSource(url, {headers});

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
/*

// sse.js
window.connectSSE = (url, dotNetRef, token) => {
    const eventSource = new EventSource(`${url}?token=${encodeURIComponent(token)}`);

    eventSource.onmessage = function (event) {
        const notification = event.data;
        // Call the .NET method to handle the notification
        dotNetRef.invokeMethodAsync('ReceiveNotification', notification)
            .catch(err => console.error(err));
    };

    eventSource.onerror = function (error) {
        console.error('Error with SSE connection:', error);
    };
};
*/

