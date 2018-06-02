package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CCRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T19:54:29.108Z")
public class CCRequest {
  @SerializedName("members")
  private List<String> members = null;

  @SerializedName("startdatetime")
  private String startdatetime = null;

  @SerializedName("duration")
  private Integer duration = null;

  public CCRequest members(List<String> members) {
    this.members = members;
    return this;
  }

  public CCRequest addMembersItem(String membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<String>();
    }
    this.members.add(membersItem);
    return this;
  }

   /**
   * Get members
   * @return members
  **/
  @ApiModelProperty(value = "")
  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }

  public CCRequest startdatetime(String startdatetime) {
    this.startdatetime = startdatetime;
    return this;
  }

   /**
   * Get startdatetime
   * @return startdatetime
  **/
  @ApiModelProperty(value = "")
  public String getStartdatetime() {
    return startdatetime;
  }

  public void setStartdatetime(String startdatetime) {
    this.startdatetime = startdatetime;
  }

  public CCRequest duration(Integer duration) {
    this.duration = duration;
    return this;
  }

   /**
   * time duration (in minutes)
   * @return duration
  **/
  @ApiModelProperty(value = "time duration (in minutes)")
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CCRequest ccRequest = (CCRequest) o;
    return Objects.equals(this.members, ccRequest.members) &&
        Objects.equals(this.startdatetime, ccRequest.startdatetime) &&
        Objects.equals(this.duration, ccRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(members, startdatetime, duration);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CCRequest {\n");
    
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    startdatetime: ").append(toIndentedString(startdatetime)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

