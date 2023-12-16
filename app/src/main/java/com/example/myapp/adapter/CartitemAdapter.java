package com.example.myapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.MainActivity;
import com.example.myapp.Models.InfoItem_colorModel;
import com.example.myapp.Models.Sizee;
import com.example.myapp.R;
import com.example.myapp.cart.CartItemProduct;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartitemAdapter extends RecyclerView.Adapter<CartitemAdapter.ViewHoler> {
    FirebaseFirestore db;
    private Context context;
    private ArrayList<CartItemProduct> list;
    private updaterThongTinCart onDataChangeListener;

    public void setOnDataChangeListener(updaterThongTinCart listener) {
        this.onDataChangeListener = listener;
    }
    public CartitemAdapter(Context context, ArrayList<CartItemProduct> list) {
        this.context = context;
        this.list = list;
    }

    public CartitemAdapter() {
    }

    @NonNull
    @Override
    public CartitemAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cartitem,parent,false);

        return new ViewHoler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartitemAdapter.ViewHoler holder, int position) {

        db=FirebaseFirestore.getInstance();
        String imageUrl =list.get(position).getImgD(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(context)
                .load(imageUrl)
                .into(holder.img);
        holder.ln.setVisibility(View.GONE);
        holder.name.setText(list.get(position).getName());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.price.setText(list.get(position).getPrice());
        holder.quan.setText(list.get(position).getQuantity());
        String idsp1=list.get(position).getIdSP().trim();
        String idsp2=list.get(position).getId().trim();
        String [] quan=load(position,idsp1);

        holder.cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value=Integer.parseInt(holder.quan.getText().toString().trim());
                System.out.println(String.valueOf(value));
                value++;
//                holder.quan.setText(String.valueOf(value));
//                System.out.println(Integer.parseInt(quan[0]) < value);
                if(Integer.parseInt(quan[0]) < value) {
                    Toast.makeText(v.getContext(),"Mặt hàng này chỉ còn:"+ quan[0],Toast.LENGTH_SHORT).show();
                }
                else {


                    upDateCartCong(idsp1,list.get(position).getColor(),list.get(position).getSize(),v,Integer.parseInt(quan[0]),position);
                    holder.quan.setText(String.valueOf(value));

                }
            }
        });
        holder.tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value=Integer.parseInt(holder.quan.getText().toString().trim());
                System.out.println(String.valueOf(value));
                value--;
//                holder.quan.setText(String.valueOf(value));
//                System.out.println(Integer.parseInt(quan[0]) < value);
                if(0>= value) {
                    Toast.makeText(v.getContext(),"Phải ít nhất 1 sản phẩm",Toast.LENGTH_SHORT).show();
                }
                else {


                    upDateCartTru(idsp1,list.get(position).getColor(),list.get(position).getSize(),v,Integer.parseInt(quan[0]),position);
                    holder.quan.setText(String.valueOf(value));


                }
            }
        });
        holder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showYesNoDialog(v,list.get(position).getId(),position);

            }
        });

    }

    private int demItem() {
        int count=0;
        for (int i=0;i<list.size();i++){
            count+= Integer.parseInt(list.get(i).getQuantity());
        }
        return count;
    }
    private int diemSL(View v){
    final int[] count = {0};
    if(MainActivity.loginWithAcc==true){
        Query query = db.collection("AddToCart")
                .document(MainActivity.auth.getCurrentUser().getUid())
                .collection("CurrentUser");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document !=null){
                            String id=document.getId().toString();
                            String idsp=document.getData().get("ProductId").toString();
                            String name= document.getData().get("PrductName").toString();
                            String imgD= document.getData().get("ProductImg").toString();
                            String price= document.getData().get("ProductPrice").toString();
                            String quantity= document.getData().get("ProductQuantity").toString();
                            String sz= document.getData().get("ProductSize").toString();
                            String color= document.getData().get("ProductColor").toString();
                            count[0] +=Integer.parseInt(quantity);
                            System.out.println(quantity);
//                            tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
////                            System.out.println(name+"color: "+ color +"size: "+sz);
//                            CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
//                            arr.add(c);
//                            System.out.println(countItemCart);
//                            System.out.println(tong);
//                            adapter.notifyDataSetChanged();
//                            countItem.setText(countItemCart +" item | "+ tong +"$");
                        }
                    }
                }
            }
        });
    }
//    System.out.println(countItemCart);
//    System.out.println(tong);
    if(MainActivity.loginWithGG==true){
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(v.getContext());
        Query query = db.collection("AddToCart")
                .document(account.getId())
                .collection("CurrentUser");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document !=null){
                            String id=document.getId().toString();
                            String idsp=document.getData().get("ProductId").toString();
                            String name= document.getData().get("PrductName").toString();
                            String imgD= document.getData().get("ProductImg").toString();
                            String price= document.getData().get("ProductPrice").toString();
                            String quantity= document.getData().get("ProductQuantity").toString();
                            String sz= document.getData().get("ProductSize").toString();
                            String color= document.getData().get("ProductColor").toString();
                            count[0]+=Integer.parseInt(quantity);
//                            tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
////                            System.out.println(name+"color: "+ color +"size: "+sz);
//                            CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
//                            arr.add(c);
//                            adapter.notifyDataSetChanged();
//                            countItem.setText(countItemCart +" item | "+ tong +"$");
                        }
                    }
                }
            }
        });
    }
    return count[0];
}
    private int sumTong(View v){
        final int[] count = {0};
        if(MainActivity.loginWithAcc==true){
            Query query = db.collection("AddToCart")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document !=null){
                                String id=document.getId().toString();
                                String idsp=document.getData().get("ProductId").toString();
                                String name= document.getData().get("PrductName").toString();
                                String imgD= document.getData().get("ProductImg").toString();
                                String price= document.getData().get("ProductPrice").toString();
                                String quantity= document.getData().get("ProductQuantity").toString();
                                String sz= document.getData().get("ProductSize").toString();
                                String color= document.getData().get("ProductColor").toString();
                                count[0]+=Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
                                System.out.println(Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", "")));
//                            tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
////                            System.out.println(name+"color: "+ color +"size: "+sz);
//                            CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
//                            arr.add(c);
//                            System.out.println(countItemCart);
//                            System.out.println(tong);
//                            adapter.notifyDataSetChanged();
//                            countItem.setText(countItemCart +" item | "+ tong +"$");
                            }
                        }
                    }
                }
            });
        }
//    System.out.println(countItemCart);
//    System.out.println(tong);
        if(MainActivity.loginWithGG==true){
            GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(v.getContext());
            Query query = db.collection("AddToCart")
                    .document(account.getId())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document !=null){
                                String id=document.getId().toString();
                                String idsp=document.getData().get("ProductId").toString();
                                String name= document.getData().get("PrductName").toString();
                                String imgD= document.getData().get("ProductImg").toString();
                                String price= document.getData().get("ProductPrice").toString();
                                String quantity= document.getData().get("ProductQuantity").toString();
                                String sz= document.getData().get("ProductSize").toString();
                                String color= document.getData().get("ProductColor").toString();
                                count[0]+=Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
//                            tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
////                            System.out.println(name+"color: "+ color +"size: "+sz);
//                            CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
//                            arr.add(c);
//                            adapter.notifyDataSetChanged();
//                            countItem.setText(countItemCart +" item | "+ tong +"$");
                            }
                        }
                    }
                }
            });
        }
        return count[0];
    }
    private int tingTongTen(){
        int tongHD=0;
      for (int i=0;i<list.size();i++){
          tongHD+= Integer.parseInt(list.get(i).getQuantity())*Double.parseDouble(list.get(i).getPrice().replaceAll("[^0-9]", ""));
      }
        return tongHD;
    }
    private void upDateCartCong(String idsp, String colorAdd, String sizeAdd,View v,int quan,int position){
//        System.out.println("id: "+idsp+"\ncolor: "+colorAdd+"\nsize: "+sizeAdd);
        Query query = db.collection("AddToCart")
                .document(MainActivity.auth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .whereEqualTo("ProductId",idsp).whereEqualTo("ProductColor",colorAdd).whereEqualTo("ProductSize",sizeAdd)
                .limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Lấy bản ghi đầu tiên
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        // Log để kiểm tra giá trị của ProductQuantity trước khi cập nhật
                        Log.d("Debug", "ProductQuantity before update: " + document.getLong("ProductQuantity"));

                        // Sản phẩm đã có trong giỏ hàng, thực hiện cập nhật ProductQuantity
                        int currentQuantity = document.getLong("ProductQuantity").intValue();
                        int newQuantity = currentQuantity + 1;
                        if(newQuantity<=quan){
                            Log.d("Tag", "New ProductQuantity: " + newQuantity);

                            // Cập nhật ProductQuantity trong cùng một bản ghi
                            document.getReference().update("ProductQuantity", newQuantity)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            notifyDataSetChanged();
                                            Toast.makeText(v.getContext(), "Update Cart Successful", Toast.LENGTH_SHORT).show();
                                            int dem=demItem() +1;
                                            int su=tingTongTen() + Integer.parseInt( list.get(position).getPrice().replaceAll("[^0-9]", ""));
                                            list.get(position).setQuantity(String.valueOf(Integer.parseInt(list.get(position).getQuantity())+1));
                                            if (onDataChangeListener != null) {
                                                onDataChangeListener.onDataChanged(dem,su);
                                                System.out.println(dem);
                                                System.out.println(su);
                                            }
                                            //                                            double tong=tingTongTen();
//                                            int countItem=demItem();
//                                            if (onDataChangeListener != null) {
//                                                onDataChangeListener.onDataChanged(countItem,tong);
//                                            }
//                                            notifyDataSetChanged();
                                            }
                                    });

                        }
                        else {
                            Toast.makeText(v.getContext(), "Sản phẩm đã có trong giỏ nếu thêm thì hàng không đủ", Toast.LENGTH_SHORT).show();
                        }

                        // Log để kiểm tra giá trị mới của ProductQuantity

                    }
                }
            }
        });
    }
    private void upDateCartTru(String idsp, String colorAdd, String sizeAdd,View v,int quan,int position){
//        System.out.println("id: "+idsp+"\ncolor: "+colorAdd+"\nsize: "+sizeAdd);
        Query query = db.collection("AddToCart")
                .document(MainActivity.auth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .whereEqualTo("ProductId",idsp).whereEqualTo("ProductColor",colorAdd).whereEqualTo("ProductSize",sizeAdd)
                .limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Lấy bản ghi đầu tiên
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        // Log để kiểm tra giá trị của ProductQuantity trước khi cập nhật
                        Log.d("Debug", "ProductQuantity before update: " + document.getLong("ProductQuantity"));

                        // Sản phẩm đã có trong giỏ hàng, thực hiện cập nhật ProductQuantity
                        int currentQuantity = document.getLong("ProductQuantity").intValue();
                        int newQuantity = currentQuantity - 1;
                        if(newQuantity<=quan){
                            Log.d("Tag", "New ProductQuantity: " + newQuantity);

                            // Cập nhật ProductQuantity trong cùng một bản ghi
                            document.getReference().update("ProductQuantity", newQuantity)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            notifyDataSetChanged();
                                            Toast.makeText(v.getContext(), "Update Cart Successful", Toast.LENGTH_SHORT).show();
                                            int dem=demItem() -1;
                                            int su=tingTongTen() - Integer.parseInt( list.get(position).getPrice().replaceAll("[^0-9]", ""));
                                            list.get(position).setQuantity(String.valueOf(Integer.parseInt(list.get(position).getQuantity())- 1));
                                            if (onDataChangeListener != null) {
                                                onDataChangeListener.onDataChanged(dem,su);
                                                System.out.println(dem);
                                                System.out.println(su);
                                            }
//                                            double tong=tingTongTen();
//                                            int countItem=demItem();
//                                            if (onDataChangeListener != null) {
//                                                onDataChangeListener.onDataChanged(countItem,tong);
//                                            }
//                                            notifyDataSetChanged();
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(v.getContext(), "Sản phẩm đã có trong giỏ nếu thêm thì hàng không đủ", Toast.LENGTH_SHORT).show();
                        }

                        // Log để kiểm tra giá trị mới của ProductQuantity

                    }
                }
            }
        });
    }
    private void showYesNoDialog(View v, String idSP, int position) {
        MyAlertDialog.showYesNoDialog(
                v.getContext(),  // context
                "Thông báo",  // title
                "Bạn có muốn xóa sản phẩm này không?",  // message
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng chọn "Yes"
                        // Thí dụ: Gọi hàm xóa bản ghi từ Firestore ở đây

                        Toast.makeText(context, "Hehe\uD83E\uDD21", Toast.LENGTH_SHORT).show();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng chọn "No"
                        db=FirebaseFirestore.getInstance();

                        deleteSanPham( idSP, position,v);
                        Toast.makeText(context, "đã xóa", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void deleteSanPham(String idSP1, int positon,View v) {
//        System.out.println(MainActivity.auth.getCurrentUser().getUid());
//        System.out.println(idSP1);
        if (MainActivity.loginWithAcc==true){
            FirebaseFirestore.getInstance().collection("AddToCart")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .document(idSP1)
                    .delete();
            list.remove(positon);
            int dem=demItem();
            int su=tingTongTen();
            System.out.println(dem +"  "+su);
//        System.out.println(list.get(positon).getQuantity());
//        System.out.println(Integer.parseInt( list.get(positon).getPrice().replaceAll("[^0-9]", "")));
//        System.out.println(Integer.parseInt(list.get(positon).getQuantity()));
//        notifyDataSetChanged();
            if (onDataChangeListener != null) {
                onDataChangeListener.onDataChanged(dem,su);
                System.out.println(dem);
                System.out.println(su);
            }
            notifyDataSetChanged();
        }
        if(MainActivity.loginWithGG==true){
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(v.getContext());
            System.out.println(account.getId());
            FirebaseFirestore.getInstance().collection("AddToCart")
                    .document(account.getId())
                    .collection("CurrentUser")
                    .document(idSP1)
                    .delete();
            list.remove(positon);
            int dem=demItem();
            int su=tingTongTen();
            System.out.println(dem +"  "+su);
//        System.out.println(list.get(positon).getQuantity());
//        System.out.println(Integer.parseInt( list.get(positon).getPrice().replaceAll("[^0-9]", "")));
//        System.out.println(Integer.parseInt(list.get(positon).getQuantity()));
//        notifyDataSetChanged();
            if (onDataChangeListener != null) {
                onDataChangeListener.onDataChanged(dem,su);
                System.out.println(dem);
                System.out.println(su);
            }
            notifyDataSetChanged();
        }


    }

    public static class MyAlertDialog {

        static void showYesNoDialog(Context context, String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("Yes", noListener)
                    .setPositiveButton("No", yesListener)
                    .show();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView chk;
        ImageView img;
        TextView name,color,size,price,quan,cong,tru,quanText;
        LinearLayout ln;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            chk=itemView.findViewById(R.id.checkbuy);
            img=itemView.findViewById(R.id.imgProductCart);
            name=itemView.findViewById(R.id.nameProductCart);
            color=itemView.findViewById(R.id.colorProductCart);
            size=itemView.findViewById(R.id.sizeProductCart);
            price=itemView.findViewById(R.id.priceProductCart);
            quan=itemView.findViewById(R.id.quantityProductCart);
            cong=itemView.findViewById(R.id.congquancart);
            tru=itemView.findViewById(R.id.truquancart);
            quanText=itemView.findViewById(R.id.quanSLProductCart);
            ln=itemView.findViewById(R.id.lnSL);
        }
    }
    public String[] load(int position,String idsp1){
        final String[] quantityMain = {""};
        db.collection("giay")
                .document(idsp1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document


                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        String ccc = "";
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {

                                                String imgdd = colorMapDes.get("colorDes").toString();
//                                                System.out.println("Mau:" + imgdd.toUpperCase());
//                                                System.out.println("mau p: "+list.get(position).getColor().toUpperCase());
//                                                System.out.println(imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase()));
                                                if (imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
                                                    String imgV = colorMapDes.get("img").toString();
                                                    ccc = imgdd;
//                                                    System.out.println("Mau3:" + ccc);
                                                }

                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
//                                                                System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
//                                                                System.out.println("size p: "+list.get(position).getSize());
                                                        if (sizeDesValue.toUpperCase().compareTo(list.get(position).getSize().toString().toUpperCase())==0 && ccc.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
//                                                                    System.out.println("Mau:" + ccc);
//                                                            System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
                                                            quantityMain[0] =quantityValue;
                                                            break;

                                                        }
//                                                                sizee.setSizeDes(sizeDesValue);
//                                                                sizee.setQuantity(quantityValue);
//                                                                sz.add(sizee);

                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
//                                                        modelss.setSize(sz);
                                            }


                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data


                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }

                    }
                });
        db.collection("QuanAo")
                .document(idsp1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document


                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        String ccc = "";
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {

                                                String imgdd = colorMapDes.get("colorDes").toString();
//                                                System.out.println("Mau:" + imgdd.toUpperCase());
//                                                System.out.println("mau p: "+list.get(position).getColor().toUpperCase());
//                                                System.out.println(imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase()));
                                                if (imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
                                                    String imgV = colorMapDes.get("img").toString();
                                                    ccc = imgdd;
//                                                    System.out.println("Mau3:" + ccc);
                                                }

                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
//                                                                System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
//                                                                System.out.println("size p: "+list.get(position).getSize());
                                                        if (sizeDesValue.toUpperCase().compareTo(list.get(position).getSize().toString().toUpperCase())==0 && ccc.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
//                                                                    System.out.println("Mau:" + ccc);
//                                                            System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
                                                            quantityMain[0] =quantityValue;
                                                            break;

                                                        }
//                                                                sizee.setSizeDes(sizeDesValue);
//                                                                sizee.setQuantity(quantityValue);
//                                                                sz.add(sizee);

                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
//                                                        modelss.setSize(sz);
                                            }


                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data


                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }

                    }
                });
        db.collection("ball")
                .document(idsp1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document


                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        String ccc = "";
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {

                                                String imgdd = colorMapDes.get("colorDes").toString();
//                                                System.out.println("Mau:" + imgdd.toUpperCase());
//                                                System.out.println("mau p: "+list.get(position).getColor().toUpperCase());
//                                                System.out.println(imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase()));
                                                if (imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
                                                    String imgV = colorMapDes.get("img").toString();
                                                    ccc = imgdd;
//                                                    System.out.println("Mau3:" + ccc);
                                                }

                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
//                                                                System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
//                                                                System.out.println("size p: "+list.get(position).getSize());
                                                        if (sizeDesValue.toUpperCase().compareTo(list.get(position).getSize().toString().toUpperCase())==0 && ccc.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
//                                                                    System.out.println("Mau:" + ccc);
//                                                            System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
                                                            quantityMain[0] =quantityValue;
                                                            break;

                                                        }
//                                                                sizee.setSizeDes(sizeDesValue);
//                                                                sizee.setQuantity(quantityValue);
//                                                                sz.add(sizee);

                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
//                                                        modelss.setSize(sz);
                                            }


                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data


                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }

                    }
                });
        db.collection("baoho")
                .document(idsp1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document


                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        String ccc = "";
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {

                                                String imgdd = colorMapDes.get("colorDes").toString();
//                                                System.out.println("Mau:" + imgdd.toUpperCase());
//                                                System.out.println("mau p: "+list.get(position).getColor().toUpperCase());
//                                                System.out.println(imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase()));
                                                if (imgdd.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
                                                    String imgV = colorMapDes.get("img").toString();
                                                    ccc = imgdd;
//                                                    System.out.println("Mau3:" + ccc);
                                                }

                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
//                                                                System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
//                                                                System.out.println("size p: "+list.get(position).getSize());
                                                        if (sizeDesValue.toUpperCase().compareTo(list.get(position).getSize().toString().toUpperCase())==0 && ccc.trim().toUpperCase().compareTo(list.get(position).getColor().trim().toUpperCase())==0) {
//                                                                    System.out.println("Mau:" + ccc);
//                                                            System.out.println("Size:" + sizeDesValue + " quantity2: " + quantityValue);
                                                            quantityMain[0] =quantityValue;
                                                            break;

                                                        }
//                                                                sizee.setSizeDes(sizeDesValue);
//                                                                sizee.setQuantity(quantityValue);
//                                                                sz.add(sizee);

                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
//                                                        modelss.setSize(sz);
                                            }


                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data


                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }

                    }
                });
        System.out.println(quantityMain[0]);
        return quantityMain;
    }
}