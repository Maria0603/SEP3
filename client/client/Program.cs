using client;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using client.Services;
using Stripe;
using Syncfusion.Blazor;
Syncfusion.Licensing.SyncfusionLicenseProvider.RegisterLicense("Ngo9BigBOggjHTQxAR8/V1NDaF5cWWtCf1FpRmJGdld5fUVHYVZUTXxaS00DNHVRdkdnWH1ednRQR2VZU0N2XEU=");

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