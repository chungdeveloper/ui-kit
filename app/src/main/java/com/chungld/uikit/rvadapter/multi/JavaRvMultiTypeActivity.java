package com.chungld.uikit.rvadapter.multi;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.chungld.uikit.R;
import com.chungld.uikit.databinding.ItemFiveBinding;
import com.chungld.uikit.databinding.ItemFourBinding;
import com.chungld.uikit.databinding.ItemOneBinding;
import com.chungld.uikit.databinding.ItemThreeBinding;
import com.chungld.uikit.databinding.ItemTwoBinding;
import com.chungld.uikit.rvadapter.holder.FiveViewHolder;
import com.chungld.uikit.rvadapter.holder.FourViewHolder;
import com.chungld.uikit.rvadapter.holder.OneViewHolder;
import com.chungld.uikit.rvadapter.holder.ThreeViewHolder;
import com.chungld.uikit.rvadapter.holder.TwoViewHolder;
import com.chungld.uikit.rvadapter.model.Five;
import com.chungld.uikit.rvadapter.model.Four;
import com.chungld.uikit.rvadapter.model.One;
import com.chungld.uikit.rvadapter.model.Three;
import com.chungld.uikit.rvadapter.model.Two;
import com.chungld.uipack.rvadapter.RvMultiTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaRvMultiTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        ArrayList<Object> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            data.add(new One(i, "Item number " + i + " of One"));
        }

        for (int i = 0; i < 20; i++) {
            data.add(new Two("Item number " + i + " of Two", i));
        }

        for (int i = 0; i < 20; i++) {
            data.add(new Three(i, "Item number " + i + " of Three"));
        }

        for (int i = 0; i < 20; i++) {
            data.add(new Four("Item number " + i + " of Four", i));
        }

        for (int i = 0; i < 20; i++) {
            data.add(new Five("Item number " + i + " of Five", i));
        }

        Collections.shuffle(data);

        RvMultiTypeAdapter<?> adapter = new RvMultiTypeAdapter.Builder<>(data)
                .register(1, R.layout.item_one, One.class,
                        viewDataBinding -> new OneViewHolder((ItemOneBinding) viewDataBinding))
                .register(2, R.layout.item_two, Two.class,
                        viewDataBinding -> new TwoViewHolder((ItemTwoBinding) viewDataBinding))
                .register(3, R.layout.item_three, Three.class,
                        viewDataBinding -> new ThreeViewHolder((ItemThreeBinding) viewDataBinding))
                .register(4, R.layout.item_four, Four.class,
                        viewDataBinding -> new FourViewHolder((ItemFourBinding) viewDataBinding))
                .register(5, R.layout.item_five, Five.class,
                        viewDataBinding -> new FiveViewHolder((ItemFiveBinding) viewDataBinding))
                .build();

        ((RecyclerView) findViewById(R.id.rvColor)).setAdapter(adapter);

        findViewById(R.id.btnRemoveOne).setOnClickListener(v -> adapter.removeAllItemType(One.class));

        findViewById(R.id.btnIndexFirstOne).setOnClickListener(v -> {
            int index = adapter.findFirstIndexOfItemType(One.class);
            Toast.makeText(this, "First index : " + index, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnReplaceTwo).setOnClickListener(v -> {
            ArrayList<Two> typ2 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                typ2.add(new Two("" + i, i * 10));
            }
            adapter.replaceDataType(typ2, Two.class);
        });

        findViewById(R.id.btnAppendFive).setOnClickListener(v -> {
            ArrayList<Object> type5 = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                type5.add(new Five("" + i, i * 10));
            }
            adapter.addData((List) type5);
        });
    }


}
