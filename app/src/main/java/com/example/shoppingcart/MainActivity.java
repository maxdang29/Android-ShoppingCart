package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.shoppingcart.Cart.CartActivity;
import com.example.shoppingcart.Image.Image;
import com.example.shoppingcart.database.AppDatabase;
import com.example.shoppingcart.database.Cart;
import com.example.shoppingcart.database.Product;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnItemClicked {
    AppDatabase db;
    RecyclerView recyclerViewProduct;
    ProductAdapter productAdapter;

    private static List<Product> Products = new ArrayList<>();

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-shoppingCart").build();

        insertProduct();


        recyclerViewProduct = findViewById(R.id.recyclerView);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));

    }

    public void onResume() {
        super.onResume();
        getAndShowProducts();
    }
    private void insertProduct(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Product iphone = new Product("Iphone6", 12, 100000, imageToBase64String(R.drawable.a));
                Product samsung = new Product("Samsung", 12, 100000, imageToBase64String(R.drawable.b));

                Product Iphone5 = new Product("Iphone5", 12, 100000, imageToBase64String(R.drawable.c));
                Product Iphone11 = new Product("Iphone11", 12, 100000, imageToBase64String(R.drawable.d));


                //db.ProductDao().insertAll(samsung, iphone, Iphone5, Iphone11);
                // db.ProductDao().insertAll( iphone);
                //db.ProductDao().deleteAll();
                return null;
            }
        }.execute();

    }
    private void getAndShowProducts() {
        new AsyncTask<Void, Void, List<Product>>() {
            @Override
            protected List<Product> doInBackground(Void... voids) {
                Products = db.ProductDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productAdapter = new ProductAdapter(this, Products);
                        productAdapter.setOnClick(MainActivity.this);
                        recyclerViewProduct.setAdapter(productAdapter);


                    }
                });
                return null;
            }
        }.execute();

    }

    public String imageToBase64String(int c) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), c);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }


    @Override
    public void onClickAddToCart(final int position) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Cart newCart = new Cart(Products.get(position).product_name, 1, Products.get(position).price, Products.get(position).product_image);
                db.CartDao().insertAllCart(newCart);


                //db.ProductDao().deleteAll();
                return null;
            }
        }.execute();
        Toast.makeText(getBaseContext(), "Add to cart successfully",
                Toast.LENGTH_SHORT).show();
    }

    public void showCarts(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);

    }
}
