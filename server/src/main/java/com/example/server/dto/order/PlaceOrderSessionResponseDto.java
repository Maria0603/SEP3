package com.example.server.dto.order;

public class PlaceOrderSessionResponseDto
{
  private String sessionId;
  private String Url;

  public String getUrl()
  {
    return Url;
  }

  public void setUrl(String url)
  {
    Url = url;
  }

  public String getSessionId()
  {
    return sessionId;
  }

  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }


}
