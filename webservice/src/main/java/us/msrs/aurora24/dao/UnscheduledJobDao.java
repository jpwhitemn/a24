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
import us.msrs.aurora24.model.UnscheduledJob;

@Repository
public class UnscheduledJobDao {

	private static List<UnscheduledJob> jobs;
	private List<String> temp = new ArrayList<String>();
	private List<String> temp2 = new ArrayList<String>();

	public List<UnscheduledJob> getJobs() {
		if (jobs == null) {
			jobs = new ArrayList<UnscheduledJob>();
			temp2.add("param1");
			temp2.add("param2");
			jobs.addAll(Arrays.asList(
					new UnscheduledJob(
							new JobDefinition("BeneficiaryChangeLetterJob", UUID.randomUUID(), temp, "Jim", true),
							UUID.randomUUID(), temp, JobState.Submitted, "Jim", OffsetDateTime.now(),
							OffsetDateTime.now()),
					new UnscheduledJob(
							new JobDefinition("SendRefundPayments", UUID.randomUUID(), temp2, "George", true),
							UUID.randomUUID(), temp, JobState.Running, "George", OffsetDateTime.now(),
							OffsetDateTime.now()),
					new UnscheduledJob(new JobDefinition("UpdateProjectionsJob", UUID.randomUUID(), temp, "Jim", true),
							UUID.randomUUID(), temp, JobState.Running, "Jim", OffsetDateTime.now(),
							OffsetDateTime.now())));
		}
		return jobs;
	}

	public UnscheduledJob getById(String id) {
		return getJobs().stream()
				.filter(job -> job.getId().equals(UUID.fromString(id)))
				.findFirst()
				.orElse(null);
	}

	public List<UnscheduledJob> getByState(JobState jobState) {
		return getJobs().stream()
				.filter(job -> job.getJobState().equals(jobState)).toList();
	}

	public UnscheduledJob createUnscheduledJob(JobDefinition jobDefinition, List<String> parameters, JobState jobState,
			String submitter) {
		if (jobs == null) {
			jobs = new ArrayList<UnscheduledJob>();
		}
		// #TODO - what should start time be when creating a job
		UnscheduledJob job = new UnscheduledJob(jobDefinition, UUID.randomUUID(), parameters, jobState, submitter,
				OffsetDateTime.now(), OffsetDateTime.now());
		jobs.add(job);
		return job;
	}

	public boolean deleteUnscheduledJob(String id) {
		UnscheduledJob unscheduledJob = getById(id);
		if (unscheduledJob != null) {
			jobs.remove(unscheduledJob);
			return true;
		}
		return false;
	}

	public UnscheduledJob updateUnscheduledJob(String id, JobDefinition jobDefinition, List<String> parameters,
			JobState jobState, String submitter)
			throws NotFoundByID {
		UnscheduledJob unscheduledJob = getById(id);
		if (unscheduledJob != null) {
			// #TODO what should start time be?
			UnscheduledJob updatedUnscheduledJob = new UnscheduledJob(jobDefinition, UUID.fromString(id), parameters,
					jobState, submitter, OffsetDateTime.now(), OffsetDateTime.now());
			Collections.replaceAll(jobs, unscheduledJob, updatedUnscheduledJob);
			return updatedUnscheduledJob;
		}
		throw new NotFoundByID("UnscheduledJob not found with id: " + id);
	}
	
}
