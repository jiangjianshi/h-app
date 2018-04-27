package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.Date;
import java.util.List;

import com.huifenqi.hzf_platform.context.enums.ali.FaceEnum;
import com.huifenqi.hzf_platform.context.enums.ali.FitmentTypeEnum;
import com.huifenqi.hzf_platform.context.enums.ali.RentTypeEnum;
import com.huifenqi.hzf_platform.context.enums.ali.RoomTypeEnum;
import com.huifenqi.hzf_platform.context.enums.ali.StatusEnum;

public class AliSysHouseDetailToLocalDto {

    private String title;//房屋标题
    private String desc;//房屋描述
    private List<Long> imageIds;//房屋图片ID
    private Long videoCoverId;//视频封面图ID
    private String videoId;//视频ID
    private String detailAddress;//详细地址
    private String communityName;//小区名称
    private String buildingAddr;//出租房屋门牌号
    private Long rentType;//出租类型  1长租 2短租
    private Long rentMode;//出租方式 1整租 2合租
    private Long houseType;//房源类型 1集中式 2分散式
    private Long rent;//租金 单位分
    private Long deposit;//押金 单位分
    private Long roomArea;//房间面积
    private Long bedRoomCnt;//室
    private Long livingRoomCnt;//厅
    private Long bathRoomCnt;//卫
    private Long decorateLevel;//装修程度
    private Long payModePaid;//付
    private Long payModePre;//押
    private List<Long> roomConfig;//房屋配套
    private List<Long> aroundConfig;//周边配套
    private Long face;//朝向
    private Long totalAreas;//房源总面积
    private Long houseClass;//房源类型
    private Long roomClass;//房间类型
    private Long bathRoomType;//是否独卫
    private Long bedRoomArea;//卧室面积
    private Date settlingTime;//可入住时间
    private Long floor;//楼层
    private Long totalFloor;//总楼层
    private String userNick;
    private Long houseStatus;//房屋状态 0待出租  1已租
    private String companyId;//公司ID(校验账号)
    private String source;//渠道
    private List<String> imageHzfList;
    private Long itemId;
    private String sellId;
    private int roomId;
    private String lat;
    private String lng;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public List<Long> getImageIds() {
        return imageIds;
    }
    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
    public Long getVideoCoverId() {
        return videoCoverId;
    }
    public void setVideoCoverId(Long videoCoverId) {
        this.videoCoverId = videoCoverId;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getDetailAddress() {
        return detailAddress;
    }
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
    public String getCommunityName() {
        return communityName;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    public String getBuildingAddr() {
        return buildingAddr;
    }
    public void setBuildingAddr(String buildingAddr) {
        this.buildingAddr = buildingAddr;
    }
    public Long getRentType() {
        return rentType;
    }
    public void setRentType(Long rentType) {
        this.rentType = rentType;
    }
    public Long getRentMode() {
        if (rentMode != null) {
            return RentTypeEnum.getAliCode(rentMode);
        }
        return rentMode;
    }
    public void setRentMode(Long rentMode) {
        this.rentMode = rentMode;
    }
    public Long getHouseType() {
        if (rentMode != null) {
            return RentTypeEnum.getAliCode(rentMode);
        }
        return rentMode;
    }
    public void setHouseType(Long houseType) {
        this.houseType = houseType;
    }
    public Long getRent() {
        return rent;
    }
    public void setRent(Long rent) {
        this.rent = rent;
    }
    public Long getDeposit() {
        return deposit;
    }
    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }
    public Long getRoomArea() {
        return roomArea;
    }
    public void setRoomArea(Long roomArea) {
        this.roomArea = roomArea;
    }
    public Long getBedRoomCnt() {
        return bedRoomCnt;
    }
    public void setBedRoomCnt(Long bedRoomCnt) {
        this.bedRoomCnt = bedRoomCnt;
    }
    public Long getLivingRoomCnt() {
        return livingRoomCnt;
    }
    public void setLivingRoomCnt(Long livingRoomCnt) {
        this.livingRoomCnt = livingRoomCnt;
    }
    public Long getBathRoomCnt() {
        return bathRoomCnt;
    }
    public void setBathRoomCnt(Long bathRoomCnt) {
        this.bathRoomCnt = bathRoomCnt;
    }
    public Long getDecorateLevel() {
        if (decorateLevel != null) {
            return FitmentTypeEnum.getAliCode(decorateLevel);
        }
        return decorateLevel;
    }
    public void setDecorateLevel(Long decorateLevel) {
        this.decorateLevel = decorateLevel;
    }
    public Long getPayModePaid() {
        return payModePaid;
    }
    public void setPayModePaid(Long payModePaid) {
        this.payModePaid = payModePaid;
    }
    public Long getPayModePre() {
        return payModePre;
    }
    public void setPayModePre(Long payModePre) {
        this.payModePre = payModePre;
    }
    public List<Long> getRoomConfig() {
        return roomConfig;
    }
    public void setRoomConfig(List<Long> roomConfig) {
        this.roomConfig = roomConfig;
    }
    public List<Long> getAroundConfig() {
        return aroundConfig;
    }
    public void setAroundConfig(List<Long> aroundConfig) {
        this.aroundConfig = aroundConfig;
    }
    public Long getFace() {
        if (face != null) {
            return FaceEnum.getAliCode(face);
        }
        return face;
    }
    public void setFace(Long face) {
        this.face = face;
    }
    
    public Long getTotalAreas() {
        return totalAreas;
    }
    public void setTotalAreas(Long totalAreas) {
        this.totalAreas = totalAreas;
    }
    public Long getHouseClass() {
        return houseClass;
    }
    public void setHouseClass(Long houseClass) {
        this.houseClass = houseClass;
    }
    public Long getRoomClass() {
        if (roomClass != null) {
            return RoomTypeEnum.getAliCode(roomClass);
        }
        return roomClass;
    }
    public void setRoomClass(Long roomClass) {
        this.roomClass = roomClass;
    }
    public Long getBathRoomType() {
        return bathRoomType;
    }
    public void setBathRoomType(Long bathRoomType) {
        this.bathRoomType = bathRoomType;
    }
    public Long getBedRoomArea() {
        return bedRoomArea;
    }
    public void setBedRoomArea(Long bedRoomArea) {
        this.bedRoomArea = bedRoomArea;
    }
    public Date getSettlingTime() {
        return settlingTime;
    }
    public void setSettlingTime(Date settlingTime) {
        this.settlingTime = settlingTime;
    }
    public Long getFloor() {
        return floor;
    }
    public void setFloor(Long floor) {
        this.floor = floor;
    }
    public Long getTotalFloor() {
        return totalFloor;
    }
    public void setTotalFloor(Long totalFloor) {
        this.totalFloor = totalFloor;
    }
    public String getUserNick() {
        return userNick;
    }
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
    public Long getHouseStatus() {
        if (houseStatus != null) {
            return StatusEnum.getAliCode(houseStatus);
        }
        return houseStatus;
    }
    public void setHouseStatus(Long houseStatus) {
        this.houseStatus = houseStatus;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public List<String> getImageHzfList() {
        return imageHzfList;
    }
    public void setImageHzfList(List<String> imageHzfList) {
        this.imageHzfList = imageHzfList;
    }
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getSellId() {
        return sellId;
    }
    public void setSellId(String sellId) {
        this.sellId = sellId;
    }
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
   
}
