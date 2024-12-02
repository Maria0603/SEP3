using client;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using client.Services;
using Stripe;
using Syncfusion.Blazor;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

// Configure HttpClient for API calls
builder.Services.AddScoped(sp => new HttpClient
    { BaseAddress = new Uri("http://localhost:8082/") });
builder.Services.AddScoped<IOfferService, OfferService>();
builder.Services.AddScoped<IOrderService, OrderService>();
builder.Services.AddSyncfusionBlazor();


StripeConfiguration.ApiKey =
    "sk_test_51QLXFcEJybmJ8DbtUW95vPtVV4vCIHtWi7MgOuqlhLngWoki5Bo0iMF8s2Nfxhzpub5gnIAD3d0CUpZBcSAJanmp004vLU11xd";
await builder.Build().RunAsync();