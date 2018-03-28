package com.example.myapplication;

import android.view.View;
import android.widget.TextView;
import com.example.myapplication.Info.CompanyInfo;
import java.util.List;
import io.reactivex.subjects.PublishSubject;

/**
 *
 */

 class CompanyHolder extends BaseHolder<CompanyInfo> {


    private TextView mCompanyName;

    public CompanyHolder(View itemView) {
        super(itemView);
        mCompanyName =  itemView.findViewById(R.id.company_name);
    }

    @Override
    public void bind(List<CompanyInfo> data, int position, IParamContainer container, final PublishSubject<CompanyInfo> itemClick) {
        final CompanyInfo companyInfo = data.get(position);
        mCompanyName.setText(companyInfo.getComName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onNext(companyInfo);
            }
        });

    }
}
