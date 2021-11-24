package com.enrique.apprater;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;
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
        Button negativeButton = new Button(context);
        negativeButton.setText(context.getString(ResourceTable.String_no));
        negativeButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                builder.destroy();
            }
        });
        negativeButton.setTextSize(40);
        negativeButton.setTextColor(Color.BLUE);
        negativeButton.setMarginBottom(50);

        builder.setNegativeButton(negativeButton);
        Button neutralButton = new Button(context);
        neutralButton.setText(context.getString(ResourceTable.String_dontask));
        neutralButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (editor != null) {
                    editor.putBoolean(DONT_SHOW, true);
                    editor.flushSync();
                }
                builder.destroy();
            }
        });
        neutralButton.setTextSize(40);
        neutralButton.setTextColor(Color.BLUE);
        neutralButton.setMarginBottom(50);
        builder.setNeutralButton(neutralButton);

        Button positiveButton = new Button(context);
        positiveButton.setText(context.getString(ResourceTable.String_rate).toUpperCase());
        positiveButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withUri(Uri.parse(URI + appPackageName))
                        .build();
                intent.setOperation(operation);
                context.startAbility(intent, AbilityInfo.AbilityType.WEB.ordinal());
                builder.destroy();
            }
        });
        positiveButton.setTextSize(40);
        positiveButton.setTextColor(Color.BLUE);
        positiveButton.setMarginBottom(50);
        builder.setPositiveButton(positiveButton);

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