package vn.edu.stu.myapplication.Model;

public class SP {
    public int ID;
    public String Ten;
    public String MoTa;
    public byte[] Anh;
    public String Loai;
    public Integer Gia;
    public Integer Soluong;

    public SP(int ID, String ten, String moTa, byte[] anh, String loai, Integer gia, Integer soluong) {
        this.ID = ID;
        Ten = ten;
        MoTa = moTa;
        Anh = anh;
        Loai = loai;
        Gia = gia;
        Soluong = soluong;
    }
}
