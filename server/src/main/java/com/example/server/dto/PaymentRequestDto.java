﻿package com.example.server.dto;

public class PaymentRequestDto
{
  private String paymentToken; // Token from Stripe.js
  private int amount; // Amount in ore
  private String currency="dkk"; // Currency code
  private String successUrl;
  private String cancelUrl;

  public String getCancelUrl()
  {
    return cancelUrl;
  }

  public void setCancelUrl(String cancelUrl)
  {
    this.cancelUrl = cancelUrl;
  }


  public String getSuccessUrl()
  {
    return successUrl;
  }

  public void setSuccessUrl(String successUrl)
  {
    this.successUrl = successUrl;
  }

  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency(String currency)
  {
    this.currency = currency;
  }

  public int getAmount()
  {
    return amount;
  }

  public void setAmount(int amount)
  {
    this.amount = amount;
  }

  public String getPaymentToken()
  {
    return paymentToken;
  }

  public void setPaymentToken(String paymentToken)
  {
    this.paymentToken = paymentToken;
  }



}

