using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using client;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");


// Configure HttpClient for API calls
builder.Services.AddScoped(sp => new HttpClient
    { BaseAddress = new Uri("http://localhost:8082/") });

await builder.Build().RunAsync();