package eu.geekhome.asymptote.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.ToastService;

public class HelpActionBarViewModel {
    private final Context _context;
    private final ToastService _toastService;
    private final CloudUserService _cloudUserService;

    public HelpActionBarViewModel(Activity activity, ToastService toastService, CloudUserService cloudUserService) {
        _context = activity;
        _toastService = toastService;
        _cloudUserService = cloudUserService;
    }

    public void onHelp() {
        _cloudUserService.refreshUser(new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                prepareEmail(user);
            }

            private void prepareEmail(CloudUser user) {
                String subject = user != null && user.getId() != null ? _context.getString(R.string.ask_for_support_with_uid, user.getId()) :
                        _context.getString(R.string.ask_for_support);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{_context.getString(R.string.email_support)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, _context.getString(R.string.support_problem_configuring));
                try {
                    _context.startActivity(Intent.createChooser(emailIntent, _context.getString(R.string.send_email)));
                } catch (android.content.ActivityNotFoundException ex) {
                    _toastService.makeToast(_context.getString(R.string.no_email_client_installed), true);
                }
            }

            @Override
            public void failure(CloudException exception) {
                String message = _context.getString(R.string.support_email_error, exception,
                        _context.getString(R.string.ask_for_support));
                _toastService.makeToast(message, true);
            }
        });

    }
}
