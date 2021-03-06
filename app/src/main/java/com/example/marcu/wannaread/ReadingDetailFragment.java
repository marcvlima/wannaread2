package com.example.marcu.wannaread;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.wannaread.database.DataBaseControl;
import com.example.marcu.wannaread.database.Reading;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ReadingDetailFragment extends Fragment {

    private static String READING_KEY = "readingKey";

    protected Reading reading;
    protected String bookName, author, source, priority, genre, pagesCurrent, pagesTotal;
    protected int pagesCurrentInt, pagesTotalInt;

    protected static EditText etPagesCurrent, etPagesTotal;
    protected static TextView tvBookName, tvAuthor, tvSource, tvPriority, tvGenre, tvDate;

    public ReadingDetailFragment() {
    }

    public static ReadingDetailFragment newInstance(Reading reading) {
        ReadingDetailFragment fragment = new ReadingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(READING_KEY, reading);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.reading = (Reading) getArguments().getParcelable(READING_KEY);

        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_reading_detail, container, false);

        // Finds the elements
        tvBookName = (TextView)view.findViewById(R.id.tvBookName);
        tvAuthor = (TextView)view.findViewById(R.id.tvAuthor);
        tvPriority = (TextView)view.findViewById(R.id.tvPriority);
        tvGenre = (TextView)view.findViewById(R.id.tvGenre);
        tvSource = (TextView)view.findViewById(R.id.tvSource);
        //tvDate = (TextView)view.findViewById(R.id.tvDate);
        etPagesCurrent = (EditText) view.findViewById(R.id.etPagesCurrent);
        etPagesTotal = (EditText) view.findViewById(R.id.etPagesTotal);

        tvBookName.setText(reading.getReadingName());
        tvAuthor.setText(reading.getReadingAuthor());
        tvPriority.setText(reading.getReadingPriorityName());
        tvGenre.setText(reading.getReadingGenre());
        tvSource.setText(reading.getReadingSource());
        //tvDate.setText(reading.getReadingDate());

        pagesCurrentInt = reading.getReadingPagesCurrent();
        pagesTotalInt = reading.getReadingPagesNumber();

        if(pagesCurrentInt > 0)
            etPagesCurrent.setText(String.valueOf(pagesCurrentInt));

        if(pagesTotalInt > 0)
            etPagesTotal.setText(String.valueOf(pagesTotalInt));

        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DataBaseControl crud = new DataBaseControl(getContext());

                pagesCurrent = etPagesCurrent.getText().toString();
                pagesTotal = etPagesTotal.getText().toString();

                int pagesCurrentInt = 0;
                int pagesTotalInt = 0;

                if(pagesCurrent.length() > 0)
                    pagesCurrentInt = Integer.parseInt(pagesCurrent);

                if(pagesTotal.length() >0)
                    pagesTotalInt = Integer.parseInt(pagesTotal);

                String resultadoMsg = "";
                Boolean valid = true;

                // Valida o preenchimento
                if(pagesTotalInt == 0) {
                    resultadoMsg = getResources().getString(R.string.error_numero_pagina_zerado);
                    valid = false;
                }else if(pagesCurrentInt > pagesTotalInt){
                    resultadoMsg = getResources().getString(R.string.error_pagina_atual_menor);
                    valid = false;
                }

                if(valid) {
                    Boolean result = crud.updateReading(reading.getId(), pagesTotalInt, pagesCurrentInt);
                    resultadoMsg = result ? getResources().getString(R.string.msg_update_success) : getResources().getString(R.string.msg_update_failure);
                }

                Toast.makeText(getContext(), resultadoMsg, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        } );

        return view;
    }


}
