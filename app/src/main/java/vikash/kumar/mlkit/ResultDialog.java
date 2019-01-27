package vikash.kumar.mlkit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ResultDialog extends DialogFragment {
    private Button okButton;
    private TextView resultTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_result,container,false);

        String resultText ="";
        okButton = view.findViewById(R.id.result_ok_button);
        resultTv = view.findViewById(R.id.result_text_view);

        Bundle bundle = getArguments();
        resultText = bundle.getString(FaceDetection.RESULT_TEXT);
        resultTv.setText(resultText);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
