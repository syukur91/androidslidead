package com.slidead.app.slidead.helpers.jobs;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by syukur on 25/02/18.
 */
class JobCreatorHelper implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case JobSchedulerHelper.TAG:
                return new JobSchedulerHelper();
            default:
                return null;
        }
    }
}