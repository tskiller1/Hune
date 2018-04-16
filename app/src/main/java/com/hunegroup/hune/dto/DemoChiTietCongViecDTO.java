package com.hunegroup.hune.dto;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tskil on 11/07/2017.
 */

public class DemoChiTietCongViecDTO {

    private List<DemoChiTietCongViecDTO> list;

    private int id;
    private String loai;
    private String ten;
    private String moTa;
    private String luong;
    private int soLuong;
    private String diaChi;
    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLuong() {
        return luong;
    }

    public void setLuong(String luong) {
        this.luong = luong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    public DemoChiTietCongViecDTO(){
        list = new ArrayList<>();
    }
    private void addList(){
        Double lat = 10.817393083416114;
        Double lng = 106.67777247726919;
        for (int i = 0; i < 5; i++){
            DemoChiTietCongViecDTO item = new DemoChiTietCongViecDTO();
            item.setId(i+1);
            item.setLoai("Dịch vụ, tiệc cưới");
            item.setTen("NV Phục Vụ Tiệc Cưới");
            item.setMoTa("alkdjfkajdflkjzxmcn,zmnckajflkajsdlkfjalsdkjfla;ksjdfl;kajsdlkfjasld;kfjlaksdjflkasdfjhaskdjfhkalsjdhfkljasdhfkljadshklfjashdklfjahsdljfhaklsjdfhkalsjdfhklajsfjh");
            item.setLuong("120000/Ngày");
            item.setDiaChi("371 Nguyễn Kiệm, Gò Vấp");
            item.setSoLuong(20);
            item.setLatLng(new LatLng(lat,lng));
            lat = lat + 0.005;
            list.add(item);
        }
    }
    public List<DemoChiTietCongViecDTO> getList(){
        list = new ArrayList<>();
        addList();
        return list;
    }
}
