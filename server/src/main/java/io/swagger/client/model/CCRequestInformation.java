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
 * CCRequestInformation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-03T22:43:15.870Z")
public class CCRequestInformation {
  @SerializedName("members")
  private List<String> members = null;

  @SerializedName("startDatetime")
  private String startDatetime = null;

  @SerializedName("endDatetime:")
  private String endDatetime = null;

  public CCRequestInformation members(List<String> members) {
    this.members = members;
    return this;
  }

  public CCRequestInformation addMembersItem(String membersItem) {
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

  public CCRequestInformation startDatetime(String startDatetime) {
    this.startDatetime = startDatetime;
    return this;
  }

   /**
   * Get startDatetime
   * @return startDatetime
  **/
  @ApiModelProperty(value = "")
  public String getStartDatetime() {
    return startDatetime;
  }

  public void setStartDatetime(String startDatetime) {
    this.startDatetime = startDatetime;
  }

  public CCRequestInformation endDatetime(String endDatetime) {
    this.endDatetime = endDatetime;
    return this;
  }

   /**
   * time duration (in minutes)
   * @return endDatetime
  **/
  @ApiModelProperty(value = "time duration (in minutes)")
  public String getEndDatetime() {
    return endDatetime;
  }

  public void setEndDatetime(String endDatetime) {
    this.endDatetime = endDatetime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CCRequestInformation ccRequestInformation = (CCRequestInformation) o;
    return Objects.equals(this.members, ccRequestInformation.members) &&
        Objects.equals(this.startDatetime, ccRequestInformation.startDatetime) &&
        Objects.equals(this.endDatetime, ccRequestInformation.endDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(members, startDatetime, endDatetime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CCRequestInformation {\n");
    
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    startDatetime: ").append(toIndentedString(startDatetime)).append("\n");
    sb.append("    endDatetime: ").append(toIndentedString(endDatetime)).append("\n");
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

