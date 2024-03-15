package us.msrs.aurora24.dao;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.JobDefinition;
import us.msrs.aurora24.model.JobState;
import us.msrs.aurora24.model.Schedule;
import us.msrs.aurora24.model.ScheduledJob;

@Repository
public class ScheduledJobDao {

	private static List<ScheduledJob> jobs;
	private List<String> temp = new ArrayList<String>();
	private List<String> temp2 = new ArrayList<String>();

	public List<ScheduledJob> getJobs() {
		if (jobs == null) {
			jobs = new ArrayList<ScheduledJob>();
			temp2.add("param1");
			temp2.add("param2");
			jobs.addAll(Arrays.asList(
					new ScheduledJob(
							new JobDefinition("BeneficiaryChangeLetterJob", UUID.randomUUID(), temp, "Jim", true),
							UUID.randomUUID(), temp, JobState.Submitted, "Jim", OffsetDateTime.now(),
							new Schedule("everyNight", UUID.randomUUID(), "Jim", true, "0 0 1 * * ?")),
					new ScheduledJob(new JobDefinition("SendRefundPayments", UUID.randomUUID(), temp2, "George", true),
							UUID.randomUUID(), temp, JobState.Running, "George", OffsetDateTime.now(),
							new Schedule("everyMonNoon", UUID.randomUUID(), "Jim", true, "0 0 12 ? * MON")),
					new ScheduledJob(new JobDefinition("UpdateProjectionsJob", UUID.randomUUID(), temp, "Jim", true),
							UUID.randomUUID(), temp, JobState.Running, "Jim", OffsetDateTime.now(),
							new Schedule("onceYear", UUID.randomUUID(), "Jim", true, "0 0 21 31 DEC ? *"))));
		}
		return jobs;
	}

	public ScheduledJob getById(String id) {
		return getJobs().stream()
				.filter(job -> job.getId().equals(UUID.fromString(id)))
				.findFirst()
				.orElse(null);
	}

	public List<ScheduledJob> getByState(JobState jobState) {
		return getJobs().stream()
				.filter(job -> job.getJobState().equals(jobState)).toList();
	}

	public ScheduledJob createScheduledJob(JobDefinition jobDefinition, List<String> parameters, JobState jobState,
			String submitter, Schedule schedule) {
		if (jobs == null) {
			jobs = new ArrayList<ScheduledJob>();
		}
		ScheduledJob job = new ScheduledJob(jobDefinition, UUID.randomUUID(), parameters, jobState, submitter,
				OffsetDateTime.now(), schedule);
		jobs.add(job);
		return job;
	}

	public boolean deleteScheduledJob(String id) {
		ScheduledJob scheduledJob = getById(id);
		if (scheduledJob != null) {
			jobs.remove(scheduledJob);
			return true;
		}
		return false;
	}

	public ScheduledJob updateScheduledJob(String id, JobDefinition jobDefinition, List<String> parameters,
			JobState jobState, String submitter, Schedule schedule)
			throws NotFoundByID {
		ScheduledJob scheduledJob = getById(id);
		if (scheduledJob != null) {
			ScheduledJob updatedScheduledJob = new ScheduledJob(jobDefinition, UUID.fromString(id), parameters,
					jobState,
					submitter, OffsetDateTime.now(), schedule);
			Collections.replaceAll(jobs, scheduledJob, updatedScheduledJob);
			return updatedScheduledJob;
		}
		throw new NotFoundByID("ScheduledJob not found with id: " + id);
	}
}
