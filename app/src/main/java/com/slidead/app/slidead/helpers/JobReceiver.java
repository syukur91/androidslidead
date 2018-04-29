package com.slidead.app.slidead.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

/**
 * Created by syukur on 25/02/18.
 */
public final class JobReceiver extends JobCreator.AddJobCreatorReceiver {
    @Override
    protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
        manager.addJobCreator(new JobCreatorHelper());
        manager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true);


    }
}