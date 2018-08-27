package cn.self.cloud.commonutils.simple;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ChenPing on 15-12-22.
 */
public class HyInfo {
    private int iid;
    private Integer tjid      ;
    private String pubdate   ;
    private String uname     ;
    private String citynum   ;
    private String icode     ;
    private String md5       ;
    private String state     ;
    private String source    ;
    private String infoclass ;
    private String infotype  ;
    private String pubtype   ;
    private String publist   ;
    private String phone     ;
    private String pubtimes  ;
    private Integer parkid    ;
    private String scity     ;
    private String ecity     ;
    private String gname     ;
    private String gweight   ;
    private String gwunit    ;
    private String cartype   ;
    private String carcount  ;
    private String carlen    ;
    private String price     ;
    private String punit     ;
    private String memo      ;
    private String enddate   ;
    private String infotxt   ;
    private String extdata   ;
    private Integer proxyid   ;
    private String packstr   ;
    private String devided;
    private int hash;
    private String keyword;
    private String ckeyword;
    private String scityids;
    private String ecityids;
    private  int isunvisible;
    private  int ishandle;

    public HyInfo() {
    }
    public HyInfo(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt(1)
                ,(resultSet.getObject(2)==null? null:resultSet.getInt(2)) ,
                resultSet.getString(3),resultSet.getString(4)
                ,resultSet.getString(5),resultSet.getString(6),resultSet.getString(7)
                ,resultSet.getString(8),resultSet.getString(9),resultSet.getString(10)
                ,resultSet.getString(11),resultSet.getString(12),resultSet.getString(13)
                ,resultSet.getString(14),resultSet.getString(15),
                (resultSet.getObject(16)==null?null:resultSet.getInt(16))
                ,resultSet.getString(17),resultSet.getString(18),resultSet.getString(19)
                ,resultSet.getString(20),resultSet.getString(21),resultSet.getString(22)
                ,resultSet.getString(23),resultSet.getString(24),resultSet.getString(25)
                ,resultSet.getString(26),resultSet.getString(27),resultSet.getString(28)
                ,resultSet.getString(29),resultSet.getString(30)
                ,(resultSet.getObject(31)==null?null:resultSet.getInt(31))
                ,resultSet.getString(32),resultSet.getString(33),resultSet.getString(34),resultSet.getInt(35),resultSet.getInt(36)
        );
    }
    public HyInfo(int iid,
                     Integer tjid,
                     String pubdate, String uname,
                     String citynum, String icode, String md5,
                     String state, String source, String infoclass,
                     String infotype, String pubtype, String publist,
                     String phone, String pubtimes,
                     Integer parkid,
                     String scity, String ecity, String gname,
                     String gweight, String gwunit, String cartype,
                     String carcount, String carlen, String price,
                     String punit, String memo, String enddate,
                     String infotxt, String extdata, Integer proxyid, String packstr,String scityids,String ecityids,int isunvisible,int ishandle) {
        this.iid = iid;
        this.tjid = tjid;
        this.pubdate = pubdate;
        this.uname = uname;
        this.citynum = citynum;
        this.icode = icode;
        this.md5 = md5;
        this.state = state;
        this.source = source;
        this.infoclass = infoclass;
        this.infotype = infotype;
        this.pubtype = pubtype;
        this.publist = publist;
        this.phone = phone;
        this.pubtimes = pubtimes;
        this.parkid = parkid;
        this.scity = scity;
        this.ecity = ecity;
        this.gname = gname;
        this.gweight = gweight;
        this.gwunit = gwunit;
        this.cartype = cartype;
        this.carcount = carcount;
        this.carlen = carlen;
        this.price = price;
        this.punit = punit;
        this.memo = memo;
        this.enddate = enddate;
        this.infotxt = infotxt;
        this.extdata = extdata;
        this.proxyid = proxyid;
        this.packstr = packstr;
        this.scityids=scityids;
        this.ecityids=ecityids;
        this.isunvisible=isunvisible;
        this.ishandle=ishandle;
    }

    public int getIshandle() {
        return ishandle;
    }

    public void setIshandle(int ishandle) {
        this.ishandle = ishandle;
    }

    public int getIsunvisible() {
        return isunvisible;
    }

    public void setIsunvisible(int isunvisible) {
        this.isunvisible = isunvisible;
    }

    public Integer getTjid() {
        return tjid;
    }

    public void setTjid(Integer tjid) {
        this.tjid = tjid;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCitynum() {
        return citynum;
    }

    public void setCitynum(String citynum) {
        this.citynum = citynum;
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInfoclass() {
        return infoclass;
    }

    public void setInfoclass(String infoclass) {
        this.infoclass = infoclass;
    }

    public String getInfotype() {
        return infotype;
    }

    public void setInfotype(String infotype) {
        this.infotype = infotype;
    }

    public String getPubtype() {
        return pubtype;
    }

    public void setPubtype(String pubtype) {
        this.pubtype = pubtype;
    }

    public String getPublist() {
        return publist;
    }

    public void setPublist(String publist) {
        this.publist = publist;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPubtimes() {
        return pubtimes;
    }

    public void setPubtimes(String pubtimes) {
        this.pubtimes = pubtimes;
    }

    public Integer getParkid() {
        return parkid;
    }

    public void setParkid(Integer parkid) {
        this.parkid = parkid;
    }

    public String getScity() {
        return scity;
    }

    public void setScity(String scity) {
        this.scity = scity;
    }

    public String getEcity() {
        return ecity;
    }

    public void setEcity(String ecity) {
        this.ecity = ecity;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGweight() {
        return gweight;
    }

    public void setGweight(String gweight) {
        this.gweight = gweight;
    }

    public String getGwunit() {
        return gwunit;
    }

    public void setGwunit(String gwunit) {
        this.gwunit = gwunit;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getCarcount() {
        return carcount;
    }

    public void setCarcount(String carcount) {
        this.carcount = carcount;
    }

    public String getCarlen() {
        return carlen;
    }

    public void setCarlen(String carlen) {
        this.carlen = carlen;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPunit() {
        return punit;
    }

    public void setPunit(String punit) {
        this.punit = punit;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getInfotxt() {
        return infotxt;
    }

    public void setInfotxt(String infotxt) {
        this.infotxt = infotxt;
    }

    public String getExtdata() {
        return extdata;
    }

    public void setExtdata(String extdata) {
        this.extdata = extdata;
    }

    public Integer getProxyid() {
        return proxyid;
    }

    public void setProxyid(Integer proxyid) {
        this.proxyid = proxyid;
    }

    public String getPackstr() {
        return packstr;
    }

    public void setPackstr(String packstr) {
        this.packstr = packstr;
    }

    public String getDevided() {
        return devided;
    }

    public void setDevided(String devided) {
        this.devided = devided;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCkeyword() {
        return ckeyword;
    }

    public void setCkeyword(String ckeyword) {
        this.ckeyword = ckeyword;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getScityids() {
        return scityids;
    }

    public void setScityids(String scityids) {
        this.scityids = scityids;
    }

    public String getEcityids() {
        return ecityids;
    }

    public void setEcityids(String ecityids) {
        this.ecityids = ecityids;
    }

    @Override
    public String toString() {
        return "HyInfo{" +
                "iid=" + iid +
                ", tjid=" + tjid +
                ", pubdate='" + pubdate + '\'' +
                ", uname='" + uname + '\'' +
                ", citynum='" + citynum + '\'' +
                ", icode='" + icode + '\'' +
                ", md5='" + md5 + '\'' +
                ", state='" + state + '\'' +
                ", source='" + source + '\'' +
                ", infoclass='" + infoclass + '\'' +
                ", infotype='" + infotype + '\'' +
                ", pubtype='" + pubtype + '\'' +
                ", publist='" + publist + '\'' +
                ", phone='" + phone + '\'' +
                ", pubtimes='" + pubtimes + '\'' +
                ", parkid=" + parkid +
                ", scity='" + scity + '\'' +
                ", ecity='" + ecity + '\'' +
                ", gname='" + gname + '\'' +
                ", gweight='" + gweight + '\'' +
                ", gwunit='" + gwunit + '\'' +
                ", cartype='" + cartype + '\'' +
                ", carcount='" + carcount + '\'' +
                ", carlen='" + carlen + '\'' +
                ", price='" + price + '\'' +
                ", punit='" + punit + '\'' +
                ", memo='" + memo + '\'' +
                ", enddate='" + enddate + '\'' +
                ", infotxt='" + infotxt + '\'' +
                ", extdata='" + extdata + '\'' +
                ", proxyid=" + proxyid +
                ", packstr='" + packstr + '\'' +
                ", devided='" + devided + '\'' +
                ", hash=" + hash +
                ", keyword='" + keyword + '\'' +
                ", ckeyword='" + ckeyword + '\'' +
                ", scityids='" + scityids + '\'' +
                ", ecityids='" + ecityids + '\'' +
                ", isunvisible=" + isunvisible +
                '}';
    }
}
