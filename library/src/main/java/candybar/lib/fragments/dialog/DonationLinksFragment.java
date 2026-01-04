package candybar.lib.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import candybar.lib.R;
import candybar.lib.adapters.dialog.OtherAppsAdapter;
import candybar.lib.applications.CandyBarApplication;

public class DonationLinksFragment extends DialogFragment {

    private static final String TAG = "candybar.dialog.donationlinks";

    private static DonationLinksFragment newInstance() {
        return new DonationLinksFragment();
    }

    public static void showDonationLinksDialog(@NonNull FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        try {
            DialogFragment dialog = DonationLinksFragment.newInstance();
            dialog.show(ft, TAG);
        } catch (IllegalStateException | IllegalArgumentException ignored) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(requireActivity(), R.layout.fragment_other_apps, null);
        ListView listView = view.findViewById(R.id.listview);

        Dialog dialog = new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.donate)
                .setView(view)
                .setPositiveButton(R.string.close, null)
                .create();

        List<CandyBarApplication.DonationLink> donationLinks = CandyBarApplication.getConfiguration().getDonationLinks();
        if (donationLinks != null) {
            listView.setAdapter(new OtherAppsAdapter(requireActivity(), donationLinks));
        } else {
            dismiss();
        }

        return dialog;
    }
}
