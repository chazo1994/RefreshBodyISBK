package com.example.asus.refreshbody.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.refreshbody.R;
import com.example.asus.refreshbody.adapter.CupChooseAdapter;
import com.example.asus.refreshbody.adapter.CupImageAdapter;
import com.example.asus.refreshbody.database.DBContext;
import com.example.asus.refreshbody.database.model.CupChooseItem;
import com.example.asus.refreshbody.intef.ClickListener;
import com.example.asus.refreshbody.intef.CupChooseListener;
import com.example.asus.refreshbody.database.model.DrinkIntakeItem;
import com.example.asus.refreshbody.model.CupImage;

import java.util.ArrayList;

/**
 * Created by Asus on 10/12/2016.
 */

public class FragmentChooseCup  extends Fragment implements View.OnClickListener,ClickListener{
    private RecyclerView recyclerViewCupChoose;
    private TextView tvCustomCup;

    private ArrayList<CupChooseItem> arrayListDrinkIntakeItems;
    private ArrayList<CupImage> arrayDefault;

    private CupChooseAdapter cupChooseAdapter;
    private CupImageAdapter cupImageAdapter;

    private boolean isAddNewCup;

    private DBContext dbContext;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_choose_cup,container,false);
        setupView(layout);
        setupDefaultData();

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapterForRecyclerview();
    }

    private void setAdapterForRecyclerview() {
        cupChooseAdapter=new CupChooseAdapter(arrayListDrinkIntakeItems,getActivity());
        cupChooseAdapter.setListener(new CupChooseListener() {
            @Override
            public void clickOnItem(int position) {
                Toast.makeText(getActivity(),"Click on cup choose "+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickEdit(int position) {
                Toast.makeText(getActivity(),"Click on cup edit "+position,Toast.LENGTH_SHORT).show();
                openDialogEditCup(arrayListDrinkIntakeItems.get(position));
            }
        });
        recyclerViewCupChoose.setAdapter(cupChooseAdapter);
        recyclerViewCupChoose.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void openDialogEditCup(final CupChooseItem drinkIntakeItem) {
        isAddNewCup=false;
        if(drinkIntakeItem.getAmountCup()==0)
            isAddNewCup=true;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_custom_choose_cup,null);
        dialogBuilder.setView(layout);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText editTextNameDrink= (EditText) layout.findViewById(R.id.edittext_choose_cup_name);
        final EditText editTextDrinkAmount=(EditText)layout.findViewById(R.id.edittext_choose_cup_amount);
        TextView tvCancel=(TextView)layout.findViewById(R.id.tv_cancel_choose_cup);
        TextView tvOk=(TextView)layout.findViewById(R.id.tv_ok_choose_cup);
        RecyclerView recyclerViewCupImage= (RecyclerView) layout.findViewById(R.id.recyclerview_cup_image);
        setDefaultAdapterForRecyclerviewCup(recyclerViewCupImage);

        editTextDrinkAmount.setText(drinkIntakeItem.getAmountCup()+"");
        editTextNameDrink.setText(drinkIntakeItem.getNameCup());

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(editTextDrinkAmount.getText() + "")==0){
                    Toast.makeText(getActivity(),getResources().getString(R.string.validate_add_cup),Toast.LENGTH_SHORT).show();
                } else if(!isAddNewCup) {
                    dbContext.updateCupChooseItem(drinkIntakeItem,drinkIntakeItem.getSymbol(),editTextNameDrink.getText() + "",
                            Integer.parseInt(editTextDrinkAmount.getText() + ""));
                    cupChooseAdapter.notifyDataSetChanged();
                }else {
                    drinkIntakeItem.setAmountCup(Integer.parseInt(editTextDrinkAmount.getText() + ""));
                    drinkIntakeItem.setNameCup(editTextNameDrink.getText() + "");
                    drinkIntakeItem.setIdCupChoose(arrayListDrinkIntakeItems.size()+1+"");
                    arrayListDrinkIntakeItems.add(drinkIntakeItem);
                    cupChooseAdapter.notifyDataSetChanged();
                    dbContext.createCupChooseItem(drinkIntakeItem);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void setDefaultAdapterForRecyclerviewCup(RecyclerView recyclerViewCupImage) {
        arrayDefault=new ArrayList<CupImage>();
        CupImage water100=new CupImage(R.drawable.cup_one,false);
        arrayDefault.add(water100);
        CupImage water200=new CupImage(R.drawable.cup_one,false);
        arrayDefault.add(water200);
        CupImage water300=new CupImage(R.drawable.cup_one,false);
        arrayDefault.add(water300);
        cupImageAdapter=new CupImageAdapter(arrayDefault,getActivity());
        recyclerViewCupImage.setAdapter(cupImageAdapter);
        cupImageAdapter.setListener(this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCupImage.setLayoutManager(layoutManager);

    }

    private void setupDefaultData() {
        dbContext=DBContext.getInst();
        arrayListDrinkIntakeItems=new ArrayList<CupChooseItem>();
        arrayListDrinkIntakeItems.addAll(dbContext.getAllCupChooseItem());

    }

    private void setupView(View layout) {
        recyclerViewCupChoose=(RecyclerView)layout.findViewById(R.id.recyclerview_cup);
        tvCustomCup=(TextView)layout.findViewById(R.id.tv_custom_cup_choose);

        tvCustomCup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_custom_cup_choose:
                openDialogEditCup(new CupChooseItem("",R.drawable.cup_one,"",0));
                break;
        }
    }

    @Override
    public void onClick(View view, int position) {
        for(CupImage cupImage:arrayDefault)
            cupImage.setChoose(false);
        arrayDefault.get(position).setChoose(true);
        cupImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
