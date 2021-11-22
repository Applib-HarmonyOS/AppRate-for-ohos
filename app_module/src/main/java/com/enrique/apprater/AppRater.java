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
    private static final String APP_RATER = "apprater";
    private static final String DONT_SHOW = "dontshowagain";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String FIRST_LAUNCH = "date_firstlaunch";
    private static final String URI = "https://appgallery.cloud.huawei.com/appDetail?pkgName=";
    private final Context mContext;
    private int daysUntilPrompt = 3;
    private int launchesUntilPrompt = 7;
    private String appTitle = "YOUR-APPLICATION-TITLE";
    private String appPackageName = "YOUR-PACKAGE-NAME";

    public AppRater(Context context) {
        mContext = context;
        appPackageName = context.getBundleName();
    }

    /**
     * Init method.
     *
     * @return Apprater object.
     * @noinspection checkstyle:Indentation, checkstyle:Indentation
     */
    public AppRater init() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        Preferences prefs = databaseHelper.getPreferences(APP_RATER);
        if (prefs.getBoolean(DONT_SHOW, false)) {
            return null;
        }
        long launchcount = prefs.getLong(LAUNCH_COUNT, 0) + 1;
        prefs.putLong(LAUNCH_COUNT, launchcount);
        Long datefirstLaunch = prefs.getLong(FIRST_LAUNCH, 0);
        if (datefirstLaunch == 0) {
            datefirstLaunch = System.currentTimeMillis();
            prefs.putLong(FIRST_LAUNCH, datefirstLaunch);
        }
        if ((launchcount >= launchesUntilPrompt)
                && (System.currentTimeMillis() >= datefirstLaunch + (daysUntilPrompt * 24 * 60 * 60 * 1000))) {
            showRateDialog(mContext, prefs);
        }
        prefs.flushSync();
        return this;
    }

    /**
     * ShowrateDialog.
     *
     * @param context context
     * @param editor Member variable of preferences
     * @return   AppraterObject.
     */
    public AppRater showRateDialog(final Context context, final Preferences editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(ResourceTable.String_dialog_text, appTitle));
        builder.setTitle(context.getString(ResourceTable.String_rate) + " " + appTitle);
        builder.setNegativeButton(context.getString(ResourceTable.String_no), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                dialog.destroy();
            }
        });
        builder.setNeutralButton(context.getString(ResourceTable.String_dontask), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                if (editor != null) {
                    editor.putBoolean(DONT_SHOW, true);
                    editor.flushSync();
                }
                dialog.destroy();
            }
        });
        builder.setPositiveButton(context.getString(ResourceTable.String_rate), new IDialog.ClickedListener() {
            @Override
            public void onClick(IDialog dialog, int id) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withUri(Uri.parse(URI + appPackageName))
                        .build();
                intent.setOperation(operation);
                context.startAbility(intent, AbilityInfo.AbilityType.WEB.ordinal());
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
        this.appTitle = appTitle;
        return this;
    }
}