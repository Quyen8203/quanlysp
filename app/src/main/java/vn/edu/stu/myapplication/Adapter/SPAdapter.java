package vn.edu.stu.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.stu.myapplication.ChiTietSPActivity;
import vn.edu.stu.myapplication.Database.Database;
import vn.edu.stu.myapplication.Model.SP;
import vn.edu.stu.myapplication.R;
import vn.edu.stu.myapplication.UpdateSPActivity;

public class SPAdapter extends BaseAdapter {

    Activity context;
    ArrayList<SP> list;

    public SPAdapter(Activity context, ArrayList<SP> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.dong_sp, null);

        ImageView imgAVT = (ImageView) row.findViewById(R.id.imageviewHinh);

        TextView txtTen = (TextView) row.findViewById(R.id.textviewTen);
        TextView txtLoai = (TextView) row.findViewById(R.id.textviewLoai);
        TextView txtSoluong = (TextView) row.findViewById(R.id.textviewSoLuong);

        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);

        SP SP = list.get(position);

        txtTen.setText(SP.Ten);
        txtLoai.setText(SP.Loai);
        txtSoluong.setText("Số lượng: " + SP.Soluong);

        Bitmap bmAVT = BitmapFactory.decodeByteArray(SP.Anh, 0, SP.Anh.length);
        imgAVT.setImageBitmap(bmAVT);

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle(R.string.del1);
                builder.setMessage(R.string.del2);
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(SP.ID);
                        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        imgAVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSPActivity.class);
                intent.putExtra("ID", SP.ID);
                context.startActivity(intent);
            }
        });

        imgAVT.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, ChiTietSPActivity.class);
                intent.putExtra("ID", SP.ID);
                context.startActivity(intent);
                return false;
            }
        });

        return row;
    }

    private void delete(int id) {
        SQLiteDatabase database = Database.initDatabase(context, "data.db");
        database.delete("Phu", "ID = ?", new String[]{id + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT ID, Ten, MoTa, Anh, Loai, Gia, Soluong FROM Phu", null);
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            String Ten = cursor.getString(1);
            String MoTa = cursor.getString(2);
            byte[] Anh = cursor.getBlob(3);
            String Loai = cursor.getString(4);
            Integer Gia = cursor.getInt(5);
            Integer Soluong = cursor.getInt(6);

            list.add(new SP(ID, Ten, MoTa, Anh, Loai, Gia, Soluong));
        }
        notifyDataSetChanged();
    }


}
