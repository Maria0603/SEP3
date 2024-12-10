package com.example.shared.entities.domainEntities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "notifications") public class Notification
{
  @Id private String id;
  @Field("user_id") private String userId;
  @Field("user_role") private String userRole;
  // When the user clicks a notification, the page containing this subject will open
  @Field("subject_id") private String subjectId;
  @Field("content") private String content;
  @Field("timestamp") private LocalDateTime timestamp;
  /* For now, we only have notifications about purchases, but we should
  design the system to be Open-Closed. If we add notifications about new offers from
  favourite businesses, the subjectId will be the offer ID, and we will know what
  methods to call depending on this type - if it's PURCHASE, then we call
  getPurchaseById(), if it's OFFER, we call getOfferById() and so on....*/
  @Field("type") private String type;

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getSubjectId()
  {
    return subjectId;
  }

  public void setSubjectId(String subjectId)
  {
    this.subjectId = subjectId;
  }

  public LocalDateTime getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public String getUserRole()
  {
    return userRole;
  }

  public void setUserRole(String userRole)
  {
    this.userRole = userRole;
  }

}
