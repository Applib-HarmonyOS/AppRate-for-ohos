package com.enrique.apprater;


import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.window.dialog.IDialog;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.utils.net.Uri;
import com.hmos.compat.app.AlertDialog;

/**
 * AppRater Library.
 */
public class AppRater {

    private  String apptitlE = "YOUR-APPLICATION-TITLE";

    private String appPackageName = "YOUR-PACKAGE-NAME";

    private static final String APP_RATER = "apprater";

    private static final String DONT_SHOW = "dontshowagain";

    private static final String LAUNCH_COUNT = "launch_count";

    private static final String FIRST_LAUNCH = "date_firstlaunch";

    private int daysUntilPrompt = 3;

    private int launchesUntilPrompt = 7;

    private Context contexT;


    public AppRater(Context context) {
        contexT = context;
        appPackageName = context.getBundleName();
    }

    /** Init method.
     *
     *
     *
     * @return Apprater object.
     * @noinspection checkstyle:Indentation, checkstyle:Indentation
     */
    public AppRater init() {
        DatabaseHelper databaseHelper = new DatabaseHelper(contexT);


        Preferences prefs = databaseHelper.getPreferences(APP_RATER);
        if (prefs.getBoolean(DONT_SHOW, false)) {
            return null;
        }

        Preferences editor = prefs;
        long launchcount = prefs.getLong(LAUNCH_COUNT, 0) + 1;
        editor.putLong(LAUNCH_COUNT, launchcount);
        Long datefirstLaunch = prefs.getLong(FIRST_LAUNCH, 0);
        if (datefirstLaunch == 0) {
            datefirstLaunch = System.currentTimeMillis();
            editor.putLong(FIRST_LAUNCH, datefirstLaunch);
        }
        if ((launchcount >= launchesUntilPrompt)
                && (System.currentTimeMillis() >= datefirstLaunch + (daysUntilPrompt * 24 * 60 * 60 * 1000))) {
            showRateDialog(contexT, editor);

        }
        editor.flushSync();
        return this;
    }

    /**  ShowrateDialog.
     *
     *
     * @param mcontext
     *
     * @param editor Member variable of preferences
     * @return   AppraterObject.
     */

    public AppRater showRateDialog(final Context mcontext, final Preferences editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setMessage(mcontext.getString(ResourceTable.String_dialog_text, apptitlE));
        builder.setTitle(mcontext.getString(ResourceTable.String_rate) + " " + apptitlE);
        builder.setNegativeButton(mcontext.getString(ResourceTable.String_no), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                dialog.destroy();
            }
        });

        builder.setNeutralButton(mcontext.getString(ResourceTable.String_dontask), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                if (editor != null) {
                    editor.putBoolean(DONT_SHOW, true);
                    editor.flushSync();
                }
                dialog.destroy();
            }
        });

        builder.setPositiveButton(mcontext.getString(ResourceTable.String_rate), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withUri(Uri.parse("https://appgallery.cloud.huawei.com/appDetail?pkgName=" + appPackageName))
                        .build();
                intent.setOperation(operation);
                mcontext.startAbility(intent, AbilityInfo.AbilityType.WEB.ordinal());
                dialog.destroy();
            }
        });

        IDialog dialog = builder.create();
        dialog.show();
        return this;
    }

    public AppRater setMinDays(int minDays) {
        daysUntilPrompt = minDays;
        return this;
    }

    public AppRater setMinLaunches(int minLaunches) {
        launchesUntilPrompt = minLaunches;
        return this;
    }

    public AppRater setAppTitle(String appTitle) {
        apptitlE = appTitle;
        return this;
    }
}
